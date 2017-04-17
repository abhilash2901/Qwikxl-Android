package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Food_list extends Activity {

    GetSet_Foodlist getSet_foodlist = null;
    ListViewAdapter1 listAdapter1 = null;
    ListView lv = null;
    ProgressDialog pgBar = null;
    String cat_id = null, ca_name = null, food_value;
    static SharedPreferences pref = null;
    Float number = null;
    Bundle bb = null, b = null;
    String getStatus = null, food_id = null, shop_id, stock, food_idd, price_value, total_amount, food_name1, stock_data;
    TextView cat_name = null, price = null, available_stock = null, cart_number = null;
    ImageView cart = null, home = null;
    LinearLayout foodlist_layout = null;
    int position_new, quantitty = 0;
    AABDatabaseManager db = null;
    ArrayList<ArrayList<Object>> data = null;
    ArrayList<Object> row = null;
    AlertDialog.Builder ab = null;
    AlertDialog dialog = null;
    String commnt = "Nothing";
    Button my_account = null, serach = null, order_status = null, reward = null;
    AABDatabaseManager db1;
    Animation shake = null;
    Cart_sqlite cart_sqlite = null;
    int flag, food_quantity;
    int alert_flag=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);

        foodlist_layout = (LinearLayout) findViewById(R.id.foodlist_layout);
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        db = new AABDatabaseManager(this);
        cart = (ImageView) findViewById(R.id.cart);
        home = (ImageView) findViewById(R.id.home);
        cat_name = (TextView) findViewById(R.id.cat_name);
        cart_number = (TextView) findViewById(R.id.cart_number);
        order_status = (Button) findViewById(R.id.order_status);
        my_account = (Button) findViewById(R.id.my_account);
        reward = (Button) findViewById(R.id.reward);
        bb = new Bundle();
        b = new Bundle();
        lv = (ListView) findViewById(R.id.list);
        lv.setItemsCanFocus(true);
        db1 = new AABDatabaseManager(this);
        cart_sqlite = new Cart_sqlite();
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cart_shake);
        try {
            Intent i = getIntent();
            bb = i.getExtras();
            cat_id = bb.getString("subcategory_idd");
            shop_id = bb.getString("shop_id");
            ca_name = bb.getString("subcat_name");
            // food_value = bb.getString("key");
            //  Toast.makeText(getApplicationContext(),cat_id+" cat id food list",Toast.LENGTH_SHORT).show();
            cat_name.setText(ca_name);
            getStatus = pref.getString("username", "");
            foodlist_layout.setBackgroundResource(R.drawable.header22);

            getting_foodlist(shop_id, cat_id);
            cart_item_no();
            if (getStatus.equals("")) {
                my_account.setBackgroundResource(R.drawable.login);

            } else {
                my_account.setBackgroundResource(R.drawable.myaccnt);
            }
        } catch (Exception e) {

        }
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Cart_sqlite.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Home.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        my_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getStatus = pref.getString("username", "");
                if (getStatus.equals("")) {
                    b.putString("class_name", "Home");
                    Intent i = new Intent(getApplicationContext(), Home_login.class);
                    i.putExtras(b);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                } else {
                    b.putString("username", getStatus);
                    Intent i = new Intent(getApplicationContext(), My_account.class);
                    i.putExtras(b);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            }
        });
        reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getStatus = pref.getString("username", "");
                    if (getStatus.equals("")) {

                        Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), Home_login.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    } else {
                        Intent i = new Intent(getApplicationContext(), Reward_point.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        order_status.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                getStatus = pref.getString("username", "");
                                                if (getStatus.equals("")) {
                                                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_LONG).show();
                                                    Intent i = new Intent(getApplicationContext(), Home_login.class);
                                                    startActivity(i);
                                                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                                                } else {
                                                    Intent i = new Intent(getApplicationContext(), Order_status.class);
                                                    startActivity(i);
                                                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                                }
                                            }
                                        }
        );

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

    private void getting_foodlist(String shop_id, String key) {
        pgBar = Progress_class.createProgressDialog(Food_list.this);
        pgBar.show();
        ApiClient.getPosApp().getfooddetails(shop_id, key, new Callback<GetSet_Foodlist>() {
            public void success(GetSet_Foodlist cartModel, Response response) {
                if (pgBar.isShowing()) {
                    pgBar.dismiss();
                }
                consumeApiData1(cartModel);
            }

            @Override
            public void failure(RetrofitError error) {
                if (pgBar.isShowing()) {
                    pgBar.dismiss();
                }
                consumeApiData1(null);
            }

            private void consumeApiData1(GetSet_Foodlist GetSet_food) {
                getSet_foodlist = GetSet_food;
                if (getSet_foodlist != null) {

                    try {
                        listAdapter1 = new ListViewAdapter1(getApplicationContext(), GetSet_food);
                        lv.setAdapter(listAdapter1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public class ListViewAdapter1 extends BaseAdapter {

        Context this_context;
        LayoutInflater inflater;
        ImageLoader imageLoader;
        DisplayImageOptions options;
        ImageView im;
        View itemView1;

        public ListViewAdapter1(Context context, GetSet_Foodlist getSet_food1) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getSet_foodlist = getSet_food1;
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration
                    .createDefault(getApplicationContext()));
            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .showImageOnLoading(R.drawable.ic_launcher).cacheOnDisk(true)
                    .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return getSet_foodlist.getfoodlist().size();
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

            final View itemView = inflater.inflate(R.layout.foodlist_row, parent, false);
            TextView f_name = (TextView) itemView.findViewById(R.id.f_name);
            TextView caption = (TextView) itemView.findViewById(R.id.caption);
            available_stock = (TextView) itemView.findViewById(R.id.available_stock);
            price = (TextView) itemView.findViewById(R.id.price);
            Button add_basket = (Button) itemView.findViewById(R.id.add_basket);
            final EditText quantity = (EditText) itemView.findViewById(R.id.quantity);
            im = (ImageView) itemView.findViewById(R.id.im);
            itemView.setId(position);
            itemView.setLabelFor(position);
            itemView1 = (View) findViewById(itemView.getId());
            try {
                if (!getSet_foodlist.getfoodlist().get(position).getimage().equals("")) {
                    imageLoader.displayImage(ApiClient.url_common + "/" + getSet_foodlist.getfoodlist().get(position).getimage(), im, options,
                            new SimpleImageLoadingListener() {
                                Animation rotation;

                                @Override
                                public void onLoadingStarted(String imageUri, View view) {

                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view,
                                                              Bitmap loadedImage) {
                                    im.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                            }, new ImageLoadingProgressListener() {

                                public void onProgressUpdate(String imageUri, View view,
                                                             int current, int total) {
                                }
                            });
                } else {
                    im.setBackgroundResource(R.drawable.ic_launcher);
                }
                stock = getSet_foodlist.getfoodlist().get(position).get_quantity();
                f_name.setText(getSet_foodlist.getfoodlist().get(position).getfood_name());
                price.setText("$" + getSet_foodlist.getfoodlist().get(position).getprice());
                caption.setText(getSet_foodlist.getfoodlist().get(position).get_description());
                available_stock.setText(stock);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        v = itemView;
                        int position_new = v.getLabelFor();
                        food_id = getSet_foodlist.getfoodlist().get(position_new).getfood_id();
                        String food_name = getSet_foodlist.getfoodlist().get(position_new).getfood_name();
                        bb.putString("food_id", food_id);
                        bb.putString("food_name", food_name);
                        bb.putString("shop_id", shop_id);
                        Intent i = new Intent(getApplicationContext(), Food_page.class);
                        i.putExtras(bb);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                    }
                });

                add_basket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            v = itemView;
                            int position_new1 = v.getLabelFor();
                            food_idd = getSet_foodlist.getfoodlist().get(position_new1).getfood_id();
                            food_name1 = getSet_foodlist.getfoodlist().get(position_new1).getfood_name();
                            food_quantity = Integer.parseInt(quantity.getText().toString());
                            price_value = getSet_foodlist.getfoodlist().get(position_new1).getprice();
                            total_amount = String.valueOf(food_quantity * (Double.parseDouble(price_value)));
                            stock_data = getSet_foodlist.getfoodlist().get(position_new1).get_quantity();

                            if (food_quantity <= 0) {
                                Toast.makeText(getApplication(), "1 sec! You didn't add quantity", Toast.LENGTH_SHORT).show();
                            } else if (food_quantity > Integer.parseInt(getSet_foodlist.getfoodlist().get(position_new1).get_quantity())) {
                                Toast.makeText(getApplication(), "Enter Available Quantity", Toast.LENGTH_SHORT).show();

                            } else {
                                data = db.getAllRowsAsArrays();
                                flag = 0;
                                for (int position = 0; position < data.size(); position++) {
                                    row = data.get(position);
                                    String food_item_id = row.get(1).toString();
                                    String shop_idd = row.get(8).toString();
                                    if (Stores.store_id.equals(shop_idd)) {

                                        if (food_idd.equals(food_item_id)) {
                                            int idd = Integer.parseInt(row.get(0).toString());
                                            int updated_quant = Integer.parseInt(row.get(3).toString());
                                            int qunatity_calculation = updated_quant + food_quantity;
                                            if (qunatity_calculation > Integer.parseInt(getSet_foodlist.getfoodlist().get(position_new1).get_quantity())) {
                                                flag = 1;
                                                Toast.makeText(getApplication(), "Please check your cart,Enter Available Quantity", Toast.LENGTH_SHORT).show();
                                            } else {
                                                food_quantity = food_quantity + updated_quant;
                                                String total1 = String.valueOf(food_quantity * (Double.parseDouble(row.get(4).toString())));
                                                db.update_food(idd, String.valueOf(food_quantity), total1, commnt, stock_data);
                                                cart_item_no();
                                                flag = 1;

                                            }
                                        }

                                    } else {
                                        flag = 1;
                                        alert_box();
                                        // Toast.makeText(getApplicationContext(), "Different store is trying to add in cart", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (flag == 0) {
                                    adding_cart();
                                    //Toast.makeText(getApplicationContext(), "db adding", Toast.LENGTH_SHORT).show();


                                    //alert_box();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return itemView;
        }
    }

    private void adding_cart() {
        db.addRow(food_idd, food_name1, String.valueOf(food_quantity), price_value, commnt, total_amount, stock_data, shop_id);
        cart_item_no();
        flag = 1;
    }

    protected void onResume() {
        super.onResume();
        getStatus = pref.getString("username", "");
        if (getStatus.equals("")) {
            my_account.setBackgroundResource(R.drawable.login);

        } else {
            my_account.setBackgroundResource(R.drawable.myaccnt);
        }
        cart_item_no();
    }

    public void alert_box() {

        final Dialog dialog = new Dialog(this, R.style.CustomDialogTheme);
        dialog.setContentView(R.layout.popup_order);
        Button yes = (Button) dialog.findViewById(R.id.yes);
        Button no = (Button) dialog.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                alert_flag=0;
                db.delete();
                adding_cart();
              //  flag = 0;
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.e("dismiss", "no click");
                alert_flag=0;
                dialog.dismiss();
            }
        });
        if(alert_flag==0) {
            dialog.show();
            alert_flag=1;
        }


//        //new Handler().postDelayed(new Runnable() {
//            public void run() {
//                dialog.dismiss();
//            }
//        }, 2 * 1000);
    }
}
