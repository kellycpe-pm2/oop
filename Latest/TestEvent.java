import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TestEvent {
    static Scanner scan = new Scanner(System.in);
    static EventManagementSystem ems = new EventManagementSystem();

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

    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        // load all saved events from files on startup
        loadAllEvents();

        boolean active = true;
        while (active) {
            int option = mainMenu();
            switch (option) {
                case 1:
                    organizerMenu();
                    break;
                case 2:
                    attendeeMenu();
                    break;
                case 0:
                    System.out.println("GoodBye!");
                    active = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MAIN MENU
    // ─────────────────────────────────────────────────────────────────────────
    static int mainMenu() {
        int option = -1;
        do {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("\n \t\t\tEvent Management System");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("\n \t\t\t1. Organizer");
            System.out.println(" \t\t\t2. Attendee");
            System.out.println(" \t\t\t0. Exit");
            System.out.print("Enter Your Option: ");
            option = scan.nextInt();
            scan.nextLine();
            if (option < 0 || option > 2) {
                System.out.println("Invalid option. Try again.");
            }
        } while (option < 0 || option > 2);
        return option;
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
            System.out.println("║  7: Save All Events          ║");
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
                case 7:
                    saveAllEvents();
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
            System.out.print("Max Tickets      : ");
            maxTix = scan.nextInt();
            scan.nextLine();
        } while (!ems.validationMaxTickets(maxTix));

        if (type == 1) {
            // Concert
            Concert c = new Concert(title, parsedDate, venue, maxTix);
            concerts.add(c);
            events[eventCount++] = c;
            System.out.println("Concert created successfully : " + c.getEventID());

        } else if (type == 2) {
            // Workshop
            Workshop w = new Workshop(title, parsedDate, venue, maxTix);
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
                System.out.println("Date updated.");
                break;
            case 3:
                String newVenue;
                do {
                    System.out.print("New Venue       : ");
                    newVenue = scan.nextLine();
                } while (!ems.validationVenue(newVenue));
                e.setVenue(newVenue);
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
        // persist the change
        Conference.storeConferenceData(conferences);
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
    static void saveAllEvents() {
        Concert.storeConcertData(concerts);
        Workshop.storeWorkshopData(workshops);
        Conference.storeConferenceData(conferences);
        System.out.println("All events saved successfully.");
    }

    static void loadAllEvents() {
        concerts.clear();
        workshops.clear();
        conferences.clear();
        eventCount = 0;

        concerts = Concert.readConcertData();
        workshops = Workshop.readWorkshopData();
        conferences = Conference.readConferenceData();

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
    static void attendeeMenu() {
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
                    purchaseTicket();
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
    static void purchaseTicket() {
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

        if (event.getTicketType() == null) {
            System.out.println("Error: No ticket types configured for this event yet.");
            return;
        }

        // pick ticket type
        String ticketType = "";
        while (true) {
            System.out.println("===== Ticket Type =====");
            System.out.println("1. EarlyBird  - RM " + event.getTicketType().getPrice("earlybird"));
            System.out.println("2. Standard   - RM " + event.getTicketType().getPrice("standard"));
            System.out.println("3. VIP        - RM " + event.getTicketType().getPrice("vip"));
            System.out.print("Select ticket type: ");
            int type = scan.nextInt();
            scan.nextLine();
            if (type == 1) {
                ticketType = "earlybird";
                break;
            } else if (type == 2) {
                ticketType = "standard";
                break;
            } else if (type == 3) {
                ticketType = "vip";
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }

        // payment
        System.out.println("\nThe total amount = RM " + event.getTicketType().getPrice(ticketType));
        while (true) {
            System.out.println("Payment Method");
            System.out.println("1. Touch N Go");
            System.out.println("2. Credit/Debit Card");
            System.out.println("3. Online Banking");
            System.out.print("Select your payment method: ");
            int method = scan.nextInt();
            scan.nextLine();
            if (method == 1 || method == 2 || method == 3) {
                System.out.println("Processing payment...");
                System.out.println("Payment Success!");
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }

        // create a temporary attendee for this purchase
        Attendee tempAttendee = new Attendee("guest", "", "", "");
        Ticket ticket = ems.purchaseTicket(tempAttendee, event, ticketType);
        if (ticket != null) {
            tempAttendee.displayHistory();
        }
    }
}

//speaker part
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
    List<Session> assignedSessions = new ArrayList<>();
    
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
        if (targetSession != null) break;
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
