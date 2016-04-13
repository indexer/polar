package indexer.com.polar.callback;

import retrofit.Callback;

/**
 * Created by indexer on 10/31/15.
 */
public abstract class RestCallBack<T> implements Callback<T> {

  /**
   * Invoked when a network or unexpected exception occurred during the HTTP request.
   */
  @Override public void onFailure(Throwable t) {
    t.printStackTrace();
  }
}
