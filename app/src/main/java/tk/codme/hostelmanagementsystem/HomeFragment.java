package tk.codme.hostelmanagementsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private View mMainView;
    private  int[] mRoom={0,R.id.i1,R.id.i2,R.id.i3,R.id.i4,R.id.i5,R.id.i6,R.id.i7,R.id.i8,R.id.i9,R.id.i10,R.id.i11,R.id.i12,R.id.i13,R.id.i14,R.id.i15};
    private ImageView[] chk=new ImageView[16];
    private String[] alloc=new String[16];
    private DatabaseReference mRef;
    private Boolean b;

@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mMainView = inflater.inflate(R.layout.fragment_home, container, false);
    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    final String uid = current_user.getUid();

    SharedPreferences sp=this.getActivity().getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
    final String designation=sp.getString("designation","");
    final String caretaker=sp.getString("caretaker","");

    for(int i=0;i<=15;i++){
        chk[i]=(ImageView)mMainView.findViewById(mRoom[i]);
    }
    if(designation.equals("student")) {
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child("wardens").child(caretaker).child("seats");
    }
    else{
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child("wardens").child(uid).child("seats");
    }
        try{
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        for (int i = 1; i <= 15; i++) {
                            alloc[i] = dataSnapshot.child("s" + i).getValue().toString();
                            Log.d(i + "", alloc[i]);
                            if (alloc[i].equals("true"))
                                chk[i].setImageResource(R.drawable.yes);
                        }
                    }
                    catch(Exception e){}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
catch(Exception e){}
    return mMainView;
}
}
