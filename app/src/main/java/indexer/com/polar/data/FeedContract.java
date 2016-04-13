package indexer.com.polar.data;

/**
 * Created by indexer on 3/27/16.
 */

import android.net.Uri;

public class FeedContract {
  public static final String CONTENT_AUTHORITY = "com.polar.tldr";
  public static final Uri BASE_URI = Uri.parse("content://com.polar.tldr");

  private FeedContract() {
  }

  interface ItemsColumns {
    /** Type: INTEGER PRIMARY KEY AUTOINCREMENT */
    String _ID = "_id";
    /** Type: TEXT NOT NULL */
    String TITLE = "title";
    /** Type: TEXT NOT NULL */
    String SUMMERY = "summery_bullet";

    String IMAGE_URL = "image_url";

    String ORGINAL_URL = "originals_url";

    String TIME_SAVE = "time_saved";

    String CATEGORY = "category";
  }

  public static class Items implements ItemsColumns {
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.polar.tldr.items";
    public static final String CONTENT_ITEM_TYPE =
        "vnd.android.cursor.item/vnd.com.polar.tldr.items";

    /** Matches: /items/ */
    public static Uri buildDirUri() {
      return BASE_URI.buildUpon().appendPath("items").build();
    }

    /** Matches: /items/[_id]/ */
    public static Uri buildItemUri(long _id) {
      return BASE_URI.buildUpon().appendPath("items").appendPath(Long.toString(_id)).build();
    }

    /** Read item ID item detail URI. */
    public static long getItemId(Uri itemUri) {
      return Long.parseLong(itemUri.getPathSegments().get(1));
    }
  }
}