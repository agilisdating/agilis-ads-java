package dating.agilis.ads.exception;

/** No response was received: a connection error, a timeout or an interrupted request. */
public class ConnectionException extends AgilisException {

  private static final long serialVersionUID = 1L;

  /**
   * Creates the error.
   *
   * @param message the error message
   * @param cause the underlying cause
   */
  public ConnectionException(String message, Throwable cause) {
    super(message, 0, null, null, null, null, cause);
  }
}
