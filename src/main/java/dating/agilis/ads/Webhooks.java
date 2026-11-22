package dating.agilis.ads;

import com.fasterxml.jackson.databind.JsonNode;
import dating.agilis.ads.exception.SignatureVerificationException;
import dating.agilis.ads.exception.SignatureVerificationException.Reason;
import dating.agilis.ads.internal.Json;
import dating.agilis.ads.model.WebhookEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Verifies the webhooks Agilis sends to your endpoints.
 *
 * <p>Every request carries an {@code Agilis-Signature} header, {@code
 * t=<timestamp>,v1=<signature>}: the hex HMAC-SHA256 of {@code <timestamp>.<raw body>} with the
 * endpoint's secret. While a secret is being rolled there are two {@code v1} values; either may
 * match.
 */
public final class Webhooks {

  /** The name of the signature header. */
  public static final String SIGNATURE_HEADER = "Agilis-Signature";

  /** The default tolerance, in seconds, between the signature's timestamp and now. */
  public static final long DEFAULT_TOLERANCE_SECONDS = 300;

  private Webhooks() {}

  /**
   * Verifies a received webhook and returns its event, with the default tolerance of 300 seconds.
   *
   * @param rawBody the request body exactly as received
   * @param signatureHeader the value of the {@code Agilis-Signature} header
   * @param secret the endpoint's signing secret, {@code whsec_...}
   * @return the event
   * @throws SignatureVerificationException when the header is malformed, the timestamp is outside
   *     the tolerance, or no signature matches
   */
  public static WebhookEvent constructEvent(String rawBody, String signatureHeader, String secret) {
    return constructEvent(rawBody, signatureHeader, secret, DEFAULT_TOLERANCE_SECONDS);
  }

  /**
   * Verifies a received webhook and returns its event.
   *
   * @param rawBody the request body exactly as received
   * @param signatureHeader the value of the {@code Agilis-Signature} header
   * @param secret the endpoint's signing secret, {@code whsec_...}
   * @param toleranceSeconds the largest accepted difference between the timestamp and now
   * @return the event
   * @throws SignatureVerificationException when the header is malformed, the timestamp is outside
   *     the tolerance, or no signature matches
   */
  public static WebhookEvent constructEvent(
      String rawBody, String signatureHeader, String secret, long toleranceSeconds) {
    if (rawBody == null) {
      throw new SignatureVerificationException("The body is missing", Reason.PAYLOAD);
    }
    return constructEvent(
        rawBody.getBytes(StandardCharsets.UTF_8), signatureHeader, secret, toleranceSeconds);
  }

  /**
   * Verifies a received webhook and returns its event.
   *
   * @param rawBody the request body exactly as received
   * @param signatureHeader the value of the {@code Agilis-Signature} header
   * @param secret the endpoint's signing secret, {@code whsec_...}
   * @param toleranceSeconds the largest accepted difference between the timestamp and now
   * @return the event
   * @throws SignatureVerificationException when the header is malformed, the timestamp is outside
   *     the tolerance, or no signature matches
   */
  public static WebhookEvent constructEvent(
      byte[] rawBody, String signatureHeader, String secret, long toleranceSeconds) {
    return constructEvent(
        rawBody, signatureHeader, secret, toleranceSeconds, System.currentTimeMillis() / 1000L);
  }

  static WebhookEvent constructEvent(
      byte[] rawBody,
      String signatureHeader,
      String secret,
      long toleranceSeconds,
      long nowSeconds) {
    if (rawBody == null) {
      throw new SignatureVerificationException("The body is missing", Reason.PAYLOAD);
    }
    if (secret == null || secret.isEmpty()) {
      throw new IllegalArgumentException("The webhook secret is missing");
    }
    if (signatureHeader == null || signatureHeader.isBlank()) {
      throw new SignatureVerificationException(
          "The " + SIGNATURE_HEADER + " header is missing", Reason.HEADER);
    }

    String timestamp = null;
    List<String> signatures = new ArrayList<>();
    for (String item : signatureHeader.split(",")) {
      String part = item.trim();
      int eq = part.indexOf('=');
      if (eq <= 0) {
        continue;
      }
      String name = part.substring(0, eq).trim();
      String value = part.substring(eq + 1).trim();
      if ("t".equals(name) && timestamp == null) {
        timestamp = value;
      } else if ("v1".equals(name) && !value.isEmpty()) {
        signatures.add(value.toLowerCase(Locale.ROOT));
      }
    }
    if (timestamp == null || !timestamp.matches("\\d{1,18}")) {
      throw new SignatureVerificationException(
          "The " + SIGNATURE_HEADER + " header has no valid timestamp", Reason.HEADER);
    }
    if (signatures.isEmpty()) {
      throw new SignatureVerificationException(
          "The " + SIGNATURE_HEADER + " header has no v1 signature", Reason.HEADER);
    }
    long t = Long.parseLong(timestamp);
    if (Math.abs(nowSeconds - t) > toleranceSeconds) {
      throw new SignatureVerificationException(
          "The signature's timestamp is outside the tolerance", Reason.TIMESTAMP);
    }

    byte[] expected = hmacHex(secret, timestamp, rawBody);
    boolean matched = false;
    for (String signature : signatures) {
      if (MessageDigest.isEqual(expected, signature.getBytes(StandardCharsets.US_ASCII))) {
        matched = true;
      }
    }
    if (!matched) {
      throw new SignatureVerificationException(
          "No signature matches the body and secret", Reason.SIGNATURE);
    }

    try {
      JsonNode node = Json.mapper().readTree(rawBody);
      if (node == null || !node.isObject()) {
        throw new SignatureVerificationException("The body is not a JSON object", Reason.PAYLOAD);
      }
      return Json.mapper().treeToValue(node, WebhookEvent.class);
    } catch (IOException e) {
      throw new SignatureVerificationException("The body is not valid JSON", Reason.PAYLOAD);
    }
  }

  /**
   * Computes a signature header for a body, as Agilis does. Useful in tests of your own webhook
   * handler.
   *
   * @param rawBody the body
   * @param secret the signing secret
   * @param timestampSeconds the timestamp, in Unix seconds
   * @return the header value, {@code t=<timestamp>,v1=<signature>}
   */
  public static String signatureHeader(String rawBody, String secret, long timestampSeconds) {
    String timestamp = Long.toString(timestampSeconds);
    byte[] hex = hmacHex(secret, timestamp, rawBody.getBytes(StandardCharsets.UTF_8));
    return "t=" + timestamp + ",v1=" + new String(hex, StandardCharsets.US_ASCII);
  }

  private static byte[] hmacHex(String secret, String timestamp, byte[] body) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
      ByteArrayOutputStream payload = new ByteArrayOutputStream();
      payload.writeBytes(timestamp.getBytes(StandardCharsets.US_ASCII));
      payload.write('.');
      payload.writeBytes(body);
      byte[] digest = mac.doFinal(payload.toByteArray());
      byte[] hex = new byte[digest.length * 2];
      byte[] alphabet = "0123456789abcdef".getBytes(StandardCharsets.US_ASCII);
      for (int i = 0; i < digest.length; i++) {
        hex[i * 2] = alphabet[(digest[i] >> 4) & 0xf];
        hex[i * 2 + 1] = alphabet[digest[i] & 0xf];
      }
      return hex;
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException("HmacSHA256 is not available", e);
    }
  }
}
