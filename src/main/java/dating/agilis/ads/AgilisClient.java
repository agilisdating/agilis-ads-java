package dating.agilis.ads;

import dating.agilis.ads.http.JdkHttpTransport;
import dating.agilis.ads.http.Transport;
import dating.agilis.ads.model.CategoryList;
import dating.agilis.ads.model.EventCategoryList;
import dating.agilis.ads.model.EventCurrencyList;
import dating.agilis.ads.model.InterestList;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

/**
 * The client for the Agilis Advertising REST API.
 *
 * <pre>{@code
 * AgilisClient client = AgilisClient.builder()
 *     .apiKey(System.getenv("AGILIS_API_KEY"))
 *     .build();
 * CategoryList categories = client.categories().list();
 * }</pre>
 *
 * <p>The key decides the mode: a {@code pk_test_} key works in Test mode and a {@code pk_live_} key
 * in Live mode ({@link #mode()}). Operations that take an {@code environment} fill it in from the
 * key when it is left out. A client is safe to share between threads.
 */
public final class AgilisClient {

  /** The default base URL. */
  public static final String DEFAULT_BASE_URL = "https://ads.agilis.dating";

  /** The default timeout of each request. */
  public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(60);

  /** The default number of retries. */
  public static final int DEFAULT_MAX_RETRIES = 2;

  /** The environment variable read when no API key is given. */
  public static final String API_KEY_ENV = "AGILIS_API_KEY";

  /** This library's version. */
  public static final String VERSION = loadVersion();

  private final Requester requester;
  private final Mode mode;
  private final String baseUrl;
  private final Duration timeout;
  private final int maxRetries;

  private final ListService<CategoryList> categories;
  private final ListService<InterestList> interests;
  private final OffersService offers;
  private final ListService<EventCategoryList> eventCategories;
  private final ListService<EventCurrencyList> eventCurrencies;
  private final EventConfigService eventConfig;
  private final EventsService events;
  private final WebhookEndpointsService webhookEndpoints;
  private final WebhookEventsService webhookEvents;

  private AgilisClient(Builder builder) {
    String key = builder.apiKey;
    if (key == null || key.isBlank()) {
      key = builder.env.apply(API_KEY_ENV);
    }
    if (key == null || key.isBlank()) {
      throw new IllegalStateException(
          "No API key: pass one to AgilisClient.builder().apiKey(...) or set " + API_KEY_ENV);
    }
    key = key.trim();
    this.mode = Mode.fromApiKey(key);
    this.baseUrl = builder.baseUrl;
    this.timeout = builder.timeout;
    this.maxRetries = builder.maxRetries;
    Transport transport = builder.transport;
    if (transport == null) {
      HttpClient httpClient = builder.httpClient;
      if (httpClient == null) {
        httpClient = HttpClient.newBuilder().connectTimeout(timeout).build();
      }
      transport = new JdkHttpTransport(httpClient);
    }
    this.requester =
        new Requester(
            key,
            baseUrl,
            timeout,
            maxRetries,
            transport,
            builder.sleeper,
            builder.random,
            userAgent());
    this.categories = new ListService<>(this, "/categories", CategoryList.class);
    this.interests = new ListService<>(this, "/interests", InterestList.class);
    this.offers = new OffersService(this);
    this.eventCategories = new ListService<>(this, "/event-categories", EventCategoryList.class);
    this.eventCurrencies = new ListService<>(this, "/event-currencies", EventCurrencyList.class);
    this.eventConfig = new EventConfigService(this);
    this.events = new EventsService(this);
    this.webhookEndpoints = new WebhookEndpointsService(this);
    this.webhookEvents = new WebhookEventsService(this);
  }

  /**
   * Starts a builder.
   *
   * @return a new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * The mode of the API key.
   *
   * @return {@link Mode#TEST} or {@link Mode#LIVE}, or {@link Mode#UNKNOWN} for a key with neither
   *     prefix
   */
  public Mode mode() {
    return mode;
  }

  /**
   * The base URL requests are sent to.
   *
   * @return the base URL
   */
  public String baseUrl() {
    return baseUrl;
  }

  /**
   * The timeout of each request.
   *
   * @return the timeout
   */
  public Duration timeout() {
    return timeout;
  }

  /**
   * The maximum number of retries of each request.
   *
   * @return the number of retries
   */
  public int maxRetries() {
    return maxRetries;
  }

  /**
   * Offer categories: {@code GET /categories}.
   *
   * @return the categories resource
   */
  public ListService<CategoryList> categories() {
    return categories;
  }

  /**
   * Member interests for targeting: {@code GET /interests}.
   *
   * @return the interests resource
   */
  public ListService<InterestList> interests() {
    return interests;
  }

  /**
   * Offers.
   *
   * @return the Offers resource
   */
  public OffersService offers() {
    return offers;
  }

  /**
   * Event categories: {@code GET /event-categories}.
   *
   * @return the Event categories resource
   */
  public ListService<EventCategoryList> eventCategories() {
    return eventCategories;
  }

  /**
   * Ticket price currencies for Events: {@code GET /event-currencies}.
   *
   * @return the Event currencies resource
   */
  public ListService<EventCurrencyList> eventCurrencies() {
    return eventCurrencies;
  }

  /**
   * Event settings: {@code GET /event-config}.
   *
   * @return the Event config resource
   */
  public EventConfigService eventConfig() {
    return eventConfig;
  }

  /**
   * Events.
   *
   * @return the Events resource
   */
  public EventsService events() {
    return events;
  }

  /**
   * Webhook endpoints.
   *
   * @return the webhook endpoints resource
   */
  public WebhookEndpointsService webhookEndpoints() {
    return webhookEndpoints;
  }

  /**
   * Webhook events.
   *
   * @return the webhook events resource
   */
  public WebhookEventsService webhookEvents() {
    return webhookEvents;
  }

  Requester requester() {
    return requester;
  }

  Map<String, Object> withEnvironment(Map<String, Object> fields) {
    Map<String, Object> copy = new LinkedHashMap<>(fields);
    if (copy.get("environment") == null && mode.environment() != null) {
      copy.put("environment", mode.environment());
    }
    return copy;
  }

  static String idempotencyKey(String given) {
    return given == null || given.isBlank() ? UUID.randomUUID().toString() : given;
  }

  static String userAgent() {
    return "agilis-ads-java/" + VERSION + " (Java " + System.getProperty("java.version") + ")";
  }

  private static String loadVersion() {
    try (InputStream in = AgilisClient.class.getResourceAsStream("agilis-ads.properties")) {
      if (in != null) {
        Properties props = new Properties();
        props.load(in);
        String version = props.getProperty("version");
        if (version != null && !version.isBlank() && !version.startsWith("$")) {
          return version.trim();
        }
      }
    } catch (IOException e) {
      // fall through
    }
    return "0.0.0";
  }

  @Override
  public String toString() {
    return "AgilisClient[mode=" + mode + ", baseUrl=" + baseUrl + "]";
  }

  /** Builds an {@link AgilisClient}. */
  public static final class Builder {

    private String apiKey;
    private String baseUrl = DEFAULT_BASE_URL;
    private Duration timeout = DEFAULT_TIMEOUT;
    private int maxRetries = DEFAULT_MAX_RETRIES;
    private HttpClient httpClient;
    private Transport transport;
    private Requester.Sleeper sleeper = d -> Thread.sleep(d.toMillis());
    private DoubleSupplier random = () -> ThreadLocalRandom.current().nextDouble();
    private Function<String, String> env = System::getenv;

    private Builder() {}

    /**
     * Sets the API key. When none is given, {@code AGILIS_API_KEY} is read from the environment.
     *
     * @param apiKey a {@code pk_test_} or {@code pk_live_} key
     * @return this builder
     */
    public Builder apiKey(String apiKey) {
      this.apiKey = apiKey;
      return this;
    }

    /**
     * Sets the base URL. The default is {@value AgilisClient#DEFAULT_BASE_URL}.
     *
     * @param baseUrl the base URL
     * @return this builder
     */
    public Builder baseUrl(String baseUrl) {
      URI uri = URI.create(Objects.requireNonNull(baseUrl, "baseUrl"));
      if (uri.getScheme() == null || uri.getHost() == null) {
        throw new IllegalArgumentException("baseUrl must be an absolute http(s) URL");
      }
      this.baseUrl = baseUrl;
      return this;
    }

    /**
     * Sets the timeout of each request. The default is 60 seconds.
     *
     * @param timeout the timeout
     * @return this builder
     */
    public Builder timeout(Duration timeout) {
      Objects.requireNonNull(timeout, "timeout");
      if (timeout.isNegative() || timeout.isZero()) {
        throw new IllegalArgumentException("timeout must be positive");
      }
      this.timeout = timeout;
      return this;
    }

    /**
     * Sets the maximum number of retries of each request. The default is 2; 0 turns retries off.
     *
     * @param maxRetries the number of retries
     * @return this builder
     */
    public Builder maxRetries(int maxRetries) {
      if (maxRetries < 0) {
        throw new IllegalArgumentException("maxRetries must not be negative");
      }
      this.maxRetries = maxRetries;
      return this;
    }

    /**
     * Sets the {@link HttpClient} to send requests with, for example one with a proxy.
     *
     * @param httpClient the client
     * @return this builder
     */
    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
      return this;
    }

    /**
     * Sets a {@link Transport} to send requests with instead of {@link HttpClient}, for example in
     * tests.
     *
     * @param transport the transport
     * @return this builder
     */
    public Builder transport(Transport transport) {
      this.transport = Objects.requireNonNull(transport, "transport");
      return this;
    }

    Builder sleeper(Requester.Sleeper sleeper) {
      this.sleeper = sleeper;
      return this;
    }

    Builder random(DoubleSupplier random) {
      this.random = random;
      return this;
    }

    Builder env(Function<String, String> env) {
      this.env = env;
      return this;
    }

    /**
     * Builds the client.
     *
     * @return the client
     * @throws IllegalStateException when no API key was given or found in the environment
     */
    public AgilisClient build() {
      return new AgilisClient(this);
    }
  }
}
