public class Session {
    private String sessionID;
    private String topic;
    private String time;
    private Speaker[] speakers; // aggregation — Speakers exist outside Session
    private int speakerCount;
    private static final int MAX_SPEAKERS = 5;

    // auto-generate sessionID: S001, S002, ...
    private static int sessionCounter = 1;

    private static String generateSessionID() {
        return String.format("S%03d", sessionCounter++);
    }

    public Session(String topic, String time) {
        this.sessionID = generateSessionID();
        this.topic = topic;
        this.time = time;
        this.speakers = new Speaker[MAX_SPEAKERS];
        this.speakerCount = 0;
    }

    // Getters
    public String getSessionID() {
        return sessionID;
    }

    public String getTopic() {
        return topic;
    }

    public String getTime() {
        return time;
    }

    public int getSpeakerCount() {
        return speakerCount;
    }

    public Speaker[] getSpeakers() {
        return speakers;
    }

    // Setters
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // Assign a Speaker to this session (aggregation)
    public boolean assignSpeaker(Speaker sp) {
        if (speakerCount < MAX_SPEAKERS) {
            speakers[speakerCount] = sp;
            speakerCount++;
            return true;
        }
        System.out.println("Session is full. Cannot add more speakers.");
        return false;
    }

    // Display all assigned speakers
    public void displaySpeakers() {
        System.out.println("      Speakers for Session [" + sessionID + "] - " + topic + ":");
        if (speakerCount == 0) {
            System.out.println("        No speakers assigned.");
        } else {
            for (int i = 0; i < speakerCount; i++) {
                System.out.println("        " + (i + 1) + ": " + speakers[i].toString(0));
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%-6s %-25s %-10s [%d speaker(s)]",
                sessionID, topic, time, speakerCount);
    }
}