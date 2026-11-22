# Agilis Advertising API: Java library

The official Java library for the [Agilis Advertising REST API](https://www.agilis.dating/api-doc). Create and manage Offers and Events, read their results, and receive webhooks.

- Java 17 or later. HTTP goes through `java.net.http.HttpClient`; the only dependency is Jackson.
- API reference: [api-doc](https://www.agilis.dating/api-doc) · [OpenAPI](https://www.agilis.dating/openapi.json) · [AsyncAPI (webhooks)](https://www.agilis.dating/asyncapi.json) · [agilis-ads-api](https://github.com/agilisdating/agilis-ads-api)

## Install

Maven:

```xml
<dependency>
  <groupId>dating.agilis</groupId>
  <artifactId>agilis-ads</artifactId>
  <version>0.1.0</version>
</dependency>
```

Gradle:

```kotlin
implementation("dating.agilis:agilis-ads:0.1.0")
```

## Quick start in Test mode

Create a Test key in the Ad Portal under **Manage API Keys**, in Test mode. A `pk_test_` key works in Test mode: members never see what you create and nothing is charged.

```java
import dating.agilis.ads.AgilisClient;
import dating.agilis.ads.Mode;
import dating.agilis.ads.model.Category;
import dating.agilis.ads.model.PriceEstimate;
import dating.agilis.ads.param.EstimateOfferPriceParams;
import java.time.Duration;
import java.time.Instant;

AgilisClient client = AgilisClient.builder()
    .apiKey(System.getenv("AGILIS_API_KEY"))   // or leave out: AGILIS_API_KEY is read
    .build();

if (client.mode() != Mode.TEST) {
  throw new IllegalStateException("Use a Test key while trying things out");
}

for (Category category : client.categories().list().getCategories()) {
  System.out.println(category.getCategoryId() + " " + category.getCategoryName());
}

Instant start = Instant.now().plus(Duration.ofHours(1));
PriceEstimate estimate = client.offers().estimatePrice(
    EstimateOfferPriceParams.builder()
        .startDatetime(start)
        .endDatetime(start.plus(Duration.ofDays(2)))
        .location(51.5074, -0.1278)
        .radius(10)
        .build());
System.out.println(estimate.getEstimatedCost() + " " + estimate.getCurrency());
```

More in [`examples/`](examples): a Test-mode script and a webhook receiver.

## The client

```java
AgilisClient client = AgilisClient.builder()
    .apiKey("pk_test_...")
    .baseUrl("https://ads.agilis.dating")   // the default
    .timeout(Duration.ofSeconds(60))        // the default, per request
    .maxRetries(2)                          // the default; 0 turns retries off
    .httpClient(HttpClient.newBuilder().proxy(ProxySelector.getDefault()).build())  // optional
    .build();
```

| Option | Default | |
|---|---|---|
| `apiKey` | `AGILIS_API_KEY` from the environment | `pk_test_` or `pk_live_` |
| `baseUrl` | `https://ads.agilis.dating` | |
| `timeout` | 60 seconds | for each request |
| `maxRetries` | 2 | see [Retries](#idempotency-and-retries) |
| `httpClient` | a new `HttpClient` | your own `java.net.http.HttpClient` |
| `transport` | none | replaces the HTTP layer entirely, for example in tests |

**Mode.** The key decides it: `client.mode()` is `Mode.TEST` for a `pk_test_` key and `Mode.LIVE` for a `pk_live_` key. Operations that take `environment` fill it in from the key when you leave it out. With a Live key changes are real: members see them, and creating an Offer or publishing an Event is paid for.

The key is sent in the `x-api-key` header only. It is never logged, never part of a URL, and never included in an error message. A client is safe to share between threads.

## Resources and methods

Each method takes path parameters as arguments and everything else as one parameters object, built with the API's own fields (`ListOffersParams.builder().offerStatus("active").build()`). Every builder also has `put(name, value)` for a field this version of the library does not know.

| Resource | Method | HTTP |
|---|---|---|
| `client.categories()` | `list()` | `GET /categories` |
| `client.interests()` | `list()` | `GET /interests` |
| `client.offers()` | `estimatePrice(EstimateOfferPriceParams)` | `POST /offers/price-estimate` |
| | `create(CreateOfferParams)` | `POST /offers` (multipart) |
| | `list()`, `list(ListOffersParams)` | `GET /offers` |
| | `listAutoPaging()`, `listAutoPaging(ListOffersParams)` | every page of `GET /offers` |
| | `pause(offerId)` | `POST /offers/{offer_id}/pause` |
| | `resume(offerId)` | `POST /offers/{offer_id}/resume` |
| | `analytics(offerId, interval)`, `analytics(offerId, AnalyticsParams)` | `GET /offers/{offer_id}/analytics` |
| | `delete(offerId)`, `delete(offerId, DryRunParams)` | `DELETE /offers/{offer_id}` |
| `client.eventCategories()` | `list()` | `GET /event-categories` |
| `client.eventCurrencies()` | `list()` | `GET /event-currencies` |
| `client.eventConfig()` | `get()` | `GET /event-config` |
| `client.events()` | `preview(PreviewEventParams)` | `POST /events/preview` (multipart) |
| | `publish(PublishEventParams)` | `POST /events` (multipart) |
| | `list()`, `list(ListEventsParams)` | `GET /events` |
| | `listAutoPaging()`, `listAutoPaging(ListEventsParams)` | every page of `GET /events` |
| | `get(eventId)` | `GET /events/{event_id}` |
| | `edit(eventId, EditEventParams)` | `PATCH /events/{event_id}` (multipart, sent as `POST` with `X-HTTP-Method-Override: PATCH`) |
| | `delete(eventId)`, `delete(eventId, DryRunParams)` | `DELETE /events/{event_id}` |
| | `reschedule(eventId, RescheduleEventParams)` | `POST /events/{event_id}/reschedule` |
| | `cancel(eventId)`, `cancel(eventId, DryRunParams)` | `POST /events/{event_id}/cancel` |
| | `analytics(eventId, interval)`, `analytics(eventId, AnalyticsParams)` | `GET /events/{event_id}/analytics` |
| `client.webhookEndpoints()` | `list()` | `GET /webhook-endpoints` |
| | `create(CreateWebhookEndpointParams)` | `POST /webhook-endpoints` |
| | `get(endpointId)` | `GET /webhook-endpoints/{endpoint_id}` |
| | `update(endpointId, UpdateWebhookEndpointParams)` | `PATCH /webhook-endpoints/{endpoint_id}` |
| | `delete(endpointId)` | `DELETE /webhook-endpoints/{endpoint_id}` |
| | `rollSecret(endpointId)`, `rollSecret(endpointId, RollWebhookSecretParams)` | `POST /webhook-endpoints/{endpoint_id}/roll-secret` |
| | `sendTest(endpointId)`, `sendTest(endpointId, SendTestWebhookParams)` | `POST /webhook-endpoints/{endpoint_id}/test` |
| `client.webhookEvents()` | `list()`, `list(ListWebhookEventsParams)` | `GET /webhook-events` |
| | `listAutoPaging()`, `listAutoPaging(ListWebhookEventsParams)` | every page of `GET /webhook-events` |
| | `get(webhookEventId)` | `GET /webhook-events/{webhook_event_id}` |
| | `resend(webhookEventId)`, `resend(webhookEventId, ResendWebhookEventParams)` | `POST /webhook-events/{webhook_event_id}/resend` |
| `Webhooks` | `constructEvent(rawBody, signatureHeader, secret[, toleranceSeconds])` | verifies a received webhook; no request |

### Creating an Offer

```java
OfferCreated created = client.offers().create(
    CreateOfferParams.builder()
        .categoryId(1)
        .title("20% off summer sale")
        .description("Discount valid through July")
        .pushNotificationTitle("20% off this summer")
        .pushNotificationMessage("Show this at the till for 20% off everything.")
        .url("https://example.com/offer")
        .image(Path.of("offer.jpg"))          // or FileUpload.of(bytes, "offer.jpg") or FileUpload.of(stream, "offer.jpg")
        .location(51.5074, -0.1278)
        .radius(10)
        .gender("Male", "Female")             // sent as repeated gender[] parts
        .interests(3, 7)                      // sent as repeated interests[] parts
        .startDatetime(Instant.parse("2027-07-10T08:00:00Z"))
        .expiryDatetime("2027-07-12T20:30:00Z")
        .paymentMethod("B")
        .build());
System.out.println(created.getOfferId() + " " + created.getStatus());
```

An image's content type is detected from its file name. For Events, `crop(x, y, w)` sends `crop[x]`, `crop[y]` and `crop[w]`.

### Responses

Responses are typed (`Offer`, `Event`, `PriceEstimate` and so on). Fields this version does not know are kept: read them with `get("field_name")` or `getExtraFields()`, so a new API field never breaks your code. The HTTP status and headers stay reachable:

```java
CategoryList categories = client.categories().list();
String remaining = categories.getLastResponse().getHeader("X-RateLimit-Remaining");
```

## Pagination

`list` returns one page. `getData()` holds its items; `hasMore()` and `getNextCursor()` lead to the next.

```java
Page<Offer> page = client.offers().list(ListOffersParams.builder().limit(50).build());
```

`listAutoPaging` iterates over every item, fetching each page as the iteration reaches it and following `next_cursor` until `has_more` is false:

```java
for (Event event : client.events().listAutoPaging()) {
  System.out.println(event.getTitle());
}

long upcoming = client.events()
    .listAutoPaging(ListEventsParams.builder().eventStatus("upcoming").build())
    .stream()
    .count();
```

## Idempotency and retries

`offers().create` and `events().publish` always send an `Idempotency-Key`: yours, set with `idempotencyKey(...)`, or a random UUID generated once per call and reused on every retry of that call. Retrying with the same key never creates or charges twice.

`dryRun(true)` on `offers().create`, `events().publish`, and `DryRunParams` on `offers().delete`, `events().delete` and `events().cancel`, sends `dry_run=true`: the API checks the request and answers what would happen, without changing anything.

```java
client.events().cancel(3104, DryRunParams.builder().dryRun(true).build());
```

Requests are retried up to `maxRetries` times (2 by default):

- **Retried:** connection errors and timeouts; HTTP 429; and HTTP 500, 502, 503 and 504 for `GET` and `DELETE` requests and for `POST` requests that carry an `Idempotency-Key`.
- **Not retried:** other `POST` and `PATCH` requests on a 5xx, because the change may already have happened.
- **The wait:** the `Retry-After` header when present. Otherwise 0.5 seconds × 2^attempt with ±25% jitter, at most 8 seconds.

## Errors

Every error is an `AgilisException` (unchecked), carrying `getStatusCode()`, `getReturnCode()`, `getMessage()`, the parsed `getBody()` and `getHeaders()`.

| Type | When |
|---|---|
| `AuthenticationException` | 401 |
| `PermissionException` | 403 |
| `NotFoundException` | 404 |
| `ConflictException` | 409 |
| `InvalidRequestException` | 400, 422, and HTTP 200 with a `return_code` starting with `E` |
| `RateLimitException` | 429; `getRetryAfter()` |
| `ApiException` | 5xx and anything else |
| `ConnectionException` | no response (status 0) |
| `SignatureVerificationException` | `Webhooks.constructEvent`; `getReason()` |

Some refusals arrive as HTTP 200 with an `E` code, for example `E3` when the balance is too low for an Offer. These raise `InvalidRequestException` with status 200. `S1` and `S2` are both success.

```java
try {
  client.offers().create(params);
} catch (InvalidRequestException e) {
  System.out.println(e.getReturnCode() + ": " + e.getMessage());
} catch (RateLimitException e) {
  e.getRetryAfter().ifPresent(wait -> System.out.println("Wait " + wait.toSeconds() + " s"));
} catch (AgilisException e) {
  System.out.println("HTTP " + e.getStatusCode() + ": " + e.getMessage());
}
```

## Webhooks

Agilis signs every webhook with your endpoint's secret in the `Agilis-Signature` header. Verify it against the raw body, exactly as received, with `Webhooks.constructEvent`: it checks the signature in constant time, accepts any matching `v1` (there are two while a secret is being rolled), refuses a timestamp more than 300 seconds from now, and returns the parsed event.

A minimal handler with the JDK's own HTTP server:

```java
HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
server.createContext("/webhooks", exchange -> {
  byte[] body = exchange.getRequestBody().readAllBytes();
  String signature = exchange.getRequestHeaders().getFirst("Agilis-Signature");
  int status;
  try {
    WebhookEvent event = Webhooks.constructEvent(body, signature, secret, 300);
    // Answer within 10 seconds, then do the work. Use event.getId() to ignore repeats.
    switch (event.getType()) {
      case WebhookEventTypes.EVENT_PUBLISHED -> event.asEvent().ifPresent(e -> System.out.println(e.getTitle()));
      case WebhookEventTypes.OFFER_STARTED -> event.asOffer().ifPresent(o -> System.out.println(o.getTitle()));
      default -> System.out.println(event.getType());
    }
    status = 200;
  } catch (SignatureVerificationException e) {
    status = 400;
  }
  exchange.sendResponseHeaders(status, -1);
  exchange.close();
});
server.start();
```

`event.dataObject(SomeType.class)` converts `data.object` to any type; `event.getData().get("amount_charged")` reads the extra facts. In a framework such as Spring, read the body as a `String` or `byte[]` (not a parsed object) so the signature matches.

`Webhooks.signatureHeader(body, secret, timestamp)` builds a valid header, for testing your own handler.

## Development

```sh
./mvnw verify      # format check, unit tests and Javadoc
./mvnw spotless:apply
```

The unit tests fake the HTTP layer. The integration tests call the API in Test mode and run only with a Test key in `AGILIS_TEST_API_KEY`:

```sh
AGILIS_TEST_API_KEY=pk_test_... ./mvnw test -Dtest.groups=integration -Dtest.excludedGroups=
```

## Security

See [SECURITY.md](SECURITY.md) to report a problem or if a key is exposed.

## Licence

MIT. See [LICENSE](LICENSE).
