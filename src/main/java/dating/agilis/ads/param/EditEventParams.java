package dating.agilis.ads.param;

import java.math.BigDecimal;
import java.nio.file.Path;

/**
 * Parameters for {@code PATCH /events/{event_id}}, sent as multipart form data. Send only the
 * fields to change.
 */
public final class EditEventParams extends Params {

  private EditEventParams(Builder builder) {
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

  /** Builds {@link EditEventParams}. */
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
     * Sets {@code url}: your booking page.
     *
     * @param url the value
     * @return this builder
     */
    public Builder url(String url) {
      return put("url", url);
    }

    /**
     * Sets {@code advertised_name}: the business name; empty to go back to your trading name.
     *
     * @param advertisedName the value
     * @return this builder
     */
    public Builder advertisedName(String advertisedName) {
      return put("advertised_name", advertisedName);
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
     * Sets {@code address}: the street address.
     *
     * @param address the value
     * @return this builder
     */
    public Builder address(String address) {
      return put("address", address);
    }

    /**
     * Sets {@code city}: the city.
     *
     * @param city the value
     * @return this builder
     */
    public Builder city(String city) {
      return put("city", city);
    }

    /**
     * Sets {@code country_code}: the ISO 3166-1 alpha-2 country code.
     *
     * @param countryCode the value
     * @return this builder
     */
    public Builder countryCode(String countryCode) {
      return put("country_code", countryCode);
    }

    /**
     * Sets {@code location}: the latitude and longitude.
     *
     * @param latitude the latitude
     * @param longitude the longitude
     * @return this builder
     */
    public Builder location(double latitude, double longitude) {
      return super.location(latitude, longitude);
    }

    /**
     * Sets {@code price_min}: the lowest ticket price; send with the highest price and the
     * currency.
     *
     * @param priceMin the value
     * @return this builder
     */
    public Builder priceMin(BigDecimal priceMin) {
      return put("price_min", priceMin);
    }

    /**
     * Sets {@code price_min}: the lowest ticket price; send with the highest price and the
     * currency.
     *
     * @param priceMin the value
     * @return this builder
     */
    public Builder priceMin(double priceMin) {
      return decimal("price_min", priceMin);
    }

    /**
     * Sets {@code price_max}: the highest ticket price; send with the lowest price and the
     * currency.
     *
     * @param priceMax the value
     * @return this builder
     */
    public Builder priceMax(BigDecimal priceMax) {
      return put("price_max", priceMax);
    }

    /**
     * Sets {@code price_max}: the highest ticket price; send with the lowest price and the
     * currency.
     *
     * @param priceMax the value
     * @return this builder
     */
    public Builder priceMax(double priceMax) {
      return decimal("price_max", priceMax);
    }

    /**
     * Sets {@code currency}: the ticket price currency.
     *
     * @param currency the value
     * @return this builder
     */
    public Builder currency(String currency) {
      return put("currency", currency);
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
     * Sets {@code preview_token}: a token from {@code POST /events/preview}, instead of an image.
     *
     * @param previewToken the value
     * @return this builder
     */
    public Builder previewToken(String previewToken) {
      return put("preview_token", previewToken);
    }

    /**
     * Sets {@code source_event_id}: an existing Event whose image to reuse, instead of an image.
     *
     * @param sourceEventId the value
     * @return this builder
     */
    public Builder sourceEventId(long sourceEventId) {
      return put("source_event_id", sourceEventId);
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
     * Builds the parameters.
     *
     * @return the parameters
     */
    public EditEventParams build() {
      return new EditEventParams(this);
    }
  }
}
