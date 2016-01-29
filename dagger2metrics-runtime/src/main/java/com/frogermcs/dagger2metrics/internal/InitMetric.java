package com.frogermcs.dagger2metrics.internal;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Miroslaw Stanek on 23.01.2016.
 */
public class InitMetric {

    public long initTimeMillis = 0;
    public Class<?> cls;
    public Set<InitMetric> args = new HashSet<>();

    public long getTotalInitTime() {
        long total = initTimeMillis;
        for (InitMetric initMetric : args) {
            total += initMetric.getTotalInitTime();
        }
        return total;
    }

    public long getInitTimeWithoutArgs() {
        return initTimeMillis;
    }

    public String getClassName() {
        if (Proxy.isProxyClass(cls)) {
            final Class<?>[] interfaces = cls.getInterfaces();
            if (interfaces.length == 1) {
                return interfaces[0].getSimpleName();
            } else {
                return Arrays.asList(interfaces).toString();
            }
        } else {
            return cls.getSimpleName();
        }
    }

    @Override
    public String toString() {
        if (Proxy.isProxyClass(cls)) {
            return "InitMetric{" +
                    "initTimeMillis=" + initTimeMillis +
                    ", cls=" + Arrays.asList(cls.getInterfaces()) +
                    ", args=" + args +
                    '}';
        } else {
            return "InitMetric{" +
                    "initTimeMillis=" + initTimeMillis +
                    ", cls=" + cls.getSimpleName() +
                    ", args=" + args +
                    '}';
        }
    }
}
