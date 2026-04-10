import java.time.LocalDate;

public abstract class Event {
    private String eventID;
    private String title;
    private LocalDate date;
    private String venue;
    private int maxTickets;

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

    public static int getEventCounter(){
        return Event.eventCounter;
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

    // abstract method — subclasses must implement
    public abstract void displayInfo();
    
    public abstract boolean isConcert();

    public abstract boolean isConference();
    
    public abstract boolean isWorkshop();

    @Override
    public String toString() {
        return String.format("\t\t\t│ %-10s │ %-18s │ %-17s │ %-10s │",
                            getEventID(),
                            getTitle(),
                            getVenue(),
                            getDate());
    }
    public boolean equals(Object o) {
       if(this == o){return true;}
       if (o==null){
            return false;
        }
        if (o instanceof Event) {
            Event e = (Event) o;
            return this.eventID.equals(e.eventID);
        }
        return false; // the object does Not belong to Event
    }

    public boolean hasEvent(String eventId){
        if (this.eventID.equals(eventId)){
            return true;
        }
        return false;
    }
}
