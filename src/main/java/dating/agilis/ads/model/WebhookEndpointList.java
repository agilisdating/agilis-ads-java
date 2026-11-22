package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The response of {@code GET /webhook-endpoints}. */
public class WebhookEndpointList extends ApiResponse {

  @JsonProperty("data")
  private List<WebhookEndpoint> data;

  /** Creates an empty object; the client fills it from JSON. */
  public WebhookEndpointList() {}

  /**
   * The {@code data} field: the endpoints.
   *
   * @return the value, or {@code null} when absent
   */
  public List<WebhookEndpoint> getData() {
    return data;
  }
}
