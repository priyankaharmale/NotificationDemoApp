package com.hnweb.clawpal.BuyorSale.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.hnweb.clawpal.BuyorSale.adaptor.BuySaleDetailsPetAdaptorRecyle;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Priyanka on 04-Dec-17.
 */

public class PetDetailsRecylerActivity extends AppCompatActivity implements Serializable{
    RecyclerView recyclerView;
    ArrayList<All_Pets_List_Model> all_pets_list_models;
    BuySaleDetailsPetAdaptorRecyle buySaleDetailsPetAdaptorRecyle;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petdetails_recyler);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_btn_img);
        toolbar.setNavigationOnClickListener(

                new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {

                        finish();
                    }

                }

        );
        if (getIntent().hasExtra("arraylist")) {
            Bundle bundle = getIntent().getExtras();
            if(bundle!=null)
                all_pets_list_models = (ArrayList<All_Pets_List_Model>) bundle.getSerializable("arraylist");
            else
                Log.e("null","null");
        }
        recyclerView=(RecyclerView) findViewById(R.id.recycler_view_pet);
        recyclerView.setHasFixedSize(true);

        buySaleDetailsPetAdaptorRecyle = new BuySaleDetailsPetAdaptorRecyle(PetDetailsRecylerActivity.this, all_pets_list_models);
        recyclerView.setAdapter(buySaleDetailsPetAdaptorRecyle);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(PetDetailsRecylerActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    //          Toast.makeText(getApplicationContext(), doctorRecylerViewAdaptor.get(position), Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }
}
