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
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class RoomFragment extends Fragment {
    private View mMainView;
    private  int[] mRoom={0,R.id.c1,R.id.c2,R.id.c3,R.id.c4,R.id.c5,R.id.c6,R.id.c7,R.id.c8,R.id.c9,R.id.c10,R.id.c11,R.id.c12,R.id.c13,R.id.c14,R.id.c15};
    private CheckBox[] chk=new CheckBox[16];
    private Button submit;
    private String[] alloc=new String[16];
    private DatabaseReference mRef;

    public RoomFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.activity_roomfrag, container, false);

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = current_user.getUid();

        SharedPreferences sp=this.getActivity().getSharedPreferences("tk.codme.hostelmanagementsystem", Context.MODE_PRIVATE);
        final String designation=sp.getString("designation","");
        final String caretaker=sp.getString("caretaker","");

        for(int i=0;i<=15;i++){
            chk[i]=(CheckBox) mMainView.findViewById(mRoom[i]);
        }
        if(designation.equals("student")) {
            mRef = FirebaseDatabase.getInstance().getReference().child("users").child("wardens").child(caretaker).child("seats");
        }
        else if(designation.equals("admin")){
            mRef = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("users").child("wardens").orderByChild("caretaker").equalTo(uid);
            mRef.child("seats");
        }
        else{
            mRef = FirebaseDatabase.getInstance().getReference().child("users").child("wardens").child(uid).child("seats");
        }

        mRef.keepSynced(true);
        try{
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        for (int i = 1; i <= 15; i++) {
                            alloc[i] = dataSnapshot.child("s" + i).getValue().toString();
                            Log.d(i + "", alloc[i]);
                            if (alloc[i].equals("true")) {
                                if (chk[i].isChecked()) {

                                } else {
                                    chk[i].setChecked(true);
                                }
                            }
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
        //FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
        //final String uid=current_user.getUid();
        mRef= FirebaseDatabase.getInstance().getReference().child("users").child("wardens").child(uid).child("seats");
        for (int i = 1; i <=15; i++) {
            chk[i] = (CheckBox) mMainView.findViewById(mRoom[i]);
        }
        submit=(Button)mMainView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=1;i<=15;i++){
                    if(chk[i].isChecked()){
                        alloc[i]="true";
                    }
                    else alloc[i]="false";
                }
                try {
                    for (int i = 1; i <= 15; i++) {
                        mRef.child("s" + i).setValue(alloc[i]);
                    }
                    Toast.makeText(getContext(),"data uploaded",Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    Toast.makeText(getContext(),"data upload failed",Toast.LENGTH_SHORT).show();
                }


               // List<Boolean> list2 = new ArrayList<Boolean>(Arrays.asList(alloc));

            }
        });

        return mMainView;
    }
}
