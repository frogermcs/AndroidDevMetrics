package com.frogermcs.androiddevmetrics.internal.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.frogermcs.androiddevmetrics.aspect.ActivityLifecycleAnalyzer;
import com.frogermcs.androiddevmetrics.internal.ActivityMetricDescription;
import com.frogermcs.androiddevmetrics.internal.MethodsTracingManager;

/**
 * Created by Miroslaw Stanek on 17.03.2016.
 */
public class ActivitiesMethodsPickerDialog extends DialogFragment {
    public static final String TAG = "ActivitiesMethodsPickerDialog";

    private static final String ARG_ACTIVITY_NAME = "ARG_ACTIVITY_NAME";
    private static final String ARG_IMPLEMENTED_METHODS = "ARG_IMPLEMENTED_METHODS";

    String[] items;
    boolean[] enabledItems;
    private String activityName;

    public static ActivitiesMethodsPickerDialog newInstance(ActivityMetricDescription activityMetricDescription) {
        ActivitiesMethodsPickerDialog dialog = new ActivitiesMethodsPickerDialog();
        Bundle args = new Bundle();
        args.putString(ARG_ACTIVITY_NAME, activityMetricDescription.activityName);
        args.putStringArray(ARG_IMPLEMENTED_METHODS, activityMetricDescription.getImplementedMethods());
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityName = getArguments().getString(ARG_ACTIVITY_NAME);
        items = getArguments().getStringArray(ARG_IMPLEMENTED_METHODS);
        enabledItems = new boolean[]{
                MethodsTracingManager.getInstance().shouldTraceMethod(activityName, ActivityLifecycleAnalyzer.METHOD_ON_CREATE),
                MethodsTracingManager.getInstance().shouldTraceMethod(activityName, ActivityLifecycleAnalyzer.METHOD_ON_START),
                MethodsTracingManager.getInstance().shouldTraceMethod(activityName, ActivityLifecycleAnalyzer.METHOD_ON_RESUME)
        };
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick methods (implemented only)")
                .setMultiChoiceItems(items, enabledItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        enabledItems[which] = isChecked;
                        if (isChecked) {
                            MethodsTracingManager.getInstance().scheduleMethodTracing(activityName, items[which]);
                        } else {
                            MethodsTracingManager.getInstance().disableMethodTracing(activityName, items[which]);
                        }
                    }
                })
                .setPositiveButton("OK", null);

        return builder.create();
    }
}