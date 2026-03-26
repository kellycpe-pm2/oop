
public class User {
    // Sign Up of Instance
    private String signUpName;
    private String signUpEmail;
    private String signUpContactNo;
    private String signUpPassword;
    private String signUpPassword2;

    // Login of Instance
    private String loginUsername;
    private String loginPassword;

    // the user
    private String username;
    private String pswd;
    private String email;
    private String contactNo;
    private int no;

    // --------------------------Sign Up of Constructor---------------------------
    // default constructor
    User() {
    }

    // parameterized constructor
    User(String signUpName, String signUpEmail, String signUpPassword, String signUpPassword2, String signUpContactNo) {
        this.signUpName = signUpName;
        this.signUpEmail = signUpEmail;
        this.signUpContactNo = signUpContactNo;
        this.signUpPassword = signUpPassword;
        this.signUpPassword2 = signUpPassword2;
    }

    User(String loginUsername, String loginPassword) {
        this.loginPassword = loginPassword;
        this.loginUsername = loginUsername;
    }

    User(String username, String email, String password, String contactNo) {
        this.username = username;
        this.email = email;
        this.contactNo = contactNo;
        this.pswd = password;
        this.contactNo = contactNo;
    }

    // -------------------------- getter---------------------------
    public String getSignUpName() {
        return this.signUpName;
    }

    public String getSignUpEmail() {
        return this.signUpEmail;
    }

    public String getSignUpPassword() {
        return this.signUpPassword;
    }

    public String getSignUpContactNo() {
        return this.signUpContactNo;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public String getAccessUsername() {
        return this.username;
    }

    public String getAccessEmail() {
        return this.email;
    }

    public String getAccessContactNo() {
        return this.contactNo;
    }

    public String getAccessPassword() {
        return this.pswd;
    }

    public int getno() {
        return this.no;
    }

    // -------------------------- setter---------------------------
    public void setSignUpName(String signUpName) {
        this.signUpName = signUpName;
    }

    public void setSignUpEmail(String signUpEmail) {
        this.signUpEmail = signUpEmail;
    }

    public void setSignUpContactNo(String signUpContactNo) {
        this.signUpContactNo = signUpContactNo;
    }

    public void setSignUpPassword(String signUpPassword) {
        this.signUpPassword = signUpPassword;
    }

    public void setSignUpPassword2(String signUpPassword2) {
        this.signUpPassword2 = signUpPassword2;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
    // -------------------------- method---------------------------

    // -------------------------Access User------------------------

    public void signUpUser() {
        username = signUpName;
        email = signUpEmail;
        contactNo = signUpContactNo;
        pswd = signUpPassword;
    }

    public void loginUser(String[] email, String[] contactNo) {
        username = loginUsername;
        this.email = email[no];
        this.contactNo = contactNo[no];
        pswd = loginPassword;
    }

    // validation for signup
    public boolean validationExist(String[] existUser) {
        if (validationEmpty(this.signUpName)) {

            return false;
        }
        for (int i = 0; i < existUser.length; i++) {
            if (this.signUpName.equals(existUser[i])) {
                System.out.println("Error: The Username Has Already Exist ! ");

                return false;
            }
        }
        return true;
    }

    public boolean validationName() {
        char[] namearray = this.signUpName.toCharArray();
        if (validationEmpty(this.signUpName)) {
            return false;

        } else if (namearray.length < 3) {
            System.out.println("Input Error : Your Name length must be at least 3 length.");
            return false;
        }

        else {

            for (char charname : namearray) {
                if (!((int) charname >= 65 && (int) charname <= 90)
                        && !((int) charname >= 97 && (int) charname <= 122)) {
                    System.out.println("Input Error : Please Enter In alpha !");
                    return false;
                }
            }

            return true;
        }

    }

    public boolean validationEmail() {
        if (validationEmpty(this.signUpEmail)) {
            return false;
        }
        char[] emailArray = this.signUpEmail.toCharArray();

        // check the first character of email
        if (!((int) emailArray[0] >= 65 && (int) emailArray[0] <= 90)
                && !((int) emailArray[0] >= 97 && (int) emailArray[0] <= 122)) {
            System.out.println("Input Error: The first Character Cannot Be Symbols ! ");
            return false;
        }
        // check the email format
        else if (!(this.signUpEmail.contains("@gmail.com"))) {

            System.out.println("Input Error: Please Input In Gmail Format ! ");
            return false;

        } else {

            return true;

        }
    }

    // validation for contact number
    public boolean validationContactNo() {

        char[] contactNoArray = this.signUpContactNo.toCharArray();
        if (validationEmpty(this.signUpContactNo)) {
            return false;
        }

        // check the contact number length
        else if (contactNoArray.length != 11) {
            System.out.println("Input Error: Please Enter In Format ! ");
            return false;

        }
        // check the character is in number

        else {
            for (int i = 0; i < contactNoArray.length; i++) {
                if (!((int) contactNoArray[i] >= 48 && (int) contactNoArray[i] <= 57)) {
                    System.out.println("Input Error: Please Enter In Format ! ");
                    return false;
                }

            }

            return true;

        }

    }

    // validation password (it length must be more than 5 char)
    public boolean validationPassword() {
        if (validationEmpty(this.signUpPassword)) {
            return false;
        }
        if (this.signUpPassword.length() < 5) {

            System.out.println("Input Error: Your Password Must be More Than 5 Character ! ");

            return false;
        } else {
            return true;
        }
    }

    public boolean validationPassword2() {
        if (validationEmpty(this.signUpPassword2)) {
            return false;
        }
        if (!(this.signUpPassword2.equals(this.signUpPassword))) {
            System.out.println(" Error: Your Password Is Not Matched ! ");

            return false;
        } else {
            return true;
        }
    }

    public boolean validationEmpty(String data) {
        if (data == null) {
            System.out.println("Input Error: Don't Empty Your Input ! ");

            return true;

        } else if (data.isEmpty()) {
            System.out.println("Input Error: Don't Empty Your Input ! ");

            return true;

        } else {

            return false;
        }
    }

    // validation for login

    public boolean validationNoExistName(String[] existUser) {
        if (validationEmpty(this.loginUsername)) {

            return false;
        }
        for (int i = 0; i < existUser.length; i++) {
            if (this.loginUsername.equals(existUser[i])) {
                this.no = i;
                return true;
            }
        }
        System.out.println("Error: The Username Is Not Matched ! ");

        return false;
    }

    public boolean validationLoginPwd(String[] password) {
        if (validationEmpty(this.loginPassword)) {
            return false;
        }
        if (this.loginPassword.equals(password[this.no])) {
            return true;

        }
        System.out.println("Error: Inccorrect Password ! ");

        return false;
    }

}