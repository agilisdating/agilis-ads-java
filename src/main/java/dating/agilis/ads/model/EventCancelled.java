package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The response of {@code POST /events/{event_id}/cancel}. */
public class EventCancelled extends ApiResponse {

  @JsonProperty("event_id")
  private Long eventId;

  @JsonProperty("status")
  private String status;

  @JsonProperty("dry_run")
  private Boolean dryRun;

  /** Creates an empty object; the client fills it from JSON. */
  public EventCancelled() {}

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
   * The {@code dry_run} field: {@code true} for a dry run, when nothing was changed.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getDryRun() {
    return dryRun;
  }
}
