package dating.agilis.ads.param;

import java.util.List;

/** Parameters for {@code POST /webhook-endpoints}. */
public final class CreateWebhookEndpointParams extends Params {

  private CreateWebhookEndpointParams(Builder builder) {
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

  /** Builds {@link CreateWebhookEndpointParams}. */
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
     * Sets {@code enabled_events}: the event types to send, or {@code *} for all; see {@link
     * dating.agilis.ads.model.WebhookEventTypes}.
     *
     * @param enabledEvents the values
     * @return this builder
     */
    public Builder enabledEvents(String... enabledEvents) {
      return strings("enabled_events", List.of(enabledEvents));
    }

    /**
     * Sets {@code enabled_events}: the event types to send, or {@code *} for all; see {@link
     * dating.agilis.ads.model.WebhookEventTypes}.
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
     * Builds the parameters.
     *
     * @return the parameters
     */
    public CreateWebhookEndpointParams build() {
      return new CreateWebhookEndpointParams(this);
    }
  }
}
