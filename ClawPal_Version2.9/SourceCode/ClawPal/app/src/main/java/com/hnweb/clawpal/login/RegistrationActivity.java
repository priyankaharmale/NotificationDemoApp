package com.hnweb.clawpal.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hnweb.clawpal.AppController;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.WebUrl.WebURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HNWeb-11 on 7/26/2016.
 */
public class RegistrationActivity extends AppCompatActivity {
    EditText mEmail, mEtCompanyName, mEtAddress, lname, mEtCountry, mEtState, mEtCity, mEtZip;
    EditText mFullName, mPassword, mConfirmPassword, mTelphone;
    Button mSubmit;
    String password, RePassword;
    Toolbar toolbar;

    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_registration);
        getInit();
        mSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                   /* email = mEmail.getText().toString();
                    FullName = mFullName.getText().toString();*/
                    password = mPassword.getText().toString();
                    RePassword = mConfirmPassword.getText().toString();

                  /*  int selectedId = radioSexGroup.getCheckedRadioButtonId();
                    radioSexButton = (RadioButton) findViewById(selectedId);*/
                    if (!isValidEmail(mEmail.getText().toString())) {
                        Toast.makeText(RegistrationActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    } else if (password.length() == 0) {
                        Toast.makeText(RegistrationActivity.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                    } else if (RePassword.length() == 0) {
                        Toast.makeText(RegistrationActivity.this, "Please enter Confirm Password", Toast.LENGTH_SHORT).show();
                    } else if (!password.equals(RePassword)) {
                        Toast.makeText(getApplicationContext(), "Password and Confirm Password Should Be same", Toast.LENGTH_SHORT).show();
                  /*  } else if (radioSexButton.getText().length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please select Subscribe", Toast.LENGTH_SHORT).show();
                    } else if (!mChkPolicy.isChecked()) {
                        Toast.makeText(getApplicationContext(), "Please check privacy policy", Toast.LENGTH_SHORT).show();*/
                    } else {
                        registeruser();
                        //new Sign_Up_Task().execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void registeruser() {
        //    String url = WebUrl.USER_REGISTRATION_URL + "first_name=" + mFullName.getText().toString() + "&last_name=" + lname.getText().toString() + "&email=" + mEmail.getText().toString() + "&phone=" + phone.toString() + "&role=" + mEtYourRole.getText() + "&training_taken=" + "YES" + "&training_vendor=" + mEtTraiVendor.getText() + "&most_used_vendors=" + mEtMostUseVendor.getText() + "&company=" + mEtCompanyName.getText().toString() + "&address=" + mEtAddress.getText() + "&country=" + mEtCountry.getText() + "&state=" + mEtState.getText() + "&city=" + mEtCity.getText() + "&zipcode=" + mEtZip.getText() + "&password=" + password.toString() + "&subscribe=" + radioSexButton.getText();
        String url = WebURL.REGISTER_URL + "full_name=NA&email=" + mEmail.getText().toString() + "&phone=NA&password=" + password.toString() + "&confirm_password=" + password.toString();
        // String url="http://designer321.com/knjohn/etac/api/sign-up.php?first_name=dghu&last_name=dgj&email=null&phone=234556778&role=Vendor/Partner Engineer&training_taken=Storage&training_vendor=SkyLine&most_used_vendors=Hp-Aruba&company=gf&address=gh&country=USA&state=USA&city=ABC&zipcode=123&password=123&subscribe=Yes";
        String final_url = url.replaceAll(" ", "%20");
        // JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, WebUrl.USER_REGISTRATION_URL+)&email="+email+"&phone="+phone.toString()+"&role="+mSpRole.getSelectedItem().toString()+"&training_taken="+mSptrainTaken.getSelectedItem().toString(), null, new Response.Listener<JSONObject>() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, final_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("Responce===", response.toString());
                try {

                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject jsonObject2 = jsonObject.getJSONObject("response");
                    int msg_code = jsonObject2.getInt("flag");

                    if (msg_code == 1) {
                        //displayAlertDialog(jsonObject.getString("msg"));
                        displayAlertDialog(jsonObject2.getString("msg"));


                    } else {
                        try {
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(RegistrationActivity.this);
                            builder.setMessage(jsonObject2.getString("msg"));
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                   // finish();
                                }
                            });
                            android.support.v7.app.AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest, "jreq");
    }

    public void getInit() {
        mPassword = (EditText) findViewById(R.id.mPasword_editText_signup);
        mConfirmPassword = (EditText) findViewById(R.id.mConfirmpasword_editText_signup);
        mEmail = (EditText) findViewById(R.id.mEmail_editText_signup);
        mSubmit = (Button) findViewById(R.id.mSubmit_button_signup);
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
    }

    // validating email id
    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void displayAlertDialog(String msg) {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setMessage(msg);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            android.support.v7.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
