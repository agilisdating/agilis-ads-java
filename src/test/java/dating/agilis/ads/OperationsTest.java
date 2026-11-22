package dating.agilis.ads;

import static dating.agilis.ads.MultipartParser.names;
import static dating.agilis.ads.MultipartParser.parse;
import static dating.agilis.ads.MultipartParser.value;
import static dating.agilis.ads.MultipartParser.values;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import dating.agilis.ads.MultipartParser.Part;
import dating.agilis.ads.http.TransportRequest;
import dating.agilis.ads.internal.Json;
import dating.agilis.ads.model.ApiMessage;
import dating.agilis.ads.model.CategoryList;
import dating.agilis.ads.model.EventConfig;
import dating.agilis.ads.model.EventDetail;
import dating.agilis.ads.model.EventPublished;
import dating.agilis.ads.model.OfferCreated;
import dating.agilis.ads.model.PriceEstimate;
import dating.agilis.ads.model.WebhookEndpoint;
import dating.agilis.ads.param.AnalyticsParams;
import dating.agilis.ads.param.CreateOfferParams;
import dating.agilis.ads.param.CreateWebhookEndpointParams;
import dating.agilis.ads.param.DryRunParams;
import dating.agilis.ads.param.EditEventParams;
import dating.agilis.ads.param.EstimateOfferPriceParams;
import dating.agilis.ads.param.FileUpload;
import dating.agilis.ads.param.ListEventsParams;
import dating.agilis.ads.param.ListOffersParams;
import dating.agilis.ads.param.ListWebhookEventsParams;
import dating.agilis.ads.param.PreviewEventParams;
import dating.agilis.ads.param.PublishEventParams;
import dating.agilis.ads.param.RescheduleEventParams;
import dating.agilis.ads.param.ResendWebhookEventParams;
import dating.agilis.ads.param.RollWebhookSecretParams;
import dating.agilis.ads.param.SendTestWebhookParams;
import dating.agilis.ads.param.UpdateWebhookEndpointParams;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class OperationsTest {

  private final FakeTransport http = new FakeTransport();
  private final AgilisClient client = http.client();

  private static byte[] sampleImage() throws IOException {
    try (InputStream in = OperationsTest.class.getResourceAsStream("/sample-offer.png")) {
      return in.readAllBytes();
    }
  }

  private void assertRequest(String method, String pathAndQuery) {
    TransportRequest r = http.last();
    assertEquals(method, r.method());
    String actual =
        r.uri().getRawPath() + (r.uri().getRawQuery() == null ? "" : "?" + r.uri().getRawQuery());
    assertEquals(pathAndQuery, actual);
    assertEquals("https", r.uri().getScheme());
    assertEquals("ads.agilis.dating", r.uri().getHost());
  }

  private JsonNode jsonBody() throws IOException {
    assertEquals("application/json", http.last().header("Content-Type"));
    return Json.mapper().readTree(http.last().body());
  }

  private void assertNoBody() {
    assertNull(http.last().body());
    assertNull(http.last().header("Content-Type"));
  }

  @Test
  void listCategories() {
    http.ok("{\"categories\":[{\"category_id\":1,\"category_name\":\"Food & Drink\"}]}");
    CategoryList list = client.categories().list();
    assertRequest("GET", "/categories");
    assertNoBody();
    assertEquals(1L, list.getCategories().get(0).getCategoryId());
    assertEquals("Food & Drink", list.getCategories().get(0).getCategoryName());
  }

  @Test
  void listInterests() {
    http.ok("{\"interests\":[{\"interest_id\":3,\"interest_name\":\"Music\"}]}");
    assertEquals("Music", client.interests().list().getInterests().get(0).getInterestName());
    assertRequest("GET", "/interests");
  }

  @Test
  void estimateOfferPrice() throws IOException {
    http.ok(
        "{\"return_code\":\"S1\",\"price_estimate\":480,\"matching_users\":10,"
            + "\"estimated_cost\":4.8,\"currency\":\"GBP\"}");
    PriceEstimate estimate =
        client
            .offers()
            .estimatePrice(
                EstimateOfferPriceParams.builder()
                    .startDatetime("2027-07-10T08:00:00Z")
                    .endDatetime(Instant.parse("2027-07-12T20:30:00.250Z"))
                    .location(51.5074, -0.1278)
                    .radius(10)
                    .gender("Male", "Female")
                    .interests(3, 7)
                    .build());
    assertRequest("POST", "/offers/price-estimate");
    assertNull(http.last().header("Idempotency-Key"));
    JsonNode body = jsonBody();
    assertEquals("2027-07-10T08:00:00Z", body.get("start_datetime").asText());
    assertEquals("2027-07-12T20:30:00Z", body.get("end_datetime").asText());
    assertEquals(51.5074, body.get("location").get(0).asDouble());
    assertEquals(-0.1278, body.get("location").get(1).asDouble());
    assertEquals(10, body.get("radius").asInt());
    assertEquals("Female", body.get("gender").get(1).asText());
    assertEquals(7, body.get("interests").get(1).asInt());
    assertEquals("TEST", body.get("environment").asText());
    assertEquals(new BigDecimal("4.8"), estimate.getEstimatedCost());
    assertEquals(new BigDecimal("480"), estimate.getPriceEstimate());
  }

  @Test
  void createOffer() throws IOException {
    http.ok(
        "{\"return_code\":\"S1\",\"offer_id\":501,\"status\":\"Awaiting Payment\","
            + "\"payment_url\":\"https://checkout.example/x\",\"currency\":\"GBP\"}");
    byte[] image = sampleImage();
    OfferCreated created =
        client
            .offers()
            .create(
                CreateOfferParams.builder()
                    .categoryId(1)
                    .title("20% off summer sale")
                    .description("Discount valid through July")
                    .termsAndConditions("Valid online only.")
                    .pushNotificationTitle("20% off this summer")
                    .pushNotificationMessage("Show this at the till for 20% off.")
                    .buttonPreset("book_now")
                    .url("https://advertiseroffer.com")
                    .image(FileUpload.of(image, "offer.png"))
                    .location(51.5074, -0.1278)
                    .radius(10)
                    .gender("Male", "Female")
                    .interests(3, 7)
                    .startDatetime("2027-07-10T08:00:00Z")
                    .expiryDatetime("2027-07-12T20:30:00Z")
                    .paymentMethod("E")
                    .build());
    assertRequest("POST", "/offers");
    String key = http.last().header("Idempotency-Key");
    assertNotNull(key);
    assertEquals(4, java.util.UUID.fromString(key).version());
    assertTrue(http.last().header("Content-Type").startsWith("multipart/form-data; boundary="));
    List<Part> parts = parse(http.last());
    assertEquals("1", value(parts, "category_id"));
    assertEquals("20% off summer sale", value(parts, "title"));
    assertEquals("book_now", value(parts, "button_preset"));
    assertEquals(List.of("51.5074", "-0.1278"), values(parts, "location[]"));
    assertEquals(List.of("Male", "Female"), values(parts, "gender[]"));
    assertEquals(List.of("3", "7"), values(parts, "interests[]"));
    assertEquals("10", value(parts, "radius"));
    assertEquals("E", value(parts, "payment_method"));
    assertEquals("TEST", value(parts, "environment"));
    Part file = parts.stream().filter(p -> p.name().equals("image")).findFirst().orElseThrow();
    assertEquals("offer.png", file.fileName());
    assertEquals("image/png", file.contentType());
    assertArrayEquals(image, file.content());
    assertFalse(names(parts).contains("location"));
    assertEquals(501L, created.getOfferId());
    assertEquals("https://checkout.example/x", created.getPaymentUrl());
  }

  @Test
  void createOfferWithOwnKeyAndDryRun() {
    http.ok("{\"return_code\":\"S1\",\"dry_run\":true,\"price\":4.8}");
    OfferCreated result =
        client
            .offers()
            .create(
                CreateOfferParams.builder()
                    .title("t")
                    .idempotencyKey("my-key-1")
                    .dryRun(true)
                    .environment("TEST")
                    .build());
    assertRequest("POST", "/offers?dry_run=true");
    assertEquals("my-key-1", http.last().header("Idempotency-Key"));
    assertTrue(result.getDryRun());
    assertEquals(new BigDecimal("4.8"), result.get("price"));
  }

  @Test
  void listOffers() {
    http.ok(
        "{\"data\":[{\"offer_id\":7,\"title\":\"A\",\"gender\":[\"All\"],"
            + "\"interests\":[{\"interest_id\":3,\"interest_name\":\"Music\"}]}],"
            + "\"pagination\":{\"limit\":2,\"count\":1,\"has_more\":false,\"next_cursor\":null}}");
    var page =
        client
            .offers()
            .list(
                ListOffersParams.builder()
                    .limit(2)
                    .cursor("abc+/=")
                    .offerStatus("active")
                    .createdAfter("2027-01-01T00:00:00Z")
                    .createdBefore(Instant.parse("2027-02-01T00:00:00Z"))
                    .build());
    assertRequest(
        "GET",
        "/offers?limit=2&cursor=abc%2B%2F%3D&offer_status=active"
            + "&created_after=2027-01-01T00%3A00%3A00Z&created_before=2027-02-01T00%3A00%3A00Z"
            + "&environment=TEST");
    assertNoBody();
    assertEquals(7L, page.getData().get(0).getOfferId());
    assertEquals("Music", page.getData().get(0).getInterests().get(0).getInterestName());
    assertFalse(page.hasMore());
  }

  @Test
  void pauseResumeAnalyticsDeleteOffer() {
    http.ok("{\"return_code\":\"S1\",\"message\":\"Offer paused\"}");
    ApiMessage paused = client.offers().pause(42);
    assertRequest("POST", "/offers/42/pause");
    assertNoBody();
    assertEquals("Offer paused", paused.getMessage());

    http.ok("{\"return_code\":\"S2\",\"message\":\"Offer resumed\"}");
    assertEquals("S2", client.offers().resume(42).getReturnCode());
    assertRequest("POST", "/offers/42/resume");

    http.ok("{\"offer_id\":42,\"interval\":\"7d\",\"metrics\":{\"views\":10,\"clicks\":2}}");
    assertEquals(2L, client.offers().analytics(42, "7d").getMetrics().getClicks());
    assertRequest("GET", "/offers/42/analytics?interval=7d");

    client.offers().analytics(42, AnalyticsParams.builder().interval("all").build());
    assertRequest("GET", "/offers/42/analytics?interval=all");

    client.offers().delete(42);
    assertRequest("DELETE", "/offers/42");
    assertNoBody();

    client.offers().delete(42, DryRunParams.builder().dryRun(true).build());
    assertRequest("DELETE", "/offers/42?dry_run=true");
  }

  @Test
  void eventReferenceData() {
    http.ok("{\"categories\":[{\"category_id\":9,\"code\":\"classes\",\"category_name\":\"C\"}]}");
    assertEquals("classes", client.eventCategories().list().getCategories().get(0).getCode());
    assertRequest("GET", "/event-categories");

    http.ok("{\"currencies\":[\"GBP\",\"EUR\"]}");
    assertEquals(List.of("GBP", "EUR"), client.eventCurrencies().list().getCurrencies());
    assertRequest("GET", "/event-currencies");

    http.ok(
        "{\"listing_fee\":10,\"currency\":\"GBP\",\"balance\":25.5,\"live_publishing_open\":true,"
            + "\"image\":{\"max_mb\":10,\"min_width\":1280,\"min_height\":720}}");
    EventConfig config = client.eventConfig().get();
    assertRequest("GET", "/event-config");
    assertEquals(1280, config.getImage().getMinWidth());
    assertEquals(new BigDecimal("25.5"), config.getBalance());
    assertTrue(config.getLivePublishingOpen());
  }

  @Test
  void previewEvent() throws IOException {
    http.ok(
        "{\"return_code\":\"S1\",\"category\":{\"category_id\":9,\"category_name\":\"Classes\"},"
            + "\"image\":{\"preview_token\":\"evp_1\",\"crop\":{\"x\":0,\"y\":10,\"w\":1280,\"h\":720}}}");
    var preview =
        client
            .events()
            .preview(
                PreviewEventParams.builder()
                    .title("Salsa")
                    .description("Drop-in class")
                    .venueName("Bar Salsa")
                    .image(FileUpload.of(sampleImage(), "event.jpg"))
                    .crop(0, 10, 1280)
                    .build());
    assertRequest("POST", "/events/preview");
    assertNull(http.last().header("Idempotency-Key"));
    List<Part> parts = parse(http.last());
    assertEquals("0", value(parts, "crop[x]"));
    assertEquals("10", value(parts, "crop[y]"));
    assertEquals("1280", value(parts, "crop[w]"));
    assertEquals("TEST", value(parts, "environment"));
    assertEquals(
        "image/jpeg",
        parts.stream()
            .filter(p -> p.name().equals("image"))
            .findFirst()
            .orElseThrow()
            .contentType());
    assertEquals("evp_1", preview.getImage().getPreviewToken());
    assertEquals(720, preview.getImage().getCrop().getH());
  }

  @Test
  void publishEvent() {
    http.ok(
        "{\"return_code\":\"S1\",\"event_id\":3104,\"status\":\"Scheduled\",\"amount_charged\":0,"
            + "\"currency\":\"GBP\",\"category\":{\"category_id\":9,\"category_name\":\"Classes\"}}");
    EventPublished published =
        client
            .events()
            .publish(
                PublishEventParams.builder()
                    .title("Beginners Cuban Salsa")
                    .description("Drop-in class")
                    .venueName("Bar Salsa")
                    .address("96 Charing Cross Road")
                    .city("London")
                    .countryCode("GB")
                    .location(51.5133, -0.13)
                    .startsAt("2027-10-16T18:30:00Z")
                    .endsAt("2027-10-16T23:00:00Z")
                    .url("https://www.barsalsa.co.uk/classes/friday")
                    .priceMin(12)
                    .priceMax(new BigDecimal("12.50"))
                    .currency("GBP")
                    .previewToken("evp_1")
                    .build());
    assertRequest("POST", "/events");
    assertNotNull(http.last().header("Idempotency-Key"));
    List<Part> parts = parse(http.last());
    assertEquals(List.of("51.5133", "-0.13"), values(parts, "location[]"));
    assertEquals("12", value(parts, "price_min"));
    assertEquals("12.50", value(parts, "price_max"));
    assertEquals("evp_1", value(parts, "preview_token"));
    assertEquals("B", value(parts, "payment_method"));
    assertEquals("TEST", value(parts, "environment"));
    assertEquals(3104L, published.getEventId());
    assertEquals("Classes", published.getCategory().getCategoryName());
  }

  @Test
  void publishEventDryRun() {
    client
        .events()
        .publish(PublishEventParams.builder().title("x").sourceEventId(3100).dryRun(true).build());
    assertRequest("POST", "/events?dry_run=true");
    assertEquals("3100", value(parse(http.last()), "source_event_id"));
  }

  @Test
  void listEvents() {
    http.ok(
        "{\"data\":[{\"event_id\":3104,\"price_min\":12,\"owner\":{\"user_id\":41,\"name\":\"T\"},"
            + "\"category\":{\"category_id\":9,\"category_name\":\"C\"}}],"
            + "\"pagination\":{\"limit\":20,\"count\":1,\"has_more\":false,\"next_cursor\":null}}");
    var page =
        client
            .events()
            .list(ListEventsParams.builder().eventStatus("upcoming").ownerUserId(41).build());
    assertRequest("GET", "/events?event_status=upcoming&owner_user_id=41&environment=TEST");
    assertEquals(41L, page.getData().get(0).getOwner().getUserId());
    assertEquals(new BigDecimal("12"), page.getData().get(0).getPriceMin());
  }

  @Test
  void getEvent() {
    http.ok(
        "{\"event_id\":3104,\"title\":\"Salsa\",\"can_manage\":true,"
            + "\"history\":[{\"action\":\"published\",\"details\":{\"environment\":1}}]}");
    EventDetail event = client.events().get(3104);
    assertRequest("GET", "/events/3104");
    assertEquals("Salsa", event.getTitle());
    assertTrue(event.getCanManage());
    assertEquals("published", event.getHistory().get(0).getAction());
  }

  @Test
  void editEventWithoutFileIsSentAsPostWithOverride() {
    client
        .events()
        .edit(3104, EditEventParams.builder().title("New title").advertisedName("").build());
    assertRequest("POST", "/events/3104");
    assertEquals("PATCH", http.last().header("X-HTTP-Method-Override"));
    assertTrue(http.last().header("Content-Type").startsWith("multipart/form-data; boundary="));
    List<Part> parts = parse(http.last());
    assertEquals("New title", value(parts, "title"));
    assertEquals("", value(parts, "advertised_name"));
    assertFalse(names(parts).contains("environment"));
  }

  @Test
  void editEventWithFileIsSentAsPostWithOverride() throws IOException {
    client
        .events()
        .edit(
            3104,
            EditEventParams.builder()
                .image(FileUpload.of(sampleImage(), "new.png"))
                .crop(0, 0, 1280)
                .build());
    assertRequest("POST", "/events/3104");
    assertEquals("PATCH", http.last().header("X-HTTP-Method-Override"));
    assertEquals("new.png", parse(http.last()).get(0).fileName());
  }

  @Test
  void deleteRescheduleCancelAnalyticsEvent() throws IOException {
    client.events().delete(3104);
    assertRequest("DELETE", "/events/3104");
    client.events().delete(3104, DryRunParams.builder().dryRun(true).build());
    assertRequest("DELETE", "/events/3104?dry_run=true");

    client
        .events()
        .reschedule(
            3104,
            RescheduleEventParams.builder()
                .startsAt("2027-10-23T18:30:00Z")
                .endsAt("2027-10-23T23:00:00Z")
                .build());
    assertRequest("POST", "/events/3104/reschedule");
    assertNull(http.last().header("Idempotency-Key"));
    assertEquals("2027-10-23T18:30:00Z", jsonBody().get("starts_at").asText());

    client.events().cancel(3104);
    assertRequest("POST", "/events/3104/cancel");
    client.events().cancel(3104, DryRunParams.builder().dryRun(true).build());
    assertRequest("POST", "/events/3104/cancel?dry_run=true");

    http.ok("{\"event_id\":3104,\"metrics\":{\"clicks\":4,\"attendees\":\"10-25\"}}");
    assertEquals("10-25", client.events().analytics(3104, "30d").getMetrics().getAttendees());
    assertRequest("GET", "/events/3104/analytics?interval=30d");
  }

  @Test
  void webhookEndpoints() throws IOException {
    http.ok("{\"return_code\":\"S1\",\"data\":[{\"id\":\"we_1\",\"livemode\":false}]}");
    assertEquals("we_1", client.webhookEndpoints().list().getData().get(0).getId());
    assertRequest("GET", "/webhook-endpoints");

    http.ok("{\"return_code\":\"S1\",\"id\":\"we_1\",\"secret\":\"whsec_abc\"}");
    WebhookEndpoint created =
        client
            .webhookEndpoints()
            .create(
                CreateWebhookEndpointParams.builder()
                    .url("https://example.com/hooks")
                    .enabledEvents("event.published", "offer.started")
                    .description("Main")
                    .build());
    assertRequest("POST", "/webhook-endpoints");
    JsonNode body = jsonBody();
    assertEquals("https://example.com/hooks", body.get("url").asText());
    assertEquals("offer.started", body.get("enabled_events").get(1).asText());
    assertFalse(body.has("environment"));
    assertEquals("whsec_abc", created.getSecret());

    client.webhookEndpoints().get("we_1");
    assertRequest("GET", "/webhook-endpoints/we_1");

    client
        .webhookEndpoints()
        .update("we_1", UpdateWebhookEndpointParams.builder().status("disabled").build());
    assertRequest("PATCH", "/webhook-endpoints/we_1");
    assertEquals("disabled", jsonBody().get("status").asText());

    http.ok("{\"return_code\":\"S1\",\"id\":\"we_1\",\"deleted\":true}");
    assertTrue(client.webhookEndpoints().delete("we_1").getDeleted());
    assertRequest("DELETE", "/webhook-endpoints/we_1");

    client
        .webhookEndpoints()
        .rollSecret("we_1", RollWebhookSecretParams.builder().expiresIn("24h").build());
    assertRequest("POST", "/webhook-endpoints/we_1/roll-secret");
    assertEquals("24h", jsonBody().get("expires_in").asText());

    client.webhookEndpoints().rollSecret("we_1");
    assertEquals("{}", http.lastBody());

    http.ok(
        "{\"return_code\":\"S1\",\"webhook_event_id\":\"whevt_1\",\"test\":true,"
            + "\"delivery\":{\"endpoint_id\":\"we_1\",\"status\":\"delivered\",\"attempts\":1}}");
    var test =
        client
            .webhookEndpoints()
            .sendTest("we_1", SendTestWebhookParams.builder().type("offer.created").build());
    assertRequest("POST", "/webhook-endpoints/we_1/test");
    assertEquals("offer.created", jsonBody().get("type").asText());
    assertEquals(1, test.getDelivery().getAttempts());

    client.webhookEndpoints().get("we 1/x");
    assertRequest("GET", "/webhook-endpoints/we%201%2Fx");
  }

  @Test
  void webhookEvents() throws IOException {
    http.ok(
        "{\"return_code\":\"S1\",\"data\":[{\"id\":\"whevt_1\",\"type\":\"offer.started\","
            + "\"data\":{\"object\":{\"offer_id\":7}}}],"
            + "\"pagination\":{\"limit\":20,\"count\":1,\"has_more\":false,\"next_cursor\":null}}");
    var page =
        client
            .webhookEvents()
            .list(
                ListWebhookEventsParams.builder()
                    .type("offer.started")
                    .deliverySuccess(false)
                    .limit(5)
                    .build());
    assertRequest("GET", "/webhook-events?type=offer.started&delivery_success=false&limit=5");
    assertEquals(7L, page.getData().get(0).asOffer().orElseThrow().getOfferId());

    client.webhookEvents().get("whevt_1");
    assertRequest("GET", "/webhook-events/whevt_1");

    client
        .webhookEvents()
        .resend("whevt_1", ResendWebhookEventParams.builder().endpointId("we_1").build());
    assertRequest("POST", "/webhook-events/whevt_1/resend");
    assertEquals("we_1", jsonBody().get("endpoint_id").asText());
  }

  @Test
  void liveKeyFillsLiveEnvironment() {
    AgilisClient live = http.client(FakeTransport.LIVE_KEY);
    assertEquals(Mode.LIVE, live.mode());
    live.offers().list();
    assertRequest("GET", "/offers?environment=LIVE");
  }

  @Test
  void explicitEnvironmentIsKept() {
    client.events().list(ListEventsParams.builder().environment("test").build());
    assertRequest("GET", "/events?environment=test");
  }

  @Test
  void extraFieldsCanBeSent() {
    client.offers().list(ListOffersParams.builder().put("new_filter", "x").build());
    assertRequest("GET", "/offers?new_filter=x&environment=TEST");
  }
}
