public class Organizer extends User {

    // instance variable
    private final String ROLE = "Organizer";

    // ------------------constructor-------------------------------
    Organizer() {
        super();
    }

    Organizer(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
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
