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
    private int availableEarlyBird;
    private int availableStandard;
    private int availableVip;
    private double priceEarlyBird;
    private double priceStandard;
    private double priceVip;
    private LocalDate salesStart;
    private LocalDate salesEnd;
    private LocalDate earlyBirdEnd;
    private int [] quantityOfAllTicketType=new int [3];

    private List<String> vipSeats;
    private List<String> standardSeats;
    private List<String> earlyBirdSeats;

    // constructor
    public TicketType(String eventId, int totalQuantity, int totalearlyBird,int totalsandrand,int totalvip, int availableQuantity, int availableEarlyBird, int availableStandard, int availableVip,
            double priceEarlyBird, double priceStandard, double priceVip, String perks, LocalDate salesStart,
            LocalDate salesEnd) {
        this.eventId = eventId;
        this.totalQuantity = totalQuantity;
        this.quantityOfAllTicketType[0]=totalearlyBird;
        this.quantityOfAllTicketType[1]=totalsandrand;
        this.quantityOfAllTicketType[2]=totalvip;
        this.availableQuantity = availableQuantity;
        this.availableEarlyBird = availableEarlyBird;
        this.availableStandard = availableStandard;
        this.availableVip = availableVip;
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

    public int getTotalQuantity(){
        return this.totalQuantity;
    }

    public int [] getQuantityOfAllTicketType() {
        return quantityOfAllTicketType;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
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

    public int getAvailableType(String typeName) {
        switch (typeName.toLowerCase()) {
            case "earlybird":
                return this.availableEarlyBird;
            case "standard":
                return this.availableStandard;
            case "vip":
                return this.availableVip;
        }
        return 0;
    }

    // setter method
    public void setTotalQuantity(int totalQuantity, int quantityEarlyBird, int quantityStandard, int quantityVip) {
        this.totalQuantity = totalQuantity;
        this.quantityOfAllTicketType[0] = quantityEarlyBird;
        this.quantityOfAllTicketType[1] = quantityStandard;
        this.quantityOfAllTicketType[2] = quantityVip;
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

    // create TicketType file
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
                    int totalQuantity = (int)Double.parseDouble(lines.get(i + 1));
                    int quantityEarlyBird = (int)Double.parseDouble(lines.get(i + 2));
                    int quantityStandard = (int)Double.parseDouble(lines.get(i + 3));
                    int quantityVip = (int)Double.parseDouble(lines.get(i + 4));
                    int availableQuantity = (int)Double.parseDouble(lines.get(i + 5));
                    int availableEarlyBird = (int)Double.parseDouble(lines.get(i + 6));
                    int availableStandard = (int)Double.parseDouble(lines.get(i + 7));
                    int availableVip = (int)Double.parseDouble(lines.get(i + 8));
                    double priceEarlyBird = Double.parseDouble(lines.get(i + 9));
                    double priceStandard = Double.parseDouble(lines.get(i + 10));
                    double priceVip = Double.parseDouble(lines.get(i + 11));
                    String perks = lines.get(i + 12);
                    LocalDate salesStart = LocalDate.parse(lines.get(i + 13));
                    LocalDate salesEnd = LocalDate.parse(lines.get(i + 14));
                    // earlyBirdEnd is NOT stored — constructor computes it as salesStart.plusDays(1)

                    TicketType tt = new TicketType(eventId, totalQuantity, quantityEarlyBird, quantityStandard, quantityVip, availableQuantity, availableEarlyBird, availableStandard, availableVip, priceEarlyBird, priceStandard, priceVip, perks, salesStart, salesEnd);
                    ticketTypes.add(tt);

                    // NEW: Load tickets and remove already sold seats
                    List<Ticket> allTickets = Ticket.readTicketFile();
                    for (Ticket ticket : allTickets) {
                        if (ticket.getEventId().equals(tt.getEventId())) {
                            // Remove the seat that was already sold
                            String seatToRemove = ticket.getSeatNum();
                            tt.removeSeat(ticket.getTicketType(), seatToRemove);
                        }
                    }

                    i += 15; // 15 lines per record
                }
            }
            else{
                System.out.println("There is no ticket type record created.");
            }
        } catch (IOException e) {
            System.out.println("Error reading ticket type data: " + e.getMessage());
        }
        return ticketTypes;
    }

    // display all ticket type
    public static void displayAllTicketType(List<TicketType> TicketTypes) {
        System.out.println("=== Ticket Type Info ===");
        for (TicketType tt : TicketTypes) {
            System.out.println("Event Id: "+tt.getEventId());
            System.out.println("Total Quantity: "+tt.getTotalQuantity());
            System.out.println("Quantity of Early Bird, Standard, Vip: "+tt.getQuantityOfAllTicketType());
            System.out.println("Available Quantity: "+tt.getAvailableQuantity());
            System.out.println("Available Early Bird, Standard, Vip: "+tt.getAvailableType("earlybird")+", "+tt.getAvailableType("standard")+", "+tt.getAvailableType("vip"));
            System.out.println("Price Early Bird, Standard, Vip: (RM)"+tt.getPrice("earlybird")+", "+tt.getPrice("standard")+", "+tt.getPrice("vip"));
            System.out.println("Perks: "+tt.getPerks());
            System.out.println("Date Sales Start: "+tt.getSalesStart());
            System.out.println("Date Sales End: "+tt.getSalesEnd());
            System.out.println("Date Early Bird End: "+tt.getSalesStart().plusDays(1));
        }
    }

    // store ticket type data
    public static void storeTicketTypeData(List<TicketType> TicketTypes) {
        try (Writer writer = new java.io.FileWriter("TicketType.json")) {
            for (TicketType tt : TicketTypes) {
                writer.write(tt.getEventId() + "\n");
                writer.write(tt.getTotalQuantity() + "\n");
                writer.write(tt.getQuantityOfAllTicketType()[0] + "\n");
                writer.write(tt.getQuantityOfAllTicketType()[1] + "\n");
                writer.write(tt.getQuantityOfAllTicketType()[2] + "\n");
                writer.write(tt.getAvailableQuantity() + "\n");
                writer.write(tt.getAvailableType("earlybird") + "\n");
                writer.write(tt.getAvailableType("standard") + "\n");
                writer.write(tt.getAvailableType("vip") + "\n");
                writer.write(String.format("%.2f", tt.getPrice("earlybird")) + "\n");
                writer.write(String.format("%.2f", tt.getPrice("standard")) + "\n");
                writer.write(String.format("%.2f", tt.getPrice("vip")) + "\n"); 
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
                if (availableEarlyBird > 0) {
                    return true;
                }
                break;
            case "standard":
                if (availableStandard > 0) {
                    return true;
                }
                break;
            case "vip":
                if (availableVip > 0) {
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
                if (availableEarlyBird > 0) {
                    availableEarlyBird--;
                    availableQuantity--;
                    return true;
                }
                break;
            case "standard":
                if (availableQuantity > 0) {
                    availableStandard--;
                    availableQuantity--;
                    return true;
                }
                break;
            case "vip":
                if (availableVip > 0) {
                    availableVip--;
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

        int currentRow = 0; // Start from row A (index 0)

        // Generate VIP seats
        for (int i = 0; i < quantityOfAllTicketType[2]; i++) {
            int col = (i % seatsPerRow) + 1;
            char rowChar = (char) ('A' + currentRow);
            vipSeats.add(rowChar + "" + col);

            // Move to next row when current row is full
            if ((i + 1) % seatsPerRow == 0) {
                currentRow++;
            }
        }

        // Move to next row if VIP didn't fill a complete row
        if (quantityOfAllTicketType[2] % seatsPerRow != 0) {
            currentRow++;
        }

        // Generate Standard seats
        int standardStartRow = currentRow;
        for (int i = 0; i < quantityOfAllTicketType[1]; i++) {
            int col = (i % seatsPerRow) + 1;
            char rowChar = (char) ('A' + standardStartRow + (i / seatsPerRow));
            standardSeats.add(rowChar + "" + col);
        }

        // Calculate next row after Standard
        int standardRows = (int) Math.ceil((double) quantityOfAllTicketType[1] / seatsPerRow);
        int earlyBirdStartRow = standardStartRow + standardRows;

        // Generate EarlyBird seats
        for (int i = 0; i < quantityOfAllTicketType[0]; i++) {
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

    // Remove a specific seat from the available seats list
    public void removeSeat(String ticketType, String seatNum) {
        switch (ticketType.toLowerCase()) {
            case "earlybird":
                earlyBirdSeats.remove(seatNum);
                break;
            case "standard":
                standardSeats.remove(seatNum);
                break;
            case "vip":
                vipSeats.remove(seatNum);
                break;
        }
    }
}
