import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.swing.text.View;

public class TestUser {
    static Scanner scan = new Scanner(System.in);
    static EventManagementSystem ems = new EventManagementSystem();

    static List<TicketType> ticketTypes = new java.util.ArrayList<>();
    static List<Ticket> tickets = new java.util.ArrayList<>();

    // in-memory lists — loaded from / saved to JSON files
    static List<Concert> concerts = new java.util.ArrayList<>();
    static List<Workshop> workshops = new java.util.ArrayList<>();
    static List<Conference> conferences = new java.util.ArrayList<>();

    // flat array used by attendee ticket purchasing (same pattern as TestUser)
    static Event[] events = new Event[300];
    static int eventCount = 0;

    // speaker pool
    static Speaker[] speakerPool = new Speaker[100];
    static int speakerCount = 0;

    static Organizer organizer;
    static Attendee attendee;
    
        public static void main(String[] args) {
        // data store for user

        User[][] alluser = new User[4][100];
        // no set as array to pass the value and change the value (only reference
        // variable will be affect)
        int[] no = { 0, 0, 0, 0 };

        // no is from 0 to 99
        // load data

        readUserData(no, alluser);
        loadAllEvents(); // load all events and ticket types from files on startup

        boolean active = true;
        while (active) {
            int option = displayAccessInterface();
            User user = new User();

            switch (option) {
                case 1:
                    // login
                    if (!displayLoginInterface(user, alluser, no)) {
                        break;
                    }
                    // set the access user
                    accessmenu(user, alluser, no);
                    break;

                case 2:
                    // signup

                    displaySignUpInterface(user, alluser, no);
                    // set the access user
                    user.signUpUser();

                    // create and store data
                    createAccount(no, user, alluser);
                    storeUserData(user.getSignUpName(), user.getSignUpPassword(), user.getSignUpEmail(),
                            user.getSignUpContactNo());

                    accessmenu(user, alluser, no);
                    break;
                case 0:
                    System.out.println("GoodBye!");
                    active = false;
                    break;
                default:
                    System.out.println("Error: Please Select The Correct Number");

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

    public static boolean displayLoginInterface(User user, User[][] alluser, int[] no) {
        Scanner scan = new Scanner(System.in);
        int logincount = 0;

        System.out.println("-------------------------- Login --------------------------");
        do {

            logincount++;
            System.out.print("\nPlease enter your name: ");
            String name = scan.nextLine();
            user.setLoginUsername(name);
            if (logincount > 2) {
                return false;
            }
        } while (!user.validationNoExistName(alluser, no));

        do {

            System.out.print("\nPlease enter your Password: ");
            String password = scan.nextLine();
            user.setLoginPassword(password);

        } while (!user.validationLoginPwd(alluser));

        System.out.println("------------------------------------------------------------");
        return true;
    }

    public static void displaySignUpInterface(User user, User[][] alluser, int[] no) {
        Scanner scan = new Scanner(System.in);

        System.out.println("--------------------------Sign Up--------------------------");

        do {

            System.out.print("\nPlease enter your name: ");
            String name = scan.nextLine();
            user.setSignUpName(name);
        } while (!user.validationName() || !user.validationExist(alluser));

        do {

            System.out.print("\nPlease enter your email: ");
            String email = scan.nextLine();
            user.setSignUpEmail(email);
            ;

        } while (!user.validationEmail());

        do {

            System.out.print("\nPlease enter your Contact Number [eg. 01113018399]: ");
            String contactNo = scan.nextLine();
            user.setSignUpContactNo(contactNo);

        } while (!user.validationContactNo());

        do {

            System.out.print("\nPlease enter your Password: ");
            String password = scan.nextLine();
            user.setSignUpPassword(password);

        } while (!user.validationPassword());

        do {

            System.out.print("\nPlease enter your comfirm password: ");
            String password2 = scan.nextLine();
            user.setSignUpPassword2(password2);

        } while (!user.validationPassword2());

        System.out.println("------------------------------------------------------------");
    }

    public static void accessmenu(User user, User[][] alluser, int[] no) {
        // Check if user is organizer (password "12345")
        if (user.getAccessPassword().equals("12345")) {

            Organizer organizer = (Organizer) alluser[0][user.getno()];

            System.out.println(organizer.toString());
            waitForEnter();
            organizerMenu();
        }

        // Check if user is a speaker (exists in speakerPool)
        else if (user.getAccessPassword().equals("54321")) {
            Speaker speaker = (Speaker) alluser[1][user.getno()];
            System.out.println(speaker.toString());
            waitForEnter();
            speakerMenu(speaker, events);
        }
        // Otherwise, user is an attendee
        else if (user.getAccessPassword().equals("13148")) {
            Staff staff = (Staff) alluser[3][user.getno()];
            System.out.println(staff.toString());
            waitForEnter();
            staffMenu(staff, alluser, no);
        } else {
            Attendee attendee = (Attendee) alluser[2][user.getno()];
            System.out.println(attendee.toString());
            waitForEnter();
            attendeeMenu(attendee);
        }
    }

    // load the data from user.json
    public static void readUserData(int[] no, User[][] alluser) {

        // Read user data from the file and populate the arrays
        try {

            List<String> lines = Files.readAllLines(Paths.get("user.json"));
            // check the file is empty or not
            if (lines.isEmpty()) {

                // STORE Data
            } else {
                int size = (lines.size() / 4);
                // store data into 2D array
                String[][] information = new String[size][4];

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
                    if ((i) % 4 == 3) {
                        information[(i / 4)][3] = lines.get(i);
                    }

                }

                for (int i = 0; i < size; i++) {

                    String current_pwd = information[i][1];
                    // to store different user type data
                    if (current_pwd.equals("12345")) {

                        // get the no of last user

                        alluser[0][no[0]] = new Organizer(information[i][0], information[i][1], information[i][2],
                                information[i][3]);
                        no[0]++;
                    } else if (current_pwd.equals("54321")) {

                        alluser[1][no[1]] = new Speaker(information[i][0], information[i][1], information[i][2],
                                information[i][3]);
                        no[1]++;

                    } else if (current_pwd.equals("13148")) {
                        alluser[3][no[3]] = new Staff(information[i][0], information[i][1], information[i][2],
                                information[i][3]);
                        no[3]++;
                    } else {

                        alluser[2][no[2]] = new Attendee(information[i][0], information[i][1], information[i][2],
                                information[i][3]);
                        no[2]++;
                    }

                }

                // get the latest no index for each user type
                for (int i = 0; i < 0; i++) {

                    // if no any record , do not need to minus
                    if (no[i] != 0) {

                        no[i]--;

                    }
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
                writer.write(contactNo + "\n");
            }

        } catch (IOException e) {
            System.out.println("Error storing user data: " + e.getMessage());
        }
    }

    public static void createAccount(int[] no, User user, User[][] alluser) {

        String username = user.getSignUpName();
        String password = user.getSignUpPassword();
        String email = user.getSignUpEmail();
        String contactNo = user.getSignUpContactNo();
        //
        if (password.equals("12345")) {

            alluser[0][no[0]] = new Organizer(username, password, email, contactNo);
            user.setNo(no[0]);
            no[0]++;

        } else if (password.equals("54321")) {

            alluser[1][no[1]] = new Speaker(username, password, email, contactNo);
            user.setNo(no[1]);
            no[1]++;
        } else if (password.equals("13148")) {

            alluser[3][no[3]] = new Staff(username, password, email, contactNo);
            user.setNo(no[3]);
            no[3]++;

        } else {

            alluser[2][no[2]] = new Attendee(username, password, email, contactNo);
            user.setNo(no[2]);
            no[2]++;

        }

    }

    // create user file if not exist
    public static void createUserFile() {
        try {
            File user = new File("user.json");
            if (user.createNewFile()) {
                System.out.println("Please Waiting...");
                System.out.println("User file created: User.json");
            }

        } catch (IOException e) {
            System.out.println("Error creating user file: " + e.getMessage());
        }
    }

    // additional function
    public static void waitForEnter() {
        try {
            scan.nextLine(); // Reads a single byte
            System.in.skip(System.in.available()); // Clear buffer
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearScreen() {
        try {
            // Try ANSI escape codes first
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            // Fallback to blank lines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    // _________________________________________________________________________
    // Staff Part
    // _________________________________________________________________________
    public static void staffMenu(Staff staff, User[][] alluser, int[] no) {
        boolean inMenu = true;
        clearScreen();

        try {

            while (inMenu) {
                System.out.println("\n╔════════════════════════════════════════════════════════════╗");
                System.out.println("║                      STAFF Menu                            ║");
                System.out.println("╠════════════════════════════════════════════════════════════╣");
                System.out.println("║  🎟️  CHECK-IN MENU:                                         ║");
                System.out.println("║     1. Check-in Attendee by Ticket ID                      ║");
                System.out.println("║     2. View Check-in List                                  ║");
                System.out.println("║     3. View Pending Attendees List                         ║");
                System.out.println("╠════════════════════════════════════════════════════════════╣");
                System.out.println("║  📊 REPORT MENU:                                           ║");
                System.out.println("║     4. View Event Report                                   ║");
                System.out.println("║     5. View Sales Report                                   ║");
                System.out.println("║     6. View All Check-ins Report                           ║");
                System.out.println("╠════════════════════════════════════════════════════════════╣");
                System.out.println("║  📁 EXPORT MENU:                                           ║");
                System.out.println("║     7. Export Check-in Report                              ║");
                System.out.println("║     8. Export Sales Report                                 ║");
                System.out.println("║     9. Export Event Report                                 ║");
                System.out.println("╠════════════════════════════════════════════════════════════╣");
                System.out.println("║     0. Exit                                                ║");
                System.out.println("╚════════════════════════════════════════════════════════════╝");
                System.out.print("Enter option: ");
                int choice = scan.nextInt();

                switch (choice) {
                    case 1:
                        checkIn_Attendee(alluser, no);
                        break;
                    case 2:
                        view_checkin(alluser);
                        break;
                    case 3:
                        view_pending_attendee_list(alluser);
                        break;
                    case 4:
                    event_report();
                        break;
                    case 5:
                    sale_report();
                        break;
                    case 6:
                        all_check_in_report(alluser);
                        break;
                    case 7:
                     exportCheckInReportToFile(alluser);
                        break;
                    case 8:                        
                        exportSalesReportToFile();
                    break;
                    case 9:
                        exportEventReportToFile();
                    break;
                    case 0:
                        inMenu = false;
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }

            }
        } catch (Exception e) {
            System.out.println("Error: Existing Unknown Char. Please Input Correctly!!!");
            scan.nextLine();
            inMenu = true;
            waitForEnter();
        } finally {
            if (inMenu) {
                staffMenu(staff, alluser, no);

            }

        }
    }


    public static void checkIn_Attendee(User[][] alluser, int[] no) {
        Scanner scan = new Scanner(System.in);
        User current_attendee = null;
        Ticket currentTicket = null;
        boolean search = true;
    
        clearScreen();
        System.out.println("\n\t\t╔════════════════════════════════════════════════════════════╗");
        System.out.println("\t\t║                   CHECK-IN ATTENDEE                        ║");
        System.out.println("\t\t╚════════════════════════════════════════════════════════════╝\n\n");
        System.out.println("\t\t        1. Ticket ID       2. Booking ID      0. Exit");
    
        int option = 0;
    
        // Get valid option
        try {
        do {
            System.out.print("\n\t\tEnter Your Option: ");
            option = scan.nextInt();
            scan.nextLine();
        } while (option > 2 || option < 0);
        } catch (Exception e) {
            System.out.println("\t\tError: Invalid Character!!! Please enter number.");
            scan.nextLine(); // Clear buffer
            checkIn_Attendee(alluser, no);
            return;
    }
    
        if (option == 0) {
            return; // Exit
    }
    
        // Process based on option
        switch (option) {
            case 1:
                System.out.print("\n\t\tEnter Ticket ID: ");
                String ticketId = scan.nextLine();
                boolean ticketFound = false;
            
            for (Ticket ticket : tickets) {
                if (ticket.getTicketId().equals(ticketId)) {
                    ticketFound = true;

                    
                    // Find attendee by buyer name
                    for (int i = 0; i < no[2]; i++) {
                        if (alluser[2][i] != null && 
                            ticket.getBuyerName().equals(alluser[2][i].getAccessUsername())) {
                            current_attendee = alluser[2][i];
                            currentTicket = ticket;
                            search = false;
                            break;
                        }
                    }
                    
                    if (current_attendee == null) {
                        System.out.println("\t\tERROR: Attendee Information Not Found!");
                        waitForEnter();
                        checkIn_Attendee(alluser, no);
                        return;
                    }
                    break;
                }
            }
            
            if (!ticketFound) {
                System.out.println("\t\tERROR: Ticket ID Not Found!");
                waitForEnter();
                checkIn_Attendee(alluser, no);
                return;
            }
            break;
            
        case 2:
            System.out.print("\n\t\tEnter Booking ID: ");
            String bookingId = scan.nextLine();
            boolean bookingFound = false;
            
            for (Ticket ticket : tickets) {
                if (ticket.getBookingId().equals(bookingId)) {
                    bookingFound = true;
                    currentTicket = ticket;
                    // Find attendee by buyer name
                    for (int i = 0; i < no[2]; i++) {
                        if (alluser[2][i] != null && 
                            ticket.getBuyerName().equals(alluser[2][i].getAccessUsername())) {
                            current_attendee = alluser[2][i];
                            search = false;
                            break;
                        }
                    }
                    
                    if (current_attendee == null) {

                        System.out.println("\t\tERROR: Attendee Information Not Found!");
                        waitForEnter();
                        checkIn_Attendee(alluser, no);
                        return;
                    }
                    break;
                }
            }
            
            if (!bookingFound) {
                System.out.println("\t\tERROR: Booking ID Not Found!");
                waitForEnter();
                checkIn_Attendee(alluser, no);
                return;
            }
            break;
    }

            // Check if already checked in
        if (!currentTicket.getStatus()) {
            System.out.println("\n\t\t╔════════════════════════════════════════════════════════════╗");
            System.out.println("\t\t║                  ❌ ALREADY CHECKED IN!                     ║");
            System.out.println("\t\t╚════════════════════════════════════════════════════════════╝");
            waitForEnter();
            checkIn_Attendee(alluser, no);
            return;
        }
        
       System.out.printf(
        "╔════════════════════════════════════════════════════════════╗\n" +
        "║                      CHECK-IN DETAILS                      ║\n" +
        "╠════════════════════════════════════════════════════════════╣\n" +
        "║          Name           :  %-31s ║\n" +
        "║          Email          :  %-31s ║\n" +
        "║          Contact No     :  %-31s ║\n" +
        "║          Booking ID     :  %-31s ║\n" +
        "║          Ticket ID      :  %-31s ║\n" +
        "║          Ticket Type    :  %-31s ║\n" +
        "║          Seat No        :  %-31s ║\n" +
        "║          Status         :  %-31s ║\n" +
        "║          Purchase Date  :  %-31s ║\n" +
        "╚════════════════════════════════════════════════════════════╝\n",
        current_attendee.getAccessUsername(),
        current_attendee.getAccessEmail(),
        current_attendee.getAccessContactNo(),
        currentTicket.getBookingId(),
        currentTicket.getTicketId(),
        currentTicket.getTicketType(),
        currentTicket.getSeatNum(),
        status_ToString(currentTicket.getStatus()),
        currentTicket.getPurchasedDate()
    ); 


    System.out.println("\n     Press Enter Key To Continue");
        waitForEnter();
        System.out.println("\n\t\t╔════════════════════════════════════════════════════════════╗");
        System.out.println("\t\t║                   CONFIRM CHECK-IN                         ║");
        System.out.println("\t\t╚════════════════════════════════════════════════════════════╝\n\n");
        System.out.println("\t\t          1. Yes                        0. No");
        
        

        try {
        do {
            System.out.print("\n\t\tEnter Your Option: ");
            option = scan.nextInt();
            scan.nextLine();
        } while (option > 2 || option < 0);
        } catch (Exception e) {
            System.out.println("\t\tError: Invalid Character!!! Please enter number.");
            scan.nextLine(); // Clear buffer
            checkIn_Attendee(alluser, no);
            return;
    }


    
        if (option == 1) {
            currentTicket.setStatus(false);
            Staff.increase_CheckIn_Couter();
            checkIn_Attendee(alluser, no); // Exit
    }else{
        return;
    }


    }


    public static void view_checkin(User [] []alluser){
        System.out.println("\n\t\t╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("\t\t║                           CHECK-IN LIST                                        ║");
        System.out.printf("\t\t║                               %-48s ║\n",LocalDate.now());
        System.out.println("\t\t╚════════════════════════════════════════════════════════════════════════════════╝");
        if (Staff.getCheckin_Couter()==0) {
        System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("\t\t\t│                         ⚠️ NO CHECK-INS TODAY                                  │");
        System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘");
        return;
        }else{
        System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────┐");
        System.out.println("\t\t\t│                            CHECKED-IN LIST                         │");
        System.out.println("\t\t\t├────┬────────────────────┬─────────────────────┬──────────┬─────────┤");
        System.out.println("\t\t\t│ No │      Name          │        Email        │  Ticket  │ Status  │");
        System.out.println("\t\t\t├────┼────────────────────┼─────────────────────┼──────────┼─────────┤");    
        int no=1;

        for (Ticket ticket : tickets){
            if (ticket !=null){
            
                if (alluser[2].length==0){
                    System.out.println("ERROR: No Any Attendee Resgister The System !!!");
                    return;
                }
            if(!ticket.getStatus()){
                for(User user : alluser[2]){
                    if (ticket.getBuyerName().equals(user.getAccessUsername())){
                        System.out.printf("\t\t\t│ %-2d │ %-18s │ %-19s │ %-8s │ %-8s│\n",
                        no++,
                        ticket.getBuyerName(),
                        user.getAccessEmail(),
                        ticket.getTicketId(),
                        status_ToString(ticket.getStatus()));
                        break;
                    }
                
                }
            }

        


            System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────┘");
            System.out.println("\n\n\t\t\t Press Enter Key To Return Menu");
            waitForEnter();
            waitForEnter();
        }
        }
    }


    }

    public static void view_pending_attendee_list(User[][]alluser){
       System.out.println("\n\t\t╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("\t\t║                           PENDING ATTENDEES LIST                               ║");
        System.out.printf("\t\t║                               %-48s ║\n",LocalDate.now());
        System.out.println("\t\t╚════════════════════════════════════════════════════════════════════════════════╝");
        if (tickets.size()==0) {
        System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("\t\t\t│                                  NO ANY EVENT                                  │");
        System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘");
        return;
        }else{
        System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────┐");
        System.out.println("\t\t\t│                     PENDING ATTENDEES LIST                         │");
        System.out.println("\t\t\t├────┬────────────────────┬─────────────────────┬──────────┬─────────┤");
        System.out.println("\t\t\t│ No │      Name          │        Email        │  Ticket  │ Status  │");
          
        int no=1;

        for (Ticket ticket : tickets){
            if (ticket !=null){
            
                if (alluser[2].length==0){
                    System.out.println("ERROR: No Any Attendee Resgister The System !!!");
                    return;
                }
            if(ticket.getStatus()){
                for(User user : alluser[2]){
                    if (ticket.getBuyerName().equals(user.getAccessUsername())){
                        System.out.println("\t\t\t├────┼────────────────────┼─────────────────────┼──────────┼─────────┤"); 
                        System.out.printf("\t\t\t│ %-2d │ %-18s │ %-19s │ %-8s │ %-8s│\n",
                        no++,
                        ticket.getBuyerName(),
                        user.getAccessEmail(),
                        ticket.getTicketId(),
                        status_ToString(ticket.getStatus()));
                        break;
                    }
                
                }
            }

        


            
        }
        }
            System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────┘");
            System.out.println("\n\n\t\t\t\t Press Enter Key To Return Menu");
            waitForEnter();
            waitForEnter();
    }

    }


    public static void all_check_in_report(User [][]alluser){
       System.out.println("\n\t\t╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("\t\t║                              ALL CHECK-INS REPORT                              ║");
        System.out.printf("\t\t║                              Generated :%-38s ║\n",LocalDate.now());
        System.out.printf("\t\t║                               Total Check-ins : %-30s ║\n",tickets.size());
        System.out.println("\t\t╚════════════════════════════════════════════════════════════════════════════════╝");
        if (tickets.size()==0) {
        System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("\t\t\t│                                  NO ANY BUYER                                  │");
        System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘");
        return;
        }else{
        System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────┐");
        System.out.println("\t\t\t│                       ALL CHECK-INS REPORT                         │");
        System.out.println("\t\t\t├────┬────────────────────┬─────────────────────┬──────────┬─────────┤");
        System.out.println("\t\t\t│ No │      Name          │        Email        │  Ticket  │ Status  │");   
        int no=1;

        for (Ticket ticket : tickets){
            if (ticket !=null){
            
                if (alluser[2].length==0){
                    System.out.println("ERROR: No Any Attendee Resgister The System !!!");
                    return;
                }
                for(User user : alluser[2]){
                    if (ticket.getBuyerName().equals(user.getAccessUsername())){
                        System.out.println("\t\t\t├────┼────────────────────┼─────────────────────┼──────────┼─────────┤"); 
                        System.out.printf("\t\t\t│ %-2d │ %-18s │ %-19s │ %-8s │ %-8s│\n",
                        no++,
                        ticket.getBuyerName(),
                        user.getAccessEmail(),
                        ticket.getTicketId(),
                        status_ToString(ticket.getStatus()));
                        break;
                    }
                
                }
            

            }
        }


            System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────┘");
            System.out.println("\n\n\t\t\t\t Press Enter Key To Mone On To Next Page");
            waitForEnter();
            waitForEnter();
            
            int checkinRate = (Staff.getCheckin_Couter() * 100) / tickets.size();
            String rateText = checkinRate + "%";
            System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────────────────┐");
            System.out.println("\t\t\t│                                 CHECK-IN STATISTICS                            │");
            System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘"); 
            System.out.println("\t\t\t│                                                                                │");
            System.out.println("\t\t\t│                                                                                │");
            System.out.printf("\t\t\t│        Total Check-ins          :             %-32d │\n",Staff.getCheckin_Couter());
            System.out.println("\t\t\t│                                                                                │");
            System.out.printf("\t\t\t│        Total Attendees          :             %-32d │\n",tickets.size());
            System.out.println("\t\t\t│                                                                                │");
            System.out.printf("\t\t\t│        Check-in Rate            :             %-32s │\n", rateText);
            System.out.println("\t\t\t│                                                                                │");
            System.out.println("\t\t\t│                                                                                │");
            System.out.println("\t\t\t│                                 Check-in Rate                                  │");
            System.out.println("\t\t\t│            ┌──────────────────────────────────────────────────────────────┐    │");
            System.out.print("\t\t\t|            │ ");
            int displaybar=(int)(double)checkinRate/100*59;
            for (int i=0; i<59;i++){
                if(displaybar>=i){
                    System.out.print("█");
                }else{
                    System.out.print("░");
                }
                    

                }
            

            
            System.out.println("  │    │");
            System.out.println("\t\t\t│            └──────────────────────────────────────────────────────────────┘    │");
            System.out.println("\t\t\t│                                                                                │ ");
            System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘");

                waitForEnter();
                waitForEnter();
                System.out.println("\t\t\tPlease Press Enter Key to Return Back Menu.");
   


        
        }
    

    }

    public static void event_report(){
       System.out.println("\n\t\t╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("\t\t║                                    EVENT REPORT                               ║");
        System.out.printf("\t\t║                              Generated    :%-35s ║\n",LocalDate.now());
        System.out.println("\t\t╚════════════════════════════════════════════════════════════════════════════════╝");
        
        System.out.println("\n\t\t\t┌─────────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("\t\t\t│                              Event List                                         │");
        System.out.println("\t\t\t├────────────┬────────────────────┬───────────────────┬────────────┬──────────────┤");
        System.out.println("\t\t\t│ Event ID   │      Title         │        Venue      │    Date    │      Type    │");



        if (events.length==0){

        
        System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("\t\t\t│                                  NO ANY EVENT                                  │");
        System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘");
    }else{        
        String type= "Conference";
        for(Conference conference : conferences){
                    if (conference!=null){
                        System.out.println("\t\t\t├────────────┼────────────────────┼───────────────────┼────────────┼──────────────┤"); 
                        System.out.printf("\t\t\t│ %-2s │ %-18s │ %-19s │ %-8s │ %-8s│\n",
                        conference.getEventID(),
                        conference.getTitle(),
                        conference.getVenue(),
                        conference.getDate(),
                        type);
                        
                    }else{
                        break;
                    }
        }
         type= "Workshop";
        for(Workshop workshop : workshops){
                    if (workshop!=null){
                        System.out.println("\t\t\t├────────────┼────────────────────┼───────────────────┼────────────┼──────────────┤"); 
                        System.out.printf("\t\t\t│ %-12s │ %-16s │ %-18s │ %-8s │ %-11s│\n",
                        workshop.getEventID(),
                        workshop.getTitle(),
                        workshop.getVenue(),
                        workshop.getDate(),
                        type);
                        
                    }else{
                        break;
                    }
        }
         type= "Concert";
        for(Concert concert : concerts){
                    if (concert!=null){
                        System.out.println("\t\t\t├────────────┼────────────────────┼───────────────────┼────────────┼──────────────┤"); 
                        System.out.printf("\t\t\t│ %-10s │ %-18s │ %-17s │ %-8s │ %-13s│\n",
                        concert.getEventID(),
                        concert.getTitle(),
                        concert.getVenue(),
                        concert.getDate(),
                        type);
                        
                    }else{
                        break;
                    }
        }
        
        
            System.out.println("\t\t\t└─────────────────────────────────────────────────────────────────────────────────┘");

            System.out.print("\n\t\t\tEnter Event ID : ");
            
            scan.nextLine();
            String eventId=scan.nextLine();

            Event current_event=ems.findEventById(events, eventId); 
            if (current_event!=null){
                System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────────────────┐");
                System.out.println("\t\t\t│                                 EVENT INFORMATION                              │");
                System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘"); 
                System.out.println("\t\t\t│                                                                                │");
                System.out.println("\t\t\t│                                                                                │");
                System.out.printf("\t\t\t│        Event ID                 :             %-32s │\n",current_event.getEventID());
                System.out.println("\t\t\t│                                                                                │");
                System.out.printf("\t\t\t│        Event Name               :             %-32s │\n",current_event.getTitle());
                System.out.println("\t\t\t│                                                                                │");
                System.out.printf("\t\t\t│        Date                     :             %-32s │\n", current_event.getDate());
                System.out.println("\t\t\t│                                                                                │");
                System.out.printf("\t\t\t│        Venue                    :             %-32s |\n", current_event.getVenue());
                System.out.println("\t\t\t│                                                                                │");
                System.out.printf("\t\t\t│        Max Capacity             :             %-32d |\n",current_event.getMaxTickets());
                System.out.println("\t\t\t│                                                                                │");
                System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘");

                waitForEnter();
                waitForEnter();
                System.out.println("\t\t\tPlease Press Enter Key to Continue.");
                
                TicketType current_TicketType= TicketType.findTicketTypeById(ticketTypes, eventId);
                int [] tol= current_TicketType.getTotalTicketType(); 
                // Early Bird
            int totalEarly = tol[0];
            int soldEarly = totalEarly - current_TicketType.getQuantityEarlyBird();
            int availableEarly = current_TicketType.getQuantityEarlyBird();
            double revenueEarly = soldEarly * current_TicketType.getPrice("earlybird");

// Standard
            int totalStandard = tol[1];
            int soldStandard = totalStandard - current_TicketType.getQuantityStandard();
            int availableStandard = current_TicketType.getQuantityEarlyBird();
            double revenueStandard = soldStandard * current_TicketType.getPrice("standard");

// VIP
           int totalVip = tol[2];
           int soldVip = totalVip - current_TicketType.getQuantityVip();
           int availableVip = current_TicketType.getQuantityVip();
        double revenueVip = soldVip * current_TicketType.getPrice("vip");

// Print table
        System.out.println("\n\t\t\t┌─────────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("\t\t\t│                                 Tickets Sales                                   │");
        System.out.println("\t\t\t├─────────────────────────────────────────────────────────────────────────────────┤");
        System.out.println("\t\t\t│ Ticket Type        │ Total        │ Sold         │ Available    │ Revenue       │");
        System.out.println("\t\t\t├────────────────────┼──────────────┼──────────────┼──────────────┼───────────────┤");

        System.out.printf("\t\t\t│ Early Bird         │ %-12f │ %-12f │ %-12d │ RM %-10.2f │\n", 
        totalEarly, soldEarly, availableEarly, revenueEarly);

        System.out.printf("\t\t\t│ Standard           │ %-12d │ %-12d │ %-12d │ RM %-10.2f │\n", 
        totalStandard, soldStandard, availableStandard, revenueStandard);

        System.out.printf("\t\t\t│ VIP                │ %-12d │ %-12d │ %-12d │ RM %-10.2f │\n", 
        totalVip, soldVip, availableVip, revenueVip);

        System.out.println("\t\t\t├────────────────────┼──────────────┼──────────────┼──────────────┼───────────────┤");

        int totalAll = totalEarly + totalStandard + totalVip;
        int soldAll = soldEarly + soldStandard + soldVip;
        int availableAll = availableEarly + availableStandard + availableVip;
        double revenueAll = revenueEarly + revenueStandard + revenueVip;

        System.out.printf("\t\t\t│ TOTAL              │ %-12d │ %-12d │ %-12d │ RM %-10.2f │\n", 
        totalAll, soldAll, availableAll, revenueAll);
        System.out.println("\t\t\t└────────────────────┴──────────────┴──────────────┴──────────────┴───────────────┘");

                waitForEnter();
                System.out.println("\t\t\tPlease Press Enter Key to Continue.");
                
                
        int total_ticket_checkin=0;
        for (Ticket ticket:tickets){
                    if(ticket.getEventId().equals(eventId)){
                        if(!ticket.getStatus()){
                            total_ticket_checkin++;

                        }
                    }
                }

        double checkinRate = (soldAll > 0) ? (double) total_ticket_checkin / soldAll * 100 : 0;
        String rateText = checkinRate + "%";
        System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("\t\t\t│                                 CHECK-IN STATISTICS                            │");
        System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘"); 
        System.out.println("\t\t\t│                                                                                │");
        System.out.println("\t\t\t│                                                                                │");
        System.out.printf("\t\t\t│        Total Check-ins          :             %-32d │\n",total_ticket_checkin);
        System.out.println("\t\t\t│                                                                                │");
        System.out.printf("\t\t\t│        Total Attendees          :             %-32d │\n",soldAll);
        System.out.println("\t\t\t│                                                                                │");
        System.out.printf("\t\t\t│        Check-in Rate            :             %-32s │\n", rateText);
        System.out.println("\t\t\t│                                                                                │");
        System.out.println("\t\t\t│                                                                                │");
        System.out.println("\t\t\t│                                 Check-in Rate                                  │");
        System.out.println("\t\t\t│            ┌──────────────────────────────────────────────────────────────┐    │");
        System.out.print("\t\t\t|            │ ");
        
        int displaybar=(int)(double)checkinRate/100*59;
        for (int i=0; i<59;i++){
                if(displaybar>=i){
                    System.out.print("█");
                }else{
                    System.out.print("░");
                }
                    

                }
            

            
            System.out.println("  │    │");
            System.out.println("\t\t\t│            └──────────────────────────────────────────────────────────────┘    │");
            System.out.println("\t\t\t│                                                                                │ ");
            System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘");

                waitForEnter();
                waitForEnter();
                System.out.println("\t\t\tPlease Press Enter Key to Return Back Menu.");


        

            }else{
                System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────────────────┐");
                System.out.println("\t\t\t│                                  NO FOUND                                      │");
                System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘");
            }
    }



    }     

    public static void sale_report(){
        
       System.out.println("\n\t\t╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("\t\t║                                     SALES REPORT                                ║");
        System.out.printf("\t\t║                              Generated    :%-35s ║\n",LocalDate.now());
        System.out.println("\t\t╚════════════════════════════════════════════════════════════════════════════════╝");
        
        System.out.println("\n\t\t\t┌─────────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("\t\t\t│                              Event List                                         │");
        System.out.println("\t\t\t├────────────┬────────────────────┬───────────────────┬────────────┬──────────────┤");
        System.out.println("\t\t\t│ Event ID   │      Title         │        Venue      │    Date    │      Type    │");



        if (events.length==0){

        
        System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("\t\t\t│                                  NO ANY EVENT                                  │");
        System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘");
    }else{        
        String type= "Conference";
        for(Conference conference : conferences){
                    if (conference!=null){
                        System.out.println("\t\t\t├────────────┼────────────────────┼───────────────────┼────────────┼──────────────┤"); 
                        System.out.printf("\t\t\t│ %-2s │ %-18s │ %-19s │ %-8s │ %-8s│\n",
                        conference.getEventID(),
                        conference.getTitle(),
                        conference.getVenue(),
                        conference.getDate(),
                        type);
                        
                    }else{
                        break;
                    }
        }
         type= "Workshop";
        for(Workshop workshop : workshops){
                    if (workshop!=null){
                        System.out.println("\t\t\t├────────────┼────────────────────┼───────────────────┼────────────┼──────────────┤"); 
                        System.out.printf("\t\t\t│ %-12s │ %-16s │ %-18s │ %-8s │ %-11s│\n",
                        workshop.getEventID(),
                        workshop.getTitle(),
                        workshop.getVenue(),
                        workshop.getDate(),
                        type);
                        
                    }else{
                        break;
                    }
        }
         type= "Concert";
        for(Concert concert : concerts){
                    if (concert!=null){
                        System.out.println("\t\t\t├────────────┼────────────────────┼───────────────────┼────────────┼──────────────┤"); 
                        System.out.printf("\t\t\t│ %-10s │ %-18s │ %-17s │ %-8s │ %-13s│\n",
                        concert.getEventID(),
                        concert.getTitle(),
                        concert.getVenue(),
                        concert.getDate(),
                        type);
                        
                    }else{
                        break;
                    }
        }
        
        
            System.out.println("\t\t\t└─────────────────────────────────────────────────────────────────────────────────┘");

            System.out.print("\n\t\t\tEnter Event ID : ");
            
            scan.nextLine();
            String eventId=scan.nextLine();

            Event current_event=ems.findEventById(events, eventId); 
            if (current_event!=null){
                System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────────────────┐");
                System.out.println("\t\t\t│                                 EVENT INFORMATION                              │");
                System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘"); 
                System.out.println("\t\t\t│                                                                                │");
                System.out.println("\t\t\t│                                                                                │");
                System.out.printf("\t\t\t│        Event ID                 :             %-32s │\n",current_event.getEventID());
                System.out.println("\t\t\t│                                                                                │");
                System.out.printf("\t\t\t│        Event Name               :             %-32s │\n",current_event.getTitle());
                System.out.println("\t\t\t│                                                                                │");
                System.out.printf("\t\t\t│        Date                     :             %-32s │\n", current_event.getDate());
                System.out.println("\t\t\t│                                                                                │");
                System.out.printf("\t\t\t│        Venue                    :             %-32s |\n", current_event.getVenue());
                System.out.println("\t\t\t│                                                                                │");
                System.out.printf("\t\t\t│        Max Capacity             :             %-32d |\n",current_event.getMaxTickets());
                System.out.println("\t\t\t│                                                                                │");
                System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘");

                waitForEnter();
                waitForEnter();
                System.out.println("\t\t\tPlease Press Enter Key to Continue.");
                
                TicketType current_TicketType= TicketType.findTicketTypeById(ticketTypes, eventId);
                int [] tol= current_TicketType.getTotalTicketType(); 
                // Early Bird
            double totalEarly = tol[0]*current_TicketType.getPrice("earlybird");
            double soldEarly = totalEarly - current_TicketType.getQuantityEarlyBird()*current_TicketType.getPrice("earlybird");
            double availableEarly = (double)current_TicketType.getQuantityEarlyBird()*current_TicketType.getPrice("earlybird");
            double revenueEarly = soldEarly;

// Standard
            double totalStandard = tol[1]*current_TicketType.getPrice("standard");
            double soldStandard = totalStandard - (current_TicketType.getQuantityStandard())*current_TicketType.getPrice("standard");
            double availableStandard = current_TicketType.getQuantityEarlyBird()*current_TicketType.getPrice("standard");
            double revenueStandard = soldStandard ;

// VIP
           double totalVip = tol[2]* current_TicketType.getPrice("vip");
           double soldVip = totalVip - current_TicketType.getQuantityVip()* current_TicketType.getPrice("vip");
           double availableVip = current_TicketType.getQuantityVip()*current_TicketType.getPrice("vip");
        double revenueVip = soldVip ;

// Print table
        System.out.println("\n\t\t\t┌─────────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("\t\t\t│                                 Tickets Sales                                   │");
        System.out.println("\t\t\t├─────────────────────────────────────────────────────────────────────────────────┤");
        System.out.println("\t\t\t│ Ticket Type        │ Total        │ Sold         │ Available    │ Revenue       │");
        System.out.println("\t\t\t├────────────────────┼──────────────┼──────────────┼──────────────┼───────────────┤");

        System.out.printf("\t\t\t│ Early Bird         │ RM %-9.2f │ RM %-9.2f │ RM %-9.2f │ RM %-10.2f │\n", 
        totalEarly, soldEarly, availableEarly, revenueEarly);

        System.out.printf("\t\t\t│ Standard           │ RM %-9.2f │ RM %-9.2f │ RM %-9.2f │ RM %-10.2f │\n", 
        totalStandard, soldStandard, availableStandard, revenueStandard);

        System.out.printf("\t\t\t│ VIP                │ RM %-9.2f │ RM %-9.2f │ RM %-9.2f │ RM %-10.2f │\n", 
        totalVip, soldVip, availableVip, revenueVip);

        System.out.println("\t\t\t├────────────────────┼──────────────┼──────────────┼──────────────┼───────────────┤");

        double totalAll = totalEarly + totalStandard + totalVip;
        double soldAll = soldEarly + soldStandard + soldVip;
        double availableAll = availableEarly + availableStandard + availableVip;
        double revenueAll = revenueEarly + revenueStandard + revenueVip;

        System.out.printf("\t\t\t│ TOTAL              │ RM %-9.2f │ RM %-9.2f │ RM %-9.2f │ RM %-10.2f │\n", 
        totalAll, soldAll, availableAll, revenueAll);
        System.out.println("\t\t\t└────────────────────┴──────────────┴──────────────┴──────────────┴───────────────┘");

                waitForEnter();
                System.out.println("\t\t\tPlease Press Enter Key to Continue.");

            }else{
                System.out.println("\n\t\t\t┌────────────────────────────────────────────────────────────────────────────────┐");
                System.out.println("\t\t\t│                                  NO FOUND                                      │");
                System.out.println("\t\t\t└────────────────────────────────────────────────────────────────────────────────┘");
            }
    



    }     

    }

    public static String status_ToString(boolean status){
        if (status){
            return "Pending";
        }else{
            return "Confirm";
        }
    } 

    public static void exportCheckInReportToFile(User[][] alluser) {
    try {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "CheckIn_Report_" + timestamp + ".txt";
        FileWriter writer = new FileWriter(filename);
        
        writer.write("\t\t╔════════════════════════════════════════════════════════════════════════════════╗\n");
        writer.write("\t\t║                           CHECK-IN REPORT                                      ║\n");
        writer.write("\t\t╠════════════════════════════════════════════════════════════════════════════════╣\n");
        writer.write("\t\t║  Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "                                      ║\n");
        writer.write("\t\t╠════════════════════════════════════════════════════════════════════════════════╣\n");
        writer.write("\t\t║                                                                                ║\n");
        writer.write("\t\t║  ┌──────────────────────────────────────────────────────────────────────────┐ ║\n");
        writer.write("\t\t║  │                          CHECK-IN HISTORY                                │ ║\n");
        writer.write("\t\t║  ├────┬────────────────────┬─────────────────────┬──────────────┬───────────┤ ║\n");
        writer.write("\t\t║  │ No │ Name               │ Email               │ Ticket ID    │ Status    │ ║\n");
        writer.write("\t\t║  ├────┼────────────────────┼─────────────────────┼──────────────┼───────────┤ ║\n");
        
        int no = 1;
        for (Ticket ticket : tickets) {
            if (ticket != null) {
                for (User user : alluser[2]) {
                    if (user != null && ticket.getBuyerName().equals(user.getAccessUsername())) {
                        writer.write(String.format("\t\t║  %2d │ %-18s │ %-19s │ %-12s │ %-8s │ ║\n",
                            no++, ticket.getBuyerName(), user.getAccessEmail(), 
                            ticket.getTicketId(), status_ToString(ticket.getStatus())));
                        break;
                    }
                }
            }
        }
        
        writer.write("\t\t║  └────┴────────────────────┴─────────────────────┴──────────────┴───────────┘ ║\n");
        writer.write("\t\t║                                                                                ║\n");
        writer.write("\t\t╚════════════════════════════════════════════════════════════════════════════════╝\n");
        
        writer.close();
        System.out.println("\n\t\t Check-in Report exported successfully!");
        System.out.println("\t\t   File: " + filename);
    } catch (IOException e) {
        System.out.println("\t\t Error exporting check-in report: " + e.getMessage());
    }
    waitForEnter();
}

    public static void exportSalesReportToFile() {

    try {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "Sales_Report_" + timestamp + ".txt";
        FileWriter writer = new FileWriter(filename);
        
        writer.write("\t\t╔════════════════════════════════════════════════════════════════════════════════╗\n");
        writer.write("\t\t║                                SALES REPORT                                    ║\n");
        writer.write("\t\t╠════════════════════════════════════════════════════════════════════════════════╣\n");
        writer.write("\t\t║  Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "                                      ║\n");
        writer.write("\t\t╠════════════════════════════════════════════════════════════════════════════════╣\n");
        writer.write("\t\t║                                                                                ║\n");
        writer.write("\t\t║  ┌──────────────────────────────────────────────────────────────────────────┐ ║\n");
        writer.write("\t\t║  │                           REVENUE BREAKDOWN                               │ ║\n");
        writer.write("\t\t║  ├────────────────────┬──────────────┬───────────────────────────────────────┤ ║\n");
        writer.write("\t\t║  │ Ticket Type        │ Sold         │ Revenue                               │ ║\n");
        writer.write("\t\t║  ├────────────────────┼──────────────┼───────────────────────────────────────┤ ║\n");
        
        // Calculate totals
        int earlyBirdSold = 0, standardSold = 0, vipSold = 0;
        double earlyBirdRevenue = 0, standardRevenue = 0, vipRevenue = 0;
        
        for (Ticket ticket : tickets) {
            if (ticket != null) {
                switch (ticket.getTicketType().toLowerCase()) {
                    case "earlybird":
                        earlyBirdSold++;
                        earlyBirdRevenue += ticket.getTotalAmount();
                        break;
                    case "standard":
                        standardSold++;
                        standardRevenue += ticket.getTotalAmount();
                        break;
                    case "vip":
                        vipSold++;
                        vipRevenue += ticket.getTotalAmount();
                        break;
                }
            }
        }
        
        writer.write(String.format("\t\t║  │ Early Bird         │ %-12d │ RM %-40.2f ║\n", earlyBirdSold, earlyBirdRevenue));
        writer.write(String.format("\t\t║  │ Standard           │ %-12d │ RM %-40.2f ║\n", standardSold, standardRevenue));
        writer.write(String.format("\t\t║  │ VIP                │ %-12d │ RM %-40.2f ║\n", vipSold, vipRevenue));
        
        double totalRevenue = earlyBirdRevenue + standardRevenue + vipRevenue;
        int totalSold = earlyBirdSold + standardSold + vipSold;
        
        writer.write("\t\t║  ├────────────────────┼──────────────┼───────────────────────────────────────┤ ║\n");
        writer.write(String.format("\t\t║  │ TOTAL              │ %-12d │ RM %-40.2f ║\n", totalSold, totalRevenue));
        writer.write("\t\t║  └──────────────────────────────────────────────────────────────────────────┘ ║\n");
        writer.write("\t\t║                                                                                ║\n");
        writer.write("\t\t╚════════════════════════════════════════════════════════════════════════════════╝\n");
        
        writer.close();
        System.out.println("\n\t\t✅ Sales Report exported successfully!");
        System.out.println("\t\t   File: " + filename);
    } catch (IOException e) {
        System.out.println("\t\t❌ Error exporting sales report: " + e.getMessage());
    }
    waitForEnter();
}
    
    public static void exportEventReportToFile() {
    scan.nextLine();
    System.out.print("\n\t\tEnter Event ID to export: ");
    String eventId = scan.nextLine();
    
    Event current_event = ems.findEventById(events, eventId);
    if (current_event == null) {
        System.out.println("\t\t Event not found!");
        waitForEnter();
        return;
    }
    
    try {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "Event_Report_" + eventId + "_" + timestamp + ".txt";
        FileWriter writer = new FileWriter(filename);
        
        writer.write("\t\t╔════════════════════════════════════════════════════════════════════════════════╗\n");
        writer.write("\t\t║                                EVENT REPORT                                    ║\n");
        writer.write("\t\t╠════════════════════════════════════════════════════════════════════════════════╣\n");
        writer.write("\t\t║                                                                                ║\n");
        writer.write("\t\t║  ┌──────────────────────────────────────────────────────────────────────────┐ ║\n");
        writer.write("\t\t║  │                           EVENT INFORMATION                              │ ║\n");
        writer.write("\t\t║  ├──────────────────────────────────────────────────────────────────────────┤ ║\n");
        writer.write(String.format("\t\t║  │  Event ID       : %-62s ║\n", current_event.getEventID()));
        writer.write(String.format("\t\t║  │  Event Name     : %-62s ║\n", current_event.getTitle()));
        writer.write(String.format("\t\t║  │  Date           : %-62s ║\n", current_event.getDate()));
        writer.write(String.format("\t\t║  │  Venue          : %-62s ║\n", current_event.getVenue()));
        writer.write(String.format("\t\t║  │  Max Capacity   : %-62d ║\n", current_event.getMaxTickets()));
        writer.write("\t\t║  └──────────────────────────────────────────────────────────────────────────┘ ║\n");
        writer.write("\t\t║                                                                                ║\n");
        
        TicketType current_TicketType = TicketType.findTicketTypeById(ticketTypes, eventId);
        if (ticketTypes != null) {
            writer.write("\t\t║  ┌──────────────────────────────────────────────────────────────────────────┐ ║\n");
            writer.write("\t\t║  │                           TICKET SALES                                   │ ║\n");
            writer.write("\t\t║  ├────────────────────┬──────────────┬──────────────┬───────────────────────┤ ║\n");
            writer.write("\t\t║  │ Ticket Type        │ Total        │ Sold         │ Revenue               │ ║\n");
            writer.write("\t\t║  ├────────────────────┼──────────────┼──────────────┼───────────────────────┤ ║\n");
            
            int [] tol= current_TicketType.getTotalTicketType(); 
                // Early Bird
            int totalEarly = tol[0];
            int soldEarly = totalEarly - current_TicketType.getQuantityEarlyBird();
            double revenueEarly = soldEarly*current_TicketType.getPrice("earlybird");

// Standard
            int totalStandard = tol[1];
            int soldStandard = totalStandard - current_TicketType.getQuantityStandard();
            double revenueStandard = soldStandard *current_TicketType.getPrice("standard");

// VIP
           int totalVip = tol[2];
        int soldVip = totalVip - current_TicketType.getQuantityVip();
        double revenueVip = soldVip * current_TicketType.getPrice("vip");

            
            writer.write(String.format("\t\t║  │ Early Bird         │ %-12d │ %-12d │ RM %-22.2f ║\n", totalEarly, soldEarly, revenueEarly));
            writer.write(String.format("\t\t║  │ Standard           │ %-12d │ %-12d │ RM %-22.2f ║\n", totalStandard, soldStandard, revenueStandard));
            writer.write(String.format("\t\t║  │ VIP                │ %-12d │ %-12d │ RM %-22.2f ║\n", totalVip, soldVip, revenueVip));
            
            writer.write("\t\t║  ├────────────────────┼──────────────┼──────────────┼───────────────────────┤ ║\n");
            writer.write(String.format("\t\t║  │ TOTAL              │ %-12d │ %-12d │ RM %-22.2f ║\n", 
                (totalEarly + totalStandard + totalVip), 
                (soldEarly + soldStandard + soldVip), 
                (revenueEarly + revenueStandard + revenueVip)));
            writer.write("\t\t║  └──────────────────────────────────────────────────────────────────────────┘ ║\n");
        }
        
        writer.write("\t\t║                                                                                ║\n");
        writer.write("\t\t╚════════════════════════════════════════════════════════════════════════════════╝\n");
        
        writer.close();
        System.out.println("\n\t\t Event Report exported successfully!");
        System.out.println("\t\t   File: " + filename);
    } catch (IOException e) {
        System.out.println("\t\t Error exporting event report: " + e.getMessage());
    }
    waitForEnter();
}

    public static void exportAllReportsToCSV() {
    try {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        
        // Export Check-in CSV
        String checkinFilename = "CheckIn_Report_" + timestamp + ".csv";
        FileWriter checkinWriter = new FileWriter(checkinFilename);
        checkinWriter.write("Name,Email,Ticket ID,Status\n");
        
        for (Ticket ticket : tickets) {
            if (ticket != null) {
                checkinWriter.write(String.format("%s,%s,%s,%s\n",
                    ticket.getBuyerName(), "", ticket.getTicketId(), status_ToString(ticket.getStatus())));
            }
        }
        checkinWriter.close();
        
        // Export Sales CSV
        String salesFilename = "Sales_Report_" + timestamp + ".csv";
        FileWriter salesWriter = new FileWriter(salesFilename);
        salesWriter.write("Event ID,Event Name,Ticket Type,Sold,Revenue\n");
        
        int earlyBirdSold = 0, standardSold = 0, vipSold = 0;
        double earlyBirdRevenue = 0, standardRevenue = 0, vipRevenue = 0;
        
        for (Ticket ticket : tickets) {
            if (ticket != null) {
                switch (ticket.getTicketType().toLowerCase()) {
                    case "earlybird":
                        earlyBirdSold++;
                        earlyBirdRevenue += ticket.getTotalAmount();
                        break;
                    case "standard":
                        standardSold++;
                        standardRevenue += ticket.getTotalAmount();
                        break;
                    case "vip":
                        vipSold++;
                        vipRevenue += ticket.getTotalAmount();
                        break;
                }
            }
        }
        
        salesWriter.write(String.format("ALL,ALL,Early Bird,%d,%.2f\n", earlyBirdSold, earlyBirdRevenue));
        salesWriter.write(String.format("ALL,ALL,Standard,%d,%.2f\n", standardSold, standardRevenue));
        salesWriter.write(String.format("ALL,ALL,VIP,%d,%.2f\n", vipSold, vipRevenue));
        salesWriter.close();
        
        // Export Event CSV
        String eventFilename = "Event_Report_" + timestamp + ".csv";
        FileWriter eventWriter = new FileWriter(eventFilename);
        eventWriter.write("Event ID,Event Name,Date,Venue,Max Capacity\n");
        
        for (Event event : events) {
            if (event != null) {
                eventWriter.write(String.format("%s,%s,%s,%s,%d\n",
                    event.getEventID(), event.getTitle(), event.getDate(), 
                    event.getVenue(), event.getMaxTickets()));
            }
        }
        eventWriter.close();
        
        System.out.println("\n\t\t✅ All Reports exported successfully!");
        System.out.println("\t\t   Check-in Report: " + checkinFilename);
        System.out.println("\t\t   Sales Report: " + salesFilename);
        System.out.println("\t\t   Event Report: " + eventFilename);
    } catch (IOException e) {
        System.out.println("\t\t❌ Error exporting reports: " + e.getMessage());
    }
    waitForEnter();
}
// ─────────────────────────────────────────────────────────────────────────
    // ORGANIZER MENU
    // ─────────────────────────────────────────────────────────────────────────
    static void organizerMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║      ORGANIZER MENU          ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1: Create Event             ║");
            System.out.println("║  2: Remove Event             ║");
            System.out.println("║  3: Update Event             ║");
            System.out.println("║  4: Manage Sessions          ║");
            System.out.println("║  5: Manage Speakers          ║");
            System.out.println("║  6: View All Events          ║");
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
                    removeEvent();
                    break;
                case 3:
                    updateEvent();
                    break;
                case 4:
                    manageSessionsMenu();
                    break;
                case 5:
                    manageSpeakersMenu();
                    break;
                case 6:
                    viewAllEvents();
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CREATE EVENT
    // ─────────────────────────────────────────────────────────────────────────
    static void createEvent() {
        System.out.println("\n--- Create Event ---");
        System.out.println("1: Concert");
        System.out.println("2: Workshop");
        System.out.println("3: Conference");
        System.out.print("Enter option: ");
        int type = scan.nextInt();
        scan.nextLine();

        if (type < 1 || type > 3) {
            System.out.println("Invalid event type.");
            return;
        }

        // common fields
        String title;
        do {
            System.out.print("Title            : ");
            title = scan.nextLine();
        } while (!ems.validationTitle(title));

        String date;
        LocalDate parsedDate;
        do {
            System.out.print("Date (YYYY-MM-DD) : ");
            date = scan.nextLine();
            parsedDate = ems.validationDate(date);
        } while (parsedDate == null);

        String venue;
        do {
            System.out.print("Venue            : ");
            venue = scan.nextLine();
        } while (!ems.validationVenue(venue));

        int maxTix;
        do {
            System.out.print("Creating ticket type......");
            System.out.print("\nMax Ticket (Recommend 150):");
            maxTix = scan.nextInt();
            scan.nextLine();
        } while (!ems.validationMaxTickets(maxTix));

        int qeb;
        int qsd;
        int qvip;
        do {
            System.out.print("Quantity Early Bird (Recommend 20% of total ticket):");
            qeb = scan.nextInt();
            scan.nextLine();
            System.out.print("Quantity Standard (Recommend 60% of total ticket):");
            qsd = scan.nextInt();
            scan.nextLine();
            System.out.print("Quantity Vip (Recommend 20% of total ticket):");
            qvip = scan.nextInt();
            scan.nextLine();
        } while (!ems.validationQuantityTicket(maxTix, qeb, qsd, qvip));

        double peb;
        double psd;
        double pvip;
        do {
            System.out.print("Price Early Bird (RM):");
            peb = scan.nextInt();
            scan.nextLine();
            System.out.print("Price Standard (RM):");
            psd = scan.nextInt();
            scan.nextLine();
            System.out.print("Price Vip (RM):");
            pvip = scan.nextInt();
            scan.nextLine();
        } while (!ems.validationPrice(peb, psd, pvip));

        String perks;
        do {
            System.out.print("Perks Provided: (if no just enter -) ");
            perks = scan.nextLine();
        } while (!ems.validationPerks(perks));

        String ssdate;
        LocalDate salesStartDate;
        do {
            System.out.print("Sales Start Date (YYYY-MM-DD) : ");
            ssdate = scan.nextLine();
            salesStartDate = ems.validationSalesStartDate(ssdate, parsedDate);
        } while (salesStartDate == null);

        String sedate;
        LocalDate salesEndDate;
        do {
            System.out.print("Sales End Date (YYYY-MM-DD) : ");
            sedate = scan.nextLine();
            salesEndDate = ems.validationSalesEndDate(sedate, salesStartDate, parsedDate);
        } while (salesEndDate == null);

        System.out.println("Ticket type created.");

        if (type == 1) {
            // Concert
            Concert c = new Concert(title, parsedDate, venue, maxTix);
            TicketType tt = new TicketType(c.getEventID(), maxTix, qeb, qsd, qvip, peb, psd, pvip, perks,
                    salesStartDate, salesEndDate,qeb,qsd,qvip);
            ticketTypes.add(tt);
            concerts.add(c);
            events[eventCount++] = c;
            System.out.println("Concert created successfully : " + c.getEventID());

        } else if (type == 2) {
            // Workshop
            Workshop w = new Workshop(title, parsedDate, venue, maxTix);
            TicketType tt = new TicketType(w.getEventID(), maxTix, qeb, qsd, qvip, peb, psd, pvip, perks,
                    salesStartDate, salesEndDate,qeb,qsd,qvip);
            ticketTypes.add(tt);
            workshops.add(w);
            events[eventCount++] = w;
            System.out.println("Workshop created successfully : " + w.getEventID());

        } else {
            // Conference — ask how many sessions to create right away
            System.out.print("How many sessions to create now? (0 to skip): ");
            int numSessions = scan.nextInt();
            scan.nextLine();

            String[] topics = new String[numSessions];
            String[] times = new String[numSessions];

            for (int i = 0; i < numSessions; i++) {
                System.out.print("  Session " + (i + 1) + " Topic            : ");
                topics[i] = scan.nextLine();

                // validate time in HHMM format
                do {
                    System.out.print("  Session " + (i + 1) + " Time (HHMM 0000-2359): ");
                    times[i] = scan.nextLine();
                } while (!ems.validationSessionTime(times[i]));
            }

            Conference conf = new Conference(title, parsedDate, venue, maxTix);
            TicketType tt = new TicketType(conf.getEventID(), maxTix, qeb, qsd, qvip, peb, psd, pvip, perks,
                    salesStartDate, salesEndDate,qeb,qsd,qvip);
            ticketTypes.add(tt);
            if (numSessions > 0) {
                conf.autoCreateSessions(topics, times);
            }
            conferences.add(conf);
            events[eventCount++] = conf;
            System.out.println("Conference created successfully : " + conf.getEventID());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // REMOVE EVENT
    // ─────────────────────────────────────────────────────────────────────────
    static void removeEvent() {
        if (eventCount == 0) {
            System.out.println("No events available.");
            return;
        }
        System.out.println("\n--- Remove Event ---");
        listEvents();
        System.out.print("Enter Event ID to remove: ");
        String eventID = scan.nextLine();

        boolean found = false;
        for (int i = 0; i < eventCount; i++) {
            if (events[i].getEventID().equals(eventID)) {
                if (events[i] instanceof Concert) {
                    Concert.removeConcert(concerts, eventID);
                } else if (events[i] instanceof Workshop) {
                    Workshop.removeWorkshop(workshops, eventID);
                } else if (events[i] instanceof Conference) {
                    Conference.removeConference(conferences, eventID);
                }
                // remove from flat events array
                for (int j = i; j < eventCount - 1; j++) {
                    events[j] = events[j + 1];
                }
                events[eventCount - 1] = null;
                eventCount--;
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Error: Event [" + eventID + "] not found !");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UPDATE EVENT
    // ─────────────────────────────────────────────────────────────────────────
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
                String newTitle;
                do {
                    System.out.print("New Title       : ");
                    newTitle = scan.nextLine();
                } while (!ems.validationTitle(newTitle));
                e.setTitle(newTitle);
                saveEvent(e);
                System.out.println("Title updated.");
                break;
            case 2:
                LocalDate newDate = null;
                do {
                    System.out.print("New Date (YYYY-MM-DD): ");
                    String newDateStr = scan.nextLine();
                    newDate = ems.validationDate(newDateStr);
                } while (newDate == null);
                e.setDate(newDate);
                saveEvent(e);
                System.out.println("Date updated.");
                break;
            case 3:
                String newVenue;
                do {
                    System.out.print("New Venue       : ");
                    newVenue = scan.nextLine();
                } while (!ems.validationVenue(newVenue));
                e.setVenue(newVenue);
                saveEvent(e);
                System.out.println("Venue updated.");
                break;
            case 4:
                int newMax;
                do {
                    System.out.print("New Max Tickets : ");
                    newMax = scan.nextInt();
                    scan.nextLine();
                } while (!ems.validationMaxTickets(newMax));
                e.setMaxTickets(newMax);
                saveEvent(e);
                System.out.println("Max tickets updated.");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MANAGE SESSIONS MENU
    // ─────────────────────────────────────────────────────────────────────────
    static void manageSessionsMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║      MANAGE SESSIONS         ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1: Add Session              ║");
            System.out.println("║  2: Remove Session           ║");
            System.out.println("║  3: View Sessions            ║");
            System.out.println("║  0: Back                     ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print("Enter option: ");
            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    addSession();
                    break;
                case 2:
                    removeSession();
                    break;
                case 3:
                    viewSessions();
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // add a session to a conference
    static void addSession() {
        Conference conf = selectConference();
        if (conf == null) {
            return;
        }
        System.out.print("Session Topic            : ");
        String topic = scan.nextLine();

        String time;
        do {
            System.out.print("Session Time (HHMM 0000-2359): ");
            time = scan.nextLine();
        } while (!ems.validationSessionTime(time));

        Session s = conf.createSession(topic, time);
        if (s != null) {
            System.out.println("Session [" + s.getSessionID() + "] added to conference [" + conf.getEventID() + "].");
        }
    }

    // remove a session from a conference
    static void removeSession() {
        Conference conf = selectConference();
        if (conf == null) {
            return;
        }
        if (conf.getSessionCount() == 0) {
            System.out.println("No sessions in this conference.");
            return;
        }
        conf.displaySessions();
        System.out.print("Enter Session ID to remove: ");
        String sessionID = scan.nextLine();
        conf.removeSession(sessionID);
        saveEvent(conf); // auto-save after session removed
    }

    // view sessions of a conference
    static void viewSessions() {
        Conference conf = selectConference();
        if (conf == null) {
            return;
        }
        conf.displaySessions();
    }

    // helper: pick a conference from the list
    static Conference selectConference() {
        int count = 0;
        Conference[] confList = new Conference[eventCount];
        for (int i = 0; i < eventCount; i++) {
            if (events[i] instanceof Conference) {
                confList[count++] = (Conference) events[i];
            }
        }
        if (count == 0) {
            System.out.println("No conferences found. Create a Conference event first.");
            return null;
        }
        System.out.println("\n--- Select Conference ---");
        for (int i = 0; i < count; i++) {
            System.out.println("  " + (i + 1) + ": [" + confList[i].getEventID() + "] " + confList[i].getTitle());
        }
        System.out.print("Select conference number: ");
        int idx = scan.nextInt() - 1;
        scan.nextLine();
        if (idx < 0 || idx >= count) {
            System.out.println("Invalid selection.");
            return null;
        }
        return confList[idx];
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MANAGE SPEAKERS MENU
    // ─────────────────────────────────────────────────────────────────────────
    static void manageSpeakersMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║      MANAGE SPEAKERS         ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1: Register Speaker         ║");
            System.out.println("║  2: Assign Speaker to Session║");
            System.out.println("║  3: Remove Speaker from Sess ║");
            System.out.println("║  4: View Speakers            ║");
            System.out.println("║  0: Back                     ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print("Enter option: ");
            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    registerSpeaker();
                    break;
                case 2:
                    assignSpeaker();
                    break;
                case 3:
                    removeSpeakerFromSession();
                    break;
                case 4:
                    viewSpeakers();
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // register a new speaker into the pool
    static void registerSpeaker() {
        System.out.println("\n--- Register Speaker ---");
        System.out.print("Username : ");
        String username = scan.nextLine();
        System.out.print("Password : ");
        String password = scan.nextLine();
        System.out.print("Email    : ");
        String email = scan.nextLine();
        System.out.print("Bio      : ");
        String bio = scan.nextLine();

        Speaker sp = new Speaker(username, password, email, bio);
        speakerPool[speakerCount++] = sp;
        System.out.println("Speaker [" + username + "] registered successfully.");
    }

    // assign a speaker from the pool to a session
    static void assignSpeaker() {
        if (speakerCount == 0) {
            System.out.println("No speakers registered yet. Register a speaker first.");
            return;
        }
        Conference conf = selectConference();
        if (conf == null) {
            return;
        }
        if (conf.getSessionCount() == 0) {
            System.out.println("No sessions in this conference. Add a session first.");
            return;
        }
        conf.displaySessions();
        System.out.print("Enter Session ID to assign speaker to: ");
        String sessionID = scan.nextLine();
        Session targetSession = null;
        for (int i = 0; i < conf.getSessionCount(); i++) {
            if (conf.getSessions()[i].getSessionID().equals(sessionID)) {
                targetSession = conf.getSessions()[i];
                break;
            }
        }
        if (targetSession == null) {
            System.out.println("Error: Session [" + sessionID + "] not found !");
            return;
        }
        viewSpeakers();
        System.out.print("Select speaker number: ");
        int spIdx = scan.nextInt() - 1;
        scan.nextLine();
        if (spIdx < 0 || spIdx >= speakerCount) {
            System.out.println("Invalid selection.");
            return;
        }
        ems.assignSpeaker(targetSession, speakerPool[spIdx]);
    }

    // remove a speaker from a session
    static void removeSpeakerFromSession() {
        Conference conf = selectConference();
        if (conf == null) {
            return;
        }
        if (conf.getSessionCount() == 0) {
            System.out.println("No sessions in this conference.");
            return;
        }
        conf.displaySessions();
        System.out.print("Enter Session ID: ");
        String sessionID = scan.nextLine();
        Session targetSession = null;
        for (int i = 0; i < conf.getSessionCount(); i++) {
            if (conf.getSessions()[i].getSessionID().equals(sessionID)) {
                targetSession = conf.getSessions()[i];
                break;
            }
        }
        if (targetSession == null) {
            System.out.println("Error: Session [" + sessionID + "] not found !");
            return;
        }
        System.out.print("Enter Speaker Username to remove: ");
        String speakerUsername = scan.nextLine();
        ems.removeSpeaker(targetSession, speakerUsername);
    }

    // view all registered speakers
    static void viewSpeakers() {
        if (speakerCount == 0) {
            System.out.println("No speakers registered.");
            return;
        }
        System.out.println("\n--- Registered Speakers ---");
        System.out.printf("%-5s %-15s%n", "No.", "Username");
        System.out.println("--------------------");
        for (int i = 0; i < speakerCount; i++) {
            System.out.printf("%-5d %-15s%n", (i + 1), speakerPool[i].getAccessUsername());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // VIEW ALL EVENTS
    // ─────────────────────────────────────────────────────────────────────────
    static void viewAllEvents() {
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

    // helper: print numbered event list (used by other methods)
    static void listEvents() {
        for (int i = 0; i < eventCount; i++) {
            System.out.println("  " + (i + 1) + ": [" + events[i].getEventID() + "] "
                    + events[i].getTitle()
                    + " (" + events[i].getClass().getSimpleName() + ")"
                    + " | Date: " + events[i].getDate()
                    + " | MaxTix: " + events[i].getMaxTickets());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SAVE / LOAD ALL EVENTS
    // ─────────────────────────────────────────────────────────────────────────
    // Auto-save a single event's list to the correct JSON file based on its type
    static void saveEvent(Event e) {
        if (e instanceof Concert) {
            Concert.storeConcertData(concerts);
        } else if (e instanceof Workshop) {
            Workshop.storeWorkshopData(workshops);
        } else if (e instanceof Conference) {
            Conference.storeConferenceData(conferences);
        }
    }

    static void saveAllEvents() {
        Concert.storeConcertData(concerts);
        Workshop.storeWorkshopData(workshops);
        Conference.storeConferenceData(conferences);
        TicketType.storeTicketTypeData(ticketTypes);
        System.out.println("All events saved successfully.");
    }

    static void loadAllEvents() {
        concerts.clear();
        workshops.clear();
        conferences.clear();
        ticketTypes.clear();
        eventCount = 0;

        concerts = Concert.readConcertData();
        workshops = Workshop.readWorkshopData();
        conferences = Conference.readConferenceData();
        ticketTypes = TicketType.readTicektTypeData(); // load ticket types so purchase works
        for (Concert c : concerts) {
            events[eventCount++] = c;
        }
        for (Workshop w : workshops) {
            events[eventCount++] = w;
        }
        for (Conference cf : conferences) {
            events[eventCount++] = cf;
        }

        if (eventCount > 0) {
            System.out.println(eventCount + " event(s) loaded from files.");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ATTENDEE MENU
    // ─────────────────────────────────────────────────────────────────────────
    static void attendeeMenu(Attendee attendee) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║       ATTENDEE MENU          ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1: View Events              ║");
            System.out.println("║  2: Purchase Ticket          ║");
            System.out.println("║  0: Back to Main Menu        ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print("Enter option: ");
            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    viewAllEvents();
                    break;
                case 2:
                    purchaseTicket(attendee);
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // purchase ticket flow
    static void purchaseTicket(Attendee a) {
        if (eventCount == 0) {
            System.out.println("No events available.");
            return;
        }

        // pick event
        String eventId;
        while (true) {
            listEvents();
            System.out.print("Enter Event ID: ");
            eventId = scan.nextLine();
            if (ems.validationInputEventId(events, eventId)) {
                break;
            } else {
                System.out.println("Invalid Event ID. Try again.");
            }
        }

        Event event = ems.findEventById(events, eventId);
        
        TicketType tt = TicketType.findTicketTypeById(ticketTypes, eventId);

        if (tt == null) {
            System.out.println("Error: No ticket types configured for this event yet.");
            return;
        }

        // pick ticket type
        String ticketType = "";
        while (true) {
            System.out.println("===== Ticket Type =====");
            System.out.println("1. EarlyBird  - RM " + tt.getPrice("earlybird"));
            System.out.println("2. Standard   - RM " + tt.getPrice("standard"));
            System.out.println("3. VIP        - RM " + tt.getPrice("vip"));
            System.out.print("Select ticket type: ");
            int type = scan.nextInt();
            scan.nextLine();
            if (type == 1) {
                ticketType = "earlybird";
            } else if (type == 2) {
                ticketType = "standard";
            } else if (type == 3) {
                ticketType = "vip";
            } else {
                System.out.println("Invalid option. Try again.");
                continue;
            }

            if (!tt.isAvailable(ticketType)) {
                System.out.println("Sorry, no " + ticketType + " tickets available!");
                return;
            }
            break;
        }

        // payment
        System.out.println("\nThe total amount = RM " + tt.getPrice(ticketType));
        while (true) {
            System.out.println("Payment Method");
            System.out.println("1. Touch N Go");
            System.out.println("2. Credit/Debit Card");
            System.out.println("3. Online Banking");
            System.out.print("Select your payment method: ");
            int method = scan.nextInt();
            scan.nextLine();
            if (method == 1 || method == 2 || method == 3) {
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
        System.out.println("Processing payment...");
        System.out.println("Payment Success!");

        String bookingId = ems.generateBookingId(eventId);

        Payment p = new Payment(a, eventId, bookingId, tt.getPrice(ticketType));
        System.out.print(p.toString());

        Ticket ticket = ems.purchaseTicket(a, event, tt, ticketType, bookingId);

        if (ticket != null) {
            System.out.println("\nPurchase completed successfully!");
            ticket.displayTicketDetails();
            tickets.add(ticket);
            Ticket.storeTicketData(tickets);
            TicketType.storeTicketTypeData(ticketTypes); // update available quantity
        } else {
            System.out.println("Purchase failed. Please try again.");
        }
    }

    // speaker part
    static void speakerMenu(Speaker loggedInSpeaker, Event[] events) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║       SPEAKER MENU           ║");
            System.out.println("║  Logged in: " + String.format("%-17s", loggedInSpeaker.getAccessUsername()) + "║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1: View & Respond to        ║");
            System.out.println("║     Assigned Sessions        ║");
            System.out.println("║  2: View My Sessions         ║");
            System.out.println("║  3: Update Session Topic     ║");
            System.out.println("║  4: Update Bio               ║");
            System.out.println("║  5: View My Info             ║");
            System.out.println("║  6: Back to Main Menu        ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print("Enter option: ");
            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    // View assigned sessions and choose to accept/reject
                    manageAssignedSessions(loggedInSpeaker, events);
                    break;

                case 2:
                    // View all my sessions (with status only)
                    viewMyAssignedSessions(loggedInSpeaker, events);
                    break;

                case 3:
                    // Update session topic
                    updateSessionTopic(loggedInSpeaker, events);
                    break;

                case 4:
                    // Update Bio
                    updateSpeakerBio(loggedInSpeaker);
                    break;

                case 5:
                    // View My Info
                    loggedInSpeaker.displaySingleInfo();
                    break;

                case 0:
                    inMenu = false;
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Manage assigned sessions (accept/reject)
    static void manageAssignedSessions(Speaker speaker, Event[] events) {
        List<Session> assignedSessions = new java.util.ArrayList<>();

        // Collect all sessions this speaker is assigned to
        for (Event e : events) {
            if (e != null && e instanceof Conference) {
                Conference conf = (Conference) e;
                for (int i = 0; i < conf.getSessionCount(); i++) {
                    Session session = conf.getSessions()[i];
                    if (session.hasSpeaker(speaker.getAccessUsername())) {
                        assignedSessions.add(session);
                    }
                }
            }
        }

        if (assignedSessions.isEmpty()) {
            System.out.println("\nYou are not assigned to any sessions.");
            return;
        }

        boolean continueManaging = true;
        while (continueManaging) {
            // Display all assigned sessions with current status
            System.out.println("\n=== Your Assigned Sessions ===");
            System.out.println("No. | Session ID | Topic | Time | Status");
            System.out.println("----------------------------------------");
            for (int i = 0; i < assignedSessions.size(); i++) {
                Session s = assignedSessions.get(i);
                String status = s.getSpeakerStatus(speaker.getAccessUsername());
                System.out.println((i + 1) + ".   " + s.getSessionID() + "   | " +
                        s.getTopic() + " | " +
                        s.getTime() + " | " +
                        status);
            }

            System.out.println("\n0. Back to Main Menu");
            System.out.print("Select session number to respond (or 0 to exit): ");
            int choice = scan.nextInt();
            scan.nextLine();

            if (choice == 0) {
                continueManaging = false;
            } else if (choice >= 1 && choice <= assignedSessions.size()) {
                Session selectedSession = assignedSessions.get(choice - 1);
                String currentStatus = selectedSession.getSpeakerStatus(speaker.getAccessUsername());

                if (!"pending".equals(currentStatus)) {
                    System.out.println("You have already " + currentStatus + " this session.");
                    System.out.println("Press Enter to continue...");
                    scan.nextLine();
                    continue;
                }

                // Show session details and ask for response
                System.out.println("\n=== Session Details ===");
                System.out.println("Conference: " + getConferenceName(events, selectedSession));
                System.out.println("Session ID: " + selectedSession.getSessionID());
                System.out.println("Topic: " + selectedSession.getTopic());
                System.out.println("Time: " + selectedSession.getTime());
                System.out.println("\nDo you want to ACCEPT or REJECT this session?");
                System.out.println("1. ACCEPT");
                System.out.println("2. REJECT");
                System.out.print("Enter your choice (1/2): ");
                int response = scan.nextInt();
                scan.nextLine();

                if (response == 1) {
                    selectedSession.acceptInvitation(speaker.getAccessUsername());
                    System.out.println("You have accepted the session: " + selectedSession.getTopic());
                } else if (response == 2) {
                    System.out.print("Please provide a reason for rejection: ");
                    String reason = scan.nextLine();
                    selectedSession.rejectInvitation(speaker.getAccessUsername(), reason);
                    System.out.println("You have rejected the session: " + selectedSession.getTopic());
                } else {
                    System.out.println("Invalid choice.");
                }

                System.out.println("\nPress Enter to continue...");
                scan.nextLine();
            } else {
                System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    // View my assigned sessions (view only)
    static void viewMyAssignedSessions(Speaker speaker, Event[] events) {
        System.out.println("\n=== My Assigned Sessions ===");
        boolean hasSessions = false;

        for (Event e : events) {
            if (e != null && e instanceof Conference) {
                Conference conf = (Conference) e;
                for (int i = 0; i < conf.getSessionCount(); i++) {
                    Session session = conf.getSessions()[i];
                    if (session.hasSpeaker(speaker.getAccessUsername())) {
                        hasSessions = true;
                        String status = session.getSpeakerStatus(speaker.getAccessUsername());
                        System.out.println("\nConference: " + conf.getTitle());
                        System.out.println("  Session ID: " + session.getSessionID());
                        System.out.println("  Topic: " + session.getTopic());
                        System.out.println("  Time: " + session.getTime());
                        System.out.println("  Status: " + status);
                        if ("rejected".equals(status)) {
                            String reason = session.getRejectionReason(speaker.getAccessUsername());
                            if (!reason.isEmpty()) {
                                System.out.println("  Rejection Reason: " + reason);
                            }
                        }
                    }
                }
            }
        }

        if (!hasSessions) {
            System.out.println("You are not assigned to any sessions.");
        }
    }

    // View editable sessions (accepted sessions only)
    static void viewEditableSessions(Speaker speaker, Event[] events) {
        System.out.println("\n=== Sessions You Can Edit (Accepted) ===");
        boolean hasEditable = false;
        int count = 0;

        for (Event e : events) {
            if (e != null && e instanceof Conference) {
                Conference conf = (Conference) e;
                for (int i = 0; i < conf.getSessionCount(); i++) {
                    Session session = conf.getSessions()[i];
                    if (session.hasSpeaker(speaker.getAccessUsername())) {
                        String status = session.getSpeakerStatus(speaker.getAccessUsername());
                        if ("accepted".equals(status)) {
                            hasEditable = true;
                            count++;
                            System.out.println("\n" + count + ". Conference: " + conf.getTitle());
                            System.out.println("   Session ID: " + session.getSessionID());
                            System.out.println("   Current Topic: " + session.getTopic());
                            System.out.println("   Time: " + session.getTime());
                        }
                    }
                }
            }
        }

        if (!hasEditable) {
            System.out.println("You have no accepted sessions to edit.");
            System.out.println("Note: You must accept a session first before you can update its topic.");
        }
    }

    // Update session topic
    static void updateSessionTopic(Speaker speaker, Event[] events) {
        viewEditableSessions(speaker, events);

        System.out.print("\nEnter Session ID to update topic: ");
        String sessionId = scan.nextLine();

        // Find the session
        Session targetSession = null;
        for (Event e : events) {
            if (e != null && e instanceof Conference) {
                Conference conf = (Conference) e;
                for (int i = 0; i < conf.getSessionCount(); i++) {
                    Session s = conf.getSessions()[i];
                    if (s.getSessionID().equals(sessionId)) {
                        targetSession = s;
                        break;
                    }
                }
            }
            if (targetSession != null)
                break;
        }

        if (targetSession == null) {
            System.out.println("Session not found!");
            return;
        }

        System.out.print("Enter new topic: ");
        String newTopic = scan.nextLine();

        speaker.uploadSessionTopic(speaker.getAccessUsername(), targetSession, newTopic);
    }

    // Update speaker bio
    static void updateSpeakerBio(Speaker speaker) {
        System.out.println("\n--- Update Your Bio ---");
        System.out.println("Current Bio: " + speaker.getBio());
        System.out.print("Enter new bio: ");
        String newBio = scan.nextLine();

        if (newBio != null && !newBio.trim().isEmpty()) {
            boolean success = speaker.uploadBio(speaker.getAccessUsername(), newBio);
            if (success) {
                System.out.println("Bio updated successfully!");
            } else {
                System.out.println("Failed to update bio.");
            }
        } else {
            System.out.println("Bio cannot be empty. Update cancelled.");
        }
    }

    // Helper method to get conference name
    static String getConferenceName(Event[] events, Session session) {
        for (Event e : events) {
            if (e != null && e instanceof Conference) {
                Conference conf = (Conference) e;
                for (int i = 0; i < conf.getSessionCount(); i++) {
                    if (conf.getSessions()[i] == session) {
                        return conf.getTitle();
                    }
                }
            }
        }
        return "Unknown Conference";
    }

    // Display single speaker info
    static void displaySpeakerInfo(Speaker speaker) {
        System.out.println("\n=== Speaker Info ===");
        System.out.println("Username: " + speaker.getAccessUsername());
        System.out.println("Email: " + speaker.getAccessEmail());
        System.out.println("Bio: " + speaker.getBio());
    }

}
