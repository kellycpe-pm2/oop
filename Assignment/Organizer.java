import java.util.ArrayList;

// Organizer class inherits from User
public class Organizer extends User {

    // Organizer owns multiple events
    private ArrayList<Event> eventList = new ArrayList<>();

    // Constructor
    public Organizer(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    // Create a new event
    public void createEvent(Event e) {
        eventList.add(e);
    }

    // Find event using event ID
    public Event findEvent(String id) {

        // Search through event list
        for (Event e : eventList) {

            // If event ID matches
            if (e.getEventID().equals(id))
                return e;
        }

        // If event not found
        return null;
    }

    // Display all events created by organizer
    public void displayEvents() {

        for (Event e : eventList) {
            e.displayEvent();
        }
    }

    // Implementation of abstract method
    @Override
    public void displayInfo() {
        System.out.println("Organizer: " + username);
    }
}