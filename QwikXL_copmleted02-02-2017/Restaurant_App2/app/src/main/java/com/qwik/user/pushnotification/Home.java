package com.qwik.user.pushnotification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.qwik.user.restaurant_app.R;
public class Home extends Activity {
    String pushMessage;
    TextView txttitle;
    Button ok,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Locate the TextView
        txttitle = (TextView) findViewById(R.id.push_message);
        ok=(Button)findViewById(R.id.ok);
        cancel=(Button)findViewById(R.id.cancel);


        Bundle extras = getIntent().getExtras();
        pushMessage=extras.getString("message");
        Toast.makeText(getApplicationContext(), "Message: " + pushMessage, Toast.LENGTH_LONG).show();
        txttitle.setText(pushMessage);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), com.qwik.user.restaurant_app.Home.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.exit(0);
//            }
//        });
    }

}
