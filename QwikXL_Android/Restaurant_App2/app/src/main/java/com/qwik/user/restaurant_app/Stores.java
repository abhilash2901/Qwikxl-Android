package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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


public class Stores extends Activity {

    ListView lv = null;
    ImageView cart = null, home = null;
    TextView cart_number = null;
    ProgressDialog pgBar = null;
    GetSet_hotel getset_hotel = null;
    ListViewAdapter1 listAdapter1 = null;
    String keyvalue = null, lati = null, longi = null, getStatus = null,pickup_type;
    Float number = null;
    Bundle bb = null;
    static SharedPreferences pref = null;
    static  String store_id;
    Home home1 = null;
    LinearLayout custom_layout;
    AABDatabaseManager db1;
    Animation shake = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customlist);

        custom_layout = (LinearLayout) findViewById(R.id.image_layout);
        cart = (ImageView) findViewById(R.id.cart);
        home = (ImageView) findViewById(R.id.home);
        cart_number = (TextView) findViewById(R.id.cart_number);
        home1 = new Home();
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        bb = new Bundle();
        lv = (ListView) findViewById(R.id.list);
        db1 = new AABDatabaseManager(this);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cart_shake);

        try {

            Intent i = getIntent();
            bb = i.getExtras();
           // keyvalue = bb.getString("key");
            lati = bb.getString("lat");
            longi = bb.getString("lon");
            pickup_type = pref.getString("pickup_type", "");
            getStatus = pref.getString("username", "");

            custom_layout.setBackgroundResource(R.drawable.hearer2);
            getting_departments(lati, longi, pickup_type);
            cart_item_no();

        } catch (Exception e) {
            e.printStackTrace();
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
    }

    private void getting_departments(String lati, String longi,String pickup_type) {
        pgBar = Progress_class.createProgressDialog(Stores.this);
        pgBar.show();
        ApiClient.getPosApp().getCurrencydetails(lati,longi,pickup_type,new Callback<GetSet_hotel>() {
            public void success(GetSet_hotel cartModel, Response response) {
                //  Toast.makeText(getApplicationContext(),"api success",Toast.LENGTH_SHORT).show();
                System.out.print("*******Sucess download data********" + response.getUrl());
                if (pgBar.isShowing()) {
                    pgBar.dismiss();
                }
                consumeApiData1(cartModel);
            }

            @Override
            public void failure(RetrofitError error) {
                // Toast.makeText(getApplicationContext(),"api failed",Toast.LENGTH_SHORT).show();
                System.out.print("*******Sucess download data failed********");
                consumeApiData1(null);
            }

            private void consumeApiData1(GetSet_hotel GetSet_reg) {
                getset_hotel = GetSet_reg;
                if (getset_hotel != null) {

                    try {
                        listAdapter1 = new ListViewAdapter1(getApplicationContext(), GetSet_reg);
                        lv.setAdapter(listAdapter1);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String rest_id = getset_hotel.gethotel().get(position).getid();
                                String rest_name = getset_hotel.gethotel().get(position).getname();
                                store_id=rest_id;
                               // bb.putString("key", keyvalue);
                                bb.putString("rest_id", rest_id);
                                bb.putString("rest_name", rest_name);
                                Intent i = new Intent(getApplicationContext(), Department.class);
                                i.putExtras(bb);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                            }
                        });

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

    public class ListViewAdapter1 extends BaseAdapter {

        Context this_context;
        LayoutInflater inflater;
        ImageLoader imageLoader;
        DisplayImageOptions options;
        ImageView im;

        public ListViewAdapter1(Context context, GetSet_hotel getSet_reg) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getset_hotel = getSet_reg;
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
            return getset_hotel.gethotel().size();
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

            View itemView = inflater.inflate(R.layout.custom_list_row, parent, false);
            TextView tv = (TextView) itemView.findViewById(R.id.tv);
            //  TextView shop_distance = (TextView) itemView.findViewById(R.id.shop_distance);
            //  RatingBar rate = (RatingBar) itemView.findViewById(R.id.rate);
            // LayerDrawable stars = (LayerDrawable) rate.getProgressDrawable();
            //  stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            // TextView specials = (TextView) itemView.findViewById(R.id.specials);
//            TextView delivery_minim = (TextView) itemView.findViewById(R.id.delivery_minim);
//            TextView delivery_charge = (TextView) itemView.findViewById(R.id.delivery_charge);
//            TextView ship = (TextView) itemView.findViewById(R.id.ship);
            im = (ImageView) itemView.findViewById(R.id.im);


            try {
                if (!getset_hotel.gethotel().get(position).getimage().equals("")) {
                    imageLoader.displayImage(ApiClient.url_common + "/" + getset_hotel.gethotel().get(position).getimage(), im, options,
                            new SimpleImageLoadingListener() {
                                Animation rotation;

                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view,
                                                              Bitmap loadedImage) {
                                    im.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                }
                            }, new ImageLoadingProgressListener() {

                                public void onProgressUpdate(String imageUri, View view,
                                                             int current, int total) {
                                }
                            });
                } else {
                    im.setBackgroundResource(R.drawable.ic_launcher);
                }
                tv.setText(getset_hotel.gethotel().get(position).getname());
                String sdelivery_charge, sdelivery_minim;
                sdelivery_minim = getset_hotel.gethotel().get(position).getdelivery_minim();
                sdelivery_charge = getset_hotel.gethotel().get(position).getdelivery_charge();
                //specials.setText(getset_hotel.gethotel().get(position).getspecials());
                //shop_distance.setText(getset_hotel.gethotel().get(position).getdistance() + " miles away");
                String rate_value = getset_hotel.gethotel().get(position).getrate();
                number = Float.parseFloat(rate_value);
                // rate.setRating(number);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return itemView;
        }
    }

    private void get_hotelsname(String h_name) {
        pgBar = Progress_class.createProgressDialog(Stores.this);
        pgBar.show();
        ApiClient.getPosApp().gethotel(h_name, keyvalue, lati, longi, new Callback<GetSet_hotel>() {
            public void success(GetSet_hotel cartModel, Response response) {
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

            private void consumeApiData1(GetSet_hotel GetSet_reg) {
                getset_hotel = GetSet_reg;
                if (getset_hotel != null) {
                    try {
                        listAdapter1 = new ListViewAdapter1(getApplicationContext(), GetSet_reg);
                        lv.setAdapter(listAdapter1);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                String rest_id = getset_hotel.gethotel().get(position).getid();
                                String rest_name = getset_hotel.gethotel().get(position).getname();
                                bb.putString("rest_id", rest_id);
                                bb.putString("rest_name", rest_name);
                                Intent i = new Intent(getApplicationContext(), Department.class);
                                i.putExtras(bb);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            }
                        });

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
        cart_item_no();
    }
}
