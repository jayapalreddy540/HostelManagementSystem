package tk.codme.hostelmanagementsystem;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    private Button mRegBtn,mLogBtn;
    private ImageView hmsImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


       hmsImg=(ImageView)findViewById(R.id.hmsImg);


        mLogBtn=(Button)findViewById(R.id.start_login_btn);
        mLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log_intent=new Intent(StartActivity.this,LoginActivity.class);
                startActivity(log_intent);
            }
        });
    }
}
