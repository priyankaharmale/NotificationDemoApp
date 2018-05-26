package com.hnweb.clawpal.adaption.fragment;

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
import com.hnweb.clawpal.adaption.model.AdoptPetModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Shree on 31-Jan-18.
 */

public class Tab2Adoption extends Fragment {

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
            Type type = new TypeToken<ArrayList<AdoptPetModel>>() {
            }.getType();
            ArrayList<AdoptPetModel> arrayList = gson.fromJson(json, type);



            String json2 = sharedPrefs.getString("MyObject", "");
            AdoptPetModel petObject = gson.fromJson(json2, AdoptPetModel.class);
            mTvTypeOfPet.setText(petObject.getType_of_animal());
            mTvGender.setText(petObject.getAge_range());
            mTvAge.setText(petObject.getGender());
            mTvBreed.setText(petObject.getBreed_type());
            mTvNeutered.setText(petObject.getNeutered());
            mTvVaccinated.setText(petObject.getVaccinated());
            mTvPrice.setText(petObject.getPrice_range());

            String youString;
            int i = petObject.getAdaptionDate().indexOf(" ");
            youString = petObject.getAdaptionDate().substring(0, i);
            tv_postedOn.setText(youString);
            return rootView;

        }
}
