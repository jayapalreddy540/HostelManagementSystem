package tk.codme.hostelmanagementsystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class DateTimeFragment extends Fragment {

    View mMainView;
    private EditText reason;
    private Button outbtn,inbtn,submit;
    private TextView outtime,intime;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef,mRef;
    private ProgressDialog mRegProgress;
    private String caretaker;
    private Long intimetext,outtimetext;
    private Calendar calendar,date;

    public DateTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView= inflater.inflate(R.layout.fragment_date_time, container, false);

        SharedPreferences sp=this.getActivity().getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
         caretaker=sp.getString("caretaker","");
        Log.d("caretaker",caretaker);

        reason=(EditText)mMainView.findViewById(R.id.reason);
        outbtn=(Button)mMainView.findViewById(R.id.outtime);
        inbtn=(Button)mMainView.findViewById(R.id.intime);
        submit=(Button)mMainView.findViewById(R.id.submit);
        outtime=(TextView)mMainView.findViewById(R.id.tvouttime);
        intime=(TextView)mMainView.findViewById(R.id.tvintime);
        calendar = Calendar.getInstance();
        outbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outtimetext=showDateTimePicker();
                outtime.setText("Out Time : " +getTimeDate(outtimetext));
            }
        });
        inbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intimetext=showDateTimePicker();
                intime.setText("In Time : " +getTimeDate(intimetext));
            }
        });


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!outtimetext.equals(null)&&!intimetext.equals(null))
                    {
                        mRegProgress = new ProgressDialog(getContext());
                        mRegProgress.setTitle("PLease Wait");
                        mRegProgress.setMessage("Setting Outing  Time...   ");
                        mRegProgress.setCanceledOnTouchOutside(false);
                        mRegProgress.show();


                        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = current_user.getUid();
                        mRootRef = FirebaseDatabase.getInstance().getReference();

                        mRef = mRootRef.child("outing").child(caretaker);
                        String reason1 = reason.getEditableText().toString();

                        HashMap<String, String> userMap = new HashMap<>();
                        userMap.put("reason", reason1);
                        userMap.put("outtime", String.valueOf(outtimetext));
                        userMap.put("intime", String.valueOf(intimetext));
                        userMap.put("name",current_user.getDisplayName());
                        userMap.put("uid", uid);
                        String timestamp = calendar.get(Calendar.YEAR) + "" + calendar.get(Calendar.MONTH) + "" + calendar.get(Calendar.DAY_OF_MONTH) + "" + calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE);
                        mRef.child(timestamp).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                mRegProgress.dismiss();
                                Toast.makeText(getActivity(), "outing time registered", Toast.LENGTH_LONG).show();

                                Intent mainIntent = new Intent(getContext(), MainActivity.class);
                                startActivity(mainIntent);
                                mRegProgress.hide();
                            }

                        });
                    }
                    else{
                        Toast.makeText(getContext(),"times not set",Toast.LENGTH_SHORT).show();
                    }
            }


    });

        return mMainView;
    }

    public Long showDateTimePicker() {

        calendar = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v("datetime", "The choosen one " + date.getTime());
                        Log.v("datetime1", "The choosen one " + date.getTimeInMillis());
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE)).show();

       return date.getTimeInMillis();
    }


    public static String getTimeDate(long timedate){
        DateFormat dateFormat=DateFormat.getDateTimeInstance();
        Date netDate=new Date(timedate);
        return dateFormat.format(netDate);
    }
}
