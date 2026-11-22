package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The image of an Event preview. */
public class PreviewImage extends ApiObject {

  @JsonProperty("preview_url")
  private String previewUrl;

  @JsonProperty("width")
  private Integer width;

  @JsonProperty("height")
  private Integer height;

  @JsonProperty("master_width")
  private Integer masterWidth;

  @JsonProperty("master_height")
  private Integer masterHeight;

  @JsonProperty("crop")
  private Crop crop;

  @JsonProperty("preview_token")
  private String previewToken;

  @JsonProperty("expires_at")
  private String expiresAt;

  /** Creates an empty object; the client fills it from JSON. */
  public PreviewImage() {}

  /**
   * The {@code preview_url} field: a temporary URL of the framed image.
   *
   * @return the value, or {@code null} when absent
   */
  public String getPreviewUrl() {
    return previewUrl;
  }

  /**
   * The {@code width} field: the framed width.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getWidth() {
    return width;
  }

  /**
   * The {@code height} field: the framed height.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getHeight() {
    return height;
  }

  /**
   * The {@code master_width} field: the original width.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getMasterWidth() {
    return masterWidth;
  }

  /**
   * The {@code master_height} field: the original height.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getMasterHeight() {
    return masterHeight;
  }

  /**
   * The {@code crop} field: the crop area used.
   *
   * @return the value, or {@code null} when absent
   */
  public Crop getCrop() {
    return crop;
  }

  /**
   * The {@code preview_token} field: the token to publish exactly this image.
   *
   * @return the value, or {@code null} when absent
   */
  public String getPreviewToken() {
    return previewToken;
  }

  /**
   * The {@code expires_at} field: when the token expires.
   *
   * @return the value, or {@code null} when absent
   */
  public String getExpiresAt() {
    return expiresAt;
  }
}
