package indexer.com.polar.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by indexer on 3/27/16.
 */
public class FeedSyncService extends Service {

  private static final String TAG = "FeedSyncService";
  private static final Object mFeeAdapterLock = new Object();
  private static FeedSyncAdapter mFeedAdapter = null;

  //
  @Override public void onCreate() {
    super.onCreate();
    synchronized (mFeeAdapterLock) {
      if (mFeedAdapter == null) {
        mFeedAdapter = new FeedSyncAdapter(getApplicationContext(), true);
      }
    }
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return mFeedAdapter.getSyncAdapterBinder();
  }
}
