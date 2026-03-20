import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;
import java.io.File;
import java.io.Writer;
import java.lang.String;

public class user {
    // instance variable
    private static String[] username = new String[100];
    private static String[] password = new String[100];
    private static String[] email = new String[100];
    // no is from 0 to 99
    private static int no = 0;

    // ------------------constructor-------------------------------
    // default constructor
    user() {

    }

    // parameterized constructor
    user(String username, String password, String email) {
        this.username[no] = username;
        this.password[no] = password;
        this.email[no] = email;
        this.no = no;
    }

    // ------------------getter-------------------------------
    public String getUsername(int no) {
        return this.username[no];
    }

    public String getPassword(int no) {
        return this.password[no];
    }

    public String getEmail(int no) {
        return this.email[no];
    }

    public int getNo() {
        return this.no;
    }

    // ------------------setter-------------------------------
    public void setUsername(String username) {
        this.username[no] = username;
    }

    public void setPassword(String password) {
        this.password[no] = password;
    }

    public void setEmail(String email) {
        this.email[no] = email;
    }

    // ------------------method-------------------------------

    // create user file if not exist
    public void createUserFile() {
        try {
            File user = new File("user.json");
            if (user.createNewFile()) {
                System.out.println("Please Waiting...");
                System.out.println("User file created: " + user.getName());
            }

        } catch (IOException e) {
            System.out.println("Error creating user file: " + e.getMessage());
        }
    }

    // load the data from user.json
    public void readUserData() {
        // Read user data from the file and populate the arrays
        try {
            List<String> lines = Files.readAllLines(Paths.get("user.json"));
            // check the file is empty or not
            if (lines.isEmpty()) {
                no = 0;

                // STORE Data
            } else {
                // get the index of last user
                no = (lines.size() / 3) - 1;
                // store data into 2D array
                String[][] information = new String[no + 1][3];

                for (int i = 0; i < lines.size(); i += 1) {

                    // for username
                    if (i % 3 == 0) {
                        information[i / 3][0] = lines.get(i);
                    }

                    // for password
                    if ((i) % 3 == 1) {
                        information[i / 3][1] = lines.get(i);
                    }

                    // for email
                    if ((i) % 3 == 2) {
                        information[(i / 3)][2] = lines.get(i);
                    }
                }

                for (int i = 0; i < no + 1; i++) {
                    this.username[i] = information[i][0];
                    this.password[i] = information[i][1];
                    this.email[i] = information[i][2];
                }

            } // create user file
        } catch (IOException e) {
            createUserFile();
        }

    }

    public void storeUserData() {
        // append the new user data to user.json
        // if user.json is not exist, create it first
        try (Writer writer = new java.io.FileWriter("user.json", true)) {
            // avoid store null data into file
            if (this.username[no] != null && this.password[no] != null && this.email[no] != null) {
                writer.write(this.username[no] + "\n");
                writer.write(this.password[no] + "\n");
                writer.write(this.email[no] + "\n");
            }

        } catch (IOException e) {
            System.out.println("Error storing user data: " + e.getMessage());
        }
    }

    public void createAccount(signUp signUpObj) {

        readUserData();
        this.no++;
        this.username[no] = signUpObj.getSignUpName();
        this.password[no] = signUpObj.getSignUpPassword();
        this.email[no] = signUpObj.getSignUpEmail();

        storeUserData();

    }

}