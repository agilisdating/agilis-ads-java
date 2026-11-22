package dating.agilis.ads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.fasterxml.jackson.databind.JsonNode;
import dating.agilis.ads.exception.SignatureVerificationException;
import dating.agilis.ads.exception.SignatureVerificationException.Reason;
import dating.agilis.ads.internal.Json;
import dating.agilis.ads.model.Event;
import dating.agilis.ads.model.WebhookEvent;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class WebhooksTest {

  private static JsonNode vectors() throws IOException {
    try (InputStream in =
        WebhooksTest.class.getResourceAsStream("/webhook-signature-vectors.json")) {
      return Json.mapper().readTree(in);
    }
  }

  @TestFactory
  List<DynamicTest> sharedSignatureVectors() throws IOException {
    JsonNode v = vectors();
    String secret = v.get("secret").asText();
    long now = v.get("now_unix").asLong();
    long tolerance = v.get("tolerance_seconds").asLong();
    byte[] body = v.get("body").asText().getBytes(StandardCharsets.UTF_8);
    List<DynamicTest> tests = new ArrayList<>();
    for (JsonNode c : v.get("cases")) {
      String name = c.get("name").asText();
      String header = c.get("header").asText();
      boolean valid = c.get("valid").asBoolean();
      tests.add(
          dynamicTest(
              name,
              () -> {
                if (valid) {
                  WebhookEvent event =
                      Webhooks.constructEvent(body, header, secret, tolerance, now);
                  assertEquals("event.published", event.getType());
                } else {
                  SignatureVerificationException e =
                      assertThrows(
                          SignatureVerificationException.class,
                          () -> Webhooks.constructEvent(body, header, secret, tolerance, now));
                  String expected = c.get("error").asText().toUpperCase(Locale.ROOT);
                  assertEquals(Reason.valueOf(expected), e.getReason(), name);
                }
              }));
    }
    assertEquals(12, tests.size());
    return tests;
  }

  @Test
  void validEventIsParsedAndTyped() throws IOException {
    JsonNode v = vectors();
    byte[] body = v.get("body").asText().getBytes(StandardCharsets.UTF_8);
    String header = v.get("cases").get(0).get("header").asText();
    WebhookEvent event =
        Webhooks.constructEvent(
            body, header, v.get("secret").asText(), 300, v.get("now_unix").asLong());
    assertEquals("whevt_01JD0S6Q4M8K1Z0X7C2V9B3N5R", event.getId());
    assertEquals("webhook_event", event.getObject());
    assertEquals(1795075200L, event.getCreated());
    assertTrue(event.getLivemode());
    assertEquals(1, event.getVersion());
    assertEquals("api", event.getRequest().getSource());
    assertEquals(
        0, new BigDecimal("10").compareTo((BigDecimal) event.getData().get("amount_charged")));
    Event published = event.asEvent().orElseThrow();
    assertEquals(3104L, published.getEventId());
    assertEquals("Bar Salsa", published.getVenueName());
    assertEquals(41L, published.getOwner().getUserId());
    assertTrue(event.asOffer().isEmpty());
  }

  @Test
  void signatureHeaderRoundTrip() {
    String body = "{\"id\":\"whevt_x\",\"type\":\"offer.started\",\"data\":{\"object\":{}}}";
    long now = System.currentTimeMillis() / 1000L;
    String header = Webhooks.signatureHeader(body, "whsec_round_trip", now);
    WebhookEvent event = Webhooks.constructEvent(body, header, "whsec_round_trip");
    assertEquals("offer.started", event.getType());
    assertThrows(
        SignatureVerificationException.class,
        () -> Webhooks.constructEvent(body + " ", header, "whsec_round_trip"));
  }

  @Test
  void uppercaseSignatureIsAccepted() {
    String body = "{\"type\":\"balance.low\"}";
    String signed = Webhooks.signatureHeader(body, "whsec_u", 1000);
    String header =
        "t=1000,v1=" + signed.substring(signed.indexOf("v1=") + 3).toUpperCase(Locale.ROOT);
    WebhookEvent event =
        Webhooks.constructEvent(
            body.getBytes(StandardCharsets.UTF_8), header, "whsec_u", 300, 1000);
    assertEquals("balance.low", event.getType());
  }

  @Test
  void nullHeaderAndNonObjectBody() {
    SignatureVerificationException missing =
        assertThrows(
            SignatureVerificationException.class,
            () -> Webhooks.constructEvent("{}", null, "whsec_x"));
    assertEquals(Reason.HEADER, missing.getReason());

    String header = Webhooks.signatureHeader("[1]", "whsec_x", 1000);
    SignatureVerificationException payload =
        assertThrows(
            SignatureVerificationException.class,
            () ->
                Webhooks.constructEvent(
                    "[1]".getBytes(StandardCharsets.UTF_8), header, "whsec_x", 300, 1000));
    assertEquals(Reason.PAYLOAD, payload.getReason());
  }
}
