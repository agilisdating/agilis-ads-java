package dating.agilis.ads.exception;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Too many requests (HTTP 429). {@link #getRetryAfter()} says how long to wait. */
public class RateLimitException extends AgilisException {

  private static final long serialVersionUID = 1L;

  private final Duration retryAfter;

  /**
   * Creates the error.
   *
   * @param message the error message
   * @param statusCode the HTTP status
   * @param returnCode the API's {@code return_code}, or {@code null}
   * @param body the parsed JSON body, or {@code null}
   * @param rawBody the response body as text, or {@code null}
   * @param headers the response headers, or {@code null}
   * @param retryAfter the wait from the {@code Retry-After} header, or {@code null}
   */
  public RateLimitException(
      String message,
      int statusCode,
      String returnCode,
      Map<String, Object> body,
      String rawBody,
      Map<String, List<String>> headers,
      Duration retryAfter) {
    super(message, statusCode, returnCode, body, rawBody, headers, null);
    this.retryAfter = retryAfter;
  }

  /**
   * The wait the API asked for in its {@code Retry-After} header.
   *
   * @return the wait, or empty when the header was absent
   */
  public Optional<Duration> getRetryAfter() {
    return Optional.ofNullable(retryAfter);
  }
}
