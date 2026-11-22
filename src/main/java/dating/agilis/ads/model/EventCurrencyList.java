package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** The response of {@code GET /event-currencies}. */
public class EventCurrencyList extends ApiResponse {

  @JsonProperty("currencies")
  private List<String> currencies;

  /** Creates an empty object; the client fills it from JSON. */
  public EventCurrencyList() {}

  /**
   * The {@code currencies} field: the ticket price currencies.
   *
   * @return the value, or {@code null} when absent
   */
  public List<String> getCurrencies() {
    return currencies;
  }
}
