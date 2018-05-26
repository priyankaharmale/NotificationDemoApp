package com.hnweb.clawpal.myfavpet.adaptor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hnweb.clawpal.myfavpet.fragment.AdaptionFragment;
import com.hnweb.clawpal.myfavpet.fragment.BuySaleFragment;
import com.hnweb.clawpal.myfavpet.fragment.LostFoundFragment;

/**
 * Created by Priyanka on 06-Dec-17.
 */

public class TabsPagerAdapter  extends FragmentStatePagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new BuySaleFragment();
            case 1:
                // Games fragment activity
                return new LostFoundFragment();
            case 2:
                // Movies fragment activity
                return new AdaptionFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
}
