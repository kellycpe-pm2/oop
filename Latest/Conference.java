import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Conference extends Event {
    private Session[] sessions;
    private int sessionCount;
    private static final int MAX_SESSIONS = 10;
    private final String type = "Conference";

    public Conference(String title, LocalDate date, String venue, int maxTickets) {
        super(title, date, venue, maxTickets);
        this.sessions = new Session[MAX_SESSIONS];
        this.sessionCount = 0;
        appendToFile(); // auto-save to Conference.json on creation
    }

    // Private constructor used only when loading from file — skips auto-save
    private Conference(String title, LocalDate date, String venue, int maxTickets, boolean fromFile) {
        super(title, date, venue, maxTickets);
        this.sessions = new Session[MAX_SESSIONS];
        this.sessionCount = 0;
    }

    // ── File helpers ─────────────────────────────────────────────────────────

    private void appendToFile() {
        try {
            File confFile = new File("Conference.json");
            confFile.createNewFile();
            try (Writer writer = new java.io.FileWriter(confFile, true)) {
                writeConferenceRecord(writer);
            }
        } catch (IOException e) {
            System.out.println("Error auto-saving conference data: " + e.getMessage());
        }
    }

    private void updateInFile() {
        try {
            List<Conference> conferences = readConferenceData();
            for (int i = 0; i < conferences.size(); i++) {
                if (conferences.get(i).getEventID().equals(this.getEventID())) {
                    conferences.set(i, this);
                    break;
                }
            }
            storeConferenceData(conferences);
        } catch (Exception e) {
            System.out.println("Error updating conference data: " + e.getMessage());
        }
    }

    /**
     * Writes a single conference record.
     *
     * Format per record:
     * eventID, title, date, venue, maxTickets, sessionCount
     * then per session: sessionID, topic, time, speakerCount, [username x
     * speakerCount]
     *
     * Session now stores usernames directly (String[]), so no Speaker object is
     * needed.
     */
    private void writeConferenceRecord(Writer writer) throws IOException {
        writer.write(getEventID() + "\n");
        writer.write(getTitle() + "\n");
        writer.write(getDate().toString() + "\n");
        writer.write(getVenue() + "\n");
        writer.write(getMaxTickets() + "\n");
        writer.write(sessionCount + "\n");
        for (int s = 0; s < sessionCount; s++) {
            Session session = sessions[s];
            writer.write(session.getSessionID() + "\n");
            writer.write(session.getTopic() + "\n");
            writer.write(session.getTime() + "\n");
            writer.write(session.getSpeakerCount() + "\n");
            // getSpeakers() returns String[] — no Speaker object required
            String[] usernames = session.getSpeakers();
            for (int sp = 0; sp < session.getSpeakerCount(); sp++) {
                writer.write(usernames[sp] + "\n");
            }
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public int getSessionCount() {
        return sessionCount;
    }

    public Session[] getSessions() {
        return sessions;
    }

    // ── Session management ───────────────────────────────────────────────────

    /** Used only when loading from file — does NOT trigger a file update. */
    private Session loadSession(String topic, String time) {
        if (sessionCount < MAX_SESSIONS) {
            Session s = new Session(topic, time);
            sessions[sessionCount++] = s;
            return s;
        }
        return null;
    }

    /** Create a new session and persist to Conference.json. */
    public Session createSession(String topic, String time) {
        if (sessionCount < MAX_SESSIONS) {
            Session s = new Session(topic, time);
            sessions[sessionCount++] = s;
            updateInFile();
            return s;
        }
        System.out.println("Cannot add more sessions. Limit reached.");
        return null;
    }

    /** Convenience method: create multiple sessions at once. */
    public int autoCreateSessions(String[] topics, String[] times) {
        int count = Math.min(topics.length, times.length);
        int created = 0;
        for (int i = 0; i < count; i++) {
            if (createSession(topics[i], times[i]) != null)
                created++;
        }
        System.out.println(created + " session(s) created for conference: " + getTitle());
        return created;
    }

    /** Remove a session by sessionID. */
    public boolean removeSession(String sessionID) {
        for (int i = 0; i < sessionCount; i++) {
            if (sessions[i].getSessionID().equals(sessionID)) {
                for (int j = i; j < sessionCount - 1; j++) {
                    sessions[j] = sessions[j + 1];
                }
                sessions[sessionCount - 1] = null;
                sessionCount--;
                System.out.println("Session [" + sessionID
                        + "] removed from conference [" + getEventID() + "] successfully.");
                return true;
            }
        }
        System.out.println("Error: Session [" + sessionID
                + "] not found in conference [" + getEventID() + "] !");
        return false;
    }

    // ── Display helpers ──────────────────────────────────────────────────────

    public void displaySessions() {
        System.out.println("  Sessions for Conference: " + getTitle());
        if (sessionCount == 0) {
            System.out.println("    No sessions created.");
        } else {
            for (int i = 0; i < sessionCount; i++) {
                System.out.println("    " + (i + 1) + ": " + sessions[i].toString());
            }
        }
    }

    public void createConferenceFile() {
        try {
            File confFile = new File("Conference.json");
            if (confFile.createNewFile()) {
                System.out.println("Please Waiting...");
                System.out.println("Conference file created: " + confFile.getName());
            }
        } catch (IOException e) {
            System.out.println("Error creating conference file: " + e.getMessage());
        }
    }

    // ── Static file I/O ──────────────────────────────────────────────────────

    /**
     * Reads all conferences from "Conference.json".
     * Session speaker slots are restored as plain usernames (String) — no Speaker
     * object is constructed.
     */
    public static List<Conference> readConferenceData() {
        List<Conference> conferences = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("Conference.json"));
            if (!lines.isEmpty()) {
                int i = 0;
                while (i < lines.size()) {
                    String eventID = lines.get(i++);
                    String title = lines.get(i++);
                    LocalDate date = LocalDate.parse(lines.get(i++));
                    String venue = lines.get(i++);
                    int maxTickets = Integer.parseInt(lines.get(i++));
                    int storedSessionCount = Integer.parseInt(lines.get(i++));

                    Conference conf = new Conference(title, date, venue, maxTickets, true);
                    conf.setEventID(eventID);

                    for (int s = 0; s < storedSessionCount; s++) {
                        String sessionID = lines.get(i++);
                        String topic = lines.get(i++);
                        String time = lines.get(i++);
                        int speakerCount = Integer.parseInt(lines.get(i++));

                        Session session = conf.loadSession(topic, time);
                        session.setSessionID(sessionID);

                        // Restore speaker usernames directly — no Speaker object needed
                        for (int sp = 0; sp < speakerCount; sp++) {
                            String username = lines.get(i++);
                            session.addSpeaker(username);
                        }
                    }
                    conferences.add(conf);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading conference data: " + e.getMessage());
        }
        return conferences;
    }

    public static void displayAllConferences(List<Conference> conferences) {
        System.out.println("=== Conference Info ===");
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n",
                "ID", "Title", "Date", "Venue", "MaxTix");
        System.out.println("--------------------------------------------------------------------");
        for (Conference conf : conferences) {
            System.out.println(conf.toString());
            conf.displaySessions();
        }
    }

    public static void storeConferenceData(List<Conference> conferences) {
        try (Writer writer = new java.io.FileWriter("Conference.json")) {
            for (Conference conf : conferences) {
                conf.writeConferenceRecord(writer);
            }
        } catch (IOException e) {
            System.out.println("Error storing conference data: " + e.getMessage());
        }
    }

    public static boolean removeConference(List<Conference> conferences, String eventID) {
        for (int i = 0; i < conferences.size(); i++) {
            if (conferences.get(i).getEventID().equals(eventID)) {
                conferences.remove(i);
                storeConferenceData(conferences);
                System.out.println("Conference [" + eventID
                        + "] and all its sessions removed successfully.");
                return true;
            }
        }
        System.out.println("Error: Conference [" + eventID + "] not found !");
        return false;
    }

    // ── Overrides ────────────────────────────────────────────────────────────

    @Override
    public void displayInfo() {
        System.out.println("=== Conference Info ===");
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n",
                "ID", "Title", "Date", "Venue", "MaxTix");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(this.toString());
        displaySessions();
    }

    @Override
    public String toString() {
        return super.toString() + String.format("%-14s│\n", type);
    }

    public boolean isConference() {
        return true;
    }

    public boolean isConcert() {
        return false;
    }

    public boolean isWorkshop() {
        return false;
    }

    public boolean equals(Object o) {
        if (super.equals(o)){
            return true;
        }else{
            return false;
        }
    }
}
