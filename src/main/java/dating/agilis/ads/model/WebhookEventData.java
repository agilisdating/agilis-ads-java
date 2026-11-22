package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * The {@code data} of a webhook event: the object, plus any extra facts in {@link #get(String)}.
 */
public class WebhookEventData extends ApiObject {

  @JsonProperty("object")
  private Map<String, Object> object;

  /** Creates an empty object; the client fills it from JSON. */
  public WebhookEventData() {}

  /**
   * The {@code object} field: the Offer, Event or invoice, as the API returns it elsewhere.
   *
   * @return the value, or {@code null} when absent
   */
  public Map<String, Object> getObject() {
    return object;
  }
}
