package tk.codme.hostelmanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
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

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mUserDatabase;
    private NavigationView navigationView;

    private ProgressDialog mSignoutProgress;
    //  private Button mWardens,mAddwarden,mSend,mMaps;
    private TextView mStatus;

    GridLayout mainGrid;

    private String designation,lastloctime;
    private String currentUid,name,image,caretaker;
    private Double latitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sp=getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
        designation=sp.getString("designation","");
        name=sp.getString("name","");
        image=sp.getString("image","default");

        FloatingActionButton fab;
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(designation.equals("student")) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Feedback", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Intent feedback = new Intent(MainActivity.this, FeedBackActivity.class);
                    startActivity(feedback);
                }
            });
        }
        else { fab.setVisibility(View.INVISIBLE);}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);


        Menu nav_Menu = navigationView.getMenu();
        if(designation.equals("student")) {
            nav_Menu.findItem(R.id.nav_members).setVisible(false);
            nav_Menu.findItem(R.id.nav_addmembers).setVisible(false);
            nav_Menu.findItem(R.id.nav_feedbacks).setVisible(false);
            nav_Menu.findItem(R.id.nav_rooms).setVisible(false);
            nav_Menu.findItem(R.id.nav_outingdetails).setVisible(false);
        }
        else if(designation.equals("warden")){
            nav_Menu.findItem(R.id.nav_outing).setVisible(false);
        }
        else if(designation.equals("admin")){
            nav_Menu.findItem(R.id.nav_outing).setVisible(false);
            nav_Menu.findItem(R.id.nav_rooms).setVisible(false);
            nav_Menu.findItem(R.id.nav_outingdetails).setVisible(false);
        }



        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_frame, new HomeFragment());
        tx.commit();


        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();


        TextView navUsername = (TextView) headerView.findViewById(R.id.textView);
        navUsername.setText(name);

        final CircleImageView imageview=(CircleImageView)headerView.findViewById(R.id.imageView);
        if (!image.equals("default")) {
            //Picasso.get().load(image).placeholder(R.drawable.default_img).into(mDisplayImage);
            Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_img).into(imageview, new Callback() {
                @Override
                public void onSuccess() {
                    Picasso.get().load(image).placeholder(R.drawable.default_img).into(imageview);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

       // CircleImageView imageview=(CircleImageView)headerView.findViewById(R.id.imageView);
        //Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_img).into(imageview);

        mSignoutProgress=new ProgressDialog(MainActivity.this);

        if(currentUser!=null) {
            currentUid = currentUser.getUid();
            Log.d("desig",designation);
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(designation+"s").child(currentUid);
            mUserDatabase.keepSynced(true);
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     name = dataSnapshot.child("name").getValue().toString();
                    designation=dataSnapshot.child("designation").getValue().toString();
                     image=dataSnapshot.child("image").getValue().toString();
                     try {
                         caretaker = dataSnapshot.child("caretaker").getValue().toString();
                     }
                     catch (Exception e){}


                    SharedPreferences sp1=getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
                    sp1.edit().putString("designation",designation).apply();
                    sp1.edit().putString("name",name).apply();
                    sp1.edit().putString("image",image).apply();
                    try {
                        sp1.edit().putString("caretaker", caretaker).apply();
                    }
                    catch (Exception e){}

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

        Intent startIntent=new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        SharedPreferences preferences = getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        mSignoutProgress.dismiss();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        if(designation.equals("warden")||designation.equals("admin"))
            menu.getItem(1).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.main_feedback_btn){
            Intent usersIntent=new Intent(MainActivity.this,FeedBackActivity.class);
            startActivity(usersIntent);
        }
        if(item.getItemId()==R.id.main_settings_btn){
            Intent settingsIntent=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settingsIntent);
        }
        if(item.getItemId()==R.id.main_logout_btn){
            SharedPreferences preferences = getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            mSignoutProgress.setTitle("Logout");
            mSignoutProgress.setMessage("Logging out...");
            mSignoutProgress.setCanceledOnTouchOutside(false);
            mSignoutProgress.show();
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }


        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        if(id==R.id.nav_home){
            HomeFragment fragmenthome = new HomeFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_frame,fragmenthome).commit();
        }

        else if (id == R.id.nav_members) {
            if(designation.equals("admin")){
              /*  Intent wlIntent = new Intent(MainActivity.this, WardenListFragment.class);
                startActivity(wlIntent);*/
                WardenListFragment fragmentwardenlist = new WardenListFragment();
                FragmentManager manager=getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.content_frame,fragmentwardenlist).commit();
            }
            else if(designation.equals("warden")){
                StudentListFragment fragmentstudentlist = new StudentListFragment();
                FragmentManager manager=getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.content_frame,fragmentstudentlist).commit();
            }
        } else if (id == R.id.nav_addmembers) {
            if(designation.equals("admin")){
                SharedPreferences sp=getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
                sp.edit().putString("designationto","warden").apply();
                sp.edit().putString("pid",currentUid).apply();
                AddWardenFragment fragmentaddwarden = new AddWardenFragment();
                FragmentManager manager=getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.content_frame,fragmentaddwarden).commit();
            }
            else if(designation.equals("warden")){
                SharedPreferences sp2=getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
                sp2.edit().putString("designationto","student").apply();
                sp2.edit().putString("pid",currentUid).apply();
                AddStudentFragment fragmentaddstudent = new AddStudentFragment();
                FragmentManager manager=getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.content_frame,fragmentaddstudent).commit();
            }

        } else if (id == R.id.nav_location) {

           Intent mapIntent=new Intent(MainActivity.this,MapsActivity.class);
            startActivity(mapIntent);

        } else if (id == R.id.nav_outing) {
            TimerFragment fragmenttime= new TimerFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_frame,fragmenttime).commit();
            /*Intent timeIntent=new Intent(MainActivity.this,TimerFragment.class);
            startActivity(timeIntent);*/

        } else if (id == R.id.nav_rooms) {
            RoomFragment fragmentroom= new RoomFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_frame,fragmentroom).commit();

        } else if (id == R.id.nav_feedbacks) {
            FeedBackFragment fragmentfeedback= new FeedBackFragment();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_frame,fragmentfeedback).commit();
        }
        else if (id == R.id.nav_outingdetails) {
            OutingFragment fragmentouting = new  OutingFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_frame, fragmentouting).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
