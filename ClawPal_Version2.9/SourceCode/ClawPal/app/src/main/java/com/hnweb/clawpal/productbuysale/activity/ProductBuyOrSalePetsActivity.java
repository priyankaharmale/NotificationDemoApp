package com.hnweb.clawpal.productbuysale.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabWidget;
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
import com.google.gson.Gson;
import com.hnweb.clawpal.AppController;
import com.hnweb.clawpal.BuyorSale.activity.MyPetListBuyorSaleActivity;
import com.hnweb.clawpal.BuyorSale.activity.PetListActivity;
import com.hnweb.clawpal.BuyorSale.activity.PetListDemoActivity;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.CustomClass.InAppCreateActivity;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.Utils.ValidationMethods;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.login.LoginActivity;
import com.hnweb.clawpal.util.AppConstant;
import com.hnweb.clawpal.util.IabBroadcastReceiver;
import com.hnweb.clawpal.util.IabHelper;
import com.hnweb.clawpal.util.IabResult;
import com.hnweb.clawpal.util.Inventory;
import com.hnweb.clawpal.util.Purchase;
import com.hnweb.clawpal.util.Utilities;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HNWeb-11 on 7/27/2016.
 */
public class ProductBuyOrSalePetsActivity extends AppCompatActivity {
    static final String SKU_PLAN1 = "plan1";
    static final String SKU_PLAN2 = "plan2";
    static final int RC_REQUEST = 10001;
    final String[] items = {"Dog", "Cat"};
    String user_Id;
    boolean mIsPremium = false;
    int mTank;
    // The helper object
    IabHelper mHelper;
    String paymentId;
    IabBroadcastReceiver mBroadcastReceiver;
    Toolbar toolbar;
    String selectedPetType = "Dog";
    Button mBtnPetType, mBtnBreedType, mBtnGender, mBtnAge, mBtnNeuture, mBtnVaccinated, mBtnNext;
    EditText mEtPrice, mEtDescription, mEtCurrentAddress, mEtName, mEtNumber, mEtCity, mEtTitle, mEtemailId;
    TabHost host;
    ImageView mIvplus, mIvLogout;
    ProgressDialog progress;
    ScrollView mSvParrent, mScChild;
    Uri selectedImage;
    InputStream is;
    String pettype, breedtype, gender, age, natuered, vaccinated, price, type, desc, locality, c_add, city, name, number, userEmail, title;
    Bitmap bitmap1;
    int SELECT_FILE = 1;
    int REQUEST_CAMERA = 0;
    String userChoosenTask;
    ImageView mIvPetImage;
    ImageView mIvfirst, mIvSec, mIvThird;
    SharedPreferences prefs;
    String currentadd, petimage;
    RadioGroup radioGroup;
    RadioButton radioButtonBuy, radioButtonSale;
    String flag;
    int flag1;
    RadioButton radioButton1, radioButton2;
    String updated_flag = "0";
    String set_image_flag = "1";
    ImageView mIvDeleteFirst, iv_setting;
    int image_flag = 1;
    TextView tv_profile;
    String callfrom;
    InAppCreateActivity inAppCreateActivity;
    String mypetbuysaledetails;
    All_Pets_List_Model all_pets_list_model;
    String messages;
    private TimePickerDialog.OnTimeSetListener timePickerListener;
    private String TAG = LoginActivity.class.getSimpleName();

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_addproductbuysale);
        prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
        currentadd = prefs.getString("Current_add", null);
        userEmail = prefs.getString("user_email", null);
        tv_profile = (TextView) findViewById(R.id.profile);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);
        getInit();
        SetListener();
        setTabsView();
        SetListener();
        inAppCreateActivity = new InAppCreateActivity();
        mBtnPetType.setText(items[0].toString());
        mBtnBreedType.setText("Akita");
        mBtnGender.setText("Male");
        mBtnAge.setText("Puppies/Kittens");
        mBtnNeuture.setText("Yes");
        mBtnVaccinated.setText("Yes");
        ProductBuyOrSalePetsActivity.this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            callfrom = extras.getString("callfrom");
            Gson gson = new Gson();

            mypetbuysaledetails = extras.getString(AppConstant.MYPETLIST.toString(), null);

            all_pets_list_model = gson.fromJson(mypetbuysaledetails, All_Pets_List_Model.class);
        }
        if (callfrom != null) {
            editDetails();
        }
        tv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductBuyOrSalePetsActivity.this, MyPetListBuyorSaleActivity.class);
                startActivity(intent);
            }
        });
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  popupwindow(view);
            }
        });
    }

    public void getInit() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        host = (TabHost) findViewById(R.id.tabHost);
        toolbar.setNavigationIcon(R.drawable.back_btn_img);
        mBtnPetType = (Button) findViewById(R.id.btn_pet_Type);
        mBtnBreedType = (Button) findViewById(R.id.btn_breed_type);
        mBtnGender = (Button) findViewById(R.id.btn_gender);
        mBtnAge = (Button) findViewById(R.id.btn_age);
        mBtnNeuture = (Button) findViewById(R.id.btn_neutured);
        mBtnVaccinated = (Button) findViewById(R.id.btn_vaccinated);
        mEtPrice = (EditText) findViewById(R.id.et_price);
        mBtnNext = (Button) findViewById(R.id.btn_next);
        // mEtDescription=(EditText)findViewById(R.id.et_desc);
        mIvplus = (ImageView) findViewById(R.id.iv_plus);
        // mSvParrent = (ScrollView) findViewById(R.id.sc_parrent);
        mScChild = (ScrollView) findViewById(R.id.sv_child);
        mIvfirst = (ImageView) findViewById(R.id.iv_pet1);
        mIvPetImage = (ImageView) findViewById(R.id.pet_image);
        mIvLogout = (ImageView) toolbar.findViewById(R.id.iv_logout);
        mEtDescription = (EditText) findViewById(R.id.et_desc);
        mEtCurrentAddress = (EditText) findViewById(R.id.et_current_address);
        mEtCurrentAddress.setText(currentadd);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtNumber = (EditText) findViewById(R.id.et_contact_number);
        mEtemailId = (EditText) findViewById(R.id.et_emailAdress);
        mEtCity = (EditText) findViewById(R.id.et_city);
        mIvSec = (ImageView) findViewById(R.id.iv_pet2);
        mIvThird = (ImageView) findViewById(R.id.iv_pet3);
        mEtTitle = (EditText) findViewById(R.id.et_title);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        mIvDeleteFirst = (ImageView) findViewById(R.id.iv_ic_delete_first);
        mIvDeleteFirst.setVisibility(View.GONE);

        mEtPrice.setVisibility(View.VISIBLE);

        if (userEmail != null) {
            mEtemailId.setText(userEmail);
        } else {
            mEtemailId.setText("");
        }
    }

    public void SetListener() {

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        finish();

                    }

                }

        );
        mIvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref;
                SharedPreferences.Editor editor;
                pref = getApplication().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                editor = pref.edit();
                editor.remove("userid");
                editor.apply();
                Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("key", "BuyOrSalePets");
                startActivity(intent1);
                finish();
                startActivity(intent1);
            }
        });
        mBtnPetType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtPrice.setVisibility(View.VISIBLE);
                petTypePopup();
            }
        });
        mBtnBreedType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petBreedTypePopup(selectedPetType);
            }
        });
        mBtnGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petGenderePopup();
            }
        });
        mBtnAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petAgePopup();
            }
        });
        mBtnNeuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petNeuturedAPopup();
            }
        });
        mBtnVaccinated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petVaccinatedPopup();
            }
        });
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtTitle.setVisibility(View.GONE);
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtTitle.setVisibility(View.VISIBLE);
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current_tab = host.getCurrentTab();

                ValidationMethods vm = new ValidationMethods();

                if (current_tab == 0) {
                    if (mEtDescription.getText().toString().equals("")) {
                        mEtDescription.setError("Please enter  Description");
                        mEtDescription.requestFocus();
                        return;
                    } else {
                        host.setCurrentTab(1);
                    }

                } else if (current_tab == 1) {

                    if (mBtnPetType.getText().toString().equals("Select Pet Type")) {
                        Toast.makeText(ProductBuyOrSalePetsActivity.this, "Please Select pet type", Toast.LENGTH_SHORT).show();

                    } else if (mBtnBreedType.getText().toString().equals("Select Breed Type")) {
                        Toast.makeText(ProductBuyOrSalePetsActivity.this, "Please Select Breed type", Toast.LENGTH_SHORT).show();

                    } else if (mBtnGender.getText().toString().equals("Select Gender")) {
                        Toast.makeText(ProductBuyOrSalePetsActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();

                    } else if (mBtnAge.getText().toString().equals("Select Age Range")) {
                        Toast.makeText(ProductBuyOrSalePetsActivity.this, "Please Select Age Range", Toast.LENGTH_SHORT).show();

                    } else if (mBtnNeuture.getText().toString().equals("Select Neutured")) {
                        Toast.makeText(ProductBuyOrSalePetsActivity.this, "Please Select Neutured", Toast.LENGTH_SHORT).show();

                    } else if (mBtnVaccinated.getText().toString().equals("Select Vaccinated")) {
                        Toast.makeText(ProductBuyOrSalePetsActivity.this, "Please Select Vaccinated", Toast.LENGTH_SHORT).show();

                    } else if (!vm.isValidLocation(mEtPrice.getText().toString())) {
                        mEtPrice.setError("Please enter  Price");
                        mEtPrice.requestFocus();
                        return;
                    } else

                    {
                        host.setCurrentTab(2);
                    }

                } else if (current_tab == 2) {

                    if (!vm.isValidName(mEtName.getText().toString())) {
                        mEtName.setError("Please enter  Name");
                        mEtName.requestFocus();
                        return;
                    } else if (!vm.isValidNumber(mEtNumber.getText().toString())) {
                        mEtNumber.setError("Please Valid  Contact Number");
                        mEtNumber.requestFocus();
                        return;
                    } else if (!vm.isValidLocation(mEtCity.getText().toString())) {
                        mEtCity.setError("Please enter  City");
                        mEtCity.requestFocus();
                        return;
                    } else if (mEtCurrentAddress.getText().toString().equals("")) {
                        mEtCurrentAddress.setError("Please enter  Description");
                        mEtCurrentAddress.requestFocus();
                        return;
                    } else {
                        host.setCurrentTab(3);
                        mBtnNext.setText("Save");
                    }
                } else {


                    System.out.println("PetImae" + petimage);
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    progress = new ProgressDialog(ProductBuyOrSalePetsActivity.this);
                                    progress.setMessage("Please wait...");
                                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progress.setIndeterminate(true);
                                    // progress.setCancelable(true);
                                    progress.show();
                                    pettype = mBtnPetType.getText().toString();
                                    breedtype = mBtnBreedType.getText().toString();
                                    gender = mBtnGender.getText().toString();
                                    age = mBtnAge.getText().toString();
                                    natuered = mBtnNeuture.getText().toString();
                                    vaccinated = mBtnVaccinated.getText().toString();
                                    price = mEtPrice.getText().toString();
                                    desc = mEtDescription.getText().toString();
                                    if (pettype.equals("Select Pet Type")) {
                                        pettype = "";
                                    }
                                    if (breedtype.equals("Select Breed Type")) {

                                        breedtype = "";
                                    }
                                    if (gender.equals("Select Gender")) {
                                        gender = "";

                                    }
                                    if (age.equals("Select Age Range")) {

                                        age = "";
                                    }
                                    if (natuered.equals("Select Neutured")) {

                                        natuered = "";
                                    }
                                    if (vaccinated.equals("Select Vaccinated")) {
                                        vaccinated = "";
                                    }

                                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                                    View radioButton = radioGroup.findViewById(radioButtonID);
                                    int idx = radioGroup.indexOfChild(radioButton);
                                    if (idx == 0) {
                                        type = "Buy";
                                        title = "";
                                    } else {
                                        type = "Sale";
                                        title = mEtTitle.getText().toString();
                                    }
                                    locality = mEtCurrentAddress.getText().toString();
                                    city = mEtCity.getText().toString();
                                    name = mEtName.getText().toString();
                                    number = mEtNumber.getText().toString();

                                    try {

                                        mIvPetImage.buildDrawingCache();
                                        Bitmap bmap = mIvPetImage.getDrawingCache();
                                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                        bmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                        byte[] bb = bos.toByteArray();
                                        petimage = Base64.encodeToString(bb, Base64.DEFAULT);
                                        System.out.println("PetImae" + petimage);
                                        System.out.println("array12" + bb[0]);
                                        //  sendData();

                                        if (callfrom != null) {
                                         //   updateDataSend();
                                        } else {
                                           // registerAsynchTask obj = new registerAsynchTask();
                                           // obj.execute();
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                case DialogInterface.BUTTON_NEGATIVE:

                                    // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductBuyOrSalePetsActivity.this);
                    //  builder.setTitle("Message");
                    builder.setCancelable(true);
                    builder.setMessage("Are you sure?").setPositiveButton("OK", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

                }


            }
        });

        mIvplus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectImage();

            }
        });


        mIvfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvPetImage.setImageDrawable(mIvfirst.getDrawable());
                updated_flag = "1";
            }
        });
        mIvSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvPetImage.setImageDrawable(mIvSec.getDrawable());
                updated_flag = "2";
            }
        });
        mIvThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvPetImage.setImageDrawable(mIvThird.getDrawable());
                updated_flag = "3";

            }
        });
        mIvDeleteFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                if (updated_flag.equals("1")) {
                                    mIvfirst.setImageResource(R.drawable.no_image);
                                    mIvPetImage.setImageResource(R.drawable.no_image);
                                    set_image_flag = "1";
                                    updated_flag = "1";
                                } else if (updated_flag.equals("2")) {
                                    mIvSec.setImageResource(R.drawable.no_image);
                                    mIvPetImage.setImageResource(R.drawable.no_image);
                                    set_image_flag = "2";
                                    updated_flag = "2";
                                } else {
                                    mIvThird.setImageResource(R.drawable.no_image);
                                    mIvPetImage.setImageResource(R.drawable.no_image);
                                    set_image_flag = "3";
                                }

                            case DialogInterface.BUTTON_NEGATIVE:

                                // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductBuyOrSalePetsActivity.this);
                //   builder.setTitle("Message");
                builder.setCancelable(true);
                builder.setMessage("Are you sure?").setPositiveButton("OK", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

            }
        });
    }


    public void sendData() {

        try {
            String url = WebURL.POST_BUY_SALE_PETS + "Image=NA&type=Buy" +
                    "&animal=" + pettype + "&gender=" + gender + "&description=" + mEtDescription.getText() + "&locality=" + mEtCurrentAddress.getText() + "&title=NA" +
                    "&city=" + mEtCity.getText() + "&age_range=" + age + "&category=NA&breed_type=" + breedtype + "&currency=" + mEtPrice.getText() +
                    "&email=NA&name=" + mEtName.getText() + "&neutered=" + natuered + "&vaccinated=" + vaccinated + "contact=" + mEtNumber.getText();
            String final_url = url.replaceAll(" ", "%20");
            Log.i("data=", url);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, final_url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                        Log.i("Responce=", response.toString());
                        JSONArray jsonArray = response.getJSONArray("response");
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

    public void setTabsView() {


        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("About");
        host.addTab(spec);

       /* //Tab 2
        spec = host.newTabSpec("Tab Two");

        spec.setContent(R.id.tab2);
        spec.setIndicator("Details");

        host.addTab(spec);*/

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Contact");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Tab Four");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Photo");
        host.addTab(spec);
        final TabWidget tw = (TabWidget) host.findViewById(android.R.id.tabs);
        for (int i = 0; i < tw.getChildCount(); ++i) {
            final View tabView = tw.getChildTabViewAt(i);
            final TextView tv = (TextView) tabView.findViewById(android.R.id.title);
            tv.setTextSize(9);

        }

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("Tab Two")) {
                    hideSoftKeyboard();
                }
                if (tabId.equals("Tab Four")) {
                    if (mEtDescription.getText().toString().equals("") || mBtnPetType.getText().toString().equals("Select Pet Type") ||
                            mBtnBreedType.getText().toString().equals("Select Breed Type") || mBtnGender.getText().toString().equals("Select Gender")
                            || mBtnAge.getText().toString().equals("Select Age Range") || mBtnNeuture.getText().toString().equals("Select Neutured")
                            || mBtnVaccinated.getText().toString().equals("Select Vaccinated") || mEtPrice.getText().toString().equals("") || mEtName.getText().toString().equals("")
                            || mEtNumber.getText().toString().equals("") || mEtCity.getText().toString().equals("") || mEtCurrentAddress.getText().toString().equals("")) {
                        mBtnNext.setVisibility(View.GONE);
                    } else {
                        mBtnNext.setVisibility(View.VISIBLE);
                        mBtnNext.setText("Save");
                    }


                } else {

                    mBtnNext.setVisibility(View.VISIBLE);
                    mBtnNext.setText("Next");
                }
                if (!tabId.equals("Tab One")) {
                    radioGroup.getChildAt(0).setEnabled(false);
                    radioGroup.getChildAt(1).setEnabled(false);
                } else {
                    radioGroup.getChildAt(0).setEnabled(true);
                    radioGroup.getChildAt(1).setEnabled(true);
                }


            }
        });

    }

    public void petTypePopup() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Pet Type ");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection


                selectedPetType = items[item].toString();
                mBtnPetType.setText(items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void petBreedTypePopup(String selectedPetType) {
        if (selectedPetType != null && !selectedPetType.isEmpty()) {
            // doSomething

            final String[] items;
            if (selectedPetType.equals("Dog")) {
                items = new String[]{
                        "Akita", "Alaskan Malamute", "American English Coonhound", "American Eskimo Dog",
                        "American Foxhound", "American Pit Bull Terrier", "American Water Spaniel", "Anatolian Shepherd Dog",
                        "Appenzeller Sennenhunde", "Australian Cattle Dog", "Australian Shepherd", "Australian Terrier", "Azawakh",
                        "Barbet", "Basenji", "Basset Hound", "Beagle", "Bearded Collie", "Bedlington Terrier", "Belgian Malinois",
                        "Belgian Sheepdog", "Belgian Tervuren", "Berger Picard", "Bernese Mountain Dog", "Bichon Frise",
                        "Black and Tan Coonhound", "Black Russian Terrier", "Bloodhound", "Bluetick Coonhound", "Bolognese",
                        "Border Collie", "Border Terrier", "Borzoi", "Boston Terrier", "Bouvier des Flandres", "Boxer", "Boykin Spaniel",
                        "Bracco Italiano", "Briard", "Brittany", "Brussels Griffon", "Bull Terrier", "Bulldog", "Bullmastiff",
                        "Cairn Terrier", "Canaan Dog", "Cane Corso", "Cardigan Welsh Corgi", "Catahoula Leopard Dog",
                        "Cavalier King Charles Spaniel", "Cesky Terrier", "Chesapeake Bay Retriever", "Chihuahua", "Chinese Crested",
                        "Chinese Shar-Pei", "Chinook", "Chow Chow", "Clumber Spaniel", "Cockapoo",
                        "Cocker Spaniel", "Collie", "Coton de Tulear", "Curly-Coated Retriever",
                        "Dachshund", "Dalmatian", "Dandie Dinmont Terrier", "Doberman Pinscher", "Dogue de Bordeaux",
                        "English Cocker Spaniel", "English Foxhound", "English Setter", "English Springer Spaniel", "English Toy Spaniel",
                        "Entlebucher Mountain Dog", "Field Spaniel", "Finnish Lapphund", "Finnish Spitz", "Flat-Coated Retriever", "Fox Terrier",
                        "French Bulldog", "German Pinscher", "German Shepherd Dog", "German Shorthaired Pointer", "German Wirehaired Pointer",
                        "Giant Schnauzer", "Glen of Imaal Terrier", "Goldador", "Golden Retriever", "Goldendoodle", "Gordon Setter",
                        "Great Dane", "Great Pyrenees", "Greater Swiss Mountain Dog", "Greyhound", "Harrier", "Havanese", "Ibizan Hound",
                        "Icelandic Sheepdog", "Irish Red and White Setter", "Irish Setter", "Irish Terrier", "Irish Water Spaniel",
                        "Irish Wolfhound", "Italian Greyhound", "Jack Russell Terrier", "Japanese Chin", "Korean Jindo Dog", "Keeshond",
                        "Kerry Blue Terrier", "Komondor", "Kooikerhondje", "Kuvasz", "Labradoodle", "Labrador Retriever", "Lakeland Terrier",
                        "Lancashire Heeler", "Leonberger", "Lhasa Apso", "Lowchen", "Maltese", "Maltese Shih Tzu", "Maltipoo",
                        "Manchester Terrier", "Mastiff", "Miniature Pinscher", "Miniature Schnauzer", "Mutt", "Neapolitan Mastiff",
                        "Newfoundland", "Norfolk Terrier", "Norwegian Buhund", "Norwegian Elkhound", "Norwegian Lundehund", "Norwich Terrier",
                        "Nova Scotia Duck Tolling Retriever", "Old English Sheepdog", "Otterhound", "Papillon", "Peekapoo", "Pekingese",
                        "Pembroke Welsh Corgi", "Petit Basset Griffon Vendeen", "Pharaoh Hound", "Plott", "Pocket Beagle", "Pointer",
                        "Polish Lowland Sheepdog", "Pomeranian", "Poodle", "Portuguese Water Dog", "Pug", "Puggle", "Puli", "Pyrenean Shepherd",
                        "Rat Terrier", "Redbone Coonhound", "Rhodesian Ridgeback", "Rottweiler", "Saint Bernard", "Saluki", "Samoyed",
                        "Schipperke", "Schnoodle", "Scottish Deerhound", "Scottish Terrier", "Sealyham Terrier", "Shetland Sheepdog",
                        "Shiba Inu", "Shih Tzu", "Siberian Husky", "Silky Terrier", "Skye Terrier", "Sloughi", "Small Munsterlander Pointer",
                        "Soft Coated Wheaten Terrier", "Stabyhoun", "Staffordshire Bull Terrier", "Standard Schnauzer", "Sussex Spaniel",
                        "Swedish Vallhund", "Tibetan Mastiff", "Tibetan Spaniel", "Tibetan Terrier", "Toy Fox Terrier",
                        "Treeing Tennessee Brindle", "Treeing Walker Coonhound", "Vizsla", "Weimaraner", "Welsh Springer Spaniel",
                        "Welsh Terrier", "West Highland White Terrier", "Whippet", "Wirehaired Pointing Griffon", "Xoloitzcuintli",
                        "Yorkipoo", "Yorkshire Terrier"
                };
            } else {
                items = new String[]{
                        "Abyssinian", "American Bobtail", "American Curl", "American Shorthair", "American Wirehair", "Balinese", "Birman",
                        "Bombay", "British Shorthair", "Burmese", "Chartreux", "Colorpoint Shorthair", "Cornish Rex", "Devon Rex", "Egyptian Mau",
                        "European Burmese", "Exotic", "Havana Brown", "Japanese Bobtail", "Korat", "LaPerm", "Maine Coon", "Manx",
                        "Norwegian Forest Cat", "Ocicat", "Oriental", "Persian", "RagaMuffin", "Ragdoll", "Russian Blue", "Scottish Fold",
                        "Selkirk Rex", "Siamese", "Siberian", "Singapura", "Somali", "Sphynx", "Tonkinese", "Turkish Angora", "Turkish Van"
                };
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Breed Type ");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    // selectedPetType = items[item].toString();
                    mBtnBreedType.setText(items[item].toString());
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            Toast.makeText(ProductBuyOrSalePetsActivity.this, "Please select Pet Type ", Toast.LENGTH_SHORT).show();
        }
    }

    public void petGenderePopup() {

        final String[] items = {
                "Male", "Female"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Gender ");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                // selectedPetType = items[item].toString();
                mBtnGender.setText(items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    public void petAgePopup() {

        final String[] items = {
                "Puppies/Kittens", "Adolescent", "Adult", "Senior"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Age Range ");
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                mBtnAge.setText(items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    public void petNeuturedAPopup() {

        final String[] items = {
                "Yes", "No"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Neutured ");
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                // selectedPetType = items[item].toString();
                mBtnNeuture.setText(items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    public void petVaccinatedPopup() {

        final String[] items = {
                "Yes", "No"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Vaccinated ");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                // selectedPetType = items[item].toString();
                mBtnVaccinated.setText(items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductBuyOrSalePetsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ProductBuyOrSalePetsActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
//code for deny
                }
                break;
        }
    }



    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
              /*

                if (mIvfirst.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.no_image).getConstantState()) {


                } else if (mIvSec.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.no_image).getConstantState()) {
                    mIvSec.setImageBitmap(bm);
                    updated_flag="2";
                } else {
                    mIvThird.setImageBitmap(bm);
                    updated_flag="3";
                }*/
                if (set_image_flag.equals("1")) {
                    mIvPetImage.setImageBitmap(bm);
                    mIvfirst.setImageBitmap(bm);
                    updated_flag = "1";
                    set_image_flag = "2";
                    mIvDeleteFirst.setVisibility(View.VISIBLE);
                } else if (set_image_flag.equals("2")) {
                    mIvPetImage.setImageBitmap(bm);
                    mIvSec.setImageBitmap(bm);
                    updated_flag = "2";
                    set_image_flag = "3";
                } else {
                    mIvPetImage.setImageBitmap(bm);
                    mIvThird.setImageBitmap(bm);
                    updated_flag = "3";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();


          /*  mIvPetImage.setImageBitmap(thumbnail);
            if (mIvfirst.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.no_image).getConstantState()) {

                mIvfirst.setImageBitmap(thumbnail);
                updated_flag="1";
            } else if (mIvSec.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.no_image).getConstantState()) {
                mIvSec.setImageBitmap(thumbnail);
                updated_flag="2";
            } else {
                mIvThird.setImageBitmap(thumbnail);
                updated_flag="3";
            }*/
            if (set_image_flag.equals("1")) {
                mIvPetImage.setImageBitmap(thumbnail);
                mIvfirst.setImageBitmap(thumbnail);
                updated_flag = "1";
                set_image_flag = "2";
                mIvDeleteFirst.setVisibility(View.VISIBLE);
            } else if (set_image_flag.equals("2")) {
                mIvPetImage.setImageBitmap(thumbnail);
                mIvSec.setImageBitmap(thumbnail);
                updated_flag = "2";
                set_image_flag = "3";
            } else {
                mIvPetImage.setImageBitmap(thumbnail);
                mIvThird.setImageBitmap(thumbnail);
                updated_flag = "3";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void popupwindow(View view) {


        PopupMenu popup = new PopupMenu(ProductBuyOrSalePetsActivity.this, view);

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
                    intent1.putExtra("key", "BuyOrSalePets");
                    startActivity(intent1);
                    finish();
                    startActivity(intent1);

                } else {
                    Intent intent = new Intent(ProductBuyOrSalePetsActivity.this, MyPetListBuyorSaleActivity.class);
                    startActivity(intent);

                }
                return true;
            }
        });


        popup.show();//showing popup menu
    }




    public void editDetails() {

        System.out.println("Age Range" + all_pets_list_model.getAge_range());
        try {
            mEtDescription.setText(all_pets_list_model.getDescription());
            mEtPrice.setText(all_pets_list_model.getCurrency());
            mEtName.setText(all_pets_list_model.getName());
            mEtNumber.setText(all_pets_list_model.getContact());
            mEtemailId.setText(all_pets_list_model.getEmail());
            mEtCity.setText(all_pets_list_model.getCity());
            mEtCurrentAddress.setText(all_pets_list_model.getLocality());

            mBtnPetType.setText(all_pets_list_model.getAnimal()
            );
            mBtnBreedType.setText(all_pets_list_model.getBreed_type());
            mBtnGender.setText(all_pets_list_model.getGender());
            mBtnAge.setText(all_pets_list_model.getAge_range());
            mBtnNeuture.setText(all_pets_list_model.getNeutered());
            mBtnVaccinated.setText(all_pets_list_model.getVaccinated());
            UrlImageViewHelper.setUrlDrawable(mIvPetImage, all_pets_list_model.getImage(), 0,
                    new UrlImageViewCallback() {
                        @Override
                        public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                             boolean loadedFromCache) {
                        }
                    });

            if (all_pets_list_model.getType().equalsIgnoreCase("buy")) {
                radioButton1.setChecked(true);
                mEtTitle.setVisibility(View.GONE);
            } else {
                if (all_pets_list_model.getType().equalsIgnoreCase("sale")) {
                    radioButton2.setChecked(true);
                    mEtTitle.setVisibility(View.VISIBLE);
                    mEtTitle.setText(all_pets_list_model.getTitle());
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    public void updateDataSend() {

        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.MYPET_EDIT_BUYSALE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        System.out.println("responsde" + response.toString());
                        try {
                            JSONObject j = new JSONObject(response);
                            flag1 = j.getInt("flag");
                            if (flag1 == 1) {
                                messages = j.getString("response");
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductBuyOrSalePetsActivity.this);
                                builder.setMessage(messages)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(ProductBuyOrSalePetsActivity.this, MyPetListBuyorSaleActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductBuyOrSalePetsActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    String user_id = preferences.getString("userid", "");

                    params.put("user_id", user_id);
                    params.put("pet_sale_buy_id", all_pets_list_model.getPet_sale_buy_id());

                    params.put("pet_profile_picture", petimage);

                    params.put("type", type);
                    params.put("animal", pettype);
                    params.put("gender", gender);
                    params.put("description", desc);
                    params.put("locality", locality);
                    params.put("title", title);
                    params.put("city", city);
                    params.put("age_range", age);
                    params.put("category", "Na");
                    params.put("breed_type", breedtype);
                    params.put("currency", price);
                    params.put("price", "");
                    params.put("email", userEmail);
                    params.put("name", name);
                    params.put("neutered", natuered);
                    params.put("vaccinated", vaccinated);
                    params.put("contact", number);
                } catch (Exception e) {
                    System.out.println("arsherror" + e.toString());
                }
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }
}
