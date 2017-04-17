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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Order_status extends Activity {

    Bundle b = null;
    ListView order_list = null;
    ProgressDialog pgBar = null;
    GetSet_orderid getSet_order;
    ListViewAdapter listAdapter = null;
    Button my_account = null, serach = null, order_status = null, reward = null;
    static SharedPreferences pref = null;
    static String getStatus = null;
    ImageView home = null, cart_bt = null;
    TextView cart_number = null;
    AABDatabaseManager db1 = null;
    Animation shake = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_status);
        cart_number = (TextView) findViewById(R.id.cart_number);
        cart_bt = (ImageView) findViewById(R.id.cart);
        order_status = (Button) findViewById(R.id.order_status);
        my_account = (Button) findViewById(R.id.my_account);
        reward = (Button) findViewById(R.id.reward);
        home = (ImageView) findViewById(R.id.home);
        db1 = new AABDatabaseManager(this);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cart_shake);
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        order_list = (ListView) findViewById(R.id.order_list);
        b = new Bundle();
        getStatus = pref.getString("username", "");
        try {
            getting_order_history(getStatus);
            cart_item_no();

            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });
            cart_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Cart_sqlite.class);
                    startActivity(i);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getting_order_history(String userid) {
        pgBar = Progress_class.createProgressDialog(Order_status.this);
        pgBar.show();
        ApiClient.getPosApp().getorderdata(userid, new Callback<GetSet_orderid>() {
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
               // if(getSet_order.getstatus().equalsIgnoreCase("true"))
                //{
                if (getSet_order != null) {
               // if(getSet_order.getstatus().toString().equalsIgnoreCase("Success")){
                    try {
                        listAdapter = new ListViewAdapter(getApplicationContext(), GetSet_order);
                        order_list.setAdapter(listAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Record found !", Toast.LENGTH_SHORT).show();
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
        ImageLoader imageLoader;
        DisplayImageOptions options;
        ImageView im;

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

            View itemView = inflater.inflate(R.layout.order_history_inflate, parent, false);
            TextView order = (TextView) itemView.findViewById(R.id.orderid);
            TextView status = (TextView) itemView.findViewById(R.id.status);
            TextView date = (TextView) itemView.findViewById(R.id.date);
            final Button view_order = (Button) itemView.findViewById(R.id.view_order);
            view_order.setTag(position);
            order.setText(getSet_order.getorder().get(position).getunique_id());
            date.setText(getSet_order.getorder().get(position).getdate());
            view_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    String order_id = getSet_order.getorder().get(tag).getorderid();
                    b.putString("order_id", order_id);
                    Intent i = new Intent(getApplicationContext(), Order_history.class);
                    i.putExtras(b);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });
            return itemView;
        }
    }

    private void cart_item_no() {

        int item_no = 0;
        db1 = new AABDatabaseManager(this);
        ArrayList<ArrayList<Object>> data1 = db1.getAllRowsAsArrays();
        ArrayList<Object> row1 = null;
        for (int position = 0; position < data1.size(); position++) {
            row1 = data1.get(position);
            int quantity = Integer.parseInt(row1.get(3).toString());
            item_no = item_no + quantity;
        }
        cart_number.setText(String.valueOf(item_no));
        cart_number.startAnimation(shake);
    }

    protected void onResume() {
        super.onResume();
        cart_item_no();
    }

    public void onBackPressed() {
        Intent newIntent = new Intent(Order_status.this, Home.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
