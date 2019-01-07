package tk.codme.hostelmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class StudentListActivity extends AppCompatActivity {

    private RecyclerView mUsersList;
    private Query mUsersDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardenlist);

        mAuth=FirebaseAuth.getInstance();
        String cid=mAuth.getCurrentUser().getUid();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users").child("students").orderByChild("caretaker").equalTo(cid);


        mUsersList = (RecyclerView) findViewById(R.id.wardens_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Wardens> options = new FirebaseRecyclerOptions.Builder<Wardens>()
                .setQuery(mUsersDatabase, Wardens.class)
                .build();


        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Wardens, WardensViewHolder>(options) {
            @NonNull
            @Override
            public WardensViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wardens_single_layout, parent, false);
                return new WardensViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull WardensViewHolder usersViewHolder, int i, @NonNull Wardens users) {

                usersViewHolder.setName(users.getName());
                usersViewHolder.setMobile(users.getMobile());

                final String user_id=getRef(i).getKey();
                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent=new Intent(StudentListActivity.this,ProfileActivity.class);
                        profileIntent.putExtra("user_id",user_id);
                        profileIntent.putExtra("designation","students");
                        startActivity(profileIntent);
                    }
                });
            }
        };

        mUsersList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // firebaseRecyclerAdapter.stopListening();
    }

}

