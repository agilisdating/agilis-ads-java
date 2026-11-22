package dating.agilis.ads.param;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

/** Parameters for {@code POST /offers}, sent as multipart form data. */
public final class CreateOfferParams extends Params {

  private final String idempotencyKey;
  private final boolean dryRun;

  private CreateOfferParams(Builder builder) {
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

  /** Builds {@link CreateOfferParams}. */
  public static final class Builder extends Params.Builder<Builder> {

    private String idempotencyKey;
    private boolean dryRun;

    private Builder() {}

    @Override
    protected Builder self() {
      return this;
    }

    /**
     * Sets {@code category_id}: a category id from {@code GET /categories}.
     *
     * @param categoryId the value
     * @return this builder
     */
    public Builder categoryId(long categoryId) {
      return put("category_id", categoryId);
    }

    /**
     * Sets {@code title}: the title, up to 40 characters.
     *
     * @param title the value
     * @return this builder
     */
    public Builder title(String title) {
      return put("title", title);
    }

    /**
     * Sets {@code description}: the description, up to 500 characters.
     *
     * @param description the value
     * @return this builder
     */
    public Builder description(String description) {
      return put("description", description);
    }

    /**
     * Sets {@code terms_and_conditions}: the terms and conditions.
     *
     * @param termsAndConditions the value
     * @return this builder
     */
    public Builder termsAndConditions(String termsAndConditions) {
      return put("terms_and_conditions", termsAndConditions);
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
     * Sets {@code push_notification_title}: the notification title, 5 to 50 characters.
     *
     * @param pushNotificationTitle the value
     * @return this builder
     */
    public Builder pushNotificationTitle(String pushNotificationTitle) {
      return put("push_notification_title", pushNotificationTitle);
    }

    /**
     * Sets {@code push_notification_message}: the notification message, 5 to 150 characters.
     *
     * @param pushNotificationMessage the value
     * @return this builder
     */
    public Builder pushNotificationMessage(String pushNotificationMessage) {
      return put("push_notification_message", pushNotificationMessage);
    }

    /**
     * Sets {@code button_preset}: {@code claim_offer}, {@code visit_website}, {@code install_app},
     * {@code book_now}, {@code get_directions} or {@code custom}.
     *
     * @param buttonPreset the value
     * @return this builder
     */
    public Builder buttonPreset(String buttonPreset) {
      return put("button_preset", buttonPreset);
    }

    /**
     * Sets {@code button_text}: the button text when the preset is {@code custom}.
     *
     * @param buttonText the value
     * @return this builder
     */
    public Builder buttonText(String buttonText) {
      return put("button_text", buttonText);
    }

    /**
     * Sets {@code url}: your Offer page.
     *
     * @param url the value
     * @return this builder
     */
    public Builder url(String url) {
      return put("url", url);
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
     * Sets {@code radius}: the radius in miles, 5 to 30.
     *
     * @param radius the value
     * @return this builder
     */
    public Builder radius(int radius) {
      return put("radius", radius);
    }

    /**
     * Sets {@code gender}: one or more of {@code Male}, {@code Female}, {@code Non-binary}, {@code
     * All}.
     *
     * @param gender the values
     * @return this builder
     */
    public Builder gender(String... gender) {
      return strings("gender", List.of(gender));
    }

    /**
     * Sets {@code gender}: one or more of {@code Male}, {@code Female}, {@code Non-binary}, {@code
     * All}.
     *
     * @param gender the values
     * @return this builder
     */
    public Builder gender(List<String> gender) {
      return strings("gender", gender);
    }

    /**
     * Sets {@code interests}: interest ids from {@code GET /interests}.
     *
     * @param interests the ids
     * @return this builder
     */
    public Builder interests(long... interests) {
      return longs("interests", interests);
    }

    /**
     * Sets {@code interests}: interest ids from {@code GET /interests}.
     *
     * @param interests the ids
     * @return this builder
     */
    public Builder interests(List<Long> interests) {
      return put("interests", interests == null ? null : List.copyOf(interests));
    }

    /**
     * Sets {@code start_datetime}: the start, ISO 8601 UTC.
     *
     * @param startDatetime the time, ISO 8601 UTC, for example {@code 2027-07-10T08:00:00Z}
     * @return this builder
     */
    public Builder startDatetime(String startDatetime) {
      return put("start_datetime", startDatetime);
    }

    /**
     * Sets {@code start_datetime}: the start, ISO 8601 UTC.
     *
     * @param startDatetime the time, sent in UTC to the second
     * @return this builder
     */
    public Builder startDatetime(Instant startDatetime) {
      return put("start_datetime", startDatetime);
    }

    /**
     * Sets {@code expiry_datetime}: the end, ISO 8601 UTC.
     *
     * @param expiryDatetime the time, ISO 8601 UTC, for example {@code 2027-07-10T08:00:00Z}
     * @return this builder
     */
    public Builder expiryDatetime(String expiryDatetime) {
      return put("expiry_datetime", expiryDatetime);
    }

    /**
     * Sets {@code expiry_datetime}: the end, ISO 8601 UTC.
     *
     * @param expiryDatetime the time, sent in UTC to the second
     * @return this builder
     */
    public Builder expiryDatetime(Instant expiryDatetime) {
      return put("expiry_datetime", expiryDatetime);
    }

    /**
     * Sets {@code payment_method}: {@code E} for a Stripe payment link, {@code B} for the balance.
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
    public CreateOfferParams build() {
      return new CreateOfferParams(this);
    }
  }
}
