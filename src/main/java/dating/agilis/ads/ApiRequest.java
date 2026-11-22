package dating.agilis.ads;

import dating.agilis.ads.internal.Json;
import java.io.UncheckedIOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/** One API call, before it is sent. */
final class ApiRequest {

  final String method;
  final String path;
  final Map<String, Object> query = new LinkedHashMap<>();
  final Map<String, String> headers = new LinkedHashMap<>();
  byte[] body;
  String contentType;

  private ApiRequest(String method, String path) {
    this.method = method;
    this.path = path;
  }

  static ApiRequest of(String method, String path) {
    return new ApiRequest(method, path);
  }

  ApiRequest query(String name, Object value) {
    if (value != null) {
      query.put(name, value);
    }
    return this;
  }

  ApiRequest query(Map<String, Object> values) {
    for (Map.Entry<String, Object> e : values.entrySet()) {
      query(e.getKey(), e.getValue());
    }
    return this;
  }

  ApiRequest header(String name, String value) {
    headers.put(name, value);
    return this;
  }

  ApiRequest json(Map<String, Object> values) {
    try {
      this.body = Json.mapper().writeValueAsBytes(values);
    } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
      throw new UncheckedIOException(e);
    }
    this.contentType = "application/json";
    return this;
  }

  ApiRequest multipart(Map<String, Object> values) {
    Multipart.Encoded encoded = Multipart.encode(values);
    this.body = encoded.body();
    this.contentType = encoded.contentType();
    return this;
  }

  ApiRequest dryRun(boolean dryRun) {
    if (dryRun) {
      query.put("dry_run", "true");
    }
    return this;
  }

  boolean hasIdempotencyKey() {
    return headers.containsKey("Idempotency-Key");
  }

  static String segment(Object value) {
    return URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8).replace("+", "%20");
  }
}
