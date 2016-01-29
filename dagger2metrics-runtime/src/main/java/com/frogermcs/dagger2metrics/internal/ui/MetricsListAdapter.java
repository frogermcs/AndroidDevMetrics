package com.frogermcs.dagger2metrics.internal.ui;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.frogermcs.dagger2metrics.R;
import com.frogermcs.dagger2metrics.internal.MetricDescription;

/**
 * Created by Miroslaw Stanek on 26.01.2016.
 */
public class MetricsListAdapter extends ArrayAdapter<MetricDescription> {
    public MetricsListAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_metrics_description, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MetricDescription metricDescription = getItem(position);
        viewHolder.tvClassName.setText(metricDescription.className);
        viewHolder.tvInitTime.setText(metricDescription.formattedInitTime);
        viewHolder.tvTreeDescription.setText(Html.fromHtml(metricDescription.treeDescription));
        viewHolder.collapse();
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    private class ViewHolder {
        View root;
        TextView tvClassName;
        TextView tvInitTime;
        TextView tvTreeDescription;
        ImageView ivExpandCollapse;

        public ViewHolder(View view) {
            this.root = view;
            tvClassName = (TextView) view.findViewById(R.id.tvClassName);
            tvInitTime = (TextView) view.findViewById(R.id.tvInitTime);
            tvTreeDescription = (TextView) view.findViewById(R.id.tvTreeDescription);
            ivExpandCollapse = (ImageView) view.findViewById(R.id.ivExpandCollapse);

            this.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvTreeDescription.getVisibility() == View.VISIBLE) {
                        collapse();
                    } else {
                        expand();
                    }
                }
            });
        }

        public void expand() {
            ivExpandCollapse.setImageResource(R.drawable.ic_triangle_up);
            tvTreeDescription.setVisibility(View.VISIBLE);
        }

        public void collapse() {
            ivExpandCollapse.setImageResource(R.drawable.ic_triangle_down);
            tvTreeDescription.setVisibility(View.GONE);
        }
    }

}
