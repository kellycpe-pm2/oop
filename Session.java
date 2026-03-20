public class Session {
    private String sessionID;
    private String topic;
    private String time;

    // auto-generate sessionID: S001, S002, ...
    private static int sessionCounter = 1;

    private static String generateSessionID() {
        return String.format("S%03d", sessionCounter++);
    }

    public Session(String topic, String time) {
        this.sessionID = generateSessionID();
        this.topic = topic;
        this.time = time;
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

    // Setters
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTime(String time) {
        this.time = time;
    }

}