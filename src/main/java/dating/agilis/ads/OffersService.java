package dating.agilis.ads;

import dating.agilis.ads.model.ApiMessage;
import dating.agilis.ads.model.Offer;
import dating.agilis.ads.model.OfferAnalytics;
import dating.agilis.ads.model.OfferCreated;
import dating.agilis.ads.model.Page;
import dating.agilis.ads.model.PriceEstimate;
import dating.agilis.ads.param.AnalyticsParams;
import dating.agilis.ads.param.CreateOfferParams;
import dating.agilis.ads.param.DryRunParams;
import dating.agilis.ads.param.EstimateOfferPriceParams;
import dating.agilis.ads.param.ListOffersParams;
import java.util.Map;

/** Offers: {@code client.offers()}. */
public final class OffersService {

  private final AgilisClient client;

  OffersService(AgilisClient client) {
    this.client = client;
  }

  /**
   * Estimates what an Offer will cost ({@code POST /offers/price-estimate}).
   *
   * @param params the targeting and dates
   * @return the estimate
   */
  public PriceEstimate estimatePrice(EstimateOfferPriceParams params) {
    Map<String, Object> body = client.withEnvironment(params.toMap());
    return client
        .requester()
        .execute(ApiRequest.of("POST", "/offers/price-estimate").json(body), PriceEstimate.class);
  }

  /**
   * Creates an Offer ({@code POST /offers}). An {@code Idempotency-Key} is always sent: the one in
   * the parameters, or a random UUID reused on every retry of this call.
   *
   * @param params the Offer, including its image
   * @return the new Offer's id and status, or what would happen for a dry run
   */
  public OfferCreated create(CreateOfferParams params) {
    String key = AgilisClient.idempotencyKey(params.getIdempotencyKey());
    ApiRequest request =
        ApiRequest.of("POST", "/offers")
            .header("Idempotency-Key", key)
            .dryRun(params.isDryRun())
            .multipart(client.withEnvironment(params.toMap()));
    return client.requester().execute(request, OfferCreated.class);
  }

  /**
   * Lists one page of your Offers ({@code GET /offers}), in the key's mode.
   *
   * @return the first page
   */
  public Page<Offer> list() {
    return list(ListOffersParams.builder().build());
  }

  /**
   * Lists one page of your Offers ({@code GET /offers}).
   *
   * @param params the filters and paging
   * @return the page
   */
  public Page<Offer> list(ListOffersParams params) {
    return page(client.withEnvironment(params.toMap()), null);
  }

  /**
   * Lists every Offer, fetching pages as the iteration reaches them.
   *
   * @return the Offers
   */
  public AutoPagingIterable<Offer> listAutoPaging() {
    return listAutoPaging(ListOffersParams.builder().build());
  }

  /**
   * Lists every Offer that matches the filters, fetching pages as the iteration reaches them.
   *
   * @param params the filters; {@code cursor}, when set, is where the iteration starts
   * @return the Offers
   */
  public AutoPagingIterable<Offer> listAutoPaging(ListOffersParams params) {
    Map<String, Object> query = client.withEnvironment(params.toMap());
    Object start = query.get("cursor");
    return new AutoPagingIterable<>(
        cursor -> page(query, cursor), start == null ? null : start.toString());
  }

  private Page<Offer> page(Map<String, Object> query, String cursor) {
    ApiRequest request = ApiRequest.of("GET", "/offers").query(query);
    if (cursor != null) {
      request.query("cursor", cursor);
    }
    return client.requester().executePage(request, Offer.class);
  }

  /**
   * Pauses an Offer ({@code POST /offers/{offer_id}/pause}).
   *
   * @param offerId the Offer id
   * @return the result
   */
  public ApiMessage pause(long offerId) {
    return client
        .requester()
        .execute(ApiRequest.of("POST", "/offers/" + offerId + "/pause"), ApiMessage.class);
  }

  /**
   * Resumes a paused Offer ({@code POST /offers/{offer_id}/resume}).
   *
   * @param offerId the Offer id
   * @return the result; {@code S2} is a success too
   */
  public ApiMessage resume(long offerId) {
    return client
        .requester()
        .execute(ApiRequest.of("POST", "/offers/" + offerId + "/resume"), ApiMessage.class);
  }

  /**
   * Returns an Offer's views and clicks ({@code GET /offers/{offer_id}/analytics}).
   *
   * @param offerId the Offer id
   * @param params the interval
   * @return the analytics
   */
  public OfferAnalytics analytics(long offerId, AnalyticsParams params) {
    ApiRequest request =
        ApiRequest.of("GET", "/offers/" + offerId + "/analytics").query(params.toMap());
    return client.requester().execute(request, OfferAnalytics.class);
  }

  /**
   * Returns an Offer's views and clicks ({@code GET /offers/{offer_id}/analytics}).
   *
   * @param offerId the Offer id
   * @param interval {@code 24h}, {@code 7d}, {@code 30d}, {@code 60d}, {@code 90d} or {@code all}
   * @return the analytics
   */
  public OfferAnalytics analytics(long offerId, String interval) {
    return analytics(offerId, AnalyticsParams.builder().interval(interval).build());
  }

  /**
   * Deletes an Offer ({@code DELETE /offers/{offer_id}}).
   *
   * @param offerId the Offer id
   * @return the result
   */
  public ApiMessage delete(long offerId) {
    return delete(offerId, DryRunParams.builder().build());
  }

  /**
   * Deletes an Offer ({@code DELETE /offers/{offer_id}}), or checks it could be deleted.
   *
   * @param offerId the Offer id
   * @param params {@code dryRun} to check without deleting
   * @return the result
   */
  public ApiMessage delete(long offerId, DryRunParams params) {
    ApiRequest request = ApiRequest.of("DELETE", "/offers/" + offerId).dryRun(params.isDryRun());
    return client.requester().execute(request, ApiMessage.class);
  }
}
