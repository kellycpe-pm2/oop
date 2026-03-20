public class Conference extends Event {
    private Session[] sessions; // composition — Sessions are created & owned by Conference
    private int sessionCount;
    private static final int MAX_SESSIONS = 10;

    public Conference(String title, String date, String venue, int maxTickets) {
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