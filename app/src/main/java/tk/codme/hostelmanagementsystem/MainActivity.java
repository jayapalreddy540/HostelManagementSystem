package tk.codme.hostelmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog mSignoutProgress;
    private Button mlogout,mWardens,mAddwarden;
    private TextView mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mSignoutProgress=new ProgressDialog(this);
        mlogout=(Button)findViewById(R.id.btnLogout);
        mWardens=(Button)findViewById(R.id.btnWardens);
        mAddwarden=(Button)findViewById(R.id.btnAddwarden);
        mStatus=(TextView)findViewById(R.id.txtStatus);
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
        mWardens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wlIntent=new Intent(MainActivity.this,WardenListActivity.class);
                startActivity(wlIntent);
            }
        });
        mAddwarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent awIntent=new Intent(MainActivity.this,AddWardenActivity.class);
                startActivity(awIntent);
                awIntent.putExtra("designation","warden");

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
