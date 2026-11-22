import dating.agilis.ads.AgilisClient;
import dating.agilis.ads.Mode;
import dating.agilis.ads.model.Category;
import dating.agilis.ads.model.Event;
import dating.agilis.ads.model.PriceEstimate;
import dating.agilis.ads.param.EstimateOfferPriceParams;
import java.time.Duration;
import java.time.Instant;

/**
 * Lists categories and Events and estimates an Offer's price, in Test mode.
 *
 * <p>Run with a Test key: {@code AGILIS_API_KEY=pk_test_... java -cp <classpath>
 * examples/TestModeQuickStart.java}
 */
public class TestModeQuickStart {

  public static void main(String[] args) {
    AgilisClient client = AgilisClient.builder().build(); // reads AGILIS_API_KEY
    if (client.mode() != Mode.TEST) {
      throw new IllegalStateException("This example only runs with a pk_test_ key");
    }

    for (Category category : client.categories().list().getCategories()) {
      System.out.println(category.getCategoryId() + " " + category.getCategoryName());
    }

    Instant start = Instant.now().plus(Duration.ofHours(1));
    PriceEstimate estimate =
        client
            .offers()
            .estimatePrice(
                EstimateOfferPriceParams.builder()
                    .startDatetime(start)
                    .endDatetime(start.plus(Duration.ofDays(2)))
                    .location(51.5074, -0.1278)
                    .radius(10)
                    .build());
    System.out.println(
        "Estimated cost: " + estimate.getEstimatedCost() + " " + estimate.getCurrency());

    for (Event event : client.events().listAutoPaging()) {
      System.out.println(event.getEventId() + " " + event.getTitle() + " " + event.getStartsAt());
    }
  }
}
