package dating.agilis.ads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dating.agilis.ads.http.TransportRequest;
import dating.agilis.ads.model.CategoryList;
import dating.agilis.ads.model.Event;
import dating.agilis.ads.model.PriceEstimate;
import dating.agilis.ads.param.EstimateOfferPriceParams;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ClientTest {

  @Test
  void modeComesFromTheKey() {
    FakeTransport http = new FakeTransport();
    assertEquals(Mode.TEST, http.client("pk_test_abc").mode());
    assertEquals(Mode.LIVE, http.client("pk_live_abc").mode());
    assertEquals(Mode.UNKNOWN, http.client("something_else").mode());
    assertEquals("TEST", Mode.TEST.environment());
    assertNull(Mode.UNKNOWN.environment());
  }

  @Test
  void unknownModeFillsNoEnvironment() {
    FakeTransport http = new FakeTransport();
    http.client("legacy-key").offers().list();
    assertEquals("/offers", http.last().uri().toString().replace("https://ads.agilis.dating", ""));
  }

  @Test
  void keyIsReadFromTheEnvironment() {
    FakeTransport http = new FakeTransport();
    AgilisClient client =
        AgilisClient.builder()
            .transport(http)
            .env(name -> "AGILIS_API_KEY".equals(name) ? "pk_live_fromenv" : null)
            .build();
    assertEquals(Mode.LIVE, client.mode());
    client.categories().list();
    assertEquals("pk_live_fromenv", http.last().header("x-api-key"));
  }

  @Test
  void missingKeyIsRefused() {
    IllegalStateException e =
        assertThrows(
            IllegalStateException.class,
            () -> AgilisClient.builder().transport(new FakeTransport()).env(n -> null).build());
    assertTrue(e.getMessage().contains("AGILIS_API_KEY"));
  }

  @Test
  void defaults() {
    AgilisClient client = AgilisClient.builder().apiKey("pk_test_x").env(n -> null).build();
    assertEquals("https://ads.agilis.dating", client.baseUrl());
    assertEquals(Duration.ofSeconds(60), client.timeout());
    assertEquals(2, client.maxRetries());
  }

  @Test
  void headersAreSent() {
    FakeTransport http = new FakeTransport();
    http.client().categories().list();
    TransportRequest r = http.last();
    assertEquals(FakeTransport.TEST_KEY, r.header("x-api-key"));
    assertEquals("application/json", r.header("Accept"));
    String ua = r.header("User-Agent");
    assertTrue(
        ua.matches("agilis-ads-java/\\d+\\.\\d+\\.\\d+.* \\(Java .+\\)"), "User-Agent: " + ua);
    assertEquals(
        "agilis-ads-java/"
            + AgilisClient.VERSION
            + " (Java "
            + System.getProperty("java.version")
            + ")",
        ua);
    assertNull(r.header("Authorization"));
    assertTrue(!r.uri().toString().contains(FakeTransport.TEST_KEY));
    assertEquals(Duration.ofSeconds(60), r.timeout());
  }

  @Test
  void versionIsTheBuildVersion() {
    assertTrue(AgilisClient.VERSION.matches("\\d+\\.\\d+\\.\\d+.*"), AgilisClient.VERSION);
    assertTrue(!AgilisClient.VERSION.equals("0.0.0"));
  }

  @Test
  void baseUrlAndTimeoutOptions() {
    FakeTransport http = new FakeTransport();
    AgilisClient client =
        http.builder(FakeTransport.TEST_KEY)
            .baseUrl("http://localhost:8080/")
            .timeout(Duration.ofSeconds(5))
            .build();
    client.categories().list();
    assertEquals("http://localhost:8080/categories", http.last().uri().toString());
    assertEquals(Duration.ofSeconds(5), http.last().timeout());
    assertThrows(IllegalArgumentException.class, () -> AgilisClient.builder().baseUrl("nope"));
    assertThrows(IllegalArgumentException.class, () -> AgilisClient.builder().maxRetries(-1));
    assertThrows(
        IllegalArgumentException.class, () -> AgilisClient.builder().timeout(Duration.ZERO));
  }

  @Test
  void unknownFieldsAreKept() {
    FakeTransport http = new FakeTransport();
    http.ok(
        "{\"categories\":[{\"category_id\":1,\"category_name\":\"A\",\"icon\":\"cup\"}],"
            + "\"new_field\":{\"nested\":[1,2]}}");
    CategoryList list = http.client().categories().list();
    assertEquals("cup", list.getCategories().get(0).get("icon"));
    assertEquals(Map.of("nested", List.of(1, 2)), list.get("new_field"));
    assertTrue(list.getExtraFields().containsKey("new_field"));
  }

  @Test
  void statusAndHeadersStayReachable() {
    FakeTransport http = new FakeTransport();
    http.respond(
        200,
        "{\"return_code\":\"S1\",\"price_estimate\":1}",
        "X-RateLimit-Limit",
        "60",
        "X-RateLimit-Remaining",
        "59");
    PriceEstimate estimate =
        http.client().offers().estimatePrice(EstimateOfferPriceParams.builder().build());
    assertEquals(200, estimate.getLastResponse().getStatusCode());
    assertEquals("59", estimate.getLastResponse().getHeader("x-ratelimit-remaining"));
    assertEquals("60", estimate.getLastResponse().getHeaders().get("X-RATELIMIT-LIMIT").get(0));
  }

  @Test
  void nullsAndMissingFieldsAreNull() {
    FakeTransport http = new FakeTransport();
    http.ok("{\"event_id\":1,\"advertised_name\":null}");
    Event event = http.client().events().get(1);
    assertNull(event.getAdvertisedName());
    assertNull(event.getTitle());
  }
}
