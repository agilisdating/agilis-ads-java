package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/** An Event. */
public class Event extends ApiResponse {

  @JsonProperty("event_id")
  private Long eventId;

  @JsonProperty("title")
  private String title;

  @JsonProperty("description")
  private String description;

  @JsonProperty("category")
  private Category category;

  @JsonProperty("venue_name")
  private String venueName;

  @JsonProperty("address")
  private String address;

  @JsonProperty("city")
  private String city;

  @JsonProperty("country_code")
  private String countryCode;

  @JsonProperty("latitude")
  private Double latitude;

  @JsonProperty("longitude")
  private Double longitude;

  @JsonProperty("starts_at")
  private String startsAt;

  @JsonProperty("ends_at")
  private String endsAt;

  @JsonProperty("url")
  private String url;

  @JsonProperty("advertised_name")
  private String advertisedName;

  @JsonProperty("price_min")
  private BigDecimal priceMin;

  @JsonProperty("price_max")
  private BigDecimal priceMax;

  @JsonProperty("currency")
  private String currency;

  @JsonProperty("image")
  private String image;

  @JsonProperty("image_large")
  private String imageLarge;

  @JsonProperty("image_thumb")
  private String imageThumb;

  @JsonProperty("event_status")
  private String eventStatus;

  @JsonProperty("attendees")
  private String attendees;

  @JsonProperty("owner")
  private EventOwner owner;

  @JsonProperty("environment")
  private String environment;

  @JsonProperty("reschedule_count")
  private Integer rescheduleCount;

  @JsonProperty("cloned_from_event_id")
  private Long clonedFromEventId;

  @JsonProperty("creation_datetime")
  private String creationDatetime;

  @JsonProperty("last_edited_at")
  private String lastEditedAt;

  /** Creates an empty object; the client fills it from JSON. */
  public Event() {}

  /**
   * The {@code event_id} field: the Event id.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getEventId() {
    return eventId;
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
   * The {@code description} field: the description.
   *
   * @return the value, or {@code null} when absent
   */
  public String getDescription() {
    return description;
  }

  /**
   * The {@code category} field: the category.
   *
   * @return the value, or {@code null} when absent
   */
  public Category getCategory() {
    return category;
  }

  /**
   * The {@code venue_name} field: the venue name.
   *
   * @return the value, or {@code null} when absent
   */
  public String getVenueName() {
    return venueName;
  }

  /**
   * The {@code address} field: the street address.
   *
   * @return the value, or {@code null} when absent
   */
  public String getAddress() {
    return address;
  }

  /**
   * The {@code city} field: the city.
   *
   * @return the value, or {@code null} when absent
   */
  public String getCity() {
    return city;
  }

  /**
   * The {@code country_code} field: the ISO 3166-1 alpha-2 country code.
   *
   * @return the value, or {@code null} when absent
   */
  public String getCountryCode() {
    return countryCode;
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
   * The {@code starts_at} field: the start, ISO 8601 UTC.
   *
   * @return the value, or {@code null} when absent
   */
  public String getStartsAt() {
    return startsAt;
  }

  /**
   * The {@code ends_at} field: the end, ISO 8601 UTC.
   *
   * @return the value, or {@code null} when absent
   */
  public String getEndsAt() {
    return endsAt;
  }

  /**
   * The {@code url} field: the booking page.
   *
   * @return the value, or {@code null} when absent
   */
  public String getUrl() {
    return url;
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
   * The {@code price_min} field: the lowest ticket price.
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getPriceMin() {
    return priceMin;
  }

  /**
   * The {@code price_max} field: the highest ticket price.
   *
   * @return the value, or {@code null} when absent
   */
  public BigDecimal getPriceMax() {
    return priceMax;
  }

  /**
   * The {@code currency} field: the ticket price currency.
   *
   * @return the value, or {@code null} when absent
   */
  public String getCurrency() {
    return currency;
  }

  /**
   * The {@code image} field: the original image URL.
   *
   * @return the value, or {@code null} when absent
   */
  public String getImage() {
    return image;
  }

  /**
   * The {@code image_large} field: the large image URL.
   *
   * @return the value, or {@code null} when absent
   */
  public String getImageLarge() {
    return imageLarge;
  }

  /**
   * The {@code image_thumb} field: the thumbnail URL.
   *
   * @return the value, or {@code null} when absent
   */
  public String getImageThumb() {
    return imageThumb;
  }

  /**
   * The {@code event_status} field: the status.
   *
   * @return the value, or {@code null} when absent
   */
  public String getEventStatus() {
    return eventStatus;
  }

  /**
   * The {@code attendees} field: the number of members going, as a band.
   *
   * @return the value, or {@code null} when absent
   */
  public String getAttendees() {
    return attendees;
  }

  /**
   * The {@code owner} field: who published it.
   *
   * @return the value, or {@code null} when absent
   */
  public EventOwner getOwner() {
    return owner;
  }

  /**
   * The {@code environment} field: the mode, {@code TEST} or {@code LIVE}.
   *
   * @return the value, or {@code null} when absent
   */
  public String getEnvironment() {
    return environment;
  }

  /**
   * The {@code reschedule_count} field: how many times it was rescheduled.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getRescheduleCount() {
    return rescheduleCount;
  }

  /**
   * The {@code cloned_from_event_id} field: the Event whose image it reused.
   *
   * @return the value, or {@code null} when absent
   */
  public Long getClonedFromEventId() {
    return clonedFromEventId;
  }

  /**
   * The {@code creation_datetime} field: when it was created.
   *
   * @return the value, or {@code null} when absent
   */
  public String getCreationDatetime() {
    return creationDatetime;
  }

  /**
   * The {@code last_edited_at} field: when it was last edited.
   *
   * @return the value, or {@code null} when absent
   */
  public String getLastEditedAt() {
    return lastEditedAt;
  }
}
