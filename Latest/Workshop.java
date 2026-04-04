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

    // ======================== EQUALS (search by eventID, Workshop-only) ========================

    // Returns true only when the other object is also a Workshop with the same eventID.
    @Override
    public boolean equals(Object o) {
        if (o instanceof Workshop) {
            Workshop w = (Workshop) o;
            return this.getEventID().equals(w.getEventID());
        }
        return false; // the object does not belong to Workshop
    }

    // ======================== FILE OPERATIONS ========================

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

    // 6 lines per record: eventID, title, date, venue, maxTickets, ticketType
    public static List<Workshop> readWorkshopData() {
        List<Workshop> workshops = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("Workshop.json"));
            if (!lines.isEmpty()) {
                for (int i = 0; i < lines.size(); i += 6) {
                    String eventID          = lines.get(i);
                    String title            = lines.get(i + 1);
                    LocalDate date          = LocalDate.parse(lines.get(i + 2));
                    String venue            = lines.get(i + 3);
                    int maxTickets          = Integer.parseInt(lines.get(i + 4));
                    String stringTicketType = lines.get(i + 5);

                    Workshop w = new Workshop(title, date, venue, maxTickets);
                    w.setEventID(eventID);

                    if (stringTicketType != null && !stringTicketType.isEmpty()) {
                        String[] parts = stringTicketType.split(" ");
                        if (parts.length >= 11) {
                            TicketType tt = new TicketType(
                                parts[0],
                                Integer.parseInt(parts[1]),
                                Integer.parseInt(parts[2]),
                                Integer.parseInt(parts[3]),
                                Integer.parseInt(parts[4]),
                                Double.parseDouble(parts[5]),
                                Double.parseDouble(parts[6]),
                                Double.parseDouble(parts[7]),
                                parts[8],
                                LocalDate.parse(parts[9]),
                                LocalDate.parse(parts[10]));
                            w.setTicketType(tt);
                        }
                    }
                    workshops.add(w);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading workshop data: " + e.getMessage());
        }
        return workshops;
    }

    public static void displayAllWorkshops(List<Workshop> workshops) {
        System.out.println("=== Workshop Info ===");
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n", "ID", "Title", "Date", "Venue", "MaxTix");
        System.out.println("--------------------------------------------------------------------");
        for (Workshop w : workshops) {
            System.out.println(w.toString());
        }
    }

    public static void storeWorkshopData(List<Workshop> workshops) {
        try (Writer writer = new java.io.FileWriter("Workshop.json")) {
            for (Workshop w : workshops) {
                writer.write(w.getEventID()              + "\n");
                writer.write(w.getTitle()                + "\n");
                writer.write(w.getDate().toString()      + "\n");
                writer.write(w.getVenue()                + "\n");
                writer.write(w.getMaxTickets()           + "\n");
                // write empty line when no ticket type has been set yet
                writer.write((w.getTicketType() != null ? w.getTicketType().toString() : "") + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error storing workshop data: " + e.getMessage());
        }
    }

    public static boolean removeWorkshop(List<Workshop> workshops, String eventID) {
        for (int i = 0; i < workshops.size(); i++) {
            if (workshops.get(i).getEventID().equals(eventID)) {
                workshops.remove(i);
                storeWorkshopData(workshops);
                System.out.println("Workshop [" + eventID + "] removed successfully.");
                return true;
            }
        }
        System.out.println("Error: Workshop [" + eventID + "] not found !");
        return false;
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
