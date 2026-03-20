
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class TestUser {
    static Scanner scan = new Scanner(System.in);
    static Organizer organizer;
    static Event[] events = new Event[100];
    static Speaker[] speakerPool = new Speaker[100];
    static int speakerCount = 0;
    static int eventCount = 0;

    public static void main(String[] args) {
        // data store for user
        String[] username = new String[100];
        String[] password = new String[100];
        String[] email = new String[100];
        String[] contactNo = new String[100];

        // no set as array to pass the value and change the value (only reference
        // variable will be affect)
        int[] no = { 0 };
        // no is from 0 to 99
        // load data
        readUserData(no, username, password, email, contactNo);
        boolean active = true;
        while (active) {

            int option = displayAccessInterface();
            User user = new User();

            switch (option) {
                case 1:
                    // login
                    displayLoginInterface(user, username, password);

                    // set the access user
                    user.loginUser(email, contactNo);

                    accessmenu(user);
                    break;

                case 2:
                    // signup

                    displaySignUpInterface(username, user);
                    // set the access user
                    user.signUpUser();

                    // create and store data
                    createAccount(no, username, password, email, contactNo, user);
                    storeUserData(user.getSignUpName(), user.getSignUpPassword(), user.getSignUpEmail(),
                            user.getSignUpContactNo());

                    accessmenu(user);
                    break;
                case 0:
                    System.out.println("GoodBye!");
                    active = false;

            }


        }

    }

    // to select the method to access the system
    public static int displayAccessInterface() {
        Scanner scan = new Scanner(System.in);
        int option = 0;
        do {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("\n \t\t\tWelcome To Our System");
            System.out.println("------------------------------------------------------------------------------");
            System.out.print("\n \t\t\t1. Login");
            System.out.print("\n \t\t\t2. Sign Up");
            System.out.println("\n \t\t\t0. Exit");

            System.out.print("Enter Your Option:\t");
            option = scan.nextInt();

            if (option > 3 || option < 0) {
                System.out.println("Input Error: Please Select 1 or 2 !");
            }

        } while (option > 3 || option < 0);
        return option;
    }

    public static void displayLoginInterface(User user, String[] checkname, String[] checkpwd) {
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

    public static void displaySignUpInterface(String[] username, User user) {
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
            user.setSignUpEmail(email);
            ;

        } while (!user.validationEmail());

        do {

            System.out.println("Please enter your Contact Number: ");
            String contactNo = scan.nextLine();
            user.setSignUpContactNo(contactNo);

        } while (!user.validationContactNo());

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

    public static void accessmenu(User user){
            if (user.getAccessPassword().equals("12345")) {
                organizer = new Organizer(user.getAccessUsername(), user.getAccessPassword(), user.getAccessEmail(),user.getAccessContactNo());
                organizerMenu();

            }
    }

    // load the data from user.json
    public static void readUserData(int[] no, String[] username, String[] password, String[] email,
            String[] contactNo) {
        // Read user data from the file and populate the arrays
        try {
            List<String> lines = Files.readAllLines(Paths.get("user.json"));
            // check the file is empty or not
            if (lines.isEmpty()) {
                no[0] = 0;

                // STORE Data
            } else {
                // get the index of last user
                no[0] = (lines.size() / 4) - 1;
                System.out.println(no);
                // store data into 2D array
                String[][] information = new String[no[0] + 1][4];

                for (int i = 0; i < lines.size(); i += 1) {

                    // for username
                    if (i % 4 == 0) {
                        information[i / 4][0] = lines.get(i);
                    }

                    // for password
                    if ((i) % 4 == 1) {
                        information[i / 4][1] = lines.get(i);
                    }

                    // for email
                    if ((i) % 4 == 2) {
                        information[(i / 4)][2] = lines.get(i);
                    }
                    // for contact Number
                    if ((i) % 4 == 2) {
                        information[(i / 4)][3] = lines.get(i);
                    }

                }

                for (int i = 0; i < no[0] + 1; i++) {
                    username[i] = information[i][0];
                    password[i] = information[i][1];
                    email[i] = information[i][2];
                    contactNo[i] = information[i][3];

                }

            } // create user file
        } catch (IOException e) {
            createUserFile();
        }

    }

    public static void storeUserData(String username, String password, String email, String contactNo) {
        // append the new user data to user.json
        // if user.json is not exist, create it first
        try (Writer writer = new java.io.FileWriter("user.json", true)) {
            // avoid store null data into file
            if (username != null && password != null && email != null) {
                writer.write(username + "\n");
                writer.write(password + "\n");
                writer.write(email + "\n");
                writer.write(contactNo+"\n");
            }

        } catch (IOException e) {
            System.out.println("Error storing user data: " + e.getMessage());
        }
    }

    public static void createAccount(int[] no, String[] username, String[] password, String[] email, String[] contactNo,
            User user) {

        username[no[0]] = user.getSignUpName();
        password[no[0]] = user.getSignUpPassword();
        email[no[0]] = user.getSignUpEmail();
        email[no[0]] = user.getSignUpContactNo();
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

    // Organizer Part
    // enable organizer select their activity
    static void organizerMenu() {

        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║      ORGANIZER MENU          ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1: Create Event             ║");
            System.out.println("║  2: Update Event             ║");
            System.out.println("║  3: Create Session           ║");
            System.out.println("║  4: View My Events           ║");
            System.out.println("║  0: Back to Main Menu        ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print("Enter option: ");
            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    createEvent();
                    break;
                case 2:
                    updateEvent();
                    break;
                case 3:
                    // createSession();
                    break;
                case 4:
                    viewEvents();
                    break;
                case 0:
                    inMenu = false;

                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ── Create Event ──────────────────────────────────────────────────────────
    static void createEvent() {
        System.out.println("\n--- Create Event ---");
        System.out.println("Event type:");
        System.out.println("1: Concert");
        System.out.println("2: Conference");
        System.out.println("3: Workshop");
        System.out.print("Enter option: ");
        int type = scan.nextInt();
        scan.nextLine();

        // common fields — Event ID is auto-generated, not asked
        System.out.print("Title      : ");
        String title = scan.nextLine();
        System.out.print("Date       : ");
        String date = scan.nextLine();
        System.out.print("Venue      : ");
        String venue = scan.nextLine();
        System.out.print("Max Tickets: ");
        int maxTix = scan.nextInt();
        scan.nextLine();

        if (type == 1) {
            // Concert — no extra fields
            events[eventCount++] = new Concert(title, date, venue, maxTix);

        } else if (type == 2) {
            // Conference
            events[eventCount++] = new Conference(title, date, venue, maxTix);

        } else if (type == 3) {
            events[eventCount++] = new Workshop(title, date, venue, maxTix);

        } else {
            System.out.println("Invalid event type.");
            return;
        }
        // show the auto-generated ID to the organizer
        System.out.println("Event created! ID assigned: " + events[eventCount - 1].getEventID());
    }

    // ── Update Event ─────────────────────────────────────────────────────────
    static void updateEvent() {
        if (eventCount == 0) {
            System.out.println("No events available to update.");
            return;
        }

        System.out.println("\n--- Update Event ---");
        listEvents();
        System.out.print("Select event number: ");
        int idx = scan.nextInt() - 1;
        scan.nextLine();

        if (idx < 0 || idx >= eventCount) {
            System.out.println("Invalid selection.");
            return;
        }

        Event e = events[idx];
        System.out.println("What to update?");
        System.out.println("1: Title");
        System.out.println("2: Date");
        System.out.println("3: Venue");
        System.out.println("4: Max Tickets");

        System.out.print("Enter option: ");
        int field = scan.nextInt();
        scan.nextLine();

        switch (field) {
            case 1:
                System.out.print("New Title      : ");
                e.setTitle(scan.nextLine());
                System.out.println("Title updated.");
                break;
            case 2:
                System.out.print("New Date       : ");
                e.setDate(scan.nextLine());
                System.out.println("Date updated.");
                break;
            case 3:
                System.out.print("New Venue      : ");
                e.setVenue(scan.nextLine());
                System.out.println("Venue updated.");
                break;
            case 4:
                System.out.print("New Max Tickets: ");
                e.setMaxTickets(scan.nextInt());
                scan.nextLine();
                System.out.println("Max tickets updated.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    // ── Create Session (Conference only) ─────────────────────────────────────
    /*
     * static void createSession() {
     * if (eventCount == 0) {
     * System.out.println("No events available.");
     * return;
     * }
     * 
     * System.out.println("\n--- Create Session (Conference only) ---");
     * boolean hasConference = false;
     * for (int i = 0; i < eventCount; i++) {
     * if (events[i] instanceof Conference) {
     * System.out.println("  " + (i + 1) + ": [" + events[i].getEventID() + "] " +
     * events[i].getTitle());
     * hasConference = true;
     * }
     * }
     * if (!hasConference) {
     * System.out.
     * println("No Conference events found. Create a Conference event first.");
     * return;
     * }
     * 
     * System.out.print("Select Conference number: ");
     * int idx = scan.nextInt() - 1;
     * scan.nextLine();
     * 
     * if (idx < 0 || idx >= eventCount || !(events[idx] instanceof Conference)) {
     * System.out.println("Invalid selection or not a Conference.");
     * return;
     * }
     * 
     * Conference conf = (Conference) events[idx];
     * 
     * // Session ID is auto-generated — only ask topic and time
     * System.out.print("Topic : ");
     * String topic = scan.nextLine();
     * System.out.print("Time  : ");
     * String time = scan.nextLine();
     * Session sess = conf.createSession(topic, time);
     * 
     * if (sess != null) {
     * System.out.println("Session created! ID assigned: " + sess.getSessionID());
     * 
     * // assign speakers from pool
     * if (speakerCount > 0) {
     * System.out.println("Available Speakers:");
     * for (int k = 0; k < speakerCount; k++) {
     * System.out.println("  " + (k + 1) + ": "
     * + speakerPool[k].getUsername(0));
     * }
     * System.out.print("How many speakers to assign? ");
     * int numSp = scan.nextInt();
     * scan.nextLine();
     * for (int sp = 0; sp < numSp; sp++) {
     * System.out.print("  Select speaker: ");
     * int spOpt = scan.nextInt();
     * scan.nextLine();
     * if (spOpt >= 1 && spOpt <= speakerCount) {
     * sess.assignSpeaker(speakerPool[spOpt - 1]);
     * } else {
     * System.out.println("Invalid speaker selection.");
     * }
     * }
     * } else {
     * System.out.
     * println("No speakers registered yet. Session created without speakers.");
     * }
     * 
     * allSessions[sessionCount++] = sess;
     * }
     * }
     */

    // ── View Events ───────────────────────────────────────────────────────────
    static void viewEvents() {
        if (eventCount == 0) {
            System.out.println("No events created yet.");
            return;
        }
        System.out.println("\n--- All Events ---");
        for (int i = 0; i < eventCount; i++) {
            events[i].displayInfo();
            System.out.println();
        }
    }

    // helper: print numbered event list
    static void listEvents() {
        for (int i = 0; i < eventCount; i++) {
            System.out.println("  " + (i + 1) + ": [" + events[i].getEventID() + "] "
                    + events[i].getTitle()
                    + " (" + events[i].getClass().getSimpleName() + ")"
                    + " | MaxTickets: " + events[i].getMaxTickets());
        }
    }
    // Speaker part
    /*
     * static void speakerMenu() {
     * System.out.println("\n--- Speaker Login / Register ---");
     * System.out.println("1: Register as new Speaker");
     * System.out.println("2: Login as existing Speaker");
     * System.out.print("Enter option: ");
     * int loginChoice = scan.nextInt();
     * scan.nextLine();
     * 
     * String loggedInUsername = null;
     * Speaker loggedInSpeaker = null;
     * 
     * if (loginChoice == 1) {
     * System.out.print("Username  : ");
     * String username = scan.nextLine();
     * System.out.print("Password  : ");
     * String password = scan.nextLine();
     * System.out.print("Email     : ");
     * String email = scan.nextLine();
     * System.out.print("Bio       : ");
     * String bio = scan.nextLine();
     * Speaker sp = new Speaker(username, password, email, bio);
     * speakerPool[speakerCount++] = sp;
     * loggedInUsername = username;
     * loggedInSpeaker = sp;
     * System.out.println("Speaker registered successfully!");
     * 
     * } else if (loginChoice == 2) {
     * System.out.print("Username : ");
     * String username = scan.nextLine();
     * System.out.print("Password : ");
     * String password = scan.nextLine();
     * for (int i = 0; i < speakerCount; i++) {
     * if (speakerPool[i].getUsername(0) != null
     * && speakerPool[i].getUsername(0).equals(username)
     * && speakerPool[i].getPassword(0) != null
     * && speakerPool[i].getPassword(0).equals(password)) {
     * loggedInUsername = username;
     * loggedInSpeaker = speakerPool[i];
     * break;
     * }
     * }
     * if (loggedInSpeaker == null) {
     * System.out.println("Invalid username or password.");
     * return;
     * }
     * System.out.println("Welcome back, " + loggedInUsername + "!");
     * 
     * } else {
     * System.out.println("Invalid option.");
     * return;
     * }
     * 
     * boolean inMenu = true;
     * while (inMenu) {
     * System.out.println("\n╔══════════════════════════════╗");
     * System.out.println("║       SPEAKER MENU           ║");
     * System.out.println("║  Logged in: " + String.format("%-17s",
     * loggedInUsername) + "║");
     * System.out.println("╠══════════════════════════════╣");
     * System.out.println("║  1: Update Bio               ║");
     * System.out.println("║  2: Update Session Topic     ║");
     * System.out.println("║  3: View My Info             ║");
     * System.out.println("║  0: Back to Main Menu        ║");
     * System.out.println("╚══════════════════════════════╝");
     * System.out.print("Enter option: ");
     * int choice = scan.nextInt();
     * scan.nextLine();
     * 
     * switch (choice) {
     * case 1:
     * //uploadBio(loggedInSpeaker, loggedInUsername);
     * break;
     * case 2:
     * //uploadSessionTopic(loggedInSpeaker, loggedInUsername);
     * break;
     * case 3:
     * /* System.out.println("\n--- Your Info ---");
     * System.out.printf("%-15s %-25s %-30s%n", "Username", "Email", "Bio");
     * System.out.println(
     * "--------------------------------------------------------------------");
     * System.out.println(loggedInSpeaker.toString(0));
     * break;
     * case 0:
     * inMenu = false;
     * break;
     * default:
     * System.out.println("Invalid option. Try again.");
     * }
     * }
     * }
     */
}
