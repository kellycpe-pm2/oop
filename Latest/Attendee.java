import java.util.List;

public class Attendee extends User{
    private int eventCount;
    private double totalSpent;
    private String membershipTier;

    // constructor
    private final String ROLE= "Attendee";
    public Attendee(){
        this(" ", " ", " ", " ");
    }

    public Attendee(String username, String password, String email, String contactNo) {
        super(username, password, email, contactNo);
        this.eventCount=0;
        this.totalSpent=0.0;
        this.membershipTier="Bronze";
    }

    public int getEventCount(){
        return this.eventCount;
    }

    public double getTotalSpent(){
        return this.totalSpent;
    }

    public String getMembershipTier(){
        return this.membershipTier;
    }

    public void setEventCount(int eventCount){
        this.eventCount=eventCount;
    }

    public void setTotalSpent(double totalSpent){
        this.totalSpent=totalSpent;
    }

    public void setMembershipTier(String membershipTier){
        this.membershipTier=membershipTier;
    }

    public void TicketPurchasedHistory(List<Ticket> tickets){
        eventCount=0;    

        if (tickets.isEmpty()) {
            System.out.println("No tickets purchased in the system.");
            return;
        }

        System.out.println("\n--- All Tickets History for " + super.getUsername() + " ---");

        for (Ticket t : tickets) {
            if (hasUser(t.getBuyerName())) {
                t.displayTicketDetails();
                System.out.println();
            }
        }

        if (eventCount == 0) {
            System.out.println("No tickets purchased by " + super.getUsername() + " yet.");
        } else {
            System.out.println("Found " + eventCount + " ticket(s) for " + super.getUsername());
        }
    }

    private void updateMembershipTier() {
        if (totalSpent >= 1000) {
            membershipTier = "Platinum";
        } else if (totalSpent >= 500) {
            membershipTier = "Gold";
        } else if (totalSpent >= 200) {
            membershipTier = "Silver";
        } else {
            membershipTier = "Bronze";
        }
    }

    public void addToTotalSpent(double amount) {
        if (amount > 0) {
            this.totalSpent += amount;
            updateMembershipTier();
        }
    }

    public String toString(){
            return super.toString()+String.format("|          Position       :  %-31s|\n",ROLE)+
               String.format("|          Total Spent    :  RM%-29.2f|\n", totalSpent) +
               String.format("|          Membership     :  %-31s|\n", membershipTier);
    }

    public boolean checkClass(Object o){
        if(o instanceof Attendee){
            return true;
        }
        return false;
    }

    public boolean equals(Object o) {
        if (super.equals(o)){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean hasUser(String username){
        if(getUsername().equals(username)){
            return true;
        }
        return false;
    }
}

