package com.frogermcs.dagger2metrics;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.frogermcs.dagger2metrics.aspect.GraphAnalyzer;
import com.frogermcs.dagger2metrics.internal.InitManager;
import com.frogermcs.dagger2metrics.internal.ui.MetricsActivity;


/**
 * Created by Miroslaw Stanek on 25.01.2016.
 */
public class Dagger2Metrics {
    public static int WARNING_1_LIMIT_MILLIS = 30;
    public static int WARNING_2_LIMIT_MILLIS = 50;
    public static int WARNING_3_LIMIT_MILLIS = 100;

    public static void enableCapturing(Application application) {
        GraphAnalyzer.setEnabled(true);
        InitManager.getInstance().initializedMetrics.clear();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(application)
                .setSmallIcon(R.drawable.ic_timeline_white_18dp)
                .setContentTitle(application.getString(R.string.dagger2metrics_name))
                .setContentText(application.getString(R.string.dagger2metrics_notification_content))
                .setAutoCancel(false);

        Intent resultIntent = new Intent(application, MetricsActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(application, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify("Dagger2Metrics".hashCode(), mBuilder.build());
    }
}
