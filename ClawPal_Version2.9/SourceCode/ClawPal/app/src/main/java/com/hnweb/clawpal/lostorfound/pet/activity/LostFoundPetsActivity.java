package com.hnweb.clawpal.lostorfound.pet.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.hnweb.clawpal.BuyorSale.activity.BuyOrSalePetsActivity;
import com.hnweb.clawpal.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by HNWeb-11 on 8/2/2016.
 */
public class LostFoundPetsActivity extends AppCompatActivity {
    Button mBtnLostFound;
    Toolbar toolbar;
    String selectedPetType = "";
    Button mBtnPetType, mBtnBreedType, mBtnGender, mBtnAge, mBtnNeuture, mBtnVaccinated, mBtnNext;
    EditText mEtPrice, mEtDescription;
    TabHost host;
    ImageView mIvplus;
    private static int RESULT_LOAD_IMAGE = 1;
    ScrollView mSvParrent, mScChild;
    Uri selectedImage;
    InputStream is;
    String filePath1;
    Bitmap bitmap1;
    int SELECT_FILE = 1;
    int REQUEST_CAMERA = 0;
    String userChoosenTask;
    ImageView mIvPetImage;
    ImageView mIvfirst;
    TextView tv_sort;
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_lost_found_pets);
        getInit();
        SetListener();
        setTabsView();
        SetListener();
    }
    public void getInit() {
        mBtnLostFound = (Button) findViewById(R.id.activity_login_btn_buysale);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        host = (TabHost) findViewById(R.id.tabHost);
        toolbar.setNavigationIcon(R.drawable.back_btn_img);
        mBtnPetType = (Button) findViewById(R.id.btn_pet_Type);
        mBtnBreedType = (Button) findViewById(R.id.btn_breed_type);
        mBtnGender = (Button) findViewById(R.id.btn_gender);
        mBtnAge = (Button) findViewById(R.id.btn_age);
        mBtnNeuture = (Button) findViewById(R.id.btn_neutured);
        mBtnVaccinated = (Button) findViewById(R.id.btn_vaccinated);
        mEtPrice = (EditText) findViewById(R.id.et_price);
        mBtnNext = (Button) findViewById(R.id.btn_next);
        // mEtDescription=(EditText)findViewById(R.id.et_desc);
        mIvplus = (ImageView) findViewById(R.id.iv_plus);
        // mSvParrent = (ScrollView) findViewById(R.id.sc_parrent);
        mScChild = (ScrollView) findViewById(R.id.sv_child);
        mIvfirst = (ImageView) findViewById(R.id.iv_pet1);
        mIvPetImage = (ImageView) findViewById(R.id.pet_image);


    }

    public void SetListener() {

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {

                        finish();
                    }

                }

        );
       mBtnPetType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtPrice.setVisibility(View.VISIBLE);
                petTypePopup();
            }
        });
        mBtnBreedType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petBreedTypePopup(selectedPetType);
            }
        });
        mBtnGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petGenderePopup();
            }
        });
        mBtnAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petAgePopup();
            }
        });
        mBtnNeuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petNeuturedAPopup();
            }
        });
        mBtnVaccinated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                petVaccinatedPopup();
            }
        });
      /*  mEtPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtPrice.setFocusable(true);
            }
        });*/
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current_tab = host.getCurrentTab();
                if (current_tab == 0) {
                    host.setCurrentTab(1);
                } else if (current_tab == 1) {
                    host.setCurrentTab(2);
                } else if (current_tab == 2) {
                    host.setCurrentTab(3);
                    mBtnNext.setText("Save");
                } else {
                    Toast.makeText(LostFoundPetsActivity.this, "end", Toast.LENGTH_SHORT).show();
                }


            }
        });

        mIvplus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                selectImage();

            }
        });

     /*   mSvParrent.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("p", "PARENT TOUCH");

                findViewById(R.id.sv_child).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        mScChild.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("p", "CHILD TOUCH");

                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/


    }

    public void setTabsView() {


        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("About");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Details");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Contact");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Tab Four");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Photo");
        host.addTab(spec);
        final TabWidget tw = (TabWidget) host.findViewById(android.R.id.tabs);
        for (int i = 0; i < tw.getChildCount(); ++i) {
            final View tabView = tw.getChildTabViewAt(i);
            final TextView tv = (TextView) tabView.findViewById(android.R.id.title);
            tv.setTextSize(9);

        }
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("Tab Two")) {
                    LostFoundPetsActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
                if (tabId.equals("Tab Four")) {
                    mBtnNext.setText("Save");

                } else {
                    mBtnNext.setText("Next...");
                }
               /* if (tabId.equals("Tab One")) {
                    findViewById(R.id.sc_parrent).getParent().requestDisallowInterceptTouchEvent(true);
                }*/

            }
        });

    }
    public void petTypePopup() {

        final String[] items = {
                "Dog", "Cat"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Pet Type ");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                selectedPetType = items[item].toString();
                mBtnPetType.setText(items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void petBreedTypePopup(String selectedPetType) {
        if (selectedPetType != null && !selectedPetType.isEmpty()) {
            // doSomething

            final String[] items;
            if (selectedPetType.equals("Dog")) {
                items = new String[]{
                        "Akita", "Alaskan Malamute", "American English Coonhound", "American Eskimo Dog",
                        "American Foxhound", "American Pit Bull Terrier", "American Water Spaniel", "Anatolian Shepherd Dog",
                        "Appenzeller Sennenhunde", "Australian Cattle Dog", "Australian Shepherd", "Australian Terrier", "Azawakh",
                        "Barbet", "Basenji", "Basset Hound", "Beagle", "Bearded Collie", "Bedlington Terrier", "Belgian Malinois",
                        "Belgian Sheepdog", "Belgian Tervuren", "Berger Picard", "Bernese Mountain Dog", "Bichon Frise",
                        "Black and Tan Coonhound", "Black Russian Terrier", "Bloodhound", "Bluetick Coonhound", "Bolognese",
                        "Border Collie", "Border Terrier", "Borzoi", "Boston Terrier", "Bouvier des Flandres", "Boxer", "Boykin Spaniel",
                        "Bracco Italiano", "Briard", "Brittany", "Brussels Griffon", "Bull Terrier", "Bulldog", "Bullmastiff",
                        "Cairn Terrier", "Canaan Dog", "Cane Corso", "Cardigan Welsh Corgi", "Catahoula Leopard Dog",
                        "Cavalier King Charles Spaniel", "Cesky Terrier", "Chesapeake Bay Retriever", "Chihuahua", "Chinese Crested",
                        "Chinese Shar-Pei", "Chinook", "Chow Chow", "Clumber Spaniel", "Cockapoo",
                        "Cocker Spaniel", "Collie", "Coton de Tulear", "Curly-Coated Retriever",
                        "Dachshund", "Dalmatian", "Dandie Dinmont Terrier", "Doberman Pinscher", "Dogue de Bordeaux",
                        "English Cocker Spaniel", "English Foxhound", "English Setter", "English Springer Spaniel", "English Toy Spaniel",
                        "Entlebucher Mountain Dog", "Field Spaniel", "Finnish Lapphund", "Finnish Spitz", "Flat-Coated Retriever", "Fox Terrier",
                        "French Bulldog", "German Pinscher", "German Shepherd Dog", "German Shorthaired Pointer", "German Wirehaired Pointer",
                        "Giant Schnauzer", "Glen of Imaal Terrier", "Goldador", "Golden Retriever", "Goldendoodle", "Gordon Setter",
                        "Great Dane", "Great Pyrenees", "Greater Swiss Mountain Dog", "Greyhound", "Harrier", "Havanese", "Ibizan Hound",
                        "Icelandic Sheepdog", "Irish Red and White Setter", "Irish Setter", "Irish Terrier", "Irish Water Spaniel",
                        "Irish Wolfhound", "Italian Greyhound", "Jack Russell Terrier", "Japanese Chin", "Korean Jindo Dog", "Keeshond",
                        "Kerry Blue Terrier", "Komondor", "Kooikerhondje", "Kuvasz", "Labradoodle", "Labrador Retriever", "Lakeland Terrier",
                        "Lancashire Heeler", "Leonberger", "Lhasa Apso", "Lowchen", "Maltese", "Maltese Shih Tzu", "Maltipoo",
                        "Manchester Terrier", "Mastiff", "Miniature Pinscher", "Miniature Schnauzer", "Mutt", "Neapolitan Mastiff",
                        "Newfoundland", "Norfolk Terrier", "Norwegian Buhund", "Norwegian Elkhound", "Norwegian Lundehund", "Norwich Terrier",
                        "Nova Scotia Duck Tolling Retriever", "Old English Sheepdog", "Otterhound", "Papillon", "Peekapoo", "Pekingese",
                        "Pembroke Welsh Corgi", "Petit Basset Griffon Vendeen", "Pharaoh Hound", "Plott", "Pocket Beagle", "Pointer",
                        "Polish Lowland Sheepdog", "Pomeranian", "Poodle", "Portuguese Water Dog", "Pug", "Puggle", "Puli", "Pyrenean Shepherd",
                        "Rat Terrier", "Redbone Coonhound", "Rhodesian Ridgeback", "Rottweiler", "Saint Bernard", "Saluki", "Samoyed",
                        "Schipperke", "Schnoodle", "Scottish Deerhound", "Scottish Terrier", "Sealyham Terrier", "Shetland Sheepdog",
                        "Shiba Inu", "Shih Tzu", "Siberian Husky", "Silky Terrier", "Skye Terrier", "Sloughi", "Small Munsterlander Pointer",
                        "Soft Coated Wheaten Terrier", "Stabyhoun", "Staffordshire Bull Terrier", "Standard Schnauzer", "Sussex Spaniel",
                        "Swedish Vallhund", "Tibetan Mastiff", "Tibetan Spaniel", "Tibetan Terrier", "Toy Fox Terrier",
                        "Treeing Tennessee Brindle", "Treeing Walker Coonhound", "Vizsla", "Weimaraner", "Welsh Springer Spaniel",
                        "Welsh Terrier", "West Highland White Terrier", "Whippet", "Wirehaired Pointing Griffon", "Xoloitzcuintli",
                        "Yorkipoo", "Yorkshire Terrier"
                };
            } else {
                items = new String[]{
                        "Abyssinian", "American Bobtail", "American Curl", "American Shorthair", "American Wirehair", "Balinese", "Birman",
                        "Bombay", "British Shorthair", "Burmese", "Chartreux", "Colorpoint Shorthair", "Cornish Rex", "Devon Rex", "Egyptian Mau",
                        "European Burmese", "Exotic", "Havana Brown", "Japanese Bobtail", "Korat", "LaPerm", "Maine Coon", "Manx",
                        "Norwegian Forest Cat", "Ocicat", "Oriental", "Persian", "RagaMuffin", "Ragdoll", "Russian Blue", "Scottish Fold",
                        "Selkirk Rex", "Siamese", "Siberian", "Singapura", "Somali", "Sphynx", "Tonkinese", "Turkish Angora", "Turkish Van"
                };
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Breed Type ");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    // selectedPetType = items[item].toString();
                    mBtnBreedType.setText(items[item].toString());
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            Toast.makeText(LostFoundPetsActivity.this, "Please select Pet Type ", Toast.LENGTH_SHORT).show();
        }
    }

    public void petGenderePopup() {

        final String[] items = {
                "Male", "Female"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Gender ");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                // selectedPetType = items[item].toString();
                mBtnGender.setText(items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    public void petAgePopup() {

        final String[] items = {
                "Puppies/Kittens", "Adolescent", "Adult", "Senior"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Age Range ");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                // selectedPetType = items[item].toString();
                mBtnAge.setText(items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    public void petNeuturedAPopup() {

        final String[] items = {
                "Yes", "No"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Neutured ");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                // selectedPetType = items[item].toString();
                mBtnNeuture.setText(items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }

    public void petVaccinatedPopup() {

        final String[] items = {
                "Yes", "No"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Vaccinated ");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                // selectedPetType = items[item].toString();
                mBtnVaccinated.setText(items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


    }
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(LostFoundPetsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = BuyOrSalePetsActivity.Utility.checkPermission(LostFoundPetsActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case BuyOrSalePetsActivity.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
//code for deny
                }
                break;
        }
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mIvfirst.setImageBitmap(bm);
        mIvPetImage.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mIvfirst.setImageBitmap(thumbnail);
        mIvPetImage.setImageBitmap(thumbnail);
    }
}
