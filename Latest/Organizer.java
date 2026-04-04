public class Organizer extends User {

    // instance variable
    private String contactNo;
    private static int no = 0;

    // ------------------constructor-------------------------------
    // default constructor
    Organizer() {
        super();
    }

    // parameterized constructor
    Organizer(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
        Organizer.no++;

    }

    // ------------------getter-------------------------------
    public String getContactNo(int no) {
        return this.contactNo;
    }

    @Override
    public int getno() {
        return Organizer.no;
    }

    // ------------------setter-------------------------------
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

         public String toString(){
        return super.toString();
     }

}