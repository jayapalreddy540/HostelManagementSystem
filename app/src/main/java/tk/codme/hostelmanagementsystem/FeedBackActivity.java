package tk.codme.hostelmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FeedBackActivity extends AppCompatActivity {

   private EditText nameField,feedbackField,emailField;
    private Spinner feedbackSpinner;
    private CheckBox responseCheckbox;
    private Button Submit;
    private String name,email,feedback,feedbackType,caretaker;
    private DatabaseReference mFeedbackDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        nameField = (EditText) findViewById(R.id.name);
        emailField = (EditText) findViewById(R.id.email);
        feedbackField = (EditText) findViewById(R.id.FeedbackBody);
         feedbackSpinner = (Spinner) findViewById(R.id.SpinnerFeedbackType);
        responseCheckbox = (CheckBox) findViewById(R.id.CheckBoxResponse);

        Submit=(Button)findViewById(R.id.ButtonSendFeedback);
        SharedPreferences sp=getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
        caretaker=sp.getString("caretaker","");


        mFeedbackDatabase= FirebaseDatabase.getInstance().getReference().child("feedback");

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameField.getText().toString();
                email = emailField.getText().toString();
                feedback = feedbackField.getText().toString();
                final boolean bRequiresResponse = responseCheckbox.isChecked();

                feedbackType = feedbackSpinner.getSelectedItem().toString();
                if (!(name.isEmpty() || email.isEmpty() || feedback.isEmpty())) {
                    DatabaseReference newFeedbackref = mFeedbackDatabase.push();
                    String newFeedbackId = newFeedbackref.getKey();
                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("caretaker",caretaker);
                    userMap.put("name", name);
                    userMap.put("email", email);
                    userMap.put("feedbackType", feedbackType);
                    userMap.put("feedback", feedback);
                    userMap.put("response", String.valueOf(bRequiresResponse));
                    userMap.put("date", String.valueOf(ServerValue.TIMESTAMP));


                    mFeedbackDatabase.child(newFeedbackId).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "your feedback has been recorded thank you!! ", Toast.LENGTH_LONG).show();
                            Intent mainIntent = new Intent(FeedBackActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }
                    });


                } else {
                    Toast.makeText(getApplicationContext(), "please fill all  the fields in the form ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
