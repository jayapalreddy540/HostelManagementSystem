package tk.codme.hostelmanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChangePassActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextInputEditText newpass,oldpass;
    private Button mStatusBtn;

    private DatabaseReference mRef;
    private FirebaseUser currentUser;

    private ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);


        //mToolbar=(Toolbar)findViewById(R.id.status_appbar);
        // setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Change Password");
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences sp = getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
        String designation = sp.getString("designation", "");

        newpass=(TextInputEditText)findViewById(R.id.newpass);
        oldpass=(TextInputEditText)findViewById(R.id.oldpass);
        mStatusBtn=(Button)findViewById(R.id.status_btn);
       mStatusBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final String newpass1=newpass.getText().toString();
               final String oldpass1=oldpass.getText().toString();
               final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
               mRegProgress=new ProgressDialog(ChangePassActivity.this);
               mRegProgress.show();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
               AuthCredential credential = EmailAuthProvider
                       .getCredential(user.getEmail(), oldpass1);

// Prompt the user to re-provide their sign-in credentials
               user.reauthenticate(credential)
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()) {
                                   user.updatePassword(newpass1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()) {

                                               Toast.makeText(ChangePassActivity.this, "Password updated", Toast.LENGTH_SHORT).show();

                                               mRegProgress.setTitle("Important Messsage ");
                                               mRegProgress.setMessage("You Should Re-login to continue.");
                                               mRegProgress.setCanceledOnTouchOutside(false);
                                               SharedPreferences preferences = getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
                                               SharedPreferences.Editor editor = preferences.edit();
                                               editor.clear();
                                               editor.commit();
                                               FirebaseAuth.getInstance().signOut();

                                               Intent mainIntent=new Intent(ChangePassActivity.this,LoginActivity.class);
                                               startActivity(mainIntent);
                                               mRegProgress.hide();
                                               finish();
                                           } else {
                                               Toast.makeText(ChangePassActivity.this, "Error password not updated", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });
                               } else {
                                   Log.d("change password", "Error auth failed");
                               }
                           }
                       });
/*
        mRegProgress = new ProgressDialog(ChangePassActivity.this);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = currentUser.getUid();
        currentUser.updatePassword(pass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("password change", "User password updated.");
                            Toast.makeText(ChangePassActivity.this, "password changed", Toast.LENGTH_SHORT).show();

                            mRegProgress=new ProgressDialog(ChangePassActivity.this);
                            mRegProgress.setTitle("Important Messsage ");
                            mRegProgress.setMessage("You Should Re-login to continue.");
                            mRegProgress.setCanceledOnTouchOutside(false);
                            mRegProgress.show();
                            FirebaseAuth.getInstance().signOut();
                            mRegProgress.hide();
                            Intent mainIntent=new Intent(ChangePassActivity.this,LoginActivity.class);
                            startActivity(mainIntent);
                        } else {
                            Toast.makeText(ChangePassActivity.this, "password change failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                */
           }
       });
    }
}
