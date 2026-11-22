package dating.agilis.ads;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dating.agilis.ads.exception.AgilisException;
import dating.agilis.ads.exception.ApiException;
import dating.agilis.ads.exception.AuthenticationException;
import dating.agilis.ads.exception.ConflictException;
import dating.agilis.ads.exception.ConnectionException;
import dating.agilis.ads.exception.InvalidRequestException;
import dating.agilis.ads.exception.NotFoundException;
import dating.agilis.ads.exception.PermissionException;
import dating.agilis.ads.exception.RateLimitException;
import dating.agilis.ads.http.Transport;
import dating.agilis.ads.http.TransportRequest;
import dating.agilis.ads.http.TransportResponse;
import dating.agilis.ads.internal.Json;
import dating.agilis.ads.model.ApiResponse;
import dating.agilis.ads.model.Page;
import dating.agilis.ads.model.ResponseInfo;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleSupplier;
import java.util.regex.Pattern;

/** Sends API requests: headers, retries, and turning responses into objects or errors. */
final class Requester {

  /** Waits between attempts. Replaced in tests. */
  @FunctionalInterface
  interface Sleeper {
    void sleep(Duration duration) throws InterruptedException;
  }

  private static final Pattern KEY_PATTERN = Pattern.compile("pk_(test|live)_[A-Za-z0-9_\\-]+");
  private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};
  private static final double MAX_BACKOFF_SECONDS = 8.0;

  private final String apiKey;
  private final String baseUrl;
  private final Duration timeout;
  private final int maxRetries;
  private final Transport transport;
  private final Sleeper sleeper;
  private final DoubleSupplier random;
  private final String userAgent;
  private final ObjectMapper mapper = Json.mapper();

  Requester(
      String apiKey,
      String baseUrl,
      Duration timeout,
      int maxRetries,
      Transport transport,
      Sleeper sleeper,
      DoubleSupplier random,
      String userAgent) {
    this.apiKey = apiKey;
    this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    this.timeout = timeout;
    this.maxRetries = maxRetries;
    this.transport = transport;
    this.sleeper = sleeper;
    this.random = random;
    this.userAgent = userAgent;
  }

  <T extends ApiResponse> T execute(ApiRequest request, Class<T> type) {
    return parse(send(request), mapper.constructType(type));
  }

  <T> Page<T> executePage(ApiRequest request, Class<T> itemType) {
    JavaType type = mapper.getTypeFactory().constructParametricType(Page.class, itemType);
    return parse(send(request), type);
  }

  TransportResponse send(ApiRequest request) {
    Map<String, String> headers = new LinkedHashMap<>();
    headers.put("x-api-key", apiKey);
    headers.put("Accept", "application/json");
    headers.put("User-Agent", userAgent);
    if (request.contentType != null) {
      headers.put("Content-Type", request.contentType);
    }
    headers.putAll(request.headers);
    TransportRequest transportRequest =
        new TransportRequest(request.method, uri(request), headers, request.body, timeout);
    boolean retryServerErrors =
        "GET".equals(request.method)
            || "DELETE".equals(request.method)
            || ("POST".equals(request.method) && request.hasIdempotencyKey());

    for (int attempt = 0; ; attempt++) {
      TransportResponse response;
      try {
        response = transport.send(transportRequest);
      } catch (IOException e) {
        if (attempt < maxRetries) {
          pause(backoff(attempt));
          continue;
        }
        throw new ConnectionException(
            redact(
                "Could not reach the Agilis API ("
                    + e.getClass().getSimpleName()
                    + (e.getMessage() == null ? "" : ": " + e.getMessage())
                    + ")"),
            e);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new ConnectionException("The request was interrupted", e);
      }
      int status = response.statusCode();
      boolean retry =
          status == 429
              || (retryServerErrors
                  && (status == 500 || status == 502 || status == 503 || status == 504));
      if (retry && attempt < maxRetries) {
        Duration wait = retryAfter(response);
        pause(wait != null ? wait : backoff(attempt));
        continue;
      }
      return response;
    }
  }

  private void pause(Duration duration) {
    try {
      sleeper.sleep(duration);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ConnectionException("The request was interrupted", null);
    }
  }

  Duration backoff(int attempt) {
    double seconds = 0.5 * Math.pow(2, attempt);
    double jitter = 1.0 + (random.getAsDouble() * 0.5 - 0.25);
    seconds = Math.min(seconds * jitter, MAX_BACKOFF_SECONDS);
    return Duration.ofMillis(Math.round(seconds * 1000));
  }

  static Duration retryAfter(TransportResponse response) {
    String value = response.header("Retry-After");
    if (value == null) {
      return null;
    }
    try {
      double seconds = Double.parseDouble(value.trim());
      if (seconds < 0 || Double.isNaN(seconds) || Double.isInfinite(seconds)) {
        return null;
      }
      return Duration.ofMillis(Math.round(seconds * 1000));
    } catch (NumberFormatException e) {
      return null;
    }
  }

  private URI uri(ApiRequest request) {
    StringBuilder sb = new StringBuilder(baseUrl).append(request.path);
    char sep = '?';
    for (Map.Entry<String, Object> e : request.query.entrySet()) {
      Object value = e.getValue();
      if (value instanceof Collection<?>) {
        for (Object item : (Collection<?>) value) {
          sb.append(sep).append(encode(e.getKey() + "[]")).append('=').append(encode(item));
          sep = '&';
        }
      } else {
        sb.append(sep).append(encode(e.getKey())).append('=').append(encode(value));
        sep = '&';
      }
    }
    return URI.create(sb.toString());
  }

  private static String encode(Object value) {
    return URLEncoder.encode(Multipart.text(value), StandardCharsets.UTF_8);
  }

  private <T> T parse(TransportResponse response, JavaType type) {
    int status = response.statusCode();
    String text = redact(response.bodyText());
    JsonNode node = null;
    if (!text.isBlank()) {
      try {
        node = mapper.readTree(text);
      } catch (JsonProcessingException e) {
        node = null;
      }
    }
    Map<String, Object> body =
        node != null && node.isObject() ? mapper.convertValue(node, MAP_TYPE) : null;
    String returnCode = textField(node, "return_code");
    String message = textField(node, "message");
    Map<String, List<String>> headers = response.headers();

    if (status >= 200 && status < 300) {
      if (returnCode != null && returnCode.startsWith("E")) {
        throw new InvalidRequestException(
            describe(message, status, returnCode), status, returnCode, body, text, headers);
      }
      if (node == null || !node.isObject()) {
        throw new ApiException(
            "The API returned HTTP " + status + " without a JSON object",
            status,
            returnCode,
            null,
            text,
            headers);
      }
      T value;
      try {
        value = mapper.convertValue(node, type);
      } catch (IllegalArgumentException e) {
        throw new ApiException(
            "Could not read the API response: " + e.getMessage(),
            status,
            returnCode,
            body,
            text,
            headers);
      }
      if (value instanceof ApiResponse) {
        ((ApiResponse) value).setLastResponse(new ResponseInfo(status, headers));
      }
      return value;
    }
    throw error(status, returnCode, describe(message, status, returnCode), body, text, response);
  }

  private static AgilisException error(
      int status,
      String returnCode,
      String message,
      Map<String, Object> body,
      String text,
      TransportResponse response) {
    Map<String, List<String>> headers = response.headers();
    switch (status) {
      case 400:
      case 422:
        return new InvalidRequestException(message, status, returnCode, body, text, headers);
      case 401:
        return new AuthenticationException(message, status, returnCode, body, text, headers);
      case 403:
        return new PermissionException(message, status, returnCode, body, text, headers);
      case 404:
        return new NotFoundException(message, status, returnCode, body, text, headers);
      case 409:
        return new ConflictException(message, status, returnCode, body, text, headers);
      case 429:
        return new RateLimitException(
            message, status, returnCode, body, text, headers, retryAfter(response));
      default:
        return new ApiException(message, status, returnCode, body, text, headers);
    }
  }

  private static String describe(String message, int status, String returnCode) {
    if (message != null && !message.isBlank()) {
      return message;
    }
    return "HTTP " + status + (returnCode == null ? "" : " " + returnCode);
  }

  private static String textField(JsonNode node, String name) {
    if (node == null || !node.isObject()) {
      return null;
    }
    JsonNode value = node.get(name);
    return value != null && value.isValueNode() && !value.isNull() ? value.asText() : null;
  }

  String redact(String text) {
    if (text == null) {
      return null;
    }
    String out = text;
    if (apiKey != null && apiKey.length() >= 4) {
      out = out.replace(apiKey, "[redacted]");
    }
    return KEY_PATTERN.matcher(out).replaceAll("[redacted]");
  }
}
