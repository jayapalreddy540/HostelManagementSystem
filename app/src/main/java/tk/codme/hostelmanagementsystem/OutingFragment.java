package tk.codme.hostelmanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OutingFragment extends Fragment {
    private View mMainView;
    private DatabaseReference mRef;
    private RecyclerView moutingList;
    private Query mUsersDatabase;
    private FirebaseAuth mAuth;
    private String caretaker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.activity_outingfrag, container, false);

        SharedPreferences sp=this.getActivity().getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
        caretaker=sp.getString("caretaker","");
        Log.d("caretaker",caretaker);
        mAuth=FirebaseAuth.getInstance();
        String cid=mAuth.getCurrentUser().getUid();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("outing").child(cid);

        moutingList = (RecyclerView) mMainView.findViewById(R.id.outing_list);
        moutingList.setHasFixedSize(true);
        moutingList.setLayoutManager(new LinearLayoutManager(getContext()));

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Outing> options = new FirebaseRecyclerOptions.Builder<Outing>()
                .setQuery(mUsersDatabase, Outing.class)
                .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Outing, OutingViewHolder>(options) {
            @NonNull
            @Override
            public OutingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outing_layout, parent, false);
                return new OutingViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OutingViewHolder usersViewHolder, int i, @NonNull Outing users) {

                usersViewHolder.setIntime(users.getIntime());
                usersViewHolder.setReason(users.getReason());
                usersViewHolder.setOutime(users.getOuttime());
                usersViewHolder.setName(users.getName());
           /*     final String user_id=getRef(i).getKey();
                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileIntent=new Intent(getContext(),ProfileActivity.class);
                        profileIntent.putExtra("user_id",user_id);
                        profileIntent.putExtra("designation","student");
                        startActivity(profileIntent);
                    }
                });
            */
            }
        };

        moutingList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        // firebaseRecyclerAdapter.stopListening();
    }

}
