package com.frogermcs.androiddevmetrics.internal.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.frogermcs.androiddevmetrics.R;
import com.frogermcs.androiddevmetrics.internal.metrics.ActivityLaunchMetrics;
import com.frogermcs.androiddevmetrics.internal.metrics.ActivityLifecycleMetrics;

/**
 * Created by Miroslaw Stanek on 22.02.2016.
 */
public class ActivitiesMetricsFragment extends Fragment {

    private ExpandableListView lvActivitiesMetrics;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adm_fragment_activities_metrics, container, false);
        lvActivitiesMetrics = (ExpandableListView) view.findViewById(R.id.lvActivitiesMetrics);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ExpandableActivitiesMetricsListAdapter adapter = new ExpandableActivitiesMetricsListAdapter();
        lvActivitiesMetrics.setAdapter(adapter);
        adapter.updateMetrics(ActivityLifecycleMetrics.getInstance().getListOfMetricDescriptions());
    }
}
