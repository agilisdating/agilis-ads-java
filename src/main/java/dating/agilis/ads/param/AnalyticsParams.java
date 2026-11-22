package dating.agilis.ads.param;

/** Parameters for the Offer and Event analytics operations. */
public final class AnalyticsParams extends Params {

  private AnalyticsParams(Builder builder) {
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

  /** Builds {@link AnalyticsParams}. */
  public static final class Builder extends Params.Builder<Builder> {

    private Builder() {}

    @Override
    protected Builder self() {
      return this;
    }

    /**
     * Sets {@code interval}: {@code 24h}, {@code 7d}, {@code 30d}, {@code 60d}, {@code 90d} or
     * {@code all}.
     *
     * @param interval the value
     * @return this builder
     */
    public Builder interval(String interval) {
      return put("interval", interval);
    }

    /**
     * Builds the parameters.
     *
     * @return the parameters
     */
    public AnalyticsParams build() {
      return new AnalyticsParams(this);
    }
  }
}
