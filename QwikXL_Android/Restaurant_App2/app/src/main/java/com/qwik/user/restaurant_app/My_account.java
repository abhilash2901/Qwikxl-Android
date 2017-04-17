package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class My_account extends Activity {

    static SharedPreferences pref = null;
    ImageView profile_pic = null;
    ImageButton settings = null, back = null;
    TextView address1 = null, address2 = null, first_name = null, second_name = null, email_id = null, mobile = null;
    Spinner sp_countrycode = null;
    Bundle bundle;
    String user_name = null, m_firstname = null, m_lastname = null, m_email = null, m_mobile = null, m_countrycode = null, getStatus, password = null, country_code = null, image = null;
    ProgressDialog pgBar = null, pgBars = null;
    GetSet_country getset_country = null;
    ImageLoader imageLoader = null;
    DisplayImageOptions options = null;
    ListViewAdapter1 listAdapter1 = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_edit);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        first_name = (TextView) findViewById(R.id.first_name);
        second_name = (TextView) findViewById(R.id.second_name);
        email_id = (TextView) findViewById(R.id.email_id);
        mobile = (TextView) findViewById(R.id.mobile);
        address1 = (TextView) findViewById(R.id.adress1);
        back = (ImageButton) findViewById(R.id.back);
        sp_countrycode = (Spinner) findViewById(R.id.sp_countycode);
        settings = (ImageButton) findViewById(R.id.settings);
        bundle = new Bundle();
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        try {
            getStatus = pref.getString("username", "");
            getting_data(getStatus);
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(My_account.this, settings);
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().toString().equalsIgnoreCase("Edit Profile")) {
                                Intent i = new Intent(getApplicationContext(), Edit_profile.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            } else if (item.getTitle().toString().equalsIgnoreCase("Edit card details")) {
                                Intent i = new Intent(getApplicationContext(), Edit_creditcard.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            } else if (item.getTitle().toString().equalsIgnoreCase("Change Password")) {
                                bundle.putString("password", password);
                                Intent i = new Intent(getApplicationContext(), Change_password.class);
                                i.putExtras(bundle);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            } else if (item.getTitle().toString().equalsIgnoreCase("Logout")) {
                                Data_values.logout();
                                Toast.makeText(getApplicationContext(), "Successfully Logout", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), Home.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            }

                            return true;
                        }
                    });

                    popup.show(); //showing popup
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getting_data(String user_name) {
        pgBars = Progress_class.createProgressDialog(My_account.this);
        pgBars.setTitle("data");
        pgBars.show();
        try {
            ApiClient.getPosApp().getmyaccount(user_name, new Callback<GetSet_myaccount2>() {
                @Override
                public void success(GetSet_myaccount2 getSet_myaccount, Response response) {

                    Log.e("sucess Login  ", response.toString());
                    if (pgBars.isShowing()) {
                        pgBars.dismiss();
                    }

                    m_firstname = getSet_myaccount.getaccount().get(0).getFirstnamename();
                    m_lastname = getSet_myaccount.getaccount().get(0).getlastname();
                    m_email = getSet_myaccount.getaccount().get(0).getemail();
                    m_countrycode = getSet_myaccount.getcountrycodeId();
                    m_mobile = getSet_myaccount.getaccount().get(0).getmobile();
                    password = getSet_myaccount.getaccount().get(0).getpassword();
                    image = ApiClient.url_common+"/"+getSet_myaccount.getaccount().get(0).getimage();
                   // Log.e("image_url",image);

                    address1.setText(getSet_myaccount.getaccount().get(0).getaddress());
                    first_name.setText(m_firstname);
                    second_name.setText(m_lastname);
                    email_id.setText(m_email);
                    mobile.setText(m_mobile);
                    //Toast.makeText(getApplicationContext(),m_countrycode,Toast.LENGTH_LONG).show();
                   // Log.e("country_code",m_countrycode);
                    getting_country_code();
                    if(getSet_myaccount.getaccount().get(0).getimage().equals(""))
                    {
                        profile_pic.setBackgroundResource(R.drawable.account_pic);
                    }
                    else
                    {
                        seting_image();
                    }

                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    if (retrofitError.getResponse() != null) {
                        Log.e("Retrofit error", retrofitError.getCause().toString());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void seting_image() {
        try {
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration
                    .createDefault(getApplicationContext()));
            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .showImageOnLoading(R.drawable.ic_launcher).cacheOnDisk(true)
                    .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

            imageLoader.displayImage(
                    image, profile_pic, options,
                    new SimpleImageLoadingListener() {

                        Animation rotation;

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {

                            // profile_pic.setScaleType(ImageView.ScaleType.FIT_XY);
                            profile_pic.setImageBitmap(getRoundedShape(loadedImage));
                        }
                    }, new ImageLoadingProgressListener() {

                        public void onProgressUpdate(String imageUri, View view,
                                                     int current, int total) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getting_country_code() {
        pgBar = Progress_class.createProgressDialog(My_account.this);
        pgBar.setTitle("coutry");
        pgBar.show();
        ApiClient.getPosApp().getcountry(new Callback<GetSet_country>() {

            public void success(GetSet_country cartModel, Response response) {
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

            private void consumeApiData1(final GetSet_country getSet_country) {
                getset_country = getSet_country;
                if (getset_country != null) {
                    try {
                        listAdapter1 = new ListViewAdapter1(getApplicationContext(), getSet_country);
                        sp_countrycode.setAdapter(listAdapter1);
                        sp_countrycode.setSelection(Integer.parseInt(m_countrycode)-1);

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

        public ListViewAdapter1(Context context, GetSet_country getSet_count) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getset_country = getSet_count;
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
            return getset_country.getcountrylist().size();
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
            View itemView = inflater.inflate(R.layout.list_row_country, parent, false);
            TextView cat_name = (TextView) itemView.findViewById(R.id.cat_name);
            im = (ImageView) itemView.findViewById(R.id.im);
            try {

                imageLoader.displayImage(
                        getset_country.getcountrylist().get(position).getflag(), im, options,
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
                cat_name.setText(getset_country.getcountrylist().get(position).getcountry_code());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return itemView;
        }
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        Bitmap bitmap = scaleBitmapImage;
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        return circleBitmap;
    }

    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
