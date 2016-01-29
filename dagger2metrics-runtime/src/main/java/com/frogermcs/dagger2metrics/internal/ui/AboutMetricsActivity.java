package com.frogermcs.dagger2metrics.internal.ui;

import android.app.Activity;
import android.os.Bundle;

import com.frogermcs.dagger2metrics.R;

/**
 * Created by Miroslaw Stanek on 25.01.2016.
 */
public class AboutMetricsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_metrics);
    }
}
