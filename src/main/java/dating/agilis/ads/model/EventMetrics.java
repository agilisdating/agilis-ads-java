package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The metrics of an Event. */
public class EventMetrics extends ApiObject {

  @JsonProperty("clicks")
  private Long clicks;

  @JsonProperty("attendees")
  private String attendees;

  @JsonProperty("invites_sent")
  private String invitesSent;

  @JsonProperty("invites_accepted")
  private String invitesAccepted;

  /** Creates an empty object; the client fills it from JSON. */
  public EventMetrics() {}

  /**
   * The {@code clicks} field: the number of clicks.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getClicks() {
    return clicks;
  }

  /**
   * The {@code attendees} field: the number of members going, as a band.
   *
   * @return the value, or {@code null} when absent
   */
  public String getAttendees() {
    return attendees;
  }

  /**
   * The {@code invites_sent} field: the number of invitations sent, as a band.
   *
   * @return the value, or {@code null} when absent
   */
  public String getInvitesSent() {
    return invitesSent;
  }

  /**
   * The {@code invites_accepted} field: the number of invitations accepted, as a band.
   *
   * @return the value, or {@code null} when absent
   */
  public String getInvitesAccepted() {
    return invitesAccepted;
  }
}
