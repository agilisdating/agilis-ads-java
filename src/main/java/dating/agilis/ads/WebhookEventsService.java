package dating.agilis.ads;

import dating.agilis.ads.model.Page;
import dating.agilis.ads.model.ResendResult;
import dating.agilis.ads.model.WebhookEvent;
import dating.agilis.ads.param.ListWebhookEventsParams;
import dating.agilis.ads.param.ResendWebhookEventParams;
import java.util.Map;

/** Webhook events of the last 30 days, in the key's mode: {@code client.webhookEvents()}. */
public final class WebhookEventsService {

  private final AgilisClient client;

  WebhookEventsService(AgilisClient client) {
    this.client = client;
  }

  /**
   * Lists one page of events ({@code GET /webhook-events}).
   *
   * @return the first page
   */
  public Page<WebhookEvent> list() {
    return list(ListWebhookEventsParams.builder().build());
  }

  /**
   * Lists one page of events ({@code GET /webhook-events}).
   *
   * @param params the filters and paging
   * @return the page
   */
  public Page<WebhookEvent> list(ListWebhookEventsParams params) {
    return page(params.toMap(), null);
  }

  /**
   * Lists every event, fetching pages as the iteration reaches them.
   *
   * @return the events
   */
  public AutoPagingIterable<WebhookEvent> listAutoPaging() {
    return listAutoPaging(ListWebhookEventsParams.builder().build());
  }

  /**
   * Lists every event that matches the filters, fetching pages as the iteration reaches them.
   *
   * @param params the filters; {@code cursor}, when set, is where the iteration starts
   * @return the events
   */
  public AutoPagingIterable<WebhookEvent> listAutoPaging(ListWebhookEventsParams params) {
    Map<String, Object> query = params.toMap();
    Object start = query.get("cursor");
    return new AutoPagingIterable<>(
        cursor -> page(query, cursor), start == null ? null : start.toString());
  }

  private Page<WebhookEvent> page(Map<String, Object> query, String cursor) {
    ApiRequest request = ApiRequest.of("GET", "/webhook-events").query(query);
    if (cursor != null) {
      request.query("cursor", cursor);
    }
    return client.requester().executePage(request, WebhookEvent.class);
  }

  /**
   * Returns one event with its deliveries ({@code GET /webhook-events/{webhook_event_id}}).
   *
   * @param webhookEventId the event id, {@code whevt_...}
   * @return the event
   */
  public WebhookEvent get(String webhookEventId) {
    return client
        .requester()
        .execute(
            ApiRequest.of("GET", "/webhook-events/" + ApiRequest.segment(webhookEventId)),
            WebhookEvent.class);
  }

  /**
   * Sends an event again to every endpoint that asked for its type.
   *
   * @param webhookEventId the event id
   * @return the new deliveries
   */
  public ResendResult resend(String webhookEventId) {
    return resend(webhookEventId, ResendWebhookEventParams.builder().build());
  }

  /**
   * Sends an event again ({@code POST /webhook-events/{webhook_event_id}/resend}).
   *
   * @param webhookEventId the event id
   * @param params optionally, the one endpoint to send it to
   * @return the new deliveries
   */
  public ResendResult resend(String webhookEventId, ResendWebhookEventParams params) {
    return client
        .requester()
        .execute(
            ApiRequest.of(
                    "POST", "/webhook-events/" + ApiRequest.segment(webhookEventId) + "/resend")
                .json(params.toMap()),
            ResendResult.class);
  }
}
