package com.frogermcs.androiddevmetrics.internal.metrics.model;

import java.util.List;

/**
 * Created by Miroslaw Stanek on 17.02.2016.
 */
public class FpsDropMetric {
    public double averageFps;
    public int dropsCount;
    public String activityName;

    public static FpsDropMetric oneDrop(double fps) {
        FpsDropMetric fpsDropMetric = new FpsDropMetric();
        fpsDropMetric.averageFps = fps;
        fpsDropMetric.dropsCount = 1;
        return fpsDropMetric;
    }

    public static FpsDropMetric fromDrops(List<Double> drops, String currentActivityName) {
        double averageFps = 0;
        int size = drops.size();
        for (int i = 0; i < size; i++) {
            averageFps += drops.get(i);
        }
        averageFps /= size;

        FpsDropMetric fpsDropMetric = new FpsDropMetric();
        fpsDropMetric.averageFps = averageFps;
        fpsDropMetric.dropsCount = 1;
        fpsDropMetric.activityName = currentActivityName;
        return fpsDropMetric;
    }

    @Override
    public String toString() {
        return "FpsDropMetric{" +
                "averageFps=" + averageFps +
                ", dropsCount=" + dropsCount +
                '}';
    }
}
