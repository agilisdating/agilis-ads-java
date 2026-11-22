package dating.agilis.ads.exception;

import java.util.List;
import java.util.Map;

/** The key may not do this (HTTP 403), for example a request for the other mode. */
public class PermissionException extends AgilisException {

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
  public PermissionException(
      String message,
      int statusCode,
      String returnCode,
      Map<String, Object> body,
      String rawBody,
      Map<String, List<String>> headers) {
    super(message, statusCode, returnCode, body, rawBody, headers, null);
  }
}
