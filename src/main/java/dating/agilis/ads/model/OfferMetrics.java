package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The metrics of an Offer. */
public class OfferMetrics extends ApiObject {

  @JsonProperty("views")
  private Long views;

  @JsonProperty("clicks")
  private Long clicks;

  /** Creates an empty object; the client fills it from JSON. */
  public OfferMetrics() {}

  /**
   * The {@code views} field: the number of views.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getViews() {
    return views;
  }

  /**
   * The {@code clicks} field: the number of clicks.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getClicks() {
    return clicks;
  }
}
