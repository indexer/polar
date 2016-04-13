package indexer.com.polar.widget;

/**
 * Created by indexer on 3/28/16.
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import indexer.com.polar.R;
import indexer.com.polar.activity.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class LatestFeedWidget extends AppWidgetProvider {

  static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
      final int appWidgetId) {
    Intent intent = new Intent(context, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

    // Construct the RemoteViews object
    final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
    views.setImageViewResource(R.id.previewImage, R.drawable.placeholder);
    views.setOnClickPendingIntent(R.id.previewImage, pendingIntent);

    appWidgetManager.updateAppWidget(appWidgetId, views);
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
