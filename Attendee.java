import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Attendee extends User{
    private static int no = 0;
    private List<Ticket> ticketHistory = new ArrayList<>();

    // constructor
    public Attendee(){
        super();
    }

    public Attendee(String username, String password, String email, String contactNo) {
        //super(username, password, email, contactNo);
    }

    public int getno() {
        return no;
    }

    public String toString(int no) {
        return String.format("%-15s",getAccessUsername());
    }

    // display info
    public void displayInfo() {
        System.out.println("=== Attendee Info ===");
        System.out.printf("%-15s %-25s %-15s%n", "Username", "Email", "Contact No");
        System.out.println("----------------------------------------------------");
        for (int i = 0; i <= no; i++) {
            System.out.println(toString(i));
        }
    }


    // create attendee file if not exist
    
    public void createAttendeeFile() {
        try {
        File attFile = new File("Attendee.json");
        if (attFile.createNewFile()) {
            System.out.println("Please Waiting...");
            System.out.println("Attendee file created: " + attFile.getName());
        }
        } catch (IOException e) {
        System.out.println("Error creating attendee file: " + e.getMessage());
        }
    }
    // load the data from attendee.json
    
    public void readAttendeeData() {
        try {
        List<String> lines = Files.readAllLines(Paths.get("Attendee.json"));
        if (lines.isEmpty()) {
            no = 0;
        } else {
    // each record has 4 fields: username, password, email, contactNo
            no = (lines.size() / 4) - 1;
            String[][] information = new String[no + 1][4];

            for (int i = 0; i < lines.size(); i++) {
                information[i / 4][i % 4] = lines.get(i);
            }

            for (int i = 0; i <= no; i++) {
                set(information[i][0]);
                setPassword(information[i][1]);
                setEmail(information[i][2]);
                this.contactNo[i] = information[i][3];
            }
        }
        } catch (IOException e) {
            createAttendeeFile();
        }
        }
    
    // store attendee data to Attendee.json
    public void storeAttendeeData() {
        try (Writer writer = new java.io.FileWriter("Attendee.json", true)) {
            if (getAccessUsername() != null && getEmail(no) != null && this.contactNo[no]!= null) {
                writer.write(getUsername(no) + "\n");
                writer.write(getPassword(no) + "\n");
                writer.write(getEmail(no) + "\n");
                writer.write(this.contactNo[no] + "\n");
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

