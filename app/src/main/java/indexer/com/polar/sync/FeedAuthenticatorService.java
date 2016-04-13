package indexer.com.polar.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FeedAuthenticatorService extends Service {
  // Instance field that stores the authenticator object
  private FeedStubAuthenticator mWeatherStubAuthenticator;

  @Override public void onCreate() {
    // Create a new authenticator object
    mWeatherStubAuthenticator = new FeedStubAuthenticator(this);
  }

  /*
   * When the system binds to this Service to make the RPC call
   * return the authenticator's IBinder.
   */
  @Override public IBinder onBind(Intent intent) {
    return mWeatherStubAuthenticator.getIBinder();
  }
}
