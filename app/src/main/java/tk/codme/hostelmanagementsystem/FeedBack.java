package tk.codme.hostelmanagementsystem;

public class FeedBack {

    public FeedBack(){

    }

    public String feedback;
    public String feedbackType;
    public String name;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public FeedBack(String feedback, String feedbackType, String name) {
        this.feedback = feedback;
        this.feedbackType = feedbackType;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
