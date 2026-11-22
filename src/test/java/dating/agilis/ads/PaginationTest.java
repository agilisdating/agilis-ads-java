package dating.agilis.ads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dating.agilis.ads.model.Event;
import dating.agilis.ads.model.Offer;
import dating.agilis.ads.model.Page;
import dating.agilis.ads.model.WebhookEvent;
import dating.agilis.ads.param.ListOffersParams;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class PaginationTest {

  private static String page(String idField, int[] ids, String nextCursor) {
    StringBuilder data = new StringBuilder();
    for (int id : ids) {
      if (data.length() > 0) {
        data.append(',');
      }
      data.append("{\"").append(idField).append("\":").append(id).append('}');
    }
    String cursor = nextCursor == null ? "null" : "\"" + nextCursor + "\"";
    return "{\"data\":["
        + data
        + "],\"pagination\":{\"limit\":2,\"count\":"
        + ids.length
        + ",\"has_more\":"
        + (nextCursor != null)
        + ",\"next_cursor\":"
        + cursor
        + "}}";
  }

  @Test
  void onePageExposesCursor() {
    FakeTransport http = new FakeTransport();
    http.ok(page("offer_id", new int[] {1, 2}, "c2"));
    Page<Offer> first = http.client().offers().list();
    assertTrue(first.hasMore());
    assertEquals("c2", first.getNextCursor());
    assertEquals(2, first.getPagination().getCount());
    assertEquals(1, http.requests.size());
  }

  @Test
  void autoPagingFollowsCursorsUntilHasMoreIsFalse() {
    FakeTransport http = new FakeTransport();
    http.ok(page("offer_id", new int[] {1, 2}, "c2"))
        .ok(page("offer_id", new int[] {3, 4}, "c3"))
        .ok(page("offer_id", new int[] {5}, null));
    List<Long> ids = new ArrayList<>();
    for (Offer offer :
        http.client().offers().listAutoPaging(ListOffersParams.builder().limit(2).build())) {
      ids.add(offer.getOfferId());
    }
    assertEquals(List.of(1L, 2L, 3L, 4L, 5L), ids);
    assertEquals(3, http.requests.size());
    assertEquals("limit=2&environment=TEST", http.requests.get(0).uri().getRawQuery());
    assertEquals("limit=2&environment=TEST&cursor=c2", http.requests.get(1).uri().getRawQuery());
    assertEquals("limit=2&environment=TEST&cursor=c3", http.requests.get(2).uri().getRawQuery());
  }

  @Test
  void autoPagingIsLazyAndSkipsEmptyPages() {
    FakeTransport http = new FakeTransport();
    http.ok(page("event_id", new int[] {}, "c2")).ok(page("event_id", new int[] {9}, null));
    Iterable<Event> events = http.client().events().listAutoPaging();
    assertEquals(0, http.requests.size());
    Iterator<Event> it = events.iterator();
    assertTrue(it.hasNext());
    assertEquals(9L, it.next().getEventId());
    assertFalse(it.hasNext());
    assertEquals(2, http.requests.size());
  }

  @Test
  void autoPagingStream() {
    FakeTransport http = new FakeTransport();
    http.ok(
            "{\"data\":[{\"id\":\"whevt_1\"}],\"pagination\":{\"has_more\":true,"
                + "\"next_cursor\":\"n\"}}")
        .ok("{\"data\":[{\"id\":\"whevt_2\"}],\"pagination\":{\"has_more\":false}}");
    List<String> ids =
        http.client().webhookEvents().listAutoPaging().stream()
            .map(WebhookEvent::getId)
            .collect(Collectors.toList());
    assertEquals(List.of("whevt_1", "whevt_2"), ids);
    assertEquals("/webhook-events?cursor=n", path(http, 1));
  }

  @Test
  void autoPagingStopsWhenTheCursorRepeats() {
    FakeTransport http = new FakeTransport();
    http.ok(page("offer_id", new int[] {1}, "same")).ok(page("offer_id", new int[] {2}, "same"));
    long count = http.client().offers().listAutoPaging().stream().count();
    assertEquals(2, count);
    assertEquals(2, http.requests.size());
  }

  @Test
  void autoPagingStartsAtTheGivenCursor() {
    FakeTransport http = new FakeTransport();
    http.ok(page("offer_id", new int[] {7}, null));
    http.client()
        .offers()
        .listAutoPaging(ListOffersParams.builder().cursor("start").build())
        .forEach(o -> {});
    assertEquals("/offers?cursor=start&environment=TEST", path(http, 0));
  }

  private static String path(FakeTransport http, int index) {
    var uri = http.requests.get(index).uri();
    return uri.getRawPath() + "?" + uri.getRawQuery();
  }
}
