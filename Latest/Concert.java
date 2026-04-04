import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Concert extends Event {

    public Concert(String title, LocalDate date, String venue, int maxTickets) {
        super(title, date, venue, maxTickets);
        appendToFile(); // auto-save to Concert.json on creation
    }

    // Private constructor used only when loading from file — skips auto-save
    private Concert(String title, LocalDate date, String venue, int maxTickets, boolean fromFile) {
        super(title, date, venue, maxTickets);
    }

    // Appends this concert's data to Concert.json
    private void appendToFile() {
        try {
            File concertFile = new File("Concert.json");
            concertFile.createNewFile(); // creates file if it doesn't exist
            try (Writer writer = new java.io.FileWriter(concertFile, true)) { // true = append mode
                writer.write(getEventID() + "\n");
                writer.write(getTitle() + "\n");
                writer.write(getDate().toString() + "\n");
                writer.write(getVenue() + "\n");
                writer.write(getMaxTickets() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error auto-saving concert data: " + e.getMessage());
        }
    }

    // create Concert file
    public void createConcertFile() {
        try {
            File concertFile = new File("Concert.json");
            if (concertFile.createNewFile()) {
                System.out.println("Please Waiting...");
                System.out.println("Concert file created: " + concertFile.getName());
            }
        } catch (IOException e) {
            System.out.println("Error creating concert file: " + e.getMessage());
        }
    }

    // Reads all concerts from "Concert.json" and returns them as a list of Concert
    // objects
    // 5 lines per record: eventID, title, date, venue, maxTickets
    public static List<Concert> readConcertData() {
        List<Concert> concerts = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("Concert.json"));
            if (!lines.isEmpty()) {
                for (int i = 0; i < lines.size(); i += 5) { // 5 lines per concert
                    String eventID = lines.get(i);
                    String title = lines.get(i + 1);
                    LocalDate date = LocalDate.parse(lines.get(i + 2));
                    String venue = lines.get(i + 3);
                    int maxTickets = Integer.parseInt(lines.get(i + 4));

                    Concert c = new Concert(title, date, venue, maxTickets, true); // fromFile=true skips auto-save
                    c.setEventID(eventID); // restore saved ID
                    concerts.add(c);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading concert data: " + e.getMessage());
        }
        return concerts;
    }

    // display all concerts
    public static void displayAllConcerts(List<Concert> concerts) {
        System.out.println("=== Concert Info ===");
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n", "ID", "Title", "Date", "Venue", "MaxTix");
        System.out.println("------------------------------------------------------------------");
        for (Concert c : concerts) {
            System.out.println(c.toString());
        }
    }

    // store concert data to Concert.json
    public static void storeConcertData(List<Concert> concerts) {
        try (Writer writer = new java.io.FileWriter("Concert.json")) { // overwrite file
            for (Concert c : concerts) {
                writer.write(c.getEventID() + "\n");
                writer.write(c.getTitle() + "\n");
                writer.write(c.getDate().toString() + "\n");
                writer.write(c.getVenue() + "\n");
                writer.write(c.getMaxTickets() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error storing concert data: " + e.getMessage());
        }
    }

    // remove a concert by eventID from the list and update Concert.json
    public static boolean removeConcert(List<Concert> concerts, String eventID) {
        for (int i = 0; i < concerts.size(); i++) {
            if (concerts.get(i).getEventID().equals(eventID)) {
                concerts.remove(i);
                storeConcertData(concerts);
                System.out.println("Concert [" + eventID + "] removed successfully.");
                return true;
            }
        }
        System.out.println("Error: Concert [" + eventID + "] not found !");
        return false;
    }

    @Override
    public void displayInfo() {
        System.out.println("=== Concert Info ===");
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n",
                "ID", "Title", "Date", "Venue", "MaxTickets");
        System.out.println("------------------------------------------------------------------");
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof Concert) {
            Concert c = (Concert) o;
            return this.getEventID().equals(c.getEventID());
        }
        return false; // the object does not belong to Concert
    }
}
