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

                    Concert c = new Concert(title, date, venue, maxTickets);
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
}