package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The response of {@code GET /offers/{offer_id}/analytics}. */
public class OfferAnalytics extends ApiResponse {

  @JsonProperty("offer_id")
  private Long offerId;

  @JsonProperty("interval")
  private String interval;

  @JsonProperty("start_datetime")
  private String startDatetime;

  @JsonProperty("end_datetime")
  private String endDatetime;

  @JsonProperty("status")
  private String status;

  @JsonProperty("metrics")
  private OfferMetrics metrics;

  /** Creates an empty object; the client fills it from JSON. */
  public OfferAnalytics() {}

  /**
   * The {@code offer_id} field: the Offer id.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getOfferId() {
    return offerId;
  }

  /**
   * The {@code interval} field: the interval.
   *
   * @return the value, or {@code null} when absent
   */
  public String getInterval() {
    return interval;
  }

  /**
   * The {@code start_datetime} field: the start of the interval.
   *
   * @return the value, or {@code null} when absent
   */
  public String getStartDatetime() {
    return startDatetime;
  }

  /**
   * The {@code end_datetime} field: the end of the interval.
   *
   * @return the value, or {@code null} when absent
   */
  public String getEndDatetime() {
    return endDatetime;
  }

  /**
   * The {@code status} field: the Offer's status.
   *
   * @return the value, or {@code null} when absent
   */
  public String getStatus() {
    return status;
  }

  /**
   * The {@code metrics} field: the metrics.
   *
   * @return the value, or {@code null} when absent
   */
  public OfferMetrics getMetrics() {
    return metrics;
  }
}
