package com.hnweb.clawpal.productbuysale.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hnweb.clawpal.AppController;
import com.hnweb.clawpal.BuyorSale.activity.BuyOrSalePetsActivity;
import com.hnweb.clawpal.BuyorSale.activity.MyPetListBuyorSaleActivity;
import com.hnweb.clawpal.BuyorSale.activity.PetDetails;
import com.hnweb.clawpal.BuyorSale.adaptor.Pet_list_Adaptor;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.DashboardActivity;
import com.hnweb.clawpal.GPSTracker;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.Utils.RowItem;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.location.LocationSet;
import com.hnweb.clawpal.location.MyLocationListener;
import com.hnweb.clawpal.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by HNWeb-11 on 7/22/2016.
 */

public class ProductListDemoActivity extends LocationSet implements LocationListener,TextWatcher {
    private static final int ITEMS_PER_REQUEST = 50;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    ImageView mIvBuySale, mIvAdoption, mIvLostFound;
    Toolbar toolbar;
    TextView mTvTitle;
    String city;
    Intent intent;
    int counttoatal=0;
    int counttotalType=0;
    int counttotalAnimal=0;
    int counttotalKeyword=0;

    ProgressDialog progress;
    Button mBtnBuyOrSale;
    ListView eLView;
    int flag = 1;
    MyLocationListener myLocationListener;
    double latitude = 0.0d;
    double longitude = 0.0d;
    EditText mTvSearch;
    ArrayList<All_Pets_List_Model> pets_list;
    Pet_list_Adaptor pet_list_adaptor;
    boolean isconnected = false;
    String locationAddress;
    GPSTracker gpsTracker;
    ImageView iv_search, iv_dog, iv_cat;
    SharedPreferences pref;
    Double lat, logit;
    ListView listView2;
    ArrayList<All_Pets_List_Model> myList;
    List<RowItem> rowItems;
    String title;

    Button loadmore;
    String search_type = "";
    //it will tell us weather to load more items or not
    boolean loadingMore = false;
    LinearLayout linearLayout, ll_top2, ll_top23;
    String userID;
    ImageView iv_setting;
    private int mult = 1;
    private RadioGroup radioGroup, radioGroup2, radioGroup23;
    // GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {

            //Loop through the new items and add them to the adapter
            if (pets_list != null && pets_list.size() > 0) {

                for (int i = 0; i < pets_list.size(); i++)
                    pet_list_adaptor.add(pets_list.get(i));
            }

            //Update the Application title
            setTitle("Never ending List with " + String.valueOf(pet_list_adaptor.getCount()) + " items");

            //Tell to the adapter that changes have been made, this will cause the list to refresh
            pet_list_adaptor.notifyDataSetChanged();

            //Done loading more.
            loadingMore = false;
        }

    };
    private Runnable loadMoreListItems = new Runnable() {

        @Override
        public void run() {

            //Set flag so we cant load new items 2 at the same time
            loadingMore = true;

            //Reset the array that holds the new items
            pets_list = new ArrayList<>();
            //Simulate a delay, delete this on a production environment!
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
            }

            //Get 10 new list items
            for (int i = 0; i < 10; i++) {

                //Fill the item with some bogus information
                pets_list.add(pet_list_adaptor.getItem(i));
            }

            //Done! now continue on the UI thread
            runOnUiThread(returnRes);
        }
    };

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        try {
            setContentView(R.layout.activity_pet_list);
            getInit();
            pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            userID = pref.getString("userid", null);
            progress = new ProgressDialog(ProductListDemoActivity.this);
            progress.setMessage("Please wait...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
            eLView = (ListView) findViewById(R.id.myListView);
            View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_view, null, false);
            eLView.addFooterView(footerView);
            loadmore = (Button) footerView.findViewById(R.id.button);
            iv_setting = (ImageView) findViewById(R.id.iv_setting);

            loadmore.setVisibility(View.GONE);
            loadmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    //   Toast.makeText(PetListActivity.this, ""+ eLView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                    // getAll_Pets_service(eLView.getLastVisiblePosition() + 10);
                    next_record_service(flag);
                }
            });

            iv_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {

                    if (userID != null) {
                        popupwindow1(view);

                    } else {
                        popupwindow2(view);
                    }

                }
            });
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            mTvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbar.setNavigationIcon(R.drawable.back_btn_img);

            myLocationListener = new MyLocationListener(ProductListDemoActivity.this);
            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), ProductListDemoActivity.this)) {
                fetchLocationData();
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), ProductListDemoActivity.this);
            }
            intent = getIntent();
            title = intent.getStringExtra("title");
            mTvTitle.setText(title);
            iv_search = (ImageView) findViewById(R.id.iv_search);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);


            // getBuy_Sale_Pets_List("Buy");
            // login("Buy");
            mTvSearch = (EditText) findViewById(R.id.searchtxt);
            iv_dog = (ImageView) findViewById(R.id.iv_dog);
            iv_cat = (ImageView) findViewById(R.id.iv_cat);
            radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
            radioGroup.clearCheck();
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {
                        mTvSearch.setText("");
                        search_type = rb.getText().toString();
                        loadmore.setVisibility(View.GONE);
                        // Toast.makeText(PetListActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                        progress = new ProgressDialog(ProductListDemoActivity.this);
                        progress.setMessage("Please wait");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        // progress.setCancelable(false);
                        progress.show();
                        //  getBuy_Sale_Pets_List(rb.getText().toString());

                        getListByType(city, rb.getText().toString(),1);
                    }

                }
            });

            iv_dog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getList(city, "Dog",1);
                }
            });
            iv_cat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // popupwindow(view);
                    getList(city, "Cat",1);
                }
            });

            iv_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mTvSearch.getText().toString().length() != 0) {
                        if (getCurrentFocus() != null) {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }

                        progress.setIndeterminate(true);
                        // progress.setCancelable(false);
                        progress.show();
                        // getPetsListFromCurrentLocation(mTvSearch.getText().toString(), search_type);
                        // getList(mTvSearch.getText().toString(), search_type);
                        getDataByCityLocationEnter(mTvSearch.getText().toString(),1);

                    } else {
                        Toast.makeText(ProductListDemoActivity.this, "Please enter search text.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
            radioGroup2.clearCheck();
            radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {
                        // Toast.makeText(PetListActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                       /* progress = new ProgressDialog(PetListActivity.this);
                        progress.setMessage("Please wait...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        progress.setCancelable(false);
                        progress.show();*/
                        getBuy_Sale_Pets_List(rb.getText().toString());
                    }

                }
            });
            radioGroup23 = (RadioGroup) findViewById(R.id.radioGroup23);
            radioGroup23.clearCheck();
            radioGroup23.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {
                        // Toast.makeText(PetListActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                        progress = new ProgressDialog(ProductListDemoActivity.this);
                        progress.setMessage("Please wait...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        progress.setCancelable(false);
                        progress.show();
                        getBuy_Sale_Pets_List(rb.getText().toString());
                    }

                }
            });
            toolbar.setNavigationOnClickListener(

                    new View.OnClickListener() {
                        @Override

                        public void onClick(View v) {

                            Intent intent = new Intent(ProductListDemoActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }

            );

            eLView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    All_Pets_List_Model all_pets_list_model = pet_list_adaptor.getItem(position);
                    Intent intent2 = new Intent(ProductListDemoActivity.this, PetDetails.class);


                    Bundle bundleObject = new Bundle();
                    bundleObject.putSerializable("key", all_pets_list_model);

// Put Bundle in to Intent and call start Activity
                    intent2.putExtras(bundleObject);
                    startActivity(intent2);
                /*    Intent intent2 = new Intent(PetListActivity.this, BuySaleDetailsSwipeListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("arraylist", pets_list);
                    bundle.putInt("position",position);
                    bundle.putSerializable("petobject",all_pets_list_model);
                    intent2.putExtras(bundle);
                    startActivity(intent2);*/
                }
            });


            mBtnBuyOrSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  //  if (userID != null) {
                        Intent intent = new Intent(ProductListDemoActivity.this, BuyOrSalePetsActivity.class);
                        startActivity(intent);
                    //}
                    /* else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        Intent intent = new Intent(PetListDemoActivity.this, LoginActivity.class);
                                        intent.putExtra("key", "PetListActivity");
                                        startActivity(intent);
                                    case DialogInterface.BUTTON_NEGATIVE:

                                        // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(PetListDemoActivity.this);
                        builder.setTitle("Message");
                        builder.setCancelable(false);
                        builder.setMessage("Please Login ").setPositiveButton("Login", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

                    }*/
                }
            });

            //  getAll_Pets_service(flag);

            //getAll_Pets_serviceNew();

           /* GPSTracker gpsTracker = new GPSTracker(this);
            if (gpsTracker.getIsGPSTrackingEnabled()) {
                Double stringLatitude = gpsTracker.latitude;
                Double stringgong = gpsTracker.longitude;


                LocationAddress locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(stringLatitude, stringgong,
                        getApplicationContext(), new GeocoderHandler());


            } else {

                gpsTracker.showSettingsAlert();
            }*/
            mTvSearch.addTextChangedListener(this);

            mTvSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            mTvSearch.setImeActionLabel("Search", EditorInfo.IME_ACTION_SEARCH);

            mTvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        getDataByCityLocationEnter(mTvSearch.getText().toString(),1);

                        return true;
                    }
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getInit() {
        linearLayout = (LinearLayout) findViewById(R.id.ll_top);
        ll_top2 = (LinearLayout) findViewById(R.id.ll_top2);
        ll_top23 = (LinearLayout) findViewById(R.id.ll_top23);
        mBtnBuyOrSale = (Button) findViewById(R.id.activity_login_btn_buysale);

    }

    public void getAll_Pets_service(int page_no) {
        progress.show();
        try {
            String url = WebURL.ALL_PETS_PAGE_URL + page_no;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        progress.dismiss();
                      /*  if (progress.isShowing()) {
                            progress.dismiss();
                        }*/
                        if (response.has("flag")) {

                            System.out.println("response123" + response);
                            pets_list = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                pets_list.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                        jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                        jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                        jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                        jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                        jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("state"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("email" + jsonObject.getString("email"));
                            }

                            pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);
                            flag = flag + 1;
                          /*  pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);
                            //  listView2.setAdapter(pet_list_adaptor);
                            //  listView.setVisibility(View.VISIBLE);*/

                        } else {
                            Toast.makeText(ProductListDemoActivity.this, "No Record found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, "jreq");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAll_Pets_serviceNew() {
        progress.show();
        try {
            String url = WebURL.GET_BUYSALE_PAYMENTDONE_LIST;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        progress.dismiss();
                      /*  if (progress.isShowing()) {
                            progress.dismiss();
                        }*/
                        if (response.has("flag")) {

                            System.out.println("response123" + response);
                            pets_list = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                pets_list.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                        jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                        jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                        jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                        jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                        jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("state"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("email" + jsonObject.getString("email"));
                            }

                            pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);
                            flag = flag + 1;
                          /*  pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);
                            //  listView2.setAdapter(pet_list_adaptor);
                            //  listView.setVisibility(View.VISIBLE);*/

                        } else {
                            Toast.makeText(ProductListDemoActivity.this, "No Record found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, "jreq");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    public void next_record_service(int page_no) {
        progress.show();
        try {
            String url = WebURL.ALL_PETS_PAGE_URL + page_no;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        progress.dismiss();
                      /*  if (progress.isShowing()) {
                            progress.dismiss();
                        }*/
                        if (response.has("flag")) {
                            System.out.println("response123next" + response);

                            myList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                myList.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                        jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                        jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                        jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                        jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                        jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("state"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                pets_list.add(myList.get(i));
                            }
                          /*  pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, pets_list);
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);*/
                            pet_list_adaptor.notifyDataSetChanged();
                            flag = flag + 1;
                            Log.i("page_n0=", "" + flag);
                          /*  pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);
                            //  listView2.setAdapter(pet_list_adaptor);
                            //  listView.setVisibility(View.VISIBLE);*/

                        } else {
                            Toast.makeText(ProductListDemoActivity.this, "No Record found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, "jreq");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //   get list of pets according to buy or sale
    public void getBuy_Sale_Pets_List(String buyORSale) {

        System.out.println("responsebuy " + buyORSale);
        eLView.setVisibility(View.VISIBLE);
        try {
            String url = WebURL.GET_BUYSALE_PAYMENTDONE_BYTYPE; //+ "Sale";

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url + buyORSale,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        System.out.println("responsebuy123 " + response.toString());
                        if (response.has("flag")) {
                            pets_list = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                pets_list.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                        jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                        jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                        jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                        jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                        jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("state"),jsonObject.getString("country"),jsonObject.getString("zip")));

                            }


                            pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                            eLView.setAdapter(pet_list_adaptor);


                        } else {
                            Toast.makeText(ProductListDemoActivity.this, "No Record found", Toast.LENGTH_SHORT).show();

                            eLView.setVisibility(View.INVISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("jsonError" + e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, "jreq");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        logit = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //	method to supply fake items to adapter
    private ArrayList<All_Pets_List_Model> creatingItems(int mult) {

        myList = new ArrayList<>();
        int cnt = (pets_list.size() < 5) ? pets_list.size() : 5;
        for (int i = 0; i < cnt; i++) {
            int total = mult + i;
            if (pets_list.size() < mult + i) {

            } else {
                myList.add(pets_list.get(total - 1));
            }

        }

        return myList;
    }

    public void getPetsListFromCurrentLocation(String location, String s_type) {
        try {
            String url = WebURL.SEARCH_PETS_FROM_KEYWORD + "keyword=" + location + "&type=" + s_type;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        int flag = response.getInt("flag");
                        System.out.println("Response...." + response.toString());
                        System.out.println("Flag...." + flag);

                        if (flag == 1) {
                            pets_list = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                pets_list.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                        jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                        jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                        jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                        jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                        jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("state"),jsonObject.getString("country"),jsonObject.getString("zip")));
                            }
                           /* pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);*/

                            pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                            eLView.setAdapter(pet_list_adaptor);
                            loadmore.setVisibility(View.GONE);
                        } else {
                            loadmore.setVisibility(View.GONE);
                            pets_list = new ArrayList<>();
                            pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                            eLView.setAdapter(pet_list_adaptor);
                            Toast.makeText(ProductListDemoActivity.this, "No record found.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error.." + error);
                }
            });
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, "jreq");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getList(String location,final  String s_type,int count) {
        counttotalAnimal=count;
        String url = WebURL.SEARCH_BUYSALE_CITYNANIMAL_PAGE+ "keyword=" + location + "&type=" + s_type+ "&currentpage="+counttotalAnimal;;

        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        System.out.println("responsde" + response.toString());
                        try {
                            JSONObject j = new JSONObject(response);
                            flag = j.getInt("flag");
                            System.out.println("Flag.." + flag);
                            int totalcount=j.getInt("totalpages");
                            if(totalcount==counttotalAnimal)
                            {
                                loadmore.setVisibility(View.GONE);
                            }else
                            {
                                loadmore.setVisibility(View.VISIBLE);
                            }
                            if (flag == 1) {
                                pets_list = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    pets_list.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                            jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                            jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                            jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                            jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                            jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                            jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("state"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                }
                           /* pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);*/

                                pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                                eLView.setAdapter(pet_list_adaptor);
                                counttotalAnimal=counttotalAnimal+1;

                                loadmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        getList(city,s_type,counttotalAnimal);
                                    }
                                });                            } else {
                                loadmore.setVisibility(View.GONE);
                                pets_list = new ArrayList<>();
                                pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                                eLView.setAdapter(pet_list_adaptor);
                                Toast.makeText(ProductListDemoActivity.this, "No record found.", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductListDemoActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void getListByType(String location, final String s_type, int count) {
        counttotalType=count;
        String url = WebURL.SEARCH_BUYSALE_CITYNTYPE_PAGE+ "keyword=" + location + "&type=" + s_type+ "&currentpage="+counttotalType;


        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        System.out.println("responsde" + response.toString());
                        try {
                            JSONObject j = new JSONObject(response);
                            flag = j.getInt("flag");
                            System.out.println("Flag.." + flag);
                            int totalcount= j.getInt("totalpages");


                            if(totalcount==counttotalType)
                            {
                                loadmore.setVisibility(View.GONE);
                            }else
                            {
                                loadmore.setVisibility(View.VISIBLE);
                            }
                            if (flag == 1) {
                                pets_list = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    pets_list.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                            jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                            jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                            jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                            jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                            jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                            jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("state"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                }
                           /* pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);*/

                                pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                                eLView.setAdapter(pet_list_adaptor);
                                counttotalType=counttotalType+1;

                                loadmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        //   Toast.makeText(PetListActivity.this, ""+ eLView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                                        // getAll_Pets_service(eLView.getLastVisiblePosition() + 10);
                                        getListByType(city,s_type,counttotalType);
                                    }
                                });
                            } else {
                                loadmore.setVisibility(View.GONE);
                                pets_list = new ArrayList<>();
                                pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                                eLView.setAdapter(pet_list_adaptor);
                                Toast.makeText(ProductListDemoActivity.this, "No record found.", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductListDemoActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void popupwindow(View view) {


        PopupMenu popup = new PopupMenu(ProductListDemoActivity.this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                // Toast.makeText(PetListActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                if (search_type.equals("")) {
                    search_type = "Buy";
                }
                if (item.getTitle().toString().equals("Sort date by Ascending")) {
                    ascendingdescending("1", search_type);
                } else {
                    ascendingdescending("2", search_type);

                }
                return true;
            }
        });


        popup.show();//showing popup menu
    }

    public void popupwindow1(View view) {


        PopupMenu popup = new PopupMenu(ProductListDemoActivity.this, view);

        popup.getMenuInflater().inflate(R.menu.popup_mymenu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equals("Logout")) {
                    SharedPreferences pref;
                    SharedPreferences.Editor editor;
                    pref = getApplication().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.clear();
                    editor.commit();

                    Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.putExtra("key", "BuyOrSalePets");
                    startActivity(intent1);
                    finish();
                    startActivity(intent1);

                } else {
                    Intent intent = new Intent(ProductListDemoActivity.this, MyPetListBuyorSaleActivity.class);
                    startActivity(intent);

                }
                return true;
            }
        });


        popup.show();//showing popup menu
    }
    public void popupwindow2(View view) {


        PopupMenu popup = new PopupMenu(ProductListDemoActivity.this, view);

        popup.getMenuInflater().inflate(R.menu.popup_mymenuone, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equals("Login")) {
                    Intent intent = new Intent(ProductListDemoActivity.this, LoginActivity.class);
                    intent.putExtra("key", "PetListActivity");
                    startActivity(intent);

                }
                return true;
            }
        });


        popup.show();//showing popup menu
    }

    public void ascendingdescending(String sortvalue, String type) {
        progress.show();
        try {
            String url = WebURL.GETPETLIST_ASCDESC;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url + sortvalue + "&type=" + type,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    try {
                      /*  if (progress.isShowing()) {
                            progress.dismiss();
                        }*/
                        if (response.has("flag")) {

                            System.out.println("response123" + response);
                            pets_list = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                pets_list.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                        jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                        jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                        jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                        jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                        jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("state"),jsonObject.getString("country"),jsonObject.getString("zip")));
                            }
                            pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);

                          /*  pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);
                            //  listView2.setAdapter(pet_list_adaptor);
                            //  listView.setVisibility(View.VISIBLE);*/

                        } else {
                            Toast.makeText(ProductListDemoActivity.this, "No Record found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, "jreq");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchbyPet(String Pettype) {

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ProductListDemoActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    public void fetchLocationData() {
        // check if myLocationListener enabled
        if (myLocationListener.canGetLocation()) {
            latitude = myLocationListener.getLatitude();
            longitude = myLocationListener.getLongitude();

            // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            myLocationListener.showSettingsAlert();
            latitude = myLocationListener.getLatitude();
            longitude = myLocationListener.getLongitude();

            // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

        }

        Geocoder geocoder = new Geocoder(ProductListDemoActivity.this, Locale.getDefault());
        List<Address> addresses = null;
        StringBuilder result = new StringBuilder();

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                //  result.append(address.getAddressLine(0)).append(" ");
                result.append(address.getAddressLine(1)).append(" ");
                // result.append(address.getAddressLine(2)).append("");
                //result.append(address.getAddressLine(3)).append(" ");
                //   Toast.makeText(getApplicationContext(), address.toString(), Toast.LENGTH_LONG).show();
                System.out.println("Address" + address.toString());
                System.out.println("Address...cit" + addresses.get(0).getLocality());
                city = addresses.get(0).getLocality();
                getDataByCityLocation(addresses.get(0).getLocality(),1);

               // getListByType(city, "Buy");


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fetchLocationData();

                } else {

                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();

                }
                break;

        }
    }

    public void getDataByCityLocation(String City,int count) {
        counttoatal=count;
        String url = WebURL.SEARCH_BUYSALE_CITYNEAR_PAGE + "keyword=" + City+"&currentpage="+counttoatal;

        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        System.out.println("responsde" + response.toString());
                        try {
                            JSONObject j = new JSONObject(response);
                            flag = j.getInt("flag");
                            System.out.println("Flag.." + flag);

                            int totalcount= j.getInt("totalpages");
                            System.out.println("Flag...." + flag);

                            if(totalcount==counttoatal)
                            {
                                loadmore.setVisibility(View.GONE);
                            }else
                            {
                                loadmore.setVisibility(View.VISIBLE);
                            }
                            if (flag == 1) {
                                pets_list = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    pets_list.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                            jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                            jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                            jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                            jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                            jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                            jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("state"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                }
                           /* pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);*/

                                pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                                eLView.setAdapter(pet_list_adaptor);
                                counttoatal=counttoatal+1;

                                loadmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        //   Toast.makeText(PetListActivity.this, ""+ eLView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                                        // getAll_Pets_service(eLView.getLastVisiblePosition() + 10);
                                        getDataByCityLocation(city,counttoatal);
                                    }
                                });
                            } else {
                                loadmore.setVisibility(View.GONE);
                                pets_list = new ArrayList<>();
                                pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                                eLView.setAdapter(pet_list_adaptor);
                                Toast.makeText(ProductListDemoActivity.this, "No record found.", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductListDemoActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public void getDataByCityLocationEnter(String City,int count) {
        counttotalKeyword=count;

        String url = WebURL.SEARCH_BUYSALE_CITYONLY_PAGE+ "keyword=" + City + "&currentpage="+counttotalKeyword;;


        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        System.out.println("responsde" + response.toString());
                        try {
                            JSONObject j = new JSONObject(response);
                            flag = j.getInt("flag");
                            System.out.println("Flag.." + flag);
                            int totalcount=j.getInt("totalpages");
                            if(totalcount==counttotalKeyword)
                            {
                                loadmore.setVisibility(View.GONE);
                            }else
                            {
                                loadmore.setVisibility(View.VISIBLE);
                            }
                            if (flag == 1) {
                                pets_list = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    pets_list.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                            jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                            jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                            jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                            jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                            jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                            jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("state"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                }
                           /* pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);*/

                                pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                                eLView.setAdapter(pet_list_adaptor);
                                counttotalKeyword=counttotalKeyword+1;

                                loadmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        getDataByCityLocationEnter(city,counttotalKeyword);
                                    }
                                });

                            } else {
                                loadmore.setVisibility(View.GONE);
                                pets_list = new ArrayList<>();
                                pet_list_adaptor = new Pet_list_Adaptor(ProductListDemoActivity.this, pets_list);
                                eLView.setAdapter(pet_list_adaptor);
                                Toast.makeText(ProductListDemoActivity.this, "No record found.", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductListDemoActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    //	background thread - use this class inside fragment to avoid any orientation changes
    private class FakeLoader extends AsyncTask<Void, Void, ArrayList<All_Pets_List_Model>> {

        @Override
        protected ArrayList<All_Pets_List_Model> doInBackground(Void... params) {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return creatingItems(mult);
        }

     /*   @Override
        protected void onPostExecute(ArrayList<All_Pets_List_Model> result) {
            super.onPostExecute(result);

            eLView.addNewData(result);
        }*/
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {

            switch (message.what) {
                case 1:

                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    //    Toast.makeText(PetListActivity.this, "" + locationAddress.toString(), Toast.LENGTH_SHORT).show();
                    // getPetsListFromCurrentLocation(locationAddress.toString());

                    break;
                default:
                    locationAddress = null;
            }

        }
    }

}