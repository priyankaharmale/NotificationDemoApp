package com.hnweb.clawpal.lostorfound.pet.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.adaption.adaptor.AnimalPicAdaptor;
import com.hnweb.clawpal.login.LoginActivity;
import com.hnweb.clawpal.lostorfound.pet.adaptor.MySwipeAdaptor;
import com.hnweb.clawpal.lostorfound.pet.model.lostFoundPetListModel;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shree on 04-Dec-17.
 */

public class LostFoundDetailsSwipeListActivity extends FragmentActivity implements ActionBar.TabListener {

    public static MySwipeAdaptor myAppAdapter;

    Toolbar toolbar;
    AnimalPicAdaptor animalPicAdaptor;
    ImageView iv_fav;
    String userID;
    int position1;
    List<String> images = new ArrayList<>();
    String petId;
    ImageView iv_correct,iv_wrong;

    RecyclerView recycler_view_petImages;
    SharedPreferences sharedPrefs;
    lostFoundPetListModel petObj;
    int position;
    private ArrayList<lostFoundPetListModel> all_pets_list_models;
    private SwipeFlingAdapterView flingContainer;
    ProgressDialog progressDialog;
    String isClick = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        setContentView(R.layout.activity_swipe_layout_lostfound);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_fav=(ImageView) findViewById(R.id.iv_fav);
        userID = sharedPrefs.getString("userid", null);
        toolbar.setNavigationIcon(R.drawable.back_btn_img);
        iv_correct=(ImageView) findViewById(R.id.iv_correct);
        iv_wrong=(ImageView) findViewById(R.id.iv_wrong);

        recycler_view_petImages = (RecyclerView) findViewById(R.id.recycler_view_petImages);
        progressDialog = new ProgressDialog(LostFoundDetailsSwipeListActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        toolbar.setNavigationOnClickListener(

                new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {

                        finish();
                    }

                }

        );
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        if (getIntent().hasExtra("arraylist")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                all_pets_list_models = (ArrayList<lostFoundPetListModel>) bundle.getSerializable("arraylist");
                System.out.println("All_pets_list_models" + all_pets_list_models.size());

            } else {
                Log.e("null", "null");
            }
        }
        if (getIntent().hasExtra("petobject")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                petObj = (lostFoundPetListModel) bundle.getSerializable("petobject");
                System.out.println("PetObj" + petObj.toString());
            } else

            {
                Log.e("null", "null");
            }
        }

        if (getIntent().hasExtra("Position")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                position = bundle.getInt("Position");
                System.out.println("Position" + position);
            } else

            {
                Log.e("null", "null");
            }
        }
        petId = petObj.getLost_found_id();
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(all_pets_list_models);

        editor.putString("arraylist", json);
        editor.putString("petId", petId);
        String json2 = gson.toJson(petObj);
        editor.putString("MyObject", json2);

        editor.commit();
      /*  if (getIntent().hasExtra("position")) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                position1 = bundle.getInt("position");
            }
        }*/

        iv_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingContainer.getTopCardListener().selectLeft();
            }
        });
        iv_wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingContainer.getTopCardListener().selectRight();
            }
        });
        iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userID==null)
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:

                                    Intent intent = new Intent(LostFoundDetailsSwipeListActivity.this, LoginActivity.class);
                                    intent.putExtra("key", "LostFoundPetSwipeDetails");
                                    Bundle bundleObject = new Bundle();

                                    bundleObject.putSerializable("petobjectLostFound", petObj);
                                    bundleObject.putSerializable("arraylistLostFound", all_pets_list_models);
                                    bundleObject.putInt("PositionLostfound", position);
                                    intent.putExtras(bundleObject);
                                    startActivity(intent);
                                    finish();


                                case DialogInterface.BUTTON_NEGATIVE:

                                    // No button clicked // do nothing Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(LostFoundDetailsSwipeListActivity.this);
                    builder.setTitle("Message");
                    builder.setMessage("Please Login to view Contact Details").setPositiveButton("Login", dialogClickListener).setNegativeButton("No", dialogClickListener).show();

                }
                else
                {
                    if (isClick.equals("1")) {
                        iv_fav.setImageResource(R.drawable.ic_favoritefill);
                        isClick = "0";
                        addFav();
                    } else {
                        iv_fav.setImageResource(R.drawable.favorite_white);
                        isClick = "1";
                        remove();
                    }
                }

            }
        });

        if (userID != null) {
            //iv_fav.setVisibility(View.VISIBLE);
            isfav();
        } else {
           // iv_fav.setVisibility(View.GONE);
        }


        myAppAdapter = new MySwipeAdaptor(all_pets_list_models, LostFoundDetailsSwipeListActivity.this);


        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

        try {

                System.out.println("right previous position" + position);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                isClick = "1";
                Gson gson2 = new Gson();
                //    position++;
                petObj = all_pets_list_models.get(position + 1);
                petId = petObj.getLost_found_id();

                if (userID != null) {
                   // iv_fav.setVisibility(View.VISIBLE);
                    isfav();
                } /*else {
                    iv_fav.setVisibility(View.GONE);
                }*/
                String json = gson2.toJson(petObj);
                editor.putString("MyObject", json);
                editor.commit();
                System.out.println("right after position" + position);
                all_pets_list_models.remove(position);
                myAppAdapter.notifyDataSetChanged();

            }catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
                Toast.makeText(LostFoundDetailsSwipeListActivity .this,"No Data To Swipe", Toast.LENGTH_LONG).show();

                System.out.println("JsonException"+ e);
            }

        }

            @Override
            public void onRightCardExit(Object dataObject) {

                try {

                    System.out.println("right previous position" + position);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    isClick = "1";
                    Gson gson2 = new Gson();
                    //    position++;
                    petObj = all_pets_list_models.get(position + 1);
                    petId = petObj.getLost_found_id();

                    if (userID != null) {
                       // iv_fav.setVisibility(View.VISIBLE);
                        isfav();
                    } /*else {
                        iv_fav.setVisibility(View.GONE);
                    }*/
                    String json = gson2.toJson(petObj);
                    editor.putString("MyObject", json);
                    editor.commit();
                    System.out.println("right after position" + position);
                    all_pets_list_models.remove(position);
                    myAppAdapter.notifyDataSetChanged();
                }catch (IndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                    Toast.makeText(LostFoundDetailsSwipeListActivity .this,"No Data To Swipe", Toast.LENGTH_LONG).show();

                    System.out.println("JsonException"+ e);
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                myAppAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {


    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    private void addFav() {

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebURL.ADD_LOST_FOUND_FAV + "user_id=" + userID + "&pet_lost_found_id=" + petId,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        System.out.println("resadd " + response);

                        progressDialog.dismiss();
                        try {
                            JSONObject j = new JSONObject(response);

                            System.out.println("resadd" + response.toString() + response.toString());
                            String res = j.getString("response");
                            JSONObject jsonObject = new JSONObject(res);
                            String message = jsonObject.getString("msg");
                            int flag;
                            flag = jsonObject.getInt("flag");
                            System.out.println("res123" + res.toString());

                            if (flag == 1) {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LostFoundDetailsSwipeListActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {


                                            }
                                        });
                                android.support.v7.app.AlertDialog alert = builder.create();
                                alert.show();
                            }

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
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void remove() {

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebURL.REMOVE_LOSTFOUND + "user_id=" + userID + "&pet_lost_found_id=" + petId,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        System.out.println("resremove " + response);

                        progressDialog.dismiss();
                        try {
                            JSONObject j = new JSONObject(response);

                            System.out.println("resArshremove" + response.toString() + response.toString());
                            String res = j.getString("response");
                            int flag;
                            flag = j.getInt("flag");
                            System.out.println("res123" + res.toString());

                            if (flag == 1) {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(LostFoundDetailsSwipeListActivity.this);
                                builder.setMessage(res)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {


                                            }
                                        });
                                android.support.v7.app.AlertDialog alert = builder.create();
                                alert.show();
                            }

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
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void isfav() {


        String url = WebURL.IS_LOSTFOUND_FAV + "user_id=" + userID + "&pet_lost_found_id=" + petId;
        System.out.println("URLfav" + url);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                        System.out.println("resfav " + response);

                        progressDialog.dismiss();
                        try {
                            JSONObject j = new JSONObject(response);

                            System.out.println("resfav" + response.toString() + response.toString());
                            String res = j.getString("response");
                            int flag;
                            flag = j.getInt("flag");

                            if (flag == 1) {
                                iv_fav.setImageResource(R.drawable.ic_favoritefill);
                                isClick = "0";
                            } else {
                                iv_fav.setImageResource(R.drawable.favorite_white);
                                isClick = "1";

                            }

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
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
