import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;
import java.io.File;
import java.io.Writer;

public class Speaker extends user {

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
        super(username, password, email);
        this.bio[no] = bio;
        this.no = no;
    }

    // ------------------getter-------------------------------
    public String getBio(int no) {
        return this.bio[no];
    }

    @Override
    public int getNo() {
        return this.no;
    }

    // ------------------setter-------------------------------
    public void setBio(String bio) {
        this.bio[no] = bio;
    }

    // ------------------toString-------------------------------
    public String toString(int no) {
        return String.format("%-15s %-25s %-30s",
                getUsername(no), getEmail(no), this.bio[no]);
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

    // ------------------method-------------------------------

    // create speaker file if not exist
    public void createSpeakerFile() {
        try {
            File speakerFile = new File("speaker.json");
            if (speakerFile.createNewFile()) {
                System.out.println("Please Waiting...");
                System.out.println("Speaker file created: " + speakerFile.getName());
            }
        } catch (IOException e) {
            System.out.println("Error creating speaker file: " + e.getMessage());
        }
    }

    // load the data from speaker.json
    public void readSpeakerData() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("speaker.json"));
            if (lines.isEmpty()) {
                no = 0;
            } else {
                // each record has 4 fields: username, password, email, bio
                no = (lines.size() / 4) - 1;
                String[][] information = new String[no + 1][4];

                for (int i = 0; i < lines.size(); i++) {
                    information[i / 4][i % 4] = lines.get(i);
                }

                for (int i = 0; i <= no; i++) {
                    setUsername(information[i][0]);
                    setPassword(information[i][1]);
                    setEmail(information[i][2]);
                    this.bio[i] = information[i][3];
                }
            }
        } catch (IOException e) {
            createSpeakerFile();
        }
    }

    // store speaker data to speaker.json
    public void storeSpeakerData() {
        try (Writer writer = new java.io.FileWriter("speaker.json", true)) {
            if (getUsername(no) != null && getEmail(no) != null && this.bio[no] != null) {
                writer.write(getUsername(no) + "\n");
                writer.write(getPassword(no) + "\n");
                writer.write(getEmail(no) + "\n");
                writer.write(this.bio[no] + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error storing speaker data: " + e.getMessage());
        }
    }

    // create a new speaker account
    public void createSpeaker(signUp signUpObj, String bio) {
        readSpeakerData();
        this.no++;
        setUsername(signUpObj.getSignUpName());
        setPassword(signUpObj.getSignUpPassword());
        setEmail(signUpObj.getSignUpEmail());
        this.bio[no] = bio;
        storeSpeakerData();
    }

    // ------------------upload bio-------------------------------
    public boolean uploadBio(String username, String newBio) {
        readSpeakerData();
        for (int i = 0; i <= no; i++) {
            if (getUsername(i) != null && getUsername(i).equals(username)) {
                this.bio[i] = newBio;
                rewriteSpeakerData();
                System.out.println("Bio updated successfully for: " + username);
                return true;
            }
        }
        System.out.println("Speaker not found: " + username);
        return false;
    }

    // ------------------upload session topic-------------------------------
    public boolean uploadSessionTopic(String username, Session session, String newTopic) {
        readSpeakerData();

        // verify the speaker exists
        boolean found = false;
        for (int i = 0; i <= no; i++) {
            if (getUsername(i) != null && getUsername(i).equals(username)) {
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Speaker not found: " + username);
            return false;
        }

        // verify this speaker is assigned to the session
        Speaker[] assigned = session.getSpeakers();
        for (int i = 0; i < session.getSpeakerCount(); i++) {
            if (assigned[i] != null && assigned[i].getUsername(0) != null
                    && assigned[i].getUsername(0).equals(username)) {
                session.setTopic(newTopic);
                System.out.println("Session [" + session.getSessionID()
                        + "] topic updated to: \"" + newTopic
                        + "\" by speaker: " + username);
                return true;
            }
        }

        System.out.println("Speaker [" + username + "] is not assigned to session ["
                + session.getSessionID() + "]. Cannot update topic.");
        return false;
    }

    // ------------------rewrite speaker.json-------------------------------
    private void rewriteSpeakerData() {
        try (Writer writer = new java.io.FileWriter("speaker.json", false)) {
            for (int i = 0; i <= no; i++) {
                if (getUsername(i) != null) {
                    writer.write(getUsername(i) + "\n");
                    writer.write(getPassword(i) + "\n");
                    writer.write(getEmail(i) + "\n");
                    writer.write(this.bio[i] + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error rewriting speaker data: " + e.getMessage());
        }
    }
}