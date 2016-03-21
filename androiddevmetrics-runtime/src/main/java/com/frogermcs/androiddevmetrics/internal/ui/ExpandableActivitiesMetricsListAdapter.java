package com.frogermcs.androiddevmetrics.internal.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.frogermcs.androiddevmetrics.R;
import com.frogermcs.androiddevmetrics.internal.ActivityMetricDescription;
import com.frogermcs.androiddevmetrics.internal.ui.fragment.ActivitiesMetricsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miroslaw Stanek on 30.01.2016.
 */
public class ExpandableActivitiesMetricsListAdapter extends BaseExpandableListAdapter {

    private final List<ActivityMetricDescription> metricDescriptionList = new ArrayList<>();

    private ActivitiesMetricsFragment activitiesMetricsFragment;

    public ExpandableActivitiesMetricsListAdapter(ActivitiesMetricsFragment activitiesMetricsFragment) {
        this.activitiesMetricsFragment = activitiesMetricsFragment;
    }

    public void updateMetrics(List<ActivityMetricDescription> metricDescriptions) {
        metricDescriptionList.clear();
        metricDescriptionList.addAll(metricDescriptions);
    }

    @Override
    public int getGroupCount() {
        return metricDescriptionList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public ActivityMetricDescription getGroup(int groupPosition) {
        return metricDescriptionList.get(groupPosition);
    }

    @Override
    public ActivityMetricDescription getChild(int groupPosition, int childPosition) {
        return metricDescriptionList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        HeaderViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(com.frogermcs.androiddevmetrics.R.layout.adm_list_item_activity_metrics_header, parent, false);
            viewHolder = new HeaderViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HeaderViewHolder) convertView.getTag();
        }

        ActivityMetricDescription activityMetricDescription = getGroup(groupPosition);
        viewHolder.bindView(activityMetricDescription);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        DescriptionViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(com.frogermcs.androiddevmetrics.R.layout.adm_list_item_activity_metrics_description, parent, false);
            viewHolder = new DescriptionViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DescriptionViewHolder) convertView.getTag();
        }

        ActivityMetricDescription activityMetricDescription = getChild(groupPosition, childPosition);
        viewHolder.bindView(activityMetricDescription);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    private class HeaderViewHolder {
        View root;
        TextView tvActivityName;
        TextView tvActivityDescription;

        public HeaderViewHolder(View view) {
            this.root = view;
            tvActivityName = (TextView) view.findViewById(com.frogermcs.androiddevmetrics.R.id.tvActivityName);
            tvActivityDescription = (TextView) view.findViewById(com.frogermcs.androiddevmetrics.R.id.tvActivityDescription);
        }

        public void bindView(ActivityMetricDescription activityMetricDescription) {
            tvActivityName.setText(activityMetricDescription.activitySimpleName);
            String frameDrops;
            if (activityMetricDescription.frameDropsCount == 0) {
                frameDrops = "No frame drops";
            } else {
                frameDrops = String.format("Dropped frames %d", activityMetricDescription.frameDropsCount);
            }

            String subtitle = String.format("%s%s | %s",
                    activityMetricDescription.isLauncherActivity ? "App launcher | " : "",
                    frameDrops,
                    "instances " + activityMetricDescription.instancesCount
            );
            tvActivityDescription.setText(subtitle);
        }
    }

    private class DescriptionViewHolder {
        TextView tvOnCreateTime;
        TextView tvOnStartTime;
        TextView tvOnResumeTime;
        TextView tvOnLayoutTime;
        TextView tvOverallTime;
        TextView tvInstancesCount;
        TextView tvFrameDrops;
        Button btnScheduleMethodTracing;

        public DescriptionViewHolder(View view) {
            tvInstancesCount = (TextView) view.findViewById(R.id.tvInstancesCount);
            tvFrameDrops = (TextView) view.findViewById(R.id.tvFrameDrops);
            tvOnCreateTime = (TextView) view.findViewById(R.id.tvOnCreateTime);
            tvOnStartTime = (TextView) view.findViewById(R.id.tvOnStartTime);
            tvOnResumeTime = (TextView) view.findViewById(R.id.tvOnResumeTime);
            tvOnLayoutTime = (TextView) view.findViewById(R.id.tvOnLayoutTime);
            tvOverallTime = (TextView) view.findViewById(R.id.tvOverallTime);
            btnScheduleMethodTracing = (Button) view.findViewById(R.id.btnScheduleMethodTracing);
            btnScheduleMethodTracing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activitiesMetricsFragment.scheduleTracingForActivity(((ActivityMetricDescription)v.getTag()));
                }
            });
        }

        public void bindView(ActivityMetricDescription activityMetricDescription) {
            tvInstancesCount.setText(Integer.toString(activityMetricDescription.instancesCount));
            if (activityMetricDescription.frameDropsCount == 0) {
                tvFrameDrops.setText("No frame drops!");
            } else {
                tvFrameDrops.setText(String.format("%d/%.2f fps", activityMetricDescription.frameDropsCount, activityMetricDescription.getAverageFps()));
            }
            tvOverallTime.setText(activityMetricDescription.getOverallTimeMillis() + "ms");
            tvOnCreateTime.setText(activityMetricDescription.activityCreateMillis + "ms");
            tvOnStartTime.setText(activityMetricDescription.activityStartMillis + "ms");
            tvOnResumeTime.setText(activityMetricDescription.activityResumeMillis + "ms");
            tvOnLayoutTime.setText(activityMetricDescription.activityLayoutTimeMillis + "ms");
            btnScheduleMethodTracing.setTag(activityMetricDescription);
        }
    }
}
