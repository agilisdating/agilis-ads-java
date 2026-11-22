package dating.agilis.ads;

import dating.agilis.ads.model.DeletedWebhookEndpoint;
import dating.agilis.ads.model.TestWebhookResult;
import dating.agilis.ads.model.WebhookEndpoint;
import dating.agilis.ads.model.WebhookEndpointList;
import dating.agilis.ads.param.CreateWebhookEndpointParams;
import dating.agilis.ads.param.RollWebhookSecretParams;
import dating.agilis.ads.param.SendTestWebhookParams;
import dating.agilis.ads.param.UpdateWebhookEndpointParams;

/** Webhook endpoints, in the key's mode: {@code client.webhookEndpoints()}. */
public final class WebhookEndpointsService {

  private final AgilisClient client;

  WebhookEndpointsService(AgilisClient client) {
    this.client = client;
  }

  private static String path(String endpointId) {
    return "/webhook-endpoints/" + ApiRequest.segment(endpointId);
  }

  /**
   * Lists the endpoints ({@code GET /webhook-endpoints}).
   *
   * @return the endpoints
   */
  public WebhookEndpointList list() {
    return client
        .requester()
        .execute(ApiRequest.of("GET", "/webhook-endpoints"), WebhookEndpointList.class);
  }

  /**
   * Adds an endpoint ({@code POST /webhook-endpoints}). The response holds the signing secret,
   * once: store it safely.
   *
   * @param params the URL and event types
   * @return the endpoint, with its {@code secret}
   */
  public WebhookEndpoint create(CreateWebhookEndpointParams params) {
    return client
        .requester()
        .execute(
            ApiRequest.of("POST", "/webhook-endpoints").json(params.toMap()),
            WebhookEndpoint.class);
  }

  /**
   * Returns one endpoint ({@code GET /webhook-endpoints/{endpoint_id}}).
   *
   * @param endpointId the endpoint id
   * @return the endpoint
   */
  public WebhookEndpoint get(String endpointId) {
    return client
        .requester()
        .execute(ApiRequest.of("GET", path(endpointId)), WebhookEndpoint.class);
  }

  /**
   * Changes an endpoint ({@code PATCH /webhook-endpoints/{endpoint_id}}).
   *
   * @param endpointId the endpoint id
   * @param params the fields to change
   * @return the endpoint
   */
  public WebhookEndpoint update(String endpointId, UpdateWebhookEndpointParams params) {
    return client
        .requester()
        .execute(
            ApiRequest.of("PATCH", path(endpointId)).json(params.toMap()), WebhookEndpoint.class);
  }

  /**
   * Deletes an endpoint ({@code DELETE /webhook-endpoints/{endpoint_id}}).
   *
   * @param endpointId the endpoint id
   * @return the result
   */
  public DeletedWebhookEndpoint delete(String endpointId) {
    return client
        .requester()
        .execute(ApiRequest.of("DELETE", path(endpointId)), DeletedWebhookEndpoint.class);
  }

  /**
   * Replaces an endpoint's signing secret, with the old one expiring at once.
   *
   * @param endpointId the endpoint id
   * @return the endpoint, with its new {@code secret}
   */
  public WebhookEndpoint rollSecret(String endpointId) {
    return rollSecret(endpointId, RollWebhookSecretParams.builder().build());
  }

  /**
   * Replaces an endpoint's signing secret ({@code POST
   * /webhook-endpoints/{endpoint_id}/roll-secret}). Until the old secret expires, events are signed
   * with both.
   *
   * @param endpointId the endpoint id
   * @param params when the old secret expires
   * @return the endpoint, with its new {@code secret}
   */
  public WebhookEndpoint rollSecret(String endpointId, RollWebhookSecretParams params) {
    return client
        .requester()
        .execute(
            ApiRequest.of("POST", path(endpointId) + "/roll-secret").json(params.toMap()),
            WebhookEndpoint.class);
  }

  /**
   * Sends a test event to an endpoint.
   *
   * @param endpointId the endpoint id
   * @return the delivery
   */
  public TestWebhookResult sendTest(String endpointId) {
    return sendTest(endpointId, SendTestWebhookParams.builder().build());
  }

  /**
   * Sends a test event to an endpoint ({@code POST /webhook-endpoints/{endpoint_id}/test}).
   *
   * @param endpointId the endpoint id
   * @param params the event type to send
   * @return the delivery
   */
  public TestWebhookResult sendTest(String endpointId, SendTestWebhookParams params) {
    return client
        .requester()
        .execute(
            ApiRequest.of("POST", path(endpointId) + "/test").json(params.toMap()),
            TestWebhookResult.class);
  }
}
