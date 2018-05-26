package com.hnweb.clawpal.BuyorSale.activity;

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
import android.view.LayoutInflater;
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
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.CustomClass.InAppCreateActivity;
import com.hnweb.clawpal.MultipartRequest.MultiPart_Key_Value_Model;
import com.hnweb.clawpal.MultipartRequest.MultipartFileUploaderAsync;
import com.hnweb.clawpal.MultipartRequest.OnEventListener;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.Utils.ValidationMethods;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.login.LoginActivity;
import com.hnweb.clawpal.myfavpet.ViewPagerActivity;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HNWeb-11 on 7/27/2016.
 */
public class BuyOrSalePetsActivity extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener {
    static final String SKU_PLAN1 = "plan1";
    static final String SKU_PLAN2 = "plan2";
    static final int RC_REQUEST = 10001;
    private static int RESULT_LOAD_IMAGE = 1;
    final String[] items = {
            "Dog", "Cat"
    };
    List<String> images = new ArrayList<>();

    ArrayList<String> imagePath = new ArrayList<String>();

    ArrayList<MultiPart_Key_Value_Model> mult_list;
    Context context;
    SharedPreferences sharedPreferences;
    String from;
    String user_Id;
    String isPaymentDone;
    boolean mIsPremium = false;
    boolean mAutoRenewEnabled = false;

    int mTank;
    // The helper object
    IabHelper mHelper;
    String paymentId;

    IabBroadcastReceiver mBroadcastReceiver;
    Toolbar toolbar;
    String selectedPetType = "Dog";
    Button mBtnPetType, mBtnBreedType, mBtnGender, mBtnAge, mBtnNeuture, mBtnVaccinated, mBtnNext;
    EditText mEtPrice, mEtDescription, mEtCurrentAddress, mEtName, mEtNumber, mEtCity, mEtTitle, mEtemailId,et_state,et_country,et_zipcode;
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
    private AlertDialog b;

    // Does the user have an active subscription to the infinite gas plan?
//    boolean mSubscribedToInfiniteGas = false;
    private int chooserType;
    private DatePickerDialog.OnDateSetListener datePickerListener;
    private int day;
    private String filePath;
    private int hour;
    private ArrayList<String> mSelectPath;
    private int minute;
    private int month;
    // Graphics for the gas gauge
    private TimePickerDialog.OnTimeSetListener timePickerListener;
    private int year;
    private ArrayList<String> mSelecthandPath;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ////punnyfuzzle
    private String TAG = LoginActivity.class.getSimpleName();
    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
//                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_PLAN1);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));


            Purchase gasPurchase = inventory.getPurchase(SKU_PLAN2);
            Purchase prePurchase = inventory.getPurchase(SKU_PLAN1);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                try {
                    mHelper.consumeAsync(inventory.getPurchase(SKU_PLAN2), new IabHelper.OnConsumeFinishedListener() {
                        @Override
                        public void onConsumeFinished(Purchase purchase, IabResult result) {
                            Log.d("InApp Purchase", "onConsumeFinished= " + result.getMessage());
                        }
                    });
                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error consuming gas. Another async operation in progress.");
                    Log.d("InApp Purchase", "IabAsyncInProgressException");
                }
                return;
            }
            if (prePurchase != null && verifyDeveloperPayload(prePurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                try {
                    //     Purchase purchase = new Purchase("inapp", THAT_JSON_STRING, "");
                    mHelper.consumeAsync(inventory.getPurchase(SKU_PLAN1), new IabHelper.OnConsumeFinishedListener() {
                        @Override
                        public void onConsumeFinished(Purchase purchase, IabResult result) {
                            Log.d("InApp Purchase", "onConsumeFinished= " + result.getMessage());
                        }
                    });
                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error consuming gas. Another async operation in progress.");
                    Log.d("InApp Purchase", "IabAsyncInProgressException");
                }
                return;
            }
            //   updateUi();
            //   setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };
    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);


            if (mHelper == null) return;


            if (result.isSuccess()) {


                Utilities.showAlertDailog(BuyOrSalePetsActivity.this, "PunnyFuzzles", "PayPal Payment Successfully Done.", "Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }, false);
            } else {
//                complain("Error while consuming: " + result);
            }
            // updateUi();
            // setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");
        }
    };
    private Activity activity;
    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
//                complain("Error purchasing: " + result);
                //   setWaitScreen(false);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
//                complain("Error purchasing. Authenticity verification failed.");
                //  setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_PLAN2)) {

                ///punny fuzzle
//.makeText(getApplicationContext(),"40 puzzles done",Toast.LENGTH_LONG).show();

                Utilities.showAlertDailog(activity, "PunnyFuzzles", "Payment Successfully Done.", "Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }, false);


            } else if (purchase.getSku().equals(SKU_PLAN1)) {


                Utilities.showAlertDailog(BuyOrSalePetsActivity.this, "PunnyFuzzles", "PayPal Payment Successfully Done.", "Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }, false);

            }

        }
    };

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_buy_sale);
        prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
        currentadd = prefs.getString("Current_add", null);
        userEmail = prefs.getString("user_email", null);
        user_Id = prefs.getString("userid", null);
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
        BuyOrSalePetsActivity.this.getWindow().setSoftInputMode(
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
                Intent intent = new Intent(BuyOrSalePetsActivity.this, MyPetListBuyorSaleActivity.class);
                startActivity(intent);
            }
        });
       /* if (user_Id==null) {
            iv_setting.setVisibility(View.GONE);
        } else {
            iv_setting.setVisibility(View.VISIBLE);

        }*/

        /*if (user_Id != null) {
            iv_setting.setVisibility(View.VISIBLE);

        } else {
            iv_setting.setVisibility(View.GONE);
        }
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupwindow(view);
            }
        });*/

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user_Id != null) {
                    popupwindow(view);

                } else {
                    popupwindow2(view);
                }

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
        et_country = (EditText) findViewById(R.id.et_country);
        et_state = (EditText) findViewById(R.id.et_state);
        et_zipcode = (EditText) findViewById(R.id.et_zipcode);
        mEtCity = (EditText) findViewById(R.id.et_city);
        mIvSec = (ImageView) findViewById(R.id.iv_pet2);
        mIvThird = (ImageView) findViewById(R.id.iv_pet3);
        mEtTitle = (EditText) findViewById(R.id.et_title);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        mIvDeleteFirst = (ImageView) findViewById(R.id.iv_ic_delete_first);
        mIvDeleteFirst.setVisibility(View.GONE);

        mEtPrice.setVisibility(View.VISIBLE);


        if (userEmail == null) {
            mEtemailId.setText("");
        } else {

            mEtemailId.setText(userEmail);
            mEtemailId.setFocusable(false);
            mEtemailId.setClickable(false);

        }


    }

    public void SetListener() {

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        Intent intent = new Intent(BuyOrSalePetsActivity.this, PetListDemoActivity.class);
                        startActivity(intent);
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
                editor.clear();
                editor.commit();
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
       /* radioButton1.setOnClickListener(new View.OnClickListener() {
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
        });*/
      /*  mEtPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtPrice.setFocusable(true);
            }
        });*/
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
                        Toast.makeText(BuyOrSalePetsActivity.this, "Please Select pet type", Toast.LENGTH_SHORT).show();

                    } else if (mBtnBreedType.getText().toString().equals("Select Breed Type")) {
                        Toast.makeText(BuyOrSalePetsActivity.this, "Please Select Breed type", Toast.LENGTH_SHORT).show();

                    } else if (mBtnGender.getText().toString().equals("Select Gender")) {
                        Toast.makeText(BuyOrSalePetsActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();

                    } else if (mBtnAge.getText().toString().equals("Select Age Range")) {
                        Toast.makeText(BuyOrSalePetsActivity.this, "Please Select Age Range", Toast.LENGTH_SHORT).show();

                    } else if (mBtnNeuture.getText().toString().equals("Select Neutured")) {
                        Toast.makeText(BuyOrSalePetsActivity.this, "Please Select Neutured", Toast.LENGTH_SHORT).show();

                    } else if (mBtnVaccinated.getText().toString().equals("Select Vaccinated")) {
                        Toast.makeText(BuyOrSalePetsActivity.this, "Please Select Vaccinated", Toast.LENGTH_SHORT).show();

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
                        return;} else if (!vm.isValidNumber(mEtNumber.getText().toString())) {
                        mEtNumber.setError("Please Valid  Contact Number");
                        mEtNumber.requestFocus();
                        return;}
                    else if(!vm.isValidEmail(mEtemailId.getText().toString())) {
                       mEtemailId.setError("Please enter  Email");
                       mEtemailId.requestFocus();
                       return;}
                   else if (mEtCurrentAddress.getText().toString().equals("")) {
                       mEtCurrentAddress.setError("Please enter  Address");
                       mEtCurrentAddress.requestFocus();
                       return;}else if (!vm.isValidLocation(mEtCity.getText().toString())) {
                        mEtCity.setError("Please enter  City");
                        mEtCity.requestFocus();
                        return;}
                   else if (et_state.getText().toString().equals("")) {
                       et_state.setError("Please enter  State");
                       et_state.requestFocus();
                       return;}  else if (et_country.getText().toString().equals("")) {
                       et_country.setError("Please enter  Country");
                       et_country.requestFocus();
                       return;}
                   else if (et_zipcode.getText().toString().equals("")) {
                       et_zipcode.setError("Please enter  Zipcode");
                       et_zipcode.requestFocus();
                       return;}else
                    {
                        host.setCurrentTab(3);
                        mBtnNext.setText("Save");
                    }
                } else {
                    System.out.println("PetImae" + petimage);
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    progress = new ProgressDialog(BuyOrSalePetsActivity.this);
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
                                    //userEmail=mEtemailId.getText().toString();

                                    try {

                                        mIvfirst.buildDrawingCache();
                                        Bitmap bmap1 = mIvfirst.getDrawingCache();
                                        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
                                        bmap1.compress(Bitmap.CompressFormat.PNG, 100, bos1);
                                        byte[] bb1 = bos1.toByteArray();

                                        String petimage1 = Base64.encodeToString(bb1, Base64.DEFAULT);
                                        System.out.println("PetImaefirst" + petimage1);


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
                                            updateDataSend();
                                        } else {
                                            /*registerAsynchTask obj = new registerAsynchTask();
                                            obj.execute();*/
                                            imageUpload1();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(BuyOrSalePetsActivity.this);
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

                            /*
                                if (mIvfirst.getDrawable().getConstantState() == mIvPetImage.getDrawable().getConstantState()) {
                                    mIvfirst.setImageResource(R.drawable.no_image);
                                    mIvPetImage.setImageResource(R.drawable.no_image);

                                } else if (mIvSec.getDrawable().getConstantState() == mIvPetImage.getDrawable().getConstantState()) {
                                    mIvSec.setImageResource(R.drawable.no_image);
                                    mIvPetImage.setImageResource(R.drawable.no_image);
                                } else {
                                    mIvThird.setImageResource(R.drawable.no_image);
                                    mIvPetImage.setImageResource(R.drawable.no_image);
                                }*/


                            case DialogInterface.BUTTON_NEGATIVE:

                                // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(BuyOrSalePetsActivity.this);
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

               /* if (tabId.equals("Tab One")) {
                    findViewById(R.id.sc_parrent).getParent().requestDisallowInterceptTouchEvent(true);
                }*/

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
            Toast.makeText(BuyOrSalePetsActivity.this, "Please select Pet Type ", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(BuyOrSalePetsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(BuyOrSalePetsActivity.this);
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

    public void decodeFile1(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap1 = BitmapFactory.decodeFile(filePath, o2);
        System.out.println("-- 5 -->" + bitmap1);
        // imgView.setImageBitmap(bitmap);

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

        if (requestCode == RC_REQUEST) {

            if (resultCode == RESULT_OK) {
                try {
                    int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
                    String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
                    String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
                    Log.d("InAPPPrchase", "dataSignature= " + dataSignature);
                    Log.d("InAPPPrchase", "purchaseData" + purchaseData);
                    Log.d("InAPPPrchase", "responseCode" + String.valueOf(responseCode));

                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    paymentId = jo.getString("orderId");

                    //      Toast.makeText(getApplicationContext(),"sku"+sku,Toast.LENGTH_LONG).show();
                    System.out.println("op" + sku);

                    if (sku.equals("plan1")) {
                        //   Toast.makeText(getApplicationContext(),"40 Puzzles called",Toast.LENGTH_LONG).show();
                        addPaymentInfo(paymentId);

                    }

                } catch (JSONException e) {
                    alert("Failed to parse purchase data.");
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                //Bitmap bm  = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                if (set_image_flag.equals("1")) {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    // Log.i("Path", imagePath12);
                    FileOutputStream fo;
                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    String imagePath12 = destination.getAbsolutePath();
                    imagePath.add(imagePath12);
                    mIvPetImage.setImageBitmap(bm);
                    mIvfirst.setImageBitmap(bm);
                    updated_flag = "1";
                    set_image_flag = "2";
                    mIvDeleteFirst.setVisibility(View.VISIBLE);
                } else if (set_image_flag.equals("2")) {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    // Log.i("Path", imagePath12);
                    FileOutputStream fo;
                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    String imagePath12 = destination.getAbsolutePath();
                    imagePath.add(imagePath12);
                    mIvPetImage.setImageBitmap(bm);
                    mIvSec.setImageBitmap(bm);
                    updated_flag = "2";
                    set_image_flag = "3";
                } else {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    // Log.i("Path", imagePath12);
                    FileOutputStream fo;
                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    String imagePath12 = destination.getAbsolutePath();
                    imagePath.add(imagePath12);

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

        try {


            if (set_image_flag.equals("1")) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                // Log.i("Path", imagePath12);
                FileOutputStream fo;
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
                String imagePath12 = destination.getAbsolutePath();
                mIvPetImage.setImageBitmap(thumbnail);
                mIvfirst.setImageBitmap(thumbnail);
                updated_flag = "1";
                set_image_flag = "2";
                imagePath.add(imagePath12);
                mIvDeleteFirst.setVisibility(View.VISIBLE);
            } else if (set_image_flag.equals("2")) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                // Log.i("Path", imagePath12);
                FileOutputStream fo;
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
                String imagePath12 = destination.getAbsolutePath();
                mIvPetImage.setImageBitmap(thumbnail);
                mIvSec.setImageBitmap(thumbnail);
                updated_flag = "2";
                imagePath.add(imagePath12);

                set_image_flag = "3";
            } else {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                // Log.i("Path", imagePath12);
                FileOutputStream fo;
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
                String imagePath12 = destination.getAbsolutePath();
                mIvPetImage.setImageBitmap(thumbnail);
                mIvThird.setImageBitmap(thumbnail);
                imagePath.add(imagePath12);

                updated_flag = "3";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void popupwindow(View view) {


        PopupMenu popup = new PopupMenu(BuyOrSalePetsActivity.this, view);

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

                } else if (item.getTitle().toString().equals("My Listing")) {
                    Intent intent = new Intent(BuyOrSalePetsActivity.this, MyPetListBuyorSaleActivity.class);
                    startActivity(intent);
                } else if (item.getTitle().toString().equals("Favourite")) {
                    Intent intent = new Intent(BuyOrSalePetsActivity.this, ViewPagerActivity.class);
                    startActivity(intent);
                }


                return true;
            }
        });


        popup.show();//showing popup menu
    }

    public void inAPpOnCreate() {

        loadData();

        /* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
         * (that you got from the Google Play developer console). This is not your
         * developer public key, it's the *app-specific* public key.
         *
         * Instead of just storing the entire literal string here embedded in the
         * program,  construct the key at runtime from pieces or
         * use bit manipulation (for example, XOR with some other string) to hide
         * the actual key.  The key itself is not secret information, but we don't
         * want to make it easy for an attacker to replace the public key with one
         * of their own and then fake messages from the server.
         */
        String base64EncodedPublicKey = AppConstant.Base64EncodedPublicKey;

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                try {


                    try {
                        String payload = "";
                        mHelper.launchPurchaseFlow(BuyOrSalePetsActivity.this, SKU_PLAN1, RC_REQUEST,
                                mPurchaseFinishedListener, payload);
                    } catch (IabHelper.IabAsyncInProgressException e) {
//                        complain("Service Not Available , Please Try Again Later..");
                        //setWaitScreen(false);
                    }

                } catch (Exception ex) {
                    Log.e(AppConstant.TAG, "Error(btn100_onClick):" + ex.toString());
                }

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
//                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(BuyOrSalePetsActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });
        consumeProducts();
    }

    private void consumeProducts() {
    }

    void loadData() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        mTank = sp.getInt("tank", 2);
        Log.d(TAG, "Loaded data: tank = " + String.valueOf(mTank));
    }

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
//            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    private void addPaymentInfo(final String paymentId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.UPDATE_PAYMENT_URL,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        System.out.println("res= " + response);
                        //    System.out.println("reg"+GET_PLANS__URL.toString()+response.toString());
                        try {
                            JSONObject j = new JSONObject(response);
//                            flag=j.getInt("message_code");
//                            System.out.println("arshflag"+ flag);
//                            if(flag)
                            //    int flag=j.getInt("message_code");
                            // System.out.println("flag"+ response.toString());
                            System.out.println("resArsh" + response.toString() + response.toString());
                            String res = j.getString("message");
                            //      System.out.println("res" + res);

                            // Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BuyOrSalePetsActivity.this, R.style.MyAlertDialogStyle);

                            //  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity_save_data.this);
                            builder.setMessage(res)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent;


                                            try {


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                            android.support.v7.app.AlertDialog alert = builder.create();
                            alert.show();

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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {

                    params.put(AppConstant.USER_ID, user_Id);
                    params.put(AppConstant.KEY_PAYMENT_ID, paymentId);

                } catch (Exception e) {
                    System.out.println("arsherror" + e.toString());
                }


                System.out.println("myparams" + params);
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            try {
                unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {

            }
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    void alert(String message) {
        android.support.v7.app.AlertDialog.Builder bld = new android.support.v7.app.AlertDialog.Builder(this, R.style.MyAlertDialogStyle);

        bld.setMessage(message);
        bld.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        finish();
                    }
                });
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
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
            et_state.setText(all_pets_list_model.getState());
            et_country.setText(all_pets_list_model.getCountry());
            et_zipcode.setText(all_pets_list_model.getZipcode());
            mEtCurrentAddress.setText(all_pets_list_model.getLocality());
            mEtTitle.setText(all_pets_list_model.getTitle());
            mBtnPetType.setText(all_pets_list_model.getAnimal()
            );
            mBtnBreedType.setText(all_pets_list_model.getBreed_type());
            mBtnGender.setText(all_pets_list_model.getGender());
            mBtnAge.setText(all_pets_list_model.getAge_range());
            mBtnNeuture.setText(all_pets_list_model.getNeutered());
            mBtnVaccinated.setText(all_pets_list_model.getVaccinated());
           /* UrlImageViewHelper.setUrlDrawable(mIvPetImage, all_pets_list_model.getImage(), 0,
                    new UrlImageViewCallback() {
                        @Override
                        public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                             boolean loadedFromCache) {
                        }
                    });*/
            images = Arrays.asList(all_pets_list_model.getImage().replaceAll("\\s", "").split(","));

            try {
                UrlImageViewHelper.setUrlDrawable(mIvPetImage, images.get(0), 0,
                        new UrlImageViewCallback() {
                            @Override
                            public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                                 boolean loadedFromCache) {
                            }
                        });

                if (images.get(0) != null) {
                    UrlImageViewHelper.setUrlDrawable(mIvfirst, images.get(0), 0,
                            new UrlImageViewCallback() {
                                @Override
                                public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                                     boolean loadedFromCache) {
                                }
                            });
                }
                if (images.get(1) != null) {
                    UrlImageViewHelper.setUrlDrawable(mIvSec, images.get(1), 0,
                            new UrlImageViewCallback() {
                                @Override
                                public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                                     boolean loadedFromCache) {
                                }
                            });
                }
                if (images.get(2) != null) {
                    UrlImageViewHelper.setUrlDrawable(mIvThird, images.get(2), 0,
                            new UrlImageViewCallback() {
                                @Override
                                public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                                     boolean loadedFromCache) {
                                }
                            });
                }

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            if (all_pets_list_model.getType().equalsIgnoreCase("buy")) {
                radioButton1.setChecked(true);
              //  mEtTitle.setVisibility(View.GONE);
            } else {
                if (all_pets_list_model.getType().equalsIgnoreCase("sale")) {
                    radioButton2.setChecked(true);
               //     mEtTitle.setVisibility(View.VISIBLE);
                   // mEtTitle.setText(all_pets_list_model.getTitle());
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
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BuyOrSalePetsActivity.this);
                                builder.setMessage(messages)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(BuyOrSalePetsActivity.this, MyPetListBuyorSaleActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                android.support.v7.app.AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BuyOrSalePetsActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
                        System.out.println("jsonexeption" + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                try {

                    params.put("user_id", user_Id);
                    params.put("pet_sale_buy_id", all_pets_list_model.getPet_sale_buy_id());

                    params.put("pet_profile_picture", petimage);

                    params.put("type", type);
                    params.put("animal", pettype);
                    params.put("gender", gender);
                    params.put("description", desc);
                    params.put("locality", locality);
                    params.put("title", title);
                    params.put("city", city);
                    params.put("state",et_state.getText().toString());
                    params.put("country",et_country.getText().toString());
                    params.put("zip",et_zipcode.getText().toString());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BuyOrSalePetsActivity.this, PetListDemoActivity.class);
        startActivity(intent);
        finish();
    }

    public void imageUpload1() {
        mult_list = new ArrayList<MultiPart_Key_Value_Model>();
        MultiPart_Key_Value_Model OneObject = new MultiPart_Key_Value_Model();
        Map<String, String> fileParams = new HashMap<>();

        System.out.println(imagePath);
        try {
            for (int i = 0; i < imagePath.size(); i++) {
                fileParams.put("Image[" + i + "]", String.valueOf(imagePath.get(i)));
                System.out.println("Arsh Op" + String.valueOf(imagePath));
            }
        } catch (Exception e) {

        }
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final String user_id = preferences.getString("userid", "");
        System.out.println("UserId..." + user_id);
        String url = WebURL.ADD_DATA_BUYSALE_LOGIN;// ?name=sagar&email=sagar.bwd12343@gmail.com&address=pune&city=pune&state=maharashtra&password=123456&user_type=3&licence="+licenseb64+"&picture=";


        ////
        Map<String, String> Stringparams = new HashMap<>();

        if (!user_id.equals("")) {
            Stringparams.put("user_id", user_id);
        }

        // Stringparams.put("pet_adoption_photo", petimage);
        Stringparams.put("Image", petimage);
        Stringparams.put("type", type);
        Stringparams.put("animal", pettype);
        Stringparams.put("gender", gender);
        Stringparams.put("description", desc);
        Stringparams.put("locality", locality);
        Stringparams.put("title", title);
        Stringparams.put("city", city);
        Stringparams.put("state",et_state.getText().toString());
        Stringparams.put("country",et_country.getText().toString());
        Stringparams.put("zip",et_zipcode.getText().toString());
        Stringparams.put("age", age);
        Stringparams.put("category", "Na");
        Stringparams.put("breed_type", breedtype);
        Stringparams.put("currency", price);
        Stringparams.put("price", "");
        Stringparams.put("email", mEtemailId.getText().toString());
        Stringparams.put("name", name);
        Stringparams.put("neutered", natuered);
        Stringparams.put("vaccinated", vaccinated);
        Stringparams.put("contact", number);


        ////
        OneObject.setUrl(url);
        OneObject.setFileparams(fileParams);
        System.out.println("file paramsss" + fileParams);
        OneObject.setStringparams(Stringparams);


        mult_list.add(OneObject);


        MultipartFileUploaderAsync someTask = new MultipartFileUploaderAsync(getApplicationContext(), OneObject, new OnEventListener<String>() {
            @Override
            public void onSuccess(String object) {
                progress.dismiss();
                System.out.println("Response23multipart" + object);
                try {
                    JSONObject jsonObject1 = new JSONObject(object);

                    String response = jsonObject1.getString("response");
                    System.out.println("Response..." + response.toString());
                    JSONObject jsonObject11 = new JSONObject(response);
                    int flag = jsonObject11.getInt("flag");

                    if (flag == 1) {

                        if (user_id.equals("")) {
                            String message = jsonObject11.getString("msg");

                           /* DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            Intent intent = new Intent(BuyOrSalePetsActivity.this, LoginActivity.class);
                                            intent.putExtra("key", "PetListActivity");
                                            startActivity(intent);
                                        case DialogInterface.BUTTON_NEGATIVE:

                                            // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(BuyOrSalePetsActivity.this);
                            builder.setTitle(message);
                            builder.setCancelable(false);
                            builder.setMessage("").setPositiveButton("Login", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
*/
                            AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(BuyOrSalePetsActivity.this);

                            dialogBuilder1.setCancelable(true);
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                            final View dialogView1 = inflater.inflate(R.layout.custom_dialog, null);
                            dialogBuilder1.setView(dialogView1);

                            final TextView editText = (TextView) dialogView1.findViewById(R.id.tv_msg);
                            editText.setText(message);

                            TextView btn_no = (TextView) dialogView1.findViewById(R.id.btn_no);
                            TextView btn_login = (TextView) dialogView1.findViewById(R.id.btn_login);
                            btn_no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    b.dismiss();

                                }
                            });
                            btn_login.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(BuyOrSalePetsActivity.this, LoginActivity.class);
                                    intent.putExtra("key", "PetListActivity");
                                    startActivity(intent);
                                }
                            });
                            b = dialogBuilder1.create();
                            b.show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Data upload successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(BuyOrSalePetsActivity.this, PetListDemoActivity.class);
                            finish();
                            startActivity(intent);
                        }

                    } else {
                        String message = jsonObject11.getString("msg");
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {

                                    case DialogInterface.BUTTON_NEGATIVE:

                                        // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(BuyOrSalePetsActivity.this);
                        builder.setTitle("");
                        builder.setCancelable(false);
                        builder.setMessage(message).setPositiveButton("Ok", dialogClickListener).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Exception e) {
                progress.dismiss();
            }
        });
        someTask.execute();


        return;


    }

    public void popupwindow2(View view) {


        PopupMenu popup = new PopupMenu(BuyOrSalePetsActivity.this, view);

        popup.getMenuInflater().inflate(R.menu.popup_mymenuone, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equals("Login")) {
                    Intent intent = new Intent(BuyOrSalePetsActivity.this, LoginActivity.class);
                    intent.putExtra("key", "BuySaleActivity");
                    startActivity(intent);

                }
                return true;
            }
        });


        popup.show();//showing popup menu
    }

    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
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

    private class registerAsynchTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            String user_id = preferences.getString("userid", "");
            String url = WebURL.POST_BUY_SALE_PETS;// ?name=sagar&email=sagar.bwd12343@gmail.com&address=pune&city=pune&state=maharashtra&password=123456&user_type=3&licence="+licenseb64+"&picture=";
            String urledit = WebURL.MYPET_EDIT_BUYSALE;
            BitmapFactory.Options bfo1;

            ByteArrayOutputStream bao1;
            HttpPost httppost;
            HttpClient httpclient = new DefaultHttpClient();

            System.out.println("text callnonedit" + callfrom);
            httppost = new HttpPost(url);

            try {

                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(14);
                System.out.println("text callnonedit" + callfrom);

                nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
                nameValuePairs.add(new BasicNameValuePair("Image", petimage));
                nameValuePairs.add(new BasicNameValuePair("type", type));
                nameValuePairs.add(new BasicNameValuePair("animal", pettype));
                nameValuePairs.add(new BasicNameValuePair("gender", gender));
                nameValuePairs.add(new BasicNameValuePair("description", desc));
                nameValuePairs.add(new BasicNameValuePair("locality", locality));
                nameValuePairs.add(new BasicNameValuePair("title", title));
                nameValuePairs.add(new BasicNameValuePair("city", city));
                nameValuePairs.add(new BasicNameValuePair("age", age));
                nameValuePairs.add(new BasicNameValuePair("category", "Na"));
                nameValuePairs.add(new BasicNameValuePair("breed_type", breedtype));
                nameValuePairs.add(new BasicNameValuePair("currency", price));
                nameValuePairs.add(new BasicNameValuePair("price", ""));
                nameValuePairs.add(new BasicNameValuePair("email", userEmail));
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("neutered", natuered));
                nameValuePairs.add(new BasicNameValuePair("vaccinated", vaccinated));
                nameValuePairs.add(new BasicNameValuePair("contact", number));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity httpEntity = response.getEntity();
                is = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                reader.close();
                String json = sb.toString();
                System.out.println("complete Response:-->" + json);
                Log.i("Json----", json);
                JSONObject jObj = new JSONObject(json);
                JSONObject jObj1 = jObj.getJSONObject("response");
                flag = jObj1.getString("flag");

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block

                // dialog.dismiss();
                System.out.println("ERROR " + e.getMessage());
            } catch (IOException e) {


                // TODO Auto-generated catch block
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return flag;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
//            System.out.println("resultfdg " + result.toString());
            if (result.equalsIgnoreCase("1")) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Data upload successfully.", Toast.LENGTH_SHORT).show();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                String isPayment = pref.getString("is_payment", null);
              /*  if (isPayment.equals("0")) {
                    inAPpOnCreate();
                } else {*/
                Intent intent = new Intent(BuyOrSalePetsActivity.this, PetListDemoActivity.class);
                finish();
                startActivity(intent);


            } else {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
