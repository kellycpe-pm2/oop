import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EventManagementSystem {
    private static User [] user = new User [100];
    private static Ticket [] ticket = new Ticket[100];
    private static Payment [] payment = new Payment[100];
    private static int bookingno = 0; 
        // ── 2D array: events[TYPE][index]
    // events[CONCERT][...] → Concert objects
    // events[WORKSHOP][...] → Workshop objects
    // events[CONFERENCE][...] → Conference objects
    private static final int CONCERT = 0;
    private static final int WORKSHOP = 1;
    private static final int CONFERENCE = 2;

    private static final int MAX_EVENTS = 100;

    private Event[][] events = new Event[3][MAX_EVENTS];
    private int[] eventCount = new int[3]; // count per type
    
//get methods for Events
// Get all events across every type (flat array)
    public Event[] getActiveEvents() {
        int total = eventCount[CONCERT] + eventCount[WORKSHOP] + eventCount[CONFERENCE];
        Event[] active = new Event[total];
        int idx = 0;
        for (int type = 0; type < 3; type++) {
            for (int i = 0; i < eventCount[type]; i++) {
                active[idx++] = events[type][i];
            }
        }
        return active;
    }

    // Get only Concert events
    public Concert[] getActiveConcerts() {
        Concert[] result = new Concert[eventCount[CONCERT]];
        for (int i = 0; i < eventCount[CONCERT]; i++) {
            result[i] = (Concert) events[CONCERT][i];
        }
        return result;
    }

    // Get only Workshop events
    public Workshop[] getActiveWorkshops() {
        Workshop[] result = new Workshop[eventCount[WORKSHOP]];
        for (int i = 0; i < eventCount[WORKSHOP]; i++) {
            result[i] = (Workshop) events[WORKSHOP][i];
        }
        return result;
    }

    // Get only Conference events
    public Conference[] getActiveConferences() {
        Conference[] result = new Conference[eventCount[CONFERENCE]];
        for (int i = 0; i < eventCount[CONFERENCE]; i++) {
            result[i] = (Conference) events[CONFERENCE][i];
        }
        return result;
    }

    // Get a single event by eventID (searches all types)
    public Event getEventById(String eventID) {
        for (int type = 0; type < 3; type++) {
            for (int i = 0; i < eventCount[type]; i++) {
                if (events[type][i] != null && events[type][i].getEventID().equals(eventID)) {
                    return events[type][i];
                }
            }
        }
        System.out.println("Error: Event [" + eventID + "] not found !");
        return null;
    }

    // Total number of events across all types
    public int getEventCount() {
        return eventCount[CONCERT] + eventCount[WORKSHOP] + eventCount[CONFERENCE];
    }
//------------------------------------------------------------------------Get event end here
    public boolean validationInputTicketType(int choice) {
        if (choice == 1 || choice == 2 || choice == 3) {
            return true;
        }
        return false;
    }

    
    //----------------------------------------------------------------------------------------
    //Staff Part
    //---------------------------------------------------------------------------------------

    
    
    //generate Booking No
    public String generateBookingId(String eventId){
        // get eventID and then Remove the first character, then convert to int
        int eventNo = Integer.parseInt(eventId.substring(1));
        // to increase the booking no  
        bookingno++;
        return "B" + String.format("%03d", bookingno);
    }

    //----------------------------------------------------------------------------------------
    
    
    // -------------------------- validation for event ---------------------------

    // validate title (must not be empty and at least 3 characters)
    public boolean validationTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Error: Title cannot be empty !");
            return false;
        } else if (title.trim().length() < 3) {
            System.out.println("Error: Title must be at least 3 characters !");
            return false;
        } else {
            return true;
        }
    }

    // validate date string (must be YYYY-MM-DD format and a future date)
    // returns the parsed LocalDate if valid, or null if invalid
    public LocalDate validationDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            System.out.println("Error: Date cannot be empty !");
            return null;
        }
        try {
            LocalDate parsedDate = LocalDate.parse(date.trim());
            if (!parsedDate.isAfter(LocalDate.now())) {
                System.out.println("Error: Event date must be in the future !");
                return null;
            }
            return parsedDate;
        } catch (DateTimeParseException e) {
            System.out.println("Error: Date format must be YYYY-MM-DD !");
            return null;
        }
    }

    // validate venue (must not be empty)
    public boolean validationVenue(String venue) {
        if (venue == null || venue.trim().isEmpty()) {
            System.out.println("Error: Venue cannot be empty !");
            return false;
        } else {
            return true;
        }
    }

    // validate max tickets (must be greater than 0)
    public boolean validationMaxTickets(int maxTickets) {
        if (maxTickets <= 0) {
            System.out.println("Error: Max tickets must be greater than 0 !");
            return false;
        } else {
            return true;
        }
    }

    // validate session time (must be HHMM format, 0000 - 2359)
    public boolean validationSessionTime(String time) {
        if (time == null || time.trim().isEmpty()) {
            System.out.println("Error: Session time cannot be empty !");
            return false;
        }
        if (time.trim().length() != 4) {
            System.out.println("Error: Session time must be 4 digits in HHMM format (e.g. 0900, 1430) !");
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (!Character.isDigit(time.trim().charAt(i))) {
                System.out.println("Error: Session time must contain digits only (e.g. 0900, 1430) !");
                return false;
            }
        }
        int hh = Integer.parseInt(time.trim().substring(0, 2));
        int mm = Integer.parseInt(time.trim().substring(2, 4));
        if (hh < 0 || hh > 23) {
            System.out.println("Error: Hour must be between 00 and 23 !");
            return false;
        }
        if (mm < 0 || mm > 59) {
            System.out.println("Error: Minute must be between 00 and 59 !");
            return false;
        }
        return true;
    }
        public boolean validationInputEventId(Event[] events, String eventId) {
        for (Event e : events) {
            if (e != null && eventId.equals(e.getEventID())) {
                return true;
            }
        }
        return false;
    }

    // -------------------------- create event ---------------------------

     // Creates a Concert and stores it in events[CONCERT][]
    public Concert createConcert(String title, String date, String venue, int maxTickets) {
        if (!validationTitle(title))
            return null;
        LocalDate parsedDate = validationDate(date);
        if (parsedDate == null)
            return null;
        if (!validationVenue(venue))
            return null;
        if (!validationMaxTickets(maxTickets))
            return null;
        if (eventCount[CONCERT] >= MAX_EVENTS) {
            System.out.println("Error: Concert list is full !");
            return null;
        }

        Concert c = new Concert(title, parsedDate, venue, maxTickets);
        events[CONCERT][eventCount[CONCERT]] = c;
        eventCount[CONCERT]++;
        System.out.println("Concert created and stored successfully : " + c.getEventID());
        return c;
    }

    // Creates a Workshop and stores it in events[WORKSHOP][]
    public Workshop createWorkshop(String title, String date, String venue, int maxTickets) {
        if (!validationTitle(title))
            return null;
        LocalDate parsedDate = validationDate(date);
        if (parsedDate == null)
            return null;
        if (!validationVenue(venue))
            return null;
        if (!validationMaxTickets(maxTickets))
            return null;
        if (eventCount[WORKSHOP] >= MAX_EVENTS) {
            System.out.println("Error: Workshop list is full !");
            return null;
        }

        Workshop w = new Workshop(title, parsedDate, venue, maxTickets);
        events[WORKSHOP][eventCount[WORKSHOP]] = w;
        eventCount[WORKSHOP]++;
        System.out.println("Workshop created and stored successfully : " + w.getEventID());
        return w;
    }

    // Creates a Conference (with optional sessions) and stores it in
    // events[CONFERENCE][]
    public Conference createConference(String title, String date, String venue, int maxTickets,
            String[] sessionTopics, String[] sessionTimes) {
        if (!validationTitle(title))
            return null;
        LocalDate parsedDate = validationDate(date);
        if (parsedDate == null)
            return null;
        if (!validationVenue(venue))
            return null;
        if (!validationMaxTickets(maxTickets))
            return null;
        if (eventCount[CONFERENCE] >= MAX_EVENTS) {
            System.out.println("Error: Conference list is full !");
            return null;
        }

        Conference conf = new Conference(title, parsedDate, venue, maxTickets);
        if (sessionTopics != null && sessionTimes != null) {
            conf.autoCreateSessions(sessionTopics, sessionTimes);
        }
        events[CONFERENCE][eventCount[CONFERENCE]] = conf;
        eventCount[CONFERENCE]++;
        System.out.println("Conference created and stored successfully : " + conf.getEventID());
        return conf;
    }

    // Overload — create Conference without sessions
    public Conference createConference(String title, String date, String venue, int maxTickets) {
        return createConference(title, date, venue, maxTickets, null, null);
    }

    // -------------------------- assign speaker ---------------------------

    public boolean assignSpeaker(Session session, Speaker speaker) {
        if (session == null) {
            System.out.println("Error: Session not found !");
            return false;
        }
        if (speaker == null) {
            System.out.println("Error: Speaker not found !");
            return false;
        }
        if (session.addSpeaker(speaker)) {
            System.out.println("Speaker [" + speaker.getAccessUsername() + "] assigned to session ["
                    + session.getSessionID() + "] successfully.");
            return true;
        }
        return false;
    }

    public boolean removeSpeaker(Session session, String speakerUsername) {
        if (session == null) {
            System.out.println("Error: Session not found !");
            return false;
        }
        return session.removeSpeaker(speakerUsername);
    }

//ticket type part
        // validate the quantity set
    public boolean validationQuantityTicket(int totalQuantity, int quantityEarlyBird, int quantityStandard, int quantityVip) {
        if (totalQuantity == quantityEarlyBird + quantityStandard + quantityVip) {
            return true;
        }
        else{
            System.out.println("Sum of ticket type quantities not equal to totalQuantity. Please reset the quantity of ticket.");
            return false;
        }
    }

    // validate date set
    public boolean validationDate(LocalDate salesStart, LocalDate salesEnd, LocalDate earlyBirdEnd) {
        if (salesStart.isAfter(salesEnd)) {
            System.out.println("Sales start date must be before sales end date.");
            return false;
        }
        if (earlyBirdEnd.isBefore(salesStart) || earlyBirdEnd.isAfter(salesEnd)) {
            System.out.println("Early bird end date must be between sales start and sales end dates.");
            return false;
        }
        return true;
    }

    // validate price
    public boolean validationPrice(double priceEarlyBird, double priceStandard, double priceVip) {
        if (priceEarlyBird < 0 || priceStandard < 0 || priceVip < 0) {
            System.out.println("Ticket prices cannot be negative.");
            return false;
        }
        if (priceVip <= priceStandard) {
            System.out.println("VIP price should be greater than Standard price.");
            return false;
        }
        if (priceStandard <= priceEarlyBird) {
            System.out.println("Standard price should be greater than Early Bird price.");
            return false;
        }
        return true;
    }

    public boolean validationPerks(String perks) {
        if (perks == null || perks.trim().isEmpty()) {
            System.out.println("Error: Perks cannot be empty !");
            return false;
        } else {
            return true;
        }
    }

    public LocalDate validationSalesStartDate(String ssdate, LocalDate eventDate) {
        if (ssdate == null || ssdate.trim().isEmpty()) {
            System.out.println("Error: Sales Start Date cannot be empty !");
            return null;
        }
        try {
            LocalDate parsedDate = LocalDate.parse(ssdate.trim());
            if (parsedDate.isAfter(eventDate) || parsedDate.isEqual(eventDate)) {
                System.out.println("Error: Sales Start Date must before event date !");
                return null;
            }
            return parsedDate;
        } catch (DateTimeParseException e) {
            System.out.println("Error: Date format must be YYYY-MM-DD !");
            return null;
        }
    }

    public LocalDate validationSalesEndDate(String sedate, LocalDate salesStartDate, LocalDate eventDate) {
        if (sedate == null || sedate.trim().isEmpty()) {
            System.out.println("Error: Sales End Date cannot be empty !");
            return null;
        }
        try {
            LocalDate parsedDate = LocalDate.parse(sedate.trim());
            if (parsedDate.isAfter(eventDate) || parsedDate.isEqual(eventDate)) {
                System.out.println("Error: Sales End Date must before event date !");
                return null;
            }
            if (parsedDate.isBefore(salesStartDate) || parsedDate.isEqual(salesStartDate)){
                System.out.println("Error: Sales End Date must after sales start date !");
                return null; 
            }
            return parsedDate;
        } catch (DateTimeParseException e) {
            System.out.println("Error: Date format must be YYYY-MM-DD !");
            return null;
        }
    }

    public Event findEventById(Event[] events, String eventId) {
        for (Event e : events) {
            if (e != null && e.getEventID().equals(eventId)) {
                return e;
            }
        }
        return null;
    }

// In EventManagementSystem.java
    public Ticket purchaseTicket(Attendee attendee, Event event, TicketType tt, String ticketTypeName, String bookingId) {    
    
    if (tt == null) {
        System.out.println("Error: TicketType cannot be null!");
        return null;
    }
    
    Ticket ticket = new Ticket(tt, ticketTypeName, bookingId, event.getEventID());
    
    return ticket;
}
}
