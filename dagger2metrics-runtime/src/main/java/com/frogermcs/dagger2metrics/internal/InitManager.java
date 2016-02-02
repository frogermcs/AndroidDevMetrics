package com.frogermcs.dagger2metrics.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public final Map<String, InitMetric> initializedMetrics = new HashMap<>();
    public final Map<String, Integer> initCounter = new HashMap<>();

    public void addInitMetric(Class<?> initializedClass, Object[] args, long initTimeMillis) {
        InitMetric initMetric = new InitMetric();
        initMetric.initTimeMillis = initTimeMillis;
        initMetric.cls = initializedClass;

        String simpleName = initializedClass.getSimpleName();
        if (!initializedMetrics.containsKey(simpleName)) {
            initializedMetrics.put(simpleName, initMetric);
            int argsLength = args.length;
            for (int i = 0; i < argsLength; i++) {
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
            initializedMetrics.put(simpleName + "#" + counterVal, initMetric);
        }

    }

    public List<MetricDescription> getListOfMetricDescriptions() {
        List<MetricDescription> metricDescriptions = new ArrayList<>();
        for (InitMetric initMetric : InitManager.getInstance().initializedMetrics.values()) {
            metricDescriptions.add(MetricDescription.InitFromMetric(initMetric));
        }
        return metricDescriptions;
    }
}
