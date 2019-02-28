package tk.codme.hostelmanagementsystem;


import android.app.ProgressDialog;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;
    private Button mLogin_btn;
    private RadioGroup usergroup;
    private RadioButton userstate;
    private EditText pass,email;

    private String designation;
    private FirebaseAuth mAuth;

    private ProgressDialog mLoginProgress;

    private AppCompatCheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();


        mLoginEmail=(TextInputLayout)findViewById(R.id.login_Email);
        mLoginPassword=(TextInputLayout)findViewById(R.id.login_Pass);
        email=(EditText)findViewById(R.id.login_email);
        pass=(EditText)findViewById(R.id.login_pass);
        mLogin_btn=(Button)findViewById(R.id.login_btn);
        usergroup=(RadioGroup)findViewById(R.id.usergroup);
        checkbox = (AppCompatCheckBox) findViewById(R.id.checkbox);
        pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                                    if (isChecked) {
                                                        // show password
                                                        pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                                    } else {
                                                        // hide password
                                                        pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                                    }
                                                }
                                            });
        mLoginProgress=new ProgressDialog(LoginActivity.this);
        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mLoginEmail.getEditText().getText().toString();
                String password=mLoginPassword.getEditText().getText().toString();
               designation=checkButton(v);

                if(!TextUtils.isEmpty(email)||!TextUtils.isEmpty((password))){
                    mLoginProgress.setTitle("logging  In");
                    mLoginProgress.setMessage("Please wait while we check your credentials");
                    mLoginProgress.show();
                    loginUser(email,password);
                }
            }
        });
    }

    private void loginUser(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mAuth=FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    String currentUid=currentUser.getUid();
                    DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(designation + "s").child(currentUid);
                    mUserDatabase.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if(dataSnapshot.child("name").exists()){
                                                                     designation=dataSnapshot.child("designation").getValue().toString();
                                                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    SharedPreferences sp=getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
                                                                    sp.edit().putString("designation",designation).apply();
                                                                    mLoginProgress.dismiss();
                                                                    startActivity(mainIntent);
                                                                    finish();
                                                                }
                                                                else{
                                                                    FirebaseAuth.getInstance().signOut();
                                                                    Toast.makeText(LoginActivity.this, "Login Failed!! Please check login credentials",
                                                                            Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                    startActivity(getIntent());
                                                                }

                                                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    mLoginProgress.hide();
                    Toast.makeText(LoginActivity.this, "Login Failed!! Please check login credentials",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public String checkButton(View view) {
        int radioId=usergroup.getCheckedRadioButtonId();
        userstate=findViewById(radioId);
        return ((String) userstate.getText());
    }
}
