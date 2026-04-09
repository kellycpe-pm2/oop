public class Organizer extends User {

    // instance variable
    private static int no = 0;
    private final String role = "Organizer";

    // ------------------constructor-------------------------------
    Organizer() {
        super();
    }

    Organizer(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
        Organizer.no++;
    }

    @Override
    public int getno() {
        return Organizer.no;
    }

    // ------------------toString-------------------------------
    public String toString(int no) {
        return String.format("%-15s", getAccessUsername());
    }

    public String toString() {
        return super.toString() + String.format("║          Position       :  %-31s║\n", role);
    }

    // ------------------displayProfile-------------------------------
    public void displayProfile() {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                    Organizer Profile                      ║");
        System.out.println("║═══════════════════════════════════════════════════════════║");
        System.out.println("║                                                           ║");
        System.out.print(this.toString());
        System.out.println("║                                                           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println("Please Click Enter To continue...");
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

    public boolean checkClass(Object o) {
        if (o instanceof User) {
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

    public boolean hasOrganizer(String username) {
        if (getAccessUsername().equals(username)) {
            return true;
        }
        return false;
    }
}
