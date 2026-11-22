package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The response of {@code GET /events/{event_id}/analytics}. */
public class EventAnalytics extends ApiResponse {

  @JsonProperty("event_id")
  private Long eventId;

  @JsonProperty("interval")
  private String interval;

  @JsonProperty("starts_at")
  private String startsAt;

  @JsonProperty("ends_at")
  private String endsAt;

  @JsonProperty("status")
  private String status;

  @JsonProperty("metrics")
  private EventMetrics metrics;

  @JsonProperty("as_of")
  private String asOf;

  /** Creates an empty object; the client fills it from JSON. */
  public EventAnalytics() {}

  /**
   * The {@code event_id} field: the Event id.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getEventId() {
    return eventId;
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
   * The {@code starts_at} field: the start of the interval.
   *
   * @return the value, or {@code null} when absent
   */
  public String getStartsAt() {
    return startsAt;
  }

  /**
   * The {@code ends_at} field: the end of the interval.
   *
   * @return the value, or {@code null} when absent
   */
  public String getEndsAt() {
    return endsAt;
  }

  /**
   * The {@code status} field: the Event's status.
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
  public EventMetrics getMetrics() {
    return metrics;
  }

  /**
   * The {@code as_of} field: when the metrics were calculated.
   *
   * @return the value, or {@code null} when absent
   */
  public String getAsOf() {
    return asOf;
  }
}
