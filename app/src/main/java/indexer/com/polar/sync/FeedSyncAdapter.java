package indexer.com.polar.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import indexer.com.polar.R;
import indexer.com.polar.callback.RestCallBack;
import indexer.com.polar.data.FeedContract;
import indexer.com.polar.model.Feed;
import indexer.com.polar.rest.RestClient;
import java.util.ArrayList;
import java.util.List;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by indexer on 3/27/16.
 */
public class FeedSyncAdapter extends AbstractThreadedSyncAdapter {
  // Sync interval constants
  public static final int SECONDS_PER_MINUTE = 60;
  public static final int SYNC_INTERVAL_IN_MINUTES = 120;
  public static final int SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;
  public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 2;
  public static final String SYNC_FINISHED = "sync finished";
  private static final String TAG = "FeedSyncAdapter";
  public static String strSeparator = "__,__";
  private SharedPreferences sPreferences;
  private ContentResolver contentResolver;
  private Context mContext;

  public FeedSyncAdapter(Context context, boolean autoInitialize) {
    super(context, autoInitialize);
    contentResolver = context.getContentResolver();
  }

  public FeedSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
    super(context, autoInitialize, allowParallelSyncs);
    mContext = context;
  }

  public static void initializeSyncAdapter(Context context) {
    getSyncAccount(context);
  }

  public static Account getSyncAccount(Context context) {
    // Get an instance of the Android account manager
    AccountManager accountManager =
        (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
    // Create the account type and default account
    Account newAccount =
        new Account(context.getString(R.string.app_name), context.getString(R.string.account_type));
    // If the password doesn't exist, the account doesn't exist
    if (null == accountManager.getPassword(newAccount)) {
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
      if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
        return null;
      }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

      onAccountCreated(newAccount, context);
    }
    return newAccount;
  }

  private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
    FeedSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
    ContentResolver.setSyncAutomatically(newAccount,
        context.getString(R.string.account_authorities), true);
        /*
         * Finally, let's do a sync to get things started
         */
    syncImmediately(context);
  }

  public static void syncImmediately(Context context) {
    Bundle bundle = new Bundle();
    bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
    bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
    ContentResolver.requestSync(getSyncAccount(context),
        context.getString(R.string.account_authorities), bundle);
  }

  public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
    Account account = getSyncAccount(context);
    String authority = context.getString(R.string.account_authorities);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      // we can enable inexact timers in our periodic sync
      SyncRequest.Builder builder = new SyncRequest.Builder();
      Bundle extras = new Bundle();
      builder.setExtras(extras);
      SyncRequest request = builder.
          syncPeriodic(syncInterval, flexTime).
          setSyncAdapter(account, authority).build();
      ContentResolver.requestSync(request);
    } else {
      ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
    }
  }

  public static String convertArrayToString(List<String> array) {
    String str = "";
    for (int i = 0; i < array.size(); i++) {
      str = str + array.get(i);
      // Do not append comma at the end of last element
      if (i < array.size() - 1) {
        str = str + strSeparator;
      }
    }
    return str;
  }

  private ContentValues feedToContentValue(Feed feed) {
    ContentValues contentValues = new ContentValues();
    //contentValues.put("_id", feed.get_id());
    contentValues.put("title", feed.getTitle());
    String summery = convertArrayToString(feed.getSummeries());
    contentValues.put("summery_bullet", summery);
    contentValues.put("image_url", feed.getImageUrl());
    contentValues.put("originals_url", feed.getOriginalUrl());
    contentValues.put("time_saved", feed.getTimeSaved());
    contentValues.put("category", feed.getCategories().get(0).getName());
    return contentValues;
  }

  private void fromServer(final ArrayList<ContentProviderOperation> cpo, final Uri uri)
      throws RemoteException, OperationApplicationException {

    Call<ArrayList<Feed>> latestFeeds = RestClient.getService(mContext).getLatestFeeds();
    latestFeeds.enqueue(new RestCallBack<ArrayList<Feed>>() {
      @Override public void onResponse(Response<ArrayList<Feed>> response) {
        Log.e("In Sync", "" + response.body().size());
        for (int i = 0; i < response.body().size(); i++) {
          ContentValues mContentValue = feedToContentValue(response.body().get(i));
          cpo.add(ContentProviderOperation.newInsert(uri).withValues(mContentValue).build());
        }
        try {
          contentResolver.applyBatch(FeedContract.CONTENT_AUTHORITY, cpo);
          contentResolver.notifyChange(FeedContract.BASE_URI, null);
        } catch (RemoteException e) {
          e.printStackTrace();
        } catch (OperationApplicationException e) {
          e.printStackTrace();
        }
      }
    });
  }

  @Override public void onPerformSync(Account account, Bundle bundle, String s,
      final ContentProviderClient contentProviderClient, SyncResult syncResult) {
    ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<>();
    Uri dirUri = FeedContract.Items.buildDirUri();
    contentProviderOperations.add(ContentProviderOperation.newDelete(dirUri).build());
    try {
      fromServer(contentProviderOperations, dirUri);
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (OperationApplicationException e) {
      e.printStackTrace();
    }
    getContext().sendBroadcast(new Intent(SYNC_FINISHED));
  }
}