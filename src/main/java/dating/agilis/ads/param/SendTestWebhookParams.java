package dating.agilis.ads.param;

/** Parameters for {@code POST /webhook-endpoints/{endpoint_id}/test}. */
public final class SendTestWebhookParams extends Params {

  private SendTestWebhookParams(Builder builder) {
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

  /** Builds {@link SendTestWebhookParams}. */
  public static final class Builder extends Params.Builder<Builder> {

    private Builder() {}

    @Override
    protected Builder self() {
      return this;
    }

    /**
     * Sets {@code type}: the event type to send.
     *
     * @param type the value
     * @return this builder
     */
    public Builder type(String type) {
      return put("type", type);
    }

    /**
     * Builds the parameters.
     *
     * @return the parameters
     */
    public SendTestWebhookParams build() {
      return new SendTestWebhookParams(this);
    }
  }
}
