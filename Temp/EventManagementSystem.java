import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EventManagementSystem {

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