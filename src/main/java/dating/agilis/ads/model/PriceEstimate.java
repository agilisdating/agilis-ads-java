package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/** The response of {@code POST /offers/price-estimate}. */
public class PriceEstimate extends ApiResponse {

  @JsonProperty("price_estimate")
  private BigDecimal priceEstimate;

  @JsonProperty("matching_users")
  private Long matchingUsers;

  @JsonProperty("ad_units")
  private BigDecimal adUnits;

  @JsonProperty("ad_unit_price")
  private BigDecimal adUnitPrice;

  @JsonProperty("estimated_cost")
  private BigDecimal estimatedCost;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("exchange_rate_per_gbp")
  private BigDecimal exchangeRatePerGbp;

  @JsonProperty("exchange_rate_date")
  private String exchangeRateDate;

  /** Creates an empty object; the client fills it from JSON. */
  public PriceEstimate() {}

  /**
   * The {@code price_estimate} field: the size of the Offer in Ad Units (not money).
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getPriceEstimate() {
    return priceEstimate;
  }

  /**
   * The {@code matching_users} field: how many members the targeting reaches.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getMatchingUsers() {
    return matchingUsers;
  }

  /**
   * The {@code ad_units} field: the number of Ad Units.
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getAdUnits() {
    return adUnits;
  }

  /**
   * The {@code ad_unit_price} field: the price of one Ad Unit, in the billing currency.
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getAdUnitPrice() {
    return adUnitPrice;
  }

  /**
   * The {@code estimated_cost} field: what the Offer will cost, in the billing currency.
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getEstimatedCost() {
    return estimatedCost;
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
   * The {@code exchange_rate_per_gbp} field: the exchange rate used, per pound sterling.
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getExchangeRatePerGbp() {
    return exchangeRatePerGbp;
  }

  /**
   * The {@code exchange_rate_date} field: the date of the exchange rate.
   *
   * @return the value, or {@code null} when absent
   */
  public String getExchangeRateDate() {
    return exchangeRateDate;
  }
}
