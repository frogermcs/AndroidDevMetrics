package com.frogermcs.dagger2metrics.internal.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.frogermcs.dagger2metrics.R;
import com.frogermcs.dagger2metrics.internal.InitManager;
import com.frogermcs.dagger2metrics.internal.InitMetric;
import com.frogermcs.dagger2metrics.internal.MetricDescription;

/**
 * Created by Miroslaw Stanek on 25.01.2016.
 */
public class MetricsActivity extends Activity {

    private ListView lvMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metrics);
        lvMetrics = (ListView) findViewById(R.id.lvMetrics);

        final MetricsListAdapter adapter = new MetricsListAdapter(this);
        for (InitMetric initMetric : InitManager.getInstance().initializedMetrics.values()) {
            adapter.add(MetricDescription.InitFromMetric(initMetric));
        }

        lvMetrics.setAdapter(adapter);
    }

    public void openAboutMetrics(View view){
        startActivity(new Intent(this, AboutMetricsActivity.class));
    }
}
