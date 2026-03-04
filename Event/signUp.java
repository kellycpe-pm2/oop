package Event;

public class signUp {
    // Sign Up of Instance
    private String signUpName ;
    private String signUpEmail ;
    private String signUpPassword ;
    private String signUpPassword2;
    private user existUser = new user();

// --------------------------Sign Up of Constructor---------------------------
//default constructor
    signUp(){
        this.signUpName = " ";
        this.signUpEmail = " ";
        this.signUpPassword = " ";
        this.signUpPassword2 = " ";
    }   
//parameterized constructor
    signUp(String signUpName, String signUpEmail, String signUpPassword, String signUpPassword2){
        this.signUpName = signUpName;
        this.signUpEmail = signUpEmail;     
        this.signUpPassword = signUpPassword;
        this.signUpPassword2 = signUpPassword2;
    }

//-------------------------- getter---------------------------
    public String getSignUpName() {
        return signUpName;
    }

    public String getSignUpEmail() {
        return signUpEmail;
    }

    public String getSignUpPassword() {
        return signUpPassword;
    }
//-------------------------- setter---------------------------
    public void setSignUpName(String signUpName) {
        this.signUpName = signUpName;
    }

    public void setSignUpEmail(String signUpEmail) {
        this.signUpEmail = signUpEmail;
    }

    public void setSignUpPassword(String signUpPassword) {
        this.signUpPassword = signUpPassword;
    }
    //-------------------------- method---------------------------
    //validation
    public boolean validationExist(){
        for (int i=0 ; i< existUser.getNo();i++){
            if(this.signUpEmail.equals(existUser.getEmail(i))){
                return false;
            }
            else{
                return true;
            }
        }
        return true;
    }

    public boolean validationName(){
        if(this.signUpName.strip().isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    public boolean validationEmail(){
        char [] emailArray = this.signUpEmail.toCharArray();

        //check the first character of email
        if (!((int)emailArray[0]>=65 && (int)emailArray[0]<=90) && !((int)emailArray[0] >=97 && (int)emailArray[0] <=122)){
            return false;
        }
        
        //check the email format
        if(!this.signUpEmail.contains("@gmail.com")){
            return false;
        }
        return true;
    }

    //validation password (it length must be more than 5 char)
    public boolean validationPassword(){
        if(this.signUpPassword.length() < 5){
            return false;
        }else{
            return true;
        }
    }

    public boolean validationPassword2(){
        if(this.signUpPassword.equals(this.signUpPassword2)){
            return true;
        }else{
            return false;
        }
    }

    public boolean validationEmpty(){
        if(this.signUpName.strip().isEmpty() || this.signUpEmail.strip().isEmpty() || this.signUpPassword.strip().isEmpty() || this.signUpPassword2.strip().isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    public boolean getResult(){
        if(validationExist() && validationName() && validationEmail() && validationPassword() && validationPassword2() && validationEmpty()){
            return true;
    }else{
            return false;
    }
}


}