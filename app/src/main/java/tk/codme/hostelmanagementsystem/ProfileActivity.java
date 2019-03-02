package tk.codme.hostelmanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ProfileActivity extends AppCompatActivity {

    private CircleImageView mProfileImage;
    private TextView mProfileName,mProfileMobile,mProfilePMobile,mAddress;
    private ImageButton location;

    private Double latitude=0.0,longitude=0.0;


    private DatabaseReference mUsersDatabase,mRootRef;
    private ProgressDialog mProgressDialog;

    private FirebaseUser mCurrent_user;
    private  String designation;
    private String lastloctime,display_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id=getIntent().getStringExtra("user_id");
        final String tempDesignation=getIntent().getStringExtra("designation");

        mRootRef=FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase=mRootRef.child(tempDesignation+"s").child(user_id);
        mUsersDatabase.keepSynced(true);
        mCurrent_user=FirebaseAuth.getInstance().getCurrentUser();

        mProfileImage=(CircleImageView)findViewById(R.id.profile_image);
        mProfileName=(TextView)findViewById(R.id.profile_displayName);
        mProfileMobile=(TextView)findViewById(R.id.mobile);
        mProfilePMobile=(TextView)findViewById(R.id.pmobile);
        mAddress=(TextView)findViewById(R.id.presentloc);
        location=(ImageButton)findViewById(R.id.loc);

        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please wait while we load the user data");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String designation=dataSnapshot.child("designation").getValue().toString();
                 display_name = dataSnapshot.child("name").getValue().toString();
                String mobile = dataSnapshot.child("mobile").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                 try{
                     latitude= (Double) dataSnapshot.child("lat").getValue();
                     longitude= (Double) dataSnapshot.child("long").getValue();
                     lastloctime=getTimeDate((Long)dataSnapshot.child("lastloctime").getValue());
                 }
                 catch (Exception e){
                     location.setClickable(false);
                     Toast.makeText(ProfileActivity.this,display_name+" has not logged in since you registered him/her",Toast.LENGTH_LONG).show();
                 }

               /* GetTimeAgo getTimeAgo=new GetTimeAgo();
                long lastTime=(Long)(dataSnapshot.child("online").getValue());
                String lastSeenTime=getTimeAgo.getTimeAgo(lastTime,getApplicationContext());*/

                   mProfileName.setText("Name   : "+display_name);
                 mProfileMobile.setText("mobile : "+mobile);
                Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_img).into(mProfileImage);

                if(designation.equals("student")) {
                    String pmobile=dataSnapshot.child("pmobile").getValue().toString();
                    mProfilePMobile.setText("Parent : " + pmobile);
                }
                else mProfilePMobile.setVisibility(View.INVISIBLE);
             try {
                 Geocoder geocoder;
                 List<Address> addresses;
                 geocoder = new Geocoder(ProfileActivity.this, Locale.getDefault());

                 addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                 String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                 String city = addresses.get(0).getLocality();
                 String state = addresses.get(0).getAdminArea();
                 String country = addresses.get(0).getCountryName();
                 String postalCode = addresses.get(0).getPostalCode();
                 String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                 mAddress.setText("Last Loc : \n"+knownName+" "+address+" "+"\n\n"+"at :  "+lastloctime);
             }
             catch(Exception e){}
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
       try {
           location.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent mapIntent = new Intent(ProfileActivity.this, CustomMap.class);
                   mapIntent.putExtra("user_id", user_id);
                   mapIntent.putExtra("designation", tempDesignation);
                   mapIntent.putExtra("latitude", latitude);
                   mapIntent.putExtra("longitude", longitude);
                   startActivity(mapIntent);
               }
           });
       }
       catch (Exception e){
           Toast.makeText(ProfileActivity.this,display_name+" has not logged in since you registered him/her",Toast.LENGTH_LONG).show();
       }

    }

    public static String getTimeDate(long timedate){
        DateFormat dateFormat=DateFormat.getDateTimeInstance();
        Date netDate=new Date(timedate);
        return dateFormat.format(netDate);
    }
}


