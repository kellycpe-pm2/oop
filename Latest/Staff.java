public class Staff extends User{
    private static int checkin_couter=0;
    public Staff(){
        super();
    }

    public static int getCheckin_Couter(){
        return Staff.checkin_couter;
    }
    public static void increase_CheckIn_Couter(){
        Staff.checkin_couter++;
    }
    

    public Staff(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
    }

        public String toString(){
            return super.toString();
    }
    
}
