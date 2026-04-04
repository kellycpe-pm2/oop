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

    public Conference(String title, LocalDate date, String venue, int maxTickets) {
        super(title, date, venue, maxTickets);
        this.sessions     = new Session[MAX_SESSIONS];
        this.sessionCount = 0;
    }

    // ======================== EQUALS (search by eventID, Conference-only) ========================

    // Returns true only when the other object is also a Conference with the same eventID.
    @Override
    public boolean equals(Object o) {
        if (o instanceof Conference) {
            Conference c = (Conference) o;
            return this.getEventID().equals(c.getEventID());
        }
        return false; // the object does not belong to Conference
    }

    // ======================== GETTERS ========================

    public int getSessionCount() { return sessionCount; }
    public Session[] getSessions() { return sessions;  }

    // ======================== SESSION MANAGEMENT ========================

    // Conference creates and owns its Sessions (Composition)
    public Session createSession(String topic, String time) {
        if (sessionCount < MAX_SESSIONS) {
            Session s = new Session(topic, time);
            sessions[sessionCount] = s;
            sessionCount++;
            return s;
        }
        System.out.println("Cannot add more sessions. Limit reached.");
        return null;
    }

    public int autoCreateSessions(String[] topics, String[] times) {
        int count   = Math.min(topics.length, times.length);
        int created = 0;
        for (int i = 0; i < count; i++) {
            if (createSession(topics[i], times[i]) != null) {
                created++;
            }
        }
        System.out.println(created + " session(s) created for conference: " + getTitle());
        return created;
    }

    public boolean removeSession(String sessionID) {
        for (int i = 0; i < sessionCount; i++) {
            if (sessions[i].getSessionID().equals(sessionID)) {
                for (int j = i; j < sessionCount - 1; j++) {
                    sessions[j] = sessions[j + 1];
                }
                sessions[sessionCount - 1] = null;
                sessionCount--;
                System.out.println("Session [" + sessionID + "] removed from conference ["
                        + getEventID() + "] successfully.");
                return true;
            }
        }
        System.out.println("Error: Session [" + sessionID + "] not found in conference ["
                + getEventID() + "] !");
        return false;
    }

    // ======================== FILE OPERATIONS ========================

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

    // Lines per record:
    // eventID, title, date, venue, maxTickets, sessionCount, ticketType
    // then per session: sessionID, topic, time, speakerCount, [speakerUsername x speakerCount]
    public static List<Conference> readConferenceData() {
        List<Conference> conferences = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("Conference.json"));
            if (!lines.isEmpty()) {
                int i = 0;
                while (i < lines.size()) {
                    String eventID            = lines.get(i++);
                    String title              = lines.get(i++);
                    LocalDate date            = LocalDate.parse(lines.get(i++));
                    String venue              = lines.get(i++);
                    int maxTickets            = Integer.parseInt(lines.get(i++));
                    int storedSessionCount    = Integer.parseInt(lines.get(i++));
                    String stringTicketType   = lines.get(i++);

                    Conference conf = new Conference(title, date, venue, maxTickets);
                    conf.setEventID(eventID);

                    for (int s = 0; s < storedSessionCount; s++) {
                        String sessionID   = lines.get(i++);
                        String topic       = lines.get(i++);
                        String time        = lines.get(i++);
                        int speakerCount   = Integer.parseInt(lines.get(i++));

                        Session session = conf.createSession(topic, time);
                        session.setSessionID(sessionID);

                        for (int sp = 0; sp < speakerCount; sp++) {
                            String speakerUsername = lines.get(i++);
                            Speaker speaker = new Speaker(speakerUsername, "", "", "");
                            session.addSpeaker(speaker);
                        }
                    }

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
                            conf.setTicketType(tt);
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
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n", "ID", "Title", "Date", "Venue", "MaxTix");
        System.out.println("--------------------------------------------------------------------");
        for (Conference conf : conferences) {
            System.out.println(conf.toString());
            conf.displaySessions();
        }
    }

    public static void storeConferenceData(List<Conference> conferences) {
        try (Writer writer = new java.io.FileWriter("Conference.json")) {
            for (Conference conf : conferences) {
                writer.write(conf.getEventID()              + "\n");
                writer.write(conf.getTitle()                + "\n");
                writer.write(conf.getDate().toString()      + "\n");
                writer.write(conf.getVenue()                + "\n");
                writer.write(conf.getMaxTickets()           + "\n");
                writer.write(conf.getSessionCount()         + "\n");
                // write empty line when no ticket type has been set yet
                writer.write((conf.getTicketType() != null ? conf.getTicketType().toString() : "") + "\n");

                for (int s = 0; s < conf.getSessionCount(); s++) {
                    Session session = conf.getSessions()[s];
                    writer.write(session.getSessionID()  + "\n");
                    writer.write(session.getTopic()      + "\n");
                    writer.write(session.getTime()       + "\n");
                    writer.write(session.getSpeakerCount()+ "\n");
                    for (int sp = 0; sp < session.getSpeakerCount(); sp++) {
                        writer.write(session.getSpeakers()[sp].getAccessUsername() + "\n");
                    }
                }
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
                System.out.println("Conference [" + eventID + "] and all its sessions removed successfully.");
                return true;
            }
        }
        System.out.println("Error: Conference [" + eventID + "] not found !");
        return false;
    }

    // ======================== DISPLAY ========================

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
        return super.toString();
    }
}
