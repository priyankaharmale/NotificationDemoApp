package com.hnweb.clawpal.lostorfound.pet.adaptor;

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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.WebUrl.WebURL;
import com.hnweb.clawpal.lostorfound.pet.activity.ReportAPetActivity;
import com.hnweb.clawpal.lostorfound.pet.model.lostFoundPetListModel;
import com.hnweb.clawpal.util.AppConstant;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HNWeb-11 on 8/3/2016.
 */
public class MyLostFoundPetsAdaptor extends ArrayAdapter<lostFoundPetListModel> {


    private final LayoutInflater mInflater;
    private final ArrayList<lostFoundPetListModel> mainList;
    int flag;
    String responsetext, petid;
    Context context;
    List<String> images = new ArrayList<>();

    private ArrayList<lostFoundPetListModel> filteredData;
    private ImageLoader imageLoader;
    private ItemFilter itemFilter;
    private ProgressDialog progress;

    public MyLostFoundPetsAdaptor(Context context, ArrayList<lostFoundPetListModel> data) {
        super(context, R.layout.custom_list_item, data);
        this.filteredData = data;
        this.mainList = data;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        progress = new ProgressDialog(getContext());
        progress.setMessage("Please Wait.....");

    }


    public int getCount() {
        return filteredData.size();
    }

    public lostFoundPetListModel getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        final ViewHolder holder;
        final lostFoundPetListModel service_info = filteredData.get(position);
        if (rowView == null) {
            holder = new ViewHolder();
            holder.service_info = service_info;
            rowView = mInflater.inflate(R.layout.custom_mylist_item, parent, false);
            holder.mTvPetName = (TextView) rowView.findViewById(R.id.pets_name);
            holder.mTvPetAdd = (TextView) rowView.findViewById(R.id.pets_Address);
            holder.mTvPetCity = (TextView) rowView.findViewById(R.id.address);
            holder.mTvPetType = (TextView) rowView.findViewById(R.id.pets_type);
            holder.postedOn = (TextView) rowView.findViewById(R.id.postedOn);
            holder.MIvPetImage = (ImageView) rowView.findViewById(R.id.pets_image);
            holder.iv_delete = (ImageView) rowView.findViewById(R.id.iv_delete);
            holder.iv_edit = (ImageView) rowView.findViewById(R.id.iv_edit);


            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

  /*      holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("callfrom", "adaptor");
                Gson gson = new Gson();
                String mypetobj = gson.toJson(service_info);
                bundle.putString(AppConstant.MYPETLIST.toString(), mypetobj);
                Intent intent = new Intent(context, ReportAPetActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });*/

        holder.position = position;


        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(service_info.getLost_found_id());
            }
        });


        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Clicked on: " + holder.position, Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putString("callfrom", "adaptor");
                Gson gson = new Gson();
                String mypetobj = gson.toJson(holder.service_info);

                bundle.putString(AppConstant.MYPETLIST.toString(), mypetobj);
                System.out.println("my info" + mypetobj);
                Intent intent = new Intent(context, ReportAPetActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });
        holder.mTvPetName.setText(service_info.getTitle());
        holder.mTvPetAdd.setText(service_info.getAddress());
        holder.mTvPetCity.setText(service_info.getLocation());
        holder.mTvPetType.setText(service_info.getType_of_animal());
        petid = service_info.getLost_found_id();
        System.out.println("PetIds..." + petid);

        String youString;

        int i = service_info.getReport_date_time().indexOf(" ");
        youString = service_info.getReport_date_time().substring(0, i);
        holder.postedOn.setText("Posted On:  " + youString);
        //holder.readmore.setText(readmore[position]);
        //holder.allcourcesimg.setImageResource(imageId[position]);
        // holder.starimg.setImageResource(R.mipmap.star);

     /*   UrlImageViewHelper.setUrlDrawable(holder.MIvPetImage, service_info.getImage(), R.drawable.no_image,
                new UrlImageViewCallback() {
                    @Override
                    public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                         boolean loadedFromCache) {
                    }
                });
*/
        images = Arrays.asList(service_info.getImage().replaceAll("\\s", "").split(","));

        UrlImageViewHelper.setUrlDrawable(holder.MIvPetImage, images.get(0), R.drawable.no_image,
                new UrlImageViewCallback() {
                    @Override
                    public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                         boolean loadedFromCache) {
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

    public void delete(String PetId) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.DELETE_MY_LOSTFOUND_PETLIST + PetId,
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

    private void showAlertDialog(final  String petId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("")
                .setMessage("Are you sure want to Delete? ")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        delete(petId);
                        dialog.dismiss();
                    }
                });


        AlertDialog alert = builder.create();
        alert.show();

    }

    static class ViewHolder {


        TextView mTvPetName, mTvPetAdd, mTvPetCity, mTvPetType, postedOn;
        ImageView MIvPetImage, iv_delete, iv_edit;
        int position;
        lostFoundPetListModel service_info;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<lostFoundPetListModel> list = mainList;
            int count = list.size();
            final ArrayList<lostFoundPetListModel> nlist = new ArrayList<lostFoundPetListModel>(count);

            String filterableString;
            String filterableString2;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getType_of_animal();
                filterableString2 = list.get(i).getLocation();
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

            filteredData = (ArrayList<lostFoundPetListModel>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
            notifyDataSetChanged();
        }

    }
}
