import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    private TicketType tt;
    private String ticketId;
    private boolean status;
    private String eventId;
    private String seatNo;
    private double totalAmount;
    private String buyerName;
    private String ticketType;
    private String bookingId;
    String perks;
    private LocalDate purchaseDate;
    private static int ticketCount;


    // constructor
    public Ticket(TicketType tt, String buyerName, String ticketType, boolean status, String eventId, int ticketCount,String bookingId) {
        this.tt = tt;
        this.buyerName =buyerName;
        this.ticketType = ticketType;
        this.bookingId=bookingId;
        this.status=status;
        this.eventId = eventId;
        this.purchaseDate = LocalDate.now();
        ticketCount++;
        this.ticketId = "T" + String.format("%05d", ticketCount);
        this.totalAmount=tt.getPrice(ticketType);
        this.seatNo=tt.getSeat(ticketType);
        this.perks=tt.getPerks();
        Ticket.ticketCount=ticketCount;
        
        tt.reduceQuantity(ticketType);
    }

    public Ticket(String ticketId, boolean status, String buyerName, String eventId, String ticketType, double totalAmount, String seatNo, String perks, LocalDate purchaseDate,String bookingId) {
        this.ticketId = ticketId;
        this.status=status;
        this.buyerName=buyerName;
        this.eventId = eventId;
        this.ticketType = ticketType;
        this.totalAmount = totalAmount;
        this.seatNo = seatNo;
        this.perks=perks;
        this.purchaseDate = purchaseDate;
        this.bookingId=bookingId;
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

    public String getBuyerName(){
        return this.buyerName;
    }

    public boolean getStatus(){
        return this.status;
    }

    public String getSeatNum(){
        return this.seatNo;
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

    public void setStatus(boolean status){
        updateTicketStatus(this.ticketId);
        this.status=status;
    }

    // display ticket details
    public void displayTicketDetails() {
        System.out.println("Ticket");
        System.out.println("-----------------------");
        System.out.println("Ticket ID: " + ticketId);
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Event ID: " + eventId);
        System.out.println("Seat No: " + seatNo);
        System.out.println("Ticket Type: " + ticketType);
        System.out.println("Price: " + totalAmount);
        System.out.println("Perks: " + perks);
        System.out.println("Purchase Date: " + purchaseDate);

    }

    // display all tickets
    public static void displayAllTicket(List<Ticket> tickets) {
        System.out.println("=== Ticket History ===");
        System.out.println("------------------------------------------------------------------");
        for (Ticket t : tickets) {
            t.displayTicketDetails();
        }
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

    // Reads all ticket from "Ticket.json"
    public static List<Ticket> readTicketFile() {
        List<Ticket> tickets = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("Ticket.json"));
            if (!lines.isEmpty()) {
                int i = 0;
                while (i < lines.size()) {
                    String ticketId = lines.get(i);
                    boolean status = Boolean.parseBoolean(lines.get(i + 1));
                    String buyerName = lines.get(i + 2);
                    String eventId = lines.get(i + 3);
                    String ticketType = lines.get(i + 4);
                    double totalAmount = Double.parseDouble(lines.get(i + 5));
                    String seatNo = lines.get(i + 6);
                    String perks = lines.get(i + 7);
                    LocalDate purchasedDate = LocalDate.parse(lines.get(i + 8));
                    String bookingId = lines.get(i + 9);

                    Ticket t = new Ticket(ticketId, status, buyerName, eventId, ticketType, totalAmount, seatNo, perks, purchasedDate,bookingId);
                    tickets.add(t);
                    i += 10; // 10 lines per record
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading ticket data: " + e.getMessage());
        }
        return tickets;
    }

    // store ticket history data to Ticket.json
    public static void storeTicketData(List<Ticket> tickets) {
        try (Writer writer = new java.io.FileWriter("Ticket.json")) {
            for (Ticket t : tickets) {
                writer.write(t.getTicketId() + "\n");
                writer.write(t.getStatus() + "\n");
                writer.write(t.getBuyerName() + "\n");
                writer.write(t.getEventId() + "\n");
                writer.write(t.getTicketType() + "\n");
                writer.write(t.getTotalAmount() + "\n");
                writer.write(t.getSeatNum() + "\n");
                writer.write(t.getPerks() + "\n"); 
                writer.write(t.getPurchasedDate() + "\n"); 
                writer.write(t.getBookingId() + "\n");

            }
        } catch (IOException e) {
            System.out.println("Error storing ticket data: " + e.getMessage());
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
            if (tt != null && eventId.equals(eventId)) {
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
                             ,this.bookingId, this.eventId, this.ticketId,this.ticketType,this.seatNo,this.purchaseDate);
    
    } 


   public boolean equals(Object o) {
        if (o==null){
            return false;
        }
        if (o instanceof Ticket) {
            Ticket ticket = (Ticket) o;
            return this.ticketId.equals(ticket.getTicketId());
        }
        return false; // the object does not belong to Event
    }

    public boolean equals(String ticketId){
        if (ticketId.equals(ticketId)){
            return true;
        }
        return false;
    }

}
