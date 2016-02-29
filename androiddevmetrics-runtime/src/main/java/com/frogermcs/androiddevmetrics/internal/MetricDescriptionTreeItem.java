package com.frogermcs.androiddevmetrics.internal;

/**
 * Created by Miroslaw Stanek on 30.01.2016.
 */
public class MetricDescriptionTreeItem {
    public String description;
    public int warningLevel;

    public MetricDescriptionTreeItem() {
    }

    public MetricDescriptionTreeItem(String description) {
        this(description, 0);
    }

    public MetricDescriptionTreeItem(String description, int warningLevel) {
        this.description = description;
        this.warningLevel = warningLevel;
    }
}
