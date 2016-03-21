package com.frogermcs.androiddevmetrics.internal.metrics;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.frogermcs.androiddevmetrics.internal.MethodsTracingManager;
import com.frogermcs.androiddevmetrics.internal.ui.dialog.MethodsTracingFinishedDialog;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Miroslaw Stanek on 06.02.2016.
 */
public class ActivityLaunchMetrics implements Application.ActivityLifecycleCallbacks {

    private static class Holder {
        static final ActivityLaunchMetrics INSTANCE = new ActivityLaunchMetrics();
    }

    public static ActivityLaunchMetrics getInstance() {
        return Holder.INSTANCE;
    }

    public final Set<OnMetricsDataListener> dataListeners = new HashSet<>();

    private String currentActivityName = "not_set";
    private MethodsTracingManager methodsTracingManager;

    ActivityLaunchMetrics() {
        methodsTracingManager = MethodsTracingManager.getInstance();
    }

    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        ActivityLifecycleMetrics.getInstance().logPostOnCreate(activity);
        currentActivityName = activity.getClass().getSimpleName();
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ActivityLifecycleMetrics.getInstance().logPostOnStart(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ActivityLifecycleMetrics.getInstance().logPostOnResume(activity);
        final String[] tracedMethods = methodsTracingManager.getTracedMethods();
        if (tracedMethods != null) {
            MethodsTracingFinishedDialog dialog = MethodsTracingFinishedDialog.newInstance(tracedMethods);
            dialog.show(activity.getFragmentManager(), MethodsTracingFinishedDialog.TAG);
            methodsTracingManager.clearTracedMethods();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ActivityLifecycleMetrics.getInstance().logOnPaused(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ActivityLifecycleMetrics.getInstance().logOnStopped(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ActivityLifecycleMetrics.getInstance().logOnDestroyed(activity);
    }

    public void addMetricsDataListener(OnMetricsDataListener onDataListener) {
        dataListeners.add(onDataListener);
    }

    public void removeMetricsDataListener(OnMetricsDataListener onDataListener) {
        dataListeners.remove(onDataListener);
    }

    public String getCurrentActivityName() {
        return currentActivityName;
    }
}
