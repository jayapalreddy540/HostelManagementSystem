package tk.codme.hostelmanagementsystem;

public class Outing {

    public Outing(){

    }

    public String intime,outtime,reason,name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public Outing(String intime, String outtime, String reason, String name) {
        this.intime = intime;
        this.outtime = outtime;
        this.reason = reason;
        this.name = name;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
