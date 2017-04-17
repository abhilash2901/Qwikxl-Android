package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Forgot_password extends Activity {
    EditText email_id = null;
    Button forgot_password = null;
    String email = null;
    ProgressDialog pgBar = null;
    Data_values data_values = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email_id = (EditText) findViewById(R.id.email_id);
        data_values = new Data_values();

        forgot_password = (Button) findViewById(R.id.forgot_pass);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = email_id.getText().toString();
                if (email.equals("")) {
                    email_id.setError("Enter your user id");
                } else if (!email.matches(data_values.emailPattern))

                {
                    email_id.setError("Enter valid User Id");
                } else {
                    Toast.makeText(getApplicationContext(), "Sending...", Toast.LENGTH_SHORT).show();
                    sending_mail(email);
                }

            }

        });
    }

    private void sending_mail(String email) {
        pgBar = Progress_class.createProgressDialog(Forgot_password.this);
        pgBar.show();
        try {
            ApiClient.getPosApp().getpassword(email, new Callback<GetSet_login>() {
                @Override
                public void success(GetSet_login getSet_login, Response response) {
                    System.out.println("sucess Login  " + response);
                    if (pgBar.isShowing()) {
                        pgBar.dismiss();
                    }
                    String res = getSet_login.getstatus();
                    Log.e("Status value", res);
                    if (res.equalsIgnoreCase("Check Your Email")) {
                        try {
                            Toast.makeText(getApplicationContext(), "" + res, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(), Home.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (res.equalsIgnoreCase("Email does not exist")) {

                        Toast.makeText(getApplicationContext(), "" + res, Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "" + res, Toast.LENGTH_LONG).show();
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

