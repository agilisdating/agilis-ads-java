package dating.agilis.ads;

import dating.agilis.ads.model.EventConfig;

/** The Event settings for your organisation and mode: {@code client.eventConfig()}. */
public final class EventConfigService {

  private final AgilisClient client;

  EventConfigService(AgilisClient client) {
    this.client = client;
  }

  /**
   * Returns the listing fee, balance and image rules ({@code GET /event-config}).
   *
   * @return the configuration
   */
  public EventConfig get() {
    return client.requester().execute(ApiRequest.of("GET", "/event-config"), EventConfig.class);
  }
}
