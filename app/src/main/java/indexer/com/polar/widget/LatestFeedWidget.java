package indexer.com.polar.widget;

/**
 * Created by indexer on 3/28/16.
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import com.squareup.picasso.Picasso;
import indexer.com.polar.R;
import indexer.com.polar.activity.MainActivity;
import indexer.com.polar.model.Feed;

/**
 * Implementation of App Widget functionality.
 */
public class LatestFeedWidget extends AppWidgetProvider {
  static Feed mFeed;

  static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
      final int appWidgetId) {
    Intent intent = new Intent(context, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

    // Construct the RemoteViews object
    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
    Picasso picasso = Picasso.with(context);
    picasso.load("http://cdn0.dailydot.com/cache/e2/62/e262523d657d06e23b1c510eb59c6044.jpg") //
        .into(remoteViews, R.id.previewImage, new int[] { appWidgetId });
    remoteViews.setTextViewText(R.id.title, "Latest Feed");
       /* */
    //views.setImageViewResource(R.id.previewImage, R.drawable.placeholder);
    remoteViews.setOnClickPendingIntent(R.id.previewImage, pendingIntent);

    appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
  }

  @Override public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
      // handle intent here
      mFeed = intent.getParcelableExtra(String.valueOf(R.string.object_tag));
      Log.e("Feed", "" + mFeed.getOriginalUrl());
      if (mFeed != null) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget =
            new ComponentName(context.getPackageName(), LatestFeedWidget.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        Picasso picasso = Picasso.with(context);
        picasso.load(mFeed.getImageUrl()) //
            .into(remoteViews, R.id.previewImage, appWidgetIds);
        remoteViews.setTextViewText(R.id.title, mFeed.getTitle());
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
      }
    }

    super.onReceive(context, intent);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {

      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}
