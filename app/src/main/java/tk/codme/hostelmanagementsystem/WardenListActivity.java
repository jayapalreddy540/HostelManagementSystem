package tk.codme.hostelmanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class WardenListActivity extends AppCompatActivity {

    private RecyclerView mUsersList;
    private Query mUsersDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardenlist);

        mAuth=FirebaseAuth.getInstance();
        String cid=mAuth.getCurrentUser().getUid();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users").child("wardens").orderByChild("caretaker").equalTo(cid);


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
            protected void onBindViewHolder(@NonNull final WardensViewHolder usersViewHolder, int i, @NonNull Wardens users) {

                usersViewHolder.setName(users.getName());
                usersViewHolder.setMobile(users.getMobile());
                usersViewHolder.setImage(users.getImage());

                final String user_id=getRef(i).getKey();
                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]{"Open Profile", "Students list"};
                                final AlertDialog.Builder builder = new AlertDialog.Builder(WardenListActivity.this);
                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //click event for each item
                                        if (which == 0) {
                                            Intent profileIntent=new Intent(WardenListActivity.this,ProfileActivity.class);
                                            profileIntent.putExtra("user_id",user_id);
                                            profileIntent.putExtra("designation","wardens");
                                            startActivity(profileIntent);
                                        }
                                        if (which == 1) {
                                            Intent stdIntent = new Intent(WardenListActivity.this, StudentListActivity.class);
                                            stdIntent.putExtra("user_id", user_id);
                                            startActivity(stdIntent);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });


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

