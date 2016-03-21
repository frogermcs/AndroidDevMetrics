package com.frogermcs.androiddevmetrics.internal.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;

import java.util.Locale;

/**
 * Created by Miroslaw Stanek on 17.03.2016.
 */
public class MethodsTracingFinishedDialog extends DialogFragment {
    public static final String TAG = "MethodsTracingFinishedDialog";

    private static final String ARG_TRACED_METHODS = "ARG_TRACED_METHODS";

    private String[] items;
    private String formattedCommands;

    public static MethodsTracingFinishedDialog newInstance(String[] methods) {
        MethodsTracingFinishedDialog f = new MethodsTracingFinishedDialog();
        Bundle args = new Bundle();
        args.putStringArray(ARG_TRACED_METHODS, methods);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = getArguments().getStringArray(ARG_TRACED_METHODS);

        String cmd = "<b><i>$ adb pull %s</i></b><br/>";
        formattedCommands = "";

        for (String method : items) {
            formattedCommands += String.format(Locale.ENGLISH, cmd, method);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tracing finished")
                .setMessage(Html.fromHtml("Tracing is finished. Plug your device and type in terminal:<br/><br/>" +
                        formattedCommands + "<br/><br/>" +
                        "Then drag and drop file(s) to Android Studio.")

                )
                .setPositiveButton("OK", null);

        return builder.create();
    }
}