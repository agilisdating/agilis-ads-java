import com.sun.net.httpserver.HttpServer;
import dating.agilis.ads.Webhooks;
import dating.agilis.ads.exception.SignatureVerificationException;
import dating.agilis.ads.model.WebhookEvent;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * A minimal webhook receiver on http://localhost:8000/webhooks.
 *
 * <p>Run with your endpoint's secret: {@code AGILIS_WEBHOOK_SECRET=whsec_... java -cp <classpath>
 * examples/WebhookReceiver.java}. In production, receive webhooks over HTTPS.
 */
public class WebhookReceiver {

  public static void main(String[] args) throws Exception {
    String secret = System.getenv("AGILIS_WEBHOOK_SECRET");
    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
    server.createContext(
        "/webhooks",
        exchange -> {
          byte[] body = exchange.getRequestBody().readAllBytes();
          String signature = exchange.getRequestHeaders().getFirst(Webhooks.SIGNATURE_HEADER);
          int status;
          try {
            WebhookEvent event =
                Webhooks.constructEvent(
                    body, signature, secret, Webhooks.DEFAULT_TOLERANCE_SECONDS);
            // Answer quickly, then do the work. Use event.getId() to ignore repeats.
            System.out.println("Received " + event.getType() + " " + event.getId());
            event.asEvent().ifPresent(e -> System.out.println("Event: " + e.getTitle()));
            event.asOffer().ifPresent(o -> System.out.println("Offer: " + o.getTitle()));
            status = 200;
          } catch (SignatureVerificationException e) {
            System.out.println("Refused: " + e.getMessage());
            status = 400;
          }
          byte[] reply = "{}".getBytes(StandardCharsets.UTF_8);
          exchange.getResponseHeaders().add("Content-Type", "application/json");
          exchange.sendResponseHeaders(status, reply.length);
          exchange.getResponseBody().write(reply);
          exchange.close();
        });
    server.start();
    System.out.println("Listening on http://localhost:8000/webhooks");
  }
}
