package dating.agilis.ads.http;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;

/** A {@link Transport} that uses {@link java.net.http.HttpClient}. */
public final class JdkHttpTransport implements Transport {

  private final HttpClient httpClient;

  /**
   * Creates a transport around an HTTP client.
   *
   * @param httpClient the client to send requests with
   */
  public JdkHttpTransport(HttpClient httpClient) {
    this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
  }

  @Override
  public TransportResponse send(TransportRequest request) throws IOException, InterruptedException {
    HttpRequest.BodyPublisher publisher =
        request.body() == null
            ? HttpRequest.BodyPublishers.noBody()
            : HttpRequest.BodyPublishers.ofByteArray(request.body());
    HttpRequest.Builder builder =
        HttpRequest.newBuilder(request.uri()).method(request.method(), publisher);
    if (request.timeout() != null) {
      builder.timeout(request.timeout());
    }
    for (Map.Entry<String, String> header : request.headers().entrySet()) {
      builder.header(header.getKey(), header.getValue());
    }
    HttpResponse<byte[]> response =
        httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
    return new TransportResponse(response.statusCode(), response.headers().map(), response.body());
  }
}
