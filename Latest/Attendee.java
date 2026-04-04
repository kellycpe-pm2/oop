public class Attendee extends User{
    // constructor
    public Attendee(){
        super();
    }

    public Attendee(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
    }


    public boolean foundAttendee(User user){

        if (this == user){
            return true;

        }
            return false;
    }
}

