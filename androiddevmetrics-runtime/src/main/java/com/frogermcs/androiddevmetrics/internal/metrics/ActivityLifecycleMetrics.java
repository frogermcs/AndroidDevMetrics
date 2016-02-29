package com.frogermcs.androiddevmetrics.internal.metrics;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.view.WindowCallbackWrapper;
import android.view.Window;

import com.frogermcs.androiddevmetrics.internal.ActivityMetricDescription;
import com.frogermcs.androiddevmetrics.internal.metrics.model.FpsDropMetric;
import com.frogermcs.androiddevmetrics.internal.ui.MetricsActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Miroslaw Stanek on 28.02.2016.
 */
public class ActivityLifecycleMetrics {
    private static class Holder {
        static final ActivityLifecycleMetrics INSTANCE = new ActivityLifecycleMetrics();
    }

    public static ActivityLifecycleMetrics getInstance() {
        return Holder.INSTANCE;
    }

    public LinkedHashMap<Integer, ActivityLifecycleMetric> activityLifecycleMetricsMap = new LinkedHashMap<>();

    public void logPreOnCreate(Activity activity) {
        if (isIgnoredActivity(activity)) return;

        int activityHash = activity.hashCode();
        ActivityLifecycleMetric activityLifecycleMetric = activityLifecycleMetricsMap.get(activityHash);
        if (activityLifecycleMetric == null) {
            activityLifecycleMetric = setupNewActivityLifecycleMetric(activity, activityHash);
        }

        activityLifecycleMetric.state = ActivityLifecycleMetric.STATE_PRE_CREATED;
    }

    public void logPostOnCreate(Activity activity) {
        if (isIgnoredActivity(activity)) return;

        int activityHash = activity.hashCode();
        ActivityLifecycleMetric activityLifecycleMetric = activityLifecycleMetricsMap.get(activityHash);
        if (activityLifecycleMetric == null) {
            activityLifecycleMetric = setupNewActivityLifecycleMetric(activity, activityHash);
        }

        activityLifecycleMetric.postCreateNanoTime = System.nanoTime();
        activityLifecycleMetric.state = ActivityLifecycleMetric.STATE_POST_CREATED;

        Window window = activity.getWindow();
        window.setCallback(new MetricWindowCallbackWrapper(window.getCallback(), activityLifecycleMetric));
    }

    @NonNull
    private ActivityLifecycleMetric setupNewActivityLifecycleMetric(Activity activity, int activityHash) {
        ActivityLifecycleMetric activityLifecycleMetric;
        activityLifecycleMetric = new ActivityLifecycleMetric();
        activityLifecycleMetric.activityClass = activity.getClass();
        activityLifecycleMetric.preCreateNanoTime = System.nanoTime();
        activityLifecycleMetric.isFirstActivity = activityLifecycleMetricsMap.size() == 0;
        activityLifecycleMetricsMap.put(activityHash, activityLifecycleMetric);
        return activityLifecycleMetric;
    }

    public void logPreOnStart(Activity activity) {
        if (isIgnoredActivity(activity)) return;

        int activityHash = activity.hashCode();
        ActivityLifecycleMetric activityLifecycleMetric = activityLifecycleMetricsMap.get(activityHash);
        if (activityLifecycleMetric.state == ActivityLifecycleMetric.STATE_POST_CREATED) {
            activityLifecycleMetric.preStartNanoTime = System.nanoTime();
            activityLifecycleMetric.state = ActivityLifecycleMetric.STATE_PRE_STARTED;
        }
    }

    public void logPostOnStart(Activity activity) {
        if (isIgnoredActivity(activity)) return;

        int activityHash = activity.hashCode();
        ActivityLifecycleMetric activityLifecycleMetric = activityLifecycleMetricsMap.get(activityHash);
        if (activityLifecycleMetric.state == ActivityLifecycleMetric.STATE_POST_CREATED
                || activityLifecycleMetric.state == ActivityLifecycleMetric.STATE_PRE_STARTED
                || activityLifecycleMetric.state == ActivityLifecycleMetric.STATE_POST_STARTED) {
            if (activityLifecycleMetric.state == ActivityLifecycleMetric.STATE_POST_CREATED) {
                activityLifecycleMetric.preStartNanoTime = System.nanoTime();
            }
            activityLifecycleMetric.postStartNanoTime = System.nanoTime();
            activityLifecycleMetric.state = ActivityLifecycleMetric.STATE_POST_STARTED;
        }
    }

    public void logPreOnResume(Activity activity) {
        if (isIgnoredActivity(activity)) return;

        int activityHash = activity.hashCode();
        ActivityLifecycleMetric activityLifecycleMetric = activityLifecycleMetricsMap.get(activityHash);
        if (activityLifecycleMetric.state == ActivityLifecycleMetric.STATE_POST_STARTED) {
            activityLifecycleMetric.preResumeNanoTime = System.nanoTime();
            activityLifecycleMetric.state = ActivityLifecycleMetric.STATE_PRE_RESUMED;
        }
    }

    public void logPostOnResume(Activity activity) {
        if (isIgnoredActivity(activity)) return;

        int activityHash = activity.hashCode();
        ActivityLifecycleMetric activityLifecycleMetric = activityLifecycleMetricsMap.get(activityHash);
        if (activityLifecycleMetric.state == ActivityLifecycleMetric.STATE_POST_STARTED
                || activityLifecycleMetric.state == ActivityLifecycleMetric.STATE_PRE_RESUMED
                || activityLifecycleMetric.state == ActivityLifecycleMetric.STATE_POST_RESUMED) {
            if (activityLifecycleMetric.state == ActivityLifecycleMetric.STATE_POST_STARTED) {
                activityLifecycleMetric.preResumeNanoTime = System.nanoTime();
            }
            activityLifecycleMetric.postResumeNanoTime = System.nanoTime();
            activityLifecycleMetric.state = ActivityLifecycleMetric.STATE_POST_RESUMED;
        }
    }

    public void logOnPaused(Activity activity) {
        if (isIgnoredActivity(activity)) return;

        updateActivityMetricState(activity, ActivityLifecycleMetric.STATE_PAUSED);
    }

    public void logOnStopped(Activity activity) {
        if (isIgnoredActivity(activity)) return;

        updateActivityMetricState(activity, ActivityLifecycleMetric.STATE_STOPPED);
    }

    public void logOnDestroyed(Activity activity) {
        if (isIgnoredActivity(activity)) return;

        updateActivityMetricState(activity, ActivityLifecycleMetric.STATE_DESTROYED);
    }

    private void updateActivityMetricState(Activity activity, int state) {
        int activityHash = activity.hashCode();
        ActivityLifecycleMetric activityLifecycleMetric = activityLifecycleMetricsMap.get(activityHash);
        if (activityLifecycleMetric != null) {
            activityLifecycleMetric.state = state;
        }
    }

    public boolean isIgnoredActivity(Activity activity) {
        return activity instanceof MetricsActivity;
    }

    public List<ActivityMetricDescription> getListOfMetricDescriptions() {
        LinkedHashMap<String, ActivityMetricDescription> activityMetricDescriptions = new LinkedHashMap<>();

        for (ActivityLifecycleMetric activityLifecycleMetric : activityLifecycleMetricsMap.values()) {
            ActivityMetricDescription activityMetricDescription = activityMetricDescriptions.get(activityLifecycleMetric.activityClass.getSimpleName());
            if (activityMetricDescription == null) {
                activityMetricDescription = ActivityMetricDescription.initFrom(activityLifecycleMetric);
                activityMetricDescriptions.put(activityLifecycleMetric.activityClass.getSimpleName(), activityMetricDescription);
            } else {
                activityMetricDescription.updateWith(activityLifecycleMetric);
            }
        }

        ChoreographerMetrics.getInstance().collectDropsIfAny();
        for (FpsDropMetric fpsDropMetric : ChoreographerMetrics.getInstance().dropMetricsList) {
            ActivityMetricDescription activityMetricDescription = activityMetricDescriptions.get(fpsDropMetric.activityName);
            if (activityMetricDescription != null) {
                activityMetricDescription.updateWith(fpsDropMetric);
            }
        }

        return new ArrayList<>(activityMetricDescriptions.values());
    }

    public static class ActivityLifecycleMetric {
        public static final int STATE_NEW = 0;
        public static final int STATE_PRE_CREATED = 1;
        public static final int STATE_POST_CREATED = 2;
        public static final int STATE_PRE_STARTED = 3;
        public static final int STATE_POST_STARTED = 4;
        public static final int STATE_PRE_RESUMED = 5;
        public static final int STATE_POST_RESUMED = 6;
        public static final int STATE_VISIBLE = 7;
        public static final int STATE_PAUSED = 8;
        public static final int STATE_STOPPED = 9;
        public static final int STATE_DESTROYED = 10;

        public Class activityClass;
        public long preCreateNanoTime;
        public long postCreateNanoTime;
        public long preStartNanoTime;
        public long postStartNanoTime;
        public long preResumeNanoTime;
        public long postResumeNanoTime;
        public long viewVisibleToUserNanoTime;
        public int state = STATE_NEW;
        public boolean isFirstActivity = false;

        public long createTimeMillis() {
            if (state > STATE_POST_CREATED) {
                return TimeUnit.NANOSECONDS.toMillis(postCreateNanoTime - preCreateNanoTime);
            } else {
                return -1;
            }
        }

        public long startTimeMillis() {
            if (state > STATE_POST_STARTED) {
                return TimeUnit.NANOSECONDS.toMillis(postStartNanoTime - preStartNanoTime);
            } else {
                return -1;
            }
        }

        public long resumeTimeMillis() {
            if (state > STATE_POST_RESUMED) {
                return TimeUnit.NANOSECONDS.toMillis(postResumeNanoTime - preResumeNanoTime);
            } else {
                return -1;
            }
        }

        public long visibleTimeMillis() {
            if (state >= STATE_VISIBLE) {
                return TimeUnit.NANOSECONDS.toMillis(viewVisibleToUserNanoTime - postResumeNanoTime);
            } else {
                return -1;
            }
        }

        @Override
        public String toString() {
            return "ActivityLifecycleMetric{" +
                    "activityClass=" + activityClass +
                    ",\nCreateTimeMillis=" + createTimeMillis() +
                    ",\nStartTimeMillis=" + startTimeMillis() +
                    ",\nResumeTimeMillis=" + resumeTimeMillis() +
                    ",\nviewVisibleToUserNanoTime=" + visibleTimeMillis() +
                    ",\nstate=" + state +
                    '}';
        }
    }

    public static class MetricWindowCallbackWrapper extends WindowCallbackWrapper {

        private ActivityLifecycleMetric activityLifecycleMetric;

        public MetricWindowCallbackWrapper(Window.Callback wrapped, ActivityLifecycleMetric activityLifecycleMetric) {
            super(wrapped);
            this.activityLifecycleMetric = activityLifecycleMetric;
        }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
            if (activityLifecycleMetric.state == ActivityLifecycleMetric.STATE_POST_RESUMED && hasFocus) {
                activityLifecycleMetric.viewVisibleToUserNanoTime = System.nanoTime();
                activityLifecycleMetric.state = ActivityLifecycleMetric.STATE_VISIBLE;
            }
        }
    }
}
