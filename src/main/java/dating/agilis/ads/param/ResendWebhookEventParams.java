package dating.agilis.ads.param;

/** Parameters for {@code POST /webhook-events/{webhook_event_id}/resend}. */
public final class ResendWebhookEventParams extends Params {

  private ResendWebhookEventParams(Builder builder) {
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

  /** Builds {@link ResendWebhookEventParams}. */
  public static final class Builder extends Params.Builder<Builder> {

    private Builder() {}

    @Override
    protected Builder self() {
      return this;
    }

    /**
     * Sets {@code endpoint_id}: only resend to this endpoint.
     *
     * @param endpointId the value
     * @return this builder
     */
    public Builder endpointId(String endpointId) {
      return put("endpoint_id", endpointId);
    }

    /**
     * Builds the parameters.
     *
     * @return the parameters
     */
    public ResendWebhookEventParams build() {
      return new ResendWebhookEventParams(this);
    }
  }
}
