package com.hnweb.clawpal.lostorfound.pet.adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.hnweb.clawpal.BuyorSale.activity.PetDetails;
import com.hnweb.clawpal.lostorfound.pet.activity.LostFoundDetailsSwipeListActivity;
import com.hnweb.clawpal.lostorfound.pet.activity.LostFoundPetDetails;
import com.hnweb.clawpal.lostorfound.pet.model.lostFoundPetListModel;
import com.hnweb.clawpal.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HNWeb-11 on 8/3/2016.
 */
public class lostFoundPetsAdaptor extends ArrayAdapter<lostFoundPetListModel> {


    private final LayoutInflater mInflater;
    private final ArrayList<lostFoundPetListModel> mainList;
    String youString;
    private ArrayList<lostFoundPetListModel> filteredData;
    private ImageLoader imageLoader;
    private ItemFilter itemFilter;
    List<String> images = new ArrayList<>();
    Context context;


    public lostFoundPetsAdaptor(Context context, ArrayList<lostFoundPetListModel> data) {
        super(context, R.layout.custom_list_item, data);
        this.filteredData = data;
        this.mainList = data;
        this.context=context;
        mInflater = LayoutInflater.from(context);


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
    public View getView(final int position, View rowView, ViewGroup parent) {
        ViewHolder holder;
        final lostFoundPetListModel service_info = filteredData.get(position);
        if (rowView == null) {
            holder = new ViewHolder();
            rowView = mInflater.inflate(R.layout.custom_list_item, parent, false);
            holder.mTvPetName = (TextView) rowView.findViewById(R.id.pets_name);
            holder.mTvPetAdd = (TextView) rowView.findViewById(R.id.pets_Address);
            holder.mTvPetCity = (TextView) rowView.findViewById(R.id.address);
            holder.mTvPetType = (TextView) rowView.findViewById(R.id.pets_type);
            holder.MIvPetImage = (ImageView) rowView.findViewById(R.id.pets_image);
            holder.postedOn = (TextView) rowView.findViewById(R.id.postedOn);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.mTvPetName.setText(service_info.getTitle());
        holder.mTvPetAdd.setText(service_info.getAddress());
        holder.mTvPetCity.setText(service_info.getLocation());
        holder.mTvPetType.setText(service_info.getType_of_animal());


        //holder.readmore.setText(readmore[position]);
        //holder.allcourcesimg.setImageResource(imageId[position]);
        // holder.starimg.setImageResource(R.mipmap.star);
        int i = service_info.getReport_date_time().indexOf(" ");
        youString = service_info.getReport_date_time().substring(0, i);
        holder.postedOn.setText("Posted On:  " + youString);
       /* UrlImageViewHelper.setUrlDrawable(holder.MIvPetImage, service_info.getImage(), R.drawable.no_image,
                new UrlImageViewCallback() {
                    @Override
                    public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                         boolean loadedFromCache) {
                    }
                });*/

        images = Arrays.asList(service_info.getImage().replaceAll("\\s", "").split(","));

        UrlImageViewHelper.setUrlDrawable(holder.MIvPetImage, images.get(0), R.drawable.no_image,
                new UrlImageViewCallback() {
                    @Override
                    public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url,
                                         boolean loadedFromCache) {
                    }
                });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(getContext(), LostFoundDetailsSwipeListActivity.class);

                Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("lostFoundPetListModel", service_info);

                bundleObject.putSerializable("arraylist", filteredData);
                bundleObject.putSerializable("petobject", service_info);
                bundleObject.putInt("Position", position);

                intent2.putExtras(bundleObject);
                context.startActivity(intent2);
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

    static class ViewHolder {


        TextView mTvPetName, mTvPetAdd, mTvPetCity, mTvPetType, postedOn;
        ImageView MIvPetImage;

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
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {

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
