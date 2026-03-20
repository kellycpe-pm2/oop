import java.util.Scanner;

public class TestEvent {

    static Scanner scan = new Scanner(System.in);

    // shared data across all menus
    static Speaker[] speakerPool = new Speaker[100];
    static int speakerCount = 0;
    static Organizer organizer = null;

    static Event[] events = new Event[100];
    static int eventCount = 0;

    static Session[] allSessions = new Session[200];
    static int sessionCount = 0;

    // =========================================================================
    // MAIN
    // =========================================================================
    public static void main(String[] args) {
        // ── Load all existing data from json files on startup ──────────────
        Speaker tempSp = new Speaker();
        tempSp.readSpeakerData();
        // populate speakerPool from what was loaded into static arrays
        int loadedSpeakers = tempSp.getNo();
        if (tempSp.getUsername(0) != null) {
            for (int i = 0; i <= loadedSpeakers; i++) {
                speakerPool[i] = new Speaker(
                        tempSp.getUsername(i),
                        tempSp.getPassword(i),
                        tempSp.getEmail(i),
                        tempSp.getBio(i));
                speakerCount++;
            }
        }

        Organizer tempOrg = new Organizer();
        tempOrg.readOrganizerData();
        if (tempOrg.getUsername(0) != null) {
            organizer = new Organizer(
                    tempOrg.getUsername(0),
                    tempOrg.getPassword(0),
                    tempOrg.getEmail(0),
                    tempOrg.getContactNo(0));
        }

        if (speakerCount > 0 || organizer != null) {
            System.out.println("Existing data loaded successfully.");
        }
        // ──────────────────────────────────────────────────────────────────
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║        MAIN MENU             ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1: Organizer                ║");
            System.out.println("║  2: Speaker                  ║");
            System.out.println("║  3: Display All Records      ║");
            System.out.println("║  0: Exit                     ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print("Enter option: ");
            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    organizerMenu();
                    break;
                case 2:
                    speakerMenu();
                    break;
                case 3:
                    displayAll();
                    break;
                case 0:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
        scan.close();
    }

    // =========================================================================
    // ORGANIZER MENU
    // =========================================================================
    static void organizerMenu() {
        // Register organizer once only
        if (organizer == null) {
            System.out.println("\n--- Register Organizer ---");
            System.out.print("Username     : ");
            String username = scan.nextLine();
            System.out.print("Password     : ");
            String password = scan.nextLine();
            System.out.print("Email        : ");
            String email = scan.nextLine();
            System.out.print("Contact No   : ");
            String contact = scan.nextLine();
            organizer = new Organizer(username, password, email, contact);
            organizer.storeOrganizerData();
            System.out.println("Organizer registered successfully!");
        }

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
                    createSession();
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
    static void createSession() {
        if (eventCount == 0) {
            System.out.println("No events available.");
            return;
        }

        System.out.println("\n--- Create Session (Conference only) ---");
        boolean hasConference = false;
        for (int i = 0; i < eventCount; i++) {
            if (events[i] instanceof Conference) {
                System.out.println("  " + (i + 1) + ": [" + events[i].getEventID() + "] " + events[i].getTitle());
                hasConference = true;
            }
        }
        if (!hasConference) {
            System.out.println("No Conference events found. Create a Conference event first.");
            return;
        }

        System.out.print("Select Conference number: ");
        int idx = scan.nextInt() - 1;
        scan.nextLine();

        if (idx < 0 || idx >= eventCount || !(events[idx] instanceof Conference)) {
            System.out.println("Invalid selection or not a Conference.");
            return;
        }

        Conference conf = (Conference) events[idx];

        // Session ID is auto-generated — only ask topic and time
        System.out.print("Topic : ");
        String topic = scan.nextLine();
        System.out.print("Time  : ");
        String time = scan.nextLine();
        Session sess = conf.createSession(topic, time);

        if (sess != null) {
            System.out.println("Session created! ID assigned: " + sess.getSessionID());

            // assign speakers from pool
            if (speakerCount > 0) {
                System.out.println("Available Speakers:");
                for (int k = 0; k < speakerCount; k++) {
                    System.out.println("  " + (k + 1) + ": "
                            + speakerPool[k].getUsername(0));
                }
                System.out.print("How many speakers to assign? ");
                int numSp = scan.nextInt();
                scan.nextLine();
                for (int sp = 0; sp < numSp; sp++) {
                    System.out.print("  Select speaker: ");
                    int spOpt = scan.nextInt();
                    scan.nextLine();
                    if (spOpt >= 1 && spOpt <= speakerCount) {
                        sess.assignSpeaker(speakerPool[spOpt - 1]);
                    } else {
                        System.out.println("Invalid speaker selection.");
                    }
                }
            } else {
                System.out.println("No speakers registered yet. Session created without speakers.");
            }

            allSessions[sessionCount++] = sess;
        }
    }

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

    // =========================================================================
    // SPEAKER MENU
    // =========================================================================
    static void speakerMenu() {
        System.out.println("\n--- Speaker Login / Register ---");
        System.out.println("1: Register as new Speaker");
        System.out.println("2: Login as existing Speaker");
        System.out.print("Enter option: ");
        int loginChoice = scan.nextInt();
        scan.nextLine();

        String loggedInUsername = null;
        Speaker loggedInSpeaker = null;

        if (loginChoice == 1) {
            System.out.print("Username  : ");
            String username = scan.nextLine();
            System.out.print("Password  : ");
            String password = scan.nextLine();
            System.out.print("Email     : ");
            String email = scan.nextLine();
            System.out.print("Bio       : ");
            String bio = scan.nextLine();
            Speaker sp = new Speaker(username, password, email, bio);
            sp.storeSpeakerData();
            speakerPool[speakerCount++] = sp;
            loggedInUsername = username;
            loggedInSpeaker = sp;
            System.out.println("Speaker registered successfully!");

        } else if (loginChoice == 2) {
            System.out.print("Username : ");
            String username = scan.nextLine();
            System.out.print("Password : ");
            String password = scan.nextLine();
            for (int i = 0; i < speakerCount; i++) {
                if (speakerPool[i].getUsername(0) != null
                        && speakerPool[i].getUsername(0).equals(username)
                        && speakerPool[i].getPassword(0) != null
                        && speakerPool[i].getPassword(0).equals(password)) {
                    loggedInUsername = username;
                    loggedInSpeaker = speakerPool[i];
                    break;
                }
            }
            if (loggedInSpeaker == null) {
                System.out.println("Invalid username or password.");
                return;
            }
            System.out.println("Welcome back, " + loggedInUsername + "!");

        } else {
            System.out.println("Invalid option.");
            return;
        }

        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║       SPEAKER MENU           ║");
            System.out.println("║  Logged in: " + String.format("%-17s", loggedInUsername) + "║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1: Update Bio               ║");
            System.out.println("║  2: Update Session Topic     ║");
            System.out.println("║  3: View My Info             ║");
            System.out.println("║  0: Back to Main Menu        ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print("Enter option: ");
            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                    uploadBio(loggedInSpeaker, loggedInUsername);
                    break;
                case 2:
                    uploadSessionTopic(loggedInSpeaker, loggedInUsername);
                    break;
                case 3:
                    System.out.println("\n--- Your Info ---");
                    System.out.printf("%-15s %-25s %-30s%n", "Username", "Email", "Bio");
                    System.out.println("--------------------------------------------------------------------");
                    System.out.println(loggedInSpeaker.toString(0));
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ── Upload / Update Bio ───────────────────────────────────────────────────
    static void uploadBio(Speaker sp, String username) {
        System.out.println("\n--- Update Bio ---");
        System.out.println("Current Bio : " + sp.getBio(0));
        System.out.print("Enter new bio: ");
        String newBio = scan.nextLine();
        sp.uploadBio(username, newBio);
    }

    // ── Upload / Update Session Topic ─────────────────────────────────────────
    static void uploadSessionTopic(Speaker sp, String username) {
        System.out.println("\n--- Update Session Topic ---");
        if (sessionCount == 0) {
            System.out.println("No sessions available.");
            return;
        }

        // show only sessions this speaker is assigned to
        boolean assigned = false;
        for (int i = 0; i < sessionCount; i++) {
            Speaker[] assigned_sp = allSessions[i].getSpeakers();
            for (int j = 0; j < allSessions[i].getSpeakerCount(); j++) {
                if (assigned_sp[j] != null
                        && assigned_sp[j].getUsername(0) != null
                        && assigned_sp[j].getUsername(0).equals(username)) {
                    System.out.println("  " + (i + 1) + ": ["
                            + allSessions[i].getSessionID() + "] "
                            + allSessions[i].getTopic()
                            + " @ " + allSessions[i].getTime());
                    assigned = true;
                    break;
                }
            }
        }

        if (!assigned) {
            System.out.println("You are not assigned to any session.");
            return;
        }

        System.out.print("Select session number: ");
        int sessOpt = scan.nextInt() - 1;
        scan.nextLine();

        if (sessOpt < 0 || sessOpt >= sessionCount) {
            System.out.println("Invalid selection.");
            return;
        }

        System.out.print("Enter new topic: ");
        String newTopic = scan.nextLine();
        sp.uploadSessionTopic(username, allSessions[sessOpt], newTopic);
    }

    // =========================================================================
    // DISPLAY ALL RECORDS
    // =========================================================================
    static void displayAll() {
        System.out.println("\n==================== SPEAKERS ====================");
        if (speakerCount == 0) {
            System.out.println("  No speakers registered.");
        } else {
            System.out.printf("%-15s %-25s %-30s%n", "Username", "Email", "Bio");
            System.out.println("--------------------------------------------------------------------");
            for (int i = 0; i < speakerCount; i++) {
                System.out.println(speakerPool[i].toString(0));
            }
        }

        System.out.println("\n==================== ORGANIZER ====================");
        if (organizer == null) {
            System.out.println("  No organizer registered.");
        } else {
            System.out.printf("%-15s %-25s %-15s%n", "Username", "Email", "Contact No");
            System.out.println("----------------------------------------------------");
            System.out.println(organizer.toString(0));
        }

        System.out.println("\n==================== EVENTS ====================");
        if (eventCount == 0) {
            System.out.println("  No events created.");
        } else {
            for (int i = 0; i < eventCount; i++) {
                events[i].displayInfo();
                System.out.println();
            }
        }
    }
}