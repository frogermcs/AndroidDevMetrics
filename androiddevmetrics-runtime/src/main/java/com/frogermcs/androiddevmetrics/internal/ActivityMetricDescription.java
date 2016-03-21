package com.frogermcs.androiddevmetrics.internal;

import com.frogermcs.androiddevmetrics.aspect.ActivityLifecycleAnalyzer;
import com.frogermcs.androiddevmetrics.internal.metrics.ActivityLifecycleMetrics;
import com.frogermcs.androiddevmetrics.internal.metrics.model.FpsDropMetric;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miroslaw Stanek on 22.02.2016.
 */
public class ActivityMetricDescription {

    public String activityName;
    public String activitySimpleName;
    public int instancesCount;
    public int frameDropsCount;
    public float fpsDropsSum;
    public long activityLayoutTimeMillis;
    public long activityCreateMillis;
    public long activityStartMillis;
    public long activityResumeMillis;
    public boolean isLauncherActivity;
    public boolean hasOnCreateImplemented;
    public boolean hasOnStartImplemented;
    public boolean hasOnResumeImplemented;

    public static ActivityMetricDescription initFrom(ActivityLifecycleMetrics.ActivityLifecycleMetric activityMetrics) {
        ActivityMetricDescription activityMetricDescription = new ActivityMetricDescription();
        activityMetricDescription.activityName = activityMetrics.activityClass.getName();
        activityMetricDescription.activitySimpleName = activityMetrics.activityClass.getSimpleName();
        activityMetricDescription.instancesCount = 1;
        activityMetricDescription.frameDropsCount = 0;
        activityMetricDescription.fpsDropsSum = 0;
        activityMetricDescription.isLauncherActivity = activityMetrics.isFirstActivity;
        activityMetricDescription.activityLayoutTimeMillis = activityMetrics.visibleTimeMillis();
        activityMetricDescription.activityCreateMillis = activityMetrics.createTimeMillis();
        activityMetricDescription.activityStartMillis = activityMetrics.startTimeMillis();
        activityMetricDescription.activityResumeMillis = activityMetrics.resumeTimeMillis();
        activityMetricDescription.hasOnCreateImplemented = activityMetrics.hasOnCreateImplemented;
        activityMetricDescription.hasOnStartImplemented = activityMetrics.hasOnStartImplemented;
        activityMetricDescription.hasOnResumeImplemented = activityMetrics.hasOnResumeImplemented;
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

    public String[] getImplementedMethods() {
        List<String> implementedMethods = new ArrayList<>();
        if (hasOnCreateImplemented) implementedMethods.add(ActivityLifecycleAnalyzer.METHOD_ON_CREATE);
        if (hasOnStartImplemented) implementedMethods.add(ActivityLifecycleAnalyzer.METHOD_ON_START);
        if (hasOnResumeImplemented) implementedMethods.add(ActivityLifecycleAnalyzer.METHOD_ON_RESUME);

        String[] implementedMethodsArray = new String[implementedMethods.size()];
        implementedMethods.toArray(implementedMethodsArray);
        return implementedMethodsArray;
    }
}
