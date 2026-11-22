package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The stored original image of an Event. */
public class ImageMaster extends ApiObject {

  @JsonProperty("width")
  private Integer width;

  @JsonProperty("height")
  private Integer height;

  @JsonProperty("crop")
  private Crop crop;

  @JsonProperty("url")
  private String url;

  /** Creates an empty object; the client fills it from JSON. */
  public ImageMaster() {}

  /**
   * The {@code width} field: the width.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getWidth() {
    return width;
  }

  /**
   * The {@code height} field: the height.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getHeight() {
    return height;
  }

  /**
   * The {@code crop} field: the crop area in use.
   *
   * @return the value, or {@code null} when absent
   */
  public Crop getCrop() {
    return crop;
  }

  /**
   * The {@code url} field: the URL, when available.
   *
   * @return the value, or {@code null} when absent
   */
  public String getUrl() {
    return url;
  }
}
