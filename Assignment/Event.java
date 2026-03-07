import java.util.ArrayList;

public class Event {

    // Event information
    private String eventID;
    private String eventName;

    // Composition: event owns its sessions
    private ArrayList<Session> sessionList = new ArrayList<>();

    // Constructor to create event
    public Event(String id, String name) {
        this.eventID = id;
        this.eventName = name;
    }

    // Getter for event ID
    public String getEventID() {
        return eventID;
    }

    // Add a session into the event
    public void addSession(Session s) {
        sessionList.add(s);
    }

    // Find session using session ID
    public Session findSession(String id) {

        // Loop through session list
        for (Session s : sessionList) {

            // If ID matches, return the session
            if (s.getSessionID().equals(id))
                return s;
        }

        // If session not found
        return null;
    }

    // Display event and all sessions inside it
    public void displayEvent() {

        System.out.println("Event: " + eventName);

        // Show all sessions in this event
        for (Session s : sessionList) {
            s.displaySession();
        }
    }
}