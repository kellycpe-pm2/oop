public class Speaker extends User {

    // instance variable
    private static String[] bio = new String[100];
    private static int no = 0;
    private final String role = "Speaker";

    // ------------------constructor-------------------------------
    // default constructor
    Speaker() {
        super();
    }

    // parameterized constructor
    Speaker(String username, String password, String email, String contactno, String bio) {
        super(username, password, email, contactno);
        Speaker.bio[no] = bio;
        no++;
    }

    Speaker(String username, String password, String email, String contactno) {
        super(username, password, email, contactno);

    }

    // ------------------getter-------------------------------
      public String getBio(int no) {
        return Speaker.bio[no];
    }

    public String getBio() {
        if (no > 0) {
            return Speaker.bio[no - 1];
        }
        return "No bio available";
    }

    public static String[] getBioArray() {
    return bio;
}

    @Override
    public int getno() {
        return Speaker.no;
    }

// ------------------setter-------------------------------
// Set current speaker's bio
public void setCurrentBio(String bio) {
    if (no > 0) {
        Speaker.bio[no - 1] = bio;
    }
}

// Set the total number of speakers (for loading from file)
public static void setSpeakerCount(int count) {
    no = count;
}

// Set bio at specific index (for file operations)
public static void setBioAtIndex(int index, String bioText) {
    if (index >= 0 && index < no) {
        Speaker.bio[index] = bioText;
    }
}

    // ------------------toString-------------------------------
    public String toString(int no) {
        return String.format("%-15s %-25s %-30s",
                getAccessUsername(), Speaker.bio[no]);
    }

    // ------------------displayInfo-------------------------------
    public void displayInfo() {
        System.out.println("=== Speaker Info ===");
        System.out.printf("%-15s %-25s %-30s%n", "Username", "Email", "Bio");
        System.out.println("--------------------------------------------------------------------");
        for (int i = 0; i <= no; i++) {
            System.out.println(toString(i));
        }
    }

    public void displaySingleInfo() {
        System.out.println("=== Speaker Info ===");
        System.out.println("Username: " + getAccessUsername());
        System.out.println("Email: " + getAccessEmail());
        System.out.println("Bio: " + getBio());
    }
    // ------------------method-------------------------------

    

    // Find speaker by username from speaker array
    public static Speaker findSpeakerByUsername(String username, Speaker[] speakerArray, int speakerCount) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakerArray[i] != null && speakerArray[i].getAccessUsername().equals(username)) {
                return speakerArray[i];
            }
        }
        return null;
    }

    // ------------------upload bio-------------------------------
    public boolean uploadBio(String username, String newBio) {
    if (getAccessUsername() != null && getAccessUsername().equals(username)) {
        if (no > 0) {
            Speaker.bio[no - 1] = newBio;  // Update at current speaker's index
            System.out.println("Bio updated successfully for: " + username);
            return true;
        }
    }
    System.out.println("Speaker not found: " + username);
    return false;
}

    

    // --------------------------------------------------

    public String toString() {
        int total_bio = no + 1;
        return super.toString() + String.format("║          Position       :  %-31s║\n" +
                "║          Total Bio      :  %-31d║\n", role, total_bio);
    }

    public boolean checkClass(Object o) {
        if (o instanceof Speaker) {
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

    public boolean equals(String username) {
        if (getAccessUsername().equals(username)) {
            return true;
        }
        return false;
    }
}
