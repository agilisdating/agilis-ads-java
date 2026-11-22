package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The delivery of a webhook event to one endpoint. */
public class WebhookDelivery extends ApiObject {

  @JsonProperty("endpoint_id")
  private String endpointId;

  @JsonProperty("status")
  private String status;

  @JsonProperty("attempts")
  private Integer attempts;

  @JsonProperty("last_http_status")
  private Integer lastHttpStatus;

  @JsonProperty("last_error")
  private String lastError;

  @JsonProperty("next_attempt_at")
  private String nextAttemptAt;

  @JsonProperty("delivered_at")
  private String deliveredAt;

  /** Creates an empty object; the client fills it from JSON. */
  public WebhookDelivery() {}

  /**
   * The {@code endpoint_id} field: the endpoint id.
   *
   * @return the value, or {@code null} when absent
   */
  public String getEndpointId() {
    return endpointId;
  }

  /**
   * The {@code status} field: the delivery status.
   *
   * @return the value, or {@code null} when absent
   */
  public String getStatus() {
    return status;
  }

  /**
   * The {@code attempts} field: how many attempts were made.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getAttempts() {
    return attempts;
  }

  /**
   * The {@code last_http_status} field: the HTTP status of the last attempt.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getLastHttpStatus() {
    return lastHttpStatus;
  }

  /**
   * The {@code last_error} field: the error of the last attempt.
   *
   * @return the value, or {@code null} when absent
   */
  public String getLastError() {
    return lastError;
  }

  /**
   * The {@code next_attempt_at} field: when the next attempt is due.
   *
   * @return the value, or {@code null} when absent
   */
  public String getNextAttemptAt() {
    return nextAttemptAt;
  }

  /**
   * The {@code delivered_at} field: when it was delivered.
   *
   * @return the value, or {@code null} when absent
   */
  public String getDeliveredAt() {
    return deliveredAt;
  }
}
