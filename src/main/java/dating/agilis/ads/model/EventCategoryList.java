package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The response of {@code GET /event-categories}. */
public class EventCategoryList extends ApiResponse {

  @JsonProperty("categories")
  private List<EventCategory> categories;

  /** Creates an empty object; the client fills it from JSON. */
  public EventCategoryList() {}

  /**
   * The {@code categories} field: the Event categories.
   *
   * @return the value, or {@code null} when absent
   */
  public List<EventCategory> getCategories() {
    return categories;
  }
}
