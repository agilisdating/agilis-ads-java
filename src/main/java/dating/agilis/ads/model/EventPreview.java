package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/** The response of {@code POST /events/preview}. */
public class EventPreview extends ApiResponse {

  @JsonProperty("category")
  private Category category;

  @JsonProperty("listing_fee")
  private BigDecimal listingFee;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("balance")
  private BigDecimal balance;

  @JsonProperty("balance_after")
  private BigDecimal balanceAfter;

  @JsonProperty("balance_sufficient")
  private Boolean balanceSufficient;

  @JsonProperty("live_publishing_open")
  private Boolean livePublishingOpen;

  @JsonProperty("price_gbp")
  private BigDecimal priceGbp;

  @JsonProperty("image")
  private PreviewImage image;

  /** Creates an empty object; the client fills it from JSON. */
  public EventPreview() {}

  /**
   * The {@code category} field: the category determined for the Event.
   *
   * @return the value, or {@code null} when absent
   */
  public Category getCategory() {
    return category;
  }

  /**
   * The {@code listing_fee} field: the listing fee, in the billing currency.
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getListingFee() {
    return listingFee;
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
   * The {@code balance} field: the balance now.
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getBalance() {
    return balance;
  }

  /**
   * The {@code balance_after} field: the balance after publishing.
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getBalanceAfter() {
    return balanceAfter;
  }

  /**
   * The {@code balance_sufficient} field: whether the balance covers the fee.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getBalanceSufficient() {
    return balanceSufficient;
  }

  /**
   * The {@code live_publishing_open} field: whether LIVE publishing is open.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getLivePublishingOpen() {
    return livePublishingOpen;
  }

  /**
   * The {@code price_gbp} field: the fee in pounds sterling.
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getPriceGbp() {
    return priceGbp;
  }

  /**
   * The {@code image} field: the framed image, when one was sent.
   *
   * @return the value, or {@code null} when absent
   */
  public PreviewImage getImage() {
    return image;
  }
}
