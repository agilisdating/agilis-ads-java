package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/** One entry of an Event's history. */
public class EventHistoryEntry extends ApiObject {

  @JsonProperty("action")
  private String action;

  @JsonProperty("channel")
  private String channel;

  @JsonProperty("acted_by_user_id")
  private Long actedByUserId;

  @JsonProperty("acted_by")
  private String actedBy;

  @JsonProperty("details")
  private Map<String, Object> details;

  @JsonProperty("acknowledged")
  private Boolean acknowledged;

  @JsonProperty("at")
  private String at;

  /** Creates an empty object; the client fills it from JSON. */
  public EventHistoryEntry() {}

  /**
   * The {@code action} field: what happened.
   *
   * @return the value, or {@code null} when absent
   */
  public String getAction() {
    return action;
  }

  /**
   * The {@code channel} field: where it was done.
   *
   * @return the value, or {@code null} when absent
   */
  public String getChannel() {
    return channel;
  }

  /**
   * The {@code acted_by_user_id} field: who did it.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getActedByUserId() {
    return actedByUserId;
  }

  /**
   * The {@code acted_by} field: the name of who did it.
   *
   * @return the value, or {@code null} when absent
   */
  public String getActedBy() {
    return actedBy;
  }

  /**
   * The {@code details} field: details of the change.
   *
   * @return the value, or {@code null} when absent
   */
  public Map<String, Object> getDetails() {
    return details;
  }

  /**
   * The {@code acknowledged} field: whether it was acknowledged.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getAcknowledged() {
    return acknowledged;
  }

  /**
   * The {@code at} field: when it happened.
   *
   * @return the value, or {@code null} when absent
   */
  public String getAt() {
    return at;
  }
}
