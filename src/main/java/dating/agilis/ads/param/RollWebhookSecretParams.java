package dating.agilis.ads.param;

/** Parameters for {@code POST /webhook-endpoints/{endpoint_id}/roll-secret}. */
public final class RollWebhookSecretParams extends Params {

  private RollWebhookSecretParams(Builder builder) {
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

  /** Builds {@link RollWebhookSecretParams}. */
  public static final class Builder extends Params.Builder<Builder> {

    private Builder() {}

    @Override
    protected Builder self() {
      return this;
    }

    /**
     * Sets {@code expires_in}: when the old secret stops working: {@code now}, {@code 1h}, {@code
     * 24h} or {@code 7d}.
     *
     * @param expiresIn the value
     * @return this builder
     */
    public Builder expiresIn(String expiresIn) {
      return put("expires_in", expiresIn);
    }

    /**
     * Builds the parameters.
     *
     * @return the parameters
     */
    public RollWebhookSecretParams build() {
      return new RollWebhookSecretParams(this);
    }
  }
}
