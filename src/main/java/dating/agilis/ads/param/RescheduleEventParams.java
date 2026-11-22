package dating.agilis.ads.param;

import java.time.Instant;

/** Parameters for {@code POST /events/{event_id}/reschedule}. */
public final class RescheduleEventParams extends Params {

  private RescheduleEventParams(Builder builder) {
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

  /** Builds {@link RescheduleEventParams}. */
  public static final class Builder extends Params.Builder<Builder> {

    private Builder() {}

    @Override
    protected Builder self() {
      return this;
    }

    /**
     * Sets {@code starts_at}: the new start, ISO 8601 UTC.
     *
     * @param startsAt the time, ISO 8601 UTC, for example {@code 2027-07-10T08:00:00Z}
     * @return this builder
     */
    public Builder startsAt(String startsAt) {
      return put("starts_at", startsAt);
    }

    /**
     * Sets {@code starts_at}: the new start, ISO 8601 UTC.
     *
     * @param startsAt the time, sent in UTC to the second
     * @return this builder
     */
    public Builder startsAt(Instant startsAt) {
      return put("starts_at", startsAt);
    }

    /**
     * Sets {@code ends_at}: the new end, ISO 8601 UTC.
     *
     * @param endsAt the time, ISO 8601 UTC, for example {@code 2027-07-10T08:00:00Z}
     * @return this builder
     */
    public Builder endsAt(String endsAt) {
      return put("ends_at", endsAt);
    }

    /**
     * Sets {@code ends_at}: the new end, ISO 8601 UTC.
     *
     * @param endsAt the time, sent in UTC to the second
     * @return this builder
     */
    public Builder endsAt(Instant endsAt) {
      return put("ends_at", endsAt);
    }

    /**
     * Builds the parameters.
     *
     * @return the parameters
     */
    public RescheduleEventParams build() {
      return new RescheduleEventParams(this);
    }
  }
}
