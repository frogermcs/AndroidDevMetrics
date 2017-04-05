package com.frogermcs.androiddevmetrics.internal.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.frogermcs.androiddevmetrics.R;
import com.frogermcs.androiddevmetrics.aspect.Dagger2GraphAnalyzer;
import com.frogermcs.androiddevmetrics.internal.metrics.InitManager;
import com.frogermcs.androiddevmetrics.internal.ui.ExpandableMetricsListAdapter;
import com.frogermcs.androiddevmetrics.internal.ui.interceptor.UIInterceptor;

import java.util.List;

/**
 * Created by Miroslaw Stanek on 25.01.2016.
 */
public class Dagger2MetricsFragment extends Fragment {

    private ExpandableListView lvMetrics;
    private TextView tvEmpty;
    private Button btnMenu;
    private List<UIInterceptor> interceptorList;
    private ExpandableMetricsListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.adm_fragment_dagger2_metrics, container, false);
        lvMetrics = (ExpandableListView) view.findViewById(R.id.lvMetrics);
        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
        interceptorList = AndroidDevMetrics.singleton().interceptors();
        if (interceptorList.size() > 1) {
            btnMenu = (Button) view.findViewById(R.id.btnMenu);
            btnMenu.setVisibility(View.VISIBLE);
            btnMenu.setText(interceptorList.get(0).getName());
            btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUIInterceptorMenu();
                }
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ExpandableMetricsListAdapter();
        lvMetrics.setAdapter(adapter);
        adapter.updateMetrics(InitManager.getInstance().getListOfMetricDescriptions(interceptorList.get(0)));

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

    private void showUIInterceptorMenu() {
        String[] transformerNames = new String[interceptorList.size()];
        for (int i = 0; i < interceptorList.size(); i++) {
            transformerNames[i] = interceptorList.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Settings")
                .setItems(transformerNames, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        btnMenu.setText(interceptorList.get(which).getName());
                        adapter.updateMetrics(InitManager.getInstance().getListOfMetricDescriptions(interceptorList.get(which)));
                        adapter.notifyDataSetChanged();
                    }
                });
        builder.create().show();
    }
}
