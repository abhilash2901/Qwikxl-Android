package com.qwik.user.pushnotification;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.qwik.user.restaurant_app.R;


public class MainActivity extends Activity {

    String PROJECT_NUMBER="707970948346";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_push);

        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {

                Log.e("Registration id", registrationId);
                //send this registrationId to your server

            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
    }
}
