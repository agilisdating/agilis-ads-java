package dating.agilis.ads.param;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.Instant;

/** Parameters for {@code POST /events}, sent as multipart form data. */
public final class PublishEventParams extends Params {

  private final String idempotencyKey;
  private final boolean dryRun;

  private PublishEventParams(Builder builder) {
    super(builder);
    this.idempotencyKey = builder.idempotencyKey;
    this.dryRun = builder.dryRun;
  }

  /**
   * Starts a builder.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * The Idempotency-Key to send.
   *
   * @return the key, or {@code null} to have one generated
   */
  public String getIdempotencyKey() {
    return idempotencyKey;
  }

  /**
   * Whether to send {@code dry_run=true}.
   *
   * @return {@code true} for a dry run
   */
  public boolean isDryRun() {
    return dryRun;
  }

  /** Builds {@link PublishEventParams}. */
  public static final class Builder extends Params.Builder<Builder> {

    private String idempotencyKey;
    private boolean dryRun;

    private Builder() {}

    @Override
    protected Builder self() {
      return this;
    }

    /**
     * Sets {@code title}: the title, up to 120 characters.
     *
     * @param title the value
     * @return this builder
     */
    public Builder title(String title) {
      return put("title", title);
    }

    /**
     * Sets {@code description}: the description, up to 5,000 characters.
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
     * Sets {@code address}: the street address of the venue.
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
     * Sets {@code country_code}: the ISO 3166-1 alpha-2 country code, for example {@code GB}.
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
     * Sets {@code starts_at}: the start, ISO 8601 UTC.
     *
     * @param startsAt the time, ISO 8601 UTC, for example {@code 2027-07-10T08:00:00Z}
     * @return this builder
     */
    public Builder startsAt(String startsAt) {
      return put("starts_at", startsAt);
    }

    /**
     * Sets {@code starts_at}: the start, ISO 8601 UTC.
     *
     * @param startsAt the time, sent in UTC to the second
     * @return this builder
     */
    public Builder startsAt(Instant startsAt) {
      return put("starts_at", startsAt);
    }

    /**
     * Sets {@code ends_at}: the end, ISO 8601 UTC.
     *
     * @param endsAt the time, ISO 8601 UTC, for example {@code 2027-07-10T08:00:00Z}
     * @return this builder
     */
    public Builder endsAt(String endsAt) {
      return put("ends_at", endsAt);
    }

    /**
     * Sets {@code ends_at}: the end, ISO 8601 UTC.
     *
     * @param endsAt the time, sent in UTC to the second
     * @return this builder
     */
    public Builder endsAt(Instant endsAt) {
      return put("ends_at", endsAt);
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
     * Sets {@code advertised_name}: the business name members see instead of your trading name.
     *
     * @param advertisedName the value
     * @return this builder
     */
    public Builder advertisedName(String advertisedName) {
      return put("advertised_name", advertisedName);
    }

    /**
     * Sets {@code price_min}: the lowest ticket price.
     *
     * @param priceMin the value
     * @return this builder
     */
    public Builder priceMin(BigDecimal priceMin) {
      return put("price_min", priceMin);
    }

    /**
     * Sets {@code price_min}: the lowest ticket price.
     *
     * @param priceMin the value
     * @return this builder
     */
    public Builder priceMin(double priceMin) {
      return decimal("price_min", priceMin);
    }

    /**
     * Sets {@code price_max}: the highest ticket price.
     *
     * @param priceMax the value
     * @return this builder
     */
    public Builder priceMax(BigDecimal priceMax) {
      return put("price_max", priceMax);
    }

    /**
     * Sets {@code price_max}: the highest ticket price.
     *
     * @param priceMax the value
     * @return this builder
     */
    public Builder priceMax(double priceMax) {
      return decimal("price_max", priceMax);
    }

    /**
     * Sets {@code currency}: the ticket price currency, from {@code GET /event-currencies}.
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
     * Sets {@code payment_method}: {@code B}, the balance. Filled in when left out.
     *
     * @param paymentMethod the value
     * @return this builder
     */
    public Builder paymentMethod(String paymentMethod) {
      return put("payment_method", paymentMethod);
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
     * Sets the {@code Idempotency-Key} header. When left out, a random UUID is generated for the
     * call and reused on every retry of it.
     *
     * @param idempotencyKey a unique value for this Offer or Event
     * @return this builder
     */
    public Builder idempotencyKey(String idempotencyKey) {
      this.idempotencyKey = idempotencyKey;
      return this;
    }

    /**
     * Sends {@code dry_run=true}: the API checks the request and answers what would happen, without
     * changing anything.
     *
     * @param dryRun {@code true} for a dry run
     * @return this builder
     */
    public Builder dryRun(boolean dryRun) {
      this.dryRun = dryRun;
      return this;
    }

    /**
     * Builds the parameters.
     *
     * @return the parameters
     */
    public PublishEventParams build() {
      return new PublishEventParams(this);
    }
  }
}
