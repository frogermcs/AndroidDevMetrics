package com.frogermcs.dagger2metrics.internal.ui;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.frogermcs.dagger2metrics.R;
import com.frogermcs.dagger2metrics.internal.InitManager;
import com.frogermcs.dagger2metrics.internal.InitMetric;
import com.frogermcs.dagger2metrics.internal.MetricDescription;

/**
 * Created by Miroslaw Stanek on 25.01.2016.
 */
public class MetricsActivity extends Activity {

    private ExpandableListView lvMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d2m_activity_metrics);
        lvMetrics = (ExpandableListView) findViewById(R.id.lvMetrics);

        ExpandableMetricsListAdapter adapter = new ExpandableMetricsListAdapter();
        lvMetrics.setAdapter(adapter);
        adapter.updateMetrics(InitManager.getInstance().getListOfMetricDescriptions());
    }
}
