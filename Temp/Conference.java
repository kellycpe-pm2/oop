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
        this.sessions = new Session[MAX_SESSIONS];
        this.sessionCount = 0;
    }

    // Getters
    public int getSessionCount() {
        return sessionCount;
    }

    public Session[] getSessions() {
        return sessions;
    }

    // Composition — Conference creates and owns its Sessions
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

    // auto create multiple sessions at once
    public int autoCreateSessions(String[] topics, String[] times) {
        int count = Math.min(topics.length, times.length);
        int created = 0;
        for (int i = 0; i < count; i++) {
            if (createSession(topics[i], times[i]) != null) {
                created++;
            }
        }
        System.out.println(created + " session(s) created for conference: " + getTitle());
        return created;
    }

    // create Conference file
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

    // Reads all conferences from "Conference.json" and returns them as a list of
    // Conference objects
    // Lines per record:
    // eventID, title, date, venue, maxTickets, sessionCount
    // then for each session: sessionID, topic, time, speakerCount, [speakerUsername
    // x speakerCount]
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

                    Conference conf = new Conference(title, date, venue, maxTickets);
                    conf.setEventID(eventID); // restore saved ID

                    for (int s = 0; s < storedSessionCount; s++) {
                        String sessionID = lines.get(i++);
                        String topic = lines.get(i++);
                        String time = lines.get(i++);
                        int speakerCount = Integer.parseInt(lines.get(i++));

                        Session session = conf.createSession(topic, time);
                        session.setSessionID(sessionID); // restore saved session ID

                        for (int sp = 0; sp < speakerCount; sp++) {
                            String speakerUsername = lines.get(i++);
                            // restore speaker as a shell object with username only
                            Speaker speaker = new Speaker(speakerUsername, "", "", "");
                            session.addSpeaker(speaker);
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

    // display all conferences
    public static void displayAllConferences(List<Conference> conferences) {
        System.out.println("=== Conference Info ===");
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n", "ID", "Title", "Date", "Venue", "MaxTix");
        System.out.println("--------------------------------------------------------------------");
        for (Conference conf : conferences) {
            System.out.println(conf.toString());
            conf.displaySessions();
        }
    }

    // store conference data to Conference.json
    public static void storeConferenceData(List<Conference> conferences) {
        try (Writer writer = new java.io.FileWriter("Conference.json")) { // overwrite file
            for (Conference conf : conferences) {
                writer.write(conf.getEventID() + "\n");
                writer.write(conf.getTitle() + "\n");
                writer.write(conf.getDate().toString() + "\n");
                writer.write(conf.getVenue() + "\n");
                writer.write(conf.getMaxTickets() + "\n");
                writer.write(conf.getSessionCount() + "\n");

                for (int s = 0; s < conf.getSessionCount(); s++) {
                    Session session = conf.getSessions()[s];
                    writer.write(session.getSessionID() + "\n");
                    writer.write(session.getTopic() + "\n");
                    writer.write(session.getTime() + "\n");
                    writer.write(session.getSpeakerCount() + "\n");

                    for (int sp = 0; sp < session.getSpeakerCount(); sp++) {
                        writer.write(session.getSpeakers()[sp].getAccessUsername() + "\n");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error storing conference data: " + e.getMessage());
        }
    }

    // Display all sessions and their speakers
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