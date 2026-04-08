public class Attendee extends User{
    // constructor
    private final String role= "Attendee";
    public Attendee(){
        super();
    }

    public Attendee(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
    }

    public String toString(){
            return super.toString()+String.format("║          Position       :  %-31s║\n",role);
    }

    public boolean checkClass(Object o){
        if(o instanceof User){
            return true;
        }
        return false;
    }

    public boolean equals(Object o) {
        if (o==null){
            return false;
        }
        if (o instanceof Attendee) {
            Attendee attendee = (Attendee) o;
            return this.getAccessUsername().equals(attendee.getAccessUsername());
        }
        return false; // the object does not belong to Event
    }
    
    public boolean equals(String username){
        if(getAccessUsername().equals(username)){
            return true;
        }
        return false;
    }
}

