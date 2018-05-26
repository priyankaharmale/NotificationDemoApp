package com.hnweb.clawpal.adaption.activity;

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
import com.hnweb.clawpal.adaption.adaptor.MyAdoptPetAdaptor;
import com.hnweb.clawpal.adaption.model.AdoptPetModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shree on 24-Nov-17.
 */

public class MyPetListAdaptioActivity extends AppCompatActivity implements TextWatcher {
    ProgressDialog progress;
    ListView listView;
    String userId;
    String petid;
    MyAdoptPetAdaptor adoptPetAdaptor;
    ArrayList<AdoptPetModel> adoptPetlist;

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
        adoptPetAdaptor = new MyAdoptPetAdaptor(MyPetListAdaptioActivity.this, adoptPetlist);
        tv_norecord = (TextView) findViewById(R.id.tv_norecord);
        listView = (ListView) findViewById(R.id.myListView);

        progress = new ProgressDialog(MyPetListAdaptioActivity.this);
        progress.setMessage("Please Wait.....");

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
        getBuy_Sale_Pets_List();
       /* listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                AdoptPetModel all_pets_list_model = adoptPetAdaptor.getItem(pos);
                petid = all_pets_list_model.getPet_adoption_id();
                showAlertDialog();
                return true;
            }
        });*/

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
                    Toast.makeText(MyPetListAdaptioActivity.this, "Please enter search text.", Toast.LENGTH_SHORT).show();
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
            String url = WebURL.GETMY_ADAPTION_PETLIST + userId;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();

                    System.out.println("response obj" + response.toString());
                    try {
                        flag = response.getInt("flag");

                        if (flag == 1) {
                            adoptPetlist = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");
                            System.out.println("");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                adoptPetlist.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                        jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                        jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                        jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                        jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                        jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"),jsonObject.getString("email"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("Address..." + jsonObject.getString("address"));
                            }


                            adoptPetAdaptor = new MyAdoptPetAdaptor(MyPetListAdaptioActivity.this, adoptPetlist);
                            listView.setAdapter(adoptPetAdaptor);
                            tv_norecord.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.GONE);
                            tv_norecord.setVisibility(View.VISIBLE);
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
            AppController.getInstance().addToRequestQueue(jsonObjReq, "jreq");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyPetListAdaptioActivity.this);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.DELETE_MY_ADAPTION_PETLIST + petid,
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(MyPetListAdaptioActivity.this);
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
            String url = WebURL.SEARCH_MYADAPTION_FROM_KEYWORD + "keyword=" + location+ "&user_id=" +userId;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();
                    try {

                        if (progress.isShowing())
                        {
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


                            adoptPetAdaptor = new MyAdoptPetAdaptor(MyPetListAdaptioActivity.this, adoptPetlist);
                            listView.setAdapter(adoptPetAdaptor);
                            // ((RadioButton) radioGroup.findViewById(R.id.radioButton2)).setChecked(true);

                        } else {
                            Toast.makeText(MyPetListAdaptioActivity.this, "Record not found.", Toast.LENGTH_SHORT).show();

                            adoptPetlist = new ArrayList<>();
                            adoptPetAdaptor = new MyAdoptPetAdaptor(MyPetListAdaptioActivity.this, adoptPetlist);
                            listView.setAdapter(adoptPetAdaptor);
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
}
