package com.frogermcs.androiddevmetrics.internal.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Miroslaw Stanek on 17.03.2016.
 */
public class EmulatorIsNotSupportedDialog extends DialogFragment {
    public static final String TAG = "EmulatorIsNotSupportedDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Emulator not supported")
                .setMessage("Currently methods tracing works only on real devices.")
                .setPositiveButton("OK", null);

        return builder.create();
    }
}