package com.hnweb.clawpal.BuyorSale.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.adaption.adaptor.AnimalPicAdaptor;
import com.hnweb.clawpal.login.LoginActivity;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hnweb.clawpal.R;

/**
 * Created by Shree on 22-Jan-18.
 */

public class BuySaleDemoActivity extends AppCompatActivity {
    public static MyAppAdapter myAppAdapter;
    public static ViewHolder viewHolder;
    private SwipeFlingAdapterView flingContainer;
    List<String> images = new ArrayList<>();
    All_Pets_List_Model petObj;
    int position1;
    private ArrayList<All_Pets_List_Model> all_pets_list_models;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipelayout);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);


        if (getIntent().hasExtra("arraylist")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null)
                all_pets_list_models = (ArrayList<All_Pets_List_Model>) bundle.getSerializable("arraylist");
            else
                Log.e("null", "null");
        }
        if (getIntent().hasExtra("petobject")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null)
                petObj = (All_Pets_List_Model) bundle.getSerializable("petobject");
            else
                Log.e("null", "null");
        }
        if (getIntent().hasExtra("position")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                position1 = bundle.getInt("position");
            }
        }

        myAppAdapter = new MyAppAdapter(all_pets_list_models, BuySaleDemoActivity.this);
        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                all_pets_list_models.remove(0);
                myAppAdapter.notifyDataSetChanged();
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                all_pets_list_models.remove(0);
                myAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);

                myAppAdapter.notifyDataSetChanged()                           ;
            }
        });
    }

    public static class ViewHolder {
        All_Pets_List_Model petObject = null;
        TextView mTvPetDisc, mTvTypeOfPet, mTvGender, mTvAge, mTvBreed, mTvNeutered, mTvVaccinated, mTvPrice, mTvContact_person, mTvNumber, mTvCity, mTvAddress, tv_postedOn;
        ImageView mIvPetImage, iv_fav;
        WebView webView;
        RecyclerView recycler_view_petImages;
        LinearLayout mLlContactDetails;
        TabHost host;

    }

    public class MyAppAdapter extends BaseAdapter {


        public ArrayList<All_Pets_List_Model> allPetsListModels;
        public Context context;
        String userID, isClick = "1";
        SharedPreferences pref;

        private MyAppAdapter(ArrayList<All_Pets_List_Model> apps, Context context) {
            this.allPetsListModels = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return allPetsListModels.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = convertView;
            All_Pets_List_Model petObject = allPetsListModels.get(position);

            if (view == null) {

                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.activity_petdetailss, parent, false);
                viewHolder = new ViewHolder();
                //viewHolder.background = (FrameLayout) view.findViewById(R.id.background);
                pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
                userID = pref.getString("userid", null);
                viewHolder.host = (TabHost) view.findViewById(R.id.tabHost);

                viewHolder.iv_fav = (ImageView) view.findViewById(R.id.iv_fav);
                viewHolder.mIvPetImage = (ImageView) view.findViewById(R.id.pet_image);
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
                viewHolder.mTvPetDisc = (TextView) view.findViewById(R.id.test);

                viewHolder.webView = (WebView) view.findViewById(R.id.webview);
                viewHolder.mLlContactDetails = (LinearLayout) view.findViewById(R.id.ll_contact_details);
                viewHolder.recycler_view_petImages = (RecyclerView) findViewById(R.id.recycler_view_petImages);
                viewHolder.mLlContactDetails.setVisibility(View.GONE);
                setTabsView();

                try {

                    if (userID != null) {
                        viewHolder.iv_fav.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.iv_fav.setVisibility(View.GONE);
                    }

                    images = Arrays.asList(petObject.getImage().replaceAll("\\s", "").split(","));

                   /* if(images.size()==0)
                    {

                        viewHolder.recycler_view_petImages.setVisibility(View.GONE);
                    *//*iv_noImage.setVisibility(View.VISIBLE);
                    iv_noImage.setImageResource(R.drawable.no_image);*//*
                    }else
                    {
                        viewHolder.recycler_view_petImages.setVisibility(View.VISIBLE);
                        //iv_noImage.setVisibility(View.GONE);
                        animalPicAdaptor = new AnimalPicAdaptor(BuySaleDemoActivity.this, images);
                        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(BuySaleDemoActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        viewHolder.recycler_view_petImages.setLayoutManager(horizontalLayoutManagaer);
                        viewHolder.recycler_view_petImages.setAdapter(animalPicAdaptor);
                    }
*/
                    for (All_Pets_List_Model all_pets_list_model : allPetsListModels) {


                        if (petObj.getPet_sale_buy_id().equals(all_pets_list_model.getPet_sale_buy_id()))

                        {


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
                       /* UrlImageViewHelper.setUrlDrawable(viewHolder.mIvPetImage, petObject.getImage(), R.drawable.loading,
                                new UrlImageViewCallback() {
                                    @Override
                                    public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                                         boolean loadedFromCache) {
                                    }
                                });*/

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

                    viewHolder.iv_fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isClick.equals("1")) {
                                Toast.makeText(BuySaleDemoActivity.this, "1", Toast.LENGTH_SHORT).show();
                                viewHolder.iv_fav.setImageResource(R.drawable.ic_favoritefill);
                                isClick = "0";
                            } else {
                                Toast.makeText(BuySaleDemoActivity.this, "2", Toast.LENGTH_SHORT).show();
                                viewHolder.iv_fav.setImageResource(R.drawable.hearticon_black);
                                isClick = "1";
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                view.setTag(viewHolder);

            } else {
                viewHolder = (BuySaleDemoActivity.ViewHolder) convertView.getTag();
            }


            return view;
        }

        private void setvalues() {
        }

        public void setTabsView() {


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


            final TabWidget tw = (TabWidget) viewHolder.host.findViewById(android.R.id.tabs);
            for (int i = 0; i < tw.getChildCount(); ++i) {
                final View tabView = tw.getChildTabViewAt(i);
                final TextView tv = (TextView) tabView.findViewById(android.R.id.title);
                tv.setTextSize(12);
                //tv.setTextColor(R.color.darkgray);
                tv.setAllCaps(false);

            }


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



        }
    }
}
