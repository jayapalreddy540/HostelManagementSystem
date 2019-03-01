package tk.codme.hostelmanagementsystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


public class TimerActivity extends Fragment {
    private View mView;
    private Calendar calendar;
    private EditText reason;
    private TextView dateView,timeView;
    private Button setdate,submit;
    private FragmentActivity myContext;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef,mRef;
    private String date;

    private ProgressDialog mRegProgress;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.activity_timer, container, false);
        reason=(EditText)mView.findViewById(R.id.reasonOuting);
        dateView = (TextView)mView.findViewById(R.id.textDate);
        timeView=(TextView)mView.findViewById(R.id.textTime);
        setdate=(Button)mView.findViewById(R.id.setDate);
        submit=(Button)mView.findViewById(R.id.submitBtn);
        reason=(EditText)mView.findViewById(R.id.reasonOuting);
        calendar = Calendar.getInstance();
        return mView;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePicker();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTime();
            }
        });
    }
    public void submitTime(){

        mRegProgress=new ProgressDialog(getContext());
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

       date=calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
        String reason1=reason.getEditableText().toString();
        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("reason",reason1);
        userMap.put("date",date);
        String timestamp=calendar.get(Calendar.YEAR)+""+calendar.get(Calendar.MONTH)+""+calendar.get(Calendar.DAY_OF_MONTH)+""+calendar.get(Calendar.HOUR_OF_DAY)+calendar.get(Calendar.MINUTE);
        mRef.child(timestamp).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                mRegProgress.dismiss();
                Toast.makeText(getActivity(),"outing time registered", Toast.LENGTH_LONG).show();

                Intent mainIntent=new Intent(getContext(),MainActivity.class);
                startActivity(mainIntent);
                mRegProgress.hide();
            }

        });

    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(myContext.getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            dateView.setText("date : "+String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));
            date=String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year);
        }
    };
}

