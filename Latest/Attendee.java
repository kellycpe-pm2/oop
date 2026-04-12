public class Attendee extends User{
    // constructor
    private final String ROLE= "Attendee";
    public Attendee(){
        super();
    }

    public Attendee(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
    }

    public String toString(){
            return super.toString()+String.format("║          Position       :  %-31s║\n",ROLE);
    }

    public boolean checkClass(Object o){
        if(o instanceof Attendee){
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
    
    public boolean hasUser(String username){
        if(getUsername().equals(username)){
            return true;
        }
        return false;
    }
}

