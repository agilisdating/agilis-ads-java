package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

/** The response of {@code GET /event-config}. */
public class EventConfig extends ApiResponse {

  @JsonProperty("listing_fee")
  private BigDecimal listingFee;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("exchange_rate_per_gbp")
  private BigDecimal exchangeRatePerGbp;

  @JsonProperty("exchange_rate_date")
  private String exchangeRateDate;

  @JsonProperty("environment")
  private String environment;

  @JsonProperty("balance")
  private BigDecimal balance;

  @JsonProperty("organisation_balance")
  private BigDecimal organisationBalance;

  @JsonProperty("live_publishing_open")
  private Boolean livePublishingOpen;

  @JsonProperty("currencies")
  private List<String> currencies;

  @JsonProperty("image")
  private ImageRules image;

  @JsonProperty("max_years_ahead")
  private Integer maxYearsAhead;

  /** Creates an empty object; the client fills it from JSON. */
  public EventConfig() {}

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

  /**
   * The {@code environment} field: the mode.
   *
   * @return the value, or {@code null} when absent
   */
  public String getEnvironment() {
    return environment;
  }

  /**
   * The {@code balance} field: your balance in this mode.
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getBalance() {
    return balance;
  }

  /**
   * The {@code organisation_balance} field: the organisation's balance in this mode.
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getOrganisationBalance() {
    return organisationBalance;
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
   * The {@code currencies} field: the ticket price currencies.
   *
   * @return the value, or {@code null} when absent
   */
  public List<String> getCurrencies() {
    return currencies;
  }

  /**
   * The {@code image} field: the image rules.
   *
   * @return the value, or {@code null} when absent
   */
  public ImageRules getImage() {
    return image;
  }

  /**
   * The {@code max_years_ahead} field: how many years ahead an Event may start.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getMaxYearsAhead() {
    return maxYearsAhead;
  }
}
