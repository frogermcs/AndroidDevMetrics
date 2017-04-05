package com.frogermcs.androiddevmetrics.internal.ui.interceptor;

import com.frogermcs.androiddevmetrics.internal.metrics.model.InitMetric;

import java.util.List;

/**
 * @author amulya
 * @since 13 Jan 2017, 12:51 PM.
 */

public interface UIInterceptor {

    List<InitMetric> intercept(List<InitMetric> metrics);

    String getName();
}
