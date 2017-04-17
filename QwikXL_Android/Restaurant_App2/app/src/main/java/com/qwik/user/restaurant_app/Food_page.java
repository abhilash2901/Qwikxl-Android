package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.Api;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Food_page extends Activity {

    TextView fod_name = null, ff_name = null, f_price = null, overviewtv = null, nutriInfoTv = null, moreInfoTv = null, available_quantity = null, caption = null, cart_number = null;
    ImageView f_image = null;
    Button add_cart = null, my_account = null, order_status = null, reward = null;
    EditText quantity = null;
    ImageView cart = null, home = null;
    GetSet_fooditem getSet_fooditem = null;
    ProgressDialog pgBar = null;
    String food_item_id = null, getStatus = null, food_id = null, food_name = null, stock, shop_id, commnt, pric, total, stock_value;
    int quant = 1, flag, quan;
    Bundle bb = null, b = null;
    static SharedPreferences pref = null;
    ImageLoader imageLoader = null;
    DisplayImageOptions options = null;
    AABDatabaseManager db = null;
    ArrayList<ArrayList<Object>> data = null;
    ArrayList<Object> row = null;
    AlertDialog.Builder ab = null;
    AlertDialog dialog1 = null;
    LinearLayout food_layout, overview_layout, moreInfo_layout, nutriInfo_layout, moreinfo_line, nutriinfo_line, overview_line;
    ToggleButton overview, nutriinfoTb, moreInfoId;
    Animation slide = null;
    AABDatabaseManager db1;
    Animation shake = null;
    int alert_flag = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_page);
        food_layout = (LinearLayout) findViewById(R.id.food_layout);
        overview_line = (LinearLayout) findViewById(R.id.overview_line);
        moreinfo_line = (LinearLayout) findViewById(R.id.moreinfo_line);
        nutriinfo_line = (LinearLayout) findViewById(R.id.nutriinfo_line);
        overview_layout = (LinearLayout) findViewById(R.id.overview_layout);
        moreInfo_layout = (LinearLayout) findViewById(R.id.moreInfo_layout);
        nutriInfo_layout = (LinearLayout) findViewById(R.id.nutriInfo_layout);
        overview = (ToggleButton) findViewById(R.id.overviewid);
        nutriinfoTb = (ToggleButton) findViewById(R.id.nutriinfoTb);
        moreInfoId = (ToggleButton) findViewById(R.id.moreInfoId);
        fod_name = (TextView) findViewById(R.id.fod_name);
        ff_name = (TextView) findViewById(R.id.ff_name);
        f_price = (TextView) findViewById(R.id.ff_price);
        cart_number = (TextView) findViewById(R.id.cart_number);
        f_image = (ImageView) findViewById(R.id.f_image);
        overviewtv = (TextView) findViewById(R.id.overviewtv);
        nutriInfoTv = (TextView) findViewById(R.id.nutriInfoTv);
        moreInfoTv = (TextView) findViewById(R.id.moreInfoTv);
        caption = (TextView) findViewById(R.id.caption);
        available_quantity = (TextView) findViewById(R.id.available_quantity);
        cart = (ImageView) findViewById(R.id.cart);
        home = (ImageView) findViewById(R.id.home);
        add_cart = (Button) findViewById(R.id.add_cart);
        order_status = (Button) findViewById(R.id.order_status);
        my_account = (Button) findViewById(R.id.my_account);
        reward = (Button) findViewById(R.id.reward);
        quantity = (EditText) findViewById(R.id.quantity);
        quantity.setText("" + String.valueOf(quant));
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        db = new AABDatabaseManager(this);
        bb = new Bundle();
        b = new Bundle();
        slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        db1 = new AABDatabaseManager(this);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cart_shake);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration
                .createDefault(getApplicationContext()));
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageOnLoading(R.drawable.ic_launcher).cacheOnDisk(true)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        try {
            Intent i = getIntent();
            bb = i.getExtras();
            food_id = bb.getString("food_id");
            food_name = bb.getString("food_name");
            shop_id = bb.getString("shop_id");
            fod_name.setText(food_name);
            food_layout.setBackgroundResource(R.drawable.header22);
            // Toast.makeText(getApplicationContext(),food_id,Toast.LENGTH_SHORT).show();
            getting_fooditem(food_id);
            cart_item_no();

            overview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    if (overview.isChecked()) {
                        // The toggle is enabled
                        overview_layout.startAnimation(slide);
                        overview_layout.setVisibility(View.VISIBLE);
                    } else {
                        // The toggle is disabled
                        overview_layout.setVisibility(View.GONE);
                    }
                }
            });
            nutriinfoTb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (nutriinfoTb.isChecked()) {
                        // The toggle is enabled
                        nutriInfo_layout.startAnimation(slide);
                        nutriInfo_layout.setVisibility(View.VISIBLE);
                    } else {
                        // The toggle is disabled
                        nutriInfo_layout.setVisibility(View.GONE);
                    }
                }
            });
            moreInfoId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (moreInfoId.isChecked()) {
                        // The toggle is enabled
                        moreInfo_layout.startAnimation(slide);
                        moreInfo_layout.setVisibility(View.VISIBLE);
                    } else {
                        // The toggle is disabled
                        moreInfo_layout.setVisibility(View.GONE);
                    }
                }
            });

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
            add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        quan = Integer.parseInt(quantity.getText().toString().trim());
                        pric = getSet_fooditem.getprice().toString();
                        commnt = "nothing";
                        total = String.valueOf(quan * (Double.parseDouble(pric)));
                        stock_value = getSet_fooditem.getquantity();
                        if (quan <= 0) {
                            Toast.makeText(getApplication(), "1 sec! You didn't add quantity", Toast.LENGTH_SHORT).show();
                        } else if (quan > Integer.parseInt(available_quantity.getText().toString())) {
                            Toast.makeText(getApplication(), "Enter Available Quantity", Toast.LENGTH_SHORT).show();
                        } else {
                            data = db.getAllRowsAsArrays();
                            flag = 0;
                            for (int position = 0; position < data.size(); position++) {
                                row = data.get(position);
                                food_item_id = row.get(1).toString();
                                String shop_idd = row.get(8).toString();

                                if (Stores.store_id.equals(shop_idd)) {
                                    if (food_item_id.equals(food_id)) {
                                        int idd = Integer.parseInt(row.get(0).toString());
                                        int updated_quant = Integer.parseInt(row.get(3).toString());
                                        int qunatity_calculation = updated_quant + quan;
                                        if (qunatity_calculation > Integer.parseInt(available_quantity.getText().toString())) {
                                            flag = 1;
                                            Toast.makeText(getApplication(), "Please check your cart,Enter Available Quantity", Toast.LENGTH_SHORT).show();
                                        } else {
                                            quan = quan + updated_quant;
                                            String total1 = String.valueOf(quan * (Double.parseDouble(row.get(4).toString())));
                                            db.update_food(idd, String.valueOf(quan), total1, commnt, stock_value);
                                            cart_item_no();
                                            flag = 1;
                                            //alert_box();

                                        }
                                    }
                                } else {
                                    flag = 1;
                                    alert_box();
                                    // Toast.makeText(getApplicationContext(), "Different store is trying to add in cart", Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (flag == 0) {
                                // Toast.makeText(getApplicationContext(),stock_value,Toast.LENGTH_SHORT).show();
                                adding_cart();
                                // alert_box();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

        } catch (Exception e) {

        }
    }

    private void getting_fooditem(String key) {
        pgBar = Progress_class.createProgressDialog(Food_page.this);
        pgBar.show();
        ApiClient.getPosApp().getfooddata(key, new Callback<GetSet_fooditem>() {
            public void success(GetSet_fooditem cartModel, Response response) {
                if (pgBar.isShowing()) {
                    pgBar.dismiss();
                }
                consumeApiData1(cartModel);
            }

            @Override
            public void failure(RetrofitError error) {
                consumeApiData1(null);
            }

            private void consumeApiData1(GetSet_fooditem GetSet_fooditem) {
                getSet_fooditem = GetSet_fooditem;
                if (getSet_fooditem != null) {

                    try {
                        if (!getSet_fooditem.getimage().equals("")) {
                            imageLoader.displayImage(ApiClient.url_common + "/" + getSet_fooditem.getimage(), f_image, options,
                                    new SimpleImageLoadingListener() {
                                        Animation rotation;

                                        @Override
                                        public void onLoadingStarted(String imageUri, View view) {

                                        }

                                        @Override
                                        public void onLoadingComplete(String imageUri, View view,
                                                                      Bitmap loadedImage) {

                                            f_image.setScaleType(ImageView.ScaleType.FIT_XY);
                                        }
                                    }, new ImageLoadingProgressListener() {

                                        public void onProgressUpdate(String imageUri, View view,
                                                                     int current, int total) {
                                        }
                                    });
                        } else {
                            f_image.setBackgroundResource(R.drawable.ic_launcher);
                        }

                        stock = GetSet_fooditem.getquantity();
                        ff_name.setText(GetSet_fooditem.getname() + ": ");
                        f_price.setText("$" + GetSet_fooditem.getprice());
                        caption.setText(GetSet_fooditem.getdescription());
                        available_quantity.setText(stock);
//                        if(GetSet_fooditem.getoverview().equals("NULL"))
//                        {
//                            overview_line.setVisibility(View.GONE);
//                        }else{
//                            overviewtv.setText(GetSet_fooditem.getoverview());
//                        }
//                        if(GetSet_fooditem.getnutritionalinfo().equals("NULL"))
//                        {
//                            nutriinfo_line.setVisibility(View.GONE);
//                        }else{
//                            nutriInfoTv.setText(GetSet_fooditem.getnutritionalinfo());
//                        }
//                        if(GetSet_fooditem.getmoreinfo().equals("NULL"))
//                        {
//                            moreinfo_line.setVisibility(View.GONE);
//                        }else{
//                            moreInfoTv.setText(GetSet_fooditem.getmoreinfo());
//                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No food", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                db.delete();
                alert_flag = 0;
                adding_cart();
                // flag = 0;
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_flag = 0;
                dialog.dismiss();
            }
        });
        if (alert_flag == 0) {
            dialog.show();
            alert_flag = 1;
        }
//        //new Handler().postDelayed(new Runnable() {
//            public void run() {
//                dialog.dismiss();
//            }
//        }, 2 * 1000);
    }

    private void adding_cart() {
        db.addRow(food_id, food_name, String.valueOf(quan), pric, commnt, total, stock_value, shop_id);
        cart_item_no();
        flag = 1;
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
        getStatus = pref.getString("username", "");
        if (getStatus.equals("")) {
            my_account.setBackgroundResource(R.drawable.login);

        } else {
            my_account.setBackgroundResource(R.drawable.myaccnt);
        }
        cart_item_no();
    }
}



