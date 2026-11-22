package dating.agilis.ads.exception;

/**
 * A received webhook could not be verified by {@link
 * dating.agilis.ads.Webhooks#constructEvent(String, String, String)}.
 */
public class SignatureVerificationException extends AgilisException {

  private static final long serialVersionUID = 1L;

  /** Why verification failed. */
  public enum Reason {
    /** The {@code Agilis-Signature} header is missing or malformed, or has no {@code v1}. */
    HEADER,
    /** The timestamp is outside the tolerance. */
    TIMESTAMP,
    /** No {@code v1} signature matches the body and secret. */
    SIGNATURE,
    /** The signature is valid but the body is not a JSON object. */
    PAYLOAD
  }

  private final Reason reason;

  /**
   * Creates the error.
   *
   * @param message the error message
   * @param reason why verification failed
   */
  public SignatureVerificationException(String message, Reason reason) {
    super(message, 0, null, null, null, null, null);
    this.reason = reason;
  }

  /**
   * Why verification failed.
   *
   * @return the reason
   */
  public Reason getReason() {
    return reason;
  }
}
