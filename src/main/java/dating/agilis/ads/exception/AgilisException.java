package dating.agilis.ads.exception;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The base type of every error raised by this library.
 *
 * <p>It carries the HTTP status (0 when there was no response), the API's {@code return_code}, its
 * {@code message}, the parsed body and the response headers. The API key is never part of an
 * error's text.
 */
public class AgilisException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final int statusCode;
  private final String returnCode;
  private final transient Map<String, Object> body;
  private final String rawBody;
  private final transient Map<String, List<String>> headers;

  /**
   * Creates an error.
   *
   * @param message the error message
   * @param statusCode the HTTP status, or 0 when there was no response
   * @param returnCode the API's {@code return_code}, or {@code null}
   * @param body the parsed JSON body, or {@code null}
   * @param rawBody the response body as text, or {@code null}
   * @param headers the response headers, or {@code null}
   * @param cause the underlying cause, or {@code null}
   */
  public AgilisException(
      String message,
      int statusCode,
      String returnCode,
      Map<String, Object> body,
      String rawBody,
      Map<String, List<String>> headers,
      Throwable cause) {
    super(message, cause);
    this.statusCode = statusCode;
    this.returnCode = returnCode;
    this.body = body == null ? Collections.emptyMap() : Collections.unmodifiableMap(body);
    this.rawBody = rawBody;
    TreeMap<String, List<String>> copy = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    if (headers != null) {
      copy.putAll(headers);
    }
    this.headers = Collections.unmodifiableMap(copy);
  }

  /**
   * The HTTP status code.
   *
   * @return the status, or 0 when no response was received
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * The API's {@code return_code}, for example {@code E1}.
   *
   * @return the code, or {@code null} when the response had none
   */
  public String getReturnCode() {
    return returnCode;
  }

  /**
   * The parsed JSON body of the response.
   *
   * @return the body, empty when the response had no JSON object body
   */
  public Map<String, Object> getBody() {
    return body;
  }

  /**
   * The response body as text.
   *
   * @return the body, or {@code null} when there was no response
   */
  public String getRawBody() {
    return rawBody;
  }

  /**
   * The response headers; names are matched without regard to case.
   *
   * @return the headers, empty when there was no response
   */
  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  /**
   * Returns the first value of a response header.
   *
   * @param name the header name, in any case
   * @return the value, or {@code null} when the header is absent
   */
  public String getHeader(String name) {
    List<String> values = headers.get(name);
    return values == null || values.isEmpty() ? null : values.get(0);
  }
}
