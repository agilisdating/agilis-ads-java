package dating.agilis.ads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dating.agilis.ads.exception.AgilisException;
import dating.agilis.ads.exception.ApiException;
import dating.agilis.ads.exception.AuthenticationException;
import dating.agilis.ads.exception.ConflictException;
import dating.agilis.ads.exception.ConnectionException;
import dating.agilis.ads.exception.InvalidRequestException;
import dating.agilis.ads.exception.NotFoundException;
import dating.agilis.ads.exception.PermissionException;
import dating.agilis.ads.exception.RateLimitException;
import dating.agilis.ads.param.CreateOfferParams;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

class ErrorsTest {

  private AgilisException errorFor(int status, String body) {
    FakeTransport http = new FakeTransport();
    AgilisClient client = http.builder(FakeTransport.TEST_KEY).maxRetries(0).build();
    http.respond(status, body);
    return assertThrows(AgilisException.class, () -> client.events().get(1));
  }

  @Test
  void statusesMapToTypes() {
    assertInstanceOf(InvalidRequestException.class, errorFor(400, "{\"return_code\":\"E1\"}"));
    assertInstanceOf(
        AuthenticationException.class, errorFor(401, "{\"message\":\"Unauthorised\"}"));
    assertInstanceOf(PermissionException.class, errorFor(403, "{\"return_code\":\"E1\"}"));
    assertInstanceOf(NotFoundException.class, errorFor(404, "{\"return_code\":\"E1\"}"));
    assertInstanceOf(ConflictException.class, errorFor(409, "{}"));
    assertInstanceOf(InvalidRequestException.class, errorFor(422, "{}"));
    assertInstanceOf(RateLimitException.class, errorFor(429, "{}"));
    assertInstanceOf(ApiException.class, errorFor(500, "{}"));
    assertInstanceOf(ApiException.class, errorFor(503, "not json"));
    assertInstanceOf(ApiException.class, errorFor(418, ""));
  }

  @Test
  void errorCarriesStatusCodeMessageBodyAndHeaders() {
    FakeTransport http = new FakeTransport();
    http.respond(
        403,
        "{\"return_code\":\"E9\",\"message\":\"Accept the Terms\",\"code\":403}",
        "X-RateLimit-Remaining",
        "41");
    PermissionException e =
        assertThrows(PermissionException.class, () -> http.client().events().get(1));
    assertEquals(403, e.getStatusCode());
    assertEquals("E9", e.getReturnCode());
    assertEquals("Accept the Terms", e.getMessage());
    assertEquals(403, e.getBody().get("code"));
    assertEquals("41", e.getHeader("x-ratelimit-remaining"));
    assertTrue(e.getRawBody().contains("E9"));
  }

  @Test
  void messageFallsBackToStatusAndCode() {
    AgilisException e = errorFor(404, "{\"return_code\":\"E1\"}");
    assertEquals("HTTP 404 E1", e.getMessage());
    assertEquals("HTTP 500", errorFor(500, "").getMessage());
  }

  @Test
  void errorCodeInHttp200RaisesInvalidRequest() {
    FakeTransport http = new FakeTransport();
    http.ok("{\"return_code\":\"E3\",\"message\":\"Insufficient balance\"}");
    InvalidRequestException e =
        assertThrows(
            InvalidRequestException.class,
            () -> http.client().offers().create(CreateOfferParams.builder().build()));
    assertEquals(200, e.getStatusCode());
    assertEquals("E3", e.getReturnCode());
    assertEquals("Insufficient balance", e.getMessage());
    assertEquals(1, http.requests.size());
  }

  @Test
  void s1AndS2AreSuccess() {
    FakeTransport http = new FakeTransport();
    http.ok("{\"return_code\":\"S1\",\"message\":\"Offer paused\"}");
    http.ok("{\"return_code\":\"S2\",\"message\":\"Offer resumed\"}");
    assertEquals("S1", http.client().offers().pause(1).getReturnCode());
    assertEquals("S2", http.client().offers().resume(1).getReturnCode());
  }

  @Test
  void successWithoutJsonObjectRaisesApiException() {
    FakeTransport http = new FakeTransport();
    http.ok("[1,2]");
    ApiException e = assertThrows(ApiException.class, () -> http.client().events().get(1));
    assertEquals(200, e.getStatusCode());
  }

  @Test
  void apiKeyNeverAppearsInErrors() {
    String key = FakeTransport.TEST_KEY;
    FakeTransport http = new FakeTransport();
    AgilisClient client = http.builder(key).maxRetries(0).build();

    http.respond(401, "{\"message\":\"Key " + key + " is not valid\",\"key\":\"" + key + "\"}");
    AgilisException e = assertThrows(AgilisException.class, () -> client.events().get(1));
    assertNoKey(e, key);
    assertFalse(e.getBody().toString().contains(key));

    http.fail(new IOException("failed while sending " + key));
    ConnectionException c = assertThrows(ConnectionException.class, () -> client.events().get(1));
    assertFalse(c.getMessage().contains(key));

    http.respond(500, "{\"message\":\"other key pk_live_abcdefABCDEF123\"}");
    AgilisException other = assertThrows(AgilisException.class, () -> client.events().get(1));
    assertFalse(other.getMessage().contains("pk_live_abcdefABCDEF123"));
    assertFalse(client.toString().contains(key));
  }

  private static void assertNoKey(AgilisException e, String key) {
    StringWriter trace = new StringWriter();
    e.printStackTrace(new PrintWriter(trace));
    assertFalse(trace.toString().contains(key));
    assertFalse(e.getMessage().contains(key));
    assertFalse(e.getRawBody().contains(key));
  }
}
