package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The response of {@code PATCH /events/{event_id}}. */
public class EventEdited extends ApiResponse {

  @JsonProperty("event_id")
  private Long eventId;

  @JsonProperty("status")
  private String status;

  @JsonProperty("category")
  private Category category;

  @JsonProperty("changed_fields")
  private List<String> changedFields;

  @JsonProperty("image")
  private String image;

  @JsonProperty("image_large")
  private String imageLarge;

  @JsonProperty("venue_changed")
  private Boolean venueChanged;

  /** Creates an empty object; the client fills it from JSON. */
  public EventEdited() {}

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
   * The {@code category} field: the category, determined again after a wording change.
   *
   * @return the value, or {@code null} when absent
   */
  public Category getCategory() {
    return category;
  }

  /**
   * The {@code changed_fields} field: the fields that changed.
   *
   * @return the value, or {@code null} when absent
   */
  public List<String> getChangedFields() {
    return changedFields;
  }

  /**
   * The {@code image} field: the image URL.
   *
   * @return the value, or {@code null} when absent
   */
  public String getImage() {
    return image;
  }

  /**
   * The {@code image_large} field: the large image URL.
   *
   * @return the value, or {@code null} when absent
   */
  public String getImageLarge() {
    return imageLarge;
  }

  /**
   * The {@code venue_changed} field: whether the venue changed.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getVenueChanged() {
    return venueChanged;
  }
}
