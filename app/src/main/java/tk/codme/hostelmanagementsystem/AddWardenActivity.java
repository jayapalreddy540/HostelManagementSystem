package tk.codme.hostelmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AddWardenActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private TextInputLayout mMobile;
    private Button mCreateBtn;
private TextView headText;

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef,mRef;
    private FirebaseDatabase mDatabase;

    private ProgressDialog mRegProgress;

    private String email1,password1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final String designation=getIntent().getStringExtra("designation");
        final String pid=getIntent().getStringExtra("pid");

        setContentView(R.layout.activity_addwarden);

        mAuth = FirebaseAuth.getInstance();


        mDisplayName=(TextInputLayout)findViewById(R.id.reg_disp_name);
        mEmail=(TextInputLayout)findViewById(R.id.reg_email);
        mPassword=(TextInputLayout)findViewById(R.id.reg_pass);
        mMobile=(TextInputLayout)findViewById(R.id.reg_mobile);
        mCreateBtn=(Button)findViewById(R.id.reg_create_btn);
        headText=(TextView)findViewById(R.id.headingtext);

            headText.setText("CREATE WARDEN");
        mRegProgress=new ProgressDialog(this);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                String mobile=mMobile.getEditText().getText().toString();
                if(!TextUtils.isEmpty(display_name)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)){
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait..  we are creating your account");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(display_name,email,password,mobile,designation,pid);
                }

            }
        });
    }

    private void  register_user(final String display_name, String email, String password,final String mMobile,final String user,final String pid){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                            final String uid=current_user.getUid();
                            mRootRef=FirebaseDatabase.getInstance().getReference().child("users");

                                mRef=mRootRef.child("wardens").child(uid);


                            HashMap<String,String> userMap=new HashMap<>();
                            userMap.put("name",display_name);
                            userMap.put("designation","warden");
                            userMap.put("mobile",mMobile);
                            userMap.put("caretaker",pid);
                            userMap.put("image","default");
                            userMap.put("thumb_image","default");
                            mRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mRegProgress.dismiss();
                                    mRef.child("lat").setValue(1.2);
                                    mRef.child("long").setValue(1.2);
                                    mRef.child("lastloctime").setValue(0);
                                    Toast.makeText(AddWardenActivity.this,"Warden Created ..",Toast.LENGTH_LONG).show();
                                                mRegProgress=new ProgressDialog(AddWardenActivity.this);
                                                mRegProgress.setTitle("Important Messsage ");
                                                mRegProgress.setMessage("You Should Re-login to continue.");
                                                mRegProgress.setCanceledOnTouchOutside(false);
                                                mRegProgress.show();
                                                FirebaseAuth.getInstance().signOut();
                                                mRegProgress.hide();
                                                Intent mainIntent=new Intent(AddWardenActivity.this,LoginActivity.class);
                                                startActivity(mainIntent);

                                            }

                            });
                        }
                        else {
                            mRegProgress.hide();
                            String error="";
                            try{
                                throw task.getException();
                            }
                            catch(FirebaseAuthWeakPasswordException e){
                                error="Weak Password";
                            }catch(FirebaseAuthInvalidCredentialsException e){
                                error="invalid Email";
                            }catch(FirebaseAuthUserCollisionException e){
                                error="Account Already Exists";
                            }catch(Exception e){
                                error="Unknown error";
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(),error, Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}