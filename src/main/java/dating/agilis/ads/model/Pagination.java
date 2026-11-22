package dating.agilis.ads.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The paging details of a list response. */
public class Pagination extends ApiObject {

  @JsonProperty("limit")
  private Integer limit;

  @JsonProperty("count")
  private Integer count;

  @JsonProperty("has_more")
  private Boolean hasMore;

  @JsonProperty("next_cursor")
  private String nextCursor;

  /** Creates an empty object; the client fills it from JSON. */
  public Pagination() {}

  /**
   * The {@code limit} field: the page size.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getLimit() {
    return limit;
  }

  /**
   * The {@code count} field: the number of items on this page.
   *
   * @return the value, or {@code null} when absent
   */
  public Integer getCount() {
    return count;
  }

  /**
   * The {@code has_more} field: whether another page follows.
   *
   * @return the value, or {@code null} when absent
   */
  public Boolean getHasMore() {
    return hasMore;
  }

  /**
   * The {@code next_cursor} field: the cursor for the next page, or {@code null} on the last page.
   *
   * @return the value, or {@code null} when absent
   */
  public String getNextCursor() {
    return nextCursor;
  }
}
