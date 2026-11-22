package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The response of {@code POST /webhook-events/{webhook_event_id}/resend}. */
public class ResendResult extends ApiResponse {

  @JsonProperty("webhook_event_id")
  private String webhookEventId;

  @JsonProperty("deliveries")
  private List<WebhookDelivery> deliveries;

  /** Creates an empty object; the client fills it from JSON. */
  public ResendResult() {}

  /**
   * The {@code webhook_event_id} field: the event id.
   *
   * @return the value, or {@code null} when absent
   */
  public String getWebhookEventId() {
    return webhookEventId;
  }

  /**
   * The {@code deliveries} field: the new deliveries.
   *
   * @return the value, or {@code null} when absent
   */
  public List<WebhookDelivery> getDeliveries() {
    return deliveries;
  }
}
