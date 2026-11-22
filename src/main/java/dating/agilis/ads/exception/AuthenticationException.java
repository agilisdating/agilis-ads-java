package dating.agilis.ads.exception;

import java.util.List;
import java.util.Map;

/** The API key was missing or not accepted (HTTP 401). */
public class AuthenticationException extends AgilisException {

  private static final long serialVersionUID = 1L;

  /**
   * Creates the error.
   *
   * @param message the error message
   * @param statusCode the HTTP status
   * @param returnCode the API's {@code return_code}, or {@code null}
   * @param body the parsed JSON body, or {@code null}
   * @param rawBody the response body as text, or {@code null}
   * @param headers the response headers, or {@code null}
   */
  public AuthenticationException(
      String message,
      int statusCode,
      String returnCode,
      Map<String, Object> body,
      String rawBody,
      Map<String, List<String>> headers) {
    super(message, statusCode, returnCode, body, rawBody, headers, null);
  }
}
