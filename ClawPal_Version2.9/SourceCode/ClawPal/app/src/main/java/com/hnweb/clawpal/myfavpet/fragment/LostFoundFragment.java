package com.hnweb.clawpal.myfavpet.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hnweb.clawpal.lostorfound.pet.activity.LostFoundDetailsSwipeListActivity;
import com.hnweb.clawpal.lostorfound.pet.activity.LostFoundPetDetails;
import com.hnweb.clawpal.lostorfound.pet.model.lostFoundPetListModel;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.myfavpet.adaptor.MyLostFoundPetsFavAdaptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Shree on 06-Dec-17.
 */

public class LostFoundFragment extends Fragment {
    ArrayList<lostFoundPetListModel> all_pets_list_models;
    MyLostFoundPetsFavAdaptor myPet_list_fav_adaptor;
    ListView listView;
    ProgressDialog progressDialog;
    int flag;
    String userID;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pet_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.myListView);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        sharedPreferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        userID = sharedPreferences.getString("userid", null);
        getAllPetsLostAndFoundNew();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lostFoundPetListModel list_model = myPet_list_fav_adaptor.getItem(position);
                Intent intent2 = new Intent(getActivity(), LostFoundDetailsSwipeListActivity.class);
              /*  Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("lostFoundPetListModel", list_model);
                intent2.putExtras(bundleObject);*/

                Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("arraylist", all_pets_list_models);
                bundleObject.putSerializable("petobject", list_model);
                bundleObject.putInt("Position", position);
                System.out.println("Position in adapter"+position);
                // bundleObject.putSerializable("petObject", service_info);
                intent2.putExtras(bundleObject);
                startActivity(intent2);
            }
        });
        return rootView;
    }

    public void getAllPetsLostAndFoundNew() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        String url = WebURL.GET_MYFAV_LOSTFOUND_PETLIST + "user_id=" + userID;
        System.out.println("Lostfoundurl" + url);

        try {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    progressDialog.dismiss();
                    try {
                        Log.i("reponce", response.toString());
                        System.out.println("Lostfoundurl" + response.toString());

                        int records= response.getInt("records");
                        if (response.getInt("flag")==1) {
                            progressDialog.dismiss();
                            System.out.println("response123Lostfound" + response);

                            all_pets_list_models = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                all_pets_list_models.add(new lostFoundPetListModel(jsonObject.getString("lost_found_id"), jsonObject.getString("title"), jsonObject.getString("name"),
                                        jsonObject.getString("age_range"), jsonObject.getString("gender"), jsonObject.getString("type_of_animal"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("report_type"), jsonObject.getString("description"),
                                        jsonObject.getString("location"), jsonObject.getString("state"), jsonObject.getString("image"),
                                        jsonObject.getString("reporter_name"), jsonObject.getString("reporter_email"), jsonObject.getString("reporter_contact"),
                                        jsonObject.getString("report_date_time"), jsonObject.getString("address"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("Email..." + jsonObject.getString("reporter_email"));
                            }
                            myPet_list_fav_adaptor = new MyLostFoundPetsFavAdaptor(getActivity(), all_pets_list_models);
                            listView.setAdapter(myPet_list_fav_adaptor);


                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Record Not Found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("Exceptionjson" + e.toString());

                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("ErrorVolley" + error.toString());

                }
            });
            // Adding request to request queue
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

}
