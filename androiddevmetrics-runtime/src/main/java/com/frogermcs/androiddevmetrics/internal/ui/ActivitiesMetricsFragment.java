package com.frogermcs.androiddevmetrics.internal.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.frogermcs.androiddevmetrics.R;
import com.frogermcs.androiddevmetrics.aspect.ActivityLifecycleAnalyzer;
import com.frogermcs.androiddevmetrics.internal.metrics.ActivityLifecycleMetrics;

/**
 * Created by Miroslaw Stanek on 22.02.2016.
 */
public class ActivitiesMetricsFragment extends Fragment {

    private ExpandableListView lvActivitiesMetrics;
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adm_fragment_activities_metrics, container, false);
        lvActivitiesMetrics = (ExpandableListView) view.findViewById(R.id.lvActivitiesMetrics);
        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ExpandableActivitiesMetricsListAdapter adapter = new ExpandableActivitiesMetricsListAdapter();
        lvActivitiesMetrics.setAdapter(adapter);
        adapter.updateMetrics(ActivityLifecycleMetrics.getInstance().getListOfMetricDescriptions());

        if (!ActivityLifecycleAnalyzer.isEnabled()) {
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText("Activities lifecycle metrics disabled");
            lvActivitiesMetrics.setVisibility(View.GONE);
        } else if (adapter.getGroupCount() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            lvActivitiesMetrics.setVisibility(View.GONE);
            tvEmpty.setText("No collected data");
        } else {
            tvEmpty.setVisibility(View.GONE);
            lvActivitiesMetrics.setVisibility(View.VISIBLE);
        }
    }
}
