package dating.agilis.ads.http;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An HTTP response, as returned by a {@link Transport}.
 *
 * @param statusCode the HTTP status code
 * @param headers the response headers; names are matched without regard to case
 * @param body the response body, never {@code null}
 */
public record TransportResponse(int statusCode, Map<String, List<String>> headers, byte[] body) {

  /** Creates a response, copying the headers into a case-insensitive map. */
  public TransportResponse {
    TreeMap<String, List<String>> copy = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    if (headers != null) {
      for (Map.Entry<String, List<String>> e : headers.entrySet()) {
        if (e.getKey() != null) {
          copy.put(e.getKey(), List.copyOf(e.getValue()));
        }
      }
    }
    headers = Collections.unmodifiableMap(copy);
    body = body == null ? new byte[0] : body;
  }

  /**
   * Returns the first value of a header.
   *
   * @param name the header name, in any case
   * @return the value, or {@code null} when the header is absent
   */
  public String header(String name) {
    List<String> values = headers.get(name);
    return values == null || values.isEmpty() ? null : values.get(0);
  }

  /**
   * Returns the body as UTF-8 text.
   *
   * @return the body text
   */
  public String bodyText() {
    return new String(body, StandardCharsets.UTF_8);
  }

  @Override
  public String toString() {
    return "TransportResponse[" + statusCode + "]";
  }
}
