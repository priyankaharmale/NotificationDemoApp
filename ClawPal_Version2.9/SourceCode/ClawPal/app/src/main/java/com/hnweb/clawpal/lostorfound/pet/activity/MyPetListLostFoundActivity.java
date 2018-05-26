package com.hnweb.clawpal.lostorfound.pet.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.lostorfound.pet.adaptor.MyLostFoundPetsAdaptor;
import com.hnweb.clawpal.lostorfound.pet.model.lostFoundPetListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shree on 24-Nov-17.
 */

public class MyPetListLostFoundActivity extends AppCompatActivity implements TextWatcher{
    MyLostFoundPetsAdaptor FoundPetsAdaptor;
    ArrayList<lostFoundPetListModel> lostFoundPetList;
    ProgressDialog progress;
    ListView listView;
    String userId;
    String petid;
    int flag;
    String message;
    String responsetext;
    TextView tv_norecord;
    Toolbar toolbar;
    ImageView iv_dog, iv_cat, iv_search;
    EditText mTvSearch;
    SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_myprofilelist);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_btn_img);
        iv_dog = (ImageView) findViewById(R.id.iv_dog);
        iv_cat = (ImageView) findViewById(R.id.iv_cat);
        iv_search = (ImageView) findViewById(R.id.iv_search);

        mTvSearch = (EditText) findViewById(R.id.searchtxt);
        toolbar.setNavigationOnClickListener(

                new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {

                        finish();
                    }

                }

        );
        FoundPetsAdaptor = new MyLostFoundPetsAdaptor(MyPetListLostFoundActivity.this, lostFoundPetList);
        tv_norecord = (TextView) findViewById(R.id.tv_norecord);
        listView = (ListView) findViewById(R.id.myListView);

        progress = new ProgressDialog(MyPetListLostFoundActivity.this);
        progress.setMessage("Please Wait.....");

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
        System.out.println("User Id" + userId);
        getBuy_Sale_Pets_List();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                lostFoundPetListModel all_pets_list_model = FoundPetsAdaptor.getItem(pos);
                petid = all_pets_list_model.getLost_found_id();
                showAlertDialog();


                return true;
            }
        });
        iv_dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getlist("Dog");
            }
        });
        iv_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // popupwindow(view);
                getlist("Cat");
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
                    getlist(mTvSearch.getText().toString());
                } else {
                    Toast.makeText(MyPetListLostFoundActivity.this, "Please enter search text.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mTvSearch.addTextChangedListener(this);

        mTvSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mTvSearch.setImeActionLabel("Search", EditorInfo.IME_ACTION_SEARCH);

        mTvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    getlist(mTvSearch.getText().toString());

                    return true;
                }
                return false;
            }
        });
    }

    public void getBuy_Sale_Pets_List() {
        progress.show();
        try {
            String url = WebURL.GETMY_LOSTFOUND_PETLIST + userId;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();

                    System.out.println("response obj12" + response.toString());
                    try {
                        flag = response.getInt("flag");

                        if (flag == 1) {
                            lostFoundPetList = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");
                            System.out.println("");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                lostFoundPetList.add(new lostFoundPetListModel(jsonObject.getString("lost_found_id"), jsonObject.getString("title"), jsonObject.getString("name"),
                                        jsonObject.getString("age_range"), jsonObject.getString("gender"), jsonObject.getString("type_of_animal"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("report_type"), jsonObject.getString("description"),
                                        jsonObject.getString("location"), jsonObject.getString("state"), jsonObject.getString("image"),
                                        jsonObject.getString("reporter_name"), jsonObject.getString("reporter_email"), jsonObject.getString("reporter_contact"),
                                        jsonObject.getString("report_date_time"), jsonObject.getString("address"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("country"),jsonObject.getString("zip")));
                            }

                            FoundPetsAdaptor = new MyLostFoundPetsAdaptor(MyPetListLostFoundActivity.this, lostFoundPetList);
                            listView.setAdapter(FoundPetsAdaptor);
                            tv_norecord.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.GONE);
                            tv_norecord.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("Expections.." + e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Errror.." + error);
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjReq, "jreq");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyPetListLostFoundActivity.this);
        builder.setTitle("Message")
                .setMessage("Are you sure want to Delete? ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        delete();
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void delete() {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.DELETE_MY_LOSTFOUND_PETLIST + petid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        System.out.println("response" + response.toString());
                        try {
                            JSONObject j = new JSONObject(response);
                            flag = j.getInt("flag");
                            responsetext = j.getString("response");
                            System.out.println("message123" + response);
                            if (flag == 1) {
                                responsetext = j.getString("response");
                                System.out.println("message23432" + responsetext);
                                AlertDialog.Builder builder = new AlertDialog.Builder(MyPetListLostFoundActivity.this);
                                builder.setMessage(responsetext)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finish();
                                                startActivity(getIntent());
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
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("jsonexeption" + error.toString());
                        progress.dismiss();

                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void getlist(String location) {
        progress.show();
        try {
            String url = WebURL.SEARCH_MYLOSTFOUND_FROM_KEYWORD + "keyword=" + location + "&user_id=" + userId;
            String final_string = url.replace(" ", "%20");

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, final_string,
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
                            FoundPetsAdaptor = new MyLostFoundPetsAdaptor(MyPetListLostFoundActivity.this, lostFoundPetList);
                            listView.setAdapter(FoundPetsAdaptor);


                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(MyPetListLostFoundActivity.this, "Record not found.", Toast.LENGTH_SHORT).show();
                            lostFoundPetList = new ArrayList<>();
                            FoundPetsAdaptor = new MyLostFoundPetsAdaptor(MyPetListLostFoundActivity.this, lostFoundPetList);
                            listView.setAdapter(FoundPetsAdaptor);
                            Toast.makeText(MyPetListLostFoundActivity.this, "No record found.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("Expections123.." + e);

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
}
