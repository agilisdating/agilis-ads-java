package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/** The response of {@code POST /events}. A dry run's extra fields are in {@link #get(String)}. */
public class EventPublished extends ApiResponse {

  @JsonProperty("event_id")
  private Long eventId;

  @JsonProperty("status")
  private String status;

  @JsonProperty("payment_url")
  private String paymentUrl;

  @JsonProperty("amount_charged")
  private BigDecimal amountCharged;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("category")
  private Category category;

  @JsonProperty("dry_run")
  private Boolean dryRun;

  /** Creates an empty object; the client fills it from JSON. */
  public EventPublished() {}

  /**
   * The {@code event_id} field: the new Event's id.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getEventId() {
    return eventId;
  }

  /**
   * The {@code status} field: the Event's status.
   *
   * @return the value, or {@code null} when absent
   */
  public String getStatus() {
    return status;
  }

  /**
   * The {@code payment_url} field: always {@code null}: Events are paid from the balance.
   *
   * @return the value, or {@code null} when absent
   */
  public String getPaymentUrl() {
    return paymentUrl;
  }

  /**
   * The {@code amount_charged} field: what was taken from the balance (0 in Test mode).
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getAmountCharged() {
    return amountCharged;
  }

  /**
   * The {@code currency} field: the billing currency.
   *
   * @return the value, or {@code null} when absent
   */
  public String getCurrency() {
    return currency;
  }

  /**
   * The {@code category} field: the category determined for the Event.
   *
   * @return the value, or {@code null} when absent
   */
  public Category getCategory() {
    return category;
  }

  /**
   * The {@code dry_run} field: {@code true} for a dry run, when nothing was created.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getDryRun() {
    return dryRun;
  }
}
