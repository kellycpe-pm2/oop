import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Workshop extends Event {

    // Speaker part
    private static final int MAX_SPEAKERS = 1;
    private Speaker[] speakers = new Speaker[MAX_SPEAKERS];
    private int speakerCount = 0;
    private final String type="Workshop";
    public Workshop(String title, LocalDate date, String venue, int maxTickets) {
        super(title, date, venue, maxTickets);
        appendToFile(); // auto-save to Workshop.json on creation
    }

    // Private constructor used only when loading from file — skips auto-save
    private Workshop(String title, LocalDate date, String venue, int maxTickets, boolean fromFile) {
        super(title, date, venue, maxTickets);
    }

    // ── Speaker management ──────────────────────────────────────────────────

    public Speaker[] getSpeakers() {
        return speakers;
    }

    public int getSpeakerCount() {
        return speakerCount;
    }

    // Assign a speaker to this workshop. Returns false if already assigned or full.
    public boolean assignSpeaker(Speaker speaker) {
        if (speakerCount >= MAX_SPEAKERS) {
            System.out.println("Error: Workshop already has the maximum number of speakers.");
            return false;
        }
        for (int i = 0; i < speakerCount; i++) {
            if (speakers[i].getAccessUsername().equals(speaker.getAccessUsername())) {
                System.out.println("Error: Speaker [" + speaker.getAccessUsername()
                        + "] is already assigned to this workshop.");
                return false;
            }
        }
        speakers[speakerCount++] = speaker;
        System.out.println("Speaker [" + speaker.getAccessUsername()
                + "] assigned to workshop [" + getEventID() + "] successfully.");
        return true;
    }

    // Remove a speaker from this workshop by username.
    public boolean removeSpeaker(String username) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakers[i].getAccessUsername().equals(username)) {
                for (int j = i; j < speakerCount - 1; j++) {
                    speakers[j] = speakers[j + 1];
                }
                speakers[speakerCount - 1] = null;
                speakerCount--;
                System.out.println("Speaker [" + username
                        + "] removed from workshop [" + getEventID() + "] successfully.");
                return true;
            }
        }
        System.out.println("Error: Speaker [" + username
                + "] not found in workshop [" + getEventID() + "].");
        return false;
    }

    // Replace an existing speaker with a new one (change speaker).
    public boolean changeSpeaker(String oldUsername, Speaker newSpeaker) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakers[i].getAccessUsername().equals(oldUsername)) {
                for (int j = 0; j < speakerCount; j++) {
                    if (j != i && speakers[j].getAccessUsername()
                            .equals(newSpeaker.getAccessUsername())) {
                        System.out.println("Error: Speaker [" + newSpeaker.getAccessUsername()
                                + "] is already assigned to this workshop.");
                        return false;
                    }
                }
                speakers[i] = newSpeaker;
                System.out.println("Speaker [" + oldUsername + "] replaced with ["
                        + newSpeaker.getAccessUsername()
                        + "] in workshop [" + getEventID() + "] successfully.");
                return true;
            }
        }
        System.out.println("Error: Speaker [" + oldUsername
                + "] not found in workshop [" + getEventID() + "].");
        return false;
    }

    /** Display speakers assigned to this workshop. */
    public void displaySpeakers() {
        System.out.println("  Speakers for Workshop: " + getTitle());
        if (speakerCount == 0) {
            System.out.println("    No speakers assigned.");
        } else {
            for (int i = 0; i < speakerCount; i++) {
                System.out.println("    " + (i + 1) + ": " + speakers[i].getAccessUsername());
            }
        }
    }

    // ── File I/O ─────────────────────────────────────────────────────────────

    // Appends this workshop's data to Workshop.json
    private void appendToFile() {
        try {
            File workshopFile = new File("Workshop.json");
            workshopFile.createNewFile();
            try (Writer writer = new java.io.FileWriter(workshopFile, true)) {
                writeWorkshopRecord(writer);
            }
        } catch (IOException e) {
            System.out.println("Error auto-saving workshop data: " + e.getMessage());
        }
    }

    // Helper: writes one workshop record
    // Format: eventID / title / date / venue / maxTickets / speakerCount /
    // [username x N]
    private void writeWorkshopRecord(Writer writer) throws IOException {
        writer.write(getEventID() + "\n");
        writer.write(getTitle() + "\n");
        writer.write(getDate().toString() + "\n");
        writer.write(getVenue() + "\n");
        writer.write(getMaxTickets() + "\n");
        writer.write(speakerCount + "\n");
        for (int i = 0; i < speakerCount; i++) {
            writer.write(speakers[i].getAccessUsername() + "\n");
        }
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

    /**
     * Reads all workshops from "Workshop.json".
     * Format per record:
     * eventID, title, date, venue, maxTickets, speakerCount, [username x
     * speakerCount]
     */
    public static List<Workshop> readWorkshopData() {
        List<Workshop> workshops = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("Workshop.json"));
            if (!lines.isEmpty()) {
                int i = 0;
                while (i < lines.size()) {
                    String eventID = lines.get(i++);
                    String title = lines.get(i++);
                    LocalDate date = LocalDate.parse(lines.get(i++));
                    String venue = lines.get(i++);
                    int maxTickets = Integer.parseInt(lines.get(i++));
                    int storedSpeakerCount = Integer.parseInt(lines.get(i++));

                    Workshop w = new Workshop(title, date, venue, maxTickets, true);
                    w.setEventID(eventID);

                    for (int s = 0; s < storedSpeakerCount; s++) {
                        String username = lines.get(i++);
                        Speaker sp = new Speaker(username, "", "", "");
                        w.speakers[w.speakerCount++] = sp;
                    }
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
            w.displaySpeakers();
        }
    }

    // store workshop data to Workshop.json
    public static void storeWorkshopData(List<Workshop> workshops) {
        try (Writer writer = new java.io.FileWriter("Workshop.json")) {
            for (Workshop w : workshops) {
                w.writeWorkshopRecord(writer);
            }
        } catch (IOException e) {
            System.out.println("Error storing workshop data: " + e.getMessage());
        }
    }

    // remove a workshop by eventID from the list and update Workshop.json
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
        displaySpeakers();
    }

    public boolean isWorkshop() {
            return true;
    }

    public boolean isConcert(){
        return false;
    }

    public boolean isConference(){
        return false;
    }

    @Override
   public String toString() {
        return super.toString()+ String.format("%-14s│\n",type);
    }

    
    public boolean equals(Object o) {
       if (o==null){
            return false;
        }
        if (o instanceof Workshop) {
            Workshop w = (Workshop) o;
            return this.getEventID().equals(w.getEventID());
        }
        return false;
    }
}
