package com.hnweb.clawpal.myfavpet.adaptor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hnweb.clawpal.BuyorSale.activity.BuyOrSalePetsActivity;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.util.AppConstant;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HNWeb-11 on 7/23/2016.
 */
public class MyPet_list_Fav_Adaptor extends ArrayAdapter<All_Pets_List_Model> {


    private final LayoutInflater mInflater;
    private final ArrayList<All_Pets_List_Model> mainList;
    int flag;
    String responsetext, petid;
    Context context;
    All_Pets_List_Model service_info;
    private ArrayList<All_Pets_List_Model> filteredData;
    private ImageLoader imageLoader;
    private ItemFilter itemFilter;
    private ProgressDialog progress;

    public MyPet_list_Fav_Adaptor(Context context, ArrayList<All_Pets_List_Model> data) {
        super(context, R.layout.custom_mylist_item, data);
        this.filteredData = data;
        this.mainList = data;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        progress = new ProgressDialog(getContext());
        progress.setMessage("Please Wait.....");

    }


    public int getCount() {
        return filteredData.size();
    }

    public All_Pets_List_Model getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        final ViewHolder holder;

        service_info = filteredData.get(position);
        if (rowView == null) {
            holder = new ViewHolder();
            holder.all_pets_list_model = service_info;
            rowView = mInflater.inflate(R.layout.custom_mylist_item, parent, false);
            holder.mTvPetName = (TextView) rowView.findViewById(R.id.pets_name);
            holder.mTvPetAdd = (TextView) rowView.findViewById(R.id.pets_Address);
            holder.mTvPetCity = (TextView) rowView.findViewById(R.id.address);
            holder.mTvPetType = (TextView) rowView.findViewById(R.id.pets_type);
            holder.postedOn = (TextView) rowView.findViewById(R.id.postedOn);
            holder.iv_deletePet = (ImageView) rowView.findViewById(R.id.iv_delete);
            holder.iv_editPet = (ImageView) rowView.findViewById(R.id.iv_edit);
            holder.MIvPetImage = (ImageView) rowView.findViewById(R.id.pets_image);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        holder.mTvPetName.setText(service_info.getTitle());
        holder.mTvPetAdd.setText(service_info.getLocality());
        holder.mTvPetCity.setText(service_info.getCity());
        holder.mTvPetType.setText(service_info.getAnimal());
        petid = service_info.getPet_sale_buy_id();
        System.out.println("PetIds..." + petid);
        String youString;

        int i = service_info.getDate().indexOf(" ");
        youString = service_info.getDate().substring(0, i);
        holder.postedOn.setText("Posted On:  " + youString);

        UrlImageViewHelper.setUrlDrawable(holder.MIvPetImage, service_info.getImage(), R.drawable.no_image,
                new UrlImageViewCallback() {
                    @Override
                    public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                         boolean loadedFromCache) {
                    }
                });

        holder.iv_editPet.setVisibility(View.GONE);
        holder.iv_deletePet.setVisibility(View.GONE);

        holder.iv_deletePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(holder.all_pets_list_model.getPet_sale_buy_id());
            }
        });
        holder.iv_editPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("callfrom", "adaptor");
                Gson gson = new Gson();
                String mypetobj = gson.toJson(holder.all_pets_list_model);
                bundle.putString(AppConstant.MYPETLIST.toString(), mypetobj);
                Intent intent = new Intent(context, BuyOrSalePetsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });

        return rowView;
    }

    @Override
    public Filter getFilter() {
        if (itemFilter == null)
            itemFilter = new ItemFilter();

        return itemFilter;
    }

    public void delete(String petid) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.DELETE_MY_BUY_SALE_PETLIST + petid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        System.out.println("response" + response.toString());
                        try {
                            JSONObject j = new JSONObject(response);
                            flag = j.getInt("flag");
                            responsetext = j.getString("response");
                            System.out.println("message123" + response);
                            if (flag == 1) {
                                responsetext = j.getString("response");
                                System.out.println("message23432" + responsetext);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage(responsetext)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                ((Activity) context).finish();
                                                context.startActivity(((Activity) context).getIntent());
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (JSONException e) {
                            System.out.println("jsonexeption" + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("jsonexeption" + error.toString());
                        progress.dismiss();

                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    static class ViewHolder {


        TextView mTvPetName, mTvPetAdd, mTvPetCity, mTvPetType, postedOn;
        ImageView iv_deletePet, iv_editPet, MIvPetImage;
        All_Pets_List_Model all_pets_list_model;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<All_Pets_List_Model> list = mainList;
            int count = list.size();
            final ArrayList<All_Pets_List_Model> nlist = new ArrayList<All_Pets_List_Model>(count);

            String filterableString;
            String filterableString2;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getAnimal();
                filterableString2 = list.get(i).getCity();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
                if (filterableString2.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            Log.d("Filter Search", "results count= " + results.count);

            filteredData = (ArrayList<All_Pets_List_Model>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
            notifyDataSetChanged();
        }

    }
}
