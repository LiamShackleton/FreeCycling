package freecycling.example.freecycling.Interface.DTOs;

public class LoginDTO {

    private String userName;
    private String pass;

    public LoginDTO() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
