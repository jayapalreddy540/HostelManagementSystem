package tk.codme.hostelmanagementsystem;

import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private View mMainView;
    private  int[] mRoom={0,R.id.i1,R.id.i2,R.id.i3,R.id.i4,R.id.i5,R.id.i6,R.id.i7,R.id.i8,R.id.i9,R.id.i10,R.id.i11,R.id.i12,R.id.i13,R.id.i14,R.id.i15};
    private ImageView[] chk=new ImageView[16];
    private Boolean[] alloc=new Boolean[16];
    private DatabaseReference mRef;
    private Boolean b;

@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mMainView = inflater.inflate(R.layout.fragment_home, container, false);
    FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
    final String uid=current_user.getUid();

   /* for(int i=1;i<=15;i++) {
         alloc[i]=false;
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child("wardens").child(uid).child("seats").child("s" + i);


        Query query = mRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                   if(dataSnapshot.equals(true)){
                       b=true;
                   }
                   else b=false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                b = (Boolean) snapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        alloc[i]=b;
    }
    for(int i=1;i<=15;i++) {
        if(alloc[i].equals(false))
        chk[i].setImageDrawable(null);
        else chk[i].setImageDrawable(getResources().getDrawable(R.drawable.yes));;
    }
    */
    return mMainView;
}
}
