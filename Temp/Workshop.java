import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Workshop extends Event {

    public Workshop(String title, LocalDate date, String venue, int maxTickets) {
        super(title, date, venue, maxTickets);
    }

    // create Workshop file
    public void createWorkshopFile() {
        try {
            File workshopFile = new File("Workshop.json");
            if (workshopFile.createNewFile()) {
                System.out.println("Please Waiting...");
                System.out.println("Workshop file created: " + workshopFile.getName());
            }
        } catch (IOException e) {
            System.out.println("Error creating workshop file: " + e.getMessage());
        }
    }

    // Reads all workshops from "Workshop.json" and returns them as a list of
    // Workshop objects
    // 5 lines per record: eventID, title, date, venue, maxTickets
    public static List<Workshop> readWorkshopData() {
        List<Workshop> workshops = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("Workshop.json"));
            if (!lines.isEmpty()) {
                for (int i = 0; i < lines.size(); i += 5) { // 5 lines per workshop
                    String eventID = lines.get(i);
                    String title = lines.get(i + 1);
                    LocalDate date = LocalDate.parse(lines.get(i + 2));
                    String venue = lines.get(i + 3);
                    int maxTickets = Integer.parseInt(lines.get(i + 4));

                    Workshop w = new Workshop(title, date, venue, maxTickets);
                    w.setEventID(eventID); // restore saved ID
                    workshops.add(w);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading workshop data: " + e.getMessage());
        }
        return workshops;
    }

    // display all workshops
    public static void displayAllWorkshops(List<Workshop> workshops) {
        System.out.println("=== Workshop Info ===");
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n", "ID", "Title", "Date", "Venue", "MaxTix");
        System.out.println("--------------------------------------------------------------------");
        for (Workshop w : workshops) {
            System.out.println(w.toString());
        }
    }

    // store workshop data to Workshop.json
    public static void storeWorkshopData(List<Workshop> workshops) {
        try (Writer writer = new java.io.FileWriter("Workshop.json")) { // overwrite file
            for (Workshop w : workshops) {
                writer.write(w.getEventID() + "\n");
                writer.write(w.getTitle() + "\n");
                writer.write(w.getDate().toString() + "\n");
                writer.write(w.getVenue() + "\n");
                writer.write(w.getMaxTickets() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error storing workshop data: " + e.getMessage());
        }
    }

    @Override
    public void displayInfo() {
        System.out.println("=== Workshop Info ===");
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n",
                "ID", "Title", "Date", "Venue", "MaxTix");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}