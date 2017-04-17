package com.example.jineesh.time_picker_demo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TimePicker;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class MainActivity extends Activity implements TimePickerDialog.OnTimeSetListener {


    Button time;
    int date, month, year;
    Calendar now;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time = (Button) findViewById(R.id.time);
        try {
            time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("click", "success");
                    now = Calendar.getInstance();
                    TimePickerDialog tpd = TimePickerDialog.newInstance(
                            MainActivity.this,
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE), false
                    );
                    tpd.setAccentColor("#2c835b");
                    tpd.setMinTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
                    tpd.show(getFragmentManager(), "Timepickerdialog");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
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
      //  SimpleDateFormat time_zone = new SimpleDateFormat("a");
        String date = dateFormat.format(now.getTime());
        //String zone = time_zone.format(now.getTime());
        String[] arrayOfString = date.split(" ");
       // Log.e("Dateeee", arrayOfString[2]);
        Log.e("Date: ",date+" "+time);


    }
}
