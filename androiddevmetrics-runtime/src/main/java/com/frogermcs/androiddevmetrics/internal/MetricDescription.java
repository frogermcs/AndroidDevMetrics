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
        metricDescription.className = initMetric.getSimpleClassName();
        metricDescription.formatInitTime(initMetric.getTotalInitTime(),
                initMetric.getInitTimeWithoutArgs(),
                initMetric.getThreadName());
        metricDescription.initDescriptionsTree(initMetric.args, 0, "");
        return metricDescription;
    }

    private void formatInitTime(long overallInitTime, long noArgsInitTime, String threadName) {
        StringBuilder sb = new StringBuilder("Initialization: ");
        sb.append(noArgsInitTime);
        sb.append("ms, ");
        sb.append("with args: ");
        sb.append(overallInitTime);
        sb.append("ms");
        sb.append(" <font color='#00BFA5'>(" + threadName + ")</font>");
        formattedInitTime = sb.toString();
        warningLevel = getWarningLevel(noArgsInitTime);
    }

    private void initDescriptionsTree(Set<InitMetric> initMetrics, int depthLevel, String prev) {
        if (depthLevel == 0 && initMetrics.size() == 0) {
            descriptionTreeItems.add(new MetricDescriptionTreeItem("No args or args initialized before", 0));
        }
        int size = initMetrics.size();
        int count = 0;
        for (InitMetric initMetric : initMetrics) {
            final long initTimeWithoutArgs = initMetric.getInitTimeWithoutArgs();
            final long totalInitTime = initMetric.getTotalInitTime();
            final int warningLevel = getWarningLevel(initTimeWithoutArgs);

            String depthStr = prev + "\u2502" + space(1);
            String edgeChar = "\u251c";
            String secondRowChar = "\u2502";
            if (count == size - 1) {
                edgeChar = "\u2514";
                secondRowChar = space(1);
                depthStr = prev + space(2);
            }

            StringBuilder sb = new StringBuilder(prev);
            sb.append(edgeChar + "\u2500\u25CF <b>");
            sb.append(initMetric.getSimpleClassName());
            sb.append("</b><br/>");
            sb.append(prev + secondRowChar + space(1));
            sb.append(initTimeWithoutArgs);
            sb.append("ms, with args: ");
            sb.append(totalInitTime);
            sb.append("ms");
            sb.append(" <font color='#00BFA5'>(" + initMetric.threadName + ")</font>");
            descriptionTreeItems.add(new MetricDescriptionTreeItem(sb.toString(), warningLevel));
            if (initMetric.args.size() > 0) {
                initDescriptionsTree(initMetric.args, depthLevel + 1, depthStr);
            }

            count++;
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

    private String space(int count) {
        String spaceChar = "\u2000\u2006";
        for (int i = 0; i < count - 1; i++) {
            spaceChar += spaceChar;
        }
        return spaceChar;
    }
}
