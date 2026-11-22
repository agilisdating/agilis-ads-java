package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The response of {@code GET /interests}. */
public class InterestList extends ApiResponse {

  @JsonProperty("interests")
  private List<Interest> interests;

  /** Creates an empty object; the client fills it from JSON. */
  public InterestList() {}

  /**
   * The {@code interests} field: the interests.
   *
   * @return the value, or {@code null} when absent
   */
  public List<Interest> getInterests() {
    return interests;
  }
}
