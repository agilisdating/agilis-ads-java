package dating.agilis.ads.model;

import java.util.List;

/** The webhook event types, for {@link WebhookEvent#getType()} and endpoint subscriptions. */
public final class WebhookEventTypes {

  /** Subscribes an endpoint to every type. */
  public static final String ALL = "*";

  /** An Offer was created. */
  public static final String OFFER_CREATED = "offer.created";

  /** An Offer's payment succeeded. */
  public static final String OFFER_PAYMENT_SUCCEEDED = "offer.payment_succeeded";

  /** An Offer's payment failed. */
  public static final String OFFER_PAYMENT_FAILED = "offer.payment_failed";

  /** An Offer started. */
  public static final String OFFER_STARTED = "offer.started";

  /** An Offer was paused. */
  public static final String OFFER_PAUSED = "offer.paused";

  /** An Offer was resumed. */
  public static final String OFFER_RESUMED = "offer.resumed";

  /** An Offer ended. */
  public static final String OFFER_ENDED = "offer.ended";

  /** An Offer was deleted. */
  public static final String OFFER_DELETED = "offer.deleted";

  /** An Offer was turned off by Agilis. */
  public static final String OFFER_DISABLED = "offer.disabled";

  /** An Offer was turned back on by Agilis. */
  public static final String OFFER_ENABLED = "offer.enabled";

  /** An Event was published. */
  public static final String EVENT_PUBLISHED = "event.published";

  /** An Event was edited. */
  public static final String EVENT_UPDATED = "event.updated";

  /** An Event was rescheduled. */
  public static final String EVENT_RESCHEDULED = "event.rescheduled";

  /** An Event was cancelled. */
  public static final String EVENT_CANCELLED = "event.cancelled";

  /** An Event was deleted. */
  public static final String EVENT_DELETED = "event.deleted";

  /** An Event ended. */
  public static final String EVENT_ENDED = "event.ended";

  /** The balance was topped up. */
  public static final String BALANCE_TOPPED_UP = "balance.topped_up";

  /** The balance is low. */
  public static final String BALANCE_LOW = "balance.low";

  /** An invoice was issued. */
  public static final String INVOICE_CREATED = "invoice.created";

  /** A new version of the Advertising Terms and Conditions was issued. */
  public static final String TERMS_VERSION_ISSUED = "terms.version_issued";

  /** Every event type, without {@link #ALL}. */
  public static final List<String> VALUES =
      List.of(
          OFFER_CREATED,
          OFFER_PAYMENT_SUCCEEDED,
          OFFER_PAYMENT_FAILED,
          OFFER_STARTED,
          OFFER_PAUSED,
          OFFER_RESUMED,
          OFFER_ENDED,
          OFFER_DELETED,
          OFFER_DISABLED,
          OFFER_ENABLED,
          EVENT_PUBLISHED,
          EVENT_UPDATED,
          EVENT_RESCHEDULED,
          EVENT_CANCELLED,
          EVENT_DELETED,
          EVENT_ENDED,
          BALANCE_TOPPED_UP,
          BALANCE_LOW,
          INVOICE_CREATED,
          TERMS_VERSION_ISSUED);

  private WebhookEventTypes() {}
}
