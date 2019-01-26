package tk.codme.hostelmanagementsystem;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class StudentsViewHolder extends RecyclerView.ViewHolder{

    View mView;
    public StudentsViewHolder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;
    }
    public void setName(String name){
        TextView wardenNameView =(TextView)mView.findViewById(R.id.students_single_name);
        wardenNameView.setText(name);
    }
    public void setMobile(String mobile){
        TextView wardenMobileView =(TextView)mView.findViewById(R.id.students_single_mobile);
        wardenMobileView.setText(mobile);
    }
    public void setImage(String image){
        CircleImageView wardenImageView=(CircleImageView)mView.findViewById(R.id.students_single_img);
        if(!image.equals("default")){
            Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_img).into(wardenImageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }
    /*
    public void setMobile(String mobile){
        TextView wardenMobileView =(TextView)mView.findViewById(R.id.students_single_pmobile);
        wardenMobileView.setText(mobile);
    }
    */
}
