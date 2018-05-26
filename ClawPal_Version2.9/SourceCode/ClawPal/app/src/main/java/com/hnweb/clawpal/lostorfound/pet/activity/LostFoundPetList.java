package com.hnweb.clawpal.lostorfound.pet.activity;

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
import com.hnweb.clawpal.location.LocationSet;
import com.hnweb.clawpal.location.MyLocationListener;
import com.hnweb.clawpal.login.LoginActivity;
import com.hnweb.clawpal.lostorfound.pet.adaptor.lostFoundPetsAdaptor;
import com.hnweb.clawpal.lostorfound.pet.model.lostFoundPetListModel;

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

public class LostFoundPetList extends LocationSet implements TextWatcher {
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    ImageView mIvBuySale, mIvAdoption, mIvLostFound;
    Toolbar toolbar;
    TextView mTvTitle;
    ImageView iv_search;
    ImageView iv_dog, iv_cat;
    String city;
    ProgressDialog progress;
    Button mBtnBuyOrSale;
    MyLocationListener myLocationListener;
    double latitude = 0.0d;
    double longitude = 0.0d;
    EditText mTvSearch;
    ArrayList<lostFoundPetListModel> lostFoundPetList;
    ArrayList<lostFoundPetListModel> myList;
    lostFoundPetsAdaptor FoundPetsAdaptor;
    boolean isconnected = false;
    String locationAddress;
    GPSTracker gpsTracker;
    SharedPreferences pref;
    String search_type = "";
    // ListView listView;
    List<RowItem> rowItems;
    String title;
    LinearLayout ll_top2;
    ListView eLView;
    int flag = 1;
    Button loadmore;
    ImageView iv_setting;
    String userID;
    private int mult = 1;
    private RadioGroup radioGroup2;
    // GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_lost_found_pet_list);
        try {
            getInit();
            lostFoundPetList = new ArrayList<>();
            radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
            pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            userID = pref.getString("userid", null);
            progress = new ProgressDialog(LostFoundPetList.this);
            progress.setMessage("Please wait...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            eLView = (ListView) findViewById(R.id.myListView);
            View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_view, null, false);
            eLView.addFooterView(footerView);
            loadmore = (Button) footerView.findViewById(R.id.button);
            loadmore.setVisibility(View.GONE);
            iv_setting = (ImageView) findViewById(R.id.iv_setting);
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
            loadmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    //   Toast.makeText(PetListActivity.this, ""+ eLView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                    // getAll_Pets_service(eLView.getLastVisiblePosition() + 10);
                    next_record_service(flag);
                }
            });
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationIcon(R.drawable.back_btn_img);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            mTvSearch = (EditText) findViewById(R.id.searchtxt);
            iv_search = (ImageView) findViewById(R.id.iv_search);
            radioGroup2.clearCheck();
            radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {
                        mTvSearch.setText("");
                        loadmore.setVisibility(View.GONE);
                        // Toast.makeText(PetListActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                        search_type = rb.getText().toString();
                        progress = new ProgressDialog(LostFoundPetList.this);
                        progress.setMessage("Please wait...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        progress.setCancelable(true);
                        progress.show();
                        getLost_found_List(rb.getText().toString());
                        getlostFoundPetsListFromCurrentLocationType(city,rb.getText().toString());
                    }

                }
            });

            toolbar.setNavigationOnClickListener(

                    new View.OnClickListener() {
                        @Override

                        public void onClick(View v) {

                            Intent intent = new Intent(LostFoundPetList.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }

            );
            iv_dog = (ImageView) findViewById(R.id.iv_dog);
            iv_cat = (ImageView) findViewById(R.id.iv_cat);
          /*  tv_sort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupwindow(view);
                }
            });*/

            eLView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    lostFoundPetListModel list_model = FoundPetsAdaptor.getItem(position);
                    Intent intent2 = new Intent(LostFoundPetList.this, LostFoundPetDetails.class);
                   /* Bundle args = new Bundle();
                    args.putSerializable("ARRAYLIST",(Serializable) list_model);
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
                        progress = new ProgressDialog(LostFoundPetList.this);
                        progress.setMessage("Please wait...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        // progress.setCancelable(false);
                        progress.show();

                        //getlostFoundPetsListFromCurrentLocation(mTvSearch.getText().toString(), search_type);

                        getDataByCityLocationEnter(mTvSearch.getText().toString());
                    } else {
                        Toast.makeText(LostFoundPetList.this, "Please enter search text.", Toast.LENGTH_SHORT).show();
                    }


                }
            });
            iv_dog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getlostFoundPetsListFromCurrentLocation(city, "Dog");
                }
            });
            iv_cat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // popupwindow(view);

                    getlostFoundPetsListFromCurrentLocation(city, "Cat");
                }
            });
            mBtnBuyOrSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (userID != null) {
                        Intent intent = new Intent(LostFoundPetList.this, ReportAPetActivity.class);
                        startActivity(intent);
                    } else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

                                        Intent intent = new Intent(LostFoundPetList.this, LoginActivity.class);
                                        intent.putExtra("key", "LostFoundPetList");
                                        startActivity(intent);


                                    case DialogInterface.BUTTON_NEGATIVE:

                                        // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(LostFoundPetList.this);
                        builder.setTitle("Message");
                        builder.setCancelable(false);
                        builder.setMessage("Please Login ").setPositiveButton("Login", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

                    }

                }
            });
            //getAllPetsLostAndFound(flag);


            // getAllPetsLostAndFoundNew();

         /*   GPSTracker gpsTracker = new GPSTracker(this);
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
            myLocationListener = new MyLocationListener(LostFoundPetList.this);
            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), LostFoundPetList.this)) {
                fetchLocationData();
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), LostFoundPetList.this);
            }

            mTvSearch.addTextChangedListener(this);

            mTvSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            mTvSearch.setImeActionLabel("Search", EditorInfo.IME_ACTION_SEARCH);

            mTvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        getDataByCityLocationEnter(mTvSearch.getText().toString());

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

    public void getlostFoundPetsListFromCurrentLocation(String location, String s_type) {
        progress.show();
        try {
            String url = WebURL.SEARCH_LOSTFOUND_CITYNANIMAL + "keyword=" + location + "&type=" + s_type;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        int flag = response.getInt("flag");
                        if (flag == 1) {

                            JSONArray jsonArray = response.getJSONArray("response");
                            lostFoundPetList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lostFoundPetList.add(new lostFoundPetListModel(jsonObject.getString("lost_found_id"), jsonObject.getString("title"), jsonObject.getString("name"),
                                        jsonObject.getString("age_range"), jsonObject.getString("gender"), jsonObject.getString("type_of_animal"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("report_type"), jsonObject.getString("description"),
                                        jsonObject.getString("location"), jsonObject.getString("state"), jsonObject.getString("image"),
                                        jsonObject.getString("reporter_name"), jsonObject.getString("reporter_email"), jsonObject.getString("reporter_contact"),
                                        jsonObject.getString("report_date_time"), jsonObject.getString("address"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("country"),jsonObject.getString("zip")));

                            }
                            FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, lostFoundPetList);
                            eLView.setAdapter(FoundPetsAdaptor);

                            loadmore.setVisibility(View.GONE);

                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(LostFoundPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();
                            loadmore.setVisibility(View.GONE);
                            lostFoundPetList = new ArrayList<>();
                            FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, lostFoundPetList);
                            eLView.setAdapter(FoundPetsAdaptor);
                            Toast.makeText(LostFoundPetList.this, "No record found.", Toast.LENGTH_SHORT).show();
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

    public void getlostFoundPetsListFromCurrentLocationType(String location, String s_type) {
        progress.show();
        try {
            String url = WebURL.SEARCH_LOSTFOUND_CITYNTYPE + "keyword=" + location + "&type=" + s_type;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        int flag = response.getInt("flag");
                        if (flag == 1) {

                            JSONArray jsonArray = response.getJSONArray("response");
                            lostFoundPetList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lostFoundPetList.add(new lostFoundPetListModel(jsonObject.getString("lost_found_id"), jsonObject.getString("title"), jsonObject.getString("name"),
                                        jsonObject.getString("age_range"), jsonObject.getString("gender"), jsonObject.getString("type_of_animal"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("report_type"), jsonObject.getString("description"),
                                        jsonObject.getString("location"), jsonObject.getString("state"), jsonObject.getString("image"),
                                        jsonObject.getString("reporter_name"), jsonObject.getString("reporter_email"), jsonObject.getString("reporter_contact"),
                                        jsonObject.getString("report_date_time"), jsonObject.getString("address"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("country"),jsonObject.getString("zip")));

                            }
                            FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, lostFoundPetList);
                            eLView.setAdapter(FoundPetsAdaptor);

                            loadmore.setVisibility(View.GONE);

                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(LostFoundPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();
                            loadmore.setVisibility(View.GONE);
                            lostFoundPetList = new ArrayList<>();
                            FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, lostFoundPetList);
                            eLView.setAdapter(FoundPetsAdaptor);
                            Toast.makeText(LostFoundPetList.this, "No record found.", Toast.LENGTH_SHORT).show();
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
    public void getLost_found_List(String lostorfound) {
        try {
            String url = WebURL.GET_LOSTFOUND_PAYMENTDONE_BYTYPE;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url + lostorfound,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        if (response.has("flag")) {
                            lostFoundPetList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lostFoundPetList.add(new lostFoundPetListModel(jsonObject.getString("lost_found_id"), jsonObject.getString("title"), jsonObject.getString("name"),
                                        jsonObject.getString("age_range"), jsonObject.getString("gender"), jsonObject.getString("type_of_animal"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("report_type"), jsonObject.getString("description"),
                                        jsonObject.getString("location"), jsonObject.getString("state"), jsonObject.getString("image"),
                                        jsonObject.getString("reporter_name"), jsonObject.getString("reporter_email"), jsonObject.getString("reporter_contact"),
                                        jsonObject.getString("report_date_time"), jsonObject.getString("address"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("country"),jsonObject.getString("zip")));
                            }
                            FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, lostFoundPetList);
                            eLView.setAdapter(FoundPetsAdaptor);
                            // FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, lostFoundPetList);
                            //listView.setAdapter(FoundPetsAdaptor);

                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(LostFoundPetList.this, "No Record found", Toast.LENGTH_SHORT).show();
                            loadmore.setVisibility(View.GONE);
                            lostFoundPetList = new ArrayList<>();
                            FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, lostFoundPetList);
                            eLView.setAdapter(FoundPetsAdaptor);
                            Toast.makeText(LostFoundPetList.this, "No record found.", Toast.LENGTH_SHORT).show();
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
    public void getAllPetsLostAndFound(int page_no) {
        progress.show();
        try {
            String url = "http://www.designer321.com/johnpride/pawpal/mobile/get-pet-lost-found.php?page=" + page_no;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        progress.dismiss();
                      /*  if (progress.isShowing()) {
                            progress.dismiss();
                        }*/
                        Log.i("reponce", response.toString());

                        if (response.has("flag")) {
                            System.out.println("response123" + response);

                            lostFoundPetList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lostFoundPetList.add(new lostFoundPetListModel(jsonObject.getString("lost_found_id"), jsonObject.getString("title"), jsonObject.getString("name"),
                                        jsonObject.getString("age_range"), jsonObject.getString("gender"), jsonObject.getString("type_of_animal"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("report_type"), jsonObject.getString("description"),
                                        jsonObject.getString("location"), jsonObject.getString("state"), jsonObject.getString("image"),
                                        jsonObject.getString("reporter_name"), jsonObject.getString("reporter_email"), jsonObject.getString("reporter_contact"),
                                        jsonObject.getString("report_date_time"), jsonObject.getString("address"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("Email..." + jsonObject.getString("reporter_email"));
                            }
                            FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, creatingItems(mult));
                            eLView.setAdapter(FoundPetsAdaptor);
                            // listView.setAdapter(FoundPetsAdaptor);
                            flag = flag + 1;
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(LostFoundPetList.this, "No Record found", Toast.LENGTH_SHORT).show();
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

    public void getAllPetsLostAndFoundNew() {
        progress.show();
        try {
            String url = WebURL.GET_LOSTFOUND_PAYMENTDONE_LIST;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        progress.dismiss();
                      /*  if (progress.isShowing()) {
                            progress.dismiss();
                        }*/
                        Log.i("reponce", response.toString());

                        if (response.has("flag")) {
                            System.out.println("response123" + response);

                            lostFoundPetList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lostFoundPetList.add(new lostFoundPetListModel(jsonObject.getString("lost_found_id"), jsonObject.getString("title"), jsonObject.getString("name"),
                                        jsonObject.getString("age_range"), jsonObject.getString("gender"), jsonObject.getString("type_of_animal"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("report_type"), jsonObject.getString("description"),
                                        jsonObject.getString("location"), jsonObject.getString("state"), jsonObject.getString("image"),
                                        jsonObject.getString("reporter_name"), jsonObject.getString("reporter_email"), jsonObject.getString("reporter_contact"),
                                        jsonObject.getString("report_date_time"), jsonObject.getString("address"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("Email..." + jsonObject.getString("reporter_email"));
                            }
                            FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, creatingItems(mult));
                            eLView.setAdapter(FoundPetsAdaptor);
                            // listView.setAdapter(FoundPetsAdaptor);
                            flag = flag + 1;
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(LostFoundPetList.this, "No Record found", Toast.LENGTH_SHORT).show();
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
            String url = "http://www.designer321.com/johnpride/pawpal/mobile/get-pet-lost-found.php?page=" + page_no;
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
                            System.out.println("response123" + response);

                            myList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                myList.add(new lostFoundPetListModel(jsonObject.getString("lost_found_id"), jsonObject.getString("title"), jsonObject.getString("name"),
                                        jsonObject.getString("age_range"), jsonObject.getString("gender"), jsonObject.getString("type_of_animal"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("report_type"), jsonObject.getString("description"),
                                        jsonObject.getString("location"), jsonObject.getString("state"), jsonObject.getString("image"),
                                        jsonObject.getString("reporter_name"), jsonObject.getString("reporter_email"), jsonObject.getString("reporter_contact"),
                                        jsonObject.getString("report_date_time"), jsonObject.getString("address"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                lostFoundPetList.add(myList.get(i));
                            }
                          /*  pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, pets_list);
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);*/
                            FoundPetsAdaptor.notifyDataSetChanged();
                            flag = flag + 1;
                            Log.i("page_n0=", "" + flag);
                          /*  pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);
                            //  listView2.setAdapter(pet_list_adaptor);
                            //  listView.setVisibility(View.VISIBLE);*/

                        } else {
                            Toast.makeText(LostFoundPetList.this, "No more Record found", Toast.LENGTH_SHORT).show();
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
    private ArrayList<lostFoundPetListModel> creatingItems(int mult) {

        myList = new ArrayList<>();
        int cnt = (lostFoundPetList.size() < 5) ? lostFoundPetList.size() : 5;
        for (int i = 0; i < cnt; i++) {
            int total = mult + i;
            if (lostFoundPetList.size() < total) {

            } else {
                myList.add(lostFoundPetList.get(total - 1));
            }

        }

        return myList;
    }

    public void popupwindow(View view) {


        PopupMenu popup = new PopupMenu(LostFoundPetList.this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //Toast.makeText(LostFoundPetList.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                System.out.println("Clicked " + item.getTitle().toString());


                if (search_type.equals("")) {
                    search_type = "Lost";
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

   /* @Override
    public void loadData() {
        mult += 5;

        //	new data loader
        new FakeLoader().execute();

    }*/

    public void getAllPetsAscendingorDescending(String sortvalue, String type) {
        progress.show();
        try {
            String url = WebURL.GETLOSTFOUND_ASCDESC + sortvalue + "&type=";

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    try {
                      /*  if (progress.isShowing()) {
                            progress.dismiss();
                        }*/
                        Log.i("reponce", response.toString());

                        if (response.has("flag")) {
                            System.out.println("response123" + response);

                            lostFoundPetList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lostFoundPetList.add(new lostFoundPetListModel(jsonObject.getString("lost_found_id"), jsonObject.getString("title"), jsonObject.getString("name"),
                                        jsonObject.getString("age_range"), jsonObject.getString("gender"), jsonObject.getString("type_of_animal"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("report_type"), jsonObject.getString("description"),
                                        jsonObject.getString("location"), jsonObject.getString("state"), jsonObject.getString("image"),
                                        jsonObject.getString("reporter_name"), jsonObject.getString("reporter_email"), jsonObject.getString("reporter_contact"),
                                        jsonObject.getString("report_date_time"), jsonObject.getString("address"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("country"),jsonObject.getString("zip")));
                            }
                            FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, creatingItems(mult));
                            eLView.setAdapter(FoundPetsAdaptor);
                            // listView.setAdapter(FoundPetsAdaptor);
                            flag = flag + 1;
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(LostFoundPetList.this, "No Record found", Toast.LENGTH_SHORT).show();
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

        Intent intent = new Intent(LostFoundPetList.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    public void popupwindow1(View view) {


        PopupMenu popup = new PopupMenu(LostFoundPetList.this, view);

        popup.getMenuInflater().inflate(R.menu.popup_mymenu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equals("Logout")) {
                    SharedPreferences pref;
                    SharedPreferences.Editor editor;
                    pref = getApplication().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.remove("userid");
                    editor.apply();
                    Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.putExtra("key", "ReportAPet");
                    finish();
                    startActivity(intent1);

                } else {
                    Intent intent = new Intent(LostFoundPetList.this, MyPetListLostFoundActivity.class);
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

        Geocoder geocoder = new Geocoder(LostFoundPetList.this, Locale.getDefault());
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
                city=addresses.get(0).getLocality();
                getDataByCityLocation(addresses.get(0).getLocality());

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

    public void getDataByCityLocation(String city) {

        System.out.println("loctionCity.." +city);
        progress.show();
        try {
            String url = WebURL.SEARCH_LOSTFOUND_CITYNEAR + "keyword=" + city;

            System.out.println("CityUrl.." +url);


            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        System.out.println("CityLoctionlist"+response);
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        int flag = response.getInt("flag");
                        if (flag == 1) {

                            JSONArray jsonArray = response.getJSONArray("response");
                            lostFoundPetList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lostFoundPetList.add(new lostFoundPetListModel(jsonObject.getString("lost_found_id"), jsonObject.getString("title"), jsonObject.getString("name"),
                                        jsonObject.getString("age_range"), jsonObject.getString("gender"), jsonObject.getString("type_of_animal"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("report_type"), jsonObject.getString("description"),
                                        jsonObject.getString("location"), jsonObject.getString("state"), jsonObject.getString("image"),
                                        jsonObject.getString("reporter_name"), jsonObject.getString("reporter_email"), jsonObject.getString("reporter_contact"),
                                        jsonObject.getString("report_date_time"), jsonObject.getString("address"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("country"),jsonObject.getString("zip")));

                            }
                            FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, lostFoundPetList);
                            eLView.setAdapter(FoundPetsAdaptor);

                            loadmore.setVisibility(View.GONE);

                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(LostFoundPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();
                            loadmore.setVisibility(View.GONE);
                            lostFoundPetList = new ArrayList<>();
                            FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, lostFoundPetList);
                            eLView.setAdapter(FoundPetsAdaptor);
                            Toast.makeText(LostFoundPetList.this, "No record found.", Toast.LENGTH_SHORT).show();
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
    public void getDataByCityLocationEnter(String city) {

        System.out.println("loctionCity.." +city);
        progress.show();
        try {
            String url = WebURL.SEARCH_LOSTFOUND_CITYONLY + "keyword=" + city;
            System.out.println("CityUrl.." +url);


            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        System.out.println("CityLoctionlist"+response);
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        int flag = response.getInt("flag");
                        if (flag == 1) {

                            JSONArray jsonArray = response.getJSONArray("response");
                            lostFoundPetList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lostFoundPetList.add(new lostFoundPetListModel(jsonObject.getString("lost_found_id"), jsonObject.getString("title"), jsonObject.getString("name"),
                                        jsonObject.getString("age_range"), jsonObject.getString("gender"), jsonObject.getString("type_of_animal"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("report_type"), jsonObject.getString("description"),
                                        jsonObject.getString("location"), jsonObject.getString("state"), jsonObject.getString("image"),
                                        jsonObject.getString("reporter_name"), jsonObject.getString("reporter_email"), jsonObject.getString("reporter_contact"),
                                        jsonObject.getString("report_date_time"), jsonObject.getString("address"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("country"),jsonObject.getString("zip")));

                            }
                            FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, lostFoundPetList);
                            eLView.setAdapter(FoundPetsAdaptor);

                            loadmore.setVisibility(View.GONE);

                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(LostFoundPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();
                            loadmore.setVisibility(View.GONE);
                            lostFoundPetList = new ArrayList<>();
                            FoundPetsAdaptor = new lostFoundPetsAdaptor(LostFoundPetList.this, lostFoundPetList);
                            eLView.setAdapter(FoundPetsAdaptor);
                            Toast.makeText(LostFoundPetList.this, "No record found.", Toast.LENGTH_SHORT).show();
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
                    //  getlostFoundPetsListFromCurrentLocation(locationAddress.toString());
                    break;
                default:
                    locationAddress = null;
            }

        }
    }

    //	background thread - use this class inside fragment to avoid any orientation changes
    private class FakeLoader extends AsyncTask<Void, Void, ArrayList<lostFoundPetListModel>> {

        @Override
        protected ArrayList<lostFoundPetListModel> doInBackground(Void... params) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return creatingItems(mult);
        }

       /* @Override
        protected void onPostExecute(ArrayList<lostFoundPetListModel> result) {
            super.onPostExecute(result);

            eLView.addNewData(result);
        }*/
    }
}
