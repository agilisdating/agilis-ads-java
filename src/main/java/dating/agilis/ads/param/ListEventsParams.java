package dating.agilis.ads.param;

import java.time.Instant;

/** Parameters for {@code GET /events}. */
public final class ListEventsParams extends Params {

  private ListEventsParams(Builder builder) {
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

  /** Builds {@link ListEventsParams}. */
  public static final class Builder extends Params.Builder<Builder> {

    private Builder() {}

    @Override
    protected Builder self() {
      return this;
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
     * Sets {@code limit}: the page size, up to 100.
     *
     * @param limit the value
     * @return this builder
     */
    public Builder limit(int limit) {
      return put("limit", limit);
    }

    /**
     * Sets {@code cursor}: the {@code next_cursor} of the previous page.
     *
     * @param cursor the value
     * @return this builder
     */
    public Builder cursor(String cursor) {
      return put("cursor", cursor);
    }

    /**
     * Sets {@code event_status}: {@code upcoming}, {@code scheduled}, {@code rescheduled}, {@code
     * cancelled} or {@code ended}.
     *
     * @param eventStatus the value
     * @return this builder
     */
    public Builder eventStatus(String eventStatus) {
      return put("event_status", eventStatus);
    }

    /**
     * Sets {@code owner_user_id}: only Events published by this user.
     *
     * @param ownerUserId the value
     * @return this builder
     */
    public Builder ownerUserId(long ownerUserId) {
      return put("owner_user_id", ownerUserId);
    }

    /**
     * Sets {@code created_after}: only Events created at or after this time.
     *
     * @param createdAfter the time, ISO 8601 UTC, for example {@code 2027-07-10T08:00:00Z}
     * @return this builder
     */
    public Builder createdAfter(String createdAfter) {
      return put("created_after", createdAfter);
    }

    /**
     * Sets {@code created_after}: only Events created at or after this time.
     *
     * @param createdAfter the time, sent in UTC to the second
     * @return this builder
     */
    public Builder createdAfter(Instant createdAfter) {
      return put("created_after", createdAfter);
    }

    /**
     * Sets {@code created_before}: only Events created at or before this time.
     *
     * @param createdBefore the time, ISO 8601 UTC, for example {@code 2027-07-10T08:00:00Z}
     * @return this builder
     */
    public Builder createdBefore(String createdBefore) {
      return put("created_before", createdBefore);
    }

    /**
     * Sets {@code created_before}: only Events created at or before this time.
     *
     * @param createdBefore the time, sent in UTC to the second
     * @return this builder
     */
    public Builder createdBefore(Instant createdBefore) {
      return put("created_before", createdBefore);
    }

    /**
     * Builds the parameters.
     *
     * @return the parameters
     */
    public ListEventsParams build() {
      return new ListEventsParams(this);
    }
  }
}
