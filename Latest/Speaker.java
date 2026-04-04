import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Speaker extends User {

    // instance variable
    private static String[] bio = new String[100];
    private static int no = 0;

    // ------------------constructor-------------------------------
    // default constructor
    Speaker() {
        super();
    }

    // parameterized constructor
    Speaker(String username, String password, String email, String bio) {
        super(username, password, email, null);
        Speaker.bio[no] = bio;
        no++;
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

    @Override
    public int getno() {
        return Speaker.no;
    }

    // ------------------setter-------------------------------
    public void setBio(String bio) {
        Speaker.bio[no] = bio;
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
    System.out.println("Bio: " + (no > 0 ? Speaker.bio[no - 1] : "No bio available"));
}
    // ------------------method-------------------------------
    
     // Ensure speaker file exists (create if not)
public static void ensureSpeakerFileExists() {
    File speakerFile = new File("speaker.json");
    if (!speakerFile.exists()) {
        try {
            if (speakerFile.createNewFile()) {
                System.out.println("Speaker file created: " + speakerFile.getName());
            }
        } catch (IOException e) {
            System.out.println("Error creating speaker file: " + e.getMessage());
        }
    }
}

// Load speaker data with auto-create
public static void readSpeakerData() {
    ensureSpeakerFileExists();  // Make sure file exists first
    
    try {
        List<String> lines = Files.readAllLines(Paths.get("speaker.json"));
        
        if (lines.isEmpty()) {
            no = 0;
        } else {
            no = (lines.size() / 4);
            String[][] information = new String[no][4];

            for (int i = 0; i < lines.size(); i++) {
                information[i / 4][i % 4] = lines.get(i);
            }

            for (int i = 0; i < no; i++) {
                Speaker.bio[i] = information[i][3];
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading speaker data: " + e.getMessage());
    }
}

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
        // readSpeakerData();
        for (int i = 0; i <= no; i++) {
            if (getAccessUsername() != null && getAccessUsername().equals(username)) {
                Speaker.bio[i] = newBio;
                // rewriteSpeakerData();
                System.out.println("Bio updated successfully for: " + username);
                return true;
            }
        }
        System.out.println("Speaker not found: " + username);
        return false;
    }


    // Upload/update session topic
public boolean uploadSessionTopic(String username, Session session, String newTopic) {
    // Check if this speaker is assigned to the session
    Speaker[] assigned = session.getSpeakers();
    for (int i = 0; i < session.getSpeakerCount(); i++) {
        if (assigned[i] != null && assigned[i].getAccessUsername().equals(username)) {
            // Check if speaker has accepted the session
            String status = session.getSpeakerStatus(username);
            if ("accepted".equals(status)) {
                session.setTopic(newTopic);
                System.out.println("Session [" + session.getSessionID() 
                    + "] topic updated to: \"" + newTopic
                    + "\" by speaker: " + username);
                return true;
            } else if ("pending".equals(status)) {
                System.out.println("You need to accept the session first before updating the topic.");
                return false;
            } else if ("rejected".equals(status)) {
                System.out.println("You have rejected this session. Cannot update topic.");
                return false;
            }
        }
    }
    System.out.println("Speaker [" + username + "] is not assigned to session ["
        + session.getSessionID() + "]. Cannot update topic.");
    return false;
}
   
     
}
