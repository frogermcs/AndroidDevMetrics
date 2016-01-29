package com.frogermcs.dagger2metrics.internal;

import java.util.HashMap;
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

    public void addInitMetric(Class<?> initializedClass, Object[] args, long initTimeMillis) {
        InitMetric initMetric = new InitMetric();
        initMetric.initTimeMillis = initTimeMillis;
        initMetric.cls = initializedClass;

        initializedMetrics.put(initializedClass.getSimpleName(), initMetric);

        int argsLength = args.length;
        for (int i = 0; i < argsLength; i++) {
            String simpleName = args[i].getClass().getSimpleName();
            InitMetric argMethics = initializedMetrics.get(simpleName);
            if (argMethics != null) {
                initMetric.args.add(argMethics);
                initializedMetrics.remove(simpleName);
            }
        }
    }
}
