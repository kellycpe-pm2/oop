import java.util.List;
public class Staff extends User{
    private static int total_Checkin_counter=0;
    private final String ROLE= "Staff";
    public Staff(){
        this (null,null, null,null);
    }

    public Staff(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
    }


    public static int getTotal_Checkin_counter(){
        return Staff.total_Checkin_counter;
    }
    
    public static void setTotal_Checkin_counter(int total_Checkin_counter){
    	Staff.total_Checkin_counter = total_Checkin_counter;
    }

    public static void increase_total_Checkin_counter(){
        Staff.total_Checkin_counter++;
    }
    
    public static int checkTotal_CheckIn( List <Ticket> tickets){
        for (Ticket ticket : tickets){
           if (ticket != null && !ticket.getStatus()) {
                Staff.total_Checkin_counter++;
            }
        }
        return Staff.total_Checkin_counter;
    }

    public String toString(){
            return super.toString()+String.format("|          Position       :  %-31s|\n"+
                                                  "|          Total Check-In :  %-31d|\n",ROLE,total_Checkin_counter);
 

    }

    public boolean checkClass(Object o) {
        if (o instanceof Staff) {
            return true;
        }
        return false;
    }

    public boolean equals(Object o) {
        if (super.equals(o)){
            return true;
        }else{
            return false;
        }
    }

    public boolean hasUser(String username) {
        if (getUsername().equals(username)) {
            return true;
        }
        return false;
    }
}
