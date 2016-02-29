package com.frogermcs.androiddevmetrics.internal.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.frogermcs.androiddevmetrics.R;
import com.frogermcs.androiddevmetrics.internal.metrics.ActivityLifecycleMetrics;

/**
 * Created by Miroslaw Stanek on 25.01.2016.
 */
public class MetricsActivity extends FragmentActivity {

    private Button btnActivities;
    private Button btnDagger2;
    private ViewPager vpMetrics;

    private ActivitiesMetricsFragment activitiesMetricsFragment;
    private Dagger2MetricsFragment dagger2MetricsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adm_activity_metrics);
        btnActivities = (Button) findViewById(R.id.btnActivities);
        btnDagger2 = (Button) findViewById(R.id.btnDagger2);
        vpMetrics = (ViewPager) findViewById(R.id.vpMetrics);

        activitiesMetricsFragment = new ActivitiesMetricsFragment();
        dagger2MetricsFragment = new Dagger2MetricsFragment();

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return activitiesMetricsFragment;
                } else if (position == 1) {
                    return dagger2MetricsFragment;
                }

                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        vpMetrics.setAdapter(fragmentPagerAdapter);
        vpMetrics.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    activitiesPageSelected();
                } else if (position == 1) {
                    daggerPageSelected();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpMetrics.setCurrentItem(0);
            }
        });
        btnDagger2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpMetrics.setCurrentItem(1);
            }
        });

        activitiesPageSelected();
    }

    private void activitiesPageSelected() {
        btnActivities.setSelected(true);
        btnDagger2.setSelected(false);
    }

    private void daggerPageSelected() {
        btnActivities.setSelected(false);
        btnDagger2.setSelected(true);
    }
}
