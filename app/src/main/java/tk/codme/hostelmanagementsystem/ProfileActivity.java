package tk.codme.hostelmanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class ProfileActivity extends AppCompatActivity {

    private CircleImageView mProfileImage;
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
        final String tempDesignation=getIntent().getStringExtra("designation");

        mRootRef=FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase=mRootRef.child(tempDesignation).child(user_id);
        mUsersDatabase.keepSynced(true);
        mCurrent_user=FirebaseAuth.getInstance().getCurrentUser();

        mProfileImage=(CircleImageView)findViewById(R.id.profile_image);
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
                String designation=dataSnapshot.child("designation").getValue().toString();
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

                if(!image.equals("default")){
                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_img).into(mProfileImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(ProfileActivity.this,"cant load image",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                mProgressDialog.dismiss();

            }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

}
