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

    // ------------------toString-------------------------------
    public String toString(int no) {
        return String.format("%-15s",
                getAccessUsername());
    }

    // ------------------displayInfo-------------------------------
    public void displayInfo() {
        System.out.println("=== Organizer Info ===");
        System.out.printf("%-15s %-25s %-15s%n", "Username", "Email", "Contact No");
        System.out.println("----------------------------------------------------");
        for (int i = 0; i <= no; i++) {
            System.out.println(toString(i));
        }
    }

}