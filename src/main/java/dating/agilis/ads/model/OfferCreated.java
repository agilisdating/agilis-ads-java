package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The response of {@code POST /offers}. A dry run's extra fields are in {@link #get(String)}. */
public class OfferCreated extends ApiResponse {

  @JsonProperty("offer_id")
  private Long offerId;

  @JsonProperty("status")
  private String status;

  @JsonProperty("payment_url")
  private String paymentUrl;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("dry_run")
  private Boolean dryRun;

  /** Creates an empty object; the client fills it from JSON. */
  public OfferCreated() {}

  /**
   * The {@code offer_id} field: the new Offer's id.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getOfferId() {
    return offerId;
  }

  /**
   * The {@code status} field: the Offer's status.
   *
   * @return the value, or {@code null} when absent
   */
  public String getStatus() {
    return status;
  }

  /**
   * The {@code payment_url} field: the Stripe payment link, for {@code payment_method=E}.
   *
   * @return the value, or {@code null} when absent
   */
  public String getPaymentUrl() {
    return paymentUrl;
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
   * The {@code dry_run} field: {@code true} for a dry run, when nothing was created.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getDryRun() {
    return dryRun;
  }
}
