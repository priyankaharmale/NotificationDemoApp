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
import com.hnweb.clawpal.AppController;
import com.hnweb.clawpal.BuyorSale.activity.BuySaleDetailsSwipeListActivity;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.BuyorSale.activity.PetDetails;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.myfavpet.adaptor.MyPet_list_Fav_Adaptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Shree on 06-Dec-17.
 */

public class BuySaleFragment extends Fragment {
    ArrayList<All_Pets_List_Model> all_pets_list_models;
    MyPet_list_Fav_Adaptor myPet_list_fav_adaptor;
    ListView listView;
    String userID;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pet_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.myListView);

        sharedPreferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        userID = sharedPreferences.getString("userid", null);
        getAll_Pets();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                All_Pets_List_Model all_pets_list_model = myPet_list_fav_adaptor.getItem(position);
               Intent intent2 = new Intent(getActivity(), BuySaleDetailsSwipeListActivity.class);
               /* Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("petObject", all_pets_list_model);
                intent2.putExtras(bundleObject);*/

                Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("arraylist", all_pets_list_models);
                bundleObject.putSerializable("petobject", all_pets_list_model);
                bundleObject.putInt("Position", position);
                System.out.println("Position in adapter"+position);
                // bundleObject.putSerializable("petObject", service_info);
                intent2.putExtras(bundleObject);
                startActivity(intent2);
            }
        });

        return rootView;
    }
    public void getAll_Pets() {
        final  ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        try {
            String url = WebURL.GET_MYFAV_BUYSALE_PETLIST + "user_id="+userID;

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    try {
                        System.out.println("response123" + response);
                        if (response.getInt("flag")==1) {
                            progressDialog.dismiss();
                            all_pets_list_models = new ArrayList<>();
                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                all_pets_list_models.add(new All_Pets_List_Model(jsonObject.getString("pet_sale_buy_id"), jsonObject.getString("type"), jsonObject.getString("animal"), jsonObject.getString("image"), jsonObject.getString("title"),
                                        jsonObject.getString("gender"), jsonObject.getString("description"), jsonObject.getString("age_range"),
                                        jsonObject.getString("price_range"), jsonObject.getString("locality"), jsonObject.getString("city"),
                                        jsonObject.getString("breed_type"), jsonObject.getString("category"), jsonObject.getString("currency"),
                                        jsonObject.getString("email"), jsonObject.getString("name"), jsonObject.getString("neutered"),
                                        jsonObject.getString("vaccinated"), jsonObject.getString("contact"), jsonObject.getString("added_by"),
                                        jsonObject.getString("date"), jsonObject.getString("latitude"), jsonObject.getString("longitude"),jsonObject.getString("state"),jsonObject.getString("country"),jsonObject.getString("zip")));
                                System.out.println("email" + jsonObject.getString("email"));
                            }
                            myPet_list_fav_adaptor = new MyPet_list_Fav_Adaptor(getActivity(), all_pets_list_models);
                            //	note : this should come next to loading view
                            listView.setAdapter(myPet_list_fav_adaptor);

                        } else {
                            Toast.makeText(getActivity(), "Record Not Found", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
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
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, "jreq");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
