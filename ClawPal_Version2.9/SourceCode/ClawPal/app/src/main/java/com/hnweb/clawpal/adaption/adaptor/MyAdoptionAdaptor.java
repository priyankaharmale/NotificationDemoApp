package com.hnweb.clawpal.adaption.adaptor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.adaption.activity.AdoptionDetailsSwipeListActivity;
import com.hnweb.clawpal.adaption.model.AdoptPetModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Shree on 25-Jan-18.
 */

public class MyAdoptionAdaptor extends BaseAdapter {


    public ArrayList<AdoptPetModel> allPetsListModels;
    public Context context;
    AnimalPicAdaptor animalPicAdaptor;

    String userID, isClick = "1";
    LayoutInflater mInflater;
    SharedPreferences sharedPrefs;
    ViewHolder viewHolder;
    AdoptPetModel obj;
    List<String> images = new ArrayList<>();
    TabLayout tabLayout;
    int i;
    RecyclerView recycler_view_petImages;
    ImageView iv_dog;
    ViewPagerAdoptionAdapter viewPagerAdapter;
    AdoptPetModel petObj;
    ImageView iv_largeiv;
    ProgressDialog progressDialog;
    RelativeLayout rl_images;
    CircleImageView imageView2, imageView1, imageView3;

    public MyAdoptionAdaptor(ArrayList<AdoptPetModel> apps, Context context, AdoptPetModel petObj) {
        this.allPetsListModels = apps;
        this.context = context;
        System.out.println("adapter callsed");
        mInflater = LayoutInflater.from(context);
        this.petObj = petObj;
    }

    @Override
    public int getCount() {
        return allPetsListModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        AdoptPetModel petObject = allPetsListModels.get(position);

        if (view == null) {


            view = mInflater.inflate(R.layout.adaptor_adaption_swipe, parent, false);
            viewHolder = new ViewHolder();

            // Initilization
            i = 1;
            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText("About"));
            tabLayout.addTab(tabLayout.newTab().setText("Details"));
            tabLayout.addTab(tabLayout.newTab().setText("Contact"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait...");

            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            recycler_view_petImages = (RecyclerView) view.findViewById(R.id.recycler_view_petImages);
            // iv_dog = (ImageView) view.findViewById(R.id.iv_dog);
            iv_largeiv = (ImageView) view.findViewById(R.id.iv_dog);

            rl_images = (RelativeLayout) view.findViewById(R.id.rl_images);
            imageView1 = (CircleImageView) view.findViewById(R.id.imageView1);
            imageView2 = (CircleImageView) view.findViewById(R.id.imageView2);
            imageView3 = (CircleImageView) view.findViewById(R.id.imageView3);
            sharedPrefs = context.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            userID = sharedPrefs.getString("userid", null);
            final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
            System.out.println("TabCOunt" + tabLayout.getTabCount());
            viewPagerAdapter = new ViewPagerAdoptionAdapter
                    (((AdoptionDetailsSwipeListActivity) context).getSupportFragmentManager(), tabLayout.getTabCount(), context);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            viewPager.setOffscreenPageLimit(3);
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());

                    /// Toast.makeText(context,"STring .."+tab.getPosition(),Toast.LENGTH_LONG).show();

                    if (tab.getPosition() == 2) {
                        if (userID != null) {

                        }
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            Gson gson = new Gson();
            String json = sharedPrefs.getString("arraylist", null);
            String petId = sharedPrefs.getString("petId", null);
            Type type = new TypeToken<ArrayList<AdoptPetModel>>() {
            }.getType();
            ArrayList<AdoptPetModel> arrayList = gson.fromJson(json, type);


            String json2 = sharedPrefs.getString("MyObject", "");
            obj = gson.fromJson(json2, AdoptPetModel.class);


            images = Arrays.asList(obj.getPet_adoption_photo().replaceAll("\\s", "").split(","));

          /*  if (images.size() == 0) {

                recycler_view_petImages.setVisibility(View.GONE);
                iv_dog.setVisibility(View.VISIBLE);
                iv_dog.setImageResource(R.drawable.no_image);
            } else {
                recycler_view_petImages.setVisibility(View.VISIBLE);
                iv_dog.setVisibility(View.GONE);
                animalPicAdaptor = new AnimalPicAdaptor(context, images);
                LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                recycler_view_petImages.setLayoutManager(horizontalLayoutManagaer);
                recycler_view_petImages.setAdapter(animalPicAdaptor);
            }*/


            if (images.size() == 0) {
                rl_images.setVisibility(View.GONE);
                System.out.println("Cursor0");
               /* rl_images.setVisibility(View.GONE);
                iv_largeiv.setImageResource(R.drawable.no_image);*/
                try {
                    Glide.with(context)
                            .load(images.get(0))
                            .error(R.drawable.no_image)
                            .centerCrop()
                            .crossFade()
// .placeholder(R.mipmap.defaulteventimagesmall)

                            .into(iv_largeiv);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            } else if (images.size() == 1) {
                System.out.println("Cursor1");


                rl_images.setVisibility(View.GONE);
                if (images.get(0).equals("")) {
                    try {
                        Glide.with(context)
                                .load(R.drawable.no_image)
                                //.error(drawable)
                                .centerCrop()
                                .crossFade()
// .placeholder(R.mipmap.defaulteventimagesmall)

                                .into(iv_largeiv);
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }

                } else {
                    try {
                        Glide.with(context)
                                .load(images.get(0))
                                //.error(drawable)
                                .centerCrop()
                                .crossFade()
// .placeholder(R.mipmap.defaulteventimagesmall)

                                .into(iv_largeiv);
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                }

            } else if (images.size() == 2) {
                System.out.println("Cursor2");

                rl_images.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);


                imageView3.setVisibility(View.GONE);

                try {
                    Glide.with(context)
                            .load(images.get(0))
                            //.error(drawable)
                            .centerCrop()
                            .crossFade()
// .placeholder(R.mipmap.defaulteventimagesmall)

                            .into(iv_largeiv);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
                try {
                    Glide.with(context)
                            .load(images.get(0))
                            //.error(drawable)
                            .centerCrop()
                            .crossFade()
                            .into(imageView1);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
                try {
                    Glide.with(context)
                            .load(images.get(1))
                            //.error(drawable)
                            .centerCrop()
                            .crossFade()
                            .into(imageView2);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }

            } else {
                System.out.println("Cursor3");

                rl_images.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);
                imageView3.setVisibility(View.VISIBLE);

                try {
                    Glide.with(context)
                            .load(images.get(0))
                            //.error(drawable)
                            .centerCrop()
                            .crossFade()
// .placeholder(R.mipmap.defaulteventimagesmall)

                            .into(iv_largeiv);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
                try {
                    Glide.with(context)
                            .load(images.get(0))
                            //.error(drawable)
                            .centerCrop()
                            .crossFade()
                            .into(imageView1);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
                try {
                    Glide.with(context)
                            .load(images.get(1))
                            //.error(drawable)
                            .centerCrop()
                            .crossFade()
                            .into(imageView2);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
                try {
                    Glide.with(context)
                            .load(images.get(2))
                            //.error(drawable)
                            .centerCrop()
                            .crossFade()
                            .into(imageView3);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }

            }


           /* if(images.size()==0)
            {

                recycler_view_petImages.setVisibility(View.GONE);
                iv_dog.setVisibility(View.VISIBLE);
                iv_dog.setImageResource(R.drawable.no_image);
            }else
            {
                recycler_view_petImages.setVisibility(View.VISIBLE);
                iv_dog.setVisibility(View.GONE);
                animalPicAdaptor = new AnimalPicAdaptor(context, images);
                LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                recycler_view_petImages.setLayoutManager(horizontalLayoutManagaer);
                recycler_view_petImages.setAdapter(animalPicAdaptor);
            }*/

            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogbox(images.get(0), 1);
                }
            });
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogbox(images.get(1), 2);
                }
            });
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogbox(images.get(2), 3);
                }
            });

            iv_largeiv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.setContentView(R.layout.custom_image);

                    dialog.show();
                    ImageView iv_large = (ImageView) dialog.findViewById(R.id.iv_large);
                    final ProgressBar progress_item = (ProgressBar) dialog.findViewById(R.id.progress_item);
                    try {
                        Glide.with(context)
                                .load(images.get(0))
                                .error(R.drawable.no_image)
                                .centerCrop()
                                .crossFade()
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        progress_item.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        progress_item.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(iv_large);
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                    ImageView declineButton = (ImageView) dialog.findViewById(R.id.iv_close);
                    declineButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Close dialog
                            dialog.dismiss();
                        }
                    });

                }
            });
            view.setTag("demo");


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return view;
    }


    public static class ViewHolder {

        All_Pets_List_Model petObject = null;
        LinearLayout mLlContactDetails;
        TabHost host;

    }

    public void dialogbox(String image, final int count)

    {
        i = count;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.custom_image);

        dialog.show();
        final ProgressBar progress_item = (ProgressBar) dialog.findViewById(R.id.progress_item);

        final ImageView iv_large = (ImageView) dialog.findViewById(R.id.iv_large);
        final Button btn_prev = (Button) dialog.findViewById(R.id.btn_prev);
        final Button btn_next = (Button) dialog.findViewById(R.id.btn_next);


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i == 1) {
                    try {
                        Glide.with(context)
                                .load(images.get(1))
                                .error(R.drawable.no_image)
                                .centerCrop()
                                .crossFade()
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        progress_item.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        progress_item.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(iv_large);
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                    i = 2;
                } else if (i == 2) {
                    Toast.makeText(context, "No more photos to show", Toast.LENGTH_SHORT).show();

                    try {
                        Glide.with(context)
                                .load(images.get(2))
                                .error(R.drawable.no_image)
                                .centerCrop()
                                .crossFade()
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        progress_item.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        progress_item.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(iv_large);
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                    i = 3;
                }


            }


        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i == 1) {
                    Toast.makeText(context, "No more photos to show", Toast.LENGTH_SHORT).show();


                } else if (i == 2) {


                    try {
                        Glide.with(context)
                                .load(images.get(0))
                                .error(R.drawable.no_image)
                                .centerCrop()
                                .crossFade()
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        progress_item.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        progress_item.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(iv_large);
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                    i = 1;
                } else if (i == 3) {
                    try {
                        Glide.with(context)
                                .load(images.get(1))
                                .error(R.drawable.no_image)
                                .centerCrop()
                                .crossFade()
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        progress_item.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        progress_item.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(iv_large);
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                    i = 2;
                }
            }


        });


        try {
            Glide.with(context)
                    .load(image)
                    .error(R.drawable.no_image)
                    .centerCrop()
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progress_item.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progress_item.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(iv_large);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        ImageView declineButton = (ImageView) dialog.findViewById(R.id.iv_close);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
