package com.frogermcs.androiddevmetrics.internal;

import com.frogermcs.androiddevmetrics.internal.metrics.ActivityLifecycleMetrics;
import com.frogermcs.androiddevmetrics.internal.metrics.model.FpsDropMetric;

/**
 * Created by Miroslaw Stanek on 22.02.2016.
 */
public class ActivityMetricDescription {

    public String activityName;
    public int instancesCount;
    public int frameDropsCount;
    public float fpsDropsSum;
    public long activityLayoutTimeMillis;
    public long activityCreateMillis;
    public long activityStartMillis;
    public long activityResumeMillis;
    public boolean isLauncherActivity;

    public static ActivityMetricDescription initFrom(ActivityLifecycleMetrics.ActivityLifecycleMetric activityMetrics) {
        ActivityMetricDescription activityMetricDescription = new ActivityMetricDescription();
        activityMetricDescription.activityName = activityMetrics.activityClass.getSimpleName();
        activityMetricDescription.instancesCount = 1;
        activityMetricDescription.frameDropsCount = 0;
        activityMetricDescription.fpsDropsSum = 0;
        activityMetricDescription.isLauncherActivity = activityMetrics.isFirstActivity;
        activityMetricDescription.activityLayoutTimeMillis = activityMetrics.visibleTimeMillis();
        activityMetricDescription.activityCreateMillis = activityMetrics.createTimeMillis();
        activityMetricDescription.activityStartMillis = activityMetrics.startTimeMillis();
        activityMetricDescription.activityResumeMillis = activityMetrics.resumeTimeMillis();
        return activityMetricDescription;
    }

    public void updateWith(ActivityLifecycleMetrics.ActivityLifecycleMetric activityMetrics) {
        activityLayoutTimeMillis = ((activityLayoutTimeMillis * instancesCount) + activityMetrics.visibleTimeMillis()) / (instancesCount + 1);
        activityCreateMillis = ((activityCreateMillis * instancesCount) + activityMetrics.createTimeMillis()) / (instancesCount + 1);
        activityStartMillis = ((activityStartMillis * instancesCount) + activityMetrics.startTimeMillis()) / (instancesCount + 1);
        activityResumeMillis = ((activityResumeMillis * instancesCount) + activityMetrics.resumeTimeMillis()) / (instancesCount + 1);
        instancesCount++;
    }

    public void updateWith(FpsDropMetric fpsDropMetric) {
        frameDropsCount += fpsDropMetric.dropsCount;
        fpsDropsSum += fpsDropMetric.averageFps * fpsDropMetric.dropsCount;
    }

    public long getOverallTimeMillis() {
        return activityLayoutTimeMillis + activityCreateMillis + activityStartMillis + activityResumeMillis;
    }

    public float getAverageFps() {
        return fpsDropsSum / frameDropsCount;
    }
}
