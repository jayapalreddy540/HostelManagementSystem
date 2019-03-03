package tk.codme.hostelmanagementsystem;

import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OutingViewHolder extends RecyclerView.ViewHolder {

    View mView;
    public OutingViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setIntime (String intime){
        TextView DateView = (TextView) mView.findViewById(R.id.intime);
        DateView.setText("Intime : "+getTimeDate(Long.parseLong(intime)));
    }
    public void setOutime (String outtime){
        TextView DateView = (TextView) mView.findViewById(R.id.outtime);
        DateView.setText("OutTime : "+getTimeDate(Long.parseLong(outtime)));
    }

    public void setReason (String reason){
        TextView ReasonView = (TextView) mView.findViewById(R.id.reason);
        ReasonView.setText("Reason : "+reason);
    }

    public void setName(String name){
        TextView ReasonView = (TextView) mView.findViewById(R.id.name);
        ReasonView.setText(name);
    }

    public static String getTimeDate(long timedate){
        DateFormat dateFormat=DateFormat.getDateTimeInstance();
        Date netDate=new Date(timedate);
        return dateFormat.format(netDate);
    }
}
