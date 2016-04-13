package indexer.com.polar.data;

/**
 * Created by indexer on 3/27/16.
 */

import android.content.Context;
import android.support.v4.content.CursorLoader;
import android.net.Uri;

/**
 * Helper for loading a list of articles or a single article.
 */
public class FeedLoader extends CursorLoader {
  private FeedLoader(Context context, Uri uri) {
    super(context, uri, Query.PROJECTION, null, null, null);
  }

  public static FeedLoader newAllArticlesInstance(Context context) {
    return new FeedLoader(context, FeedContract.Items.buildDirUri());
  }

  public static FeedLoader newInstanceForItemId(Context context, long itemId) {
    return new FeedLoader(context, FeedContract.Items.buildItemUri(itemId));
  }

  public interface Query {
    String[] PROJECTION = {
        FeedContract.Items._ID, FeedContract.Items.TITLE, FeedContract.Items.SUMMERY,
        FeedContract.Items.IMAGE_URL, FeedContract.Items.ORGINAL_URL, FeedContract.Items.TIME_SAVE,
        FeedContract.Items.CATEGORY
    };

    int _ID = 0;
    int TITLE = 1;
    int SUMMERY = 2;
    int IMAGE_URL = 3;
    int ORGINAL_URL = 4;
    int TIMESAVE = 5;
    int CATEGORY = 6;
  }
}