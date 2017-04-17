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
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Edit_creditcard extends Activity {
    Animation shake = null;
    GetSet_country getset_country = null;
    static SharedPreferences pref = null;
    ProgressDialog pgBar = null, pgBars = null, pgBar1 = null;
    EditText card_no = null, zip = null, u_cvv = null, u_month = null, u_year = null;
    Button update_card = null;
    Spinner countryname = null;
    String getStatus = null, countryname_value = "India", country_name = null, country_no = null,card_token;
    ImageButton back = null;
    ListViewAdapter1 listAdapter1 = null;
    CryptLib encrypt_clas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_creditcard);

        card_no = (EditText) findViewById(R.id.card_no);
        zip = (EditText) findViewById(R.id.zip);
        u_cvv = (EditText) findViewById(R.id.cvv);
        u_month = (EditText) findViewById(R.id.month);
        u_year = (EditText) findViewById(R.id.year);
        countryname = (Spinner) findViewById(R.id.s_country);
        update_card = (Button) findViewById(R.id.update_card);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        back = (ImageButton) findViewById(R.id.back);

        try {

            getStatus = pref.getString("username", "");
            //getting_creditcard(getStatus);
            getting_country_name();
            encrypt_clas = new CryptLib();
            update_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updating_card();
                }
            });
            countryname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    country_name = getset_country.getcountrylist().get(position).getCountry_name();
                    Log.e("country_name", country_name);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(getApplicationContext(), My_account.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updating_card() {

        if (card_no.getText().toString().equals("")) {
            card_no.setError("Enter card no");
            card_no.startAnimation(shake);
        } else if (u_month.getText().toString().equals("")) {
            u_month.setError("Enter Month");
            u_month.setAnimation(shake);
        } else if (u_year.getText().toString().equals("")) {
            u_year.setError("Enter Year");
            u_year.setAnimation(shake);
        } else if (u_cvv.getText().toString().equals("")) {
            u_cvv.setError("Enter cvv");
            u_cvv.startAnimation(shake);
        } else if (zip.getText().toString().equals("")) {
            zip.setError("Enter Zip");
            zip.startAnimation(shake);
        } else if ((u_month.getText().toString().length() < 2) || (Integer.parseInt(u_month.getText().toString()) > 12)) {
            u_month.setError("Enter valid month");
            u_month.startAnimation(shake);
        } else if ((u_year.getText().toString().length() < 4) || (Integer.parseInt(u_year.getText().toString()) < 2016)) {
            u_year.setError("Enter valid year");
            u_year.startAnimation(shake);
        } else {
            Making_token(card_no.getText().toString(),u_month.getText().toString(),u_year.getText().toString(),u_cvv.getText().toString());

            //collecting_carddata();
        }
    }

    private void Making_token(String cardno,String month,String year,String cvv_no) {
        try{
            Card card = new Card(cardno, Integer.parseInt(month), Integer.parseInt(year), cvv_no);
            Stripe stripe = new Stripe(Proceed.Strip_API);
            Toast.makeText(getApplicationContext(),"Please wait...",Toast.LENGTH_LONG).show();
            stripe.createToken(
                    card,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            card_token = token.getId().toString();
                            Log.e("card_token : ",card_token);
                            collecting_carddata(card_token);

                        }

                        public void onError(Exception error) {
                            Toast.makeText(getApplicationContext(), "Card Is Invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void collecting_carddata(String card_token) {
        try {
            pgBar = Progress_class.createProgressDialog(Edit_creditcard.this);
            pgBar.show();

            ApiClient.getPosApp().getupdatecard(card_token,getStatus, country_name, zip.getText().toString(), new Callback<GetSet_login>() {
                @Override
                public void success(GetSet_login getSet_login, Response response) {
                    System.out.println("Sucess" + response.getUrl());
                    if (pgBar.isShowing()) {
                        pgBar.dismiss();
                    }
                    String res = getSet_login.getstatus();
                    if (res.equalsIgnoreCase("Your Card has been saved Successfully")) {
                        try {
                            //   Log.e("encryptt card",encrypt_clas.encrypt(card_no.getText().toString())+encrypt_clas.encrypt(u_month.getText().toString())+encrypt_clas.encrypt(u_year.getText().toString())+encrypt_clas.encrypt(u_cvv.getText().toString()));
                            Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), Home.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else  {

                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                }
            });
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    private void getting_creditcard(String getStatus) {
        pgBars = Progress_class.createProgressDialog(Edit_creditcard.this);
        pgBars.show();
        try {
            ApiClient.getPosApp().getmyaccount2(getStatus, new Callback<GetSet_myaccount2>() {
                @Override
                public void success(GetSet_myaccount2 getSet_myaccount, Response response) {
                    System.out.println("sucess Login  " + response);
                    if (pgBars.isShowing()) {
                        pgBars.dismiss();
                    }
                    try {
                        country_no = getSet_myaccount.getcountrynameId();
                        card_no.setText(encrypt_clas.decrypt(getSet_myaccount.getaccount().get(0).getcreditcardno()));
                        u_cvv.setText(encrypt_clas.decrypt(getSet_myaccount.getaccount().get(0).getcvv()));
                        zip.setText(getSet_myaccount.getaccount().get(0).getzipcode());
                        u_month.setText(encrypt_clas.decrypt(getSet_myaccount.getaccount().get(0).getmonth()));
                        u_year.setText(encrypt_clas.decrypt(getSet_myaccount.getaccount().get(0).getyear()));
                    } catch (Exception e) {
                        e.printStackTrace();
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

    private void getting_country_name() {
        pgBar1 = Progress_class.createProgressDialog(Edit_creditcard.this);
        pgBar1.show();

        ApiClient.getPosApp().getcountry(new Callback<GetSet_country>() {
            public void success(GetSet_country cartModel, Response response) {
                System.out.print("*******Sucess download data********" + response.getUrl());
                if (pgBar1.isShowing()) {
                    pgBar1.dismiss();
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
                        countryname.setAdapter(listAdapter1);
                        countryname.setSelection(Integer.parseInt(country_no) - 1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No Data Detected", Toast.LENGTH_SHORT).show();

                    if (pgBar1.isShowing()) {
                        pgBar1.dismiss();
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
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), My_account.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
