package tk.codme.hostelmanagementsystem;

public class Students {

    public Students(){

    }
    public String name;
    public String image;
    public String mobile;
    public String pmobile;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPmobile() {
        return pmobile;
    }

    public void setPmobile(String pmobile) {
        this.pmobile = pmobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Students(String name, String image, String mobile,String pmobile) {
        this.name = name;
        this.image = image;
        this.mobile = mobile;
        this.pmobile=pmobile;
    }
}
