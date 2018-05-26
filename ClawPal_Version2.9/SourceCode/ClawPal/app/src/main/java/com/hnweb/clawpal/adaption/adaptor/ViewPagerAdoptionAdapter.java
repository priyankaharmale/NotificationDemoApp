package com.hnweb.clawpal.adaption.adaptor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AlertDialog;

import com.hnweb.clawpal.BuyorSale.fragment.Tab1;
import com.hnweb.clawpal.BuyorSale.fragment.Tab2;
import com.hnweb.clawpal.BuyorSale.fragment.Tab3;
import com.hnweb.clawpal.adaption.activity.AdoptPetDetails;
import com.hnweb.clawpal.adaption.fragment.Tab1Adoption;
import com.hnweb.clawpal.adaption.fragment.Tab2Adoption;
import com.hnweb.clawpal.adaption.fragment.Tab3Adoption;
import com.hnweb.clawpal.login.LoginActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Shree on 31-Jan-18.
 */

public class ViewPagerAdoptionAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    SharedPreferences sharedPrefs;
    Context context;
    String userID;

    public ViewPagerAdoptionAdapter(FragmentManager fm, int NumOfTabs, Context context) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
      this.context=context;
    }

    @Override
    public Fragment getItem(int position) {

        sharedPrefs = context.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        userID = sharedPrefs.getString("userid", null);

        switch (position) {
            case 0:
                Tab1Adoption tab1 = new Tab1Adoption();
                return tab1;
            case 1:
                Tab2Adoption tab2 = new Tab2Adoption();
                return tab2;
            case 2:
              /*  if(userID!=null)
                {*/
                    Tab3Adoption tab3 = new Tab3Adoption();
                    return tab3;
            /*    }
            else
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:

                                    Intent intent = new Intent(context, LoginActivity.class);
                                    intent.putExtra("key", "AdoptPetDetails");
                                    Bundle bundleObject = new Bundle();

                                  //  bundleObject.putSerializable("adoptPetModel", petObject);
                                    intent.putExtras(bundleObject);
                                    context.startActivity(intent);
                                    //finish();


                                case DialogInterface.BUTTON_NEGATIVE:

                                    // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Message");
                    builder.setMessage("Please Login to view Contact Details").setPositiveButton("Login", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                }*/

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


}