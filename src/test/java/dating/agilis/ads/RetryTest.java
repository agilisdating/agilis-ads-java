package dating.agilis.ads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dating.agilis.ads.exception.ApiException;
import dating.agilis.ads.exception.ConnectionException;
import dating.agilis.ads.exception.RateLimitException;
import dating.agilis.ads.http.TransportRequest;
import dating.agilis.ads.param.CreateOfferParams;
import dating.agilis.ads.param.EditEventParams;
import dating.agilis.ads.param.PublishEventParams;
import dating.agilis.ads.param.RescheduleEventParams;
import dating.agilis.ads.param.UpdateWebhookEndpointParams;
import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class RetryTest {

  private final FakeTransport http = new FakeTransport();
  private final AgilisClient client = http.client();

  private static Duration ms(long millis) {
    return Duration.ofMillis(millis);
  }

  @Test
  void getIsRetriedOnServerErrorsWithBackoff() {
    http.respond(500, "{}").respond(503, "{}").ok("{\"categories\":[]}");
    client.categories().list();
    assertEquals(3, http.requests.size());
    assertEquals(List.of(ms(500), ms(1000)), http.sleeps);
  }

  @Test
  void eachRetryableServerStatusIsRetried() {
    for (int status : new int[] {500, 502, 503, 504}) {
      FakeTransport t = new FakeTransport();
      t.respond(status, "{}").ok("{}");
      t.client().events().get(1);
      assertEquals(2, t.requests.size(), "status " + status);
    }
  }

  @Test
  void otherServerStatusesAreNotRetried() {
    http.respond(501, "{}");
    assertThrows(ApiException.class, () -> client.events().get(1));
    assertEquals(1, http.requests.size());
  }

  @Test
  void deleteIsRetriedOnServerErrors() {
    http.respond(502, "{}").ok("{\"return_code\":\"S1\"}");
    client.offers().delete(5);
    assertEquals(2, http.requests.size());
  }

  @Test
  void retriesStopAtTheMaximum() {
    http.respond(500, "{}").respond(500, "{}").respond(500, "{\"message\":\"Server error\"}");
    ApiException e = assertThrows(ApiException.class, () -> client.categories().list());
    assertEquals(500, e.getStatusCode());
    assertEquals("Server error", e.getMessage());
    assertEquals(3, http.requests.size());
  }

  @Test
  void maxRetriesZeroTurnsRetriesOff() {
    AgilisClient noRetry = http.builder(FakeTransport.TEST_KEY).maxRetries(0).build();
    http.respond(500, "{}");
    assertThrows(ApiException.class, () -> noRetry.categories().list());
    assertEquals(1, http.requests.size());
    assertEquals(List.of(), http.sleeps);
  }

  @Test
  void idempotencyKeyIsKeptAcrossRetries() {
    http.respond(500, "{}").respond(429, "{}").ok("{\"return_code\":\"S1\",\"offer_id\":1}");
    client.offers().create(CreateOfferParams.builder().title("t").build());
    assertEquals(3, http.requests.size());
    List<String> keys =
        http.requests.stream()
            .map(r -> r.header("Idempotency-Key"))
            .distinct()
            .collect(Collectors.toList());
    assertEquals(1, keys.size());
    assertEquals(36, keys.get(0).length());
  }

  @Test
  void eachCallGetsItsOwnIdempotencyKey() {
    client.events().publish(PublishEventParams.builder().title("a").build());
    client.events().publish(PublishEventParams.builder().title("a").build());
    String first = http.requests.get(0).header("Idempotency-Key");
    String second = http.requests.get(1).header("Idempotency-Key");
    assertEquals(36, first.length());
    assertEquals(false, first.equals(second));
  }

  @Test
  void postWithIdempotencyKeyIsRetriedOnServerErrors() {
    http.respond(504, "{}").ok("{\"return_code\":\"S1\"}");
    client.events().publish(PublishEventParams.builder().title("a").build());
    assertEquals(2, http.requests.size());
  }

  @Test
  void postWithoutIdempotencyKeyIsNotRetriedOnServerErrors() {
    http.respond(503, "{}");
    assertThrows(
        ApiException.class,
        () -> client.events().reschedule(1, RescheduleEventParams.builder().build()));
    assertEquals(1, http.requests.size());
  }

  @Test
  void patchIsNotRetriedOnServerErrors() {
    http.respond(502, "{}");
    assertThrows(
        ApiException.class,
        () ->
            client
                .webhookEndpoints()
                .update("we_1", UpdateWebhookEndpointParams.builder().build()));
    assertEquals(1, http.requests.size());
  }

  @Test
  void eventEditIsNotRetriedOnServerErrors() {
    http.respond(500, "{}");
    assertThrows(
        ApiException.class,
        () -> client.events().edit(1, EditEventParams.builder().title("t").build()));
    assertEquals(1, http.requests.size());
  }

  @Test
  void rateLimitIsRetriedForAnyMethodAndHonoursRetryAfter() {
    http.respond(429, "{}", "Retry-After", "3").ok("{}");
    client.events().reschedule(1, RescheduleEventParams.builder().build());
    assertEquals(2, http.requests.size());
    assertEquals(List.of(Duration.ofSeconds(3)), http.sleeps);
  }

  @Test
  void fractionalRetryAfterIsHonoured() {
    http.respond(503, "{}", "Retry-After", "1.5").ok("{}");
    client.events().get(1);
    assertEquals(List.of(ms(1500)), http.sleeps);
  }

  @Test
  void rateLimitAfterRetriesRaisesWithRetryAfter() {
    http.respond(429, "{\"message\":\"Too many\"}", "Retry-After", "7")
        .respond(429, "{\"message\":\"Too many\"}", "Retry-After", "7")
        .respond(429, "{\"message\":\"Too many\"}", "Retry-After", "7");
    RateLimitException e = assertThrows(RateLimitException.class, () -> client.events().get(1));
    assertEquals(Duration.ofSeconds(7), e.getRetryAfter().orElseThrow());
    assertEquals(3, http.requests.size());
  }

  @Test
  void connectionErrorsAndTimeoutsAreRetried() {
    http.fail(new IOException("Connection reset")).fail(new HttpTimeoutException("timed out"));
    http.ok("{}");
    client.events().reschedule(1, RescheduleEventParams.builder().build());
    assertEquals(3, http.requests.size());
  }

  @Test
  void connectionErrorAfterRetriesRaisesConnectionException() {
    http.fail(new IOException("a")).fail(new IOException("b")).fail(new IOException("c"));
    ConnectionException e =
        assertThrows(ConnectionException.class, () -> client.categories().list());
    assertEquals(0, e.getStatusCode());
    assertEquals(3, http.requests.size());
  }

  @Test
  void backoffDoublesWithJitterAndIsCappedAtEightSeconds() {
    FakeTransport low = new FakeTransport();
    AgilisClient lowClient =
        low.builder(FakeTransport.TEST_KEY).maxRetries(6).random(() -> 0.0).build();
    for (int i = 0; i < 7; i++) {
      low.respond(500, "{}");
    }
    assertThrows(ApiException.class, () -> lowClient.categories().list());
    assertEquals(List.of(ms(375), ms(750), ms(1500), ms(3000), ms(6000), ms(8000)), low.sleeps);

    FakeTransport high = new FakeTransport();
    AgilisClient highClient =
        high.builder(FakeTransport.TEST_KEY).maxRetries(4).random(() -> 1.0).build();
    for (int i = 0; i < 5; i++) {
      high.respond(500, "{}");
    }
    assertThrows(ApiException.class, () -> highClient.categories().list());
    assertEquals(List.of(ms(625), ms(1250), ms(2500), ms(5000)), high.sleeps);
  }

  @Test
  void retriedRequestsAreIdentical() {
    http.respond(500, "{}").ok("{}");
    client.offers().create(CreateOfferParams.builder().title("t").build());
    TransportRequest first = http.requests.get(0);
    TransportRequest second = http.requests.get(1);
    assertEquals(first.uri(), second.uri());
    assertEquals(first.headers(), second.headers());
    assertEquals(new String(first.body()), new String(second.body()));
  }
}
