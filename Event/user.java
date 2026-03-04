package Event;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;
import java.io.File;
import java.io.Writer;

public class user {
    // instance variable
    private static String[] username = new String[100];
    private static String[] password = new String[100];
    private static String[] email = new String[100];
    //no is from 0 to 99
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
        return username[no];
    }

    public String getPassword(int no) {
        return password[no];
    }

    public String getEmail(int no) {
        return email[no];
    }

    public int getNo() {
        return no;
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

    public void readUserData() {
        createUserFile();
        // Read user data from the file and populate the arrays
        try {
            String filePath = "user.json";
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            int i=0;
            no = lines.size() / 5; 
            for (String line : lines) {
                line = line.strip();
                if (line.contains("  \"username\": \"")) {
                    username[i] = line.split(":")[1].trim().replace("\"", "").replace(",", "");                
                } else if (line.contains("  \"password\": \"")) {
                    password[i] = line.split(":")[1].trim().replace("\"", "").replace(",", "");
                } else if (line.contains("  \"email\": \"")) {
                    email[i] = line.split(":")[1].trim().replace("\"", "").replace(",", "");    
                    i++;
                }
                
                

            }
        } catch (IOException e) {
            System.out.println("Error reading user data: " + e.getMessage());
        }
        
    }

    public void storeUserData() {
        readUserData();
        try (Writer writer = new java.io.FileWriter("user.json",true)) {
            // Write user data to the file in JSON format
            for (int i = 0; i < no; i++) {
                if (this.username[i] != null && this.password[i] != null && this.email[i] != null) {
                    writer.write("{\n");
                    writer.write("  \"username\": \"" + this.username[i] + "\",\n");
                    writer.write("  \"password\": \"" + this.password[i] + "\",\n");
                    writer.write("  \"email\": \"" + this.email[i] + "\"\n");
                    writer.write("}\n");
                }
                
            }
        } catch (IOException e) {
            System.out.println("Error storing user data: " + e.getMessage());
        }
    }

    public void createAccount(signUp signUpObj) {
        if (signUpObj.getResult() == true) {
            this.username[no] = signUpObj.getSignUpName();
            this.password[no] = signUpObj.getSignUpPassword();
            this.email[no] = signUpObj.getSignUpEmail();
            this.no++;
            storeUserData();
            
        }
        
        this.username[no] = signUpObj.getSignUpName();
        this.password[no] = signUpObj.getSignUpPassword();
        this.email[no] = signUpObj.getSignUpEmail();
        this.no++;
        storeUserData();
        

    }

}
