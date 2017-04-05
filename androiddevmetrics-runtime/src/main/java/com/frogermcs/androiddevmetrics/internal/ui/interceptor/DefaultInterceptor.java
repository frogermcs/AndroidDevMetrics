package com.frogermcs.androiddevmetrics.internal.ui.interceptor;

import com.frogermcs.androiddevmetrics.internal.metrics.model.InitMetric;

import java.util.List;

/**
 * @author amulya
 * @since 13 Jan 2017, 12:58 PM.
 */

public final class DefaultInterceptor implements UIInterceptor {

    @Override
    public List<InitMetric> intercept(List<InitMetric> metrics) {
        return metrics;
    }

    @Override
    public String getName() {
        return "Default";
    }
}
