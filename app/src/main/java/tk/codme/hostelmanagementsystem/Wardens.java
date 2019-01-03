package tk.codme.hostelmanagementsystem;

class Wardens {

        public String name;
        public String image;
        public String mobile;

        public Wardens(){

        }
        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getMobile() {
            return mobile;
        }

        public void setStatus(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public Wardens(String name, String image, String mobile) {
            this.name = name;
            this.image = image;
            this.mobile = mobile;
        }

        public void setName(String name) {
            this.name = name;
        }

}
