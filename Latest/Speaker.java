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

 public Map<String, String> getSessionTopics() {
        return sessionTopics;
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

// Set bio at specific index (for file operations)
    public void setBioAtIndex(String bio) {
        this.bio= bio;
    }

    public void setBio(String bio) {
    this.bio = bio;
}

    public void setSessionTopics(Map<String, String> sessionTopics) {
        this.sessionTopics = sessionTopics;
    }
    
    public void addSessionTopic(String sessionId, String topic) {
        if (this.sessionTopics == null) {
            this.sessionTopics = new HashMap<>();
        }
        this.sessionTopics.put(sessionId, topic);
    }

    public String toTableRow() {
        String shortBio = (bio != null && bio.length() > 30) ? bio.substring(0, 27) + "..." : bio;
        return String.format("%-15s %-25s %-30s", getUsername(), getEmail(), shortBio);
    }

    // ------------------displayInfo-------------------------------
    public void displayInfo() {
        System.out.println("=== Speaker Info ===");
        System.out.printf("%-15s %-25s %-30s%n", "Username", "Email", "Bio");
        System.out.println("--------------------------------------------------------------------");
        
            System.out.println(toTableRow());
        
    }

    public void displaySingleInfo() {
        System.out.println("=== Speaker Info ===");
        System.out.println("Username: " + getUsername());
        System.out.println("Email: " + getEmail());
        System.out.println("Bio: " + getBio());
    }
    // ------------------method-------------------------------

    

    // Find speaker by username from speaker array
    public static Speaker findSpeakerByUsername(String username, Speaker[] speakerArray, int speakerCount) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakerArray[i] != null && speakerArray[i].getUsername().equals(username)) {
                return speakerArray[i];
            }
        }
        return null;
    }

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
