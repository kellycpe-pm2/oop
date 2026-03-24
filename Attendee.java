import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Attendee extends User{
    List<Attendee> attendees = new ArrayList<>();
    private List<Ticket> ticketHistory = new ArrayList<>();

    // constructor
    public Attendee(){
        super();
    }

    public Attendee(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
    }

    //create Attendee file
    public void createAttendeeFile() {
        try { File attFile = new File("Attendee.json");
            if (attFile.createNewFile()) { 
                System.out.println("Please Waiting..."); 
                System.out.println("Attendee file created: " + attFile.getName()); 
            } 
        } catch (IOException e) { 
            System.out.println("Error creating attendee file: " + e.getMessage()); 
        } 
    }

    // Reads all attendees from "Attendee.json" and returns them as a list of Attendee objects
    public static List<Attendee> readAttendeeData() {
        List<Attendee> attendees = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("Attendee.json"));
            if (!lines.isEmpty()) {
                for (int i = 0; i < lines.size(); i += 4) { // 4 lines per user
                    String username = lines.get(i);
                    String password = lines.get(i + 1);
                    String email = lines.get(i + 2);
                    String contactNo = lines.get(i + 3);

                    Attendee a = new Attendee(username, password, email, contactNo);
                    attendees.add(a);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading attendee data: " + e.getMessage());
        }
        return attendees;
    }

    // display attendee
    public static void displayAllAttendees(List<Attendee> attendees) {
        System.out.println("=== Attendee Info ===");
        System.out.printf("%-15s %-25s %-15s%n", "Username", "Email", "Contact No");
        System.out.println("----------------------------------------------------");
        for (Attendee a : attendees) {
            System.out.printf("%-15s %-25s %-15s%n", a.getAccessUsername(), a.getAccessEmail(), a.getAccessContactNo());
        }
    }
    
    // store attendee data to attendee file
    public static void storeAttendeeData(List<Attendee> attendees) {
        try (Writer writer = new java.io.FileWriter("Attendee.json")) { // overwrite file
            for (Attendee a : attendees) {
                writer.write(a.getAccessUsername() + "\n");
                writer.write(a.getAccessPassword() + "\n");
                writer.write(a.getAccessEmail() + "\n");
                writer.write(a.getAccessContactNo() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error storing attendee data: " + e.getMessage());
        }
    }

    //add ticket to history
    public void addTicket(Ticket ticket) {
    if (ticket != null) {
        ticketHistory.add(ticket);
    }
    }
    // display ticket history
    public void displayHistory(){
        System.out.println("\n=== Ticket Purchase History ===");

    if (ticketHistory.isEmpty()) {
        System.out.println("No tickets purchased.");
        return;
    }

    for (Ticket t : ticketHistory) {
        t.displayTicketDetails();
        System.out.println("----------------------------");
    }
    }
}

