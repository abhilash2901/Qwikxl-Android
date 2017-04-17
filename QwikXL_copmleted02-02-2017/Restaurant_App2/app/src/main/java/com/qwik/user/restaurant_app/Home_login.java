package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Home_login extends Activity {

    EditText user = null, pass = null;
    Button login = null;
    TextView new_user = null, forgot_pass = null;
    String user_name = null, password = null;
    ProgressDialog pgBar = null;
    Data_values data_values = null;
    static SharedPreferences pref = null;
    Bundle bundle;
    Animation shake = null;
    LinearLayout login_layout = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login);
        new_user = (TextView) findViewById(R.id.new_user);
        forgot_pass = (TextView) findViewById(R.id.forgot_pass);
        login_layout = (LinearLayout) findViewById(R.id.login_layout);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        data_values = new Data_values();
        bundle = new Bundle();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    user_name = user.getText().toString().trim();
                    password = Data_values.md5(pass.getText().toString().trim());
                    if (user_name.equals("")) {
                        user.setError("Enter Username");
                        user.startAnimation(shake);
                    } else if (password.equals("")) {
                        pass.setError("Enter password");
                        pass.startAnimation(shake);
                    } else if (!user_name.matches(data_values.emailPattern)) {
                        user.setError("Enter valid User ID");
                        user.startAnimation(shake);
                    } else {

                        login_check();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Registration.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"Under process",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), Forgot_password.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    private void login_check() {
        pgBar = Progress_class.createProgressDialog(Home_login.this);
        pgBar.show();
        try {
            ApiClient.getPosApp().getlogin(user_name, password, new Callback<GetSet_login>() {
                @Override
                public void success(GetSet_login getSet_login, Response response) {
                    System.out.println("sucess Login  " + response);
                    if (pgBar.isShowing()) {
                        pgBar.dismiss();
                    }
                    String res = getSet_login.getstatus();
                    if (res.equalsIgnoreCase("Login Successful")) {
                        try {
                            Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                            pref = getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("username", user_name);
                            editor.putString("mobile", getSet_login.getmobile());
                            editor.commit();
                            onBackPressed();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (res.equalsIgnoreCase("Email or Password Incorrect")) {
                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                    } else if (res.equalsIgnoreCase("Login failed")) {
                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    try {
                        if (retrofitError.getResponse() != null) {
                            Log.e("Retrofit error", retrofitError.getCause().toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getDisplayHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}
