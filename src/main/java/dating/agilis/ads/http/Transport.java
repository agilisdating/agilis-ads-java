package dating.agilis.ads.http;

import java.io.IOException;

/**
 * Sends one HTTP request and returns the response.
 *
 * <p>The client uses {@link JdkHttpTransport}, built on {@link java.net.http.HttpClient}, unless
 * another transport is given to {@link dating.agilis.ads.AgilisClient.Builder#transport}. A
 * transport sends the request once: retries are handled by the client.
 */
@FunctionalInterface
public interface Transport {

  /**
   * Sends a request.
   *
   * @param request the request to send
   * @return the response, whatever its HTTP status
   * @throws IOException when no response was received (a connection error or a timeout)
   * @throws InterruptedException when the calling thread was interrupted
   */
  TransportResponse send(TransportRequest request) throws IOException, InterruptedException;
}
