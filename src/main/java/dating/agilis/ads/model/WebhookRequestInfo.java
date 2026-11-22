package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** What caused a webhook event. */
public class WebhookRequestInfo extends ApiObject {

  @JsonProperty("source")
  private String source;

  @JsonProperty("idempotency_key")
  private String idempotencyKey;

  /** Creates an empty object; the client fills it from JSON. */
  public WebhookRequestInfo() {}

  /**
   * The {@code source} field: {@code api}, {@code mcp}, {@code ad_portal}, {@code agilis} or {@code
   * system}.
   *
   * @return the value, or {@code null} when absent
   */
  public String getSource() {
    return source;
  }

  /**
   * The {@code idempotency_key} field: the Idempotency-Key of the API request, when there was one.
   *
   * @return the value, or {@code null} when absent
   */
  public String getIdempotencyKey() {
    return idempotencyKey;
  }
}
