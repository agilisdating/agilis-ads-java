package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The image rules for Events. */
public class ImageRules extends ApiObject {

  @JsonProperty("max_mb")
  private Integer maxMb;

  @JsonProperty("min_width")
  private Integer minWidth;

  @JsonProperty("min_height")
  private Integer minHeight;

  /** Creates an empty object; the client fills it from JSON. */
  public ImageRules() {}

  /**
   * The {@code max_mb} field: the largest file size, in megabytes.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getMaxMb() {
    return maxMb;
  }

  /**
   * The {@code min_width} field: the smallest width, in pixels.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getMinWidth() {
    return minWidth;
  }

  /**
   * The {@code min_height} field: the smallest height, in pixels.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getMinHeight() {
    return minHeight;
  }
}
