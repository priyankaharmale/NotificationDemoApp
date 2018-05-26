package com.hnweb.clawpal.productbuysale.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.hnweb.clawpal.BuyorSale.adaptor.Pet_list_Adaptor;
import com.hnweb.clawpal.AppController;
import com.hnweb.clawpal.BuyorSale.activity.BuyOrSalePetsActivity;
import com.hnweb.clawpal.DashboardActivity;
import com.hnweb.clawpal.GPSTracker;
import com.hnweb.clawpal.login.LoginActivity;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.Utils.RowItem;
import com.hnweb.clawpal.BuyorSale.activity.PetDetails;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.WebUrl.WebURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HNWeb-11 on 7/22/2016.
 */
public class ProductListActivity extends AppCompatActivity   {
    Toolbar toolbar;
    TextView mTvTitle;
    Intent intent;
    ProgressDialog progress;
    Button mBtnBuyOrSale;
    ListView eLView;
    EditText mTvSearch;
    ArrayList<All_Pets_List_Model> pets_list;
    Pet_list_Adaptor pet_list_adaptor;
    ImageView iv_search, iv_dog, iv_cat;
    String title;
    SharedPreferences pref;
    Button loadmore;
    String search_type = "";
    boolean loadingMore = false;
    LinearLayout linearLayout, ll_top2, ll_top23;
    private RadioGroup radioGroup, radioGroup2, radioGroup23;
    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if (pets_list != null && pets_list.size() > 0) {

                for (int i = 0; i < pets_list.size(); i++)
                    pet_list_adaptor.add(pets_list.get(i));}
            setTitle("Never ending List with " + String.valueOf(pet_list_adaptor.getCount()) + " items");
            pet_list_adaptor.notifyDataSetChanged();
            loadingMore = false;
        }

    };

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        try {
            setContentView(R.layout.activity_product_list);
            getInit();
            progress = new ProgressDialog(ProductListActivity.this);
            progress.setMessage("Please wait...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
           // progress.show();
            eLView = (ListView) findViewById(R.id.myListView);
            View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_view, null, false);
            eLView.addFooterView(footerView);
            loadmore = (Button) footerView.findViewById(R.id.button);
            loadmore.setVisibility(View.GONE);
            loadmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                }
            });

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            mTvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbar.setNavigationIcon(R.drawable.back_btn_img);

            intent = getIntent();
            title = intent.getStringExtra("title");
            mTvTitle.setText(title);
            iv_search = (ImageView) findViewById(R.id.iv_search);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);


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
                        progress = new ProgressDialog(ProductListActivity.this);
                        progress.setMessage("Please wait");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                      //  progress.show();
                    }

                }
            });

            iv_dog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            iv_cat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                     //   progress.show();
                    } else {
                        Toast.makeText(ProductListActivity.this, "Please enter search text.", Toast.LENGTH_SHORT).show();
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
                        progress = new ProgressDialog(ProductListActivity.this);
                        progress.setMessage("Please wait...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        progress.setCancelable(false);
                        //progress.show();
                    }

                }
            });
            toolbar.setNavigationOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ProductListActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
            );

            eLView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });


            mBtnBuyOrSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    String userID = pref.getString("userid", null);
                    if (userID != null) {
                        Intent intent = new Intent(ProductListActivity.this, ProductBuyOrSalePetsActivity.class);
                        startActivity(intent);
                    } else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        Intent intent = new Intent(ProductListActivity.this, LoginActivity.class);
                                        intent.putExtra("key", "ProductPetListActivity");
                                        startActivity(intent);
                                    case DialogInterface.BUTTON_NEGATIVE:

                                        // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
                        builder.setTitle("Message");
                        builder.setCancelable(false);
                        builder.setMessage("Please Login ").setPositiveButton("Login", dialogClickListener).setNegativeButton("No", dialogClickListener).show();


                    }
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
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ProductListActivity.this,DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}