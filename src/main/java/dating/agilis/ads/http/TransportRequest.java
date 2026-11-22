package dating.agilis.ads.http;

import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An HTTP request, as handed to a {@link Transport}.
 *
 * @param method the HTTP method, for example {@code GET}
 * @param uri the full request URI
 * @param headers the request headers, one value each
 * @param body the request body, or {@code null} for none
 * @param timeout the time allowed for the whole request
 */
public record TransportRequest(
    String method, URI uri, Map<String, String> headers, byte[] body, Duration timeout) {

  /** Creates a request, copying the headers. */
  public TransportRequest {
    Objects.requireNonNull(method, "method");
    Objects.requireNonNull(uri, "uri");
    headers = Collections.unmodifiableMap(new LinkedHashMap<>(headers));
  }

  /**
   * Returns a header value, matching the name without regard to case.
   *
   * @param name the header name
   * @return the value, or {@code null} when the header is absent
   */
  public String header(String name) {
    for (Map.Entry<String, String> e : headers.entrySet()) {
      if (e.getKey().equalsIgnoreCase(name)) {
        return e.getValue();
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "TransportRequest[" + method + " " + uri + "]";
  }
}
