package com.frogermcs.androiddevmetrics.internal.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Miroslaw Stanek on 17.03.2016.
 */
public class PermissionNotGrantedDialog extends DialogFragment {
    public static final String TAG = "PermissionNotGrantedDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Grant permission")
                .setMessage("To use method tracing feature you have to grant \"WRITE_EXTERNAL_STORAGE\" permission. Go to device settings and enable it.")
                .setPositiveButton("OK", null);

        return builder.create();
    }
}