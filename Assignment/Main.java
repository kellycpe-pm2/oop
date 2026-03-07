import java.util.*;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static ArrayList<Organizer> organizerList = new ArrayList<>();
    private static ArrayList<Speaker> speakerList = new ArrayList<>();

    public static void main(String[] args) {

        // Initialize with some sample speakers
        initializeSampleSpeakers();

        int choice;

        do {
            displayMainMenu();
            choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createOrganizer();
                    break;
                case 2:
                    createEvent();
                    break;
                case 3:
                    viewEvents();
                    break;
                case 4:
                    System.out.println("Thank you for using the Event Management System!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }

        } while (choice != 4);
    }

    private static void displayMainMenu() {
        System.out.println("\n=== EVENT MANAGEMENT SYSTEM ===");
        System.out.println("1. Create Organizer");
        System.out.println("2. Create Event");
        System.out.println("3. View All Events");
        System.out.println("4. Exit");
        System.out.println("================================");
    }

    private static void createOrganizer() {
        System.out.println("\n--- CREATE NEW ORGANIZER ---");

        System.out.print("Enter Organizer ID: ");
        String id = sc.nextLine();

        System.out.print("Enter Organizer Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        Organizer organizer = new Organizer(id, name, email, password);
        organizerList.add(organizer);

        System.out.println("Organizer created successfully!");
    }

    private static void createEvent() {
        System.out.println("\n--- CREATE NEW EVENT ---");

        // Check if there are any organizers
        if (organizerList.isEmpty()) {
            System.out.println("No organizers available. Please create an organizer first.");
            return;
        }

        // Display available organizers
        System.out.println("\nAvailable Organizers:");
        for (int i = 0; i < organizerList.size(); i++) {
            System.out.print((i + 1) + ". ");
            organizerList.get(i).displayInfo();
        }

        int orgChoice = getIntInput("Select organizer (enter number): ") - 1;

        if (orgChoice < 0 || orgChoice >= organizerList.size()) {
            System.out.println("Invalid organizer selection!");
            return;
        }

        Organizer selectedOrganizer = organizerList.get(orgChoice);

        // Get event details
        System.out.print("\nEnter Event ID: ");
        String eventID = sc.nextLine();

        System.out.print("Enter Event Name: ");
        String eventName = sc.nextLine();

        Event event = new Event(eventID, eventName);
        selectedOrganizer.createEvent(event);

        // Ask if user wants to add sessions
        System.out.print("\nDo you want to add sessions to this event? (y/n): ");
        String addSessions = sc.nextLine();

        if (addSessions.equalsIgnoreCase("y")) {
            addSessionsToEvent(event);
        }

        System.out.println("Event created successfully!");
    }

    private static void addSessionsToEvent(Event event) {
        System.out.println("\n--- ADD SESSIONS TO EVENT ---");

        String choice;
        do {
            System.out.print("\nEnter Session ID: ");
            String sessionID = sc.nextLine();

            System.out.print("Enter Session Title: ");
            String title = sc.nextLine();

            Session session = new Session(sessionID, title);
            event.addSession(session);

            // Assign speaker to session
            assignSpeakerToSession(session);

            System.out.print("Add another session? (y/n): ");
            choice = sc.nextLine();

        } while (choice.equalsIgnoreCase("y"));
    }

    private static void assignSpeakerToSession(Session session) {
        if (speakerList.isEmpty()) {
            System.out.println("No speakers available to assign.");
            return;
        }

        System.out.println("\nAvailable Speakers:");
        for (Speaker sp : speakerList) {
            sp.displayInfo();
        }

        System.out.print("Enter Speaker ID to assign (or press Enter to skip): ");
        String spID = sc.nextLine();

        if (!spID.isEmpty()) {
            Speaker selected = null;
            for (Speaker sp : speakerList) {
                if (sp.getUserID().equals(spID)) {
                    selected = sp;
                    break;
                }
            }

            if (selected != null) {
                session.assignSpeaker(selected);
                System.out.println("Speaker assigned successfully!");
            } else {
                System.out.println("Speaker not found!");
            }
        }
    }

    private static void viewEvents() {
        System.out.println("\n--- ALL EVENTS ---");

        if (organizerList.isEmpty()) {
            System.out.println("No organizers found.");
            return;
        }

        boolean hasEvents = false;
        for (Organizer organizer : organizerList) {
            System.out.println("\n" + organizer.getUsername() + "'s Events:");
            organizer.displayEvents();
            hasEvents = true;
        }

        if (!hasEvents) {
            System.out.println("No events have been created yet.");
        }
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.println("Please enter a valid number!");
            sc.next();
            System.out.print(prompt);
        }
        int input = sc.nextInt();
        sc.nextLine(); // Consume newline
        return input;
    }

    private static void initializeSampleSpeakers() {
        speakerList.add(new Speaker("SP1", "Dr Tan", "tan@mail.com", "111"));
        speakerList.add(new Speaker("SP2", "Dr Lim", "lim@mail.com", "222"));
        speakerList.add(new Speaker("SP3", "Prof Wong", "wong@mail.com", "333"));
        speakerList.add(new Speaker("SP4", "Dr Sarah", "sarah@mail.com", "444"));
    }
}