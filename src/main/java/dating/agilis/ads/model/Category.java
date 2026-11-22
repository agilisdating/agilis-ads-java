package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** An Offer or Event category. */
public class Category extends ApiObject {

  @JsonProperty("category_id")
  private Long categoryId;

  @JsonProperty("category_name")
  private String categoryName;

  /** Creates an empty object; the client fills it from JSON. */
  public Category() {}

  /**
   * The {@code category_id} field: the category id.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getCategoryId() {
    return categoryId;
  }

  /**
   * The {@code category_name} field: the category name.
   *
   * @return the value, or {@code null} when absent
   */
  public String getCategoryName() {
    return categoryName;
  }
}
