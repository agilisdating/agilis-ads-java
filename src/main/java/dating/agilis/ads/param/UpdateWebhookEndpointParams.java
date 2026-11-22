package dating.agilis.ads.param;

import java.util.List;

/** Parameters for {@code PATCH /webhook-endpoints/{endpoint_id}}. */
public final class UpdateWebhookEndpointParams extends Params {

  private UpdateWebhookEndpointParams(Builder builder) {
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

  /** Builds {@link UpdateWebhookEndpointParams}. */
  public static final class Builder extends Params.Builder<Builder> {

    private Builder() {}

    @Override
    protected Builder self() {
      return this;
    }

    /**
     * Sets {@code url}: the HTTPS URL to send events to.
     *
     * @param url the value
     * @return this builder
     */
    public Builder url(String url) {
      return put("url", url);
    }

    /**
     * Sets {@code enabled_events}: the event types to send, or {@code *} for all.
     *
     * @param enabledEvents the values
     * @return this builder
     */
    public Builder enabledEvents(String... enabledEvents) {
      return strings("enabled_events", List.of(enabledEvents));
    }

    /**
     * Sets {@code enabled_events}: the event types to send, or {@code *} for all.
     *
     * @param enabledEvents the values
     * @return this builder
     */
    public Builder enabledEvents(List<String> enabledEvents) {
      return strings("enabled_events", enabledEvents);
    }

    /**
     * Sets {@code description}: a description.
     *
     * @param description the value
     * @return this builder
     */
    public Builder description(String description) {
      return put("description", description);
    }

    /**
     * Sets {@code status}: {@code enabled} or {@code disabled}.
     *
     * @param status the value
     * @return this builder
     */
    public Builder status(String status) {
      return put("status", status);
    }

    /**
     * Builds the parameters.
     *
     * @return the parameters
     */
    public UpdateWebhookEndpointParams build() {
      return new UpdateWebhookEndpointParams(this);
    }
  }
}
