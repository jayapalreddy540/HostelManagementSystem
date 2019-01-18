package tk.codme.hostelmanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private Button mWardens,mAddwarden,mSend,mMaps;
    private TextView mStatus;

    private String designation;
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        SharedPreferences sp=getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
        designation=sp.getString("designation","");

        mSignoutProgress=new ProgressDialog(this);
        mWardens=(Button)findViewById(R.id.btnWardens);
        mAddwarden=(Button)findViewById(R.id.btnAddwarden);
        mSend=(Button)findViewById(R.id.btnMsg);
        mMaps=(Button)findViewById(R.id.btnMap);
        mStatus=(TextView)findViewById(R.id.txtStatus);

        if(currentUser!=null) {
            currentUid = currentUser.getUid();
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(designation+"s").child(currentUid);
            mUserDatabase.keepSynced(true);
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     String name = dataSnapshot.child("name").getValue().toString();
                     designation=dataSnapshot.child("designation").getValue().toString();
                    mStatus.setText("you're " + name + " as " + designation);

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
                                Intent aw1Intent = new Intent(MainActivity.this, AddStudentActivity.class);
                                aw1Intent.putExtra("designation", "student");
                                aw1Intent.putExtra("pid",currentUid);
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

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msgIntent=new Intent(MainActivity.this,MessageSending.class);
                startActivity(msgIntent);
            }
        });
        mMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent=new Intent(MainActivity.this,MapsActivity.class);
                startActivity(mapIntent);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.main_logout_btn){
            mSignoutProgress.setTitle("Logout");
            mSignoutProgress.setMessage("Logging out...");
            mSignoutProgress.setCanceledOnTouchOutside(false);
            mSignoutProgress.show();
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }

        if(item.getItemId()==R.id.main_settings_btn){
            Intent settingsIntent=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settingsIntent);
        }
        if(item.getItemId()==R.id.main_feedback_btn){
            Intent usersIntent=new Intent(MainActivity.this,FeedBackActivity.class);
            startActivity(usersIntent);
        }
        return true;
    }
}
