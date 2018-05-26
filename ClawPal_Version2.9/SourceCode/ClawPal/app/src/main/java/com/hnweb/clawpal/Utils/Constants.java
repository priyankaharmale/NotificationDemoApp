package com.hnweb.clawpal.Utils;

import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.adaption.model.AdoptPetModel;
import com.hnweb.clawpal.lostorfound.pet.model.lostFoundPetListModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HNWeb-11 on 8/2/2016.
 */
public class Constants {
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";

    public static String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
    public static  ArrayList<All_Pets_List_Model> pets_list = null;
    public static  ArrayList<All_Pets_List_Model> pets_animal = null;
    public static  ArrayList<All_Pets_List_Model> pets_animalCat = null;
    public static  ArrayList<All_Pets_List_Model> pets_animalBuy = null;
    public static  ArrayList<All_Pets_List_Model> pets_animalSale = null;
    public static  ArrayList<All_Pets_List_Model> pets_animalText = null;
    public static  ArrayList<All_Pets_List_Model> pets_animalTextALL = null;
    public static  ArrayList<All_Pets_List_Model> pets_animalTextALLCAT = null;

    public static  ArrayList<lostFoundPetListModel> pets_list1 = null;
    public static  ArrayList<lostFoundPetListModel> pets_animal1 = null;
    public static  ArrayList<lostFoundPetListModel> pets_animalCat1 = null;
    public static  ArrayList<lostFoundPetListModel> pets_animalCatAll = null;
    public static  ArrayList<lostFoundPetListModel> pets_animalLost1 = null;
    public static  ArrayList<lostFoundPetListModel> pets_animalFound1 = null;
    public static  ArrayList<lostFoundPetListModel> pets_animalText1 = null;
    public static  ArrayList<lostFoundPetListModel> pets_animalText1All = null;

    public static  ArrayList<AdoptPetModel> pets_list2 = null;
    public static  ArrayList<AdoptPetModel> pets_animal2 = null;
    public static  ArrayList<AdoptPetModel> pets_animal2All = null;
    public static  ArrayList<AdoptPetModel> pets_animalCat2 = null;
    public static  ArrayList<AdoptPetModel> pets_animalCat2All = null;
    public static  ArrayList<AdoptPetModel> pets_animallooking = null;
    public static  ArrayList<AdoptPetModel> pets_animalOffering = null;
    public static  ArrayList<AdoptPetModel> pets_animalText2 = null;
}
