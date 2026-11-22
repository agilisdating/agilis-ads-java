package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** An Event category. */
public class EventCategory extends ApiObject {

  @JsonProperty("category_id")
  private Long categoryId;

  @JsonProperty("code")
  private String code;

  @JsonProperty("category_name")
  private String categoryName;

  /** Creates an empty object; the client fills it from JSON. */
  public EventCategory() {}

  /**
   * The {@code category_id} field: the category id.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getCategoryId() {
    return categoryId;
  }

  /**
   * The {@code code} field: the category code.
   *
   * @return the value, or {@code null} when absent
   */
  public String getCode() {
    return code;
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
