import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;
import java.io.File;
import java.io.Writer;

public class Organizer extends user {

    // instance variable
    private static String[] contactNo = new String[100];
    private static int no = 0;

    // ------------------constructor-------------------------------
    // default constructor
    Organizer() {
        super();
    }

    // parameterized constructor
    Organizer(String username, String password, String email, String contactNo) {
        super(username, password, email);
        this.contactNo[no] = contactNo;
        this.no = no;
    }

    // ------------------getter-------------------------------
    public String getContactNo(int no) {
        return this.contactNo[no];
    }

    @Override
    public int getNo() {
        return this.no;
    }

    // ------------------setter-------------------------------
    public void setContactNo(String contactNo) {
        this.contactNo[no] = contactNo;
    }

    // ------------------toString-------------------------------
    public String toString(int no) {
        return String.format("%-15s %-25s %-15s",
                getUsername(no), getEmail(no), this.contactNo[no]);
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

    // ------------------method-------------------------------

    // create organizer file if not exist
    public void createOrganizerFile() {
        try {
            File orgFile = new File("organizer.json");
            if (orgFile.createNewFile()) {
                System.out.println("Please Waiting...");
                System.out.println("Organizer file created: " + orgFile.getName());
            }
        } catch (IOException e) {
            System.out.println("Error creating organizer file: " + e.getMessage());
        }
    }

    // load the data from organizer.json
    public void readOrganizerData() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("organizer.json"));
            if (lines.isEmpty()) {
                no = 0;
            } else {
                // each record has 4 fields: username, password, email, contactNo
                no = (lines.size() / 4) - 1;
                String[][] information = new String[no + 1][4];

                for (int i = 0; i < lines.size(); i++) {
                    information[i / 4][i % 4] = lines.get(i);
                }

                for (int i = 0; i <= no; i++) {
                    setUsername(information[i][0]);
                    setPassword(information[i][1]);
                    setEmail(information[i][2]);
                    this.contactNo[i] = information[i][3];
                }
            }
        } catch (IOException e) {
            createOrganizerFile();
        }
    }

    // store organizer data to organizer.json
    public void storeOrganizerData() {
        try (Writer writer = new java.io.FileWriter("organizer.json", true)) {
            if (getUsername(no) != null && getEmail(no) != null && this.contactNo[no] != null) {
                writer.write(getUsername(no) + "\n");
                writer.write(getPassword(no) + "\n");
                writer.write(getEmail(no) + "\n");
                writer.write(this.contactNo[no] + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error storing organizer data: " + e.getMessage());
        }
    }

    // create a new organizer account
    public void createOrganizer(signUp signUpObj, String contactNo) {
        readOrganizerData();
        this.no++;
        setUsername(signUpObj.getSignUpName());
        setPassword(signUpObj.getSignUpPassword());
        setEmail(signUpObj.getSignUpEmail());
        this.contactNo[no] = contactNo;
        storeOrganizerData();
    }
}