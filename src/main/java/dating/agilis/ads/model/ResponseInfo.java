package dating.agilis.ads.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** The HTTP status and headers of a response. */
public final class ResponseInfo {

  private final int statusCode;
  private final Map<String, List<String>> headers;

  /**
   * Creates the response details.
   *
   * @param statusCode the HTTP status
   * @param headers the response headers
   */
  public ResponseInfo(int statusCode, Map<String, List<String>> headers) {
    this.statusCode = statusCode;
    TreeMap<String, List<String>> copy = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    if (headers != null) {
      copy.putAll(headers);
    }
    this.headers = Collections.unmodifiableMap(copy);
  }

  /**
   * The HTTP status code.
   *
   * @return the status
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * The response headers; names are matched without regard to case.
   *
   * @return the headers
   */
  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  /**
   * Returns the first value of a header, for example {@code X-RateLimit-Remaining}.
   *
   * @param name the header name, in any case
   * @return the value, or {@code null} when the header is absent
   */
  public String getHeader(String name) {
    List<String> values = headers.get(name);
    return values == null || values.isEmpty() ? null : values.get(0);
  }

  @Override
  public String toString() {
    return "ResponseInfo[" + statusCode + "]";
  }
}
