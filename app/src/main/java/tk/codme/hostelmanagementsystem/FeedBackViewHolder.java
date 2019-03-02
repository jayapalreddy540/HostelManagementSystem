package tk.codme.hostelmanagementsystem;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class FeedBackViewHolder  extends RecyclerView.ViewHolder{

    View mView;

    public FeedBackViewHolder( View itemView) {
        super(itemView);
        mView = itemView;

    }

    public void setFeedback (String feedback){
            TextView FeedbackView = (TextView) mView.findViewById(R.id.feedback);
            FeedbackView.setText(feedback);
        }

        public void setFeedbackType (String feedbackType){
            TextView FeedbackTypeView = (TextView) mView.findViewById(R.id.feedback_type);
            FeedbackTypeView.setText(feedbackType);
        }

        public void setName (String name){
            TextView FeedbackNameView = (TextView) mView.findViewById(R.id.feedback_name);
            FeedbackNameView.setText(name);
        }

}
