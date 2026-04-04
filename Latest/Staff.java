public class Staff extends User{
    
    public Staff(){
        super();
    }

    public Staff(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
    }

        public String toString(){
            return super.toString();
    }
    
}
