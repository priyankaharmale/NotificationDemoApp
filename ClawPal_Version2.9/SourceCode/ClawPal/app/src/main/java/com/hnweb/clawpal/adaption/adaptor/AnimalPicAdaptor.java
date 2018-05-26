package com.hnweb.clawpal.adaption.adaptor;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hnweb.clawpal.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Shree on 29-Sep-17.
 */

public class AnimalPicAdaptor extends RecyclerView.Adapter<AnimalPicAdaptor.MyViewHolder> {

    Context context;
    private List<String> horizontalList;
    private Dialog dialog; // class variable

    public AnimalPicAdaptor(Context context, List<String> horizontalList) {
        this.horizontalList = horizontalList;
        this.context = context;
    }

    @Override
    public AnimalPicAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adaptor_imageview, parent, false);

        return new AnimalPicAdaptor.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AnimalPicAdaptor.MyViewHolder holder, final int position) {
        final String symptomPhotos = horizontalList.get(position);
      //  Picasso.with(context).load((symptomPhotos)).into(holder.imageView);


        try {
            Glide.with(context)
                    .load(symptomPhotos)
                    //.error(drawable)
                    .centerCrop()
                    .crossFade()
// .placeholder(R.mipmap.defaulteventimagesmall)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progress_item.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progress_item.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.imageView);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        holder.setSymptomphotos(symptomPhotos);
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        ProgressBar progress_item;
        String symptomPhotos = null;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            progress_item=(ProgressBar) view.findViewById(R.id.progress_item);


         /*   imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog = new Dialog(context);  // always give context of activity.
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_dialogbox_symptom);


                    ImageView imageViewClose = (ImageView) dialog.findViewById(R.id.iv_close);
                    ImageView image = (ImageView) dialog.findViewById(R.id.image);

                    Picasso.with(context).load((symptomPhotos.getFile_path())).into(image);
                    imageViewClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            });*/


        }

        public void setSymptomphotos(String symptomPhotos) {
            this.symptomPhotos = symptomPhotos;
        }
    }


}
