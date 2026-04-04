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

    // ======================== EQUALS (search by eventID, Concert-only) ========================

    // Returns true only when the other object is also a Concert with the same eventID.
    @Override
    public boolean equals(Object o) {
        if (o instanceof Concert) {
            Concert c = (Concert) o;
            return this.getEventID().equals(c.getEventID());
        }
        return false; // the object does not belong to Concert
    }

    // ======================== FILE OPERATIONS ========================

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

    // 6 lines per record: eventID, title, date, venue, maxTickets, ticketType
    public static List<Concert> readConcertData() {
        List<Concert> concerts = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("Concert.json"));
            if (!lines.isEmpty()) {
                for (int i = 0; i < lines.size(); i += 6) {
                    String eventID     = lines.get(i);
                    String title       = lines.get(i + 1);
                    LocalDate date     = LocalDate.parse(lines.get(i + 2));
                    String venue       = lines.get(i + 3);
                    int maxTickets     = Integer.parseInt(lines.get(i + 4));
                    String stringTicketType = lines.get(i + 5);

                    Concert c = new Concert(title, date, venue, maxTickets);
                    c.setEventID(eventID);

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
                            c.setTicketType(tt);
                        }
                    }
                    concerts.add(c);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading concert data: " + e.getMessage());
        }
        return concerts;
    }

    public static void displayAllConcerts(List<Concert> concerts) {
        System.out.println("=== Concert Info ===");
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n", "ID", "Title", "Date", "Venue", "MaxTix");
        System.out.println("------------------------------------------------------------------");
        for (Concert c : concerts) {
            System.out.println(c.toString());
        }
    }

    public static void storeConcertData(List<Concert> concerts) {
        try (Writer writer = new java.io.FileWriter("Concert.json")) {
            for (Concert c : concerts) {
                writer.write(c.getEventID()              + "\n");
                writer.write(c.getTitle()                + "\n");
                writer.write(c.getDate().toString()      + "\n");
                writer.write(c.getVenue()                + "\n");
                writer.write(c.getMaxTickets()           + "\n");
                // write empty line when no ticket type has been set yet
                writer.write((c.getTicketType() != null ? c.getTicketType().toString() : "") + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error storing concert data: " + e.getMessage());
        }
    }

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
}
