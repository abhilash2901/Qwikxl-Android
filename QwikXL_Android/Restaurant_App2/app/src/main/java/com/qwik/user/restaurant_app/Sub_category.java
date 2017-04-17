package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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


public class Sub_category extends Activity {

    TextView subcat_name = null, cart_number = null;
    ListView sub_list = null;
    Bundle bb = null, b = null;
    String category_name = null, category_id = null, shop_id = null, getStatus = null;

    ProgressDialog pgBar = null;
    GetSet_subcategory getSet_subcategory = null;
    ListViewAdapter1 listAdapter1 = null;
    ImageView cart = null, home = null;
    Button my_account = null, order_status = null, reward = null;
    static SharedPreferences pref = null;
    Animation shake = null;
    AABDatabaseManager db1 = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        subcat_name = (TextView) findViewById(R.id.subcat_name);
        cart_number = (TextView) findViewById(R.id.cart_number);
        sub_list = (ListView) findViewById(R.id.sub_list);
        cart = (ImageView) findViewById(R.id.cart);
        home = (ImageView) findViewById(R.id.home);
        order_status = (Button) findViewById(R.id.order_status);
        my_account = (Button) findViewById(R.id.my_account);
        reward = (Button) findViewById(R.id.reward);
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cart_shake);
        db1 = new AABDatabaseManager(this);
        bb = new Bundle();
        b = new Bundle();

        try {
            Intent i = getIntent();
            bb = i.getExtras();
            category_name = bb.getString("category_name");
            category_id = bb.getString("category_id");
            shop_id = bb.getString("shop_id");
            pref = getSharedPreferences("Login", MODE_PRIVATE);
            getStatus = pref.getString("username", "");
            subcat_name.setText(category_name);
            //  Toast.makeText(getApplicationContext(),category_id+" cat id",Toast.LENGTH_SHORT).show();
            getting_subcategory(category_id, shop_id);
            cart_item_no();
            if (getStatus.equals("")) {
                my_account.setBackgroundResource(R.drawable.login);

            } else {
                my_account.setBackgroundResource(R.drawable.myaccnt);
            }
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

    private void getting_subcategory(String category_id1, String shop_id1) {
        pgBar = Progress_class.createProgressDialog(Sub_category.this);
        pgBar.show();
        ApiClient.getPosApp().getsub_Categaorydetails(shop_id1, category_id1, "", new Callback<GetSet_subcategory>() {
            public void success(GetSet_subcategory cartModel, Response response) {

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

            private void consumeApiData1(GetSet_subcategory GetSet_subcatt) {
                getSet_subcategory = GetSet_subcatt;
                if (getSet_subcategory.getstatus().equals("true")) {
                    if (getSet_subcategory != null) {
                        try {

                            listAdapter1 = new ListViewAdapter1(getApplicationContext(), GetSet_subcatt);
                            sub_list.setAdapter(listAdapter1);
                            sub_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String subcategory_idd = getSet_subcategory.getsubcategory().get(position).getsubcat_id();
                                    String subcategory_namee = getSet_subcategory.getsubcategory().get(position).getsubCat_name();
                                    bb.putString("subcategory_idd", subcategory_idd);
                                    bb.putString("shop_id", shop_id);
                                    bb.putString("subcat_name", subcategory_namee);
                                    // bb.putString("shop_id", keyvalue);
                                    // Toast.makeText(getApplicationContext(), subcategory_idd + "," + subcategory_namee, Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getApplicationContext(), Food_list.class);
                                    i.putExtras(bb);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (getSet_subcategory.getstatus().equals("false")) {
                    try {
                        if (pgBar.isShowing()) {
                            pgBar.dismiss();
                        }

                        Toast.makeText(getApplicationContext(), "No Data Detected", Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        e.printStackTrace();
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
        ImageView sub_image;

        public ListViewAdapter1(Context context, GetSet_subcategory getSet_catt1) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getSet_subcategory = getSet_catt1;
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
            return getSet_subcategory.getsubcategory().size();
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

            View itemView = inflater.inflate(R.layout.sub_category_list, parent, false);
            TextView subcat_name = (TextView) itemView.findViewById(R.id.sub_name);
            sub_image = (ImageView) itemView.findViewById(R.id.sub_image);
            try {
                if (!getSet_subcategory.getsubcategory().get(position).getsubimage().equals("")) {

                    imageLoader.displayImage(ApiClient.url_common+"/"+getSet_subcategory.getsubcategory().get(position).getsubimage(), sub_image, options,
                            new SimpleImageLoadingListener() {
                                Animation rotation;

                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view,
                                                              Bitmap loadedImage) {
                                    sub_image.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                            }, new ImageLoadingProgressListener() {

                                public void onProgressUpdate(String imageUri, View view,
                                                             int current, int total) {
                                }
                            });
                } else {
                    sub_image.setBackgroundResource(R.drawable.ic_launcher);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            subcat_name.setText(getSet_subcategory.getsubcategory().get(position).getsubCat_name());
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
        getStatus = pref.getString("username", "");
        if (getStatus.equals("")) {
            my_account.setBackgroundResource(R.drawable.login);

        } else {
            my_account.setBackgroundResource(R.drawable.myaccnt);
        }
        cart_item_no();
    }
}
