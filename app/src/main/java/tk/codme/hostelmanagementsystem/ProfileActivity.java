package tk.codme.hostelmanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class ProfileActivity extends AppCompatActivity {

    private ImageView mProfileImage;
    private TextView mProfileName,mProfileMobile,mProfilePMobile;


    private DatabaseReference mUsersDatabase,mRootRef;
    private ProgressDialog mProgressDialog;

    private FirebaseUser mCurrent_user;
    private  String designation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id=getIntent().getStringExtra("user_id");
        SharedPreferences sp=getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
        designation=sp.getString("designation","");

        mRootRef=FirebaseDatabase.getInstance().getReference().child("users");

        if(designation.equals("admin")) designation="warden";
        else if(designation.equals("admin")) designation="student";
        mUsersDatabase=mRootRef.child(designation+"s").child(user_id);
        mUsersDatabase.keepSynced(true);
        mCurrent_user=FirebaseAuth.getInstance().getCurrentUser();

        mProfileImage=(ImageView)findViewById(R.id.profile_image);
        mProfileName=(TextView)findViewById(R.id.profile_displayName);
        mProfileMobile=(TextView)findViewById(R.id.mobile);
        mProfilePMobile=(TextView)findViewById(R.id.pmobile);

        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please wait while we load the user data");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String designation=dataSnapshot.child("designation").getValue().toString();
                String display_name = dataSnapshot.child("name").getValue().toString();
                String mobile = dataSnapshot.child("mobile").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                   mProfileName.setText("Name   : "+display_name);
                 mProfileMobile.setText("mobile : "+mobile);

                if(designation.equals("student")) {
                    String pmobile=dataSnapshot.child("pmobile").getValue().toString();
                    mProfilePMobile.setText("Parent : " + pmobile);
                }
                else mProfilePMobile.setVisibility(View.INVISIBLE);
                //Picasso.get().load(image).placeholder(R.drawable.default_img).into(mProfileImage);
                mProgressDialog.dismiss();

            }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

}
