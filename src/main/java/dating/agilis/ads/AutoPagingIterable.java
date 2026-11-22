package dating.agilis.ads;

import dating.agilis.ads.model.Page;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Every item of a list, across pages. Pages are fetched as the iteration reaches them, following
 * {@code next_cursor} until {@code has_more} is false. Each call to {@link #iterator()} starts
 * again from the first page.
 *
 * @param <T> the item type
 */
public final class AutoPagingIterable<T> implements Iterable<T> {

  private final Function<String, Page<T>> fetch;
  private final String firstCursor;

  AutoPagingIterable(Function<String, Page<T>> fetch, String firstCursor) {
    this.fetch = Objects.requireNonNull(fetch, "fetch");
    this.firstCursor = firstCursor;
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<>() {
      private Page<T> page;
      private List<T> items;
      private int index;
      private String cursor;

      @Override
      public boolean hasNext() {
        if (page == null) {
          load(firstCursor);
        }
        while (index >= items.size()) {
          String next = page.getNextCursor();
          if (!page.hasMore() || next.equals(cursor)) {
            return false;
          }
          load(next);
        }
        return true;
      }

      @Override
      public T next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        return items.get(index++);
      }

      private void load(String nextCursor) {
        cursor = nextCursor;
        page = fetch.apply(nextCursor);
        items = page.getData();
        index = 0;
      }
    };
  }

  /**
   * Returns the items as a sequential stream. Pages are fetched as the stream is consumed.
   *
   * @return the stream
   */
  public Stream<T> stream() {
    return StreamSupport.stream(spliterator(), false);
  }
}
