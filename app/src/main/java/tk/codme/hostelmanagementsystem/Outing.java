package tk.codme.hostelmanagementsystem;

public class Outing {

    public Outing(){

    }

    public String date,reason;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Outing(String date, String reason) {
        this.date = date;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
