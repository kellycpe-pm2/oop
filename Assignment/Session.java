public class Session {

    // Session details
    private String sessionID;
    private String title;

    // Aggregation: session uses speaker but does not own it
    private Speaker speaker;

    // Constructor to create session
    public Session(String sessionID, String title) {
        this.sessionID = sessionID;
        this.title = title;
    }

    // Assign a speaker to the session
    public void assignSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    // Getter method to get session ID
    public String getSessionID() {
        return sessionID;
    }

    // Display session information
    public void displaySession() {

        // If no speaker assigned yet
        String speakerName = "None";

        // If speaker exists, show speaker name
        if (speaker != null)
            speakerName = speaker.getUsername();

        System.out.println("Session: " + title + " | Speaker: " + speakerName);
    }
}