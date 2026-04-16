import java.time.LocalDate;
import java.util.List;

public class Conference extends Event {
    private Session[] sessions;
    private int sessionCount;
    private static final int MAX_SESSIONS = 10;
    private final String TYPE = "Conference";

    public Conference() {
        this(" ", LocalDate.now(), " ", 0);
    }

    public Conference(String title, LocalDate date, String venue, int maxTickets) {
        super(title, date, venue, maxTickets);
        this.sessions = new Session[MAX_SESSIONS];
        this.sessionCount = 0;
    }
    // ── Getters ──────────────────────────────────────────────────────────────

    public int getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }

    public Session[] getSessions() {
        return sessions;
    }

    public void setSession(Session session) {
        this.sessions[sessionCount++] = session;
    }

    // ── Session management ───────────────────────────────────────────────────

    /**
     * Returns true if {@code newTime} (format "HHMM", e.g. "0900") is within
     * 60 minutes of any already-scheduled session in this conference.
     */
    private boolean isTimeConflict(String newTime) {
        int newMinutes;
        try {
            String t = newTime.trim();
            if (t.length() != 4)
                return false;
            newMinutes = Integer.parseInt(t.substring(0, 2)) * 60
                    + Integer.parseInt(t.substring(2, 4));
        } catch (Exception e) {
            return false;
        }
        for (int i = 0; i < sessionCount; i++) {
            int existing = sessions[i].getTimeInMinutes();
            if (existing == -1)
                continue;
            if (Math.abs(newMinutes - existing) < 60) {
                return true; // gap < 1 hour → conflict
            }
        }
        return false;
    }

    /** Create a new session and persist to Conference.json. */
    public Session createSession(String topic, String time) {
        if (sessionCount >= MAX_SESSIONS) {
            System.out.println("Cannot add more sessions. Limit reached.");
            return null;
        }
        if (isTimeConflict(time)) {
            System.out.println("Error: Session time [" + time
                    + "] conflicts with an existing session."
                    + " Sessions must be at least 1 hour apart.");
            return null;
        }
        Session s = new Session(topic, time);
        sessions[sessionCount++] = s;
        return s;
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
                + "] Not found in conference [" + getEventID() + "] !");
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

    public static boolean removeConference(List<Conference> conferences, String eventID) {
        for (int i = 0; i < conferences.size(); i++) {
            if (conferences.get(i).getEventID().equals(eventID)) {
                conferences.remove(i);
                System.out.println("Conference [" + eventID
                        + "] and all its sessions removed successfully.");
                return true;
            }
        }
        System.out.println("Error: Conference [" + eventID + "] Not found !");
        return false;
    }

    // ── Overrides ────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return super.toString() + String.format("%-14s│\n", TYPE);
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
        if (super.equals(o)) {
            return true;
        } else {
            return false;
        }
    }
}
