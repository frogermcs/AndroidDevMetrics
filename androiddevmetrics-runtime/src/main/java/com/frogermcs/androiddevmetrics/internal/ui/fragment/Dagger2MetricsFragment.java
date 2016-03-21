package com.frogermcs.androiddevmetrics.internal.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.frogermcs.androiddevmetrics.R;
import com.frogermcs.androiddevmetrics.aspect.Dagger2GraphAnalyzer;
import com.frogermcs.androiddevmetrics.internal.metrics.InitManager;
import com.frogermcs.androiddevmetrics.internal.ui.ExpandableMetricsListAdapter;

/**
 * Created by Miroslaw Stanek on 25.01.2016.
 */
public class Dagger2MetricsFragment extends Fragment {

    private ExpandableListView lvMetrics;
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.adm_fragment_dagger2_metrics, container, false);
        lvMetrics = (ExpandableListView) view.findViewById(R.id.lvMetrics);
        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ExpandableMetricsListAdapter adapter = new ExpandableMetricsListAdapter();
        lvMetrics.setAdapter(adapter);
        adapter.updateMetrics(InitManager.getInstance().getListOfMetricDescriptions());

        if (!Dagger2GraphAnalyzer.isEnabled()) {
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText("Dagger 2 metrics disabled");
            lvMetrics.setVisibility(View.GONE);
        } else if (adapter.getGroupCount() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            lvMetrics.setVisibility(View.GONE);
            tvEmpty.setText("No collected data");
        } else {
            tvEmpty.setVisibility(View.GONE);
            lvMetrics.setVisibility(View.VISIBLE);
        }
    }
}
