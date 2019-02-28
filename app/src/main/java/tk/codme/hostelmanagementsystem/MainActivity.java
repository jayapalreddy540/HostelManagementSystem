package tk.codme.hostelmanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mUserDatabase;

    private ProgressDialog mSignoutProgress;
    //  private Button mWardens,mAddwarden,mSend,mMaps;
    private TextView mStatus;

    GridLayout mainGrid;

    private String designation;
    private String currentUid,name,image;
    private CircleImageView mProfileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        //Set Event
        //setSingleEvent(mainGrid);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);


        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        SharedPreferences sp=getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
        designation=sp.getString("designation","");
        name=sp.getString("name","");
        image=sp.getString("image","");

        TextView navUsername = (TextView) headerView.findViewById(R.id.textView);
        navUsername.setText(name);

        CircleImageView imageview=(CircleImageView)headerView.findViewById(R.id.imageView);


        mSignoutProgress=new ProgressDialog(MainActivity.this);

        if(currentUser!=null) {
            currentUid = currentUser.getUid();
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(designation+"s").child(currentUid);
            mUserDatabase.keepSynced(true);
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     name = dataSnapshot.child("name").getValue().toString();
                    designation=dataSnapshot.child("designation").getValue().toString();
                     image=dataSnapshot.child("image").getValue().toString();
                    Double latitude = (Double) dataSnapshot.child("lat").getValue();
                    Double longitude = (Double) dataSnapshot.child("long").getValue();
                    String lastloctime = dataSnapshot.child("lastloctime").getValue().toString();
                    // mStatus.setText("you're " + name + " as " + designation);

                    SharedPreferences sp=getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
                    sp.edit().putString("designation",designation).apply();
                    sp.edit().putString("name",name).apply();
                    sp.edit().putString("image",image).apply();
                    sp.edit().putString("latitude", String.valueOf(latitude)).apply();
                    sp.edit().putString("longitude", String.valueOf(longitude)).apply();
                    sp.edit().putString("lastloctime", String.valueOf(lastloctime)).apply();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else{
            sendToStart();
        }



    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                    intent.putExtra("info", "This is activity from card item index  " + finalI);
                    startActivity(intent);

                }
            });
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_members) {
            if(designation.equals("admin")){
                Intent wlIntent = new Intent(MainActivity.this, WardenListActivity.class);
                startActivity(wlIntent);
            }
            else if(designation.equals("warden")){
                Intent wl1Intent = new Intent(MainActivity.this, StudentListActivity.class);
                startActivity(wl1Intent);
            }
        } else if (id == R.id.nav_addmembers) {
            if(designation.equals("admin")){
                Intent awIntent = new Intent(MainActivity.this, AddWardenActivity.class);
                awIntent.putExtra("designation", "warden");
                awIntent.putExtra("pid",currentUid);
                startActivity(awIntent);
            }
            else if(designation.equals("warden")){
                Intent aw1Intent = new Intent(MainActivity.this, AddStudentActivity.class);
                aw1Intent.putExtra("designation", "student");
                aw1Intent.putExtra("pid",currentUid);
                startActivity(aw1Intent);
            }

        } else if (id == R.id.nav_location) {
            Intent mapIntent=new Intent(MainActivity.this,MapsActivity.class);
            startActivity(mapIntent);

        } else if (id == R.id.nav_outing) {
            Intent timeIntent=new Intent(MainActivity.this,TimerActivity.class);
            startActivity(timeIntent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
