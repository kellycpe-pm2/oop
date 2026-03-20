//delete this file

public class login {
    // Login of Instance
    private String loginUsername;
    private String loginEmail;
    private String loginPassword;
    private user existUser = new user();

    // --------------------------Login of Constructor---------------------------
    // default constructor
    login() {
        this.loginEmail = " ";
        this.loginPassword = " ";
        this.loginUsername = " ";
    }

    // parameterized constructor
    login(String loginEmail, String loginPassword) {
        this.loginEmail = loginEmail;
        this.loginPassword = loginPassword;
        this.loginUsername = " ";
    }

    // -------------------------- getter---------------------------
    public String getLoginPassword() {
        return loginPassword;
    }

    // -------------------------- setter---------------------------
    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
    // -------------------------- method---------------------------
}
