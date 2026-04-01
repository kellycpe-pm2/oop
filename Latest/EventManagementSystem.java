import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EventManagementSystem {
        // --------------------------array
    private static final int MAX_EVENTS = 100;

    private Event[] events = new Event[MAX_EVENTS];
    private int eventCount = 0;

    // -------------------------- add method ---------------------------

    // add any Event to the array
    public boolean addEvent(Event event) {
        if (event == null) {
            System.out.println("Error: Event cannot be null !");
            return false;
        }
        if (eventCount >= MAX_EVENTS) {
            System.out.println("Error: Event list is full !");
            return false;
        }
        events[eventCount] = event;
        eventCount++;
        System.out.println("Event [" + event.getEventID() + "] added to the system.");
        return true;
    }

    // -------------------------- get methods ---------------------------

    // get all events (Concert + Workshop + Conference) combined
    public Event[] getActiveEvents() {
        Event[] active = new Event[eventCount];
        for (int i = 0; i < eventCount; i++) {
            active[i] = events[i];
        }
        return active;
    }

    // get only Concert events
    public Concert[] getActiveConcerts() {
        int count = 0;
        for (int i = 0; i < eventCount; i++) {
            if (events[i] instanceof Concert)
                count++;
        }
        Concert[] result = new Concert[count];
        int j = 0;
        for (int i = 0; i < eventCount; i++) {
            if (events[i] instanceof Concert) {
                result[j++] = (Concert) events[i];
            }
        }
        return result;
    }

    // get only Workshop events
    public Workshop[] getActiveWorkshops() {
        int count = 0;
        for (int i = 0; i < eventCount; i++) {
            if (events[i] instanceof Workshop)
                count++;
        }
        Workshop[] result = new Workshop[count];
        int j = 0;
        for (int i = 0; i < eventCount; i++) {
            if (events[i] instanceof Workshop) {
                result[j++] = (Workshop) events[i];
            }
        }
        return result;
    }

    // get only Conference events
    public Conference[] getActiveConferences() {
        int count = 0;
        for (int i = 0; i < eventCount; i++) {
            if (events[i] instanceof Conference)
                count++;
        }
        Conference[] result = new Conference[count];
        int j = 0;
        for (int i = 0; i < eventCount; i++) {
            if (events[i] instanceof Conference) {
                result[j++] = (Conference) events[i];
            }
        }
        return result;
    }

    // get a single event by eventID (any type)
    public Event getEventById(String eventID) {
        for (int i = 0; i < eventCount; i++) {
            if (events[i] != null && events[i].getEventID().equals(eventID)) {
                return events[i];
            }
        }
        System.out.println("Error: Event [" + eventID + "] not found !");
        return null;
    }

    // get total number of events stored
    public int getEventCount() {
        return eventCount;
    }

    // validate the quantity set
    public boolean validationQuantityTicket(int totalQuantity, int quantityEarlyBird, int quantityStandard, int quantityVip) {
        if (totalQuantity < quantityEarlyBird + quantityStandard + quantityVip) {
            System.out.println(
                    "Sum of ticket type quantities exceeds totalQuantity. Please reset the quantity of ticket.");
            return false;
        }
        if (totalQuantity > quantityEarlyBird + quantityStandard + quantityVip) {
            System.out.println(
                    "Sum of ticket type quantities less than totalQuantity. Please reset the quantity of ticket.");
            return false;
        } else {
            return true;
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

    public boolean validationInputEventId(Event[] events, String eventId) {
        for (Event e : events) {
            if (e != null && eventId.equals(e.getEventID())) {
                return true;
            }
        }
        return false;
    }

    public Event findEventById(Event[] events, String eventId) {
        for (Event e : events) {
            if (e != null && e.getEventID().equals(eventId)) {
                return e;
            }
        }
        return null;
    }

    public boolean validationInputTicketType(int choice) {
        if (choice == 1 || choice == 2 || choice == 3) {
            return true;
        }
        return false;
    }

    public boolean payment(double amount) {
        System.out.println("\nThe total amount = RM " + amount);
        System.out.println(
                "Payment Method\n1. Touch N Go\n2.Credit/Debit Card\n3.Online Banking\nSelect your payment method:");
        return false;
    }

    public Ticket purchaseTicket(Attendee attendee, Event event, String type) {
        TicketType tt = event.getTicketType();
        Ticket ticket = new Ticket(tt, type, null, event.getEventID(), LocalDate.now());

        if (ticket.validationTicket()) {
            attendee.addTicket(ticket);
            System.out.println("Purchase successful!");
            return ticket;
        } else {
            System.out.println("Purchase failed.");
            return null;
        }
    }

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

    // -------------------------- create event ---------------------------

    public Concert createConcert(String title, String date, String venue, int maxTickets) {
        if (!validationTitle(title)) {
            return null;
        }
        LocalDate parsedDate = validationDate(date);
        if (parsedDate == null) {
            return null;
        }
        if (!validationVenue(venue)) {
            return null;
        }
        if (!validationMaxTickets(maxTickets)) {
            return null;
        }
        Concert c = new Concert(title, parsedDate, venue, maxTickets);
        System.out.println("Concert created successfully : " + c.getEventID());
        return c;
    }

    public Workshop createWorkshop(String title, String date, String venue, int maxTickets) {
        if (!validationTitle(title)) {
            return null;
        }
        LocalDate parsedDate = validationDate(date);
        if (parsedDate == null) {
            return null;
        }
        if (!validationVenue(venue)) {
            return null;
        }
        if (!validationMaxTickets(maxTickets)) {
            return null;
        }
        Workshop w = new Workshop(title, parsedDate, venue, maxTickets);
        System.out.println("Workshop created successfully : " + w.getEventID());
        return w;
    }

    public Conference createConference(String title, String date, String venue, int maxTickets, String[] sessionTopics,
            String[] sessionTimes) {
        if (!validationTitle(title)) {
            return null;
        }
        LocalDate parsedDate = validationDate(date);
        if (parsedDate == null) {
            return null;
        }
        if (!validationVenue(venue)) {
            return null;
        }
        if (!validationMaxTickets(maxTickets)) {
            return null;
        }
        Conference conf = new Conference(title, parsedDate, venue, maxTickets);
        // auto-create sessions if provided
        if (sessionTopics != null && sessionTimes != null) {
            conf.autoCreateSessions(sessionTopics, sessionTimes);
        }
        System.out.println("Conference created successfully : " + conf.getEventID());
        return conf;
    }

    // overload — create conference without sessions
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

}
