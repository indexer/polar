package indexer.com.polar.data;

/**
 * Created by indexer on 3/27/16.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedDataBase extends SQLiteOpenHelper {
  private static final String DATABASE_NAME = "tldr.db";
  private static final int DATABASE_VERSION = 1;

  public FeedDataBase(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + FeedProvider.Tables.ITEMS + " (" + FeedContract.ItemsColumns._ID
        + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        FeedContract.ItemsColumns.TITLE + " TEXT," + FeedContract.ItemsColumns.SUMMERY
        + " TEXT NOT NULL," + FeedContract.ItemsColumns.IMAGE_URL + " TEXT," +
        FeedContract.ItemsColumns.ORGINAL_URL + " TEXT," + FeedContract.Items.TIME_SAVE + " TEXT," +
        FeedContract.ItemsColumns.CATEGORY + " TEXT" + ")");
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + FeedProvider.Tables.ITEMS);
    onCreate(db);
  }
}
