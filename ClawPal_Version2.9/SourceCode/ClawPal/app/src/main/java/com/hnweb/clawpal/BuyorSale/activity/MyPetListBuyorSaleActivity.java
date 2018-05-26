package com.hnweb.clawpal.BuyorSale.activity;

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
import com.hnweb.clawpal.BuyorSale.adaptor.MyPet_list_Adaptor;
import com.hnweb.clawpal.AppController;
import com.hnweb.clawpal.BuyorSale.adaptor.Pet_list_Adaptor;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.WebUrl.WebURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shree on 24-Nov-17.
 */

public class MyPetListBuyorSaleActivity extends AppCompatActivity implements TextWatcher {
    MyPet_list_Adaptor pet_list_adaptor;
    ArrayList<All_Pets_List_Model> pets_list;
    ProgressDialog progress;
    ListView listView;
    String userId;
    String petid;
    int flag;
    String message;
    String responsetext;
    TextView tv_norecord;
    Toolbar toolbar;
    ImageView iv_cat,iv_dog,iv_search;
    EditText mTvSearch;
    SharedPreferences sharedPreferences;

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_myprofilelist);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_btn_img);
        iv_cat=(ImageView) findViewById(R.id.iv_cat);
        iv_dog=(ImageView)findViewById(R.id.iv_dog);
        iv_search=(ImageView)findViewById(R.id.iv_search);
        mTvSearch=(EditText) findViewById(R.id.searchtxt);
        toolbar.setNavigationOnClickListener(

                new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {

                        finish();
                    }

                }

        );

        pet_list_adaptor = new MyPet_list_Adaptor(MyPetListBuyorSaleActivity.this, pets_list);
        tv_norecord = (TextView) findViewById(R.id.tv_norecord);
        listView = (ListView) findViewById(R.id.myListView);

        progress = new ProgressDialog(MyPetListBuyorSaleActivity.this);
        progress.setMessage("Please Wait.....");

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
        getBuy_Sale_Pets_List();
     /*   listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                All_Pets_List_Model all_pets_list_model = pet_list_adaptor.getItem(pos);
                petid = all_pets_list_model.getPet_sale_buy_id();
                showAlertDialog();


                return true;
            }
        });*/


        iv_dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getList("Dog");
            }
        });
        iv_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // popupwindow(view);
                getList("Cat");
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
                    getList(mTvSearch.getText().toString());
                } else {
                    Toast.makeText(MyPetListBuyorSaleActivity.this, "Please enter search text.", Toast.LENGTH_SHORT).show();
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

                    getList(mTvSearch.getText().toString());

                    return true;
                }
                return false;
            }
        });
    }

    public void getBuy_Sale_Pets_List() {
        progress.show();
        try {
            String url = WebURL.GETMY_BUY_SALE_PETLIST + userId;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.dismiss();

                    System.out.println("response obj" + response.toString());
                    try {
                        flag = response.getInt("flag");

                        if (flag == 1) {
                            pets_list = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");
                            System.out.println("");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                pets_list.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                        jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                        jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                        jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                        jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                        jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("state"),jsonObject.getString("country"),jsonObject.getString("zip")));
                            }
                            pet_list_adaptor = new MyPet_list_Adaptor(MyPetListBuyorSaleActivity.this, pets_list);
                            listView.setAdapter(pet_list_adaptor);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MyPetListBuyorSaleActivity.this);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.DELETE_MY_BUY_SALE_PETLIST + petid,
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(MyPetListBuyorSaleActivity.this);
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

    public void getList(String location) {
        String url = WebURL.SEARCH_MYBUYSALEPET_FROM_KEYWORD + "keyword=" + location +"&user_id="+ userId;

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
                                pets_list = new ArrayList<>();
                                JSONArray jsonArray = j.getJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    pets_list.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                            jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                            jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                            jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                            jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                            jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                            jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("state"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                }
                           /* pet_list_adaptor = new Pet_list_Adaptor(PetListActivity.this, creatingItems(mult));
                            //	note : this should come next to loading view
                            eLView.setAdapter(pet_list_adaptor);*/

                                pet_list_adaptor = new MyPet_list_Adaptor(MyPetListBuyorSaleActivity.this, pets_list);
                                listView.setAdapter(pet_list_adaptor);
                            } else {
                              /*  pets_list = new ArrayList<>();
                                pet_list_adaptor = new MyPet_list_Adaptor(MyPetListBuyorSaleActivity.this, pets_list);
                                listView.setAdapter(pet_list_adaptor);*/

                                    listView.setVisibility(View.GONE);
                                    tv_norecord.setVisibility(View.VISIBLE);
                                Toast.makeText(MyPetListBuyorSaleActivity.this, "No record found.", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MyPetListBuyorSaleActivity.this, "Please Check your internet connection.", Toast.LENGTH_LONG).show();
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
