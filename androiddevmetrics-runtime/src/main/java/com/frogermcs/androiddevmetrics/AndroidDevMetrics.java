package com.frogermcs.androiddevmetrics;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.frogermcs.androiddevmetrics.aspect.ActivityLifecycleAnalyzer;
import com.frogermcs.androiddevmetrics.internal.MethodsTracingManager;
import com.frogermcs.androiddevmetrics.internal.metrics.ActivityLaunchMetrics;
import com.frogermcs.androiddevmetrics.aspect.Dagger2GraphAnalyzer;
import com.frogermcs.androiddevmetrics.internal.metrics.ChoreographerMetrics;
import com.frogermcs.androiddevmetrics.internal.metrics.InitManager;
import com.frogermcs.androiddevmetrics.internal.ui.MetricsActivity;


/**
 * Created by Miroslaw Stanek on 25.01.2016.
 */
public class AndroidDevMetrics {
    private static int DAGGER2_WARNING_1_LIMIT_MILLIS = 30;
    private static int DAGGER2_WARNING_2_LIMIT_MILLIS = 50;
    private static int DAGGER2_WARNING_3_LIMIT_MILLIS = 100;

    private static int FRAME_DROPS_DEFAULT_INTERVAL_MS = 500;
    private static double FRAME_DROPS_FPS_LIMIT = 40;

    static volatile AndroidDevMetrics singleton;

    private Context context;
    private int dagger2WarningLevel1, dagger2WarningLevel2, dagger2WarningLevel3;
    private boolean enableAcitivtyMetrics;
    private boolean showNotification;
    private boolean enableDagger2Metrics;
    private int intervalMillis;
    private double maxFpsForFrameDrop;

    /**
     * Enable Activity and Dagger 2 metrics
     * */
    public static AndroidDevMetrics initWith(Context context) {
        Builder androidDevMetricsBuilder = new Builder(context)
                .enableActivityMetrics(true)
                .enableDagger2Metrics(true)
                .showNotification(true);
        return initWith(androidDevMetricsBuilder);
    }

    public static AndroidDevMetrics initWith(Builder builder) {
        return initWith(builder.build());
    }

    public static AndroidDevMetrics initWith(AndroidDevMetrics androidDevMetrics) {
        if (singleton == null) {
            synchronized (AndroidDevMetrics.class) {
                if (singleton == null) {
                    setAndroidDevMetrics(androidDevMetrics);
                }
            }
        }

        return singleton;
    }

    private static void setAndroidDevMetrics(AndroidDevMetrics androidDevMetrics) {
        singleton = androidDevMetrics;
        singleton.setupMetrics();
    }

    public static AndroidDevMetrics singleton() {
        if (singleton == null) {
            throw new IllegalStateException("Must Initialize Dagger2Metrics before using singleton()");
        } else {
            return singleton;
        }
    }

    AndroidDevMetrics(Context context) {
        this.context = context;
    }

    public int dagger2WarningLevel1() {
        return dagger2WarningLevel1;
    }

    public int dagger2WarningLevel2() {
        return dagger2WarningLevel2;
    }

    public int dagger2WarningLevel3() {
        return dagger2WarningLevel3;
    }

    private void setupMetrics() {
        Dagger2GraphAnalyzer.setEnabled(enableDagger2Metrics);
        InitManager.getInstance().initializedMetrics.clear();

        ActivityLifecycleAnalyzer.setEnabled(true);
        if (enableAcitivtyMetrics) {
            MethodsTracingManager.getInstance().init(context);
            ActivityLaunchMetrics activityLaunchMetrics = ActivityLaunchMetrics.getInstance();
            ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(activityLaunchMetrics);
            ChoreographerMetrics.getInstance().setMaxFpsForFrameDrop(maxFpsForFrameDrop);
            ChoreographerMetrics.getInstance().setIntervalMillis(intervalMillis);
            ChoreographerMetrics.getInstance().start();
        }

        if (showNotification) {
            showNotification();
        }
    }

    private void showNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_timeline_white_18dp)
                .setContentTitle(context.getString(R.string.adm_name))
                .setContentText("Click to see current metrics")
                .setAutoCancel(false);

        Intent resultIntent = new Intent(context, MetricsActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify("AndroidDevMetrics".hashCode(), mBuilder.build());
    }

    public static class Builder {
        private final Context context;
        private int dagger2WarningLevel1 = DAGGER2_WARNING_1_LIMIT_MILLIS;
        private int dagger2WarningLevel2 = DAGGER2_WARNING_2_LIMIT_MILLIS;
        private int dagger2WarningLevel3 = DAGGER2_WARNING_3_LIMIT_MILLIS;
        private boolean enableAcitivtyMetrics = true;
        private boolean enableDagger2Metrics = true;
        private boolean showNotification = true;
        private int intervalMillis = FRAME_DROPS_DEFAULT_INTERVAL_MS;
        private double maxFpsForFrameDrop = FRAME_DROPS_FPS_LIMIT;

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            } else {
                this.context = context.getApplicationContext();
            }
        }

        public Builder dagger2WarningLevelsMs(int warning1, int warning2, int warning3) {
            if (warning1 > warning2 || warning2 > warning3) {
                throw new IllegalArgumentException("Warning levels should be ascending");
            } else {
                this.dagger2WarningLevel1 = warning1;
                this.dagger2WarningLevel2 = warning2;
                this.dagger2WarningLevel3 = warning3;
            }

            return this;
        }

        public Builder frameDropsLimits(int measureIntervalMillis, double maxFpsForFrameDrop) {
            if (maxFpsForFrameDrop > 60) {
                throw new IllegalArgumentException("Max fps cannot be bigger than 60fps");
            }

            this.intervalMillis = measureIntervalMillis;
            this.maxFpsForFrameDrop = maxFpsForFrameDrop;
            return this;
        }

        public Builder enableActivityMetrics(boolean enable) {
            this.enableAcitivtyMetrics = enable;
            return this;
        }

        public Builder showNotification(boolean show) {
            this.showNotification = show;
            return this;
        }

        public Builder enableDagger2Metrics(boolean enable) {
            this.enableDagger2Metrics = enable;
            return this;
        }

        public AndroidDevMetrics build() {
            AndroidDevMetrics androidDevMetrics = new AndroidDevMetrics(context);
            androidDevMetrics.dagger2WarningLevel1 = this.dagger2WarningLevel1;
            androidDevMetrics.dagger2WarningLevel2 = this.dagger2WarningLevel2;
            androidDevMetrics.dagger2WarningLevel3 = this.dagger2WarningLevel3;
            androidDevMetrics.enableAcitivtyMetrics = this.enableAcitivtyMetrics;
            androidDevMetrics.showNotification = this.showNotification;
            androidDevMetrics.enableDagger2Metrics = this.enableDagger2Metrics;
            androidDevMetrics.maxFpsForFrameDrop = this.maxFpsForFrameDrop;
            androidDevMetrics.intervalMillis = this.intervalMillis;
            return androidDevMetrics;
        }
    }


}
