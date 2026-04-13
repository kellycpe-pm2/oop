import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class Ticket {
    private TicketType tt;
    private String ticketId;
    private boolean status;
    private String eventId;
    private String seatNumber;
    private double totalAmount;
    private String buyerName;
    private String ticketType;
    private String bookingId;
    private String perks;
    private LocalDate purchaseDate;
    private static int ticketCount=0;


    // constructor
    // Constructor for creating NEW ticket

    public Ticket(TicketType tt, String buyerName, String ticketType, boolean status, String eventId, int ticketCount,String bookingId) {
        this.tt = tt;
        this.buyerName =buyerName;
        this.ticketType = ticketType;
        this.bookingId=bookingId;
        this.status=status;
        this.eventId = eventId;
        this.purchaseDate = LocalDate.now();

        //update the data using the old data
        Ticket.ticketCount=ticketCount;
        this.ticketId = "T" + String.format("%05d", Ticket.ticketCount);

        this.totalAmount=tt.getPrice(ticketType);
        this.seatNumber=tt.getSeat(ticketType);
        this.perks=tt.getPerks();
        tt.reduceQuantity(ticketType);
        //increase for the next use        
        Ticket.ticketCount++;

    }

    // Constructor for loading EXISTING ticket from file
    public Ticket(String ticketId, boolean status, String buyerName, String eventId, String ticketType, double totalAmount, String seatNumber, String perks, LocalDate purchaseDate,String bookingId) {
        this.ticketId = ticketId;
        this.status=status;
        this.buyerName=buyerName;
        this.eventId = eventId;
        this.ticketType = ticketType;
        this.totalAmount = totalAmount;
        this.seatNumber = seatNumber;
        this.perks=perks;
        this.purchaseDate = purchaseDate;
        this.bookingId=bookingId;
    }

    public double getTotalAmount(){
        return this.totalAmount;
    }

    public String getBuyerName(){
        return this.buyerName;
    }

    public boolean getStatus(){
        return this.status;
    }

    public String getSeatNum(){
        return this.seatNumber;
    }

    public String getTicketType(){
        return this.ticketType;
    }

    public String getEventId(){
        return this.eventId;
    }

    public LocalDate getPurchasedDate(){
        return this.purchaseDate;
    }

    public String getTicketId(){
        return this.ticketId;
    }
    public String getBookingId(){
        return this.bookingId;
    }

    public String getPerks(){
        return this.perks;
    }

    public static int getTicketCount(){
        return Ticket.ticketCount;
    }
    public void setStatus(boolean status){
        updateTicketStatus(this.ticketId);
        this.status=status;
    }

    public boolean validationTicket() {

        if (!tt.reduceQuantity(ticketType)) {
            System.out.println("No tickets available for ticket type: " + ticketType);
            return false;
        }

        this.seatNumber = tt.getSeat(ticketType);
        if (seatNumber == null) {
            System.out.println("No seats available for TicketType: " + ticketType);
            return false;
        }

        this.totalAmount = tt.getPrice(ticketType);

        return true; // success
    }
    
    // display ticket details
    public void displayTicketDetails() {
        System.out.println("Ticket");
        System.out.println("------------------------------------");
        System.out.println("Ticket ID         : " + ticketId);
        System.out.println("Booking ID        : " + bookingId);
        System.out.println("Event ID          : " + eventId);
        System.out.println("Seat Number       : " + seatNumber);
        System.out.println("Ticket Type       : " + ticketType);
        System.out.println("Price             : " + totalAmount);
        System.out.println("Perks             : " + perks);
        System.out.println("Purchase Date     : " + purchaseDate);

    }

    // display all tickets
    public static void displayAllTicket(List<Ticket> tickets) {
        System.out.println("=== Ticket History ===");
        System.out.println("------------------------------------------------------------------");
        for (Ticket t : tickets) {
            t.displayTicketDetails();
        }
    }

    public static void updateTicketStatus(String ticketId) {
    try {
        // Read all lines
        List<String> lines = Files.readAllLines(Paths.get("Ticket.json"));
        
        // Find and update the status line
        for (int i = 0; i < lines.size(); i++) {
            // Ticket ID is on lines 0, 10, 20, 30...
            if (i % 10 == 0 && lines.get(i).equals(ticketId)) {
                // Status is on the next line (i + 1)
                lines.set(i + 1, String.valueOf(false));
                break;
            }
        }
        
        // Write all lines back
        Files.write(Paths.get("Ticket.json"), lines);
        
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
}


    public TicketType findTicketTypeById(TicketType[] ticketTypes, String eventId) {
        for (TicketType tt : ticketTypes) {
            if (tt != null &&  tt.getEventId().equals(eventId)) {
                return tt;
            }
        }
        return null;
    }

    public String toString(){
                return String.format(
                             "║          Booking ID     :  %-31s║\n"+
                             "║          Event ID       :  %-31s║\n"+
                             "║          Ticket ID      :  %-31s║\n"+
                             "║          Ticket Type    :  %-31s║\n"+
                             "║          Seat No        :  %-31s║\n"
                             ,this.bookingId, this.eventId, this.ticketId,this.ticketType,this.seatNumber,this.purchaseDate);
    
    } 


   public boolean equals(Object o) {
        if(this ==o){
            return true;
        }
        
        if (o==null){
            return false;
        }
        if (o instanceof Ticket) {
            Ticket ticket = (Ticket) o;
            return this.ticketId.equals(ticket.getTicketId());
        }
        return false; // the object does No belong to Event
    }

    public boolean hasTicket(String ticketId){
        return this.ticketId != null && this.ticketId.equals(ticketId); 
    }

}
