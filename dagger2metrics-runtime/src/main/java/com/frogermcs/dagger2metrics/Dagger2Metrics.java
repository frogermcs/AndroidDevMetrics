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
    public static void enableCapturing(Application application) {
        GraphAnalyzer.setEnabled(true);
        InitManager.getInstance().initializedMetrics.clear();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(application)
                .setSmallIcon(R.drawable.ic_timeline_white_18dp)
                .setContentTitle("Dagger2Metrics")
                .setContentText("Click to see current metrics")
                .setAutoCancel(false);

        Intent resultIntent = new Intent(application, MetricsActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(application, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify("Dagger2Metrics".hashCode(), mBuilder.build());
    }
}
