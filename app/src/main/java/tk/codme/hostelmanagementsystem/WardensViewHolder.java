package tk.codme.hostelmanagementsystem;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

    public class WardensViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public WardensViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setName(String name){
            TextView wardenNameView =(TextView)mView.findViewById(R.id.wardens_single_name);
            wardenNameView.setText(name);
        }
        public void setMobile(String mobile){
            TextView wardenMobileView =(TextView)mView.findViewById(R.id.wardens_single_mobile);
            wardenMobileView.setText(mobile);
        }
}
