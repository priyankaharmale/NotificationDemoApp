package com.hnweb.clawpal.BuyorSale.activity;

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
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hnweb.clawpal.BuyorSale.adaptor.Pet_list_Adaptor;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.DashboardActivity;
import com.hnweb.clawpal.GPSTracker;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.Utils.Constants;
import com.hnweb.clawpal.Utils.RowItem;
import com.hnweb.clawpal.WebUrl.WebURL;
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
 * Created by HNWeb-11 on 7/22/2016.
 */

public class PetListDemoActivity extends LocationSet implements LocationListener, TextWatcher {
    private static final int ITEMS_PER_REQUEST = 50;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    ImageView mIvBuySale, mIvAdoption, mIvLostFound;
    Toolbar toolbar;
    TextView mTvTitle;
    String city;
    Intent intent;
    int counttoatal = 0;
    int position_to_scroll;
    int position_to_scroll1;
    int position_to_scrollCat;
    int position_to_scrollBuy;
    int position_to_scrollSale;
    int position_to_scrollText;
    int position_to_scrollTextAll;
    int position_to_scrollTextAllCAT;
    int counttotalType = 0;
    int counttotalTypeSale = 0;
    int counttotalAnimal = 0;
    int counttotalALL = 0;
    int counttotalALLCat = 0;
    int counttotalAnimalCat = 0;
    int counttotalKeyword = 0;
    ProgressDialog progress;
    Button mBtnBuyOrSale;
    ListView eLView;
    int flag = 1;
    MyLocationListener myLocationListener;
    double latitude = 0.0d;
    double longitude = 0.0d;
    EditText mTvSearch;


    ArrayList<All_Pets_List_Model> myList1;
    ArrayList<All_Pets_List_Model> myList2;

    Pet_list_Adaptor pet_list_adaptor;
    boolean isconnected = false;
    String locationAddress;
    GPSTracker gpsTracker;
    ImageView iv_search, iv_dog, iv_cat;
    SharedPreferences pref;
    Double lat, logit;
    ListView listView2;

    List<RowItem> rowItems;
    String title;

    Button loadmore;
    String search_type = "";
    //it will tell us weather to load more items or not
    boolean loadingMore = false;
    LinearLayout linearLayout, ll_top2, ll_top23;
    String userID;
    ImageView iv_setting;
    RadioButton rb;
    private int mult = 1;
    private RadioGroup radioGroup, radioGroup2, radioGroup23;

    // GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    /*private Runnable returnRes = new Runnable() {

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
    };*/

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        try {
            setContentView(R.layout.activity_pet_list);
            getInit();
            pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            userID = pref.getString("userid", null);
            progress = new ProgressDialog(PetListDemoActivity.this);
            progress.setMessage("Please wait...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
            eLView = (ListView) findViewById(R.id.myListView);
            View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_view, null, false);
            eLView.addFooterView(footerView);
            loadmore = (Button) footerView.findViewById(R.id.button);
            iv_setting = (ImageView) findViewById(R.id.iv_setting);

            Constants.pets_list = new ArrayList<>();
            Constants.pets_animal = new ArrayList<>();
            Constants.pets_animalCat = new ArrayList<>();
            Constants.pets_animalSale = new ArrayList<>();
            Constants.pets_animalBuy = new ArrayList<>();
            Constants.pets_animalText = new ArrayList<>();
            Constants.pets_animalTextALL = new ArrayList<>();
            Constants.pets_animalTextALLCAT = new ArrayList<>();

      /*      eLView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    All_Pets_List_Model all_pets_list_model = pet_list_adaptor.getItem(position);
                    Intent intent2 = new Intent(PetListDemoActivity.this, PetDetails.class);

                    Bundle bundleObject = new Bundle();

                    intent2.putExtras(bundleObject);
                    startActivity(intent2);

                }
            });*/


            loadmore.setVisibility(View.GONE);
           /* loadmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    //   Toast.makeText(PetListActivity.this, ""+ eLView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                    // getAll_Pets_service(eLView.getLastVisiblePosition() + 10);
                    next_record_service(flag);
                }
            });*/

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
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            mTvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbar.setNavigationIcon(R.drawable.back_btn_img);

            myLocationListener = new MyLocationListener(PetListDemoActivity.this);
            if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), PetListDemoActivity.this)) {
                fetchLocationData();
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), PetListDemoActivity.this);
            }
            intent = getIntent();
            title = intent.getStringExtra("title");
            mTvTitle.setText(title);
            iv_search = (ImageView) findViewById(R.id.iv_search);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

           /* pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, pets_list);
            eLView.setAdapter(pet_list_adaptor);*/
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
                    rb = (RadioButton) group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {
                        //mTvSearch.setText("");
                        search_type = rb.getText().toString();
                        loadmore.setVisibility(View.GONE);

                        progress = new ProgressDialog(PetListDemoActivity.this);
                        progress.setMessage("Please wait");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        progress.show();

                        if (rb.getText().toString().equalsIgnoreCase("Looking to Buy")) {

                            if (mTvSearch.getText().toString().length() != 0) {
                                search_type = "Buy";
                                getListByTypeBuy(mTvSearch.getText().toString(), search_type, 1);
                            } else {
                                search_type = "Buy";
                                getListByTypeBuy(city, search_type, 1);
                            }

                        } else if (rb.getText().toString().equalsIgnoreCase("Pet for Sale")) {
                            if (mTvSearch.getText().toString().length() != 0) {
                                search_type = "Sale";
                                getListByTypeSale(mTvSearch.getText().toString(), search_type, 1);
                            } else {
                                search_type = "Sale";
                                getListByTypeSale(city, search_type, 1);
                            }

                        } else if (rb.getText().toString().equalsIgnoreCase("All")) {
                            Constants.pets_list = new ArrayList<>();
                            getDataByCityLocation(city, 1);
                        }

                    }

                }
            });

            iv_dog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTvSearch.getText().toString().length() != 0) {
                        if (radioGroup.getCheckedRadioButtonId() == -1) {

                            getList(mTvSearch.getText().toString(), "Dog", 1);

                        } else {
                            if (rb.getText().toString().equalsIgnoreCase("All")) {
                                getList(mTvSearch.getText().toString(), "Dog", 1);
                            } else {
                                getListAllSearch(mTvSearch.getText().toString(), search_type, "Dog", 1);
                            }
                        }

                    } else {
                        getList(city, "Dog", 1);
                    }


                }
            });
            iv_cat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mTvSearch.getText().toString().length() != 0) {


                        if (radioGroup.getCheckedRadioButtonId() == -1) {
                            getListCat(mTvSearch.getText().toString(), "Cat", 1);
                        } else {
                            if (rb.getText().toString().equalsIgnoreCase("All")) {
                                getListCat(mTvSearch.getText().toString(), "Cat", 1);
                            }else
                            {
                                getListAllSearchCat(mTvSearch.getText().toString(), search_type, "Cat", 1);

                            }
                        }

                    } else {
                        getListCat(city, "Cat", 1);

                    }
                    // popupwindow(view);
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
                        getDataByCityLocationEnter(mTvSearch.getText().toString(), 1);

                    } else {
                        Toast.makeText(PetListDemoActivity.this, "Please enter search text.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
           /* radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
            radioGroup2.clearCheck();
            radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    if (null != rb && checkedId > -1) {
                        // Toast.makeText(PetListActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                       *//* progress = new ProgressDialog(PetListActivity.this);
                        progress.setMessage("Please wait...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        progress.setCancelable(false);
                        progress.show();*//*
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
                        progress = new ProgressDialog(PetListDemoActivity.this);
                        progress.setMessage("Please wait...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        progress.setCancelable(false);
                        progress.show();
                        getBuy_Sale_Pets_List(rb.getText().toString());
                    }

                }
            });*/
            toolbar.setNavigationOnClickListener(

                    new View.OnClickListener() {
                        @Override

                        public void onClick(View v) {

                            Intent intent = new Intent(PetListDemoActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }

            );


            mBtnBuyOrSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //  if (userID != null) {
                    Intent intent = new Intent(PetListDemoActivity.this, BuyOrSalePetsActivity.class);
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

                        getDataByCityLocationEnter(mTvSearch.getText().toString(), 1);

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


    public void getList(final String location, final String s_type, final int count) {
        counttotalAnimal = count;
        String url = WebURL.SEARCH_BUYSALE_CITYNANIMAL_PAGE + "keyword=" + location + "&type=" + s_type + "&currentpage=" + counttotalAnimal;
        ;
        position_to_scroll1 = Constants.pets_animal.size();
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
                            int totalcount = j.getInt("totalpages");
                            if (totalcount == count) {
                                loadmore.setVisibility(View.GONE);
                            } else {
                                loadmore.setVisibility(View.VISIBLE);
                            }


                            if (flag == 1) {
//                          myList1 = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int counttoatal = 0; counttoatal < jsonArray.length(); counttoatal++) {

                                    System.out.println("counttoatalcounttoatal" + counttoatal);
                                    JSONObject jsonObject = jsonArray.getJSONObject(counttoatal);
                                    Constants.pets_animal.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                            jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                            jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                            jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                            jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                            jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                            jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("state"), jsonObject.getString("country"), jsonObject.getString("zip")));

//                                    Constants.pets_list.add(Constants.pets_list.get(counttoatal));

                                }

                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animal);
                                System.out.println("position_to_scroll" + position_to_scroll1);
                                eLView.setAdapter(pet_list_adaptor);
                                eLView.setSelection(position_to_scroll1 - 1);
                                eLView.smoothScrollToPosition(position_to_scroll1 - 1);


                                counttotalAnimal = counttotalAnimal + 1;

                                loadmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        getList(location, s_type, counttotalAnimal);
                                    }
                                });
                            } else {
                                loadmore.setVisibility(View.GONE);
                                Constants.pets_animal = new ArrayList<>();
                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animal);
                                eLView.setAdapter(pet_list_adaptor);
                                Toast.makeText(PetListDemoActivity.this, "No record found.", Toast.LENGTH_SHORT).show();


                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PetListDemoActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void getListAllSearch(final String location, final String s_type, final String animal, final int count) {
        counttotalALL = count;
        System.out.println("Dog...");
        String url = WebURL.SERACH_BUYSALE_ALLCRITERIA + "keyword=" + location + "&type=" + s_type + "&animal=" + animal + "&currentpage=" + counttotalALL;
        ;
        position_to_scrollTextAll = Constants.pets_animalTextALL.size();
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
                            int totalcount = j.getInt("totalpages");
                            if (totalcount == count) {
                                loadmore.setVisibility(View.GONE);
                            } else {
                                loadmore.setVisibility(View.VISIBLE);
                            }


                            if (flag == 1) {
//                          myList1 = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int counttoatal = 0; counttoatal < jsonArray.length(); counttoatal++) {

                                    System.out.println("counttoatalcounttoatal" + counttoatal);
                                    JSONObject jsonObject = jsonArray.getJSONObject(counttoatal);
                                    Constants.pets_animalTextALL.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                            jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                            jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                            jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                            jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                            jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                            jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("state"), jsonObject.getString("country"), jsonObject.getString("zip")));

//                                    Constants.pets_list.add(Constants.pets_list.get(counttoatal));

                                }

                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animalTextALL);
                                System.out.println("position_to_scroll" + position_to_scrollTextAll);
                                eLView.setAdapter(pet_list_adaptor);
                                eLView.setSelection(position_to_scrollTextAll - 1);
                                eLView.smoothScrollToPosition(position_to_scrollTextAll - 1);


                                counttotalALL = counttotalALL + 1;

                                loadmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        getListAllSearch(location, s_type, animal, counttotalALL);
                                    }
                                });
                            } else {
                                loadmore.setVisibility(View.GONE);
                                Constants.pets_animalTextALL = new ArrayList<>();
                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animalTextALL);
                                eLView.setAdapter(pet_list_adaptor);
                                Toast.makeText(PetListDemoActivity.this, "No record found.", Toast.LENGTH_SHORT).show();


                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PetListDemoActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void getListAllSearchCat(final String location, final String s_type, final String animal, final int count) {
        counttotalALLCat = count;
        String url = WebURL.SERACH_BUYSALE_ALLCRITERIA + "keyword=" + location + "&type=" + s_type + "&animal=" + animal + "&currentpage=" + counttotalALLCat;
        ;
        position_to_scrollTextAllCAT = Constants.pets_animalTextALLCAT.size();
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
                            int totalcount = j.getInt("totalpages");
                            if (totalcount == count) {
                                loadmore.setVisibility(View.GONE);
                            } else {
                                loadmore.setVisibility(View.VISIBLE);
                            }


                            if (flag == 1) {
//                          myList1 = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int counttoatal = 0; counttoatal < jsonArray.length(); counttoatal++) {

                                    System.out.println("counttoatalcounttoatal" + counttoatal);
                                    JSONObject jsonObject = jsonArray.getJSONObject(counttoatal);
                                    Constants.pets_animalTextALLCAT.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                            jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                            jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                            jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                            jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                            jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                            jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("state"), jsonObject.getString("country"), jsonObject.getString("zip")));

//                                    Constants.pets_list.add(Constants.pets_list.get(counttoatal));

                                }

                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animalTextALLCAT);
                                System.out.println("position_to_scroll" + position_to_scrollTextAllCAT);
                                eLView.setAdapter(pet_list_adaptor);
                                eLView.setSelection(position_to_scrollTextAllCAT - 1);
                                eLView.smoothScrollToPosition(position_to_scrollTextAllCAT - 1);


                                counttotalALLCat = counttotalALLCat + 1;

                                loadmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        getListAllSearchCat(location, s_type, animal, counttotalALLCat);
                                    }
                                });
                            } else {
                                loadmore.setVisibility(View.GONE);
                                Constants.pets_animalTextALLCAT = new ArrayList<>();
                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animalTextALLCAT);
                                eLView.setAdapter(pet_list_adaptor);
                                Toast.makeText(PetListDemoActivity.this, "No record found.", Toast.LENGTH_SHORT).show();


                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PetListDemoActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void getListCat(final String location, final String s_type, final int count) {
        counttotalAnimalCat = count;

        String url = WebURL.SEARCH_BUYSALE_CITYNANIMAL_PAGE + "keyword=" + location + "&type=" + s_type + "&currentpage=" + counttotalAnimalCat;
        ;
        System.out.println("URL....." + url);
        position_to_scrollCat = Constants.pets_animalCat.size();

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
                            int totalcount = j.getInt("totalpages");
                            if (totalcount == counttotalAnimalCat) {
                                loadmore.setVisibility(View.GONE);
                            } else {
                                loadmore.setVisibility(View.VISIBLE);
                            }

                            if (flag == 1) {
//                          myList1 = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int counttoatal = 0; counttoatal < jsonArray.length(); counttoatal++) {

                                    System.out.println("counttoatalcounttoatal" + counttoatal);
                                    JSONObject jsonObject = jsonArray.getJSONObject(counttoatal);
                                    Constants.pets_animalCat.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                            jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                            jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                            jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                            jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                            jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                            jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("state"), jsonObject.getString("country"), jsonObject.getString("zip")));

//                                    Constants.pets_list.add(Constants.pets_list.get(counttoatal));

                                }


                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animalCat);

                                System.out.println("position_to_scroll" + position_to_scrollCat);
                                eLView.setAdapter(pet_list_adaptor);
                                eLView.setSelection(position_to_scrollCat - 1);
                                eLView.smoothScrollToPosition(position_to_scrollCat - 1);


                                counttotalAnimalCat = counttotalAnimalCat + 1;

                                loadmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        getListCat(location, s_type, counttotalAnimalCat);
                                    }
                                });
                            } else {
                                loadmore.setVisibility(View.GONE);
                                Constants.pets_animalCat = new ArrayList<>();
                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animalCat);
                                eLView.setAdapter(pet_list_adaptor);
                                Toast.makeText(PetListDemoActivity.this, "No record found.", Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PetListDemoActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void getListByTypeBuy(String location, final String s_type, final int count) {
        counttotalType = count;
        String url = WebURL.SEARCH_BUYSALE_CITYNTYPE_PAGE + "keyword=" + location + "&type=" + s_type + "&currentpage=" + counttotalType;

        System.out.println("URL....." + url);
        position_to_scrollBuy = Constants.pets_animalBuy.size();
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
                            int totalcount = j.getInt("totalpages");


                            if (totalcount == counttotalType) {
                                loadmore.setVisibility(View.GONE);
                            } else {
                                loadmore.setVisibility(View.VISIBLE);
                            }

                            if (flag == 1) {
                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int counttoatal = 0; counttoatal < jsonArray.length(); counttoatal++) {

                                    System.out.println("counttoatalcounttoatal" + counttoatal);
                                    JSONObject jsonObject = jsonArray.getJSONObject(counttoatal);
                                    Constants.pets_animalBuy.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                            jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                            jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                            jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                            jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                            jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                            jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("state"), jsonObject.getString("country"), jsonObject.getString("zip")));

//                                    Constants.pets_list.add(Constants.pets_list.get(counttoatal));

                                }


                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animalBuy);

                                System.out.println("position_to_scroll" + position_to_scrollBuy);
                                eLView.setAdapter(pet_list_adaptor);
                                eLView.setSelection(position_to_scrollBuy - 1);
                                eLView.smoothScrollToPosition(position_to_scrollBuy - 1);
                                counttotalType = counttotalType + 1;

                                loadmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        //   Toast.makeText(PetListActivity.this, ""+ eLView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                                        // getAll_Pets_service(eLView.getLastVisiblePosition() + 10);
                                        getListByTypeBuy(city, s_type, counttotalType);
                                    }
                                });
                            } else {
                                loadmore.setVisibility(View.GONE);
                                Constants.pets_animalBuy = new ArrayList<>();
                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animalBuy);
                                eLView.setAdapter(pet_list_adaptor);
                                Toast.makeText(PetListDemoActivity.this, "No record found.", Toast.LENGTH_SHORT).show();


                            }


                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PetListDemoActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void getListByTypeSale(String location, final String s_type, final int count) {
        counttotalTypeSale = count;
        String url = WebURL.SEARCH_BUYSALE_CITYNTYPE_PAGE + "keyword=" + location + "&type=" + s_type + "&currentpage=" + counttotalTypeSale;

        position_to_scrollSale = Constants.pets_animalSale.size();
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
                            int totalcount = j.getInt("totalpages");


                            if (totalcount == counttotalTypeSale) {
                                loadmore.setVisibility(View.GONE);
                            } else {
                                loadmore.setVisibility(View.VISIBLE);
                            }

                            if (flag == 1) {
                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int counttoatal = 0; counttoatal < jsonArray.length(); counttoatal++) {

                                    System.out.println("counttoatalcounttoatal" + counttoatal);
                                    JSONObject jsonObject = jsonArray.getJSONObject(counttoatal);
                                    Constants.pets_animalSale.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                            jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                            jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                            jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                            jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                            jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                            jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("state"), jsonObject.getString("country"), jsonObject.getString("zip")));

//                                    Constants.pets_list.add(Constants.pets_list.get(counttoatal));

                                }


                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animalSale);

                                System.out.println("position_to_scroll" + position_to_scrollSale);
                                eLView.setAdapter(pet_list_adaptor);
                                eLView.setSelection(position_to_scrollSale - 1);
                                eLView.smoothScrollToPosition(position_to_scrollSale - 1);
                                counttotalTypeSale = counttotalTypeSale + 1;

                                loadmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        //   Toast.makeText(PetListActivity.this, ""+ eLView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                                        // getAll_Pets_service(eLView.getLastVisiblePosition() + 10);
                                        getListByTypeSale(city, s_type, counttotalTypeSale);
                                    }
                                });
                            } else {
                                loadmore.setVisibility(View.GONE);
                                Constants.pets_animalBuy = new ArrayList<>();
                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animalBuy);
                                eLView.setAdapter(pet_list_adaptor);
                                Toast.makeText(PetListDemoActivity.this, "No record found.", Toast.LENGTH_SHORT).show();


                            }


                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PetListDemoActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
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


    public void popupwindow1(View view) {


        PopupMenu popup = new PopupMenu(PetListDemoActivity.this, view);

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

                } else if (item.getTitle().toString().equals("My Listing")) {
                    Intent intent = new Intent(PetListDemoActivity.this, MyPetListBuyorSaleActivity.class);
                    startActivity(intent);
                } else if (item.getTitle().toString().equals("Favourite")) {
                    Intent intent = new Intent(PetListDemoActivity.this, ViewPagerActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });


        popup.show();//showing popup menu
    }

    public void popupwindow2(View view) {


        PopupMenu popup = new PopupMenu(PetListDemoActivity.this, view);

        popup.getMenuInflater().inflate(R.menu.popup_mymenuone, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equals("Login")) {
                    Intent intent = new Intent(PetListDemoActivity.this, LoginActivity.class);
                    intent.putExtra("key", "PetListActivity");
                    startActivity(intent);

                }
                return true;
            }
        });


        popup.show();//showing popup menu
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(PetListDemoActivity.this, DashboardActivity.class);
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

        Geocoder geocoder = new Geocoder(PetListDemoActivity.this, Locale.getDefault());
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
                getDataByCityLocation(addresses.get(0).getLocality(), 1);

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

    public void getDataByCityLocation(String City, final int count) {
        counttoatal = count;
        String url = WebURL.SEARCH_BUYSALE_CITYNEAR_PAGE + "keyword=" + City + "&currentpage=" + counttoatal;
        System.out.println("URL...." + url);
        position_to_scroll = Constants.pets_list.size();
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

                            int totalcount = j.getInt("totalpages");
                            System.out.println("Flag...." + flag);

                            if (totalcount == counttoatal) {
                                loadmore.setVisibility(View.GONE);
                            } else {
                                loadmore.setVisibility(View.VISIBLE);
                            }
                            if (flag == 1) {

//                                myList1 = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                System.out.println("JSonArraysize" + jsonArray.length());
                                System.out.println("Count.." + count);
                                for (int counttoatal = 0; counttoatal < jsonArray.length(); counttoatal++) {

                                    System.out.println("counttoatalcounttoatal" + counttoatal);
                                    JSONObject jsonObject = jsonArray.getJSONObject(counttoatal);
                                    Constants.pets_list.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                            jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                            jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                            jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                            jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                            jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                            jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("state"), jsonObject.getString("country"), jsonObject.getString("zip")));

                                    System.out.println("ZipCode..." + jsonObject.getString("zip"));
                                    System.out.println("country..." + jsonObject.getString("country"));
//                                    Constants.pets_list.add(Constants.pets_list.get(counttoatal));

                                }

                                /* pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);*/

                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_list);

                                System.out.println("position_to_scroll" + position_to_scroll);
                                eLView.setAdapter(pet_list_adaptor);
                                eLView.setSelection(position_to_scroll - 1);
                                eLView.smoothScrollToPosition(position_to_scroll - 1);
//                                eLView.smoothScrollToPosition(10);
                                counttoatal = counttoatal + 1;

                                loadmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        //   Toast.makeText(PetListActivity.this, ""+ eLView.getLastVisiblePosition(), Toast.LENGTH_SHORT).show();
                                        // getAll_Pets_service(eLView.getLastVisiblePosition() + 10);
                                        getDataByCityLocation(city, counttoatal);
                                        //      eLView.smoothScrollToPosition(pets_list.size());
                                    }
                                });
                            } else {
                                loadmore.setVisibility(View.GONE);
                                Constants.pets_list = new ArrayList<>();
                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_list);
                                eLView.setAdapter(pet_list_adaptor);
                                Toast.makeText(PetListDemoActivity.this, "No record found.", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PetListDemoActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public void getDataByCityLocationEnter(final String City, final int count) {
        counttotalKeyword = count;


        position_to_scrollText = Constants.pets_animalText.size();
        progress.show();

        String url = WebURL.SEARCH_BUYSALE_CITYONLY_PAGE + "keyword=" + City + "&currentpage=" + counttotalKeyword;
        ;
        System.out.println("URL...." + url);


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

                            if (flag == 1) {
                                int totalcount = j.getInt("totalpages");
                                if (totalcount == counttotalKeyword) {
                                    loadmore.setVisibility(View.GONE);
                                } else {
                                    loadmore.setVisibility(View.VISIBLE);
                                }
                                myList1 = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                System.out.println("JSonArraysize" + jsonArray.length());
                                System.out.println("Count.." + count);
                                for (int counttoatal = 0; counttoatal < jsonArray.length(); counttoatal++) {

                                    System.out.println("counttoatalcounttoatal" + counttoatal);
                                    JSONObject jsonObject = jsonArray.getJSONObject(counttoatal);
                                    Constants.pets_animalText.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                            jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                            jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                            jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                            jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                            jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                            jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("state"), jsonObject.getString("country"), jsonObject.getString("zip")));

//                                    Constants.pets_list.add(Constants.pets_list.get(counttoatal));

                                }

                                /* pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);*/

                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animalText);

                                System.out.println("position_to_scroll" + position_to_scrollText);
                                eLView.setAdapter(pet_list_adaptor);
                                eLView.setSelection(position_to_scrollText - 1);
                                eLView.smoothScrollToPosition(position_to_scrollText - 1);
//                                eLView.smoothScrollToPosition(10);
                                counttotalKeyword = counttotalKeyword + 1;


                                loadmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {

                                        getDataByCityLocationEnter(City, counttotalKeyword);
                                    }
                                });

                            } else {
                                loadmore.setVisibility(View.GONE);
                                Constants.pets_animalText = new ArrayList<>();
                                pet_list_adaptor = new Pet_list_Adaptor(PetListDemoActivity.this, Constants.pets_animalText);
                                eLView.setAdapter(pet_list_adaptor);
                                Toast.makeText(PetListDemoActivity.this, "No record found.", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PetListDemoActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
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


}