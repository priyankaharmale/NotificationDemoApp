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

public class TabOne extends Fragment {

    TextView tv_tab1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_one, container, false);
        tv_tab1 = (TextView) rootView.findViewById(R.id.tv_tab1);
        System.out.println("Tag" + rootView.getTag());
        tv_tab1.setText("Tab 1");

        SharedPreferences sharedPrefs = getActivity().getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("arraylist", null);
        String petId = sharedPrefs.getString("petId", null);
        Type type = new TypeToken<ArrayList<lostFoundPetListModel>>() {
        }.getType();
        ArrayList<lostFoundPetListModel> arrayList = gson.fromJson(json, type);



        String json2 = sharedPrefs.getString("MyObject", "");
        lostFoundPetListModel obj = gson.fromJson(json2, lostFoundPetListModel.class);

/*for (All_Pets_List_Model all_pets_list_model : arrayList)
{

    if(all_pets_list_model.getPet_sale_buy_id().equals(petId))
    {
        tv_tab1.setText(all_pets_list_model.getDescription());
    }
    else
    {

    }


}*/
       // tv_tab1.setText(obj.getDescription());

        if(obj.getDescription().equals(""))
        {
            if(obj.getType_of_animal().equals("Dog") || obj.getType_of_animal().equals("puppy"))
            {

                if (obj.getReport_type().equals("Lost"))
              {
                  tv_tab1.setText("My dog has been gone missing from few day. If you have found it, please contact me. .");
              }
              else
              {
                  tv_tab1.setText("I have found this dog near my property few days ago. Currently I am looking after it. So if you thinks its yours, please contact me.. .");

              }
            }
            else
            {
                if (obj.getReport_type().equals("Lost"))
                {
                    tv_tab1.setText("My Cat has been gone missing from few day. If you have found it, please contact me. ");
                }
                else
                {
                    tv_tab1.setText("I have found this Cat near my property few days ago. Currently I am looking after it. So if you thinks its yours, please contact me.. .");

                }
            }
        }
        else
        {

            tv_tab1.setText(obj.getDescription());
        }


return rootView;

    }
}
