package dating.agilis.ads;

import dating.agilis.ads.http.Transport;
import dating.agilis.ads.http.TransportRequest;
import dating.agilis.ads.http.TransportResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** A transport that records requests and answers from a queue. */
final class FakeTransport implements Transport {

  static final String TEST_KEY = "pk_test_unit0123456789abcdef";
  static final String LIVE_KEY = "pk_live_unit0123456789abcdef";

  final List<TransportRequest> requests = new ArrayList<>();
  final List<Duration> sleeps = new ArrayList<>();
  private final Deque<Object> queue = new ArrayDeque<>();

  FakeTransport respond(int status, String json, String... headerPairs) {
    Map<String, List<String>> headers = new LinkedHashMap<>();
    headers.put("Content-Type", List.of("application/json"));
    for (int i = 0; i + 1 < headerPairs.length; i += 2) {
      headers.put(headerPairs[i], List.of(headerPairs[i + 1]));
    }
    queue.add(new TransportResponse(status, headers, json.getBytes(StandardCharsets.UTF_8)));
    return this;
  }

  FakeTransport ok(String json) {
    return respond(200, json);
  }

  FakeTransport fail(IOException e) {
    queue.add(e);
    return this;
  }

  @Override
  public TransportResponse send(TransportRequest request) throws IOException {
    requests.add(request);
    Object next = queue.poll();
    if (next == null) {
      return new TransportResponse(
          200, Map.of(), "{\"return_code\":\"S1\"}".getBytes(StandardCharsets.UTF_8));
    }
    if (next instanceof IOException) {
      throw (IOException) next;
    }
    return (TransportResponse) next;
  }

  TransportRequest last() {
    return requests.get(requests.size() - 1);
  }

  String lastBody() {
    byte[] body = last().body();
    return body == null ? null : new String(body, StandardCharsets.UTF_8);
  }

  AgilisClient client() {
    return client(TEST_KEY);
  }

  AgilisClient client(String key) {
    return builder(key).build();
  }

  AgilisClient.Builder builder(String key) {
    return AgilisClient.builder()
        .apiKey(key)
        .transport(this)
        .sleeper(sleeps::add)
        .random(() -> 0.5)
        .env(name -> null);
  }
}
