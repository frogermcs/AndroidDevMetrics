package com.frogermcs.androiddevmetrics.internal;

import com.frogermcs.androiddevmetrics.internal.metrics.model.InitMetric;
import com.frogermcs.androiddevmetrics.AndroidDevMetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Miroslaw Stanek on 25.01.2016.
 */
public class MetricDescription extends MetricDescriptionTreeItem {
    public final List<MetricDescriptionTreeItem> descriptionTreeItems = new ArrayList<>();

    public String className;
    public String formattedInitTime;

    public MetricDescription() {
    }

    public static MetricDescription InitFromMetric(InitMetric initMetric) {
        MetricDescription metricDescription = new MetricDescription();
        metricDescription.className = initMetric.getClassName();
        metricDescription.formatInitTime(initMetric.getTotalInitTime(), initMetric.getInitTimeWithoutArgs());
        metricDescription.initDescriptionsTree(initMetric.args, 0);
        return metricDescription;
    }

    private void formatInitTime(long overallInitTime, long noArgsInitTime) {
        StringBuilder sb = new StringBuilder("Initialization: ");
        sb.append(noArgsInitTime);
        sb.append("ms, ");
        sb.append("with args: ");
        sb.append(overallInitTime);
        sb.append("ms");
        formattedInitTime = sb.toString();
        warningLevel = getWarningLevel(noArgsInitTime);
    }

    private void initDescriptionsTree(Set<InitMetric> initMetrics, int depthLevel) {
        if (depthLevel == 0 && initMetrics.size() == 0) {
            descriptionTreeItems.add(new MetricDescriptionTreeItem("No args or args initialized before", 0));
        }
        for (InitMetric initMetric : initMetrics) {
            final long initTimeWithoutArgs = initMetric.getInitTimeWithoutArgs();
            final long totalInitTime = initMetric.getTotalInitTime();
            final int warningLevel = getWarningLevel(initTimeWithoutArgs);

            String depthStr = new String(new char[depthLevel]).replace("\0", "\t\t");
            StringBuilder sb = new StringBuilder(depthStr);
            sb.append("|__<b>");
            sb.append(initMetric.getClassName());
            sb.append("</b>: ");
            sb.append(initTimeWithoutArgs);
            sb.append("ms, with args: ");
            sb.append(totalInitTime);
            sb.append("ms");
            descriptionTreeItems.add(new MetricDescriptionTreeItem(sb.toString(), warningLevel));
            if (initMetric.args.size() > 0) {
                initDescriptionsTree(initMetric.args, depthLevel + 1);
            }
        }
    }

    private int getWarningLevel(long initTimeWithoutArgs) {
        if (initTimeWithoutArgs < AndroidDevMetrics.singleton().dagger2WarningLevel1()) {
            return 0;
        } else if (initTimeWithoutArgs < AndroidDevMetrics.singleton().dagger2WarningLevel2()) {
            return 1;
        } else if (initTimeWithoutArgs < AndroidDevMetrics.singleton().dagger2WarningLevel3()) {
            return 2;
        } else {
            return 3;
        }
    }
}
