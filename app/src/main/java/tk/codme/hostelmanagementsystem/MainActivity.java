package tk.codme.hostelmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mUserDatabase;

    private ProgressDialog mSignoutProgress;
    private Button mlogout,mWardens,mAddwarden;
    private TextView mStatus;

    private String designation="student";//default
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        final String email=getIntent().getStringExtra("email");
        final String password=getIntent().getStringExtra("password");
        if(getIntent().hasExtra("designation"))
             designation=getIntent().getStringExtra("designation");

        mSignoutProgress=new ProgressDialog(this);
        mlogout=(Button)findViewById(R.id.btnLogout);
        mWardens=(Button)findViewById(R.id.btnWardens);
        mAddwarden=(Button)findViewById(R.id.btnAddwarden);
        mStatus=(TextView)findViewById(R.id.txtStatus);

        if(currentUser!=null) {
            currentUid = currentUser.getUid();
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(designation+"s").child(currentUid);
            mUserDatabase.keepSynced(true);
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     String name = dataSnapshot.child("name").getValue().toString();
                    mStatus.setText("you're " + name + " , " + designation);

                    if (designation.equals("admin")) {
                        mWardens.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent wlIntent = new Intent(MainActivity.this, WardenListActivity.class);
                                startActivity(wlIntent);
                            }
                        });
                        mAddwarden.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent awIntent = new Intent(MainActivity.this, AddWardenActivity.class);
                                awIntent.putExtra("designation", "warden");
                                awIntent.putExtra("pid",currentUid);
                                awIntent.putExtra("email",email);
                                awIntent.putExtra("password",password);
                                startActivity(awIntent);
                            }
                        });
                    } else if (designation.equals("warden")) {
                        mWardens.setText("Students List");
                        mWardens.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent wl1Intent = new Intent(MainActivity.this, StudentListActivity.class);
                                startActivity(wl1Intent);
                            }
                        });
                        mAddwarden.setText("Add Student");
                        mAddwarden.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent aw1Intent = new Intent(MainActivity.this, AddWardenActivity.class);
                                aw1Intent.putExtra("designation", "student");
                                aw1Intent.putExtra("pid",currentUid);
                                aw1Intent.putExtra("email",email);
                                aw1Intent.putExtra("password",password);
                                startActivity(aw1Intent);


                            }
                        });
                    } else if (designation.equals("student")) {
                        mWardens.setVisibility(View.INVISIBLE);
                        mAddwarden.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else{
            sendToStart();
        }

        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignoutProgress.setTitle("Logout");
                mSignoutProgress.setMessage("Logging out...");
                mSignoutProgress.setCanceledOnTouchOutside(false);
                mSignoutProgress.show();
                FirebaseAuth.getInstance().signOut();
                sendToStart();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendToStart();
        }
    }
        private void sendToStart() {
            mSignoutProgress.dismiss();
            Intent startIntent=new Intent(MainActivity.this,StartActivity.class);
            startActivity(startIntent);
            finish();
        }
}
