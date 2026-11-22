package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** A crop area, in pixels of the original image. */
public class Crop extends ApiObject {

  @JsonProperty("x")
  private Integer x;

  @JsonProperty("y")
  private Integer y;

  @JsonProperty("w")
  private Integer w;

  @JsonProperty("h")
  private Integer h;

  /** Creates an empty object; the client fills it from JSON. */
  public Crop() {}

  /**
   * The {@code x} field: the left edge.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getX() {
    return x;
  }

  /**
   * The {@code y} field: the top edge.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getY() {
    return y;
  }

  /**
   * The {@code w} field: the width.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getW() {
    return w;
  }

  /**
   * The {@code h} field: the height.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getH() {
    return h;
  }
}
