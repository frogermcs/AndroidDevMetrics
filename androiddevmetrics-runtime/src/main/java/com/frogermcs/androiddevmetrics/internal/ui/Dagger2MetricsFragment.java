package com.frogermcs.androiddevmetrics.internal.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.frogermcs.androiddevmetrics.R;
import com.frogermcs.androiddevmetrics.internal.metrics.InitManager;

/**
 * Created by Miroslaw Stanek on 25.01.2016.
 */
public class Dagger2MetricsFragment extends Fragment {

    private ExpandableListView lvMetrics;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.adm_fragment_dagger2_metrics, container, false);
        lvMetrics = (ExpandableListView) view.findViewById(R.id.lvMetrics);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ExpandableMetricsListAdapter adapter = new ExpandableMetricsListAdapter();
        lvMetrics.setAdapter(adapter);
        adapter.updateMetrics(InitManager.getInstance().getListOfMetricDescriptions());
    }
}
