import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class TicketType {
    private String eventId;
    private String perks;
    private int totalQuantity;
    private int availableQuantity;
    private int quantityEarlyBird;
    private int quantityStandard;
    private int quantityVip;
    private double priceEarlyBird;
    private double priceStandard;
    private double priceVip;
    private LocalDate salesStart;
    private LocalDate salesEnd;
    private LocalDate earlyBirdEnd;

    private List<String> vipSeats;
    private List<String> standardSeats;
    private List<String> earlyBirdSeats;

    // constructor
    public TicketType(String eventId, int totalQuantity, int quantityEarlyBird, int quantityStandard, int quantityVip,
            double priceEarlyBird, double priceStandard, double priceVip, String perks, LocalDate salesStart,
            LocalDate salesEnd) {
        this.eventId = eventId;
        this.totalQuantity = totalQuantity;
        availableQuantity=totalQuantity;
        this.quantityEarlyBird = quantityEarlyBird;
        this.quantityStandard = quantityStandard;
        this.quantityVip = quantityVip;
        this.priceEarlyBird = priceEarlyBird;
        this.priceStandard = priceStandard;
        this.priceVip = priceVip;
        this.perks = perks;
        this.salesStart = salesStart;
        this.salesEnd = salesEnd;
        this.earlyBirdEnd = salesStart.plusDays(1);

        generateSeats();
    }

    // getter method
    public String getEventId() {
        return this.eventId;
    }

    public int getTotalQuantity() {
        return this.totalQuantity;
    }

    public int getQuantityEarlyBird() {
        return this.quantityEarlyBird;
    }

    public int getQuantityStandard() {
        return this.quantityStandard;
    }

    public int getQuantityVip() {
        return this.quantityVip;
    }

    public double getPrice(String typeName) {
        switch (typeName.toLowerCase()) {
            case "earlybird":
                return this.priceEarlyBird;
            case "standard":
                return this.priceStandard;
            case "vip":
                return this.priceVip;
        }
        return 0.00;
    }

    public String getPerks() {
        return this.perks;
    }

    public LocalDate getSalesStart() {
        return this.salesStart;
    }

    public LocalDate getSalesEnd() {
        return this.salesEnd;
    }

    public LocalDate getEarlyBirdEnd() {
        return this.earlyBirdEnd;
    }

    public int getAvailableQuantity(){
        return availableQuantity;
    }

    // setter method
    public void setTotalQuantity(int totalQuantity, int quantityEarlyBird, int quantityStandard, int quantityVip) {
        this.totalQuantity = totalQuantity;
        this.quantityEarlyBird = quantityEarlyBird;
        this.quantityStandard = quantityStandard;
        this.quantityVip = quantityVip;
    }

    public void setPriceEarlyBird(double priceEarlyBird) {
        this.priceEarlyBird = priceEarlyBird;
    }

    public void setPriceStandard(double priceStandard) {
        this.priceStandard = priceStandard;
    }

    public void setPriceVip(double priceVip) {
        this.priceVip = priceVip;
    }

    public void setPerks(String perks) {
        this.perks = perks;
    }

    public void setSalesStart(LocalDate salesStart) {
        this.salesStart = salesStart;
    }

    public void setSalesEnd(LocalDate salesEnd) {
        this.salesEnd = salesEnd;
    }

    public void setEarlyBirdEnd(LocalDate earlybirdEnd) {
        this.earlyBirdEnd = earlybirdEnd;
    }

    //create TicketType file
    public void createTicektTypeFile() {
        try {
            File ttFile = new File("TicketType.json");
            if (ttFile.createNewFile()) { 
                System.out.println("Please Waiting..."); 
                System.out.println("Ticket Type file created: " + ttFile.getName()); 
            } 
        } catch (IOException e) { 
            System.out.println("Error creating ticket type file: " + e.getMessage()); 
        } 
    }

    // Reads all ticket type from "TicketType.json"
    public static List<TicketType> readTicektTypeData() {
        List<TicketType> ticketTypes = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("TicketType.json"));
            if (!lines.isEmpty()) {
                int i = 0;
                while (i < lines.size()) {
                    String eventId = lines.get(i);
                    int totalQuantity = Integer.parseInt(lines.get(i + 1));
                    int availableQuantity = Integer.parseInt(lines.get(i + 2));
                    int quantityEarlyBird = Integer.parseInt(lines.get(i + 3));
                    int quantityStandard = Integer.parseInt(lines.get(i + 4));
                    int quantityVip = Integer.parseInt(lines.get(i + 5));
                    double priceEarlyBird = Double.parseDouble(lines.get(i + 6));
                    double priceStandard = Double.parseDouble(lines.get(i + 7));
                    double priceVip = Double.parseDouble(lines.get(i + 8));
                    String perks = lines.get(i + 9);
                    LocalDate salesStart = LocalDate.parse(lines.get(i + 10));
                    LocalDate salesEnd = LocalDate.parse(lines.get(i + 11));
                    LocalDate earlyBirdEnd = LocalDate.parse(lines.get(i + 12));

                    TicketType tt = new TicketType(eventId, totalQuantity, quantityEarlyBird, quantityStandard, quantityVip, priceEarlyBird, priceStandard, priceVip, perks, salesStart, salesEnd);
                    ticketTypes.add(tt);
                }
                    // Move to next ticket type: 13 lines per record
                    i += 13;
                }
        } catch (IOException e) {
            System.out.println("Error reading ticket type data: " + e.getMessage());
        }
        return ticketTypes;
    }

    // display all ticket type
    public static void displayAllTicketType(List<TicketType> TicketTypes) {
        System.out.println("=== Ticket Type Info ===");
        System.out.printf("%-8s %-15s %-20s %-10s %-10s", "EventId", "Total Quantity", "Perks", "SalesStart", "SalesEnd");
        System.out.println("--------------------------------------------------------------------");
        for (TicketType tt : TicketTypes) {
            System.out.printf("%-8s %-15s %-20s %-10s %-10s", 
                tt.getEventId(), 
                tt.getTotalQuantity(), 
                tt.getPerks(),
                tt.getSalesStart(),
                tt.getSalesEnd());
        }
    }

    // store ticket type data
    public static void storeTicketTypeData(List<TicketType> TicketTypes) {
        try (Writer writer = new java.io.FileWriter("TicketType.json")) {
            for (TicketType tt : TicketTypes) {
                writer.write(tt.getEventId() + "\n");
                writer.write(tt.getTotalQuantity() + "\n");
                writer.write(tt.getAvailableQuantity() + "\n");
                writer.write(tt.getQuantityEarlyBird() + "\n");
                writer.write(tt.getQuantityStandard() + "\n");
                writer.write(tt.getQuantityVip() + "\n");
                writer.write(tt.getPrice("earlybird") + "\n");
                writer.write(tt.getPrice("standard") + "\n");
                writer.write(tt.getPrice("vip") + "\n");
                writer.write(tt.getPerks() + "\n");
                writer.write(tt.getSalesStart().toString() + "\n");
                writer.write(tt.getSalesEnd().toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error storing ticket type data: " + e.getMessage());
        }
    }


    // check availability of quantity ticket
    public boolean isAvailable(String typeName) {
        switch (typeName.toLowerCase()) {
            case "earlybird":
                if (quantityEarlyBird > 0) {
                    return true;
                }
                break;
            case "standard":
                if (quantityStandard > 0) {
                    return true;
                }
                break;
            case "vip":
                if (quantityVip > 0) {
                    return true;
                }
                break;
        }
        return false;
    }

    // reduce quantity of ticket left
    public boolean reduceQuantity(String typeName) {
        switch (typeName.toLowerCase()) {
            case "earlybird":
                if (quantityEarlyBird > 0) {
                    quantityEarlyBird--;
                    availableQuantity--;
                    return true;
                }
                break;
            case "standard":
                if (quantityStandard > 0) {
                    quantityStandard--;
                    availableQuantity--;
                    return true;
                }
                break;
            case "vip":
                if (quantityVip > 0) {
                    quantityVip--;
                    availableQuantity--;
                    return true;
                }
                break;
        }
        return false; // no ticket available for that ticket type
    }

    // generate all seat available and store into array list
    private void generateSeats() {
        int seatsPerRow = 10;

        vipSeats = new ArrayList<>();
        standardSeats = new ArrayList<>();
        earlyBirdSeats = new ArrayList<>();

        int currentRow = 0;  // Start from row A (index 0)
        
        // Generate VIP seats
        for (int i = 0; i < quantityVip; i++) {
            int col = (i % seatsPerRow) + 1;
            char rowChar = (char) ('A' + currentRow);
            vipSeats.add(rowChar + "" + col);
        
            // Move to next row when current row is full
            if ((i + 1) % seatsPerRow == 0) {
                currentRow++;
            }
        }
    
        // Move to next row if VIP didn't fill a complete row
        if (quantityVip % seatsPerRow != 0) {
            currentRow++;
        }
    
        // Generate Standard seats
        int standardStartRow = currentRow;
        for (int i = 0; i < quantityStandard; i++) {
            int col = (i % seatsPerRow) + 1;
            char rowChar = (char) ('A' + standardStartRow + (i / seatsPerRow));
            standardSeats.add(rowChar + "" + col);
        }
    
        // Calculate next row after Standard
        int standardRows = (int) Math.ceil((double) quantityStandard / seatsPerRow);
        int earlyBirdStartRow = standardStartRow + standardRows;
    
        // Generate EarlyBird seats
        for (int i = 0; i < quantityEarlyBird; i++) {
            int col = (i % seatsPerRow) + 1;
            char rowChar = (char) ('A' + earlyBirdStartRow + (i / seatsPerRow));
            earlyBirdSeats.add(rowChar + "" + col);
        }
    }

    // assign seat and remove it from available seat
    public String getSeat(String ticketType) {
        switch (ticketType.toLowerCase()) {
            case "vip":
                if (!vipSeats.isEmpty())
                    return vipSeats.remove(0);// check is the list empty? if no, remove the first seat inside the list
                break;
            case "standard":
                if (!standardSeats.isEmpty())
                    return standardSeats.remove(0);
                break;
            case "earlybird":
                if (!earlyBirdSeats.isEmpty())
                    return earlyBirdSeats.remove(0);
                break;
        }
        return null; // no seat available
    }

    public static TicketType findTicketTypeById(List<TicketType> tt, String eventId) {
        for (TicketType t : tt) {
            if (t != null && t.getEventId().equals(eventId)) {
                return t;
            }
        }
        return null;
    }

    // display ticket type detail
    public String toString() {
        return eventId+" "+totalQuantity+" "+quantityEarlyBird+" "+quantityStandard+" "+quantityVip+" "+priceEarlyBird+" "+priceStandard+" "+priceVip+" "+perks+" "+salesStart+" "+salesEnd;
    }

}
