
public class User {

    // the user
    private String username;
    private String password;
    private String email;
    private String contactNo;

    // --------------------------Sign Up of Constructor---------------------------
    // default constructor
    User() {
        this(null,null,null,null);
    }

    // parameterized constructor
     User(String username , String password, String email,String contactNo) {
        this.username =username;
        this.email = email;
        this.contactNo=contactNo;
        this.password = password;
    }

    

    // -------------------------- getter---------------------------

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }
    public String getContactNo() {
        return this.contactNo;
    }

    public String getPassword() {
        return this.password;
    }
    // -------------------------- setter---------------------------
    
    public void setUserName(String username){
        this.username=username;
    }
    
    public void setEmail( String email) {
        	this.email =email;
    }
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public void setPassword(String password) {
        this.password = password;
    }
   
   
    // -------------------------- method---------------------------
    public boolean validateUser() {
        return username != null && password != null && 
               email != null && contactNo != null;
    }

    public String toString(){
        return String.format(
                             "|          Username       :  %-31s|\n"+
                             "|          Email          :  %-31s|\n"+
                             "|          Contact Number :  %-31s|\n", username,email,contactNo);
    }

    public boolean checkClass(Object o){
        if(o instanceof User){
            return true;
        }
        return false;
    }

    public boolean equals(Object o) {
        if(this==o){return true;}
        if (o==null){
            return false;
        }
        if (o instanceof User) {
            User user = (User) o;
            return (username.equals(user.getUsername()));
        }
        return false; // the object does Not belong to Event
    }

    public boolean hasUser(String username){
        if(this.username.equals(username)){
            return true;
        }
        return false;
    }
}
