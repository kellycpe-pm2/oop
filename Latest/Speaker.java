import java.util.HashMap;
import java.util.Map;

public class Speaker extends User {

    // instance variable
    private String bio;
    private Map<String, String> sessionTopics;
    private static int totalSpeakers=0;
    private final String ROLE = "Speaker";

    // ------------------constructor-------------------------------
        // default constructor
    public Speaker() {
        super();
        this.bio = "No bio available"; 
        this.sessionTopics = new HashMap<>();
        totalSpeakers++;
    }

    // parameterized constructor (without bio)
    public Speaker(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
        this.bio = "No bio available"; 
        this.sessionTopics = new HashMap<>(); 
        totalSpeakers++;
    }

    // parameterized constructor (with bio)
    public Speaker(String username, String password, String email, String contactNo, String bio) {
        super(username, password, email, contactNo);  
        this.bio = (bio != null && !bio.isEmpty()) ? bio : "No bio available";
         this.sessionTopics = new HashMap<>();
        totalSpeakers++;
    }

    // ------------------getter-------------------------------
    public String getBio() {
        return bio;
    }

    public String getUpdatedSessionTopic(String sessionId) {
        if (sessionTopics != null && sessionTopics.containsKey(sessionId)) {
            return sessionTopics.get(sessionId);
        }
        return null;
    }

    // Getter for total speakers
    public static int getTotalSpeakers() {
        return totalSpeakers;
    }



// ------------------setter-------------------------------
// Set the total number of speakers (for loading from file)
    public static void setTotalSpeakers(int count) {
        totalSpeakers = count;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setSessionTopics(Map<String, String> sessionTopics) {
        this.sessionTopics = sessionTopics;
    }

    // ------------------displayInfo-------------------------------

    public void displaySingleInfo() {
        System.out.println("=== Speaker Info ===");
        System.out.println("Username: " + getUsername());
        System.out.println("Email: " + getEmail());
        System.out.println("Bio: " + getBio());
    }
    // ------------------method-------------------------------

    // ------------------upload bio-------------------------------
    public boolean uploadBio(String newBio) {
        if (newBio != null && !newBio.trim().isEmpty()) {
            this.bio = newBio;
            System.out.println("Bio updated successfully for: " + getUsername());
            return true;
        }
        System.out.println("Error: Bio cannot be empty");
        return false;
    }

    

    // --------------------------------------------------

    public String toString() {
        return super.toString() + String.format("║          Position       :  %-31s║\n", ROLE);
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

    public boolean hasUser(String username) {
        if (getUsername().equals(username)) {
            return true;
        }
        return false;
    }
}
