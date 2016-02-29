package com.frogermcs.androiddevmetrics.internal.metrics;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.Choreographer;

import com.frogermcs.androiddevmetrics.internal.metrics.model.FpsDropMetric;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Miroslaw Stanek on 16.02.2016.
 */
public class ChoreographerMetrics {

    private static class Holder {
        static final ChoreographerMetrics INSTANCE = new ChoreographerMetrics();
    }

    public static ChoreographerMetrics getInstance() {
        return Holder.INSTANCE;
    }

    public final List<FpsDropMetric> dropMetricsList = new ArrayList<>();
    public final Set<OnMetricsDataListener> dataListeners = new HashSet<>();

    private Choreographer choreographer;

    private long frameStartTime = 0;
    private int framesRendered = 0;

    private int interval = 500;

    private double dropLimit = 40;
    private int dropIntervalMillis = 1000 * 60;

    private long firstDropRegistered = 0;

    private List<Double> tempDrops = new LinkedList<>();
    private String currentActivityName;
    private FrameDropsMetrics frameDropsMetrics;

    ChoreographerMetrics() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            choreographer = Choreographer.getInstance();
            frameDropsMetrics = new FrameDropsMetrics();
        }
    }

    public void start() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            choreographer.postFrameCallback(frameDropsMetrics);
        }
    }

    public void stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            frameStartTime = 0;
            framesRendered = 0;
            choreographer.removeFrameCallback(frameDropsMetrics);
        }
    }

    public void setIntervalMillis(int interval) {
        this.interval = interval;
    }

    public void setDropLimitFps(double dropLimit) {
        this.dropLimit = dropLimit;
    }

    public void setDropIntervalMillis(int dropsInterval) {
        this.dropIntervalMillis = dropsInterval;
    }

    private void onDropRegistered(double fps, long registeredTime) {
        String activityName = ActivityLaunchMetrics.getInstance().getCurrentActivityName();
        tempDrops.add(fps);
        if (firstDropRegistered == 0) {
            firstDropRegistered = registeredTime;
            currentActivityName = activityName;
        }

        long dropTimeSpan = registeredTime - firstDropRegistered;
        if (dropTimeSpan > dropIntervalMillis || !activityName.equals(currentActivityName)) {
            pushDropsIfExists();
            currentActivityName = activityName;
        }

    }

    private void pushDropsIfExists() {
        if (tempDrops.size() > 0) {
            FpsDropMetric dropMetric = FpsDropMetric.fromDrops(tempDrops, currentActivityName);
            firstDropRegistered = 0;
            broadcastNewDropMetrics(dropMetric);
            dropMetricsList.add(dropMetric);
            tempDrops.clear();
        }
    }

    private void broadcastNewDropMetrics(FpsDropMetric dropMetric) {
        for (OnMetricsDataListener dataListener : dataListeners) {
            dataListener.onFrameDropRegistered(dropMetric);
        }
    }

    public void addMetricsDataListener(OnMetricsDataListener onDataListener) {
        dataListeners.add(onDataListener);
    }

    public void removeMetricsDataListener(OnMetricsDataListener onDataListener) {
        dataListeners.remove(onDataListener);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private class FrameDropsMetrics implements Choreographer.FrameCallback {
        @Override
        public void doFrame(long frameTimeNanos) {
            long currentTimeMillis = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos);

            if (frameStartTime > 0) {
                final long timeSpan = currentTimeMillis - frameStartTime;
                framesRendered++;

                if (timeSpan > interval) {
                    double fps = framesRendered * 1000 / (double) timeSpan;
                    frameStartTime = currentTimeMillis;
                    framesRendered = 0;
                    if (fps < dropLimit) {
                        onDropRegistered(fps, currentTimeMillis);
                    }
                }

                if (firstDropRegistered > 0) {
                    long dropIntervalTimeSpan = currentTimeMillis - firstDropRegistered;
                    if (dropIntervalTimeSpan > dropIntervalMillis) {
                        pushDropsIfExists();
                    }
                }
            } else {
                frameStartTime = currentTimeMillis;
            }

            choreographer.postFrameCallback(this);
        }
    }
}
