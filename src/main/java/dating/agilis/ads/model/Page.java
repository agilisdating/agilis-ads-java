package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;

/**
 * One page of a list: {@code GET /offers}, {@code GET /events} or {@code GET /webhook-events}.
 *
 * <p>Use {@link #hasMore()} and {@link #getNextCursor()} to fetch the next page yourself, or the
 * {@code listAutoPaging} methods to iterate over every item.
 *
 * @param <T> the item type
 */
public class Page<T> extends ApiResponse {

  @JsonProperty("data")
  private List<T> data;

  @JsonProperty("pagination")
  private Pagination pagination;

  /** Creates an empty page; the client fills it from JSON. */
  public Page() {}

  /**
   * The items on this page.
   *
   * @return the items, never {@code null}
   */
  public List<T> getData() {
    return data == null ? Collections.emptyList() : Collections.unmodifiableList(data);
  }

  /**
   * The paging details.
   *
   * @return the details, or {@code null} when absent
   */
  public Pagination getPagination() {
    return pagination;
  }

  /**
   * Whether another page follows.
   *
   * @return {@code true} when {@code pagination.has_more} is true and a cursor is given
   */
  public boolean hasMore() {
    return pagination != null
        && Boolean.TRUE.equals(pagination.getHasMore())
        && pagination.getNextCursor() != null
        && !pagination.getNextCursor().isEmpty();
  }

  /**
   * The cursor for the next page.
   *
   * @return the cursor, or {@code null} on the last page
   */
  public String getNextCursor() {
    return pagination == null ? null : pagination.getNextCursor();
  }
}
