package tk.codme.hostelmanagementsystem;
import java.util.Calendar;
import java.util.Timer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;

import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimerActivity extends Activity {
    private DatePicker datePicker;
    private Calendar calendar;
    private Timer timer;
    private TextView dateView,timeView;
    private int year, month, day;
    private int hour,minute,AMPM;
    private boolean is24HourView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        dateView = (TextView) findViewById(R.id.textDate);
        timeView=(TextView)findViewById(R.id.textTime);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        hour=calendar.get(Calendar.HOUR_OF_DAY);
        minute=calendar.get(Calendar.MINUTE);
        AMPM=calendar.get(Calendar.AM_PM);
        if(AMPM==1)
            is24HourView=true;
        else is24HourView=false;

        showDate(year, month+1, day);

        showTime(hour,minute,is24HourView);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "set Date",
                Toast.LENGTH_SHORT)
                .show();
    }

    @SuppressWarnings("deprecation")
    public void setTime(View view) {
        showDialog(990);
        Toast.makeText(getApplicationContext(), "set Time",
                Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        if(id==990){
            return new TimePickerDialog(this,myTimeListener,hour,minute,is24HourView);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private TimePickerDialog.OnTimeSetListener myTimeListener=new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            showTime(hourOfDay,minute,is24HourView);
        }
    };

    private void showTime(int hourOfDay, int minute,boolean is24HourView) {
        timeView.setText(new StringBuilder().append(hourOfDay).append(":")
                .append(minute).append(":").append(is24HourView));
    }

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}