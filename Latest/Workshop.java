import java.time.LocalDate;
import java.util.List;

public class Workshop extends Event {

    // Speaker names only — no dependency on Speaker class
    private static final int MAX_SPEAKERS = 1;
    private String[] speakerNames = new String[MAX_SPEAKERS];
    private int speakerCount = 0;
    
    // Speaker status tracking
    private String[] speakerStatus = new String[MAX_SPEAKERS];
    private String[] rejectionReason = new String[MAX_SPEAKERS];
    
    private final String type = "Workshop";

    public Workshop(String title, LocalDate date, String venue, int maxTickets) {
        super(title, date, venue, maxTickets);
        initializeStatusArrays();
    }

    // Private constructor used only when loading from file — skips auto-save
    private Workshop(String title, LocalDate date, String venue, int maxTickets, boolean fromFile) {
        super(title, date, venue, maxTickets);
        initializeStatusArrays();
    }
    
    private void initializeStatusArrays() {
        for (int i = 0; i < MAX_SPEAKERS; i++) {
            speakerStatus[i] = "pending";
            rejectionReason[i] = "";
        }
    }

    // ── Speaker management ──────────────────────────────────────────────────

    public String[] getSpeakers() {
        return speakerNames;
    }

    public int getSpeakerCount() {
        return speakerCount;
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
                return rejectionReason[i] != null ? rejectionReason[i] : "";
            }
        }
        return "";
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
                    System.out.println("Speaker [" + speakerName + "] accepted workshop invitation.");
                    return true;
                } else if ("accepted".equals(speakerStatus[i])) {
                    System.out.println("You have already accepted this workshop invitation.");
                    return false;
                } else {
                    System.out.println("You have already rejected this workshop invitation.");
                    return false;
                }
            }
        }
        System.out.println("Speaker [" + speakerName + "] not found in this workshop.");
        return false;
    }
    
    // Reject invitation with reason
    public boolean rejectInvitation(String speakerName, String reason) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i] != null && speakerNames[i].equals(speakerName)) {
                if ("pending".equals(speakerStatus[i])) {
                    speakerStatus[i] = "rejected";
                    rejectionReason[i] = reason;
                    System.out.println("Speaker [" + speakerName + "] rejected workshop invitation.");
                    System.out.println("Reason: " + reason);
                    return true;
                } else if ("accepted".equals(speakerStatus[i])) {
                    System.out.println("You have already accepted this workshop invitation.");
                    return false;
                } else {
                    System.out.println("You have already rejected this workshop invitation.");
                    return false;
                }
            }
        }
        System.out.println("Speaker [" + speakerName + "] not found in this workshop.");
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
            System.out.println("Error: Workshop already has the maximum number of speakers.");
            return false;
        }
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i].equals(speakerName)) {
                System.out.println("Error: Speaker [" + speakerName
                        + "] is already assigned to this workshop.");
                return false;
            }
        }
        speakerNames[speakerCount] = speakerName;
        speakerStatus[speakerCount] = "pending";
        rejectionReason[speakerCount] = "";
        speakerCount++;
        System.out.println("Speaker [" + speakerName
                + "] assigned to workshop [" + getEventID() + "] successfully.");
        return true;
    }

    // Remove a speaker from this workshop by name.
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
                rejectionReason[speakerCount - 1] = "";
                speakerCount--;
                System.out.println("Speaker [" + speakerName
                        + "] removed from workshop [" + getEventID() + "] successfully.");
                return true;
            }
        }
        System.out.println("Error: Speaker [" + speakerName
                + "] not found in workshop [" + getEventID() + "].");
        return false;
    }

    // Replace an existing speaker with a new one (change speaker).
    public boolean changeSpeaker(String oldSpeakerName, String newSpeakerName) {
        for (int i = 0; i < speakerCount; i++) {
            if (speakerNames[i].equals(oldSpeakerName)) {
                for (int j = 0; j < speakerCount; j++) {
                    if (j != i && speakerNames[j].equals(newSpeakerName)) {
                        System.out.println("Error: Speaker [" + newSpeakerName
                                + "] is already assigned to this workshop.");
                        return false;
                    }
                }
                speakerNames[i] = newSpeakerName;
                speakerStatus[i] = "pending";
                rejectionReason[i] = "";
                System.out.println("Speaker [" + oldSpeakerName + "] replaced with ["
                        + newSpeakerName + "] in workshop [" + getEventID() + "] successfully.");
                return true;
            }
        }
        System.out.println("Error: Speaker [" + oldSpeakerName
                + "] not found in workshop [" + getEventID() + "].");
        return false;
    }

    // Display speakers assigned to this workshop.
    public void displaySpeakers() {
        System.out.println("  Speakers for Workshop: " + getTitle());
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

    // display all workshops
    public static void displayAllWorkshops(List<Workshop> workshops) {
        System.out.println("=== Workshop Info ===");
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n", "ID", "Title", "Date", "Venue", "MaxTix");
        System.out.println("--------------------------------------------------------------------");
        for (Workshop w : workshops) {
            System.out.println(w.toString());
            w.displaySpeakers();
        }
    }

    // remove a workshop by eventID from the list and update Workshop.json
    public static boolean removeWorkshop(List<Workshop> workshops, String eventID) {
        for (int i = 0; i < workshops.size(); i++) {
            if (workshops.get(i).getEventID().equals(eventID)) {
                workshops.remove(i);
                System.out.println("Workshop [" + eventID + "] removed successfully.");
                return true;
            }
        }
        System.out.println("Error: Workshop [" + eventID + "] not found !");
        return false;
    }

    @Override
    public void displayInfo() {
        System.out.println("=== Workshop Info ===");
        System.out.printf("%-6s %-20s %-12s %-20s %-8s%n",
                "ID", "Title", "Date", "Venue", "MaxTix");
        System.out.println("--------------------------------------------------------------------");
        System.out.println(this.toString());
        displaySpeakers();
    }

    public boolean isWorkshop() {
        return true;
    }

    public boolean isConcert() {
        return false;
    }

    public boolean isConference() {
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("%-14s│\n", type);
    }

    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        } else {
            return false;
        }
    }
}
