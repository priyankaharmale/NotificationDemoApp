package com.hnweb.clawpal.BuyorSale.adaptor;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hnweb.clawpal.BuyorSale.activity.BuySaleDetailsSwipeListActivity;
import com.hnweb.clawpal.BuyorSale.model.All_Pets_List_Model;
import com.hnweb.clawpal.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Shree on 25-Jan-18.
 */

public class MyAdaptor extends BaseAdapter {


    public ArrayList<All_Pets_List_Model> allPetsListModels;
    public Context context;
    int i;
    RelativeLayout rl_images;
    LayoutInflater mInflater;
    ViewHolder viewHolder;
    List<String> images = new ArrayList<>();
    TabLayout tabLayout;
    RecyclerView recycler_view_petImages;
    ViewPagerAdapter viewPagerAdapter;
    String isClick = "1";
    All_Pets_List_Model petObj;
    String userID;
    ProgressDialog progressDialog;


    public MyAdaptor(ArrayList<All_Pets_List_Model> apps, Context context, All_Pets_List_Model petObj) {
        this.allPetsListModels = apps;
        this.context = context;
        System.out.println("adapter callsed");
        mInflater = LayoutInflater.from(context);
        this.petObj = petObj;
    }

    public static class ViewHolder {
        CircleImageView imageView1, imageView2, imageView3;
        ImageView iv_largeiv;
        ProgressBar progress_item;


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
        All_Pets_List_Model petObject = allPetsListModels.get(position);
        if (view == null) {
            view = mInflater.inflate(R.layout.adaptor_buysale_swipe, parent, false);
            viewHolder = new ViewHolder();
            // Initilization
            i = 1;
            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText("About"));
            tabLayout.addTab(tabLayout.newTab().setText("Details"));
            tabLayout.addTab(tabLayout.newTab().setText("Contact"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            //  iv_fav=(ImageView) view.findViewById(R.id.iv_fav);
            recycler_view_petImages = (RecyclerView) view.findViewById(R.id.recycler_view_petImages);
            viewHolder.iv_largeiv = (ImageView) view.findViewById(R.id.iv_dog);
            viewHolder.progress_item = (ProgressBar) view.findViewById(R.id.progress_item);
            rl_images = (RelativeLayout) view.findViewById(R.id.rl_images);


            viewHolder.imageView1 = (CircleImageView) view.findViewById(R.id.imageView1);
            viewHolder.imageView2 = (CircleImageView) view.findViewById(R.id.imageView2);
            viewHolder.imageView3 = (CircleImageView) view.findViewById(R.id.imageView3);
            final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
            System.out.println("TabCOunt" + tabLayout.getTabCount());
            viewPagerAdapter = new ViewPagerAdapter
                    (((BuySaleDetailsSwipeListActivity) context).getSupportFragmentManager(), tabLayout.getTabCount(), "gujug");
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            viewPager.setOffscreenPageLimit(3);
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            SharedPreferences sharedPrefs = context.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            Gson gson = new Gson();
            userID = sharedPrefs.getString("userid", null);
            String json = sharedPrefs.getString("arraylist", null);
            String petId = sharedPrefs.getString("petId", null);
            Type type = new TypeToken<ArrayList<All_Pets_List_Model>>() {
            }.getType();
            ArrayList<All_Pets_List_Model> arrayList = gson.fromJson(json, type);
            String json2 = sharedPrefs.getString("MyObject", "");
            All_Pets_List_Model obj = gson.fromJson(json2, All_Pets_List_Model.class);
            images = Arrays.asList(obj.getImage().replaceAll("\\s", "").split(","));
            for (All_Pets_List_Model all_pets_list_model : allPetsListModels) {
                if (all_pets_list_model.getPet_sale_buy_id().equals(obj.getPet_sale_buy_id())) {
                }
            }

            if (images.size() == 0) {
                rl_images.setVisibility(View.GONE);
                System.out.println("Cursor0");
                try {
                    Glide.with(context)
                            .load(images.get(0))
                            .error(R.drawable.no_image)
                            .centerCrop()
                            .crossFade()
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    viewHolder.progress_item.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    viewHolder.progress_item.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(viewHolder.iv_largeiv);
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
                                .into(viewHolder.iv_largeiv);
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
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        viewHolder.progress_item.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        viewHolder.progress_item.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(viewHolder.iv_largeiv);
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                }
            } else if (images.size() == 2) {
                System.out.println("Cursor2");
                rl_images.setVisibility(View.VISIBLE);
                viewHolder.imageView1.setVisibility(View.VISIBLE);
                viewHolder.imageView2.setVisibility(View.VISIBLE);
                viewHolder.imageView3.setVisibility(View.GONE);
                try {
                    Glide.with(context)
                            .load(images.get(0))
                            .centerCrop()
                            .crossFade()
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    viewHolder.progress_item.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    viewHolder.progress_item.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(viewHolder.iv_largeiv);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
                try {
                    Glide.with(context)
                            .load(images.get(0))
                            .centerCrop()
                            .crossFade()
                            .into(viewHolder.imageView1);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
                try {
                    Glide.with(context)
                            .load(images.get(1))
                            .centerCrop()
                            .crossFade()
                            .into(viewHolder.imageView2);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            } else {
                System.out.println("Cursor3");
                rl_images.setVisibility(View.VISIBLE);
                viewHolder.imageView1.setVisibility(View.VISIBLE);
                viewHolder.imageView2.setVisibility(View.VISIBLE);
                viewHolder.imageView3.setVisibility(View.VISIBLE);
                try {
                    Glide.with(context)
                            .load(images.get(0))
                            .centerCrop()
                            .crossFade()
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    viewHolder.progress_item.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    viewHolder.progress_item.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(viewHolder.iv_largeiv);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
                try {
                    Glide.with(context)
                            .load(images.get(0))
                            .centerCrop()
                            .crossFade()
                            .into(viewHolder.imageView1);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
                try {
                    Glide.with(context)
                            .load(images.get(1))
                            .centerCrop()
                            .crossFade()
                            .into(viewHolder.imageView2);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
                try {
                    Glide.with(context)
                            .load(images.get(2))
                            .centerCrop()
                            .crossFade()
                            .into(viewHolder.imageView3);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }


            viewHolder.iv_largeiv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, "ImageLarg", Toast.LENGTH_SHORT).show();

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.setContentView(R.layout.custom_image);

                    dialog.show();
                    ImageView iv_large = (ImageView) dialog.findViewById(R.id.iv_large);
                    final Button btn_prev = (Button) dialog.findViewById(R.id.btn_prev);
                    final Button btn_next = (Button) dialog.findViewById(R.id.btn_next);
                    btn_prev.setVisibility(View.GONE);
                    btn_next.setVisibility(View.GONE);

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
                            dialog.dismiss();
                        }
                    });


                }
            });
            viewHolder.imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogbox(images.get(0), 1);
                }
            });
            viewHolder.imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogbox(images.get(1), 2);
                }
            });
            viewHolder.imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogbox(images.get(2), 3);
                }
            });
            view.setTag("demo");
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return view;
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
