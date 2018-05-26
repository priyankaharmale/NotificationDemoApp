package com.hnweb.clawpal.BuyorSale.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Shree on 31-Jan-18.
 */

public class Tab2 extends Fragment {

    TextView tv_tab1;
    //Overriden method onCreateView

    TextView mTvTypeOfPet, mTvGender, mTvAge, mTvBreed, mTvNeutered, mTvVaccinated, mTvPrice, mTvContact_person, mTvEmail,mTvNumber, mTvCity, mTvAddress, tv_postedOn;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.tab_two, container, false);
            mTvTypeOfPet = (TextView) rootView.findViewById(R.id.tv_type_pet);
            mTvGender = (TextView) rootView.findViewById(R.id.tv_gender);
            mTvAge = (TextView) rootView.findViewById(R.id.tv_age);
            mTvBreed = (TextView) rootView.findViewById(R.id.tv_breed);
            mTvNeutered = (TextView) rootView.findViewById(R.id.tv_natuered);
            mTvVaccinated = (TextView) rootView.findViewById(R.id.tv_vaccinated);
            mTvPrice = (TextView) rootView.findViewById(R.id.tv_price);
            tv_postedOn = (TextView) rootView.findViewById(R.id.tv_postedOn);

            SharedPreferences sharedPrefs = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("arraylist", null);
            String petId = sharedPrefs.getString("petId", null);
            Type type = new TypeToken<ArrayList<All_Pets_List_Model>>() {
            }.getType();
            ArrayList<All_Pets_List_Model> arrayList = gson.fromJson(json, type);



            String json2 = sharedPrefs.getString("MyObject", "");
            All_Pets_List_Model obj = gson.fromJson(json2, All_Pets_List_Model.class);
            mTvTypeOfPet.setText(obj.getAnimal());
            mTvGender.setText(obj.getGender());
            mTvAge.setText(obj.getAge_range());
            mTvBreed.setText(obj.getBreed_type());
            mTvNeutered.setText(obj.getNeutered());
            mTvVaccinated.setText(obj.getVaccinated());
            if(obj.getCurrency().equals(""))
            {
                mTvPrice.setText("N/A");
            }else
            {
                mTvPrice.setText(obj.getCurrency());
            }
            String youString;
            int i = obj.getDate().indexOf(" ");
            youString = obj.getDate().substring(0, i);
            tv_postedOn.setText(youString);
            return rootView;

        }
}
