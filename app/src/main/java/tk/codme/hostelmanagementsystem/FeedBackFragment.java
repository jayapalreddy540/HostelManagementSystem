package tk.codme.hostelmanagementsystem;

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

public class FeedBackFragment extends Fragment {
    private View mMainView;
    private DatabaseReference mRef;
    private RecyclerView mfeedbackList;
    private Query mUsersDatabase;
    private FirebaseAuth mAuth;
    private String caretaker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.activity_feedbackfrag, container, false);

        /*SharedPreferences sp=this.getActivity().getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
        caretaker=sp.getString("caretaker","");
        Log.d("caretaker",caretaker);*/
        mAuth=FirebaseAuth.getInstance();
        String cid=mAuth.getCurrentUser().getUid();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("feedback").orderByChild("caretaker").equalTo(cid);

        mfeedbackList = (RecyclerView) mMainView.findViewById(R.id.feedback_list);
        mfeedbackList.setHasFixedSize(true);
        mfeedbackList.setLayoutManager(new LinearLayoutManager(getContext()));

        return mMainView;
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<FeedBack> options = new FirebaseRecyclerOptions.Builder<FeedBack>()
                .setQuery(mUsersDatabase, FeedBack.class)
                .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FeedBack, FeedBackViewHolder>(options) {
            @NonNull
            @Override
            public FeedBackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_layout, parent, false);
                return new FeedBackViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FeedBackViewHolder usersViewHolder, int i, @NonNull FeedBack users) {

                usersViewHolder.setFeedback(users.getFeedback());
                usersViewHolder.setFeedbackType(users.getFeedbackType());
                usersViewHolder.setName(users.getName());


             /*   final String user_id=getRef(i).getKey();
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

        mfeedbackList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
}

    @Override
    public void onStop() {
        super.onStop();
        // firebaseRecyclerAdapter.stopListening();
    }

}
