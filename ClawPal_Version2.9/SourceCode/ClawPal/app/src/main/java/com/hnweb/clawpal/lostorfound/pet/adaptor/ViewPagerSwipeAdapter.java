package com.hnweb.clawpal.lostorfound.pet.adaptor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hnweb.clawpal.BuyorSale.fragment.Tab1;
import com.hnweb.clawpal.BuyorSale.fragment.Tab2;
import com.hnweb.clawpal.BuyorSale.fragment.Tab3;
import com.hnweb.clawpal.lostorfound.pet.fragment.TabOne;
import com.hnweb.clawpal.lostorfound.pet.fragment.TabThree;
import com.hnweb.clawpal.lostorfound.pet.fragment.TabTwo;

/**
 * Created by Shree on 31-Jan-18.
 */

public class ViewPagerSwipeAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ViewPagerSwipeAdapter(FragmentManager fm, int NumOfTabs, String gujug) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabOne tab1 = new TabOne();
                return tab1;
            case 1:
                TabTwo tab2 = new TabTwo();
                return tab2;
            case 2:
                TabThree tab3 = new TabThree();
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