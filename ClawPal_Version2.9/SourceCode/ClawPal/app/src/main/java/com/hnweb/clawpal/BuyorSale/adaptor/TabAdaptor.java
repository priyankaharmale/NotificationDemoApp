package com.hnweb.clawpal.BuyorSale.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.login.LoginActivity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

/**
 * Created by Shree on 30-Jan-18.
 */

public class TabAdaptor extends ArrayAdapter<All_Pets_List_Model> {


    final LayoutInflater mInflater;
    public ArrayList<All_Pets_List_Model> allPetsListModels;
    public Context context;
    String userID, isClick = "1";
    SharedPreferences pref;
    All_Pets_List_Model petObj;

    public TabAdaptor(Context context, ArrayList<All_Pets_List_Model> apps, All_Pets_List_Model petObj) {
        super(context, R.layout.tab, apps);
        this.allPetsListModels = apps;
        this.context = context;
        this.petObj = petObj;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return allPetsListModels.size();
    }

    public All_Pets_List_Model getItem(int position) {
        return allPetsListModels.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        View view = convertView;
        All_Pets_List_Model petObject = allPetsListModels.get(position);


        if (view == null) {
            //viewHolder.background = (FrameLayout) view.findViewById(R.id.background);
            viewHolder = new TabAdaptor.ViewHolder();
            view = mInflater.inflate(R.layout.tab, parent, false);
            viewHolder.host = (TabHost) view.findViewById(R.id.tabHost);
            viewHolder.host.setup();
            //Tab 1
            TabHost.TabSpec spec =  viewHolder.host.newTabSpec("Tab One");
            spec.setContent(R.id.tab1);
            spec.setIndicator("Tab One");
            viewHolder.host.addTab(spec);

            //Tab 2
            spec =  viewHolder.host.newTabSpec("Tab Two");
            spec.setContent(R.id.tab2);
            spec.setIndicator("Tab Two");
            viewHolder.host.addTab(spec);

            //Tab 3
            spec =  viewHolder.host.newTabSpec("Tab Three");
            spec.setContent(R.id.tab3);
            spec.setIndicator("Tab Three");
            viewHolder.host.addTab(spec);
            //Tab 1
           /* TabHost.TabSpec spec = viewHolder.host.newTabSpec("Tab One");
            spec.setContent(R.id.tab1);
            spec.setIndicator("About");
            viewHolder.host.addTab(spec);

            //Tab 2
            spec = viewHolder.host.newTabSpec("Tab Two");
            spec.setContent(R.id.tab2);
            spec.setIndicator("Details");
            viewHolder.host.addTab(spec);

            //Tab 3
            spec = viewHolder.host.newTabSpec("Tab Three");
            spec.setContent(R.id.tab3);
            spec.setIndicator("Contact");
            viewHolder.host.addTab(spec);*/


            view.setTag(viewHolder);

        } else {
            viewHolder = (TabAdaptor.ViewHolder) view.getTag();
        }




        return view;
    }

    public static class ViewHolder {

        All_Pets_List_Model petObject = null;
        TextView mTvPetDisc, mTvTypeOfPet, mTvGender, mTvAge, mTvBreed, mTvNeutered, mTvVaccinated, mTvPrice, mTvContact_person, mTvNumber, mTvCity, mTvAddress, tv_postedOn;
        ImageView mIvPetImage, iv_fav;
        WebView webView;
        LinearLayout mLlContactDetails;
        TabHost host;

    }
}
