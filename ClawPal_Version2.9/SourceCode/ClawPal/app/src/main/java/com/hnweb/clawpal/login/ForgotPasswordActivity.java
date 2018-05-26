package com.hnweb.clawpal.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hnweb.clawpal.R;
import com.hnweb.clawpal.Utils.ValidationMethods;
import com.hnweb.clawpal.WebUrl.WebURL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shree on 16-Dec-17.
 */

public class ForgotPasswordActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText et_emailId;
    Button btn_submit;
    ProgressDialog progressDialog;
    String message;
    int flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_btn_img);
        et_emailId = (EditText) findViewById(R.id.activity_forgotpwd_et_email);
        btn_submit = (Button) findViewById(R.id.activity_forgotpwd_btn_login);
        toolbar.setNavigationOnClickListener(

                new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        finish();
                    }

                }

        );
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidationMethods vm = new ValidationMethods();
                if (!vm.isValidEmail(et_emailId.getText().toString())) {
                    et_emailId.setError("Enter Valid Email-Id");
                    et_emailId.requestFocus();
                    return;
                } else {
                    forgotPassword();
                }
            }
        });
    }


    private void forgotPassword() {
        progressDialog = ProgressDialog.show(ForgotPasswordActivity.this, null, "Please wait....", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.FORGOT_PASSWORD + "&email=" + et_emailId.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        System.out.println("responseforgot" + response.toString());


                        try {
                            JSONObject j = new JSONObject(response);
                            flag = j.getInt("flag");
                            String res=j.getString("response");
                            JSONObject jsonObject = new JSONObject(res);
                            message = jsonObject.getString("msg");
                            System.out.println("message123" + message);
                            if (flag == 1) {
                                message = jsonObject.getString("msg");
                                System.out.println("message23432" + message);
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
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
                        progressDialog.dismiss();
                      /*  String reason = AppUtils.getVolleyError(ForgotPasswordActivity.this, error);
                        showAlertDialog(reason);*/
                    }
                });
        ;


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    //==================================================================================================
    private void showAlertDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
        builder.setTitle("Message")
                .setMessage(errorMessage)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}

