package com.frogermcs.androiddevmetrics.internal.metrics;

import com.frogermcs.androiddevmetrics.internal.metrics.model.FpsDropMetric;
import com.frogermcs.androiddevmetrics.internal.metrics.model.InitMetric;

/**
 * Created by Miroslaw Stanek on 06.02.2016.
 */
public interface OnMetricsDataListener {
    void onInitNewMetricRecorded(InitMetric initMetric);
    void onFrameDropRegistered(FpsDropMetric fpsDropMetric);
}
