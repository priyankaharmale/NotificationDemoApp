package com.hnweb.clawpal.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hnweb.clawpal.BuyorSale.activity.BuyOrSalePetsActivity;
import com.hnweb.clawpal.BuyorSale.activity.BuySaleDetailsSwipeListActivity;
import com.hnweb.clawpal.BuyorSale.activity.PetDetails;
import com.hnweb.clawpal.BuyorSale.activity.PetListDemoActivity;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.DashboardActivity;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.Utils.ConnectionDetector;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.adaption.activity.AddAPetActivity;
import com.hnweb.clawpal.adaption.activity.AdoptPetDetails;
import com.hnweb.clawpal.adaption.activity.AdoptionDemoPetList;
import com.hnweb.clawpal.adaption.activity.AdoptionDetailsSwipeListActivity;
import com.hnweb.clawpal.adaption.model.AdoptPetModel;
import com.hnweb.clawpal.lostorfound.pet.activity.LostFoundDetailsSwipeListActivity;
import com.hnweb.clawpal.lostorfound.pet.activity.LostFoundPeDemotList;
import com.hnweb.clawpal.lostorfound.pet.activity.LostFoundPetDetails;
import com.hnweb.clawpal.lostorfound.pet.activity.ReportAPetActivity;
import com.hnweb.clawpal.lostorfound.pet.model.lostFoundPetListModel;
import com.hnweb.clawpal.util.AppConstant;
import com.hnweb.clawpal.util.IabBroadcastReceiver;
import com.hnweb.clawpal.util.IabHelper;
import com.hnweb.clawpal.util.IabResult;
import com.hnweb.clawpal.util.Inventory;
import com.hnweb.clawpal.util.Purchase;
import com.hnweb.clawpal.util.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HNWeb-11 on 7/22/2016.
 */
public class LoginActivity extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener {
    static final String SKU_PLAN1 = "plan1";
    static final String SKU_PLAN2 = "plan2";
    static final int RC_REQUEST = 10001;
    Toolbar toolbar;
    EditText emailvalid, passwordvalid;
    Button mSignUpButton;
    Button mLoginButton;
    TextView tv_forgot_pwd;
    lostFoundPetListModel petObjLostFound;
    int positionLostFound;
    private ArrayList<lostFoundPetListModel> all_pets_list_modelsLostFound;
    All_Pets_List_Model petObj;
    private ArrayList<All_Pets_List_Model> all_pets_list_models;
    int position;
    String password, emailID;
    ProgressDialog progress;
    CheckBox mCbxRememberMe;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ConnectionDetector connectionDetector;
    String from;
    String user_Id;
    int positionAdoption;
    AdoptPetModel petObjAdoption;
    private ArrayList<AdoptPetModel> all_pets_list_modelsAdoption;
    String isPaymentDone;
    boolean mIsPremium = false;
    boolean mAutoRenewEnabled = false;
    String mInfiniteGasSku = "";
    String mFirstChoiceSku = "";
    String mSecondChoiceSku = "";
    String mSelectedSubscriptionPeriod = "";
    int mTank;
    IabHelper mHelper;
    String paymentId;
    IabBroadcastReceiver mBroadcastReceiver;
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
    private String TAG = LoginActivity.class.getSimpleName();
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
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
            if (mHelper == null) return;
            if (result.isSuccess()) {
                Utilities.showAlertDailog(LoginActivity.this, "PunnyFuzzles", "PayPal Payment Successfully Done.", "Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }, false);
            } else {
            }
            Log.d(TAG, "End consumption flow.");
        }
    };
    private Activity activity;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            if (mHelper == null) return;
            if (result.isFailure()) {
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_PLAN2)) {
                Utilities.showAlertDailog(activity, "PunnyFuzzles", "Payment Successfully Done.", "Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }, false);
            } else if (purchase.getSku().equals(SKU_PLAN1)) {
                Utilities.showAlertDailog(LoginActivity.this, "PunnyFuzzles", "PayPal Payment Successfully Done.", "Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }, false);

            }

        }
    };
    All_Pets_List_Model petObject;
    lostFoundPetListModel lostFoundPetListModel;
    AdoptPetModel adoptPetModel;

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_btn_img);
        final Intent intent = getIntent();
        from = intent.getStringExtra("key");
        System.out.println("fromstr" + from);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        emailvalid = (EditText) findViewById(R.id.activity_login_et_email);
        passwordvalid = (EditText) findViewById(R.id.activity_login_et_password);
        mSignUpButton = (Button) findViewById(R.id.activity_login_btn_register);
        mLoginButton = (Button) findViewById(R.id.activity_login_btn_login);
        tv_forgot_pwd = (TextView) findViewById(R.id.tv_forgot_pwd);
        Bundle bundleObject = getIntent().getExtras();
        try {
            // Get ArrayList Bundle
            petObject = (All_Pets_List_Model) bundleObject.getSerializable("petObject");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getIntent().hasExtra("arraylist")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                all_pets_list_models = (ArrayList<All_Pets_List_Model>) bundle.getSerializable("arraylist");
                System.out.println("All_pets_list_models" + all_pets_list_models.size());

            } else {
                Log.e("null", "null");
            }
        }
        if (getIntent().hasExtra("petobject")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                petObj = (All_Pets_List_Model) bundle.getSerializable("petobject");
                System.out.println("PetObj" + petObj.toString());
            } else {
                Log.e("null", "null");
            }


        }

        if (getIntent().hasExtra("Position")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                position = bundle.getInt("Position");
                System.out.println("Position in bundle" + position);
            } else

            {
                Log.e("null", "null");
            }
        }


        if (getIntent().hasExtra("arraylistAdoption")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                all_pets_list_modelsAdoption = (ArrayList<AdoptPetModel>) bundle.getSerializable("arraylistAdoption");

            } else {
                Log.e("null", "null");
            }
        }
        if (getIntent().hasExtra("petobjectAdoption")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                petObjAdoption = (AdoptPetModel) bundle.getSerializable("petobjectAdoption");
            } else

            {
                Log.e("null", "null");
            }
        }

        if (getIntent().hasExtra("PositionAdoption")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                positionAdoption = bundle.getInt("PositionAdoption");
            } else

            {
                Log.e("null", "null");
            }
        }

        if (getIntent().hasExtra("arraylistLostFound")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                all_pets_list_modelsLostFound = (ArrayList<lostFoundPetListModel>) bundle.getSerializable("arraylistLostFound");

            } else {
                Log.e("null", "null");
            }
        }
        if (getIntent().hasExtra("petobjectLostFound")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                petObjLostFound = (lostFoundPetListModel) bundle.getSerializable("petobjectLostFound");
            } else

            {
                Log.e("null", "null");
            }
        }

        if (getIntent().hasExtra("PositionLostfound")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                positionLostFound = bundle.getInt("PositionLostfound");
            } else

            {
                Log.e("null", "null");
            }
        }
        try {
            // Get ArrayList Bundle
            lostFoundPetListModel = (lostFoundPetListModel) bundleObject.getSerializable("lostFoundPetListModel");

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // Get ArrayList Bundle
            adoptPetModel = (AdoptPetModel) bundleObject.getSerializable("adoptPetModel");

        } catch (Exception e) {
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(

                new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
/*
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);*/
                        finish();
                    }

                }

        );
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(i);
            }
        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  final String email = "miketaylor@gmail.com";//emailvalid.getText().toString().trim();
                // final String  password= "12345";//passwordvalid.getText().toString();
         /*      Intent intent = new Intent(LoginActivity.this, Home_Activity.class);
                startActivity(intent);*/
                connectionDetector = new ConnectionDetector(getApplicationContext());
                if (connectionDetector.isConnectingToInternet()) {
                    if (!emailvalid.getText().toString().matches(emailPattern) && emailvalid.getText().toString().length() == 0) {
                        displayAlertDialog("Please enter valid email address");
                    } else if (passwordvalid.getText().toString().length() == 0) {
                        displayAlertDialog("Please enter password");
                    } else {
                        emailID = emailvalid.getText().toString();
                        password = passwordvalid.getText().toString();
                        progress = new ProgressDialog(LoginActivity.this);
                        progress.setMessage("Please wait..");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        progress.show();
                        login_service();
                    }
                } else {
                    showAlertDialog(LoginActivity.this, "Internet Connection",
                            "Please connect to internet. ");
                }

                System.out.println("in login press success");

             /*   email = emailvalid.getText().toString().trim();
                password = passwordvalid.getText().toString();*/

            }
        });
        tv_forgot_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent1);
                //finish();
            }
        });
    }

    public void login_service() {

        // displayAlertDialog("Password is sent on your Email ID");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebURL.LOGIN_SERVICE_URL + emailID + "&password=" + password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                final String paymentDoneStatus = "";
                try {
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                    Log.i("Forgot", response);

                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject jsonObject2 = jsonObject.getJSONObject("response");
                    int msg_code = jsonObject.getInt("flag");

                    if (msg_code == 1) {

                        //displayAlertDialog(jsonObject.getString("msg"));
                        JSONObject obj3 = jsonObject2.getJSONObject("user_details");
                        editor = pref.edit();
                        user_Id = obj3.getString("user_id");
                        isPaymentDone = obj3.getString("is_payment");

                        Intent intent = null;

                      /*  if (isPaymentDone.equals("0")) {
                            inAPpOnCreate();
                   *//*     } else {*/

                        editor.putString("userid", user_Id);
                        editor.putString("is_payment", isPaymentDone);
                        editor.putString("user_email", obj3.getString("email"));
                        editor.commit();

                        if (from.equals("PetListActivity")) {
                            intent = new Intent(LoginActivity.this, BuyOrSalePetsActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (from.equals("PetDetails")) {
                            intent = new Intent(LoginActivity.this, PetDetails.class);
                            Bundle bundleObject = new Bundle();
                            bundleObject.putSerializable("petObject", petObject);
                            intent.putExtras(bundleObject);
                            startActivity(intent);
                            finish();
                        } else if (from.equals("BuySaleSwipePetDetails")) {
                            intent = new Intent(LoginActivity.this, BuySaleDetailsSwipeListActivity.class);
                            Bundle bundleObject = new Bundle();
                            bundleObject.putSerializable("petobject", petObj);
                            bundleObject.putSerializable("arraylist", all_pets_list_models);
                            bundleObject.putInt("Position", position);
                            intent.putExtras(bundleObject);
                            startActivity(intent);
                            finish();
                        } else if (from.equals("AdoptionPetSwipeDetails")) {
                            intent = new Intent(LoginActivity.this, AdoptionDetailsSwipeListActivity.class);
                            Bundle bundleObject = new Bundle();
                            bundleObject.putSerializable("petobject", petObjAdoption);
                            bundleObject.putSerializable("arraylist", all_pets_list_modelsAdoption);
                            bundleObject.putInt("Position", positionAdoption);
                            intent.putExtras(bundleObject);
                            startActivity(intent);
                            finish();
                        } else if (from.equals("LostFoundPetSwipeDetails")) {
                            intent = new Intent(LoginActivity.this, LostFoundDetailsSwipeListActivity.class);
                            Bundle bundleObject = new Bundle();
                            bundleObject.putSerializable("petobject", petObjLostFound);
                            bundleObject.putSerializable("arraylist", all_pets_list_modelsLostFound);
                            bundleObject.putInt("Position", positionLostFound);
                            intent.putExtras(bundleObject);
                            startActivity(intent);
                            finish();
                        } else if (from.equals("BuyOrSalePets")) {
                            intent = new Intent(LoginActivity.this, PetListDemoActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (from.equals("ReportAPet")) {
                            intent = new Intent(LoginActivity.this, LostFoundPeDemotList.class);
                            startActivity(intent);
                            finish();
                        } else if (from.equals("LostFoundPetList")) {
                            intent = new Intent(LoginActivity.this, ReportAPetActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (from.equals("LostFoundPetDetails")) {
                            intent = new Intent(LoginActivity.this, LostFoundPetDetails.class);
                            Bundle bundleObject = new Bundle();
                            bundleObject.putSerializable("lostFoundPetListModel", lostFoundPetListModel);
                            intent.putExtras(bundleObject);
                            startActivity(intent);
                            finish();
                        } else if (from.equals("AdoptionPetList")) {
                            intent = new Intent(LoginActivity.this, AddAPetActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (from.equals("AddAPetActivity")) {
                            intent = new Intent(LoginActivity.this, AdoptionDemoPetList.class);
                            startActivity(intent);
                            finish();
                        } else if (from.equals("AdoptPetDetails")) {
                            intent = new Intent(LoginActivity.this, AdoptPetDetails.class);
                            Bundle bundleObject = new Bundle();
                            bundleObject.putSerializable("adoptPetModel", adoptPetModel);
                            intent.putExtras(bundleObject);
                            startActivity(intent);
                            finish();
                        } else if (from.equals("Dashboard")) {
                            intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (from.equals("BuySaleActivity")) {
                            intent = new Intent(LoginActivity.this, BuyOrSalePetsActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        //}


                    } else {
                        displayAlertDialog(jsonObject2.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("JSONException = " + e.toString());

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error = " + error.toString());
            }


        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void displayAlertDialog(String msg) {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setMessage(msg);
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            android.support.v7.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        // Setting alert dialog icon
        // alertDialog.setIcon((status) ? R.drawable. : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("" + message);
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
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this, R.style.MyAlertDialogStyle);

                            //  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity_save_data.this);
                            builder.setMessage(res)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent;
                                            if (from.equals("PetListActivity")) {
                                                intent = new Intent(LoginActivity.this, BuyOrSalePetsActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else if (from.equals("PetDetails")) {
                                                finish();
                                            } else if (from.equals("BuyOrSalePets")) {
                                                intent = new Intent(LoginActivity.this, PetListDemoActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else if (from.equals("ReportAPet")) {
                                                intent = new Intent(LoginActivity.this, LostFoundPeDemotList.class);
                                                startActivity(intent);
                                                finish();
                                            } else if (from.equals("LostFoundPetList")) {
                                                intent = new Intent(LoginActivity.this, ReportAPetActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else if (from.equals("LostFoundPetDetails")) {
                                                finish();
                                            } else if (from.equals("AdoptionPetList")) {
                                                intent = new Intent(LoginActivity.this, AddAPetActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else if (from.equals("AddAPetActivity")) {
                                                intent = new Intent(LoginActivity.this, AdoptionDemoPetList.class);
                                                startActivity(intent);
                                                finish();
                                            } else if (from.equals("AdoptPetDetails")) {
                                                finish();
                                            }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("rescode" + resultCode);
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


}
