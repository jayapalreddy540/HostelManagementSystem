package tk.codme.hostelmanagementsystem;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import androidx.annotation.NonNull;

public class TimerActivity extends Activity {
    private DatePicker datePicker;
    private Calendar calendar;
    private TimePicker timePicker;
    private Timer timer;
    private EditText reason;
    private TextView dateView,timeView;
    private int year, month, day;
    private int hour,minute,second,AMPM;
    private boolean is24HourView;

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef,mRef;

    private ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        reason=(EditText)findViewById(R.id.reasonOuting);
        dateView = (TextView) findViewById(R.id.textDate);
        timeView=(TextView)findViewById(R.id.textTime);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        hour=calendar.get(Calendar.HOUR_OF_DAY);
        minute=calendar.get(Calendar.MINUTE);
        second=calendar.get(Calendar.SECOND);
        AMPM=calendar.get(Calendar.AM_PM);
        if(AMPM==1)
            is24HourView=true;
        else is24HourView=false;

        showDate(year, month, day);

        showTime(hour,minute,is24HourView);

        mAuth = FirebaseAuth.getInstance();

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

    public void submitTime(View view){

        mRegProgress=new ProgressDialog(TimerActivity.this);
        mRegProgress.setTitle("PLease Wait");
        mRegProgress.setMessage("Setting Outing  Time...   ");
        mRegProgress.setCanceledOnTouchOutside(false);
        mRegProgress.show();

        FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
        String uid=current_user.getUid();
        mRootRef= FirebaseDatabase.getInstance().getReference();

        mRef=mRootRef.child("outing").child(uid);

       // DatabaseReference newOutingref = mRef.push();
       // String newOutingId = newOutingref.getKey();


         String reason1=reason.getEditableText().toString();

        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("reason",reason1);
        userMap.put("year",year);
        userMap.put("month",month+1);
        userMap.put("day",day);
        userMap.put("hour",hour);
        userMap.put("minute",minute);
        userMap.put("timestamp", ServerValue.TIMESTAMP.toString());
        String timestamp=calendar.get(Calendar.YEAR)+""+calendar.get(Calendar.MONTH)+""+calendar.get(Calendar.DAY_OF_MONTH)+""+calendar.get(Calendar.HOUR_OF_DAY)+calendar.get(Calendar.MINUTE);
        mRef.child(timestamp).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                mRegProgress.dismiss();
                Toast.makeText(TimerActivity.this,"outing time registered",Toast.LENGTH_LONG).show();

                Intent mainIntent=new Intent(TimerActivity.this,MainActivity.class);
                startActivity(mainIntent);
                mRegProgress.hide();
            }

        });


        Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,day,hour,minute,second);
        setAlarm(calendar.getTimeInMillis());
        Log.d("time","time in milliseconds"+calendar.getTimeInMillis()+"");
    }

    private void setAlarm(long time) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(getApplicationContext(), MyAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);



        //setting the repeating alarm that will be fired every hour
        am.setInexactRepeating(AlarmManager.RTC, time, AlarmManager.INTERVAL_HOUR, pi);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
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
                    showDate(arg1, arg2, arg3);
                    year=arg1;
                    month=arg2;
                    day=arg3;

                }
            };

    private TimePickerDialog.OnTimeSetListener myTimeListener=new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay1, int minute1) {
            showTime(hourOfDay1,minute1,is24HourView);
            hour=hourOfDay1;
            minute=minute1;
        }
    };

    private void showTime(int hourOfDay1, int minute1,boolean is24HourView1) {
        timeView.setText(new StringBuilder().append(hourOfDay1).append(":")
                .append(minute1).append(":").append(is24HourView1));


    }

    private void showDate(int year1, int month1, int day1) {
        dateView.setText(new StringBuilder().append(day1).append("/")
                .append(month1+1).append("/").append(year1));

    }
}