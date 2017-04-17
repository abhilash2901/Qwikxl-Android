package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Order_history extends Activity {

    TextView t_status = null, t_total = null, b_total, b_grandtotal, b_rewardpoint;
    ListView order_history = null;
    ProgressDialog pgBar = null;
    Bundle bb = null;
    GetSet_orderid getSet_order = null;
    String order_id = null;
    ListViewAdapter listAdapter = null;
    ImageView home;
    static SharedPreferences pref;
    static String getStatus;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        t_status = (TextView) findViewById(R.id.t_status);
        t_total = (TextView) findViewById(R.id.t_total);
        b_total = (TextView) findViewById(R.id.b_total);
        b_grandtotal = (TextView) findViewById(R.id.b_grandtotal);
        b_rewardpoint = (TextView) findViewById(R.id.b_reward);
        home = (ImageView) findViewById(R.id.home);
        bb = new Bundle();
        order_history = (ListView) findViewById(R.id.order_history);
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        Intent i = getIntent();
        bb = i.getExtras();
        order_id = bb.getString("order_id");
        try {
            getting_order_item(order_id);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });

        } catch (Exception e) {

        }
    }

    private void getting_order_item(String order_id) {

        pgBar = Progress_class.createProgressDialog(Order_history.this);
        pgBar.show();
        ApiClient.getPosApp().getorderitem(order_id, new Callback<GetSet_orderid>() {
            public void success(GetSet_orderid cartModel, Response response) {
                System.out.print("*******Sucess download data********" + response.getUrl());
                if (pgBar.isShowing()) {
                    pgBar.dismiss();
                }
                consumeApiData1(cartModel);
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.print("*******Sucess download data********");
                consumeApiData1(null);
            }

            private void consumeApiData1(GetSet_orderid GetSet_order) {
                getSet_order = GetSet_order;
                if (getSet_order != null) {

                    try {
                        listAdapter = new ListViewAdapter(getApplicationContext(), GetSet_order);
                        order_history.setAdapter(listAdapter);
                       // t_total.setText(getSet_order.getgrand_total());
                        t_status.setText(getSet_order.getstatus());
                        b_total.setText("$"+getSet_order.gettotal());
                       // b_grandtotal.setText(getSet_order.getgrand_total());
                      //  b_rewardpoint.setText(getSet_order.getreward());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No Data Detected", Toast.LENGTH_SHORT).show();

                    if (pgBar.isShowing()) {
                        pgBar.dismiss();
                    }

                }
            }
        });
    }

    public class ListViewAdapter extends BaseAdapter {

        Context this_context;
        LayoutInflater inflater;
        DisplayImageOptions options;

        public ListViewAdapter(Context context, GetSet_orderid getSet_oder) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return getSet_order.getorder().size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = inflater.inflate(R.layout.order_data_popup, parent, false);
            TextView item = (TextView) itemView.findViewById(R.id.item);
            TextView quantity = (TextView) itemView.findViewById(R.id.quantity);
            TextView price = (TextView) itemView.findViewById(R.id.price);
            item.setText(getSet_order.getorder().get(position).getitem());
            price.setText(getSet_order.getorder().get(position).getprice());
            quantity.setText(getSet_order.getorder().get(position).getquantity());
            return itemView;
        }
    }

    protected void onResume() {
        super.onResume();
        getStatus = pref.getString("username", "");
    }
}
