package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

/** The response of {@code GET /events/{event_id}}: an Event with its management details. */
public class EventDetail extends Event {

  @JsonProperty("can_manage")
  private Boolean canManage;

  @JsonProperty("can_delete")
  private Boolean canDelete;

  @JsonProperty("listing_price")
  private BigDecimal listingPrice;

  @JsonProperty("billing_currency")
  private String billingCurrency;

  @JsonProperty("invoice_id")
  private String invoiceId;

  @JsonProperty("app_synced_at")
  private String appSyncedAt;

  @JsonProperty("has_member_interaction")
  private Boolean hasMemberInteraction;

  @JsonProperty("image_master")
  private ImageMaster imageMaster;

  @JsonProperty("history")
  private List<EventHistoryEntry> history;

  /** Creates an empty object; the client fills it from JSON. */
  public EventDetail() {}

  /**
   * The {@code can_manage} field: whether this key may manage the Event.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getCanManage() {
    return canManage;
  }

  /**
   * The {@code can_delete} field: whether the Event can be deleted.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getCanDelete() {
    return canDelete;
  }

  /**
   * The {@code listing_price} field: the listing price charged.
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getListingPrice() {
    return listingPrice;
  }

  /**
   * The {@code billing_currency} field: the billing currency.
   *
   * @return the value, or {@code null} when absent
   */
  public String getBillingCurrency() {
    return billingCurrency;
  }

  /**
   * The {@code invoice_id} field: the invoice id.
   *
   * @return the value, or {@code null} when absent
   */
  public String getInvoiceId() {
    return invoiceId;
  }

  /**
   * The {@code app_synced_at} field: when the apps last received it.
   *
   * @return the value, or {@code null} when absent
   */
  public String getAppSyncedAt() {
    return appSyncedAt;
  }

  /**
   * The {@code has_member_interaction} field: whether members have interacted with it.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getHasMemberInteraction() {
    return hasMemberInteraction;
  }

  /**
   * The {@code image_master} field: the stored original image.
   *
   * @return the value, or {@code null} when absent
   */
  public ImageMaster getImageMaster() {
    return imageMaster;
  }

  /**
   * The {@code history} field: the history.
   *
   * @return the value, or {@code null} when absent
   */
  public List<EventHistoryEntry> getHistory() {
    return history;
  }
}
