package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/** A webhook endpoint. */
public class WebhookEndpoint extends ApiResponse {

  @JsonProperty("id")
  private String id;

  @JsonProperty("url")
  private String url;

  @JsonProperty("description")
  private String description;

  @JsonProperty("enabled_events")
  private List<String> enabledEvents;

  @JsonProperty("status")
  private String status;

  @JsonProperty("livemode")
  private Boolean livemode;

  @JsonProperty("created")
  private String created;

  @JsonProperty("last_success_at")
  private String lastSuccessAt;

  @JsonProperty("failing_since")
  private String failingSince;

  @JsonProperty("disabled_by")
  private String disabledBy;

  @JsonProperty("disabled_reason")
  private String disabledReason;

  @JsonProperty("previous_secret_expires_at")
  private String previousSecretExpiresAt;

  @JsonProperty("secret")
  private String secret;

  /** Creates an empty object; the client fills it from JSON. */
  public WebhookEndpoint() {}

  /**
   * The {@code id} field: the endpoint id.
   *
   * @return the value, or {@code null} when absent
   */
  public String getId() {
    return id;
  }

  /**
   * The {@code url} field: the URL events are sent to.
   *
   * @return the value, or {@code null} when absent
   */
  public String getUrl() {
    return url;
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
   * The {@code enabled_events} field: the event types sent to it.
   *
   * @return the value, or {@code null} when absent
   */
  public List<String> getEnabledEvents() {
    return enabledEvents;
  }

  /**
   * The {@code status} field: {@code enabled} or {@code disabled}.
   *
   * @return the value, or {@code null} when absent
   */
  public String getStatus() {
    return status;
  }

  /**
   * The {@code livemode} field: whether it is a LIVE endpoint.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getLivemode() {
    return livemode;
  }

  /**
   * The {@code created} field: when it was created.
   *
   * @return the value, or {@code null} when absent
   */
  public String getCreated() {
    return created;
  }

  /**
   * The {@code last_success_at} field: the last successful delivery.
   *
   * @return the value, or {@code null} when absent
   */
  public String getLastSuccessAt() {
    return lastSuccessAt;
  }

  /**
   * The {@code failing_since} field: when deliveries started failing.
   *
   * @return the value, or {@code null} when absent
   */
  public String getFailingSince() {
    return failingSince;
  }

  /**
   * The {@code disabled_by} field: who turned it off.
   *
   * @return the value, or {@code null} when absent
   */
  public String getDisabledBy() {
    return disabledBy;
  }

  /**
   * The {@code disabled_reason} field: why it was turned off.
   *
   * @return the value, or {@code null} when absent
   */
  public String getDisabledReason() {
    return disabledReason;
  }

  /**
   * The {@code previous_secret_expires_at} field: when the previous secret stops being used.
   *
   * @return the value, or {@code null} when absent
   */
  public String getPreviousSecretExpiresAt() {
    return previousSecretExpiresAt;
  }

  /**
   * The {@code secret} field: the signing secret: present only when the endpoint is created and
   * when the secret is rolled.
   *
   * @return the value, or {@code null} when absent
   */
  public String getSecret() {
    return secret;
  }
}
