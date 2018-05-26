package com.hnweb.clawpal.adaption.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hnweb.clawpal.AppController;
import com.hnweb.clawpal.DashboardActivity;
import com.hnweb.clawpal.GPSTracker;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.Utils.RowItem;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.adaption.adaptor.AdoptPetAdaptor;
import com.hnweb.clawpal.adaption.model.AdoptPetModel;
import com.hnweb.clawpal.location.LocationSet;
import com.hnweb.clawpal.location.MyLocationListener;
import com.hnweb.clawpal.login.LoginActivity;
import com.hnweb.clawpal.lostorfound.pet.activity.LostFoundPeDemotList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by HNWeb-11 on 8/3/2016.
 */
public class AdoptionPetList extends LocationSet implements TextWatcher {
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    ImageView mIvBuySale, mIvAdoption, mIvLostFound;
    Toolbar toolbar;
    ImageView iv_search;
    TextView mTvTitle;
    MyLocationListener myLocationListener;
    double latitude = 0.0d;
    double longitude = 0.0d;
    Intent intent;
    String city;
    ProgressDialog progress;
    Button mBtnBuyOrSale;
    EditText mTvSearch;
    ArrayList<AdoptPetModel> adoptPetlist;
    AdoptPetAdaptor adoptPetAdaptor;
    boolean isconnected = false;
    String locationAddress;
    GPSTracker gpsTracker;
    SharedPreferences pref;
    ListView eLView;
    // ListView listView;
    List<RowItem> rowItems;
    String title;
    LinearLayout ll_top2;
    Button loadmore;
    ImageView tv_sort;
    ImageView iv_dog, iv_cat;
    ImageView iv_setting;
    String userID;
    private RadioGroup radioGroup23;
    // GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private int mult = 1;
    private ArrayList<AdoptPetModel> myList;
    private int flag = 1;
    private String search_type = "";

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_adoption);
        try {
            getInit();
            adoptPetlist = new ArrayList<>();
            pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            userID = pref.getString("userid", null);
            //  listView = (ListView) findViewById(R.id.listView1);
            // getAll_Pets_service();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            mTvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbar.setNavigationIcon(R.drawable.back_btn_img);
            eLView = (ListView) findViewById(R.id.myListView);
            progress = new ProgressDialog(AdoptionPetList.this);
            progress.setMessage("Please wait...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_view, null, false);
            eLView.addFooterView(footerView);
            loadmore = (Button) footerView.findViewById(R.id.button);
            loadmore.setVisibility(View.GONE);
            iv_setting = (ImageView) findViewById(R.id.iv_setting);

            loadmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    //   Toast.makeText(PetListActivity.this, ""+ eLView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                    // getAll_Pets_service(eLView.getLastVisiblePosition() + 10);
                    next_record_service(flag);
                }
            });
          /*  eLView.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {

                    return false;
                }
            });*/
           /* tv_sort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupwindow(view);
                }
            });*/




            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mTvSearch = (EditText) findViewById(R.id.searchtxt);
            iv_search = (ImageView) findViewById(R.id.iv_search);

            iv_dog = (ImageView) findViewById(R.id.iv_dog);
            iv_cat = (ImageView) findViewById(R.id.iv_cat);

            //  getAllPetsAdoptList(flag);
            //   getAllPetsAdoptListNew();

            radioGroup23 = (RadioGroup) findViewById(R.id.radioGroup23);
            radioGroup23.clearCheck();
            radioGroup23.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {
                        // Toast.makeText(PetListActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                        loadmore.setVisibility(View.GONE);
                        mTvSearch.setText("");

                        progress.setIndeterminate(true);
                        progress.setCancelable(false);
                        progress.show();
                        if (rb.getText().toString().equals("Looking To Adopt")) {
                            //get_adopt_pets_List("Looking");
                            getAdoptPetsListType(city, "Looking");
                            search_type = "Looking";
                        } else {
                            //  get_adopt_pets_List("Offering");
                            getAdoptPetsListType(city, "Offering");
                            search_type = "Offering";
                        }

                    }

                }
            });


            toolbar.setNavigationOnClickListener(

                    new View.OnClickListener() {
                        @Override

                        public void onClick(View v) {

                            Intent intent = new Intent(AdoptionPetList.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }

            );


            ;
            eLView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AdoptPetModel list_model = adoptPetAdaptor.getItem(position);

                    Intent intent2 = new Intent(AdoptionPetList.this, AdoptPetDetails.class);
                  /*  Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST",(Serializable)list_model);
                    intent2.putExtra("BUNDLE",args);
                    startActivity(intent);*/
                    // Create a Bundle and Put Bundle in to it
                    Bundle bundleObject = new Bundle();
                    bundleObject.putSerializable("key", list_model);
// Put Bundle in to Intent and call start Activity
                    intent2.putExtras(bundleObject);
                    startActivity(intent2);
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
                        progress = new ProgressDialog(AdoptionPetList.this);
                        progress.setMessage("Please wait...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        // progress.setCancelable(false);
                        progress.show();

                        //   getAdoptPetsListFromCurrentLocation(mTvSearch.getText().toString(), search_type);

                        getListByEnterCity(mTvSearch.getText().toString());

                    } else {
                        Toast.makeText(AdoptionPetList.this, "Please enter search text.", Toast.LENGTH_SHORT).show();
                    }


                }
            });
            if (userID != null) {
                iv_setting.setVisibility(View.VISIBLE);

            } else {
                iv_setting.setVisibility(View.GONE);
            }
            iv_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupwindow1(v);
                }
            });
            iv_dog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    getAdoptPetsListFromCurrentLocation(city, "Dog");
                }
            });
            iv_cat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // popupwindow(view);

                    getAdoptPetsListFromCurrentLocation(city, "Cat");
                }
            });
            mBtnBuyOrSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (userID != null) {
                        Intent intent = new Intent(AdoptionPetList.this, AddAPetActivity.class);
                        startActivity(intent);
                    } else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

                                        Intent intent = new Intent(AdoptionPetList.this, LoginActivity.class);
                                        intent.putExtra("key", "AdoptionPetList");
                                        startActivity(intent);


                                    case DialogInterface.BUTTON_NEGATIVE:

                                        // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdoptionPetList.this);
                        builder.setTitle("Message");
                        builder.setCancelable(false);
                        builder.setMessage("Please Login ").setPositiveButton("Login", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

                    }

                }
            });
            myLocationListener = new MyLocationListener(AdoptionPetList.this);
            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), AdoptionPetList.this)) {
                fetchLocationData();
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), AdoptionPetList.this);
            }
          /*  GPSTracker gpsTracker = new GPSTracker(this);
            if (gpsTracker.getIsGPSTrackingEnabled()) {
                Double stringLatitude = gpsTracker.latitude;
                Double stringgong = gpsTracker.longitude;
                // LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

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

                        getListByEnterCity(mTvSearch.getText().toString());

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

        ll_top2 = (LinearLayout) findViewById(R.id.ll_top2);

        mBtnBuyOrSale = (Button) findViewById(R.id.activity_login_btn_buysale);
    }

    public void getAdoptPetsListFromCurrentLocation(String location, String s_type) {
        progress.show();
        try {
            String url = WebURL.SEARCH_ADAPTION_CITYNANIMAL + "keyword=" + location + "&type=" + s_type;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    try {

                        if (progress.isShowing()) {
                            progress.dismiss();
                        }

                        System.out.println("Response...." + response.toString());
                        int flag = response.getInt("flag");
                        System.out.println("Flag...." + flag);

                        if (flag == 1) {
                            JSONArray jsonArray = response.getJSONArray("response");
                            adoptPetlist = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                adoptPetlist.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"),jsonObject.getString("email"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());

                            }


                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            eLView.setAdapter(adoptPetAdaptor);
                            loadmore.setVisibility(View.GONE);
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();

                            loadmore.setVisibility(View.GONE);
                            adoptPetlist = new ArrayList<>();
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            eLView.setAdapter(adoptPetAdaptor);
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


    public void getAdoptPetsListType(String location, String s_type) {
        progress.show();
        try {
            String url = WebURL.SEARCH_ADAPTION_CITYNTYPE + "keyword=" + location + "&type=" + s_type;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    try {

                        if (progress.isShowing()) {
                            progress.dismiss();
                        }

                        System.out.println("Response...." + response.toString());
                        int flag = response.getInt("flag");
                        System.out.println("Flag...." + flag);

                        if (flag == 1) {
                            JSONArray jsonArray = response.getJSONArray("response");
                            adoptPetlist = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                adoptPetlist.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"),jsonObject.getString("email"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());

                            }


                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            eLView.setAdapter(adoptPetAdaptor);
                            loadmore.setVisibility(View.GONE);
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();

                            loadmore.setVisibility(View.GONE);
                            adoptPetlist = new ArrayList<>();
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            eLView.setAdapter(adoptPetAdaptor);
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
    public void get_adopt_pets_List(String data) {
        try {
            String url = WebURL.GET_ADAPTION_PAYMENTDONE_BYTYPE;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url + data,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        if (response.has("flag")) {
                            adoptPetlist = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                adoptPetlist.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"),jsonObject.getString("email"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                        System.out.println("Address...." + jsonObject.getString("address").toString());

                            }
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            //	note : this should come next to loading view
                            eLView.setAdapter(adoptPetAdaptor);
                            //  adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            //    listView.setAdapter(adoptPetAdaptor);

                            loadmore.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(AdoptionPetList.this, "Record Not Found", Toast.LENGTH_SHORT).show();
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
    public void getAllPetsAdoptList(int page_no) {
        progress.show();
        try {
            String url = "http://www.designer321.com/johnpride/pawpal/mobile/get-pet-adoption.php?page=" + page_no;

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

                            adoptPetlist = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                adoptPetlist.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"),jsonObject.getString("email"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("Date...." + jsonObject.getString("adoption_date").toString());
                            }
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            //    listView.setAdapter(adoptPetAdaptor);
                            eLView.setAdapter(adoptPetAdaptor);
                            flag = flag + 1;
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionPetList.this, "Record Not Found", Toast.LENGTH_SHORT).show();
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

    public void getAllPetsAdoptListNew() {
        progress.show();
        try {
            String url = WebURL.GET_ADAPTION_PAYMENTDONE_LIST;

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

                            adoptPetlist = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                adoptPetlist.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"),jsonObject.getString("email"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("Date...." + jsonObject.getString("adoption_date").toString());
                            }
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            //    listView.setAdapter(adoptPetAdaptor);
                            eLView.setAdapter(adoptPetAdaptor);
                            flag = flag + 1;
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionPetList.this, "Record Not Found", Toast.LENGTH_SHORT).show();
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

    public void next_record_service(int page_no) {
        progress.show();
        try {
            String url = "http://www.designer321.com/johnpride/pawpal/mobile/get-pet-adoption.php?page=" + page_no;
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        progress.dismiss();
                      /*  if (progress.isShowing()) {
                            progress.dismiss();
                        }*/
                        int flag = response.getInt("flag");

                        if (flag == 1) {
                            System.out.println("response123next" + response);

                            myList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                myList.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"),jsonObject.getString("email"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());

                                adoptPetlist.add(myList.get(i));
                            }
                          /*  pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, pets_list);
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);*/
                            adoptPetAdaptor.notifyDataSetChanged();
                            flag = flag + 1;
                            Log.i("page_n0=", "" + flag);
                          /*  pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);
                            //  listView2.setAdapter(pet_list_adaptor);
                            //  listView.setVisibility(View.VISIBLE);*/

                        } else {
                            Toast.makeText(AdoptionPetList.this, "No more Record found", Toast.LENGTH_SHORT).show();
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

    //	method to supply fake items to adapter
    private ArrayList<AdoptPetModel> creatingItems(int mult) {

        myList = new ArrayList<>();
        int cnt = (adoptPetlist.size() < 5) ? adoptPetlist.size() : 5;
        for (int i = 0; i < cnt; i++) {
            int total = mult + i;
            if (adoptPetlist.size() < total) {

            } else {
                myList.add(adoptPetlist.get(total - 1));
            }

        }

        return myList;
    }

    public void popupwindow(View view) {


        PopupMenu popup = new PopupMenu(AdoptionPetList.this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //  Toast.makeText(AdoptionPetList.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                System.out.println("Clicked " + item.getTitle().toString());


                if (search_type.equals("")) {
                    search_type = "Looking";
                }

                if (item.getTitle().toString().equals("Sort date by Ascending")) {

                    getAllPetsAscendingorDescending("1", search_type);
                } else {

                    getAllPetsAscendingorDescending("2", search_type);

                }
                return true;
            }
        });


        popup.show();//showing popup menu
    }

  /*  @Override
    public void loadData() {
        mult += 5;

        //	new data loader
        new FakeLoader().execute();

    }*/

    public void getAllPetsAscendingorDescending(String sortvalue, String type) {
        progress.show();
        try {
            String url = WebURL.GETADAPTIONPET_ASCDESC + sortvalue + "&adoption_type=" + type;


            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
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

                            adoptPetlist = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                adoptPetlist.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"),jsonObject.getString("email"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());

                            }
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            //    listView.setAdapter(adoptPetAdaptor);
                            eLView.setAdapter(adoptPetAdaptor);
                            flag = flag + 1;
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionPetList.this, "Record Not Found", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(AdoptionPetList.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    public void popupwindow1(View view) {


        PopupMenu popup = new PopupMenu(AdoptionPetList.this, view);

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
                    intent1.putExtra("key", "AddAPetActivity");
                    finish();
                    startActivity(intent1);

                } else {
                    Intent intent = new Intent(AdoptionPetList.this, MyPetListAdaptioActivity.class);
                    startActivity(intent);

                }
                return true;
            }
        });


        popup.show();//showing popup menu
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

        Geocoder geocoder = new Geocoder(AdoptionPetList.this, Locale.getDefault());
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
                getListByCity(addresses.get(0).getLocality());

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

    public void getListByCity(String location) {
        progress.show();
        try {
            String url = WebURL.SEARCH_ADAPTION_CITYNEAR + "keyword=" + location;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    try {

                        if (progress.isShowing()) {
                            progress.dismiss();
                        }

                        System.out.println("Response...." + response.toString());
                        int flag = response.getInt("flag");
                        System.out.println("Flag...." + flag);

                        if (flag == 1) {
                            JSONArray jsonArray = response.getJSONArray("response");
                            adoptPetlist = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                adoptPetlist.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"),jsonObject.getString("email"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());

                            }


                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            eLView.setAdapter(adoptPetAdaptor);
                            loadmore.setVisibility(View.GONE);
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();

                            loadmore.setVisibility(View.GONE);
                            adoptPetlist = new ArrayList<>();
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            eLView.setAdapter(adoptPetAdaptor);
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

    public void getListByEnterCity(String location) {
        progress.show();
        try {
            String url = WebURL.SEARCH_ADAPTION_CITYONLY + "keyword=" + location;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    try {

                        if (progress.isShowing()) {
                            progress.dismiss();
                        }

                        System.out.println("Response...." + response.toString());
                        int flag = response.getInt("flag");
                        System.out.println("Flag...." + flag);

                        if (flag == 1) {
                            JSONArray jsonArray = response.getJSONArray("response");
                            adoptPetlist = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                adoptPetlist.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"),jsonObject.getString("email"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());

                            }


                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            eLView.setAdapter(adoptPetAdaptor);
                            loadmore.setVisibility(View.GONE);
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();

                            loadmore.setVisibility(View.GONE);
                            adoptPetlist = new ArrayList<>();
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            eLView.setAdapter(adoptPetAdaptor);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {

            switch (message.what) {
                case 1:

                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    //    Toast.makeText(PetListActivity.this, "" + locationAddress.toString(), Toast.LENGTH_SHORT).show();
                    //   getAdoptPetsListFromCurrentLocation(locationAddress.toString());

                    break;
                default:
                    locationAddress = null;
            }

        }
    }

    //	background thread - use this class inside fragment to avoid any orientation changes
    private class FakeLoader extends AsyncTask<Void, Void, ArrayList<AdoptPetModel>> {

        @Override
        protected ArrayList<AdoptPetModel> doInBackground(Void... params) {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return creatingItems(mult);
        }

    /*    @Override
        protected void onPostExecute(ArrayList<AdoptPetModel> result) {
            super.onPostExecute(result);

            eLView.addNewData(result);
        }*/
    }


}
