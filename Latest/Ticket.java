import java.time.LocalDate;

public class Ticket {

    private static int countTicket=0;

    private TicketType tt;
    private String ticketId;
    private String bookingId;
    private String eventId;
    private String seatNo;
    private double totalAmount;
    private String ticketType;
    private LocalDate purchaseDate;

    // constructor
    public Ticket(TicketType tt,String ticketType, String bookingId, String eventId, LocalDate purchaseDate) {
        this.tt = tt;
        this.ticketType = ticketType;
        this.bookingId = bookingId;
        this.eventId = eventId;
        this.purchaseDate = purchaseDate;
        this.ticketId = "T" + String.format("%05d", countTicket+1);
        countTicket++;
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
}
