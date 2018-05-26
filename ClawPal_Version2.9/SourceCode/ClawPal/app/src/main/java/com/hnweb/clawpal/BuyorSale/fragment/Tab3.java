package com.hnweb.clawpal.BuyorSale.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
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
import static com.hnweb.clawpal.R.id.tv_tab1;

/**
 * Created by Shree on 31-Jan-18.
 */

public class Tab3 extends  Fragment {

    TextView tv_tab1;
    //Overriden method onCreateView
    TextView mTvTypeOfPet, mTvGender, mTvAge, mTvBreed, mTvNeutered, mTvVaccinated, mTvPrice, mTvContact_person, mTvEmail,mTvNumber, mTvCity, mTvAddress, tv_postedOn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("TabData");
        View rootView = inflater.inflate(R.layout.tab_threee, container, false);
        mTvContact_person = (TextView) rootView.findViewById(R.id.tv_cont_person);
        mTvEmail=(TextView) rootView.findViewById(R.id.tv_email);
        mTvNumber = (TextView) rootView.findViewById(R.id.tv_number);
        mTvCity = (TextView) rootView.findViewById(R.id.tv_city);
        mTvAddress = (TextView) rootView.findViewById(R.id.tv_add);


        SharedPreferences sharedPrefs = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("arraylist", null);
        String petId = sharedPrefs.getString("petId", null);
        Type type = new TypeToken<ArrayList<All_Pets_List_Model>>() {
        }.getType();
        ArrayList<All_Pets_List_Model> arrayList = gson.fromJson(json, type);



        String json2 = sharedPrefs.getString("MyObject", "");
        final    All_Pets_List_Model obj = gson.fromJson(json2, All_Pets_List_Model.class);
        mTvContact_person.setText(obj.getName());
        mTvNumber.setText(obj.getContact());
        mTvCity.setText(obj.getCity());
        mTvAddress.setText(obj.getLocality());
        mTvEmail.setText(obj.getEmail());
        mTvEmail.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvNumber.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        mTvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+Uri.encode(obj.getContact().trim())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
            }
        });
        mTvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain"); // send email as plain text
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {obj.getEmail() });
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
        return rootView;

    }
}
