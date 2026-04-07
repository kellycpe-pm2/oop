public class Staff extends User{
    private static int checkin_couter=0;
    public Staff(){
        super();

    }
    

    public Staff(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
    }
    public static void increase_CheckIn_Couter(){
        Staff.checkin_couter++;
    }
    
    public static int getCheckin_Couter(){
        return Staff.checkin_couter;
    }
    


    public String toString(){
            return super.toString();
    }

    public boolean equalsClassType(Object o) {
        if (o instanceof Staff) {
            return true;
        }
        return false;
    }

    public boolean equals(Object o) {
        if (o instanceof Staff) {
            Staff staff = (Staff) o;
            return this.getAccessUsername().equals(staff.getAccessUsername());
        }
        return false; // the object does not belong to Event
    }
}
