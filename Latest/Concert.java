import java.time.LocalDate;
import java.util.List;

public class Concert extends Event {

    // Speaker names only 
    private static final int MAX_SPEAKERS = 1;
    private String[] speakerNames = new String[MAX_SPEAKERS];
    private int speakerCount = 0;
    
    // Speaker status tracking
    private String[] speakerStatus = new String[MAX_SPEAKERS];
    private String[] rejectionReason = new String[MAX_SPEAKERS];
    
    private final String TYPE = "Concert";

    public Concert(String title, LocalDate date, String venue, int maxTickets) {
        super(title, date, venue, maxTickets);
        initializeStatusArrays();
    }

    // Private constructor used only when loading from file — skips auto-save
    private Concert(String title, LocalDate date, String venue, int maxTickets, boolean fromFile) {
        super(title, date, venue, maxTickets);
        initializeStatusArrays();
    }
    
    private void initializeStatusArrays() {
        for (int i = 0; i < MAX_SPEAKERS; i++) {
            speakerStatus[i] = "pending";
            rejectionReason[i] = "no";
        }
    }

    // ── Speaker management ──────────────────────────────────────────────────

    public String[] getSpeakers() {
        return speakerNames;
    }

    public int getSpeakerCount() {
        return speakerCount;
    }
    
    public void setSpeakerCount(){
        this.speakerCount++;
    }
    
    // Get speaker status by name
    public String getSpeakerStatus(String speakerName) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i] != null && speakerNames[i].equals(speakerName)) {
                return speakerStatus[i] != null ? speakerStatus[i] : "pending";
            }
        }
        return "not_assigned";
    }
    
    // Get rejection reason by name
    public String getRejectionReason(String speakerName) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i] != null && speakerNames[i].equals(speakerName)) {
                return rejectionReason[i] != null ? rejectionReason[i] : "no";
            }
        }
        return "no";
    }
    // Add these methods after getRejectionReason() method
public void setSpeakerStatus(String speakerName, String status) {
    for (int i = 0; i < speakerCount; i++) {
        if (speakerNames[i] != null && speakerNames[i].equals(speakerName)) {
            speakerStatus[i] = status;
            return;
        }
    }
}

public void setRejectionReason(String speakerName, String reason) {
    for (int i = 0; i < speakerCount; i++) {
        if (speakerNames[i] != null && speakerNames[i].equals(speakerName)) {
            rejectionReason[i] = reason;
            return;
        }
    }
}


    // Accept invitation
    public boolean acceptInvitation(String speakerName) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i] != null && speakerNames[i].equals(speakerName)) {
                if ("pending".equals(speakerStatus[i])) {
                    speakerStatus[i] = "accepted";
                    System.out.println("Speaker [" + speakerName + "] accepted concert invitation.");
                    return true;
                } else if ("accepted".equals(speakerStatus[i])) {
                    System.out.println("You have already accepted this concert invitation.");
                    return false;
                } else {
                    System.out.println("You have already rejected this concert invitation.");
                    return false;
                }
            }
        }
        System.out.println("Speaker [" + speakerName + "] not found in this concert.");
        return false;
    }
    
    // Reject invitation with reason
    public boolean rejectInvitation(String speakerName, String reason) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i] != null && speakerNames[i].equals(speakerName)) {
                if ("pending".equals(speakerStatus[i])) {
                    speakerStatus[i] = "rejected";
                    rejectionReason[i] = reason;
                    System.out.println("Speaker [" + speakerName + "] rejected concert invitation.");
                    System.out.println("Reason: " + reason);
                    return true;
                } else if ("accepted".equals(speakerStatus[i])) {
                    System.out.println("You have already accepted this concert invitation.");
                    return false;
                } else {
                    System.out.println("You have already rejected this concert invitation.");
                    return false;
                }
            }
        }
        System.out.println("Speaker [" + speakerName + "] not found in this concert.");
        return false;
    }
    
    // Check if speaker is assigned
    public boolean hasSpeaker(String speakerName) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i] != null && speakerNames[i].equals(speakerName)) {
                return true;
            }
        }
        return false;
    }
    
    // Assign a speaker by name. Returns false if already assigned or full.
    public boolean assignSpeaker(String speakerName) {
        if (speakerCount >= MAX_SPEAKERS) {
            System.out.println("Error: Concert already has the maximum number of speakers.");
            return false;
        }
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i].equals(speakerName)) {
                System.out.println("Error: Speaker [" + speakerName
                        + "] is already assigned to this concert.");
                return false;
            }
        }
        speakerNames[speakerCount] = speakerName;
        speakerStatus[speakerCount] = "pending";
        rejectionReason[speakerCount] = "no";
        speakerCount++;
        System.out.println("Speaker [" + speakerName
                + "] assigned to concert [" + getEventID() + "] successfully.");
        return true;
    }

    // Remove a speaker from this concert by name.
    public boolean removeSpeaker(String speakerName) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i].equals(speakerName)) {
                for (int j = i; j < speakerCount - 1; j++) {
                    speakerNames[j] = speakerNames[j + 1];
                    speakerStatus[j] = speakerStatus[j + 1];
                    rejectionReason[j] = rejectionReason[j + 1];
                }
                speakerNames[speakerCount - 1] = null;
                speakerStatus[speakerCount - 1] = "pending";
                rejectionReason[speakerCount - 1] = "no";
                speakerCount--;
                System.out.println("Speaker [" + speakerName
                        + "] removed from concert [" + getEventID() + "] successfully.");
                return true;
            }
        }
        System.out.println("Error: Speaker [" + speakerName
                + "] not found in concert [" + getEventID() + "].");
        return false;
    }

    // Replace an existing speaker with a new one (change speaker).
    public boolean changeSpeaker(String oldSpeakerName, String newSpeakerName) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i].equals(oldSpeakerName)) {
                for (int j = 0; j < speakerCount; j++) {
                    if (j != i && speakerNames[j].equals(newSpeakerName)) {
                        System.out.println("Error: Speaker [" + newSpeakerName
                                + "] is already assigned to this concert.");
                        return false;
                    }
                }
                speakerNames[i] = newSpeakerName;
                speakerStatus[i] = "pending";
                rejectionReason[i] = "no";
                System.out.println("Speaker [" + oldSpeakerName + "] replaced with ["
                        + newSpeakerName + "] in concert [" + getEventID() + "] successfully.");
                return true;
            }
        }
        System.out.println("Error: Speaker [" + oldSpeakerName
                + "] not found in concert [" + getEventID() + "].");
        return false;
    }

    // Display speakers assigned to this concert.
    public void displaySpeakers() {
        System.out.println("  Speakers for Concert: " + getTitle());
        if (speakerCount == 0) {
            System.out.println("    No speakers assigned.");
        } else {
            for (int i = 0; i < speakerCount; i++) {
                System.out.println("    " + (i + 1) + ": " + speakerNames[i] + 
                        " [" + speakerStatus[i] + "]");
                if ("rejected".equals(speakerStatus[i]) && !rejectionReason[i].isEmpty()) {
                    System.out.println("       Reason: " + rejectionReason[i]);
                }
            }
        }
    }

    // display all concerts
    public static void displayAllConcerts(List<Concert> concerts) {
        System.out.println("=== Concert Info ===");
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n", "ID", "Title", "Date", "Venue", "MaxTix");
        System.out.println("------------------------------------------------------------------");
        for (Concert c : concerts) {
            System.out.println(c.toString());
            c.displaySpeakers();
        }
    }
    
    // remove a concert by eventID from the list and update Concert.json
    public static boolean removeConcert(List<Concert> concerts, String eventID) {
        for (int i = 0; i < concerts.size(); i++) {
            if (concerts.get(i).getEventID().equals(eventID)) {
                concerts.remove(i);
                System.out.println("Concert [" + eventID + "] removed successfully.");
                return true;
            }
        }
        System.out.println("Error: Concert [" + eventID + "] not found !");
        return false;
    }

    @Override
    public void displayInfo() {
        System.out.println("=== Concert Info ===");
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n",
                "ID", "Title", "Date", "Venue", "MaxTickets");
        System.out.println("------------------------------------------------------------------");
        System.out.println(this.toString());
        displaySpeakers();
    }

    @Override
    public String toString() {
        return super.toString() + String.format("%-14s│\n", TYPE);
    }

    public boolean isConcert() {
        return true;
    }

    public boolean isConference() {
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
