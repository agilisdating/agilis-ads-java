package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The response of {@code POST /webhook-endpoints/{endpoint_id}/test}. */
public class TestWebhookResult extends ApiResponse {

  @JsonProperty("webhook_event_id")
  private String webhookEventId;

  @JsonProperty("type")
  private String type;

  @JsonProperty("test")
  private Boolean test;

  @JsonProperty("delivery")
  private WebhookDelivery delivery;

  /** Creates an empty object; the client fills it from JSON. */
  public TestWebhookResult() {}

  /**
   * The {@code webhook_event_id} field: the test event's id.
   *
   * @return the value, or {@code null} when absent
   */
  public String getWebhookEventId() {
    return webhookEventId;
  }

  /**
   * The {@code type} field: the event type.
   *
   * @return the value, or {@code null} when absent
   */
  public String getType() {
    return type;
  }

  /**
   * The {@code test} field: always {@code true}.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getTest() {
    return test;
  }

  /**
   * The {@code delivery} field: the delivery.
   *
   * @return the value, or {@code null} when absent
   */
  public WebhookDelivery getDelivery() {
    return delivery;
  }
}
