package dating.agilis.ads.param;

/** Parameters for the delete and cancel operations. */
public final class DryRunParams extends Params {

  private final boolean dryRun;

  private DryRunParams(Builder builder) {
    super(builder);
    this.dryRun = builder.dryRun;
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
   * Whether to send {@code dry_run=true}.
   *
   * @return {@code true} for a dry run
   */
  public boolean isDryRun() {
    return dryRun;
  }

  /** Builds {@link DryRunParams}. */
  public static final class Builder extends Params.Builder<Builder> {

    private boolean dryRun;

    private Builder() {}

    @Override
    protected Builder self() {
      return this;
    }

    /**
     * Sends {@code dry_run=true}: the API checks the request and answers what would happen, without
     * changing anything.
     *
     * @param dryRun {@code true} for a dry run
     * @return this builder
     */
    public Builder dryRun(boolean dryRun) {
      this.dryRun = dryRun;
      return this;
    }

    /**
     * Builds the parameters.
     *
     * @return the parameters
     */
    public DryRunParams build() {
      return new DryRunParams(this);
    }
  }
}
