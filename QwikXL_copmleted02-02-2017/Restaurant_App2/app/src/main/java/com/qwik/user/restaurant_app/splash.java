package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;


public class splash extends Activity implements Animation.AnimationListener {

    ImageView image_view = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        image_view = (ImageView) findViewById(R.id.image_view);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(getApplicationContext(), Home.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }, 2000);
    }

    @Override
    public void onAnimationEnd(Animation arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAnimationRepeat(Animation arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAnimationStart(Animation arg0) {
        // TODO Auto-generated method stub
    }
}
