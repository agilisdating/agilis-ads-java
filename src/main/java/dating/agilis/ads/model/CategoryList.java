package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The response of {@code GET /categories}. */
public class CategoryList extends ApiResponse {

  @JsonProperty("categories")
  private List<Category> categories;

  /** Creates an empty object; the client fills it from JSON. */
  public CategoryList() {}

  /**
   * The {@code categories} field: the Offer categories.
   *
   * @return the value, or {@code null} when absent
   */
  public List<Category> getCategories() {
    return categories;
  }
}
