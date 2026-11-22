package dating.agilis.ads;

import dating.agilis.ads.model.ApiResponse;

/**
 * A resource with one list operation, such as {@code client.categories().list()}.
 *
 * @param <T> the response type
 */
public final class ListService<T extends ApiResponse> {

  private final AgilisClient client;
  private final String path;
  private final Class<T> type;

  ListService(AgilisClient client, String path, Class<T> type) {
    this.client = client;
    this.path = path;
    this.type = type;
  }

  /**
   * Fetches the list.
   *
   * @return the response
   */
  public T list() {
    return client.requester().execute(ApiRequest.of("GET", path), type);
  }
}
