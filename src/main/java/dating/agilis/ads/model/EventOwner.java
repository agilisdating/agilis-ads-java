package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The member of your organisation who published an Event. */
public class EventOwner extends ApiObject {

  @JsonProperty("user_id")
  private Long userId;

  @JsonProperty("name")
  private String name;

  /** Creates an empty object; the client fills it from JSON. */
  public EventOwner() {}

  /**
   * The {@code user_id} field: the user id.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getUserId() {
    return userId;
  }

  /**
   * The {@code name} field: the name.
   *
   * @return the value, or {@code null} when absent
   */
  public String getName() {
    return name;
  }
}
