package com.hnweb.clawpal.BuyorSale.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
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
import com.hnweb.clawpal.adaption.activity.AdoptPetDetails;
import com.hnweb.clawpal.adaption.adaptor.AnimalPicAdaptor;
import com.hnweb.clawpal.login.LoginActivity;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HNWeb-11 on 7/23/2016.
 */
public class PetDetails extends AppCompatActivity {
    All_Pets_List_Model petObject;
    ImageView mIvPetImage, iv_fav;
    SharedPreferences prefs;
    SharedPreferences pref;
    TabHost tabHost;
    int mFirst=0, mLast=0;
    TextView mTvPetDisc, mTvTypeOfPet, mTvGender, mTvAge, mTvBreed, mTvNeutered, mTvVaccinated, mTvPrice, mTvContact_person, mTvEmail,mTvNumber, mTvCity, mTvAddress, tv_postedOn;
    LinearLayout mLlContactDetails;
    Toolbar toolbar;
    List<String> images = new ArrayList<>();
    ImageView iv_right,iv_left;
    TabHost host;
    WebView webView;
    String isClick = "1";
    String userID, petId;

    ProgressDialog progressDialog;
    RecyclerView recycler_view_petImages;
    ImageView iv_noImage;
    AnimalPicAdaptor animalPicAdaptor;
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_pet_details);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        userID = pref.getString("userid", null);
        progressDialog = new ProgressDialog(PetDetails.this);
        progressDialog.setMessage("Please wait...");
        iv_noImage=(ImageView) findViewById(R.id.iv_dog) ;
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        recycler_view_petImages = (RecyclerView) findViewById(R.id.recycler_view_petImages);
        iv_right=(ImageView) findViewById(R.id.iv_right);
        iv_left=(ImageView) findViewById(R.id.iv_left);

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
                petObject = (All_Pets_List_Model) bundleObject.getSerializable("petObject");

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("USERID" + userID);
            System.out.println("PETID" + petObject.getPet_sale_buy_id());
            petId = petObject.getPet_sale_buy_id();

            if (userID != null) {
                iv_fav.setVisibility(View.VISIBLE);
                isfav();
            } else {
                iv_fav.setVisibility(View.GONE);
            }

            images = Arrays.asList(petObject.getImage().replaceAll("\\s", "").split(","));

            if(images.size()==0)
            {

                recycler_view_petImages.setVisibility(View.GONE);
                iv_noImage.setVisibility(View.VISIBLE);
                iv_noImage.setImageResource(R.drawable.no_image);
            }else
            {
                recycler_view_petImages.setVisibility(View.VISIBLE);
                iv_noImage.setVisibility(View.GONE);
                animalPicAdaptor = new AnimalPicAdaptor(PetDetails.this, images);
                LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(PetDetails.this, LinearLayoutManager.HORIZONTAL, false);
                recycler_view_petImages.setLayoutManager(horizontalLayoutManagaer);
                recycler_view_petImages.setAdapter(animalPicAdaptor);
            }



           /* UrlImageViewHelper.setUrlDrawable(mIvPetImage, petObject.getImage(), R.drawable.loading,
                    new UrlImageViewCallback() {
                        @Override
                        public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                             boolean loadedFromCache) {
                        }
                    });
*/
            host = (TabHost) findViewById(R.id.tabHost);
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

                                            Intent intent = new Intent(PetDetails.this, LoginActivity.class);
                                            intent.putExtra("key", "PetDetails");
                                            Bundle bundleObject = new Bundle();

                                            bundleObject.putSerializable("petObject", petObject);
                                            intent.putExtras(bundleObject);
                                            startActivity(intent);
                                            finish();


                                        case DialogInterface.BUTTON_NEGATIVE:

                                            // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(PetDetails.this);
                            builder.setTitle("Message");
                            builder.setMessage("Please Login to view Contact Details").setPositiveButton("Login", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

                        }

                    }
                }
            });

            setData();



/*
            recycler_view_petImages.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager llm = (LinearLayoutManager) recycler_view_petImages.getLayoutManager();
                    mLast = llm.findLastCompletelyVisibleItemPosition();
                    mFirst = llm.findFirstCompletelyVisibleItemPosition();
                }
            });

            iv_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayoutManager llm = (LinearLayoutManager) recycler_view_petImages.getLayoutManager();
                    llm.scrollToPositionWithOffset(mLast + 1, images.size());
                }
            });

            iv_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayoutManager llm = (LinearLayoutManager) recycler_view_petImages.getLayoutManager();
                    llm.scrollToPositionWithOffset(mFirst - 1, images.size());
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void getInit() {
        iv_fav = (ImageView) findViewById(R.id.iv_fav);

        webView = (WebView) findViewById(R.id.webview);
        mIvPetImage = (ImageView) findViewById(R.id.pet_image);
        mTvPetDisc = (TextView) findViewById(R.id.test);
        mTvTypeOfPet = (TextView) findViewById(R.id.tv_type_pet);
        mTvGender = (TextView) findViewById(R.id.tv_gender);
        mTvAge = (TextView) findViewById(R.id.tv_age);
        mTvBreed = (TextView) findViewById(R.id.tv_breed);
        mTvNeutered = (TextView) findViewById(R.id.tv_natuered);
        mTvVaccinated = (TextView) findViewById(R.id.tv_vaccinated);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        tv_postedOn = (TextView) findViewById(R.id.tv_postedOn);
        mTvContact_person = (TextView) findViewById(R.id.tv_cont_person);
        mTvEmail=(TextView) findViewById(R.id.tv_email);
        mTvNumber = (TextView) findViewById(R.id.tv_number);
        mTvCity = (TextView) findViewById(R.id.tv_city);
        mTvAddress = (TextView) findViewById(R.id.tv_add);
        mLlContactDetails = (LinearLayout) findViewById(R.id.ll_contact_details);
        mLlContactDetails.setVisibility(View.GONE);


    }

    public void setData() {

        if(petObject.getDescription().equals(""))
        {
            if(petObject.getAnimal().equals("Dog") || petObject.getAnimal().equals("puppy"))
            {
                String text = "<html><body>"
                        + "<p align=\"justify\">"
                        + "\n" +
                        "I have cute little puppy available for sale, if interested contact me as soon as possible."
                        + "</p> "
                        + "</body></html>";

                webView.loadData(text, "text/html", "utf-8");
            }
            else
            {
                String text = "<html><body>"
                        + "<p align=\"justify\">"
                        + "\n" +
                        "I have cute little Cat available for sale, if interested contact me as soon as possible."
                        + "</p> "
                        + "</body></html>";

                webView.loadData(text, "text/html", "utf-8");
            }

        }
        else
        {
            String text = "<html><body>"
                    + "<p align=\"justify\">"
                    + petObject.getDescription().toString()
                    + "</p> "
                    + "</body></html>";

            webView.loadData(text, "text/html", "utf-8");
        }

        // mTvPetDisc.setText(petObject.getDescription().toString());
        mTvTypeOfPet.setText(petObject.getAnimal());
        mTvGender.setText(petObject.getAge_range());
        mTvAge.setText(petObject.getGender());
        mTvBreed.setText(petObject.getBreed_type());
        mTvNeutered.setText(petObject.getNeutered());
        mTvVaccinated.setText(petObject.getVaccinated());



        System.out.println("Price..."+petObject.getPrice_range());
        if(petObject.getCurrency().equals(""))
        {
            mTvPrice.setText("N/A");
        }else
        {
            mTvPrice.setText(petObject.getCurrency());
        }

        String youString;
        int i = petObject.getDate().indexOf(" ");
        youString = petObject.getDate().substring(0, i);
        tv_postedOn.setText(youString);

        mTvContact_person.setText(petObject.getName());
        mTvNumber.setText(petObject.getContact());
        mTvCity.setText(petObject.getCity());
        mTvAddress.setText(petObject.getLocality());
        mTvEmail.setText(petObject.getEmail());


    }

    private void addFav() {

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebURL.ADD_BUY_SALE_FAV + "user_id=" + userID + "&pet_buy_sale_id=" + petId,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        System.out.println("resadd " + response);

                        progressDialog.dismiss();
                        try {
                            JSONObject j = new JSONObject(response);

                            System.out.println("resadd" + response.toString() + response.toString());
                            String res = j.getString("response");
                            JSONObject jsonObject = new JSONObject(res);
                            String message = jsonObject.getString("msg");
                            int flag;
                            flag = jsonObject.getInt("flag");
                            System.out.println("res123" + res.toString());

                            if (flag == 1) {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(PetDetails.this);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebURL.REMOVE_BUY_SALE_FAV + "user_id=" + userID + "&pet_buy_sale_id=" + petId,
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
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(PetDetails.this);
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

        String url=WebURL.IS_BUYSALEPET_FAV + "user_id=" + userID + "&pet_buy_sale_id=" + petId;
        System.out.println("URLfav"+url);

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
