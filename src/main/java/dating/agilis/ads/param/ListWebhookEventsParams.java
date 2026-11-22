package dating.agilis.ads.param;

/** Parameters for {@code GET /webhook-events}. */
public final class ListWebhookEventsParams extends Params {

  private ListWebhookEventsParams(Builder builder) {
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

  /** Builds {@link ListWebhookEventsParams}. */
  public static final class Builder extends Params.Builder<Builder> {

    private Builder() {}

    @Override
    protected Builder self() {
      return this;
    }

    /**
     * Sets {@code type}: only events of this type.
     *
     * @param type the value
     * @return this builder
     */
    public Builder type(String type) {
      return put("type", type);
    }

    /**
     * Sets {@code delivery_success}: only events whose delivery did, or did not, succeed.
     *
     * @param deliverySuccess the value
     * @return this builder
     */
    public Builder deliverySuccess(boolean deliverySuccess) {
      return put("delivery_success", deliverySuccess);
    }

    /**
     * Sets {@code limit}: the page size.
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
     * Builds the parameters.
     *
     * @return the parameters
     */
    public ListWebhookEventsParams build() {
      return new ListWebhookEventsParams(this);
    }
  }
}
