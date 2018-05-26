package com.hnweb.clawpal.BuyorSale.adaptor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.hnweb.clawpal.login.LoginActivity;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.List;

/**
 * Created by Shree on 04-Dec-17.
 */

public class BuySaleDetailsPetAdaptorRecyle extends RecyclerView.Adapter<BuySaleDetailsPetAdaptorRecyle.ViewHolder> {
    Context context;
    View view;

    SharedPreferences pref;
    private List<All_Pets_List_Model> all_pets_list_model;

    public BuySaleDetailsPetAdaptorRecyle(Context context, List<All_Pets_List_Model> all_pets_list_model) {
        this.all_pets_list_model = all_pets_list_model;
        this.context = context;
    }

    @Override
    public BuySaleDetailsPetAdaptorRecyle.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_petdetailss, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BuySaleDetailsPetAdaptorRecyle.ViewHolder viewHolder, int i) {

        final All_Pets_List_Model all_pets_list_models = all_pets_list_model.get(i);


        viewHolder.setPet(all_pets_list_models);


    }

    @Override
    public int getItemCount() {
        return all_pets_list_model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        All_Pets_List_Model petObject = null;
        TextView mTvPetDisc, mTvTypeOfPet, mTvGender, mTvAge, mTvBreed, mTvNeutered, mTvVaccinated, mTvPrice, mTvContact_person, mTvNumber, mTvCity, mTvAddress, tv_postedOn;
        ImageView mIvPetImage, iv_fav;
        WebView webView;
        LinearLayout mLlContactDetails;
        String isClick = "1";
        String userID;
        TabHost host;

        public ViewHolder(View view) {
            super(view);
            pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
            userID = pref.getString("userid", null);
            iv_fav = (ImageView) view.findViewById(R.id.iv_fav);
            webView = (WebView) view.findViewById(R.id.webview);
            mIvPetImage = (ImageView) view.findViewById(R.id.pet_image);
            mTvPetDisc = (TextView) view.findViewById(R.id.test);
            mTvTypeOfPet = (TextView) view.findViewById(R.id.tv_type_pet);
            mTvGender = (TextView) view.findViewById(R.id.tv_gender);
            mTvAge = (TextView) view.findViewById(R.id.tv_age);
            mTvBreed = (TextView) view.findViewById(R.id.tv_breed);
            mTvNeutered = (TextView) view.findViewById(R.id.tv_natuered);
            mTvVaccinated = (TextView) view.findViewById(R.id.tv_vaccinated);
            mTvPrice = (TextView) view.findViewById(R.id.tv_price);
            tv_postedOn = (TextView) view.findViewById(R.id.tv_postedOn);
            mTvContact_person = (TextView) view.findViewById(R.id.tv_cont_person);
            mTvNumber = (TextView) view.findViewById(R.id.tv_number);
            mTvCity = (TextView) view.findViewById(R.id.tv_city);
            mTvAddress = (TextView) view.findViewById(R.id.tv_add);
            mLlContactDetails = (LinearLayout) view.findViewById(R.id.ll_contact_details);
            mLlContactDetails.setVisibility(View.GONE);

            try {

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = settings.edit();
                    }
                });


                iv_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isClick.equals("1")) {
                            iv_fav.setImageResource(R.drawable.ic_favoritefill);
                            isClick = "0";
                        } else {
                            iv_fav.setImageResource(R.drawable.favorite_white);
                            isClick = "1";
                        }
                    }
                });

                if (userID != null) {
                    iv_fav.setVisibility(View.VISIBLE);
                } else {
                    iv_fav.setVisibility(View.GONE);
                }

                try {
                    // Get ArrayList Bundle

                } catch (Exception e) {
                    e.printStackTrace();
                }


                UrlImageViewHelper.setUrlDrawable(mIvPetImage, petObject.getImage(), R.drawable.loading,
                        new UrlImageViewCallback() {
                            @Override
                            public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                                 boolean loadedFromCache) {
                            }
                        });

                host = (TabHost) view.findViewById(R.id.tabHost);
                host.setup();

                //Tab 1
                TabHost.TabSpec spec = host.newTabSpec("Tab One");
                spec.setContent(R.id.tab1);
                spec.setIndicator("About");
                host.addTab(spec);

                //Tab 2
                spec = host.newTabSpec("Tab Two");
                spec.setContent(R.id.tab2);
                spec.setIndicator("Details");
                host.addTab(spec);

                //Tab 3
                spec = host.newTabSpec("Tab Three");
                spec.setContent(R.id.tab3);
                spec.setIndicator("Contact");
                host.addTab(spec);

                host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                    @Override
                    public void onTabChanged(String tabId) {
                        if (tabId.equals("Tab Three")) {


                            if (userID != null) {
                                mLlContactDetails.setVisibility(View.VISIBLE);
                            } else {
                                host.setCurrentTab(1);
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:

                                                Intent intent = new Intent(context, LoginActivity.class);
                                                intent.putExtra("key", "PetDetails");
                                                context.startActivity(intent);


                                            case DialogInterface.BUTTON_NEGATIVE:

                                                // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
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



                String text = "<html><body>"
                        + "<p align=\"justify\">"
                        + petObject.getDescription().toString()
                        + "</p> "
                        + "</body></html>";

                webView.loadData(text, "text/html", "utf-8");
                // mTvPetDisc.setText(petObject.getDescription().toString());
                mTvTypeOfPet.setText(petObject.getAnimal());
                mTvGender.setText(petObject.getAge_range());
                mTvAge.setText(petObject.getGender());
                mTvBreed.setText(petObject.getBreed_type());
                mTvNeutered.setText(petObject.getNeutered());
                mTvVaccinated.setText(petObject.getVaccinated());
                mTvPrice.setText(petObject.getPrice_range());

                String youString;
                int i = petObject.getDate().indexOf(" ");
                youString = petObject.getDate().substring(0, i);
                tv_postedOn.setText(youString);

                mTvContact_person.setText(petObject.getName());
                mTvNumber.setText(petObject.getContact());
                mTvCity.setText(petObject.getCity());
                mTvAddress.setText(petObject.getLocality());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void setPet(All_Pets_List_Model all_pets_list_model) {
            this.petObject = all_pets_list_model;
        }


    }


}
