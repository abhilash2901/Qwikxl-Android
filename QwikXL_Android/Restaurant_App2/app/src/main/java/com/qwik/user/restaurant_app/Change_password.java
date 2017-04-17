package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Change_password extends Activity {
    Button change_password = null, cancel = null;
    EditText old_passsword = null, new_password1 = null, new_password2 = null;
    Animation shake = null;
    ProgressDialog pgBar = null;
    String getStatus = null, password = null;
    static SharedPreferences pref = null;
    Bundle bundle = null;
    Data_values data_values;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        data_values = new Data_values();

        old_passsword = (EditText) findViewById(R.id.old_pass);
        new_password1 = (EditText) findViewById(R.id.new_pass);
        new_password2 = (EditText) findViewById(R.id.re_newpass);
        change_password = (Button) findViewById(R.id.change_pass);
        cancel = (Button) findViewById(R.id.cancel);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        getStatus = pref.getString("username", "");
        bundle = new Bundle();
        try {
            Intent i = getIntent();
            bundle = i.getExtras();
            password = bundle.getString("password");
            change_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (old_passsword.getText().toString().equals("")) {
                        old_passsword.setError("Enter old password");
                        old_passsword.startAnimation(shake);
                    } else if (new_password1.getText().toString().equals("")) {
                        new_password1.setError("Enter new password");
                        new_password1.startAnimation(shake);
                    } else if (!new_password1.getText().toString().matches(data_values.passmatcher)) {
                        Toast.makeText(getApplicationContext(), "Password: must be at least 6 character; minimum 1 capital letters, 1 lower case letters, 1 numbers, 1 special character", Toast.LENGTH_LONG).show();
                        new_password1.startAnimation(shake);
                    } else if (new_password1.getText().length() < 6) {
                        new_password1.setError("Enter minimum 6 digit Password");
                        new_password1.startAnimation(shake);
                    } else if (new_password2.getText().toString().equals("")) {
                        new_password2.setError("re-enter new password");
                        new_password2.startAnimation(shake);
                    } else if (!new_password2.getText().toString().matches(data_values.passmatcher)) {
                        Toast.makeText(getApplicationContext(), "Password: must be at least 6 character; minimum 1 capital letters, 1 lower case letters, 1 numbers, 1 special character", Toast.LENGTH_LONG).show();
                        new_password2.startAnimation(shake);
                    } else if (new_password1.getText().length() < 6) {
                        new_password1.setError("Enter minimum 6 digit Password");
                        new_password1.startAnimation(shake);
                    } else if (!Data_values.md5(old_passsword.getText().toString()).equals(password)) {
                        old_passsword.setError("Incorrect old password");
                        old_passsword.startAnimation(shake);
                    } else if (old_passsword.getText().toString().equals(new_password1.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Old and New Password are same", Toast.LENGTH_SHORT).show();
                    } else if (!new_password2.getText().toString().equals(new_password1.getText().toString())) {

                        Toast.makeText(getApplicationContext(), "New Password Mismatching", Toast.LENGTH_SHORT).show();
                    } else {
                        changing_password();
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), My_account.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changing_password() {
        {
            try {
                pgBar = Progress_class.createProgressDialog(Change_password.this);
                pgBar.show();
                ApiClient.getPosApp().change_password(getStatus, Data_values.md5(new_password1.getText().toString()), new Callback<GetSet_login>() {
                    @Override
                    public void success(GetSet_login getSet_login, Response response) {
                        System.out.println("Sucess" + response.getUrl());
                        if (pgBar.isShowing()) {
                            pgBar.dismiss();
                        }
                        String res = getSet_login.getstatus();
                        if (res.equalsIgnoreCase("Password Changed Successfully")) {
                            try {
                                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), Home.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else if (res.equalsIgnoreCase("Updation Failed")) {

                            Toast.makeText(getApplicationContext(), "Updation Failed,Network Problem", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                    }
                });
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
        }
    }
}
