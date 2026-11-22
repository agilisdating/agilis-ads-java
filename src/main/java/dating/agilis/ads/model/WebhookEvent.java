package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import dating.agilis.ads.internal.Json;
import java.util.List;
import java.util.Optional;

/**
 * A webhook event: as received by your endpoint and verified with {@link
 * dating.agilis.ads.Webhooks#constructEvent(String, String, String)}, or as listed by {@code GET
 * /webhook-events}.
 *
 * <p>Event types are listed in {@link WebhookEventTypes}.
 */
public class WebhookEvent extends ApiResponse {

  @JsonProperty("id")
  private String id;

  @JsonProperty("object")
  private String object;

  @JsonProperty("type")
  private String type;

  @JsonProperty("created")
  private Long created;

  @JsonProperty("livemode")
  private Boolean livemode;

  @JsonProperty("version")
  private Integer version;

  @JsonProperty("test")
  private Boolean test;

  @JsonProperty("data")
  private WebhookEventData data;

  @JsonProperty("request")
  private WebhookRequestInfo request;

  @JsonProperty("deliveries")
  private List<WebhookDelivery> deliveries;

  /** Creates an empty object; the client fills it from JSON. */
  public WebhookEvent() {}

  /**
   * The {@code id} field: the event id, {@code whevt_...}; use it to ignore repeats.
   *
   * @return the value, or {@code null} when absent
   */
  public String getId() {
    return id;
  }

  /**
   * The {@code object} field: always {@code webhook_event}.
   *
   * @return the value, or {@code null} when absent
   */
  public String getObject() {
    return object;
  }

  /**
   * The {@code type} field: the event type, for example {@code event.published}.
   *
   * @return the value, or {@code null} when absent
   */
  public String getType() {
    return type;
  }

  /**
   * The {@code created} field: when it happened, in Unix seconds.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getCreated() {
    return created;
  }

  /**
   * The {@code livemode} field: whether it is a LIVE event.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getLivemode() {
    return livemode;
  }

  /**
   * The {@code version} field: the payload version.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getVersion() {
    return version;
  }

  /**
   * The {@code test} field: whether it was sent as a test.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getTest() {
    return test;
  }

  /**
   * The {@code data} field: the object and extra facts.
   *
   * @return the value, or {@code null} when absent
   */
  public WebhookEventData getData() {
    return data;
  }

  /**
   * The {@code request} field: what caused it.
   *
   * @return the value, or {@code null} when absent
   */
  public WebhookRequestInfo getRequest() {
    return request;
  }

  /**
   * The {@code deliveries} field: the deliveries, in {@code GET /webhook-events} responses.
   *
   * @return the value, or {@code null} when absent
   */
  public List<WebhookDelivery> getDeliveries() {
    return deliveries;
  }

  /**
   * Converts {@code data.object} to a typed object, for example {@link Offer} or {@link Event}.
   *
   * @param type the class to convert to
   * @param <T> the type
   * @return the object, or {@code null} when the event has none
   */
  public <T> T dataObject(Class<T> type) {
    if (data == null || data.getObject() == null) {
      return null;
    }
    return Json.mapper().convertValue(data.getObject(), type);
  }

  /**
   * Returns {@code data.object} as an Offer, for the {@code offer.*} types.
   *
   * @return the Offer, or empty for other types
   */
  public Optional<Offer> asOffer() {
    return type != null && type.startsWith("offer.")
        ? Optional.ofNullable(dataObject(Offer.class))
        : Optional.empty();
  }

  /**
   * Returns {@code data.object} as an Event, for the {@code event.*} types.
   *
   * @return the Event, or empty for other types
   */
  public Optional<Event> asEvent() {
    return type != null && type.startsWith("event.")
        ? Optional.ofNullable(dataObject(Event.class))
        : Optional.empty();
  }
}
