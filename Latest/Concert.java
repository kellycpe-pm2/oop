import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Concert extends Event {

    // Speaker names only 
    private static final int MAX_SPEAKERS = 1;
    private String[] speakerNames = new String[MAX_SPEAKERS];
    private int speakerCount = 0;
    private final String type = "Concert";

    public Concert(String title, LocalDate date, String venue, int maxTickets) {
        super(title, date, venue, maxTickets);
        appendToFile(); // auto-save to Concert.json on creation
    }

    // Private constructor used only when loading from file — skips auto-save
    private Concert(String title, LocalDate date, String venue, int maxTickets, boolean fromFile) {
        super(title, date, venue, maxTickets);
    }

    // ── Speaker management ──────────────────────────────────────────────────

    public String[] getSpeakers() {
        return speakerNames;
    }

    public int getSpeakerCount() {
        return speakerCount;
    }

    // Assign a speaker by name. Returns false if already assigned or full.
    public boolean assignSpeaker(String speakerName) {
        if (speakerCount >= MAX_SPEAKERS) {
            System.out.println("Error: Concert already has the maximum number of speakers.");
            return false;
        }
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i].equals(speakerName)) {
                System.out.println("Error: Speaker [" + speakerName
                        + "] is already assigned to this concert.");
                return false;
            }
        }
        speakerNames[speakerCount++] = speakerName;
        System.out.println("Speaker [" + speakerName
                + "] assigned to concert [" + getEventID() + "] successfully.");
        return true;
    }

    // Remove a speaker from this concert by name.
    public boolean removeSpeaker(String speakerName) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i].equals(speakerName)) {
                for (int j = i; j < speakerCount - 1; j++) {
                    speakerNames[j] = speakerNames[j + 1];
                }
                speakerNames[speakerCount - 1] = null;
                speakerCount--;
                System.out.println("Speaker [" + speakerName
                        + "] removed from concert [" + getEventID() + "] successfully.");
                return true;
            }
        }
        System.out.println("Error: Speaker [" + speakerName
                + "] not found in concert [" + getEventID() + "].");
        return false;
    }

    // Replace an existing speaker with a new one (change speaker).
    public boolean changeSpeaker(String oldSpeakerName, String newSpeakerName) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i].equals(oldSpeakerName)) {
                for (int j = 0; j < speakerCount; j++) {
                    if (j != i && speakerNames[j].equals(newSpeakerName)) {
                        System.out.println("Error: Speaker [" + newSpeakerName
                                + "] is already assigned to this concert.");
                        return false;
                    }
                }
                speakerNames[i] = newSpeakerName;
                System.out.println("Speaker [" + oldSpeakerName + "] replaced with ["
                        + newSpeakerName + "] in concert [" + getEventID() + "] successfully.");
                return true;
            }
        }
        System.out.println("Error: Speaker [" + oldSpeakerName
                + "] not found in concert [" + getEventID() + "].");
        return false;
    }

    // Display speakers assigned to this concert.
    public void displaySpeakers() {
        System.out.println("  Speakers for Concert: " + getTitle());
        if (speakerCount == 0) {
            System.out.println("    No speakers assigned.");
        } else {
            for (int i = 0; i < speakerCount; i++) {
                System.out.println("    " + (i + 1) + ": " + speakerNames[i]);
            }
        }
    }

    // ── File I/O ─────────────────────────────────────────────────────────────

    // Appends this concert's data to Concert.json
    private void appendToFile() {
        try {
            File concertFile = new File("Concert.json");
            concertFile.createNewFile();
            try (Writer writer = new java.io.FileWriter(concertFile, true)) {
                writeConcertRecord(writer);
            }
        } catch (IOException e) {
            System.out.println("Error auto-saving concert data: " + e.getMessage());
        }
    }

    // Helper: writes one concert record
    // Format: eventID / title / date / venue / maxTickets / speakerCount / [name x
    // N]
    private void writeConcertRecord(Writer writer) throws IOException {
        writer.write(getEventID() + "\n");
        writer.write(getTitle() + "\n");
        writer.write(getDate().toString() + "\n");
        writer.write(getVenue() + "\n");
        writer.write(getMaxTickets() + "\n");
        writer.write(speakerCount + "\n");
        for (int i = 0; i < speakerCount; i++) {
            writer.write(speakerNames[i] + "\n");
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

    /**
     * Reads all concerts from "Concert.json".
     * Format per record:
     * eventID, title, date, venue, maxTickets, speakerCount, [name x speakerCount]
     */
    public static List<Concert> readConcertData() {
        List<Concert> concerts = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("Concert.json"));
            if (!lines.isEmpty()) {
                int i = 0;
                while (i < lines.size()) {
                    String eventID = lines.get(i++);
                    String title = lines.get(i++);
                    LocalDate date = LocalDate.parse(lines.get(i++));
                    String venue = lines.get(i++);
                    int maxTickets = Integer.parseInt(lines.get(i++));
                    int storedSpeakerCount = Integer.parseInt(lines.get(i++));

                    Concert c = new Concert(title, date, venue, maxTickets, true);
                    c.setEventID(eventID);

                    for (int s = 0; s < storedSpeakerCount; s++) {
                        c.speakerNames[c.speakerCount++] = lines.get(i++);
                    }
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
            c.displaySpeakers();
        }
    }

    // store concert data to Concert.json
    public static void storeConcertData(List<Concert> concerts) {
        try (Writer writer = new java.io.FileWriter("Concert.json")) {
            for (Concert c : concerts) {
                c.writeConcertRecord(writer);
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
        displaySpeakers();
    }

    @Override
    public String toString() {
        return super.toString() + String.format("%-14s│\n", type);
    }

    public boolean isConcert() {
        return true;
    }

    public boolean isConference() {
        return false;
    }

    public boolean isWorkshop() {
        return false;
    }

    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        } else {
            return false;
        }
    }
}
