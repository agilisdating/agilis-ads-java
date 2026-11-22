package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The response of {@code POST /events/{event_id}/reschedule}. */
public class EventRescheduled extends ApiResponse {

  @JsonProperty("event_id")
  private Long eventId;

  @JsonProperty("status")
  private String status;

  @JsonProperty("starts_at")
  private String startsAt;

  @JsonProperty("ends_at")
  private String endsAt;

  /** Creates an empty object; the client fills it from JSON. */
  public EventRescheduled() {}

  /**
   * The {@code event_id} field: the Event id.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getEventId() {
    return eventId;
  }

  /**
   * The {@code status} field: the status.
   *
   * @return the value, or {@code null} when absent
   */
  public String getStatus() {
    return status;
  }

  /**
   * The {@code starts_at} field: the new start.
   *
   * @return the value, or {@code null} when absent
   */
  public String getStartsAt() {
    return startsAt;
  }

  /**
   * The {@code ends_at} field: the new end.
   *
   * @return the value, or {@code null} when absent
   */
  public String getEndsAt() {
    return endsAt;
  }
}
