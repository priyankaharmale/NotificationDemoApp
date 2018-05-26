package com.hnweb.clawpal.BuyorSale.adaptor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
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

import com.hnweb.clawpal.login.LoginActivity;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

/**
 * Created by Shree on 05-Dec-17.
 */

public class MyAppAdapter extends ArrayAdapter<All_Pets_List_Model> {


    final LayoutInflater mInflater;
    public ArrayList<All_Pets_List_Model> allPetsListModels;
    public Context context;
    String userID, isClick = "1";
    SharedPreferences pref;
    All_Pets_List_Model petObj;

    public MyAppAdapter(Context context, ArrayList<All_Pets_List_Model> apps, All_Pets_List_Model petObj) {
        super(context, R.layout.activity_petdetailss, apps);
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
            viewHolder = new MyAppAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.activity_petdetailss, parent, false);

            pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
            userID = pref.getString("userid", null);
            viewHolder.iv_fav = (ImageView) view.findViewById(R.id.iv_fav);
            viewHolder.mIvPetImage = (ImageView) view.findViewById(R.id.pet_image);
            viewHolder.mTvPetDisc = (TextView) view.findViewById(R.id.test);
            viewHolder.mTvTypeOfPet = (TextView) view.findViewById(R.id.tv_type_pet);
            viewHolder.mTvGender = (TextView) view.findViewById(R.id.tv_gender);

            viewHolder.mTvAge = (TextView) view.findViewById(R.id.tv_age);
            viewHolder.mTvBreed = (TextView) view.findViewById(R.id.tv_breed);
            viewHolder.mTvNeutered = (TextView) view.findViewById(R.id.tv_natuered);
            viewHolder.mTvVaccinated = (TextView) view.findViewById(R.id.tv_vaccinated);
            viewHolder.mTvPrice = (TextView) view.findViewById(R.id.tv_price);
            viewHolder.tv_postedOn = (TextView) view.findViewById(R.id.tv_postedOn);
            viewHolder.mTvContact_person = (TextView) view.findViewById(R.id.tv_cont_person);
            viewHolder.mTvNumber = (TextView) view.findViewById(R.id.tv_number);
            viewHolder.mTvCity = (TextView) view.findViewById(R.id.tv_city);
            viewHolder.mTvAddress = (TextView) view.findViewById(R.id.tv_add);
            viewHolder.host = (TabHost) view.findViewById(R.id.tabHost);
            viewHolder.webView = (WebView) view.findViewById(R.id.webview);
            viewHolder.mLlContactDetails = (LinearLayout) view.findViewById(R.id.ll_contact_details);
            viewHolder.mLlContactDetails.setVisibility(View.GONE);
            view.setTag(viewHolder);

        } else {
            viewHolder = (MyAppAdapter.ViewHolder) view.getTag();
        }


        if (userID != null) {
            viewHolder.iv_fav.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_fav.setVisibility(View.GONE);
        }


        viewHolder.host = (TabHost) view.findViewById(R.id.tabHost);
        viewHolder.host.setup();
        //Tab 1
        TabHost.TabSpec spec = viewHolder.host.newTabSpec("Tab One");
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
        viewHolder.host.addTab(spec);


        viewHolder.host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("Tab Three")) {

                    if (userID != null) {
                        viewHolder.mLlContactDetails.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.host.setCurrentTab(1);
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

                                        Intent intent = new Intent(context, LoginActivity.class);
                                        intent.putExtra("key", "PetDetails");
                                        context.startActivity(intent);
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Message");
                        builder.setMessage("Please Login to view Contact Details").setPositiveButton("Login", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

                    }

                }
            }
        });

        viewHolder.iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick.equals("1")) {
                    Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                    viewHolder.iv_fav.setImageResource(R.drawable.ic_favoritefill);
                    isClick = "0";
                } else {
                    Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                    viewHolder.iv_fav.setImageResource(R.drawable.hearticon_black);
                    isClick = "1";
                }
            }
        });

        for (All_Pets_List_Model all_pets_list_model : allPetsListModels) {


            if (petObj.getPet_sale_buy_id().equals(all_pets_list_model.getPet_sale_buy_id()))

            {
                UrlImageViewHelper.setUrlDrawable(viewHolder.mIvPetImage, petObj.getImage(), R.drawable.loading,
                        new UrlImageViewCallback() {
                            @Override
                            public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                                 boolean loadedFromCache) {
                            }
                        });

                String text = "<html><body>"
                        + "<p align=\"justify\">"
                        + petObj.getDescription().toString()
                        + "</p> "
                        + "</body></html>";

                viewHolder.webView.loadData(text, "text/html", "utf-8");

                viewHolder.mTvPetDisc.setText(petObj.getDescription().toString());
                viewHolder.mTvTypeOfPet.setText(petObj.getAnimal());
                viewHolder.mTvGender.setText(petObj.getAge_range());
                viewHolder.mTvAge.setText(petObj.getGender());
                viewHolder.mTvBreed.setText(petObj.getBreed_type());
                viewHolder.mTvNeutered.setText(petObj.getNeutered());
                viewHolder.mTvVaccinated.setText(petObj.getVaccinated());
                viewHolder.mTvPrice.setText(petObj.getPrice_range());

                String youString;
                int i = petObj.getDate().indexOf(" ");
                youString = petObj.getDate().substring(0, i);
                viewHolder.tv_postedOn.setText(youString);

                viewHolder.mTvContact_person.setText(petObj.getName());
                viewHolder.mTvNumber.setText(petObj.getContact());
                viewHolder.mTvCity.setText(petObj.getCity());
                viewHolder.mTvAddress.setText(petObj.getLocality());

            } else {
                UrlImageViewHelper.setUrlDrawable(viewHolder.mIvPetImage, petObject.getImage(), R.drawable.loading,
                        new UrlImageViewCallback() {
                            @Override
                            public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                                 boolean loadedFromCache) {
                            }
                        });

                String text = "<html><body>"
                        + "<p align=\"justify\">"
                        + petObject.getDescription().toString()
                        + "</p> "
                        + "</body></html>";

                viewHolder.webView.loadData(text, "text/html", "utf-8");
                viewHolder.mTvPetDisc.setText(petObject.getDescription().toString());
                viewHolder.mTvTypeOfPet.setText(petObject.getAnimal());
                viewHolder.mTvGender.setText(petObject.getAge_range());
                viewHolder.mTvAge.setText(petObject.getGender());
                viewHolder.mTvBreed.setText(petObject.getBreed_type());
                viewHolder.mTvNeutered.setText(petObject.getNeutered());
                viewHolder.mTvVaccinated.setText(petObject.getVaccinated());
                viewHolder.mTvPrice.setText(petObject.getPrice_range());

                String youString;
                int i = petObject.getDate().indexOf(" ");
                youString = petObject.getDate().substring(0, i);
                viewHolder.tv_postedOn.setText(youString);

                viewHolder.mTvContact_person.setText(petObject.getName());
                viewHolder.mTvNumber.setText(petObject.getContact());
                viewHolder.mTvCity.setText(petObject.getCity());
                viewHolder.mTvAddress.setText(petObject.getLocality());
            }

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
