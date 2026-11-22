package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A JSON object returned by the API.
 *
 * <p>Fields this version of the library does not know are kept, so a new API field never breaks a
 * client: read them with {@link #get(String)} or {@link #getExtraFields()}.
 */
public abstract class ApiObject {

  private final Map<String, Object> extraFields = new LinkedHashMap<>();

  /** Creates an empty object. */
  protected ApiObject() {}

  /**
   * Keeps a field that has no typed getter.
   *
   * @param name the field name
   * @param value the field value
   */
  @JsonAnySetter
  protected void setExtraField(String name, Object value) {
    extraFields.put(name, value);
  }

  /**
   * The fields that have no typed getter, as parsed JSON (maps, lists, strings, numbers, booleans
   * and nulls).
   *
   * @return the fields, in the order received
   */
  @JsonAnyGetter
  public Map<String, Object> getExtraFields() {
    return Collections.unmodifiableMap(extraFields);
  }

  /**
   * Returns a field that has no typed getter.
   *
   * @param name the field name, as sent by the API
   * @return the value, or {@code null} when absent
   */
  public Object get(String name) {
    return extraFields.get(name);
  }
}
