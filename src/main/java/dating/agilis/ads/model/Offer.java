package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** An Offer, as listed by {@code GET /offers}. */
public class Offer extends ApiObject {

  @JsonProperty("offer_id")
  private Long offerId;

  @JsonProperty("title")
  private String title;

  @JsonProperty("category")
  private String category;

  @JsonProperty("description")
  private String description;

  @JsonProperty("terms_and_conditions")
  private String termsAndConditions;

  @JsonProperty("advertised_name")
  private String advertisedName;

  @JsonProperty("push_notification_title")
  private String pushNotificationTitle;

  @JsonProperty("push_notification_message")
  private String pushNotificationMessage;

  @JsonProperty("button_preset")
  private String buttonPreset;

  @JsonProperty("button_text")
  private String buttonText;

  @JsonProperty("url")
  private String url;

  @JsonProperty("image")
  private String image;

  @JsonProperty("latitude")
  private Double latitude;

  @JsonProperty("longitude")
  private Double longitude;

  @JsonProperty("radius")
  private Integer radius;

  @JsonProperty("gender")
  private List<String> gender;

  @JsonProperty("interests")
  private List<Interest> interests;

  @JsonProperty("start_datetime")
  private String startDatetime;

  @JsonProperty("expiry_datetime")
  private String expiryDatetime;

  @JsonProperty("creation_datetime")
  private String creationDatetime;

  @JsonProperty("offer_status")
  private String offerStatus;

  @JsonProperty("status")
  private String status;

  /** Creates an empty object; the client fills it from JSON. */
  public Offer() {}

  /**
   * The {@code offer_id} field: the Offer id.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getOfferId() {
    return offerId;
  }

  /**
   * The {@code title} field: the title.
   *
   * @return the value, or {@code null} when absent
   */
  public String getTitle() {
    return title;
  }

  /**
   * The {@code category} field: the category name.
   *
   * @return the value, or {@code null} when absent
   */
  public String getCategory() {
    return category;
  }

  /**
   * The {@code description} field: the description.
   *
   * @return the value, or {@code null} when absent
   */
  public String getDescription() {
    return description;
  }

  /**
   * The {@code terms_and_conditions} field: the terms and conditions.
   *
   * @return the value, or {@code null} when absent
   */
  public String getTermsAndConditions() {
    return termsAndConditions;
  }

  /**
   * The {@code advertised_name} field: the business name shown instead of the trading name.
   *
   * @return the value, or {@code null} when absent
   */
  public String getAdvertisedName() {
    return advertisedName;
  }

  /**
   * The {@code push_notification_title} field: the notification title.
   *
   * @return the value, or {@code null} when absent
   */
  public String getPushNotificationTitle() {
    return pushNotificationTitle;
  }

  /**
   * The {@code push_notification_message} field: the notification message.
   *
   * @return the value, or {@code null} when absent
   */
  public String getPushNotificationMessage() {
    return pushNotificationMessage;
  }

  /**
   * The {@code button_preset} field: the button preset.
   *
   * @return the value, or {@code null} when absent
   */
  public String getButtonPreset() {
    return buttonPreset;
  }

  /**
   * The {@code button_text} field: the custom button text.
   *
   * @return the value, or {@code null} when absent
   */
  public String getButtonText() {
    return buttonText;
  }

  /**
   * The {@code url} field: the Offer page.
   *
   * @return the value, or {@code null} when absent
   */
  public String getUrl() {
    return url;
  }

  /**
   * The {@code image} field: the image URL.
   *
   * @return the value, or {@code null} when absent
   */
  public String getImage() {
    return image;
  }

  /**
   * The {@code latitude} field: the latitude.
   *
   * @return the value, or {@code null} when absent
   */
  public Double getLatitude() {
    return latitude;
  }

  /**
   * The {@code longitude} field: the longitude.
   *
   * @return the value, or {@code null} when absent
   */
  public Double getLongitude() {
    return longitude;
  }

  /**
   * The {@code radius} field: the radius in miles.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getRadius() {
    return radius;
  }

  /**
   * The {@code gender} field: the gender targeting.
   *
   * @return the value, or {@code null} when absent
   */
  public List<String> getGender() {
    return gender;
  }

  /**
   * The {@code interests} field: the interest targeting.
   *
   * @return the value, or {@code null} when absent
   */
  public List<Interest> getInterests() {
    return interests;
  }

  /**
   * The {@code start_datetime} field: the start, ISO 8601.
   *
   * @return the value, or {@code null} when absent
   */
  public String getStartDatetime() {
    return startDatetime;
  }

  /**
   * The {@code expiry_datetime} field: the end, ISO 8601.
   *
   * @return the value, or {@code null} when absent
   */
  public String getExpiryDatetime() {
    return expiryDatetime;
  }

  /**
   * The {@code creation_datetime} field: when the Offer was created, ISO 8601.
   *
   * @return the value, or {@code null} when absent
   */
  public String getCreationDatetime() {
    return creationDatetime;
  }

  /**
   * The {@code offer_status} field: the lifecycle status.
   *
   * @return the value, or {@code null} when absent
   */
  public String getOfferStatus() {
    return offerStatus;
  }

  /**
   * The {@code status} field: the status.
   *
   * @return the value, or {@code null} when absent
   */
  public String getStatus() {
    return status;
  }
}
