package com.hnweb.clawpal.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;


import com.hnweb.clawpal.adaption.adaptor.AdoptPetAdaptor;
import com.hnweb.clawpal.adaption.model.AdoptPetModel;

import java.util.ArrayList;

/**
 * Created by HNWeb-11 on 9/22/2016.
 */
public class EndlessListViewForAdoption extends ListView implements AbsListView.OnScrollListener {

    private Context context;
    private View footer;
    private boolean isLoading;
    private EndLessListener listener;
    private AdoptPetAdaptor adapter;

    public EndlessListViewForAdoption(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setOnScrollListener(this);

        this.context = context;
    }

    public EndlessListViewForAdoption(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(this);

        this.context = context;
    }

    public EndlessListViewForAdoption(Context context) {
        super(context);
        this.setOnScrollListener(this);

        this.context = context;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    //	4
    public void addNewData(ArrayList<AdoptPetModel> data) {
        this.removeFooterView(footer);
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
        isLoading = false;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {

        if(getAdapter() == null)
            return;

        if(getAdapter().getCount() == 0)
            return;

        int l = visibleItemCount + firstVisibleItem;

        if(l >= totalItemCount && !isLoading){

            //	add footer layout
            this.addFooterView(footer);

            //	set progress boolean
            isLoading = true;

            //	call interface method to load new data
            listener.loadData();
        }
    }

    //	Calling order from MainActivity
    //	1
    public void setLoadingView(int resId) {
        footer = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resId, null);		//		footer = (View)inflater.inflate(resId, null);
        this.addFooterView(footer);
    }

    //	2
    public void setListener(EndLessListener listener) {
        this.listener = listener;
    }

    //	3
    public void setAdapter(AdoptPetAdaptor adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
        this.removeFooterView(footer);
    }

    //	interface
    public interface EndLessListener{
        public void loadData();
    }
}