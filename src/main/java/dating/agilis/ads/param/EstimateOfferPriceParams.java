package dating.agilis.ads.param;

import java.time.Instant;
import java.util.List;

/** Parameters for {@code POST /offers/price-estimate}. */
public final class EstimateOfferPriceParams extends Params {

  private EstimateOfferPriceParams(Builder builder) {
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

  /** Builds {@link EstimateOfferPriceParams}. */
  public static final class Builder extends Params.Builder<Builder> {

    private Builder() {}

    @Override
    protected Builder self() {
      return this;
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
     * Sets {@code end_datetime}: the end, ISO 8601 UTC.
     *
     * @param endDatetime the time, ISO 8601 UTC, for example {@code 2027-07-10T08:00:00Z}
     * @return this builder
     */
    public Builder endDatetime(String endDatetime) {
      return put("end_datetime", endDatetime);
    }

    /**
     * Sets {@code end_datetime}: the end, ISO 8601 UTC.
     *
     * @param endDatetime the time, sent in UTC to the second
     * @return this builder
     */
    public Builder endDatetime(Instant endDatetime) {
      return put("end_datetime", endDatetime);
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
    public EstimateOfferPriceParams build() {
      return new EstimateOfferPriceParams(this);
    }
  }
}
