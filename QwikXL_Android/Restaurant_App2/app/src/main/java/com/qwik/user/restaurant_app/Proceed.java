package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

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


public class Proceed extends Activity {

    Button pay_now = null;
    EditText card_no = null, cvv = null, holder_name = null;
    TextView total_amount = null;
    EditText e_month = null, e_year = null;
    ProgressDialog pgBar = null;
    ArrayList<ArrayList<Object>> data = null;
    ArrayList<Object> row = null;
    AABDatabaseManager db = null;
    JSONObject Parent = new JSONObject();
    JSONArray add_direct = null;
    JSONObject jObject = null;
    Bundle bb = null, b = null;
    LinearLayout card_layout, date_layout, name_layout, cvv_layout;
    ProgressDialog pgBars = null;
    static SharedPreferences pref = null;
    Dialog dialog = null;
    String username = null, amount = null, grandtotal = null, reward = null, stripe_token = null, key = null, orderid = null, transaction_id = null, pick_type = null, cus_mobile = null, stripe_customer_id = "0";
    Animation shake = null;
    Bundle bundle = null;
    int stripe_flag, new_card_flag = 0;
    // static String Strip_API = "pk_test_aGSHdcst67cvtrfuw2ygwC1z";
    // static String Strip_API = "sk_test_OdihBeOQOiWLAtT5AfxaedN2";

    static String Strip_API = "pk_test_KzLMHXxaf9PeBXscOW4BY6GU";

    CryptLib encrypt_clas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed);

        e_month = (EditText) findViewById(R.id.month);
        e_year = (EditText) findViewById(R.id.year);
        card_no = (EditText) findViewById(R.id.card_no);
        cvv = (EditText) findViewById(R.id.cvv);
        holder_name = (EditText) findViewById(R.id.holder_name);
        pay_now = (Button) findViewById(R.id.pay_now);
        total_amount = (TextView) findViewById(R.id.total_amount);
        card_layout = (LinearLayout) findViewById(R.id.card_layout);
        date_layout = (LinearLayout) findViewById(R.id.date_layout);
        name_layout = (LinearLayout) findViewById(R.id.name_layout);
        cvv_layout = (LinearLayout) findViewById(R.id.cvv_layout);
        db = new AABDatabaseManager(this);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        bb = new Bundle();
        b = new Bundle();
        bundle = new Bundle();
        try {
            encrypt_clas = new CryptLib();
            pref = getSharedPreferences("Login", MODE_PRIVATE);
            username = pref.getString("username", "");
            cus_mobile = pref.getString("mobile", "");
            pick_type = pref.getString("pickup_type", "");
            Intent i = getIntent();
            bb = i.getExtras();
            amount = bb.getString("sum");
            grandtotal = bb.getString("Grandtotal");
            reward = bb.getString("rewardpoint");
            total_amount.setText(grandtotal);
            Log.e("mobile no", cus_mobile);
            paymentcardoption();

        } catch (Exception e) {
            e.printStackTrace();
        }
        pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String card_number = card_no.getText().toString();
                String cvv_no = cvv.getText().toString();
                String card_holdername = holder_name.getText().toString();
                String month = e_month.getText().toString();
                String year = e_year.getText().toString();
                if (new_card_flag == 1) {
                    if (card_number.equals("")) {
                        card_no.setError("Enter card No");
                        card_no.startAnimation(shake);
                    } else if (cvv_no.equals("")) {
                        cvv.startAnimation(shake);
                        cvv.setError("Enter CVV");
                    } else if (e_month.equals("")) {
                        e_month.startAnimation(shake);
                        e_month.setError("Enter Month");

                    } else if (e_year.equals("")) {
                        e_year.startAnimation(shake);
                        e_year.setError("Enter Year");

                    } else if (card_holdername.equals("")) {
                        holder_name.startAnimation(shake);
                        holder_name.setError("Enter Name");
                    } else if ((e_month.getText().toString().length() < 2) || (Integer.parseInt(e_month.getText().toString()) > 12)) {
                        e_month.setError("Enter valid month");
                        e_month.startAnimation(shake);
                    } else if ((e_year.getText().toString().length() < 4) || (Integer.parseInt(e_year.getText().toString()) < 2016)) {
                        e_year.setError("Enter valid year");
                        e_year.startAnimation(shake);
                    } else {
                        newcard(card_number, month, year, cvv_no);
                    }
                }

                        if (stripe_flag == 1) {
                            //Toast.makeText(getApplicationContext(),"Stripe token",Toast.LENGTH_SHORT).show();
                            fetching_data("0");
                        }
                }


            }

            );
        }

    private void newcard(final String cardnumber, final String mnth, final String yer, final String cvvno) {
        //   dialog.dismiss();
        stripe_token(cardnumber, mnth, yer, cvvno);
        //Toast.makeText(getApplicationContext(), "Processing", Toast.LENGTH_LONG).show();
    }

    private void paymentcardoption() {
        dialog = new Dialog(this, R.style.CustomDialogTheme);
        dialog.setContentView(R.layout.paymentoption_layout);

        TextView tv = (TextView) dialog.findViewById(R.id.option_hint);
        tv.setText("Select card option");
        Button exist_card = (Button) dialog.findViewById(R.id.existingcard);
        exist_card.setText("Existing Card");
        Button new_card = (Button) dialog.findViewById(R.id.newcard);
        new_card.setText("New Card");
        exist_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getting_creditcard(username);
                dialog.dismiss();

            }
        });
        new_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_card_flag = 1;
                dialog.dismiss();

            }
        });


        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //do whatever you want the back key to do
                bundle.putString("Grandtotal", grandtotal);
                bundle.putString("rewardpoint", reward);
                bundle.putString("sum", amount);
                Intent i = new Intent(getApplicationContext(), Payment_option.class);
                i.putExtras(bundle);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    public void stripe_token(String cardno, String month, String year, String cvv_no) {
        try {
            pgBar = Progress_class.createProgressDialog(Proceed.this);
            pgBar.show();

            Card card = new Card(cardno, Integer.parseInt(month), Integer.parseInt(year), cvv_no);
            if (!card.validateNumber()) {
                if (pgBar.isShowing()) {
                    pgBar.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Invalid Card Number", Toast.LENGTH_SHORT).show();
            } else if (!card.validateCVC()) {
                if (pgBar.isShowing()) {
                    pgBar.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Invalid Card CVV", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("inside", "hai");
                Stripe stripe = new Stripe(Strip_API);
                stripe.createToken(
                        card,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                if (pgBar.isShowing()) {
                                    pgBar.dismiss();
                                }
                                stripe_token = token.getId().toString();
                                Log.e("stripe_token", stripe_token);
                                fetching_data(stripe_token);

                            }

                            public void onError(Exception error) {
                                if (pgBar.isShowing()) {
                                    pgBar.dismiss();
                                }
                                Toast.makeText(getApplicationContext(), "Card Is Invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetching_data(String stripe_token) {
        add_direct = new JSONArray();
        data = db.getAllRowsAsArrays();
        //  Log.e("jsonnarrayy1111", add_direct.toString());
        for (int position = 0; position < data.size(); position++) {
            try {
                row = data.get(position);
                Log.e("Testing", "Testing");
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("item_id", row.get(1).toString());
                jsonObj.put("quantity", row.get(3).toString());
                jsonObj.put("price", row.get(6).toString());
                add_direct.put(jsonObj);
                Parent.put("cart", add_direct);
                Parent.put("username", username);
                Parent.put("pick_type", pick_type);
                Parent.put("phone", cus_mobile);
                Parent.put("total", amount);
                Parent.put("grand_total", grandtotal);
                Parent.put("reward_point", reward);
                Parent.put("Stripe_token", stripe_token);
                Parent.put("stripe_customer_id", stripe_customer_id);
                // Parent.put("Stripe_token",  "");
                Log.e("Parent", Parent.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Placing_order(Parent.toString());

    }

    private void Placing_order(String parent_string) {
        Log.e("Placing_order", "Placing_order");
        pgBars = Progress_class.createProgressDialog(Proceed.this);
        pgBars.show();
        try {
            ApiClient.getPosApp().Placing_order(parent_string, new Callback<GetSet_order>() {
                @Override
                public void success(GetSet_order getSet_order, Response response) {

                    System.out.println("sucess Login  " + response);
                    if (pgBars.isShowing()) {
                        pgBars.dismiss();
                    }
                    try {
                        String status = getSet_order.getstatus();
                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                        if (status.equalsIgnoreCase("Ordering Successful")) {
                            String order_id = getSet_order.getOrderid();
                            String unique_id = getSet_order.getunique_id();
                            String transaction_idd = getSet_order.gettransaction_id();
                            b.putString("orderid", order_id);
                            b.putString("unique_id", unique_id);
                            b.putString("transaction_id", transaction_idd);
                            db.delete();
                            Intent i = new Intent(getApplicationContext(), Order_id.class);
                            i.putExtras(b);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                        } else {
                            Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                        }


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

    private void getting_creditcard(String getStatus) {
        pgBars = Progress_class.createProgressDialog(Proceed.this);
        pgBars.show();
        try {
            ApiClient.getPosApp().getmyaccount2(getStatus, new Callback<GetSet_myaccount2>() {
                @Override
                public void success(GetSet_myaccount2 getSet_myaccount, Response response) {

                    // System.out.println("sucess Login  " + response);
                    if (pgBars.isShowing()) {
                        pgBars.dismiss();
                    }
                    try {
                        if (getSet_myaccount.getStatus().equalsIgnoreCase("Success")) {
                            stripe_flag = 1;
                            card_no.setText("XXXXXXXXXXXX" + getSet_myaccount.getdata().get(0).getcard_last4());
                            date_layout.setVisibility(View.GONE);
                            cvv_layout.setVisibility(View.GONE);
                            name_layout.setVisibility(View.GONE);
                            stripe_customer_id = getSet_myaccount.getdata().get(0).getstripe_customer_id();
//                        card_no.setEnabled(false);
//                         cvv.setText(encrypt_clas.decrypt(getSet_myaccount.getaccount().get(0).getcvv()));
//                        cvv.setEnabled(false);
//                        holder_name.setText(getSet_myaccount.getaccount().get(0).getFirstnamename());
//                        holder_name.setEnabled(false);
//                        e_month.setText(encrypt_clas.decrypt(getSet_myaccount.getaccount().get(0).getmonth()));
//                        e_month.setEnabled(false);
//                        e_year.setText(encrypt_clas.decrypt(getSet_myaccount.getaccount().get(0).getyear()));
//                        e_year.setEnabled(false);
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        } else if (getSet_myaccount.getStatus().equalsIgnoreCase("Failed")) {
                            String msg = getSet_myaccount.getMessage();
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
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

//    class add_cart extends AsyncTask<Void, Void, String> {
//        private ProgressDialog Dialog;
//
//        protected void onPreExecute() {
//            Dialog = Progress_class.createProgressDialog(Proceed.this);
//            Dialog.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            String url = "http://202.88.237.237:8080/hotel/hotel/process/orderWebService.php";
//            Log.e("Inside do in background", "before url call");
//
//            try {
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("order_detail", Parent.toString()));
//                try {
//
//
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e("error mesage", e.toString());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return key;
//        }
//
//        @Override
//        protected void onPostExecute(String key) {
//            Dialog.dismiss();
//            try {
//                if (key.equalsIgnoreCase("Ordering Successful")) {
//                    Toast.makeText(getApplicationContext(), "" + key, Toast.LENGTH_SHORT).show();
//                    Log.e("orderid inside true ", orderid);
//                    b.putString("orderid", orderid);
//                    b.putString("transaction_id", transaction_id);
//                    db.delete();
//                    Intent i = new Intent(getApplicationContext(), Order_id.class);
//                    i.putExtras(b);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "" + key, Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e("error catch", e.toString());
//            }
//        }
//    }

    public void onBackPressed() {
        bundle.putString("Grandtotal", grandtotal);
        bundle.putString("rewardpoint", reward);
        bundle.putString("sum", amount);
        Intent i = new Intent(getApplicationContext(), Payment_option.class);
        i.putExtras(bundle);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}

