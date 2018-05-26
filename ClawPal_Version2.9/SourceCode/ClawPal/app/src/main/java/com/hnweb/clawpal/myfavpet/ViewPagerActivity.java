package com.hnweb.clawpal.myfavpet;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.hnweb.clawpal.DashboardActivity;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.myfavpet.adaptor.TabsPagerAdapter;

/**
 * Created by Priyanka on 06-Dec-17.
 */


public class ViewPagerActivity extends FragmentActivity {

    TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    // Tab titles
    Toolbar toolbar;
    private String[] tabs = {"Buy or Sale", "Lost or Found", "Adaption"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_btn_img);

        for (String tab_name : tabs) {
            tabLayout.addTab(tabLayout.newTab().setText(tab_name));
        }
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        onBackPressed();

                    }

                }

        );

//set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#ffffff"));

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
//change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//change ViewPager page when tab selected
//tabLayout.setupWithViewPager(viewPager);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.e("onTabSelected", "onTabUnselected" + tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.e("onTabUnselected", "onTabUnselected" + tab.getPosition());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.e("onTabReselected", "onTabReselected" + tab.getPosition());
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewPagerActivity.this,DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
