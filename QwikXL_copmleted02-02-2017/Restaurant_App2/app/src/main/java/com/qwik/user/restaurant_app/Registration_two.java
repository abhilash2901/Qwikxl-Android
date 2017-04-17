package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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
import com.qwik.user.pushnotification.GCMClientManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Registration_two extends Activity {

    Animation shake = null;
    ProgressDialog pgBar = null;
    GetSet_country getset_country = null;
    String b_firstname = null, regId=null,b_lastname = null, b_email = null, b_mobile = null, b_password = null, key = null, b_country_code = null;
    ListViewAdapter1 listAdapter1 = null;
    Spinner s_country = null;
    Bundle bundle = null;
    String country_name = null;
    Button register = null;
    EditText card_no = null, cvv = null, zip = null, month = null, year = null;
    JSONObject Parent = new JSONObject();
    JSONArray add_direct = null;
    JSONObject jObject = null;
    ImageButton back = null;
    TextView add_payment_later = null;
    CryptLib encrypt_clas=null;
    String PROJECT_NUMBER="707970948346";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_two);
        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                regId = registrationId;
                Log.e("Registration id", registrationId);

                Log.e("yhbghuyefgwbu", regId);
                //send this registrationId to your server

            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
        s_country = (Spinner) findViewById(R.id.s_country);
        register = (Button) findViewById(R.id.register);
        card_no = (EditText) findViewById(R.id.card_no);
        month = (EditText) findViewById(R.id.month);
        cvv = (EditText) findViewById(R.id.cvv);
        year = (EditText) findViewById(R.id.year);
        zip = (EditText) findViewById(R.id.zip);
        add_payment_later = (TextView) findViewById(R.id.add_payment_later);
        bundle = new Bundle();

        try {
            encrypt_clas = new CryptLib();
            Intent i = getIntent();
            bundle = i.getExtras();
            b_firstname = bundle.getString("f_name");
            b_lastname = bundle.getString("l_name");
            b_email = bundle.getString("email");
            b_mobile = bundle.getString("mobile");
            b_country_code = bundle.getString("country_code");
            b_password = bundle.getString("password");
            back = (ImageButton) findViewById(R.id.back);
            getting_country_name();
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (card_no.getText().toString().equals("")) {
                        card_no.setError("Enter card no");
                        card_no.startAnimation(shake);
                    } else if (month.getText().toString().equals("")) {
                        month.setError("Enter Month");
                        month.setAnimation(shake);
                    } else if (year.getText().toString().equals("")) {
                        year.setError("Enter Year");
                        year.setAnimation(shake);
                    } else if (cvv.getText().toString().equals("")) {
                        cvv.setError("Enter cvv");
                        cvv.startAnimation(shake);
                    } else if (zip.getText().toString().equals("")) {
                        zip.setError("Enter Zip");
                        zip.startAnimation(shake);
                    } else if ((month.getText().toString().length() < 2) || (Integer.parseInt(month.getText().toString()) > 12)) {
                        month.setError("Enter valid month");
                        month.startAnimation(shake);
                    } else if ((year.getText().toString().length() < 4) || (Integer.parseInt(year.getText().toString()) < 2016)) {
                        year.setError("Enter valid year");
                        year.startAnimation(shake);
                    } else {
                        String cardno = card_no.getText().toString();
                        String c_month = month.getText().toString();
                        String c_year = year.getText().toString();
                        String c_cvv = cvv.getText().toString();
                        String c_zip = zip.getText().toString();
                        collecting_data(cardno, c_month, c_year, c_cvv, country_name, c_zip);
                    }
                }
            });
            add_payment_later.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        collecting_data("", "", "", "", "", "");
                    } catch (Exception e) {
                        e.printStackTrace();
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
    private void collecting_data(String e_cardno, String e_month, String e_year, String e_cvv, String e_country, String e_zip) {

        add_direct = new JSONArray();
        try {
            Parent.put("json", jObject);
            Parent.put("firstname", b_firstname);
            Parent.put("lastname", b_lastname);
            Parent.put("email", b_email);
            Parent.put("country_code", b_country_code);
            Parent.put("mobile", b_mobile);
            Parent.put("password", b_password);
//            Parent.put("card_no",encrypt_clas.encrypt(e_cardno));
//            Parent.put("month",encrypt_clas.encrypt(e_month));
//            Parent.put("year",encrypt_clas.encrypt(e_year));
//            Parent.put("cvv",encrypt_clas.encrypt(e_cvv));
            Parent.put("address", "test_address");
            Parent.put("country", e_country);
            Parent.put("zipcode", e_zip);
            Parent.put("regId",regId);
            Log.e("Parent", Parent.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            new registration().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getting_country_name() {
        pgBar = Progress_class.createProgressDialog(Registration_two.this);
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
                        s_country.setAdapter(listAdapter1);
                        s_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                cat_name.setText(getset_country.getcountrylist().get(position).getCountry_name());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return itemView;
        }
    }

    class registration extends AsyncTask<Void, Void, String> {
        private ProgressDialog Dialog;

        protected void onPreExecute() {
            Dialog = Progress_class.createProgressDialog(Registration_two.this);
            Dialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            String url = "http://202.88.237.237:8080/qwikxl_admin_console/public/customerRegister";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("User_details", Parent.toString()));
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);
                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    Log.e("name value pair", nameValuePairs.toString());
                    HttpResponse response = client.execute(post);
                    String st = EntityUtils.toString(response.getEntity());
                    jObject = new JSONObject(st);
                    key = jObject.getString("Status");

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error mesage", e.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return key;
        }

        @Override
        protected void onPostExecute(String key) {
            Dialog.dismiss();
            try {
                if (key.equalsIgnoreCase("Registration Success")) {
                    Toast.makeText(getApplicationContext(), "" + key, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                } else if (key.equalsIgnoreCase("Email ID Already Exist")) {
                    Toast.makeText(getApplicationContext(), key , Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else {
                    Toast.makeText(getApplicationContext(), key + "due to Network Problem", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
