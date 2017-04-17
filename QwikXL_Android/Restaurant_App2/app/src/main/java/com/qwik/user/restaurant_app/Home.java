package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;

//import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.view.ContextThemeWrapper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.erz.timepicker_library.TimePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Home extends Activity implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener{

    protected LocationListener locationListener;
    protected Context context;
    protected boolean gps_enabled, network_enabled;
    protected LocationManager locationmanager;
    static String latitude = "0.0", longitude = "0.0", c_lattitude = null, c_longitude = null;
    static SharedPreferences pref;
    static String getStatus;
    ImageButton gps_serach = null;
    String city_name = null, latlon = null, true1 = "true";
    GPSTracker gps = null;
    Button set_picktime = null;
    ProgressDialog pgBar = null;
    Bundle b = null;
    Location l = null;
    Context ctx = null;
    Button my_account = null, serach = null, order_status = null, reward = null, all_set = null;
    EditText address = null;
    AlertDialog ab = null;
    Animation shake = null;
    Button grocery_onway = null;
    int select = 1,timer_flag=0;
    TimePicker timePicker = null;
    AlertDialog dialog = null;
    String pickup_type = "ontheway";
    static String pick_time = null;
    Calendar now;
    Dialog dialog1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_page);

        serach = (Button) findViewById(R.id.search);
        address = (EditText) findViewById(R.id.address);
        set_picktime = (Button) findViewById(R.id.set_picktime);
        grocery_onway = (Button) findViewById(R.id.sp_store);
        all_set = (Button) findViewById(R.id.grocery_btn);
        gps_serach = (ImageButton) findViewById(R.id.gps_serach);
        order_status = (Button) findViewById(R.id.order_status);
        my_account = (Button) findViewById(R.id.my_account);
        reward = (Button) findViewById(R.id.reward);
        b = new Bundle();
        gps = new GPSTracker(this);
        set_picktime.setFocusableInTouchMode(true);
        pgBar = new ProgressDialog(Home.this);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        getStatus = pref.getString("username", "");
        if (getStatus.equals("")) {
            my_account.setBackgroundResource(R.drawable.login);

        } else {
            my_account.setBackgroundResource(R.drawable.myaccnt);
        }
        try {

            if (CheckNetwork.isInternetAvailable(Home.this)) {

                getGps();
            }
            else {
                final Dialog dialog = new Dialog(this, R.style.CustomDialogTheme);
                dialog.setContentView(R.layout.net);
                Button wifi = (Button) dialog.findViewById(R.id.wifi);
                Button data = (Button) dialog.findViewById(R.id.Data);
                Button quit = (Button) dialog.findViewById(R.id.Quit);

                wifi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
                data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
                quit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.exit(0);
                    }
                });

                dialog.show();
            }

            serach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (address.getText().toString().equals("")) {
                            address.startAnimation(shake);
                            address.setError("Enter Address");
                            latitude = "0.0";
                            longitude = "0.0";
                        } else {
                            city_name = address.getText().toString().trim();
                            getting_latt_long(city_name);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                private void getting_latt_long(String city_name1) {
                    {
                        Log.e("city_name1",city_name1);
                        pgBar = Progress_class.createProgressDialog(Home.this);
                        pgBar.show();
                        try {
                            ApiClient.getPosApp().getlatlong(city_name1, new Callback<GetSet_lattitude>() {
                                @Override
                                public void success(GetSet_lattitude getSet_lattitude, Response response) {
                                    System.out.println("sucess Login  " + response);
                                    Log.e("lat and long ", response.toString());
                                    if (pgBar.isShowing()) {
                                        pgBar.dismiss();
                                    }
                                    latitude = getSet_lattitude.getlatt();
                                    longitude = getSet_lattitude.getlongi();
                                    Log.e("latitude+longitude",latitude+","+longitude);

                                    if (latitude.equals("")) {
                                        try {
                                            Toast.makeText(getApplicationContext(), "Enter valid City name", Toast.LENGTH_LONG).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "City Selected Successfully", Toast.LENGTH_LONG).show();
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
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

        try {

            set_picktime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    set_picktime.setFocusableInTouchMode(false);
//                    set_picktime.setFocusable(false);
//                    set_picktime.setBackgroundResource((R.drawable.time_green));
//                    grocery_onway.setBackgroundResource(R.drawable.run_white);
//                    select = 2;
                   // alertbox_timepicker();
                    timepicker();
                }
            });
            grocery_onway.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grocery_onway.isSelected();
                    grocery_onway.setBackgroundResource((R.drawable.run_green));
                    set_picktime.setBackgroundResource(R.drawable.time_white);
                    select = 1;
                }
            });
//            gps_serach.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (c_lattitude != null) {
//                        latlon = c_lattitude + "," + c_longitude;
//                        new JSONParse().execute();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Either Please Turn on the GPS or Enter your City name for Proper Result", Toast.LENGTH_LONG).show();
//                        address.setText(latitude + "," + longitude + "");
//                    }
//                }
//            });
            all_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (select == 1) {
                        if ((latitude.equals("")) || (longitude.equals("0.0"))) {
                           // b.putString("key", "1");
                            b.putString("lat", "");
                            b.putString("lon", "");
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("pickup_type", pickup_type);
                            editor.commit();
                            Intent i = new Intent(getApplicationContext(), Stores.class);
                            i.putExtras(b);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        } else {
                           // b.putString("key", "1");
                            b.putString("lat", latitude);
                            b.putString("lon", longitude);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("pickup_type", pickup_type);
                            editor.commit();
                            Intent i = new Intent(getApplicationContext(), Stores.class);
                            i.putExtras(b);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        }
                    } else if (select == 2) {
                        if ((latitude.equals("")) || (longitude.equals("0.0"))) {
                            //b.putString("key", "1");
                            b.putString("lat", "");
                            b.putString("lon", "");
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("pickup_type", pick_time);
                            editor.commit();
                            Intent i = new Intent(getApplicationContext(), Stores.class);
                            i.putExtras(b);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        } else {
                           // b.putString("key", "1");
                            b.putString("lat", latitude);
                            b.putString("lon", longitude);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("pickup_type", pick_time);
                            editor.commit();
                            Intent i = new Intent(getApplicationContext(), Stores.class);
                            i.putExtras(b);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
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

//    private void alertbox_timepicker() {
//        final Dialog dialog = new Dialog(this, R.style.CustomDialogTheme);
//        dialog.setContentView(R.layout.time_picker);
//        final Date date = new Date();
//        Button set_time = (Button) dialog.findViewById(R.id.set_time);
//        Button close_clock = (Button) dialog.findViewById(R.id.close_clock);
//        timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
//        timePicker.setTime(date);
//        //calendar.setTime(date);
//        final CharSequence current_time = DateFormat.format("hh:mm:ss", timePicker.getTime());
//        Log.e("current_time",current_time+"");
//
//        set_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                CharSequence character = DateFormat.format("E dd-MMM-yyyy hh:mm:ss a", timePicker.getTime());
//                pick_time = character.toString();
//                Toast.makeText(getApplicationContext(), character, Toast.LENGTH_SHORT).show();
//
//                CharSequence set_pickup_time = DateFormat.format("hh:mm:ss", timePicker.getTime());
//
//
//
//                 //int current_time=(int)date.getTime();
//
//               //  Log.e("set_pickup_time",set_pickup_time+"");
//
//
//
//                 //int set_time=(int)timePicker.getTime();
//
//
////                if(Integer.parseInt(current_time.toString())>Integer.parseInt(set_pickup_time.toString()) ){
////                    Toast.makeText(getApplicationContext(), "good", Toast.LENGTH_SHORT).show();
//////            it's after current
////                }else{
////                    Toast.makeText(getApplicationContext(), "bad", Toast.LENGTH_SHORT).show();
//////            it's before current'
////                }
//
//
////                String pattern = "hh:mm:ss";
////                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
////                try {
////                    Date curr_time = sdf.parse(current_time.toString());
////                    Date set_pictime = sdf.parse(set_pickup_time.toString());
////
////
////                    if(curr_time.compareTo(set_pictime)>0){
////                        Toast.makeText(getApplicationContext(), "good", Toast.LENGTH_SHORT).show();
////                    }
////                    else{
////                        Toast.makeText(getApplicationContext(), "bad", Toast.LENGTH_SHORT).show();
////                    }
////
////                    // Outputs -1 as date1 is before date2
////                    System.out.println(curr_time.compareTo(set_pictime));
////
////                } catch (ParseException e){
////                    // Exception handling goes here
////                }
//
//
//
////
////                Calendar datetime = Calendar.getInstance();
////                Calendar c = Calendar.getInstance();
////                datetime.set(Calendar.HOUR_OF_DAY, Calendar.HOUR);
////                datetime.set(Calendar.MINUTE, Calendar.MINUTE);
////                if(datetime.getTimeInMillis() > c.getTimeInMillis()){
////                    Toast.makeText(getApplicationContext(), "good", Toast.LENGTH_SHORT).show();
//////            it's after current
////                }else{
////                    Toast.makeText(getApplicationContext(), "bad", Toast.LENGTH_SHORT).show();
//////            it's before current'
////                }
//
//
//
//                dialog.dismiss();
//            }
//        });
//        close_clock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                grocery_onway.isSelected();
//                grocery_onway.setBackgroundResource((R.drawable.run_green));
//                set_picktime.setBackgroundResource(R.drawable.time_white);
//                select = 1;
//                dialog.dismiss();
//
//            }
//        });
//        dialog.setCancelable(false);
//        dialog.show();
//
//    }
    private void timepicker() {
        now = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                Home.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), false
        );
        tpd.setAccentColor("#2c835b");
        tpd.setMinTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE),now.get(Calendar.SECOND));
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    public void getGps() {
        gps = new GPSTracker(getApplicationContext());
        // check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            c_lattitude = "" + latitude;
            c_longitude = "" + longitude;

        } else {
            {
                try {
                    gps.showSettingsAlert();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString=null;
        String time=null;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        if(hourOfDay>12)
        {
            hourString=String.valueOf(hourOfDay - 12);
            time = hourString + ":" + minuteString + ":" + secondString+" PM";
        }
        else {
            hourString=String.valueOf(hourOfDay);
            time = hourString + ":" + minuteString + ":" + secondString+" AM";
        }
        //String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;

        SimpleDateFormat dateFormat = new SimpleDateFormat("E dd-MMM-yyyy");

        String date = dateFormat.format(now.getTime());
        //String[] arrayOfString = date.split(" ");
        set_picktime.setFocusableInTouchMode(false);
        set_picktime.setFocusable(false);
        set_picktime.setBackgroundResource((R.drawable.time_green));
        grocery_onway.setBackgroundResource(R.drawable.run_white);
        select = 2;
        pick_time=date+" "+time;

    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = Progress_class.createProgressDialog(Home.this);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONData jParser = new JSONData();
            String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latlon + "&" + true1;
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                JSONArray str = json.getJSONArray("results");
                JSONObject first = str.getJSONObject(1);
                JSONArray myRes = first.getJSONArray("address_components");
                JSONObject sec = myRes.getJSONObject(0);
                city_name = sec.getString("long_name");
                address.setText(city_name);
                latitude = c_lattitude;
                longitude = c_longitude;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void recreate() {
        getStatus = pref.getString("username", "");
        if (getStatus.equals("")) {
            my_account.setBackgroundResource(R.drawable.login);

        } else {
            my_account.setBackgroundResource(R.drawable.myaccnt);
        }
        try {
            if (CheckNetwork.isInternetAvailable(Home.this)) {
                getGps();
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo));
                LayoutInflater factory = LayoutInflater.from(Home.this);
                View layout = factory.inflate(R.layout.net, null);
                Button wifi = (Button) layout.findViewById(R.id.wifi);
                Button data = (Button) layout.findViewById(R.id.Data);
                Button quit = (Button) layout.findViewById(R.id.Quit);
                builder.setView(layout);
                wifi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
                data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
                quit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.exit(0);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    protected void onResume() {
        super.onResume();
        getStatus = pref.getString("username", "");
        if (getStatus.equals("")) {
            my_account.setBackgroundResource(R.drawable.login);

        } else {
            my_account.setBackgroundResource(R.drawable.myaccnt);
            getGps();
        }
        set_picktime.setBackgroundResource((R.drawable.time_white));
        grocery_onway.setBackgroundResource(R.drawable.run_green);

    }
}
