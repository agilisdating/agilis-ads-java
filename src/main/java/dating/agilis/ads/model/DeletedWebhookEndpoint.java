package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The response of {@code DELETE /webhook-endpoints/{endpoint_id}}. */
public class DeletedWebhookEndpoint extends ApiResponse {

  @JsonProperty("id")
  private String id;

  @JsonProperty("deleted")
  private Boolean deleted;

  /** Creates an empty object; the client fills it from JSON. */
  public DeletedWebhookEndpoint() {}

  /**
   * The {@code id} field: the endpoint id.
   *
   * @return the value, or {@code null} when absent
   */
  public String getId() {
    return id;
  }

  /**
   * The {@code deleted} field: whether it was deleted.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getDeleted() {
    return deleted;
  }
}
