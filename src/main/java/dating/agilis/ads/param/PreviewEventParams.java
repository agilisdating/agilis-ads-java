package dating.agilis.ads.param;

import java.nio.file.Path;

/** Parameters for {@code POST /events/preview}, sent as multipart form data. */
public final class PreviewEventParams extends Params {

  private PreviewEventParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts a builder.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link PreviewEventParams}. */
  public static final class Builder extends Params.Builder<Builder> {

    private Builder() {}

    @Override
    protected Builder self() {
      return this;
    }

    /**
     * Sets {@code title}: the title.
     *
     * @param title the value
     * @return this builder
     */
    public Builder title(String title) {
      return put("title", title);
    }

    /**
     * Sets {@code description}: the description.
     *
     * @param description the value
     * @return this builder
     */
    public Builder description(String description) {
      return put("description", description);
    }

    /**
     * Sets {@code venue_name}: the venue name.
     *
     * @param venueName the value
     * @return this builder
     */
    public Builder venueName(String venueName) {
      return put("venue_name", venueName);
    }

    /**
     * Sets {@code image}: the image file.
     *
     * @param image the file
     * @return this builder
     */
    public Builder image(FileUpload image) {
      return put("image", image);
    }

    /**
     * Sets {@code image}: the image file.
     *
     * @param image the file to read
     * @return this builder
     */
    public Builder image(Path image) {
      return put("image", FileUpload.of(image));
    }

    /**
     * Sets {@code crop[x]}, {@code crop[y]} and {@code crop[w]}: the 16:9 area of the image to use.
     *
     * @param x the left edge, in pixels
     * @param y the top edge, in pixels
     * @param w the width, in pixels
     * @return this builder
     */
    public Builder crop(int x, int y, int w) {
      return super.crop(x, y, w);
    }

    /**
     * Sets {@code environment}: {@code TEST} or {@code LIVE}. Filled in from the API key's mode
     * when left out.
     *
     * @param environment the value
     * @return this builder
     */
    public Builder environment(String environment) {
      return put("environment", environment);
    }

    /**
     * Builds the parameters.
     *
     * @return the parameters
     */
    public PreviewEventParams build() {
      return new PreviewEventParams(this);
    }
  }
}
