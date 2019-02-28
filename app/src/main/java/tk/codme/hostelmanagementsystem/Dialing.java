package tk.codme.hostelmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Dialing extends AppCompatActivity {
    private Button button;
    private String mobile;
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialing);
        button = (Button) findViewById(R.id.buttonCall);


        Intent intent = getIntent();
        if (intent.hasExtra("mobile")) {

            mobile = getIntent().getStringExtra("mobile");
            button.setText("call : "+ mobile);
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d("call button clicked","request to call");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(Dialing.this,
                                Manifest.permission.CALL_PHONE)) {

                        } else {
                            ActivityCompat.requestPermissions(Dialing.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
                        }
                    }
                }
                else{
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + mobile));
                    startActivity(callIntent);
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + mobile));
                    startActivity(callIntent);
                    Toast.makeText(getApplicationContext(), "calling...",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Calling failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}

