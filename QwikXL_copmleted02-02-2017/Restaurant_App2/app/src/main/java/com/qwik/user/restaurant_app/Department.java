package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Department extends Activity {

    GridView lv = null;
    TextView rest_name = null, cart_number = null;
    ImageView cart = null, home = null, image_slider = null;
    ImageButton category_search = null;
    ProgressDialog pgBar = null;
    GetSet_category getSet_category = null;
    ListViewAdapter1 listAdapter1 = null;
    String getStatus = null;
    String keyvalue = null, res_name = null, category_key = null;
    Bundle bb = null, b = null;
    LinearLayout category_layout, no_result, no_data;
    static SharedPreferences pref = null;
    Button my_account = null, order_status = null, reward = null;
    EditText category_edittext = null;
    AABDatabaseManager db1 = null;
    Animation shake = null;
    String sliderr[] = new String[10];
    String im1 = null, im2 = null, im3 = null;
    Bitmap bitmap = null;
    Handler handler = null;
    Runnable runnable = null;
    TextView view_all_department;
    int banner_size;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        category_layout = (LinearLayout) findViewById(R.id.category_layout);
        rest_name = (TextView) findViewById(R.id.rest_name);
        cart_number = (TextView) findViewById(R.id.cart_number);
        cart = (ImageView) findViewById(R.id.cart);
        home = (ImageView) findViewById(R.id.home);
        image_slider = (ImageView) findViewById(R.id.image_slider);
        order_status = (Button) findViewById(R.id.order_status);
        my_account = (Button) findViewById(R.id.my_account);
        reward = (Button) findViewById(R.id.reward);
        category_edittext = (EditText) findViewById(R.id.category_edittext);
        category_search = (ImageButton) findViewById(R.id.category_search);
        view_all_department = (TextView) findViewById(R.id.view_all);
        no_result = (LinearLayout) findViewById(R.id.no_result);
        no_data = (LinearLayout) findViewById(R.id.no_data);
        db1 = new AABDatabaseManager(this);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cart_shake);
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        bb = new Bundle();
        b = new Bundle();
        lv = (GridView) findViewById(R.id.list);
        handler = new Handler();
        try {
            Arrays.fill(sliderr, null);
            Intent i = getIntent();
            bb = i.getExtras();
            keyvalue = bb.getString("rest_id");
            res_name = bb.getString("rest_name");
            category_key = bb.getString("key");
            rest_name.setText(res_name);
            pref = getSharedPreferences("Login", MODE_PRIVATE);
            getStatus = pref.getString("username", "");
            category_layout.setBackgroundResource(R.drawable.header22);
            getting_category(keyvalue);
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
        category_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handler.removeCallbacks(runnable);
                    getting_selected_category(keyvalue, category_edittext.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        view_all_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    lv.setVisibility(View.VISIBLE);
                    no_result.setVisibility(View.GONE);
                    getting_category(keyvalue);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void getting_selected_category(String grocery_id, final String keyvalue1) {
        try {
            pgBar = Progress_class.createProgressDialog(Department.this);
            pgBar.show();
            ApiClient.getPosApp().getCategaorydetails(grocery_id, keyvalue1, new Callback<GetSet_category>() {
                public void success(GetSet_category cartModel, Response response) {

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

                private void consumeApiData1(GetSet_category GetSet_catt) {
                    getSet_category = GetSet_catt;
//                    String status=getSet_category.getstatus();
//                    Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
                    if (getSet_category.getstatus().equals("true")) {
                        if (getSet_category != null) {
                            try {
                                listAdapter1 = new ListViewAdapter1(getApplicationContext(), GetSet_catt);
                                lv.setAdapter(listAdapter1);
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String cat_id = getSet_category.getcategory().get(position).getcat_id();
                                        String cat_name = getSet_category.getcategory().get(position).getCat_name();
                                        bb.putString("Dep_id", cat_id);
                                        bb.putString("Dep_name", cat_name);
                                        bb.putString("shop_id", keyvalue);
                                        Intent i = new Intent(getApplicationContext(), Main_category.class);
                                        i.putExtras(bb);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                    }
                                });
                                if (getSet_category.getbanners().size() != 0) {
                                    banner_size = getSet_category.getbanners().size();
                                    Log.e("banner_size", String.valueOf(banner_size));
                                    slider_image();
                                }
//                                else {
//                                    Toast.makeText(getApplicationContext(),"no slider",Toast.LENGTH_SHORT).show();
//                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (getSet_category.getstatus().equals("false")) {

                        if (pgBar.isShowing()) {
                            pgBar.dismiss();
                        }
                        category_edittext.setText("");
                        lv.setAdapter(null);
                        no_result.setVisibility(View.VISIBLE);
                        lv.setVisibility(View.GONE);
                        //Toast.makeText(getApplicationContext(), "No Matching Found", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getting_category(String key) {
        pgBar = Progress_class.createProgressDialog(Department.this);
        pgBar.show();
        ApiClient.getPosApp().getCategaorydetails(key, "", new Callback<GetSet_category>() {
            public void success(GetSet_category cartModel, Response response) {

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

            private void consumeApiData1(GetSet_category GetSet_catt) {
                getSet_category = GetSet_catt;
                if (getSet_category.getstatus().equals("true")) {
                    if (getSet_category != null) {
                        try {

                            listAdapter1 = new ListViewAdapter1(getApplicationContext(), GetSet_catt);
                            lv.setAdapter(listAdapter1);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String cat_id = getSet_category.getcategory().get(position).getcat_id();
                                    String cat_name = getSet_category.getcategory().get(position).getCat_name();
                                    bb.putString("Dep_id", cat_id);
                                    bb.putString("Dep_name", cat_name);
                                    bb.putString("shop_id", keyvalue);
                                    Intent i = new Intent(getApplicationContext(), Main_category.class);
                                    i.putExtras(bb);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                }
                            });
                            if (getSet_category.getbanners().size() != 0) {
                                banner_size = getSet_category.getbanners().size();
                                //  Log.e("banner_size", String.valueOf(banner_size));
                                slider_image();
                            }
//                            else {
//                                Toast.makeText(getApplicationContext(),"no slider",Toast.LENGTH_SHORT).show();
//                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (getSet_category.getstatus().equals("false")) {
                    try {
                        if (pgBar.isShowing()) {
                            pgBar.dismiss();
                        }
                        lv.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);
                        // Toast.makeText(getApplicationContext(), "No Data Detected", Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void slider_image() {
        try {

            runnable = new Runnable() {
                int i = 0;

                public void run() {
                    new LoadImage().execute(ApiClient.url_common + "/" + getSet_category.getbanners().get(i).getimage());
                    //Toast.makeText(getApplicationContext(),"handler"+getSet_category.getbanners().get(i).getimage(), Toast.LENGTH_SHORT).show();
                    i++;
                    if (i >= banner_size) {
                        i = 0;
                    }

                    handler.postDelayed(this, 4000);  //for interval...
                }
            };
            handler.postDelayed(runnable, 4000); //for initial delay..

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ListViewAdapter1 extends BaseAdapter {

        Context this_context;
        LayoutInflater inflater;
        ImageLoader imageLoader;
        DisplayImageOptions options;
        ImageView im;

        public ListViewAdapter1(Context context, GetSet_category getSet_catt1) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getSet_category = getSet_catt1;
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
            return getSet_category.getcategory().size();
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

            View itemView = inflater.inflate(R.layout.list_row_category, parent, false);
            TextView cat_name = (TextView) itemView.findViewById(R.id.cat_name);
            im = (ImageView) itemView.findViewById(R.id.im);
            try {
                if (!getSet_category.getcategory().get(position).getimage().equals("")) {
                    imageLoader.displayImage(ApiClient.url_common + "/" + getSet_category.getcategory().get(position).getimage(), im, options,
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            cat_name.setText(getSet_category.getcategory().get(position).getCat_name());
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

    public class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //    Arrays.fill(sliderr, null);
        }

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if (image != null) {
                // Toast.makeText(getApplicationContext(),"image"+image.toString(),Toast.LENGTH_SHORT).show();
                image_slider.setImageBitmap(image);


            } else {
                //Toast.makeText(MainActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBackPressed() {

        super.onBackPressed();
        handler.removeCallbacks(runnable);
        handler.removeMessages(0);
        handler.postDelayed(null, 0);

        handler.removeCallbacksAndMessages(this);
        //     new LoadImage().cancel(true);
        //Arrays.fill(sliderr, null);
    }

    protected void onPause() {
        handler.removeCallbacks(runnable);
        handler.removeMessages(0);

        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }
}

