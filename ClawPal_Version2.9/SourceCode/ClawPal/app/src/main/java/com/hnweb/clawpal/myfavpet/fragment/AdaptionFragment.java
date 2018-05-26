package com.hnweb.clawpal.myfavpet.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hnweb.clawpal.adaption.activity.AdoptPetDetails;
import com.hnweb.clawpal.AppController;
import com.hnweb.clawpal.adaption.activity.AdoptionDetailsSwipeListActivity;
import com.hnweb.clawpal.adaption.model.AdoptPetModel;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.myfavpet.adaptor.MyAdoptPetFavAdaptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Shree on 06-Dec-17.
 */

public class AdaptionFragment extends Fragment {
    ArrayList<AdoptPetModel> adoptPetlist;
    MyAdoptPetFavAdaptor myPet_list_fav_adaptor;
    ListView listView;

    SharedPreferences sharedPreferences;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pet_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.myListView);
        sharedPreferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        userID = sharedPreferences.getString("userid", null);

        getAllPetsAdoptList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdoptPetModel list_model = myPet_list_fav_adaptor.getItem(position);
                Intent intent2 = new Intent(getActivity(), AdoptionDetailsSwipeListActivity.class);
              /*  Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("adoptPetModel", list_model);
                intent2.putExtras(bundleObject);*/
                Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("arraylist", adoptPetlist);
                bundleObject.putSerializable("petobject", list_model);
                bundleObject.putInt("Position", position);
                System.out.println("Position in adapter" + position);
                // bundleObject.putSerializable("petObject", service_info);
                intent2.putExtras(bundleObject);
                startActivity(intent2);
            }
        });
        return rootView;
    }

    public void getAllPetsAdoptList() {
        String url = WebURL.GET_MYFAV_ADAPTION_PETLIST + "user_id=" + userID;
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {

                    if (response.getInt("flag") == 1) {
                        progressDialog.dismiss();
                        System.out.println("response123" + response);
                        adoptPetlist = new ArrayList<>();
                        JSONArray jsonArray = response.getJSONArray("response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            adoptPetlist.add(new AdoptPetModel(jsonObject.getString("pet_adoption_id"), jsonObject.getString("user_id"), jsonObject.getString("pet_adoption_photo"),
                                    jsonObject.getString("gender"), jsonObject.getString("short_description"), jsonObject.getString("location"),
                                    jsonObject.getString("state"), jsonObject.getString("age_range"), jsonObject.getString("price_range"),
                                    jsonObject.getString("breed_type"), jsonObject.getString("size_type"), jsonObject.getString("compatibility"),
                                    jsonObject.getString("neutered"), jsonObject.getString("vaccinated"), jsonObject.getString("contact"),
                                    jsonObject.getString("added_by"), jsonObject.getString("adoption_type"), jsonObject.getString("type_of_animal"), jsonObject.getString("address"),
                                    jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("adoption_date"), jsonObject.getString("email"), jsonObject.getString("country"), jsonObject.getString("zip")));
                            System.out.println("Date...." + jsonObject.getString("adoption_date").toString());
                        }
                        myPet_list_fav_adaptor = new MyAdoptPetFavAdaptor(getActivity(), adoptPetlist);
                        listView.setAdapter(myPet_list_fav_adaptor);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Record Not Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, "jreq");
    }

}
