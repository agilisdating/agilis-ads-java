package dating.agilis.ads;

/**
 * The mode an API key works in. A {@code pk_test_} key is {@link #TEST} and a {@code pk_live_} key
 * is {@link #LIVE}.
 */
public enum Mode {
  /** Test mode: members never see what you create, and nothing is charged. */
  TEST("TEST"),
  /** Live mode: changes are real, and Offers and Events are paid for. */
  LIVE("LIVE"),
  /** The key has neither prefix. The client then fills in no {@code environment}. */
  UNKNOWN(null);

  private final String environment;

  Mode(String environment) {
    this.environment = environment;
  }

  /**
   * The {@code environment} value for this mode.
   *
   * @return {@code TEST} or {@code LIVE}, or {@code null} for {@link #UNKNOWN}
   */
  public String environment() {
    return environment;
  }

  /**
   * Returns the mode of an API key, from its prefix.
   *
   * @param apiKey the key
   * @return the mode
   */
  public static Mode fromApiKey(String apiKey) {
    if (apiKey != null && apiKey.startsWith("pk_test_")) {
      return TEST;
    }
    if (apiKey != null && apiKey.startsWith("pk_live_")) {
      return LIVE;
    }
    return UNKNOWN;
  }
}
