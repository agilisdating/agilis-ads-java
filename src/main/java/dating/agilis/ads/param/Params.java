package dating.agilis.ads.param;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The parameters of one operation, with the API's own field names.
 *
 * <p>Every builder also has {@link Builder#put(String, Object)}, to send a field this version of
 * the library has no method for.
 */
public abstract class Params {

  private final Map<String, Object> values;

  /**
   * Creates the parameters from a builder.
   *
   * @param builder the builder
   */
  protected Params(Builder<?> builder) {
    this.values = Collections.unmodifiableMap(new LinkedHashMap<>(builder.values));
  }

  /**
   * The fields to send, by API name. A list is sent as a JSON array, or as repeated {@code name[]}
   * parts in a multipart request.
   *
   * @return the fields, in the order they were set
   */
  public Map<String, Object> toMap() {
    return values;
  }

  /**
   * Returns one field.
   *
   * @param name the API field name
   * @return the value, or {@code null} when not set
   */
  public Object get(String name) {
    return values.get(name);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + values.keySet();
  }

  /**
   * The base of every parameters builder.
   *
   * @param <B> the builder type
   */
  public abstract static class Builder<B extends Builder<B>> {

    final Map<String, Object> values = new LinkedHashMap<>();

    /** Creates an empty builder. */
    protected Builder() {}

    /**
     * Returns this builder.
     *
     * @return this builder
     */
    protected abstract B self();

    /**
     * Sets any field by its API name, or removes it when {@code value} is {@code null}.
     *
     * @param name the API field name, for example {@code advertised_name}
     * @param value the value: a string, number, boolean, list, {@link Instant} or {@link
     *     FileUpload}
     * @return this builder
     */
    public B put(String name, Object value) {
      if (value == null) {
        values.remove(name);
      } else if (value instanceof Instant) {
        values.put(name, iso((Instant) value));
      } else {
        values.put(name, value);
      }
      return self();
    }

    B location(double latitude, double longitude) {
      return put("location", List.of(latitude, longitude));
    }

    B crop(int x, int y, int w) {
      put("crop[x]", x);
      put("crop[y]", y);
      return put("crop[w]", w);
    }

    B strings(String name, List<String> list) {
      return put(name, list == null ? null : List.copyOf(list));
    }

    B longs(String name, long... ids) {
      List<Long> list = new ArrayList<>();
      for (long id : ids) {
        list.add(id);
      }
      return put(name, list);
    }

    B decimal(String name, double value) {
      return put(name, BigDecimal.valueOf(value).stripTrailingZeros());
    }
  }

  static String iso(Instant instant) {
    return DateTimeFormatter.ISO_INSTANT.format(instant.truncatedTo(ChronoUnit.SECONDS));
  }
}
