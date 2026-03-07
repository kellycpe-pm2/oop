
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class TestUser {

    public static void main(String[] args) {
        // data store for user
        String[] username = new String[100];
        String[] password = new String[100];
        String[] email = new String[100];
        int [] no = {0};
        // no is from 0 to 99
        // load data
        readUserData(no, username, password, email);
        int option = displayAccessInterface();
        User user=new User();
        switch (option) {
            case 1:
                displayLoginInterface(user,username,password);
                user.loginUser();

                break;

            case 2:
                displaySignUpInterface( username,user);
                user.signUpUser();

                // create and store data
                createAccount(no, username, password, email, user);
                storeUserData(user.getSignUpName(),user.getSignUpPassword(),user.getSignUpEmail());
                
        }

    }

    // to select the method to access the system
    public static int displayAccessInterface() {
        Scanner scan = new Scanner(System.in);
        int option = 0;
        do {
            System.out.println("------------------------------------------------------------------------------");
            System.out.printf("\n \t\t\t%-1s \n \n", "Welcome To Our System");
            System.out.println("------------------------------------------------------------------------------");
            System.out.printf("\n \t\t\t   %-10s \n", "1. Login");
            System.out.printf("\n \t\t\t   %-10s \n\n", "2. Sign Up");
            System.out.print("Enter Your Option:\t");
            option = scan.nextInt();

            if (option > 2 || option == 0) {
                System.out.println("Input Error: Please Select 1 or 2 !");
            }

        } while (option > 2 || option == 0);
        return option;
    }

    public static void displayLoginInterface(User user, String[]checkname,String []checkpwd) {
        Scanner scan = new Scanner(System.in);

        System.out.println("-------------------------- Login --------------------------");

        do {

            System.out.println("Please enter your name: ");
            String name = scan.nextLine();
            user.setLoginUsername(name);

        } while (!user.validationNoExistName(checkname));

        do {
            
            System.out.println("Please enter your Password: ");
            String password = scan.nextLine();
            user.setLoginPassword(password);

        } while (!user.validationLoginPwd(checkpwd));

        System.out.println("------------------------------------------------------------");

    }

    public static void displaySignUpInterface( String[] username, User user) {
        Scanner scan = new Scanner(System.in);

        System.out.println("--------------------------Sign Up--------------------------");

        do {

            System.out.println("Please enter your name: ");
            String name = scan.nextLine();
            user.setSignUpName(name);
        } while (!user.validationName() || !user.validationExist(username));

        do {

            System.out.println("Please enter your email: ");
            String email = scan.nextLine();
            user.setSignUpEmail(email);;

        } while (!user.validationEmail());

        do {

            System.out.println("Please enter your Password: ");
            String password = scan.nextLine();
            user.setSignUpPassword(password);

        } while (!user.validationPassword());

        do {

            System.out.println("Please enter your comfirm password: ");
            String password2 = scan.nextLine();
            user.setSignUpPassword2(password2);

        } while (!user.validationPassword2());

        System.out.println("------------------------------------------------------------");
    }

    // load the data from user.json
    public static void readUserData(int [] no, String[] username, String[] password, String[] email) {
        // Read user data from the file and populate the arrays
        try {
            List<String> lines = Files.readAllLines(Paths.get("user.json"));
            // check the file is empty or not
            if (lines.isEmpty()) {
                no[0] = 0;

                // STORE Data
            } else {
                // get the index of last user
                no[0] = (lines.size() / 3) - 1;
                System.out.println(no);
                // store data into 2D array
                String[][] information = new String[no[0] + 1][3];

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

                for (int i = 0; i < no[0] + 1; i++) {
                    username[i] = information[i][0];
                    password[i] = information[i][1];
                    email[i] = information[i][2];
                }

            } // create user file
        } catch (IOException e) {
            createUserFile();
        }

    }

    public static void storeUserData(String username, String password, String email) {
        // append the new user data to user.json
        // if user.json is not exist, create it first
        try (Writer writer = new java.io.FileWriter("user.json", true)) {
            // avoid store null data into file
            if (username != null && password != null && email != null) {
                writer.write(username + "\n");
                writer.write(password + "\n");
                writer.write(email + "\n");
            }

        } catch (IOException e) {
            System.out.println("Error storing user data: " + e.getMessage());
        }
    }

    public static void createAccount(int []no, String[] username, String[] password, String[] email, User user) {

        username[no[0]] = user.getSignUpName();
        password[no[0]] = user.getSignUpPassword();
        email[no[0]] = user.getSignUpEmail();
        no[0]++;
    }

    // create user file if not exist
    public static void createUserFile() {
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
}
