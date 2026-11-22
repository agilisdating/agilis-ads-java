package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** A member interest used for targeting. */
public class Interest extends ApiObject {

  @JsonProperty("interest_id")
  private Long interestId;

  @JsonProperty("interest_name")
  private String interestName;

  /** Creates an empty object; the client fills it from JSON. */
  public Interest() {}

  /**
   * The {@code interest_id} field: the interest id.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getInterestId() {
    return interestId;
  }

  /**
   * The {@code interest_name} field: the interest name.
   *
   * @return the value, or {@code null} when absent
   */
  public String getInterestName() {
    return interestName;
  }
}
