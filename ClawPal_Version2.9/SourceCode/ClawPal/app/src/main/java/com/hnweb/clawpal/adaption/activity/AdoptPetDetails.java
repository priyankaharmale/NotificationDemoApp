package com.hnweb.clawpal.adaption.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.adaption.adaptor.AnimalPicAdaptor;
import com.hnweb.clawpal.adaption.model.AdoptPetModel;
import com.hnweb.clawpal.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HNWeb-11 on 8/3/2016.
 */
public class AdoptPetDetails extends AppCompatActivity {
    AdoptPetModel petObject;
    ImageView mIvPetImage;
    SharedPreferences prefs;
    Button earn_coins_button, get_views_button, free_coins_button, upload_ig_button;
    SharedPreferences pref;
    TabHost tabHost;
    TextView mTvPetDisc, tv_email,tv_postedOn, mTvTypeOfPet, mTvGender, mTvAge, mTvBreed, mTvNeutered, mTvVaccinated, mTvPrice, mTvContact_person, mTvNumber, mTvCity, mTvAddress;
    TextView mTvLocation, mTvState;
    LinearLayout mLlContactDetails;
    Toolbar toolbar;
    ImageView iv_fav;
    String user_id;
    WebView webView;
    String isClick = "1";
    String userID, petId;
    ProgressDialog progressDialog;
    List<String> images = new ArrayList<>();
    RecyclerView recycler_view_petImages;
    AnimalPicAdaptor animalPicAdaptor;

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_adopt_pet_details);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        userID = pref.getString("userid", null);
        progressDialog = new ProgressDialog(AdoptPetDetails.this);
        progressDialog.setMessage("Please wait...");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        recycler_view_petImages = (RecyclerView) findViewById(R.id.recycler_view_petImages);

        try {
            // Get the Bundle Object
            Bundle bundleObject = getIntent().getExtras();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.drawable.back_btn_img);
            toolbar.setNavigationOnClickListener(

                    new View.OnClickListener() {
                        @Override

                        public void onClick(View v) {

                            finish();
                        }

                    }

            );
            getInit();

            iv_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClick.equals("1")) {
                        iv_fav.setImageResource(R.drawable.ic_favoritefill);
                        isClick = "0";
                        addFav();
                    } else {
                        iv_fav.setImageResource(R.drawable.favorite_white);
                        isClick = "1";
                        remove();
                    }
                }
            });
            try {
                // Get ArrayList Bundle
                petObject = (AdoptPetModel) bundleObject.getSerializable("adoptPetModel");

            } catch (Exception e) {
                e.printStackTrace();
            }
            petId = petObject.getPet_adoption_id();
            if (userID != null) {
                iv_fav.setVisibility(View.VISIBLE);
                isfav();
            } else {
                iv_fav.setVisibility(View.GONE);
            }
            images = Arrays.asList(petObject.getPet_adoption_photo().replaceAll("\\s", "").split(","));


            animalPicAdaptor = new AnimalPicAdaptor(AdoptPetDetails.this, images);

            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(AdoptPetDetails.this, LinearLayoutManager.HORIZONTAL, false);
            recycler_view_petImages.setLayoutManager(horizontalLayoutManagaer);
            recycler_view_petImages.setAdapter(animalPicAdaptor);
      /*      UrlImageViewHelper.setUrlDrawable(mIvPetImage, petObject.getPet_adoption_photo(), R.drawable.loading,
                    new UrlImageViewCallback() {
                        @Override
                        public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                             boolean loadedFromCache) {
                        }
                    });*/

            final TabHost host = (TabHost) findViewById(R.id.tabHost);
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

                                            Intent intent = new Intent(AdoptPetDetails.this, LoginActivity.class);
                                            intent.putExtra("key", "AdoptPetDetails");
                                            Bundle bundleObject = new Bundle();

                                            bundleObject.putSerializable("adoptPetModel", petObject);
                                            intent.putExtras(bundleObject);
                                            startActivity(intent);
                                            finish();


                                        case DialogInterface.BUTTON_NEGATIVE:

                                            // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(AdoptPetDetails.this);
                            builder.setTitle("Message");
                            builder.setMessage("Please Login to view Contact Details").setPositiveButton("Login", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

                        }

                    }
                }
            });

            setData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getInit() {

        iv_fav = (ImageView) findViewById(R.id.iv_fav);
        webView = (WebView) findViewById(R.id.webview);
        mIvPetImage = (ImageView) findViewById(R.id.pet_image);
        mTvPetDisc = (TextView) findViewById(R.id.test);
        tv_email=(TextView) findViewById(R.id.tv_email);
        mTvTypeOfPet = (TextView) findViewById(R.id.tv_type_pet);
        mTvGender = (TextView) findViewById(R.id.tv_gender);
        mTvAge = (TextView) findViewById(R.id.tv_age);
        mTvBreed = (TextView) findViewById(R.id.tv_breed);
        mTvNeutered = (TextView) findViewById(R.id.tv_natuered);
        mTvVaccinated = (TextView) findViewById(R.id.tv_vaccinated);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        mTvContact_person = (TextView) findViewById(R.id.tv_cont_person);
        mTvNumber = (TextView) findViewById(R.id.tv_number);
        mTvCity = (TextView) findViewById(R.id.tv_city);
        mTvAddress = (TextView) findViewById(R.id.tv_add);
        mLlContactDetails = (LinearLayout) findViewById(R.id.ll_contact_details);
        mLlContactDetails.setVisibility(View.GONE);
        mTvLocation = (TextView) findViewById(R.id.tv_Location);
        mTvState = (TextView) findViewById(R.id.tv_state);
        tv_postedOn = (TextView) findViewById(R.id.tv_postedOn);
       /* earn_coins_button = (Button) findViewById(R.id.option_activity_tab_button_earn_coins);
        get_views_button = (Button) findViewById(R.id.option_activity_tab_button_get_views);
        free_coins_button = (Button) findViewById(R.id.option_activity_tab_button_free_coins);*/

    }

    public void setData() {
        String text = "<html><body>"
                + "<p align=\"justify\">"
                + petObject.getShort_description().toString()
                + "</p> "
                + "</body></html>";

        webView.loadData(text, "text/html", "utf-8");
        // mTvPetDisc.setText(petObject.getShort_description().toString());
        mTvTypeOfPet.setText(petObject.getType_of_animal());
        mTvGender.setText(petObject.getAge_range());
        mTvAge.setText(petObject.getGender());
        mTvBreed.setText(petObject.getBreed_type());
        mTvNeutered.setText(petObject.getNeutered());
        mTvVaccinated.setText(petObject.getVaccinated());
        mTvPrice.setText("-");
        mTvContact_person.setText("-");
        String youString;
        int i = petObject.getAdaptionDate().indexOf(" ");
        youString = petObject.getAdaptionDate().substring(0, i);
        tv_postedOn.setText(youString);
        mTvContact_person.setVisibility(View.GONE);
        mTvNumber.setText(petObject.getContact());
        mTvCity.setText(petObject.getState());
        mTvAddress.setText(petObject.getLocation());
        mTvLocation.setText(petObject.getLocation());
        mTvState.setText(petObject.getState());
        if(petObject.getEmail().equals(""))
        {
            tv_email.setText("N/A");
        }
        else
        {
            tv_email.setText(petObject.getEmail());

        }
    }

    private void addFav() {

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebURL.ADD_ADAPTION_FAV + "user_id=" + userID + "&pet_adaption_id=" + petId,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        System.out.println("= " + response);

                        progressDialog.dismiss();
                        try {
                            JSONObject j = new JSONObject(response);

                            System.out.println("resArsh" + response.toString() + response.toString());
                            String res = j.getString("response");
                            JSONObject jsonObject = new JSONObject(res);
                            String message = jsonObject.getString("msg");
                            int flag;
                            flag = jsonObject.getInt("flag");
                            System.out.println("res123" + res.toString());

                            if (flag == 1) {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AdoptPetDetails.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {


                                            }
                                        });
                                android.support.v7.app.AlertDialog alert = builder.create();
                                alert.show();
                            }

                        } catch (JSONException e) {
                            System.out.println("jsonexeption1111" + e.toString());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void remove() {

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebURL.REMOVE_ADAPTION_FAV + "user_id=" + userID + "&pet_adaption_id=" + petId,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        System.out.println("resremove " + response);

                        progressDialog.dismiss();
                        try {
                            JSONObject j = new JSONObject(response);

                            System.out.println("resArshremove" + response.toString() + response.toString());
                            String res = j.getString("response");
                            int flag;
                            flag = j.getInt("flag");
                            System.out.println("res123" + res.toString());

                            if (flag == 1) {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AdoptPetDetails.this);
                                builder.setMessage(res)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                android.support.v7.app.AlertDialog alert = builder.create();
                                alert.show();
                            }

                        } catch (JSONException e) {
                            System.out.println("jsonexeption1111" + e.toString());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void isfav() {

        String url = WebURL.IS_ADAPTIONPET_FAV + "user_id=" + userID + "&pet_adoption_id=" + petId;
        System.out.println("URLfav" + url);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        System.out.println("resfav " + response);

                        progressDialog.dismiss();
                        try {
                            JSONObject j = new JSONObject(response);

                            System.out.println("resfav" + response.toString() + response.toString());
                            String res = j.getString("response");
                            int flag;
                            flag = j.getInt("flag");

                            if (flag == 1) {
                                iv_fav.setImageResource(R.drawable.ic_favoritefill);
                                isClick = "0";
                            } else {
                                iv_fav.setImageResource(R.drawable.favorite_white);
                                isClick = "1";

                            }

                        } catch (JSONException e) {
                            System.out.println("jsonexeption1111" + e.toString());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
