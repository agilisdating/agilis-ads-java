package dating.agilis.ads.exception;

import java.util.List;
import java.util.Map;

/** The resource does not exist, or belongs to the other mode (HTTP 404). */
public class NotFoundException extends AgilisException {

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
  public NotFoundException(
      String message,
      int statusCode,
      String returnCode,
      Map<String, Object> body,
      String rawBody,
      Map<String, List<String>> headers) {
    super(message, statusCode, returnCode, body, rawBody, headers, null);
  }
}
