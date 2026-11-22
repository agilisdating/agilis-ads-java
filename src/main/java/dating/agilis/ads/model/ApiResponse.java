package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A top-level API response. Besides its fields, it keeps the HTTP status and headers of the
 * response it came from.
 */
public abstract class ApiResponse extends ApiObject {

  @JsonProperty("return_code")
  private String returnCode;

  @JsonProperty("message")
  private String message;

  @JsonIgnore private ResponseInfo lastResponse;

  /** Creates an empty response. */
  protected ApiResponse() {}

  /**
   * The API's {@code return_code}: {@code S1} or {@code S2} on success.
   *
   * @return the code, or {@code null} when the response had none
   */
  public String getReturnCode() {
    return returnCode;
  }

  /**
   * The API's {@code message}.
   *
   * @return the message, or {@code null} when the response had none
   */
  public String getMessage() {
    return message;
  }

  /**
   * The HTTP status and headers of the response this object came from.
   *
   * @return the response details, or {@code null} for an object that did not come directly from an
   *     HTTP response (for example an item of a page, or a webhook event)
   */
  @JsonIgnore
  public ResponseInfo getLastResponse() {
    return lastResponse;
  }

  /**
   * Sets the response details. Used by the client.
   *
   * @param lastResponse the response details
   */
  @JsonIgnore
  public void setLastResponse(ResponseInfo lastResponse) {
    this.lastResponse = lastResponse;
  }
}
