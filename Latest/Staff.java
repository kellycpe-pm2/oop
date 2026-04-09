import java.util.List;
public class Staff extends User{
    private static int checkin_couter=0;
    private final String role= "Staff";
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
            return super.toString()+String.format("║          Position       :  %-31s║\n"+
                                                  "║          Total Check-In :  %-31d║\n",role,checkin_couter);
 

    }
    public static int checkTotal_CheckIn( List <Ticket> tickets){
        for (Ticket ticket : tickets){
            if(!ticket.getStatus()){
                Staff.checkin_couter++;
            }
        }
        return Staff.checkin_couter;
    }

   public boolean checkClass(Object o){
        if(o instanceof Staff){
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
}
