package tk.codme.hostelmanagementsystem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class WardenListFragment extends Fragment {

    private RecyclerView mUsersList;
    private Query mUsersDatabase;
    private FirebaseAuth mAuth;
    private ImageButton phone,sms;
    private View mMainView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.activity_wardenlist, container, false);

        mAuth=FirebaseAuth.getInstance();
        String cid=mAuth.getCurrentUser().getUid();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users").child("wardens").orderByChild("caretaker").equalTo(cid);


        mUsersList = (RecyclerView)mMainView.findViewById(R.id.wardens_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(getContext()));
        return mMainView;
    }

    @Override
    public void onStart() {
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

                phone=(ImageButton)mMainView.findViewById(R.id.phone);
            /*    phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(WardenListFragment.this,"call",Toast.LENGTH_LONG).show();
                    }
                });
                */


                usersViewHolder.setName(users.getName());
                usersViewHolder.setMobile(users.getMobile());
                usersViewHolder.setImage(users.getImage());

                final String user_id=getRef(i).getKey();
                        usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]{"Open Profile", "Students list"};
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //click event for each item
                                        if (which == 0) {
                                            Intent profileIntent=new Intent(getContext(),ProfileActivity.class);
                                            profileIntent.putExtra("user_id",user_id);
                                            profileIntent.putExtra("designation","warden");
                                            startActivity(profileIntent);
                                        }
                                        if (which == 1) {
                                            Intent stdIntent = new Intent(getContext(), StudentListFragment.class);
                                            stdIntent.putExtra("user_id", user_id);
                                            startActivity(stdIntent);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
            }
        };

        mUsersList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        // firebaseRecyclerAdapter.stopListening();
    }

}



