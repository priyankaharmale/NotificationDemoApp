package com.hnweb.clawpal.lostorfound.pet.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.lostorfound.pet.model.lostFoundPetListModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Shree on 31-Jan-18.
 */

public class TabTwo extends Fragment {

    TextView tv_tab1;
    //Overriden method onCreateView

    TextView mTvPetDisc, mTvTypeOfPet, mTvGender, tv_email, mTvAge, mTvBreed, mTvNeutered, mTvVaccinated, tv_postedOn, mTvPrice, mTvContact_person, mTvNumber, mTvCity, mTvAddress;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.tab_twolost, container, false);
            mTvTypeOfPet = (TextView) rootView.findViewById(R.id.tv_type_pet);
            mTvGender = (TextView) rootView.findViewById(R.id.tv_gender);
            mTvAge = (TextView) rootView.findViewById(R.id.tv_age);
            mTvBreed = (TextView) rootView.findViewById(R.id.tv_breed);
            mTvNeutered = (TextView) rootView.findViewById(R.id.tv_natuered);
            tv_postedOn = (TextView) rootView.findViewById(R.id.tv_postedOn);
            mTvVaccinated = (TextView) rootView.findViewById(R.id.tv_vaccinated);
            mTvPrice = (TextView) rootView.findViewById(R.id.tv_price);

           SharedPreferences sharedPrefs = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("arraylist", null);
            String petId = sharedPrefs.getString("petId", null);
            Type type = new TypeToken<ArrayList<All_Pets_List_Model>>() {
            }.getType();
            ArrayList<lostFoundPetListModel> arrayList = gson.fromJson(json, type);



            String json2 = sharedPrefs.getString("MyObject", "");
            lostFoundPetListModel petObject = gson.fromJson(json2, lostFoundPetListModel.class);
            mTvTypeOfPet.setText(petObject.getType_of_animal());
            mTvGender.setText(petObject.getAge_range());
            mTvAge.setText(petObject.getGender());
            mTvBreed.setText(petObject.getBreed_type());
            mTvNeutered.setText(petObject.getReport_type());
            mTvVaccinated.setText("-");
            mTvPrice.setText("-");
            String youString;
            int i = petObject.getReport_date_time().indexOf(" ");
            youString = petObject.getReport_date_time().substring(0, i);
            tv_postedOn.setText(youString);

            return rootView;

        }
}
