package com.frogermcs.androiddevmetrics.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Miroslaw Stanek on 17.03.2016.
 */
public class MethodsTracingManager {

    private static final String PREFS_KEY_SCHEDULED_METHODS = "PREFS_KEY_SCHEDULED_METHODS";

    private static class Holder {
        static final MethodsTracingManager INSTANCE = new MethodsTracingManager();
    }

    public static MethodsTracingManager getInstance() {
        return Holder.INSTANCE;
    }

    private boolean isInitialized = false;
    private SharedPreferences sharedPreferences;
    private Set<String> scheduledMethods;

    private Set<String> tracedMethods;

    public void init(Context context) {
        this.isInitialized = true;
        sharedPreferences = context.getSharedPreferences("ADM_PREFS", Context.MODE_PRIVATE);
        scheduledMethods = sharedPreferences.getStringSet(PREFS_KEY_SCHEDULED_METHODS, new HashSet<String>());
        tracedMethods = new HashSet<>();
    }

    public void scheduleMethodTracing(String activityName, String method) {
        checkInitialized();

        scheduledMethods.add(methodKey(activityName, method));
        updatePrefs();
    }

    public void disableMethodTracing(String activityName, String method) {
        checkInitialized();

        scheduledMethods.remove(methodKey(activityName, method));
        updatePrefs();
    }

    public void addTracedMethod(String methodName) {
        tracedMethods.add(methodName);
    }

    public String[] getTracedMethods() {
        if (tracedMethods.size() == 0) return null;

        String[] methodsArray = new String[tracedMethods.size()];
        tracedMethods.toArray(methodsArray);
        tracedMethods.clear();
        return methodsArray;
    }

    public void clearTracedMethods() {
        tracedMethods.clear();
    }

    @NonNull
    private String methodKey(String activityName, String method) {
        return activityName + "|" + method;
    }

    private void updatePrefs() {
        sharedPreferences.edit()
                .clear()
                .putStringSet(PREFS_KEY_SCHEDULED_METHODS, scheduledMethods)
                .commit();
    }

    public boolean shouldTraceMethod(String activityName, String method) {
        checkInitialized();

        return scheduledMethods.contains(methodKey(activityName, method));
    }

    private void checkInitialized() {
        if (!isInitialized) throw new RuntimeException("MethodsTracingManager must be initialized by init(..)");
    }
}
