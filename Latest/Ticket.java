import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;

public class Ticket {

    private static int countTicket=0;

    private TicketType tt;
    private String ticketId;
    private String bookingId;
    private String eventId;
    private String seatNo;
    private double totalAmount;
    private String ticketType;
    String perks;
    private LocalDate purchaseDate;

    // constructor
    public Ticket(TicketType tt, String ticketType, String bookingId, String eventId) {
        this.tt = tt;
        this.ticketType = ticketType;
        this.bookingId = bookingId;
        this.eventId = eventId;
        this.purchaseDate = LocalDate.now();
        countTicket++;
        this.ticketId = "T" + String.format("%05d", countTicket);
        this.totalAmount=tt.getPrice(ticketType);
        this.seatNo=tt.getSeat(ticketType);
        this.perks=tt.getPerks();
        tt.reduceQuantity(ticketType);
    }

    public Ticket(String ticketId, String bookingId, String eventId, String ticketType, double totalAmount, String seatNo, String perks, LocalDate purchaseDate) {
        this.ticketId = ticketId;
        this.bookingId = bookingId;
        this.eventId = eventId;
        this.ticketType = ticketType;
        this.totalAmount = totalAmount;
        this.seatNo = seatNo;
        this.perks=perks;
        this.purchaseDate = purchaseDate;
    }


    public boolean validationTicket() {

        if (!tt.reduceQuantity(ticketType)) {
            System.out.println("No tickets available for ticket type: " + ticketType);
            return false;
        }

        this.seatNo = tt.getSeat(ticketType);
        if (seatNo == null) {
            System.out.println("No seats available for TicketType: " + ticketType);
            return false;
        }

        this.totalAmount = tt.getPrice(ticketType);

        return true; // success
    }

    public double getTotalAmount(){
        return this.totalAmount;
    }

    public String getSeatNum(){
        return this.seatNo;
    }

    public String getTicketType(){
        return this.ticketType;
    }

    public String getBookingId(){
        return this.bookingId;
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

    public String getPerks(){
        return this.perks;
    }

    // display ticket details
    public void displayTicketDetails() {
        System.out.println("Ticket ID: " + ticketId);
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Event ID: " + eventId);
        System.out.println("Seat No: " + seatNo);
        System.out.println("Ticket Type: " + ticketType);
        System.out.println("Price: " + totalAmount);
        System.out.println("Perks: " + tt.getPerks());
        System.out.println("Purchase Date: " + purchaseDate);

    }

    
    // create Ticket file
    public void createTicketFile() {
        try {
            File ticketFile = new File("Ticket.json");
            if (ticketFile.createNewFile()) {
                System.out.println("Please Waiting...");
                System.out.println("Ticket file created: " + ticketFile.getName());
            }
        } catch (IOException e) {
            System.out.println("Error creating ticket file: " + e.getMessage());
        }
    }

    // display all tickets
    public static void displayAllTicket(List<Ticket> tickets) {
        System.out.println("=== Ticket History ===");
        System.out.println("------------------------------------------------------------------");
        for (Ticket t : tickets) {
            t.displayTicketDetails();
        }
    }

    // store ticket history data to Ticket.json
    public static void storeTicketData(List<Ticket> tickets) {
        try (Writer writer = new java.io.FileWriter("Ticket.json")) { // overwrite file
            for (Ticket t : tickets) {
                writer.write(t.getEventId() + "\n");
                writer.write(t.getBookingId() + "\n");
                writer.write(t.getTicketId() + "\n");
                writer.write(t.getTicketType() + "\n");
                writer.write(t.getTotalAmount() + "\n");
                writer.write(t.getPurchasedDate()+"\n");
                
            }
        } catch (IOException e) {
            System.out.println("Error storing ticket data: " + e.getMessage());
        }
    }

    public TicketType findTicketTypeById(TicketType[] ticketTypes, String eventId) {
        for (TicketType tt : ticketTypes) {
            if (tt != null && tt.getEventId().equals(eventId)) {
                return tt;
            }
        }
        return null;
    }
}
