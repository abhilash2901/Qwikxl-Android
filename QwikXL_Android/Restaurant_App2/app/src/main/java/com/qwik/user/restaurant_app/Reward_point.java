package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Reward_point extends Activity {

    TextView reward = null;
    ProgressDialog pgBar = null;
    static String reward_point;
    String getStatus = null;
    static SharedPreferences pref = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reward_point);
        reward = (TextView) findViewById(R.id.reward);
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        try {
            getStatus = pref.getString("username", "");
            getting_reward(getStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getting_reward(String getStatus) {
        pgBar = Progress_class.createProgressDialog(Reward_point.this);
        pgBar.show();
        try {
            ApiClient.getPosApp().getrewardpoint(getStatus, new Callback<GetSet_login>() {
                @Override
                public void success(GetSet_login getSet_login, Response response) {

                    System.out.println("sucess Login  " + response);
                    if (pgBar.isShowing()) {
                        pgBar.dismiss();
                    }
                    String res = getSet_login.getstatus();
                    if (res.equalsIgnoreCase("true")) {
                        try {
                            reward_point = getSet_login.getreward();
                            reward.setText(reward_point);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (res.equalsIgnoreCase("Login Failed")) {

                        Toast.makeText(getApplicationContext(), "Username and Password are incorrect ", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    if (retrofitError.getResponse() != null) {
                        Log.e("Retrofit error", retrofitError.getCause().toString());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




