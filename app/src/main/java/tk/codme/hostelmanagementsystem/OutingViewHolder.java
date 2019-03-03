package tk.codme.hostelmanagementsystem;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OutingViewHolder extends RecyclerView.ViewHolder {

    View mView;
    public OutingViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setDate (String date){
        TextView DateView = (TextView) mView.findViewById(R.id.date);
        DateView.setText(date);
    }

    public void setReason (String reason){
        TextView ReasonView = (TextView) mView.findViewById(R.id.reason);
        ReasonView.setText(reason);
    }

}
