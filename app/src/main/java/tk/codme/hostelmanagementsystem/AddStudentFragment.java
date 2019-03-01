package tk.codme.hostelmanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;

public class AddStudentFragment extends Fragment {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private TextInputLayout mMobile,pMobile;
    private Button mCreateBtn;
    private TextView headText;

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef,mRef;
    private FirebaseDatabase mDatabase;

    private ProgressDialog mRegProgress;

    private View mMainView;

    public AddStudentFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.activity_addstudent, container, false);

        SharedPreferences sp=this.getActivity().getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
        final String designation=sp.getString("designationto","");
        final String pid=sp.getString("pid","");

        mAuth = FirebaseAuth.getInstance();


        mDisplayName=(TextInputLayout)mMainView.findViewById(R.id.reg_disp_name);
        mEmail=(TextInputLayout)mMainView.findViewById(R.id.reg_email);
        mPassword=(TextInputLayout)mMainView.findViewById(R.id.reg_pass);
        mMobile=(TextInputLayout)mMainView.findViewById(R.id.reg_mobile);
        pMobile=(TextInputLayout)mMainView.findViewById(R.id.reg_pmobile);
        mCreateBtn=(Button)mMainView.findViewById(R.id.reg_create_btn);
        headText=(TextView)mMainView.findViewById(R.id.headingtext);

            headText.setText("CREATE STUDENT");

        mRegProgress=new ProgressDialog(getContext());

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                String mobile=mMobile.getEditText().getText().toString();
                String pmobile=pMobile.getEditText().getText().toString();
                if(!TextUtils.isEmpty(display_name)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)||!TextUtils.isEmpty(mobile)||!TextUtils.isEmpty(pmobile)){
                    mRegProgress.setTitle("Registering Student..");
                    mRegProgress.setMessage("Please wait..  we are creating your account");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(display_name,email,password,mobile,pmobile,designation,pid);
                }

            }
        });
        return mMainView;
    }

    private void  register_user(final String display_name, String email, String password,final String mobile,final String pmobile,final String user,final String pid){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                            String uid=current_user.getUid();
                            mRootRef=FirebaseDatabase.getInstance().getReference().child("users");

                                mRef=mRootRef.child("students").child(uid);

                            HashMap<String,String> userMap=new HashMap<>();
                            userMap.put("name",display_name);
                            userMap.put("designation","student");
                            userMap.put("mobile",mobile);
                            userMap.put("pmobile",pmobile);
                            userMap.put("caretaker",pid);
                            userMap.put("image","default");
                            userMap.put("thumb_image","default");
                            mRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mRegProgress.dismiss();
                                    Toast.makeText(getContext(),"Student Created ..",Toast.LENGTH_LONG).show();
                                    mRegProgress=new ProgressDialog(getContext());
                                    mRegProgress.setTitle("Important Messsage ");
                                    mRegProgress.setMessage("You Should Re-login to continue.");
                                    mRegProgress.setCanceledOnTouchOutside(false);
                                    mRegProgress.show();
                                    FirebaseAuth.getInstance().signOut();
                                    mRegProgress.hide();
                                    Intent mainIntent=new Intent(getContext(),LoginActivity.class);
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
                            Toast.makeText(getActivity(),error, Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}