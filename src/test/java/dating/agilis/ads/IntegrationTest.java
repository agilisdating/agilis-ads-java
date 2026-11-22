package dating.agilis.ads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import dating.agilis.ads.model.CategoryList;
import dating.agilis.ads.model.EventPublished;
import dating.agilis.ads.model.OfferCreated;
import dating.agilis.ads.param.CreateOfferParams;
import dating.agilis.ads.param.EstimateOfferPriceParams;
import dating.agilis.ads.param.FileUpload;
import dating.agilis.ads.param.ListEventsParams;
import dating.agilis.ads.param.ListOffersParams;
import dating.agilis.ads.param.ListWebhookEventsParams;
import dating.agilis.ads.param.PublishEventParams;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Runs against the real API in Test mode with {@code AGILIS_TEST_API_KEY}. Reads, plus dry runs
 * that create nothing. Skipped without the key; refuses a Live key.
 */
@Tag("integration")
class IntegrationTest {

  private static AgilisClient client;

  @BeforeAll
  static void setUp() {
    String key = System.getenv("AGILIS_TEST_API_KEY");
    assumeTrue(key != null && !key.isBlank(), "AGILIS_TEST_API_KEY is not set");
    if (key.trim().startsWith("pk_live_")) {
      throw new IllegalStateException("Refusing to run the integration tests with a Live key");
    }
    client = AgilisClient.builder().apiKey(key).build();
    if (client.mode() != Mode.TEST) {
      throw new IllegalStateException("The integration tests need a pk_test_ key");
    }
  }

  private static byte[] sampleImage() throws IOException {
    try (InputStream in = IntegrationTest.class.getResourceAsStream("/sample-offer.png")) {
      return in.readAllBytes();
    }
  }

  /** The sample image, scaled onto a 1280 x 720 canvas: the smallest Event image accepted. */
  private static byte[] sampleEventImage() throws IOException {
    BufferedImage source = ImageIO.read(new java.io.ByteArrayInputStream(sampleImage()));
    BufferedImage canvas = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = canvas.createGraphics();
    g.drawImage(source, 0, 0, 1280, 720, null);
    g.dispose();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ImageIO.write(canvas, "png", out);
    return out.toByteArray();
  }

  @Test
  void reads() {
    CategoryList categories = client.categories().list();
    assertNotNull(categories.getCategories());
    assertNotNull(client.interests().list().getInterests());
    assertNotNull(client.offers().list(ListOffersParams.builder().limit(5).build()).getData());
    assertNotNull(client.events().list(ListEventsParams.builder().limit(5).build()).getData());
    assertNotNull(client.eventCategories().list().getCategories());
    assertNotNull(client.eventCurrencies().list().getCurrencies());
    assertEquals("TEST", client.eventConfig().get().getEnvironment().toUpperCase());
    assertNotNull(client.webhookEndpoints().list().getData());
    assertNotNull(
        client.webhookEvents().list(ListWebhookEventsParams.builder().limit(5).build()).getData());
  }

  @Test
  void priceEstimate() {
    Instant start = Instant.now().plus(Duration.ofHours(1)).truncatedTo(ChronoUnit.SECONDS);
    var estimate =
        client
            .offers()
            .estimatePrice(
                EstimateOfferPriceParams.builder()
                    .startDatetime(start)
                    .endDatetime(start.plus(Duration.ofDays(1)))
                    .location(51.5074, -0.1278)
                    .radius(10)
                    .build());
    assertNotNull(estimate.getCurrency());
  }

  @Test
  void offerCreateDryRun() throws IOException {
    long categoryId = client.categories().list().getCategories().get(0).getCategoryId();
    Instant start = Instant.now().plus(Duration.ofHours(1));
    OfferCreated result =
        client
            .offers()
            .create(
                CreateOfferParams.builder()
                    .categoryId(categoryId)
                    .title("SDK dry run")
                    .description("A dry run from the Java library's tests. Nothing is created.")
                    .pushNotificationTitle("SDK dry run")
                    .pushNotificationMessage("A dry run: nothing is created.")
                    .url("https://www.agilis.dating")
                    .image(FileUpload.of(sampleImage(), "sample-offer.png"))
                    .location(51.5074, -0.1278)
                    .radius(10)
                    .startDatetime(start)
                    .expiryDatetime(start.plus(Duration.ofDays(1)))
                    .paymentMethod("B")
                    .dryRun(true)
                    .build());
    assertTrue(Boolean.TRUE.equals(result.getDryRun()));
  }

  @Test
  void eventPublishDryRun() throws IOException {
    Instant start = Instant.now().plus(Duration.ofDays(30)).truncatedTo(ChronoUnit.HOURS);
    EventPublished result =
        client
            .events()
            .publish(
                PublishEventParams.builder()
                    .title("SDK dry run")
                    .description("A dry run from the Java library's tests. Nothing is created.")
                    .venueName("Test venue")
                    .address("1 Test Street")
                    .city("London")
                    .countryCode("GB")
                    .location(51.5133, -0.13)
                    .startsAt(start)
                    .endsAt(start.plus(Duration.ofHours(2)))
                    .url("https://www.agilis.dating")
                    .image(FileUpload.of(sampleEventImage(), "sample-event.png"))
                    .dryRun(true)
                    .build());
    assertTrue(Boolean.TRUE.equals(result.getDryRun()));
  }
}
