package com.frogermcs.androiddevmetrics.internal.metrics;

import com.frogermcs.androiddevmetrics.internal.MetricDescription;
import com.frogermcs.androiddevmetrics.internal.metrics.model.InitMetric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Miroslaw Stanek on 24.01.2016.
 */
public class InitManager {
    private static class Holder {
        static final InitManager INSTANCE = new InitManager();
    }

    public static InitManager getInstance() {
        return Holder.INSTANCE;
    }

    public final Set<OnMetricsDataListener> dataListeners = new HashSet<>();

    public final LinkedHashMap<String, InitMetric> initializedMetrics = new LinkedHashMap<>();
    public final LinkedHashMap<String, Integer> initCounter = new LinkedHashMap<>();

    public void addInitMetric(Class<?> initializedClass, Object[] args, long initTimeMillis) {
        InitMetric initMetric = new InitMetric();
        initMetric.initTimeMillis = initTimeMillis;
        initMetric.cls = initializedClass;

        String simpleName = initializedClass.getSimpleName();
        if (!initializedMetrics.containsKey(simpleName)) {
            putInitMetric(simpleName, initMetric);
            int argsLength = args.length;
            for (int i = 0; i < argsLength; i++) {
                if (args[i] == null) continue;
                String argClassSimpleName = args[i].getClass().getSimpleName();
                InitMetric argMethics = initializedMetrics.get(argClassSimpleName);
                if (argMethics != null) {
                    initMetric.args.add(argMethics);
                    initializedMetrics.remove(argClassSimpleName);
                }
            }
            initMetric.instanceNo = 0;
            initCounter.put(simpleName, 0);
        } else {
            int counterVal = initCounter.get(simpleName) + 1;
            initCounter.put(simpleName, counterVal);
            initMetric.instanceNo = counterVal;
            putInitMetric(simpleName + "#" + counterVal, initMetric);
        }
    }

    private void putInitMetric(String key, InitMetric initMetric) {
        initializedMetrics.put(key, initMetric);
        for (OnMetricsDataListener dataListener : dataListeners) {
            dataListener.onInitNewMetricRecorded(initMetric);
        }
    }

    public List<MetricDescription> getListOfMetricDescriptions() {
        List<MetricDescription> metricDescriptions = new ArrayList<>();
        for (InitMetric initMetric : InitManager.getInstance().initializedMetrics.values()) {
            metricDescriptions.add(MetricDescription.InitFromMetric(initMetric));
        }
        return metricDescriptions;
    }

    public void addMetricsDataListener(OnMetricsDataListener onDataListener) {
        dataListeners.add(onDataListener);
    }

    public void removeMetricsDataListener(OnMetricsDataListener onDataListener) {
        dataListeners.remove(onDataListener);
    }
}
