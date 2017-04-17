package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class Order_id extends Activity {

    TextView order_id = null, view_order = null, transaction_id = null;
    Bundle b = null;
    String order_value = null, transactionid = null,unique_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_id);

        b = new Bundle();
        order_id = (TextView) findViewById(R.id.order_id);
        transaction_id = (TextView) findViewById(R.id.transaction_id);
        view_order = (TextView) findViewById(R.id.view_order);
        Intent i = getIntent();
        b = i.getExtras();
        order_value = b.getString("orderid");
        unique_id = b.getString("unique_id");
        transactionid = b.getString("transaction_id");

        try {
            order_id.setText("Your Order Id is: " + unique_id);
            transaction_id.setText("Transaction Id is: " + transactionid);
            view_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b.putString("order_id", order_value);
                    Intent i = new Intent(getApplicationContext(), Order_history.class);
                    i.putExtras(b);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        finish();
        Intent i = new Intent(getApplicationContext(), Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
