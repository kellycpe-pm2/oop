public class Attendee extends User{
    // constructor
    public Attendee(){
        super();
    }

    public Attendee(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
    }


    public boolean equalsClassType(Object o) {
        if (o instanceof Attendee) {
            return true;
        }
        return false;
    }

    public boolean equals(Object o) {
        if (o instanceof Attendee) {
            Attendee attendee = (Attendee) o;
            return this.getAccessUsername().equals(attendee.getAccessUsername());
        }
        return false; // the object does not belong to Event
    }
}

