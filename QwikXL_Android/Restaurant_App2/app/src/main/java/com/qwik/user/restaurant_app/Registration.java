package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Registration extends Activity {

    Data_values data_values;
    Animation shake = null;
    ProgressDialog pgBar = null;
    GetSet_country getset_country = null;
    Spinner sp_country = null;
    ListViewAdapter1 listAdapter1 = null;
    Button next = null;
    EditText f_name = null, l_name = null, email = null, mobile = null, password_one = null, password_two = null, address = null;
    Bundle bundle = null;
    String country_code = null, country_name;
    ImageButton back = null;
    JSONObject Parent = new JSONObject();
    GetSet_login getset_login;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_one);

        sp_country = (Spinner) findViewById(R.id.sp_country);
        next = (Button) findViewById(R.id.next);
        f_name = (EditText) findViewById(R.id.f_name);
        l_name = (EditText) findViewById(R.id.l_name);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        address = (EditText) findViewById(R.id.address);
        password_one = (EditText) findViewById(R.id.password_one);
        password_two = (EditText) findViewById(R.id.password_two);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        data_values = new Data_values();
        bundle = new Bundle();
        back = (ImageButton) findViewById(R.id.back);

        try {
            getting_country_code();
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (f_name.getText().toString().equals("")) {
                        f_name.setError("Enter Name");
                        f_name.startAnimation(shake);
                    } else if (l_name.getText().toString().equals("")) {
                        l_name.setError("Enter Name");
                        l_name.startAnimation(shake);
                    } else if (email.getText().toString().equals("")) {
                        email.setError("Enter Email");
                        email.startAnimation(shake);
                    } else if (!email.getText().toString().matches(data_values.emailPattern)) {
                        email.setError("Enter valid email");
                        email.startAnimation(shake);
                    } else if (mobile.getText().toString().equals("")) {
                        mobile.setError("Enter Mobile No");
                        mobile.startAnimation(shake);

                    } else if (address.getText().toString().equals("")) {
                        address.setError("Enter Address");
                        address.startAnimation(shake);

                    } else if (password_one.getText().toString().equals("")) {
                        password_one.setError("Enter Password");
                        password_one.startAnimation(shake);
                    }
                    else if(!password_one.getText().toString().matches(data_values.passmatcher)){
                        Toast.makeText(getApplicationContext(),"Password: must be at least 6 character; minimum 1 capital letters, 1 lower case letters, 1 numbers, 1 special character",Toast.LENGTH_LONG).show();
                        password_one.startAnimation(shake);
                    }
                    else if (password_one.getText().length() < 6) {
                        password_one.setError("Password: must be at least 6 character");
                        password_one.startAnimation(shake);
                    } else if (password_two.getText().toString().equals("")) {
                        password_two.setError(" Re-enter Password");
                        password_two.startAnimation(shake);
                    }
                    else if(!password_two.getText().toString().matches(data_values.passmatcher)){
                        Toast.makeText(getApplicationContext(),"Password: must be at least 6 character; minimum 1 capital letters, 1 lower case letters, 1 numbers, 1 special character",Toast.LENGTH_LONG).show();
                        password_two.startAnimation(shake);
                    } else if (!password_one.getText().toString().equals(password_two.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Password Mismatching", Toast.LENGTH_SHORT).show();
                    } else {
                        data_to_next();
                    }
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void data_to_next() {

//        bundle.putString("f_name", f_name.getText().toString());
//        bundle.putString("l_name", l_name.getText().toString());
//        bundle.putString("email", email.getText().toString());
//        bundle.putString("mobile", mobile.getText().toString());
//        bundle.putString("country_code", country_code);
//        bundle.putString("password", password_one.getText().toString());
//        Intent i = new Intent(getApplicationContext(), Registration_two.class);
//        i.putExtras(bundle);
//        startActivity(i);
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        try {
            // Parent.put("json", jObject);
            //String encrypted_passsword=data_values.md5(password_one.getText().toString());
          //  Log.e("encrypted_passsword: ",encrypted_passsword);
            Parent.put("firstname", f_name.getText().toString());
            Parent.put("lastname", l_name.getText().toString());
            Parent.put("email", email.getText().toString());
            Parent.put("country_code", country_code);
            Parent.put("mobile", mobile.getText().toString());
            Parent.put("password", Data_values.md5(password_one.getText().toString()));
            Parent.put("country", country_name);
            Parent.put("address", address.getText().toString());

            Registering(Parent);

            Log.e("Parent", Parent.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Registering(JSONObject parent) {
        Log.e("parent_json", parent.toString());
        pgBar = Progress_class.createProgressDialog(Registration.this);
        pgBar.show();
        ApiClient.getPosApp().getregister(parent.toString(), new Callback<GetSet_login>() {
            public void success(GetSet_login cartModel, Response response) {
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

            private void consumeApiData1(final GetSet_login getSet_login) {
                getset_login = getSet_login;
                if (getset_login != null) {
                    try {
                        String status = getset_login.getstatus();
                        if (status.equalsIgnoreCase("Registration Success")) {
                            Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), Home.class);
                            i.putExtras(bundle);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                        } else {
                            Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                        }

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

    private void getting_country_code() {
        pgBar = Progress_class.createProgressDialog(Registration.this);
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
                        sp_country.setAdapter(listAdapter1);
                        sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                country_code = getSet_country.getcountrylist().get(position).getcountry_code();
                                country_name = getSet_country.getcountrylist().get(position).getCountry_name();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

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
            TextView country_name = (TextView) itemView.findViewById(R.id.country_name);
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
                country_name.setText(getset_country.getcountrylist().get(position).getCountry_name());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return itemView;
        }
    }
}
