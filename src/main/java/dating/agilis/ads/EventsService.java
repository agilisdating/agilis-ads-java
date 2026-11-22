package dating.agilis.ads;

import dating.agilis.ads.model.ApiMessage;
import dating.agilis.ads.model.Event;
import dating.agilis.ads.model.EventAnalytics;
import dating.agilis.ads.model.EventCancelled;
import dating.agilis.ads.model.EventDetail;
import dating.agilis.ads.model.EventEdited;
import dating.agilis.ads.model.EventPreview;
import dating.agilis.ads.model.EventPublished;
import dating.agilis.ads.model.EventRescheduled;
import dating.agilis.ads.model.Page;
import dating.agilis.ads.param.AnalyticsParams;
import dating.agilis.ads.param.DryRunParams;
import dating.agilis.ads.param.EditEventParams;
import dating.agilis.ads.param.ListEventsParams;
import dating.agilis.ads.param.PreviewEventParams;
import dating.agilis.ads.param.PublishEventParams;
import dating.agilis.ads.param.RescheduleEventParams;
import java.util.Map;

/** Events: {@code client.events()}. */
public final class EventsService {

  private final AgilisClient client;

  EventsService(AgilisClient client) {
    this.client = client;
  }

  /**
   * Previews an Event's category, fee and framed image without publishing it ({@code POST
   * /events/preview}).
   *
   * @param params the wording and, optionally, the image
   * @return the preview
   */
  public EventPreview preview(PreviewEventParams params) {
    ApiRequest request =
        ApiRequest.of("POST", "/events/preview").multipart(client.withEnvironment(params.toMap()));
    return client.requester().execute(request, EventPreview.class);
  }

  /**
   * Publishes an Event ({@code POST /events}), paid from the balance. An {@code Idempotency-Key} is
   * always sent: the one in the parameters, or a random UUID reused on every retry of this call.
   * {@code payment_method} defaults to {@code B}.
   *
   * @param params the Event
   * @return the new Event's id, charge and category, or what would happen for a dry run
   */
  public EventPublished publish(PublishEventParams params) {
    String key = AgilisClient.idempotencyKey(params.getIdempotencyKey());
    Map<String, Object> fields = client.withEnvironment(params.toMap());
    fields.putIfAbsent("payment_method", "B");
    ApiRequest request =
        ApiRequest.of("POST", "/events")
            .header("Idempotency-Key", key)
            .dryRun(params.isDryRun())
            .multipart(fields);
    return client.requester().execute(request, EventPublished.class);
  }

  /**
   * Lists one page of your Events ({@code GET /events}), in the key's mode.
   *
   * @return the first page
   */
  public Page<Event> list() {
    return list(ListEventsParams.builder().build());
  }

  /**
   * Lists one page of your Events ({@code GET /events}).
   *
   * @param params the filters and paging
   * @return the page
   */
  public Page<Event> list(ListEventsParams params) {
    return page(client.withEnvironment(params.toMap()), null);
  }

  /**
   * Lists every Event, fetching pages as the iteration reaches them.
   *
   * @return the Events
   */
  public AutoPagingIterable<Event> listAutoPaging() {
    return listAutoPaging(ListEventsParams.builder().build());
  }

  /**
   * Lists every Event that matches the filters, fetching pages as the iteration reaches them.
   *
   * @param params the filters; {@code cursor}, when set, is where the iteration starts
   * @return the Events
   */
  public AutoPagingIterable<Event> listAutoPaging(ListEventsParams params) {
    Map<String, Object> query = client.withEnvironment(params.toMap());
    Object start = query.get("cursor");
    return new AutoPagingIterable<>(
        cursor -> page(query, cursor), start == null ? null : start.toString());
  }

  private Page<Event> page(Map<String, Object> query, String cursor) {
    ApiRequest request = ApiRequest.of("GET", "/events").query(query);
    if (cursor != null) {
      request.query("cursor", cursor);
    }
    return client.requester().executePage(request, Event.class);
  }

  /**
   * Returns one Event with its management details ({@code GET /events/{event_id}}).
   *
   * @param eventId the Event id
   * @return the Event
   */
  public EventDetail get(long eventId) {
    return client
        .requester()
        .execute(ApiRequest.of("GET", "/events/" + eventId), EventDetail.class);
  }

  /**
   * Edits an Event ({@code PATCH /events/{event_id}}), free of charge. Send only the fields to
   * change. The request is always sent as a multipart {@code POST} with {@code
   * X-HTTP-Method-Override: PATCH}, with or without an image, so the API receives every field.
   *
   * @param eventId the Event id
   * @param params the fields to change
   * @return the result
   */
  public EventEdited edit(long eventId, EditEventParams params) {
    ApiRequest request =
        ApiRequest.of("POST", "/events/" + eventId)
            .header("X-HTTP-Method-Override", "PATCH")
            .multipart(params.toMap());
    return client.requester().execute(request, EventEdited.class);
  }

  /**
   * Deletes an Event ({@code DELETE /events/{event_id}}).
   *
   * @param eventId the Event id
   * @return the result
   */
  public ApiMessage delete(long eventId) {
    return delete(eventId, DryRunParams.builder().build());
  }

  /**
   * Deletes an Event ({@code DELETE /events/{event_id}}), or checks it could be deleted.
   *
   * @param eventId the Event id
   * @param params {@code dryRun} to check without deleting
   * @return the result
   */
  public ApiMessage delete(long eventId, DryRunParams params) {
    ApiRequest request = ApiRequest.of("DELETE", "/events/" + eventId).dryRun(params.isDryRun());
    return client.requester().execute(request, ApiMessage.class);
  }

  /**
   * Moves an Event to new dates ({@code POST /events/{event_id}/reschedule}).
   *
   * @param eventId the Event id
   * @param params the new start and end
   * @return the result
   */
  public EventRescheduled reschedule(long eventId, RescheduleEventParams params) {
    ApiRequest request =
        ApiRequest.of("POST", "/events/" + eventId + "/reschedule").json(params.toMap());
    return client.requester().execute(request, EventRescheduled.class);
  }

  /**
   * Cancels an Event ({@code POST /events/{event_id}/cancel}).
   *
   * @param eventId the Event id
   * @return the result
   */
  public EventCancelled cancel(long eventId) {
    return cancel(eventId, DryRunParams.builder().build());
  }

  /**
   * Cancels an Event ({@code POST /events/{event_id}/cancel}), or checks it could be cancelled.
   *
   * @param eventId the Event id
   * @param params {@code dryRun} to check without cancelling
   * @return the result
   */
  public EventCancelled cancel(long eventId, DryRunParams params) {
    ApiRequest request =
        ApiRequest.of("POST", "/events/" + eventId + "/cancel").dryRun(params.isDryRun());
    return client.requester().execute(request, EventCancelled.class);
  }

  /**
   * Returns an Event's results ({@code GET /events/{event_id}/analytics}).
   *
   * @param eventId the Event id
   * @param params the interval
   * @return the analytics
   */
  public EventAnalytics analytics(long eventId, AnalyticsParams params) {
    ApiRequest request =
        ApiRequest.of("GET", "/events/" + eventId + "/analytics").query(params.toMap());
    return client.requester().execute(request, EventAnalytics.class);
  }

  /**
   * Returns an Event's results ({@code GET /events/{event_id}/analytics}).
   *
   * @param eventId the Event id
   * @param interval {@code 24h}, {@code 7d}, {@code 30d}, {@code 60d}, {@code 90d} or {@code all}
   * @return the analytics
   */
  public EventAnalytics analytics(long eventId, String interval) {
    return analytics(eventId, AnalyticsParams.builder().interval(interval).build());
  }
}
