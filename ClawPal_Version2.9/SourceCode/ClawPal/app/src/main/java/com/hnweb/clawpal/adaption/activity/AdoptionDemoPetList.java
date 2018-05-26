package com.hnweb.clawpal.adaption.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.hnweb.clawpal.Utils.Constants;
import com.hnweb.clawpal.Utils.RowItem;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.adaption.adaptor.AdoptPetAdaptor;
import com.hnweb.clawpal.adaption.model.AdoptPetModel;
import com.hnweb.clawpal.location.LocationSet;
import com.hnweb.clawpal.location.MyLocationListener;
import com.hnweb.clawpal.login.LoginActivity;
import com.hnweb.clawpal.myfavpet.ViewPagerActivity;

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
public class AdoptionDemoPetList extends LocationSet implements TextWatcher {
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    ImageView mIvBuySale, mIvAdoption, mIvLostFound;
    Toolbar toolbar;
    ImageView iv_search;
    TextView mTvTitle;
    MyLocationListener myLocationListener;
    double latitude = 0.0d;
    int position_to_scroll;
    int position_to_scroll1;
    int position_to_scrollAll;
    int position_to_scrollCat;
    int position_to_scrollCat1;
    int position_to_scrollBuy;
    int position_to_scrollSale;
    int position_to_scrollText;
    int counttoatal = 0;
    int counttotalType = 0;
    int counttotalTypeOffer = 0;
    int counttotalAnimal = 0;
    int counttotalAnimalAll = 0;
    int counttotalAnimalCat = 0;
    int counttotalAnimalCatAll = 0;
    int counttotalKeyword = 0;
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
    RadioButton rb;
    String userID;
    ArrayList<AdoptPetModel> pets_list = new ArrayList<AdoptPetModel>();
    ArrayList<AdoptPetModel> pets_listLooking = new ArrayList<AdoptPetModel>();
    ArrayList<AdoptPetModel> pets_listOffering = new ArrayList<AdoptPetModel>();
    ArrayList<AdoptPetModel> pets_listText = new ArrayList<AdoptPetModel>();
    ArrayList<AdoptPetModel> pets_listAnimalDog = new ArrayList<AdoptPetModel>();
    ArrayList<AdoptPetModel> pets_listAnimalCat = new ArrayList<AdoptPetModel>();
    private RadioGroup radioGroup23;
    // GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private int mult = 1;
    private ArrayList<AdoptPetModel> myList;
    private ArrayList<AdoptPetModel> myList1;
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
            progress = new ProgressDialog(AdoptionDemoPetList.this);
            progress.setMessage("Please wait...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_view, null, false);
            eLView.addFooterView(footerView);
            loadmore = (Button) footerView.findViewById(R.id.button);
            loadmore.setVisibility(View.GONE);
            iv_setting = (ImageView) findViewById(R.id.iv_setting);
            Constants.pets_list2 = new ArrayList<>();
            Constants.pets_animal2 = new ArrayList<>();
            Constants.pets_animal2All = new ArrayList<>();
            Constants.pets_animalCat2 = new ArrayList<>();
            Constants.pets_animalCat2All = new ArrayList<>();
            Constants.pets_animalOffering = new ArrayList<>();
            Constants.pets_animallooking = new ArrayList<>();
            Constants.pets_animalText2 = new ArrayList<>();
           /* loadmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    //   Toast.makeText(PetListActivity.this, ""+ eLView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                    // getAll_Pets_service(eLView.getLastVisiblePosition() + 10);
                    next_record_service(flag);
                }
            });*/
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
                    rb = (RadioButton) group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {
                        // Toast.makeText(PetListActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                        loadmore.setVisibility(View.GONE);
                 //       mTvSearch.setText("");

                        progress.setIndeterminate(true);
                        progress.setCancelable(false);
                        progress.show();
                        if (rb.getText().toString().equals("Looking To Adopt")) {
                            if (mTvSearch.getText().toString().length() != 0) {
                                getAdoptPetsListTypeLooking(mTvSearch.getText().toString(), "Looking", 1);
                                search_type = "Looking";
                            } else {
                                getAdoptPetsListTypeLooking(city, "Looking", 1);
                                search_type = "Looking";
                            }

                        } else if (rb.getText().toString().equals("Offering To Adopt")) {

                            if (mTvSearch.getText().toString().length() != 0) {
                                getAdoptPetsListTypeOffering(mTvSearch.getText().toString(), "Offering", 1);
                                search_type = "Offering";
                            } else {
                                getAdoptPetsListTypeOffering(city, "Offering", 1);
                                search_type = "Offering";
                            }

                        } else if (rb.getText().toString().equals("All")) {
                            getListByCity(city, 1);
                        }


                    }

                }
            });


            toolbar.setNavigationOnClickListener(

                    new View.OnClickListener() {
                        @Override

                        public void onClick(View v) {

                            Intent intent = new Intent(AdoptionDemoPetList.this, DashboardActivity.class);
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

                    Intent intent2 = new Intent(AdoptionDemoPetList.this, AdoptPetDetails.class);
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
                        progress = new ProgressDialog(AdoptionDemoPetList.this);
                        progress.setMessage("Please wait...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        // progress.setCancelable(false);
                        progress.show();

                        //   getAdoptPetsListFromCurrentLocation(mTvSearch.getText().toString(), search_type);

                        getListByEnterCity(mTvSearch.getText().toString(), 1);

                    } else {
                        Toast.makeText(AdoptionDemoPetList.this, "Please enter search text.", Toast.LENGTH_SHORT).show();
                    }


                }
            });

            iv_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (userID != null) {
                        popupwindow1(view);

                    } else {
                        popupwindow2(view);
                    }

                }
            });
            iv_dog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mTvSearch.getText().toString().length() != 0) {
                        if (radioGroup23.getCheckedRadioButtonId() == -1) {
                            getAdoptPetsListDog(mTvSearch.getText().toString(), "Dog", 1);
                        } else {
                            if (rb.getText().toString().equalsIgnoreCase("All")) {
                                getAdoptPetsListDog(mTvSearch.getText().toString(), "Dog", 1);
                            } else {
                                getAdoptPetsListDogAll(mTvSearch.getText().toString(), search_type, "Dog", 1);

                            }

                        }
                    } else {
                        getAdoptPetsListDog(city, "Dog", 1);

                    }
                }
            });
            iv_cat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // popupwindow(view);
                    if (mTvSearch.getText().toString().length() != 0) {
                        if (radioGroup23.getCheckedRadioButtonId() == -1) {
                            getAdoptPetsListCat(mTvSearch.getText().toString(), "Cat", 1);
                        } else {
                            if (rb.getText().toString().equalsIgnoreCase("All")) {
                                getAdoptPetsListCat(mTvSearch.getText().toString(), "Cat", 1);
                            } else {
                                getAdoptPetsListCatAll(mTvSearch.getText().toString(), search_type, "Cat", 1);

                            }
                        }
                    } else {
                        getAdoptPetsListCat(city, "Cat", 1);

                    }
                }
            });
            mBtnBuyOrSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  /*  if (userID != null) {*/
                    Intent intent = new Intent(AdoptionDemoPetList.this, AddAPetActivity.class);
                    startActivity(intent);
                   /* } else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

                                        Intent intent = new Intent(AdoptionDemoPetList.this, LoginActivity.class);
                                        intent.putExtra("key", "AdoptionPetList");
                                        startActivity(intent);


                                    case DialogInterface.BUTTON_NEGATIVE:

                                        // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdoptionDemoPetList.this);
                        builder.setTitle("Message");
                        builder.setCancelable(false);
                        builder.setMessage("Please Login ").setPositiveButton("Login", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

                    }*/

                }
            });
            myLocationListener = new MyLocationListener(AdoptionDemoPetList.this);
            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), AdoptionDemoPetList.this)) {
                fetchLocationData();
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), AdoptionDemoPetList.this);
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

                        getListByEnterCity(mTvSearch.getText().toString(), 1);

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

    public void getAdoptPetsListDog(final String location, final String s_type, int count) {
        counttotalAnimal = count;
        progress.show();
        position_to_scroll1 = Constants.pets_animal2.size();
        try {
            String url = WebURL.SEARCH_ADAPTION_CITYNANIMAL_PAGE + "keyword=" + location + "&type=" + s_type + "&currentpage=" + counttotalAnimal;
            ;


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
                        int totalcount = response.getInt("totalpages");
                        if (totalcount == counttotalAnimal) {
                            loadmore.setVisibility(View.GONE);
                        } else {
                            loadmore.setVisibility(View.VISIBLE);
                        }

                        if (flag == 1) {
                            JSONArray jsonArray = response.getJSONArray("response");
                            myList1 = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Constants.pets_animal2.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());

                            }


                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animal2);
                            eLView.setAdapter(adoptPetAdaptor);
                            eLView.setSelection(position_to_scroll1 - 1);
                            eLView.smoothScrollToPosition(position_to_scroll1 - 1);
                            counttotalAnimal = counttotalAnimal + 1;

                            loadmore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {

                                    getAdoptPetsListDog(location, s_type, counttotalAnimal);
                                }
                            });
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionDemoPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();

                            loadmore.setVisibility(View.GONE);
                            Constants.pets_animal2 = new ArrayList<>();
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animal2);
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

    public void getAdoptPetsListDogAll(final String location, final String s_type, final String animal, int count) {
        counttotalAnimalAll = count;
        progress.show();
        position_to_scrollAll = Constants.pets_animal2All.size();
        try {
            String url = WebURL.SERACH_ADAPTION_ALLCRITERIA + "keyword=" + location + "&type=" + s_type + "&animal=" + animal + "&currentpage=" + counttotalAnimalAll;
            ;


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
                        int totalcount = response.getInt("totalpages");
                        if (totalcount == counttotalAnimalAll) {
                            loadmore.setVisibility(View.GONE);
                        } else {
                            loadmore.setVisibility(View.VISIBLE);
                        }

                        if (flag == 1) {
                            JSONArray jsonArray = response.getJSONArray("response");
                            myList1 = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Constants.pets_animal2All.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());

                            }


                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animal2All);
                            eLView.setAdapter(adoptPetAdaptor);
                            eLView.setSelection(position_to_scrollAll - 1);
                            eLView.smoothScrollToPosition(position_to_scrollAll - 1);
                            counttotalAnimalAll = counttotalAnimalAll + 1;

                            loadmore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {

                                    getAdoptPetsListDogAll(location, s_type, animal, counttotalAnimalAll);
                                }
                            });
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionDemoPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();

                            loadmore.setVisibility(View.GONE);
                            Constants.pets_animal2All = new ArrayList<>();
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animal2All);
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

    public void getAdoptPetsListCat(final String location, final String s_type, int count) {
        counttotalAnimalCat = count;
        progress.show();
        position_to_scrollCat = Constants.pets_animalCat2.size();
        try {
            String url = WebURL.SEARCH_ADAPTION_CITYNANIMAL_PAGE + "keyword=" + location + "&type=" + s_type + "&currentpage=" + counttotalAnimalCat;
            ;


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
                        int totalcount = response.getInt("totalpages");
                        if (totalcount == counttotalAnimalCat) {
                            loadmore.setVisibility(View.GONE);
                        } else {
                            loadmore.setVisibility(View.VISIBLE);
                        }

                        if (flag == 1) {
                            JSONArray jsonArray = response.getJSONArray("response");
                            myList1 = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Constants.pets_animalCat2.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());

                            }


                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animalCat2);
                            eLView.setAdapter(adoptPetAdaptor);
                            eLView.setSelection(position_to_scrollCat - 1);
                            eLView.smoothScrollToPosition(position_to_scrollCat - 1);
                            counttotalAnimalCat = counttotalAnimalCat + 1;

                            loadmore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {

                                    getAdoptPetsListCat(location, s_type, counttotalAnimalCat);
                                }
                            });
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionDemoPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();

                            loadmore.setVisibility(View.GONE);
                            Constants.pets_animalCat2 = new ArrayList<>();
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animalCat2);
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

    public void getAdoptPetsListCatAll(final String location, final String s_type, final String animal, int count) {
        counttotalAnimalCatAll = count;
        progress.show();
        position_to_scrollCat1 = Constants.pets_animalCat2All.size();
        try {
            String url = WebURL.SERACH_ADAPTION_ALLCRITERIA + "keyword=" + location + "&type=" + s_type + "&animal=" + animal + "&currentpage=" + counttotalAnimalCatAll;
            ;


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
                        int totalcount = response.getInt("totalpages");
                        if (totalcount == counttotalAnimalCatAll) {
                            loadmore.setVisibility(View.GONE);
                        } else {
                            loadmore.setVisibility(View.VISIBLE);
                        }

                        if (flag == 1) {
                            JSONArray jsonArray = response.getJSONArray("response");
                            myList1 = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Constants.pets_animalCat2All.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());

                            }


                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animalCat2All);
                            eLView.setAdapter(adoptPetAdaptor);
                            eLView.setSelection(position_to_scrollCat1 - 1);
                            eLView.smoothScrollToPosition(position_to_scrollCat1 - 1);
                            counttotalAnimalCatAll = counttotalAnimalCatAll + 1;

                            loadmore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {

                                    getAdoptPetsListCatAll(location, s_type, animal, counttotalAnimalCatAll);
                                }
                            });
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionDemoPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();

                            loadmore.setVisibility(View.GONE);
                            Constants.pets_animalCat2All = new ArrayList<>();
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animalCat2All);
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

    public void getAdoptPetsListTypeLooking(final String location, final String s_type, int count) {
        counttotalType = count;
        position_to_scrollBuy = Constants.pets_animallooking.size();
        progress.show();
        try {
            String url = WebURL.SEARCH_ADAPTION_CITYNTYPE_PAGE + "keyword=" + location + "&type=" + s_type + "&currentpage=" + counttotalType;
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
                        int totalcount = response.getInt("totalpages");
                        if (totalcount == counttotalType) {
                            loadmore.setVisibility(View.GONE);
                        } else {
                            loadmore.setVisibility(View.VISIBLE);
                        }

                        if (flag == 1) {
                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Constants.pets_animallooking.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());
                            }


                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animallooking);
                            eLView.setAdapter(adoptPetAdaptor);

                            System.out.println("position_to_scroll" + position_to_scrollBuy);

                            eLView.setSelection(position_to_scrollBuy - 1);
                            eLView.smoothScrollToPosition(position_to_scrollBuy - 1);
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);
                            counttotalType = counttotalType + 1;

                            loadmore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {

                                    //   Toast.makeText(PetListActivity.this, ""+ eLView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                                    // getAll_Pets_service(eLView.getLastVisiblePosition() + 10);
                                    getAdoptPetsListTypeLooking(location, s_type, counttotalType);
                                }
                            });

                        } else {
                            Toast.makeText(AdoptionDemoPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();

                            loadmore.setVisibility(View.GONE);
                            Constants.pets_animallooking = new ArrayList<>();
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animallooking);
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

    public void getAdoptPetsListTypeOffering(final String location, final String s_type, int count) {
        counttotalTypeOffer = count;
        position_to_scrollSale = Constants.pets_animalOffering.size();
        progress.show();
        try {
            String url = WebURL.SEARCH_ADAPTION_CITYNTYPE_PAGE + "keyword=" + location + "&type=" + s_type + "&currentpage=" + counttotalTypeOffer;
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
                        int totalcount = response.getInt("totalpages");
                        if (totalcount == counttotalTypeOffer) {
                            loadmore.setVisibility(View.GONE);
                        } else {
                            loadmore.setVisibility(View.VISIBLE);
                        }

                        if (flag == 1) {
                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Constants.pets_animalOffering.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());
                            }


                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animalOffering);
                            eLView.setAdapter(adoptPetAdaptor);

                            System.out.println("position_to_scroll" + position_to_scrollSale);

                            eLView.setSelection(position_to_scrollSale - 1);
                            eLView.smoothScrollToPosition(position_to_scrollSale - 1);
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);
                            counttotalTypeOffer = counttotalTypeOffer + 1;

                            loadmore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {

                                    //   Toast.makeText(PetListActivity.this, ""+ eLView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                                    // getAll_Pets_service(eLView.getLastVisiblePosition() + 10);
                                    getAdoptPetsListTypeOffering(location, s_type, counttotalTypeOffer);
                                }
                            });

                        } else {
                            Toast.makeText(AdoptionDemoPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();

                            loadmore.setVisibility(View.GONE);
                            Constants.pets_animalOffering = new ArrayList<>();
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animalOffering);
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
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());

                            }
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, adoptPetlist);
                            //	note : this should come next to loading view
                            eLView.setAdapter(adoptPetAdaptor);
                            //  adoptPetAdaptor = new AdoptPetAdaptor(AdoptionPetList.this, adoptPetlist);
                            //    listView.setAdapter(adoptPetAdaptor);

                            loadmore.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(AdoptionDemoPetList.this, "Record Not Found", Toast.LENGTH_SHORT).show();
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
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
                                System.out.println("Date...." + jsonObject.getString("adoption_date").toString());
                            }
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, adoptPetlist);
                            //    listView.setAdapter(adoptPetAdaptor);
                            eLView.setAdapter(adoptPetAdaptor);
                            flag = flag + 1;
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionDemoPetList.this, "Record Not Found", Toast.LENGTH_SHORT).show();
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
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
                                System.out.println("Date...." + jsonObject.getString("adoption_date").toString());
                            }
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, adoptPetlist);
                            //    listView.setAdapter(adoptPetAdaptor);
                            eLView.setAdapter(adoptPetAdaptor);
                            flag = flag + 1;
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionDemoPetList.this, "Record Not Found", Toast.LENGTH_SHORT).show();
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
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
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
                            Toast.makeText(AdoptionDemoPetList.this, "No more Record found", Toast.LENGTH_SHORT).show();
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


        PopupMenu popup = new PopupMenu(AdoptionDemoPetList.this, view);
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
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());

                            }
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, adoptPetlist);
                            //    listView.setAdapter(adoptPetAdaptor);
                            eLView.setAdapter(adoptPetAdaptor);
                            flag = flag + 1;
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionDemoPetList.this, "Record Not Found", Toast.LENGTH_SHORT).show();
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

        Intent intent = new Intent(AdoptionDemoPetList.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    public void popupwindow1(View view) {


        PopupMenu popup = new PopupMenu(AdoptionDemoPetList.this, view);

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

                } else if (item.getTitle().toString().equals("My Listing")) {
                    Intent intent = new Intent(AdoptionDemoPetList.this, MyPetListAdaptioActivity.class);
                    startActivity(intent);
                } else if (item.getTitle().toString().equals("Favourite")) {
                    Intent intent = new Intent(AdoptionDemoPetList.this, ViewPagerActivity.class);
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

        Geocoder geocoder = new Geocoder(AdoptionDemoPetList.this, Locale.getDefault());
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
                getListByCity(addresses.get(0).getLocality(), 1);

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

    public void getListByCity(String location, int count) {
        progress.show();

        counttoatal = count;
        position_to_scroll = Constants.pets_list2.size();
        try {
            String url = WebURL.SEARCH_ADAPTION_CITYNEAR_PAGE + "keyword=" + location + "&currentpage=" + counttoatal;

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
                        int totalcount = response.getInt("totalpages");
                        System.out.println("Flag...." + flag);

                        if (totalcount == counttoatal) {
                            loadmore.setVisibility(View.GONE);
                        } else {
                            loadmore.setVisibility(View.VISIBLE);
                        }

                        if (flag == 1) {

                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Constants.pets_list2.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());
                                System.out.println("Email...." + jsonObject.getString("email").toString());

                            }


                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_list2);
                            eLView.setAdapter(adoptPetAdaptor);
                            eLView.setSelection(position_to_scroll - 1);
                            eLView.smoothScrollToPosition(position_to_scroll - 1);

                            counttoatal = counttoatal + 1;
                            loadmore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {

                                    //   Toast.makeText(PetListActivity.this, ""+ eLView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                                    // getAll_Pets_service(eLView.getLastVisiblePosition() + 10);
                                    getListByCity(city, counttoatal);
                                }
                            });
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionDemoPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();

                            loadmore.setVisibility(View.GONE);
                            Constants.pets_list2 = new ArrayList<>();
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_list2);
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

    public void getListByEnterCity(final String location, int count) {
        counttotalKeyword = count;
        progress.show();
        position_to_scrollText = Constants.pets_animalText2.size();
        try {
            String url = WebURL.SEARCH_ADAPTION_CITYONLY_PAGE + "keyword=" + location + "&currentpage=" + counttotalKeyword;
            ;


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
                            int totalcount = response.getInt("totalpages");
                            if (totalcount == counttotalKeyword) {
                                loadmore.setVisibility(View.GONE);
                            } else {
                                loadmore.setVisibility(View.VISIBLE);
                            }
                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Constants.pets_animalText2.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
                                System.out.println("Address...." + jsonObject.getString("address").toString());

                            }


                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animalText2);
                            eLView.setAdapter(adoptPetAdaptor);
                            eLView.setSelection(position_to_scrollText - 1);
                            eLView.smoothScrollToPosition(position_to_scrollText - 1);
                            counttotalKeyword = counttotalKeyword + 1;

                            loadmore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {

                                    getListByEnterCity(location, counttotalKeyword);
                                }
                            });

                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(AdoptionDemoPetList.this, "Record not found.", Toast.LENGTH_SHORT).show();

                            loadmore.setVisibility(View.GONE);
                            Constants.pets_animalText2 = new ArrayList<>();
                            adoptPetAdaptor = new AdoptPetAdaptor(AdoptionDemoPetList.this, Constants.pets_animalText2);
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

    public void popupwindow2(View view) {


        PopupMenu popup = new PopupMenu(AdoptionDemoPetList.this, view);

        popup.getMenuInflater().inflate(R.menu.popup_mymenuone, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equals("Login")) {
                    Intent intent = new Intent(AdoptionDemoPetList.this, LoginActivity.class);
                    intent.putExtra("key", "AddAPetActivity");
                    startActivity(intent);

                }
                return true;
            }
        });


        popup.show();//showing popup menu
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
