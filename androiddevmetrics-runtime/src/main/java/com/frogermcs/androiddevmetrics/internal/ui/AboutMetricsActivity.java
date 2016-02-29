package com.frogermcs.androiddevmetrics.internal.ui;

import android.app.Activity;
import android.os.Bundle;

import com.frogermcs.androiddevmetrics.R;

/**
 * Created by Miroslaw Stanek on 25.01.2016.
 */
public class AboutMetricsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adm_activity_about_metrics);
    }
}
