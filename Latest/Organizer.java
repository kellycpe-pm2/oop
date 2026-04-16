public class Organizer extends User {

    // instance variable
    private final String ROLE = "Organizer";
    private static int oREventCount;

    // ------------------constructor-------------------------------
    public Organizer() {
        super();
    }

    public Organizer(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
    }

    // ------------------OReventcount getter/setter-------------------------------

    // Counts non-null events directly from TestUser.events array.
    public static int getoREventCount() {
        int count = 0;
        for (Event e : TestUser.events) {
            if (e != null)
                count++;
        }
        Organizer.oREventCount = count;
        return oREventCount;
    }

    public static void setoREventCount(int oREventCount) {
        Organizer.oREventCount = oREventCount;
    }

    // ------------------toString-------------------------------

    public String toString() {
        return super.toString() + String.format("|          Position       :  %-31s|\n", ROLE);
    }

    // ------------------displayProfile-------------------------------
    public void displayProfile() {
        System.out.println("-------------------------------------------------------------");
        System.out.println("|                    Organizer Profile                      |");
        System.out.println("|-----------------------------------------------------------|");
        System.out.println("|                                                           |");
        System.out.print(this.toString());
        System.out.println("|                                                           |");
        System.out.println("-------------------------------------------------------------");
        System.out.println("Please Click Enter To continue...");
    }

    public boolean checkClass(Object o) {
        if (o instanceof Organizer) {
            return true;
        }
        return false;
    }

    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        } else {
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
