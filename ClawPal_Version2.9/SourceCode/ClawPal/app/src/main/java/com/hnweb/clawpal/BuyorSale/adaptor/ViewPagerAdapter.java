package com.hnweb.clawpal.BuyorSale.adaptor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hnweb.clawpal.BuyorSale.fragment.Tab1;
import com.hnweb.clawpal.BuyorSale.fragment.Tab2;
import com.hnweb.clawpal.BuyorSale.fragment.Tab3;
import com.hnweb.clawpal.myfavpet.fragment.AdaptionFragment;
import com.hnweb.clawpal.myfavpet.fragment.BuySaleFragment;
import com.hnweb.clawpal.myfavpet.fragment.LostFoundFragment;

/**
 * Created by Shree on 31-Jan-18.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs, String gujug) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Tab1 tab1 = new Tab1();
                return tab1;
            case 1:
                Tab2 tab2 = new Tab2();
                return tab2;
            case 2:
                Tab3 tab3 = new Tab3();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


}