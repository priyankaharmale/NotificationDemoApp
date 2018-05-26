package com.hnweb.clawpal;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hnweb.clawpal.BuyorSale.activity.PetListActivity;
import com.hnweb.clawpal.BuyorSale.activity.PetListDemoActivity;
import com.hnweb.clawpal.adaption.activity.AdoptionDemoPetList;
import com.hnweb.clawpal.adaption.activity.AdoptionPetList;
import com.hnweb.clawpal.location.LocationSet;
import com.hnweb.clawpal.location.MyLocationListener;
import com.hnweb.clawpal.login.LoginActivity;
import com.hnweb.clawpal.lostorfound.pet.activity.LostFoundPeDemotList;
import com.hnweb.clawpal.lostorfound.pet.activity.LostFoundPetList;
import com.hnweb.clawpal.myfavpet.ViewPagerActivity;
import com.hnweb.clawpal.productbuysale.activity.ProductListActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by HNWeb-11 on 7/22/2016.
 */
public class DashboardActivity extends LocationSet implements View.OnClickListener {
    ImageView mIvBuySale, mIvAdoption, mIvLostFound;
    Button btn_productBuysale;
    TextView tv_myfavlist;
    RelativeLayout rl_fav;
    MyLocationListener myLocationListener;
    double latitude = 0.0d;
    double longitude = 0.0d;
    GPSTracker gpsTracker;
    TextView tv_loginhere;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    SharedPreferences sharedPreferences;
    String userID;

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_dashboard);
        getInit();
        setListener();
        sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        userID = sharedPreferences.getString("userid", null);
        if (userID != null) {
            rl_fav.setVisibility(View.VISIBLE);
            tv_loginhere.setVisibility(View.GONE);
        } else {
            rl_fav.setVisibility(View.GONE);
            tv_loginhere.setVisibility(View.VISIBLE);

        }

        myLocationListener = new MyLocationListener(DashboardActivity.this);
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), DashboardActivity.this)) {
            fetchLocationData();
        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), DashboardActivity.this);
        }
    }
    public void getInit() {
        mIvBuySale = (ImageView) findViewById(R.id.activity_deshboard_img_buy_sale);
        mIvLostFound = (ImageView) findViewById(R.id.activity_deshboard_img_lost_found);
        mIvAdoption = (ImageView) findViewById(R.id.activity_deshboard_img_adoption);
        tv_myfavlist = (TextView) findViewById(R.id.tv_myfavlist);
        btn_productBuysale=(Button) findViewById(R.id.activity_deshboard_img_product);
        rl_fav = (RelativeLayout) findViewById(R.id.rl_fav);
        tv_loginhere=(TextView) findViewById(R.id.tv_loginhere);
    }
    public void setListener() {
        mIvBuySale.setOnClickListener(this);
        mIvLostFound.setOnClickListener(this);
        mIvAdoption.setOnClickListener(this);
        rl_fav.setOnClickListener(this);
        btn_productBuysale.setOnClickListener(this);
        tv_loginhere.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_deshboard_img_buy_sale:
              /*  LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {*/
                Intent intent = new Intent(DashboardActivity.this, PetListDemoActivity.class);
                intent.putExtra("title", "Buy/Sale");
                startActivity(intent);
                finish();
                break;
            case R.id.activity_deshboard_img_lost_found:
                Intent intent2 = new Intent(DashboardActivity.this, LostFoundPeDemotList.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.activity_deshboard_img_adoption:
                Intent intent3 = new Intent(DashboardActivity.this, AdoptionDemoPetList.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.rl_fav:
                Intent intent4 = new Intent(DashboardActivity.this, ViewPagerActivity.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.activity_deshboard_img_product:
                Intent intent5 = new Intent(DashboardActivity.this, ProductListActivity.class);
                intent5.putExtra("title", "Product Buy/Sale");
                startActivity(intent5);
                finish();
                break;
            case R.id.tv_loginhere:
                Intent intent6 = new Intent(DashboardActivity.this, LoginActivity.class);
                intent6.putExtra("key", "Dashboard");
                startActivity(intent6);
                finish();
                break;

        }
    }


    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
    public void fetchLocationData() {
        // check if myLocationListener enabled
        if (myLocationListener.canGetLocation()) {
            latitude = myLocationListener.getLatitude();
            longitude = myLocationListener.getLongitude();

            // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            myLocationListener.showSettingsAlert();
            latitude = myLocationListener.getLatitude();
            longitude = myLocationListener.getLongitude();

            // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

        }


    }
}
