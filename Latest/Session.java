public class Session {
    private String sessionID;
    private String topic;
    private String time;

    // speaker association
    private static final int MAX_SPEAKERS = 5;
    private Speaker[] speakers = new Speaker[MAX_SPEAKERS];
    private int speakerCount = 0;

    //speaker status tracking
     private String[] speakerStatus = new String[MAX_SPEAKERS];
     private String[] rejectionReason = new String[MAX_SPEAKERS];
   

    // auto-generate sessionID: S001, S002, ...
    private static int sessionCounter = 1;

    private static String generateSessionID() {
        return String.format("S%03d", sessionCounter++);
    }

    public Session(String topic, String time) {
        this.sessionID = generateSessionID();
        this.topic = topic;
        this.time = time;

         for (int i = 0; i < MAX_SPEAKERS; i++) {
        speakerStatus[i] = "pending";
        rejectionReason[i] = "";
    }
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

    public Speaker[] getSpeakers() {
        return speakers;
    }

    public int getSpeakerCount() {
        return speakerCount;
    }

    // Setters
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // add speaker to session
    public boolean addSpeaker(Speaker speaker) {
        if (speakerCount >= MAX_SPEAKERS) {
            System.out.println("Error: Session [" + sessionID + "] already has the maximum number of speakers.");
            return false;
        }
        for (int i = 0; i < speakerCount; i++) {
            if (speakers[i].getAccessUsername().equals(speaker.getAccessUsername())) {
                System.out.println("Error: Speaker [" + speaker.getAccessUsername()
                        + "] is already assigned to session [" + sessionID + "].");
                return false;
            }
        }
        speakers[speakerCount] = speaker;
        speakerCount++;
        return true;
    }

    // remove speaker from session
    public boolean removeSpeaker(String username) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakers[i].getAccessUsername().equals(username)) {
                for (int j = i; j < speakerCount - 1; j++) {
                    speakers[j] = speakers[j + 1];
                }
                speakers[speakerCount - 1] = null;
                speakerCount--;
                System.out.println("Speaker [" + username + "] removed from session [" + sessionID + "].");
                return true;
            }
        }
        System.out.println("Error: Speaker [" + username + "] not found in session [" + sessionID + "].");
        return false;
    }

    // Get speaker status by username
public String getSpeakerStatus(String username) {
    for (int i = 0; i < speakerCount; i++) {
        if (speakers[i] != null && speakers[i].getAccessUsername().equals(username)) {
            return speakerStatus[i] != null ? speakerStatus[i] : "pending";
        }
    }
    return "not_assigned";
}

// Get rejection reason for a speaker
public String getRejectionReason(String username) {
    for (int i = 0; i < speakerCount; i++) {
        if (speakers[i] != null && speakers[i].getAccessUsername().equals(username)) {
            return rejectionReason[i] != null ? rejectionReason[i] : "";
        }
    }
    return "";
}

// Check if a speaker is assigned to this session
public boolean hasSpeaker(String username) {
    for (int i = 0; i < speakerCount; i++) {
        if (speakers[i] != null && speakers[i].getAccessUsername().equals(username)) {
            return true;
        }
    }
    return false;
}

// Accept session invitation
public boolean acceptInvitation(String username) {
    for (int i = 0; i < speakerCount; i++) {
        if (speakers[i] != null && speakers[i].getAccessUsername().equals(username)) {
            if ("pending".equals(speakerStatus[i])) {
                speakerStatus[i] = "accepted";
                System.out.println("Speaker [" + username + "] accepted session [" + sessionID + "].");
                return true;
            } else if ("accepted".equals(speakerStatus[i])) {
                System.out.println("Speaker [" + username + "] has already accepted this session.");
                return false;
            } else if ("rejected".equals(speakerStatus[i])) {
                System.out.println("Speaker [" + username + "] has already rejected this session.");
                return false;
            }
        }
    }
    System.out.println("Speaker [" + username + "] not found in session [" + sessionID + "].");
    return false;
}

// Reject session invitation with reason
public boolean rejectInvitation(String username, String reason) {
    for (int i = 0; i < speakerCount; i++) {
        if (speakers[i] != null && speakers[i].getAccessUsername().equals(username)) {
            if ("pending".equals(speakerStatus[i])) {
                speakerStatus[i] = "rejected";
                rejectionReason[i] = reason;
                System.out.println("Speaker [" + username + "] rejected session [" + sessionID + "].");
                System.out.println("Reason: " + reason);
                return true;
            } else if ("accepted".equals(speakerStatus[i])) {
                System.out.println("Speaker [" + username + "] has already accepted this session.");
                return false;
            } else if ("rejected".equals(speakerStatus[i])) {
                System.out.println("Speaker [" + username + "] has already rejected this session.");
                return false;
            }
        }
    }
    System.out.println("Speaker [" + username + "] not found in session [" + sessionID + "].");
    return false;
}
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[" + sessionID + "] Topic: " + topic + "  Time: " + time + "  Speakers: ");
        if (speakerCount == 0) {
            sb.append("None");
        } else {
            for (int i = 0; i < speakerCount; i++) {
                if (i > 0)
                    sb.append(", ");
                sb.append(speakers[i].getAccessUsername());
            }
        }
        return sb.toString();
    }
}
