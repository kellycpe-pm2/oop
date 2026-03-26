import java.time.LocalDate;

public abstract class Event {
    private String eventID;
    private String title;
    private LocalDate date;
    private String venue;
    private int maxTickets;
    private TicketType ticketType;

    // auto-generate eventID
    private static int eventCounter = 1;

    private static String generateEventID() {
        return String.format("E%03d", eventCounter++);
    }

    public Event(String title, LocalDate date, String venue, int maxTickets) {
        this.eventID = generateEventID();
        this.title = title;
        this.date = date;
        this.venue = venue;
        this.maxTickets = maxTickets;
    }

    // Getters
    public String getEventID() {
        return eventID;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getVenue() {
        return venue;
    }

    public int getMaxTickets() {
        return maxTickets;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    // Setters
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setMaxTickets(int maxTickets) {
        this.maxTickets = maxTickets;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    // abstract method — subclasses must implement
    public abstract void displayInfo();

    @Override
    public String toString() {
        return String.format("%-6s %-20s %-12s %-20s %-8d",
                eventID, title, date, venue, maxTickets);
    }
}