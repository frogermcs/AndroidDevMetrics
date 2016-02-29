package com.frogermcs.androiddevmetrics.internal.ui;

import android.content.res.Resources;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.frogermcs.androiddevmetrics.R;
import com.frogermcs.androiddevmetrics.internal.MetricDescription;
import com.frogermcs.androiddevmetrics.internal.MetricDescriptionTreeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miroslaw Stanek on 30.01.2016.
 */
public class ExpandableMetricsListAdapter extends BaseExpandableListAdapter {

    private final List<MetricDescription> metricDescriptionList = new ArrayList<>();

    public void updateMetrics(List<MetricDescription> metricDescriptions) {
        metricDescriptionList.clear();
        metricDescriptionList.addAll(metricDescriptions);
    }

    @Override
    public int getGroupCount() {
        return metricDescriptionList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return metricDescriptionList.get(groupPosition).descriptionTreeItems.size();
    }

    @Override
    public MetricDescription getGroup(int groupPosition) {
        return metricDescriptionList.get(groupPosition);
    }

    @Override
    public MetricDescriptionTreeItem getChild(int groupPosition, int childPosition) {
        return metricDescriptionList.get(groupPosition).descriptionTreeItems.get(childPosition);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adm_list_item_metrics_header, parent, false);
            viewHolder = new HeaderViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HeaderViewHolder) convertView.getTag();
        }

        MetricDescription metricDescription = getGroup(groupPosition);
        viewHolder.bindView(metricDescription);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        DescriptionViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adm_list_item_metrics_description, parent, false);
            viewHolder = new DescriptionViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DescriptionViewHolder) convertView.getTag();
        }

        MetricDescriptionTreeItem metricDescription = getChild(groupPosition, childPosition);
        viewHolder.bindView(metricDescription);
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
        TextView tvClassName;
        TextView tvInitTime;

        public HeaderViewHolder(View view) {
            this.root = view;
            tvClassName = (TextView) view.findViewById(R.id.tvClassName);
            tvInitTime = (TextView) view.findViewById(R.id.tvInitTime);
        }

        public void bindView(MetricDescription metricDescription) {
            tvClassName.setText(metricDescription.className);
            tvInitTime.setText(metricDescription.formattedInitTime);

            final Resources resources = tvClassName.getContext().getResources();
            if (metricDescription.warningLevel == 1) {
                root.setBackgroundResource(R.color.d2m_bg_warning_1);
                tvClassName.setTextColor(resources.getColor(R.color.d2m_font_warning_1_and_2));
                tvInitTime.setTextColor(resources.getColor(R.color.d2m_font_warning_1_and_2));
            } else if (metricDescription.warningLevel == 2) {
                root.setBackgroundResource(R.color.d2m_bg_warning_2);
                tvClassName.setTextColor(resources.getColor(R.color.d2m_font_warning_1_and_2));
                tvInitTime.setTextColor(resources.getColor(R.color.d2m_font_warning_1_and_2));
            } else if (metricDescription.warningLevel == 3) {
                root.setBackgroundResource(R.color.d2m_bg_warning_3);
                tvClassName.setTextColor(resources.getColor(R.color.d2m_font_warning_3));
                tvInitTime.setTextColor(resources.getColor(R.color.d2m_font_warning_3));
            } else {
                root.setBackgroundResource(R.color.d2m_transparent);
                tvInitTime.setTextColor(resources.getColor(R.color.d2m_font_default_description));
                tvClassName.setTextColor(resources.getColor(R.color.d2m_font_default_title));
            }
        }
    }

    private class DescriptionViewHolder {
        TextView tvTreeDescription;

        public DescriptionViewHolder(View view) {
            tvTreeDescription = (TextView) view.findViewById(R.id.tvTreeDescription);
        }

        public void bindView(MetricDescriptionTreeItem metricDescription) {
            final Resources resources = tvTreeDescription.getContext().getResources();
            tvTreeDescription.setText(Html.fromHtml(metricDescription.description));
            if (metricDescription.warningLevel == 1) {
                tvTreeDescription.setBackgroundResource(R.color.d2m_bg_warning_1);
                tvTreeDescription.setTextColor(resources.getColor(R.color.d2m_font_warning_1_and_2));
            } else if (metricDescription.warningLevel == 2) {
                tvTreeDescription.setBackgroundResource(R.color.d2m_bg_warning_2);
                tvTreeDescription.setTextColor(resources.getColor(R.color.d2m_font_warning_1_and_2));
            } else if (metricDescription.warningLevel == 3) {
                tvTreeDescription.setBackgroundResource(R.color.d2m_bg_warning_3);
                tvTreeDescription.setTextColor(resources.getColor(R.color.d2m_font_warning_3));
            } else {
                tvTreeDescription.setBackgroundResource(R.color.d2m_transparent);
                tvTreeDescription.setTextColor(resources.getColor(R.color.d2m_font_default_description));
            }
        }
    }
}
