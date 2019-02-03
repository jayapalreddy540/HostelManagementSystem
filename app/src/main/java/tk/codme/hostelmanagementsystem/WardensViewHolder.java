package tk.codme.hostelmanagementsystem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import androidx.annotation.BinderThread;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class WardensViewHolder extends RecyclerView.ViewHolder{
    public ImageButton phoneBtn,msgBtn;
    private String mobileno;
    private Context context;

        View mView;
        public WardensViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            context = itemView.getContext();

            phoneBtn=(ImageButton)mView.findViewById(R.id.phone);
            msgBtn=(ImageButton)mView.findViewById(R.id.sms);

            phoneBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(WardensViewHolder.this,this,Toast.LENGTH_LONG).show();
                    Log.d("warden phone clicked","phone clicked"+mobileno);
                    Intent callIntent=new Intent(context,Dialing.class);
                    callIntent.putExtra("mobile",mobileno);
                    context.startActivity(callIntent);
                }
            });
            msgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("warden sms clicked","sms clicked"+mobileno);
                    Intent msgIntent=new Intent(context,MessageSending.class);
                    msgIntent.putExtra("mobile",mobileno);
                    context.startActivity(msgIntent);
                }
            });
        }
        public void setName(String name){
            TextView wardenNameView =(TextView)mView.findViewById(R.id.wardens_single_name);
            wardenNameView.setText(name);
        }
        public void setMobile(String mobile){
            mobileno=mobile;
            TextView wardenMobileView =(TextView)mView.findViewById(R.id.wardens_single_mobile);
            wardenMobileView.setText(mobile);
        }
        public void setImage(String image){
        CircleImageView wardenImageView=(CircleImageView)mView.findViewById(R.id.wardens_single_img);
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
}
