package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


public class Payment_option extends Activity {

    LinearLayout google_wallet = null, visa_card = null;
    Bundle b1 = null, b2 = null, b3 = null;
    ImageButton back = null;
    String amount = null, grandtotal = null, reward = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        google_wallet = (LinearLayout) findViewById(R.id.google_wallet);
        visa_card = (LinearLayout) findViewById(R.id.visa_card);
        back = (ImageButton) findViewById(R.id.back);
        b1 = new Bundle();
        b2 = new Bundle();
        b3 = new Bundle();
        try {
            Intent i = getIntent();
            b1 = i.getExtras();
            amount = b1.getString("sum");
            grandtotal = b1.getString("Grandtotal");
            reward = b1.getString("rewardpoint");
        } catch (Exception e) {

        }
        visa_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    b3.putString("sum", amount);
                    b3.putString("Grandtotal", grandtotal);
                    b3.putString("rewardpoint", reward);
                    Intent i = new Intent(getApplicationContext(), Proceed.class);
                    i.putExtras(b3);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        google_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "On progressing", Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Home.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
