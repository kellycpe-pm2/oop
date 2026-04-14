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
    public TicketType(){
        this(" ", 0, 0, 0, 0, 0, 0, 0, 0, 0.0, 0.0, 0.0, " ", null, null);
    }
    
    public TicketType(String eventId, int totalQuantity, int totalEarlyBird,int totalStandard,int totalVip, int availableQuantity, int availableEarlyBird, int availableStandard, int availableVip,
            double priceEarlyBird, double priceStandard, double priceVip, String perks, LocalDate salesStart,
            LocalDate salesEnd) {
        this.eventId = eventId;
        this.totalQuantity = totalQuantity;
        this.quantityOfAllTicketType[0]=totalEarlyBird;
        this.quantityOfAllTicketType[1]=totalStandard;
        this.quantityOfAllTicketType[2]=totalVip;
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
    public void setEventId(String eventId){
        this.eventId=eventId;
    }

    public void setAvailableQuantity(int availableQuantity, int availableEarlyBird, int availableStandard, int availableVip){
        this.availableQuantity=availableQuantity;
        this.availableEarlyBird=availableEarlyBird;
        this.availableStandard=availableStandard;
        this.availableVip=availableVip;
    }
    
    public void setTotalQuantity(int totalQuantity, int quantityEarlyBird, int quantityStandard, int quantityVip) {
        this.totalQuantity = totalQuantity;
        this.quantityOfAllTicketType[0] = quantityEarlyBird;
        this.quantityOfAllTicketType[1] = quantityStandard;
        this.quantityOfAllTicketType[2] = quantityVip;
    }

    public void setPrice(double priceEarlyBird, double priceStandard, double priceVip) {
        this.priceEarlyBird = priceEarlyBird;
        this.priceStandard=priceStandard;
        this.priceVip=priceVip;
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
        return false; // totalSpeakers ticket available for that ticket type
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
                    return vipSeats.remove(0);// check is the list empty? if totalSpeakers, remove the first seat inside the list
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
        return null; // totalSpeakers seat available
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

    public void updateQuantityWithSoldTickets(int newEarlyBird, int newStandard, int newVip) {
        int actualSoldEarlyBird = 0;
        int actualSoldStandard = 0;
        int actualSoldVip = 0;
        
        // Get tickets from TestUser.tickets
        for (Ticket ticket : TestUser.tickets) {
            if (ticket.getEventId().equals(this.eventId)) {
                switch (ticket.getTicketType().toLowerCase()) {
                    case "earlybird":
                        actualSoldEarlyBird++;
                        break;
                    case "standard":
                        actualSoldStandard++;
                        break;
                    case "vip":
                        actualSoldVip++;
                        break;
                }
            }
        }
        
        // Update available quantities based on ACTUAL sold tickets
        this.availableEarlyBird = newEarlyBird - actualSoldEarlyBird;
        if (this.availableEarlyBird < 0) this.availableEarlyBird = 0;
        
        this.availableStandard = newStandard - actualSoldStandard;
        if (this.availableStandard < 0) this.availableStandard = 0;
        
        this.availableVip = newVip - actualSoldVip;
        if (this.availableVip < 0) this.availableVip = 0;
        
        this.availableQuantity = this.availableEarlyBird + this.availableStandard + this.availableVip;

        generateSeats();
    }

    // display all ticket type
    public static void displayAllTicketType(List<TicketType> TicketTypes) {
        int i = 1 ;
        for (TicketType tt : TicketTypes) {
            System.out.println("\nTicket Type "+i);
            i++;
            System.out.println("========================");
            System.out.println("Event Id: "+tt.getEventId());
            System.out.println("Total Quantity: "+tt.getTotalQuantity());
            System.out.println("Quantity of Early Bird, Standard, Vip: "+tt.getQuantityOfAllTicketType()[0]+", "+tt.getQuantityOfAllTicketType()[1]+", "+tt.getQuantityOfAllTicketType()[2]);
            System.out.println("Available Quantity: "+tt.getAvailableQuantity());
            System.out.println("Available Early Bird, Standard, Vip: "+tt.getAvailableType("earlybird")+", "+tt.getAvailableType("standard")+", "+tt.getAvailableType("vip"));
            System.out.println("Price Early Bird, Standard, Vip: (RM)"+tt.getPrice("earlybird")+", "+tt.getPrice("standard")+", "+tt.getPrice("vip"));
            System.out.println("Perks: "+tt.getPerks());
            System.out.println("Date Sales Start: "+tt.getSalesStart());
            System.out.println("Date Sales End: "+tt.getSalesEnd());
            System.out.println("Date Early Bird End: "+tt.getSalesStart().plusDays(1));
        }
    }

    public String toString(){
        return "Event Id: "+this.getEventId()+"\nTotal Quantity: "+this.getTotalQuantity()+"\nQuantity of Early Bird, Standard, Vip: "+this.getQuantityOfAllTicketType()[0]+", "+this.getQuantityOfAllTicketType()[1]+", "+this.getQuantityOfAllTicketType()[2]
            +"\nAvailable Quantity: "+this.getAvailableQuantity()+"\nAvailable Early Bird, Standard, Vip: "+this.getAvailableType("earlybird")+", "+this.getAvailableType("standard")+", "+this.getAvailableType("vip")
            +"\nPrice Early Bird, Standard, Vip: (RM)"+this.getPrice("earlybird")+", "+this.getPrice("standard")+", "+this.getPrice("vip")
            +"\nPerks: "+this.getPerks()+"\nDate Sales Start: "+this.getSalesStart()+"\nDate Sales End: "+this.getSalesEnd()+"\nDate Early Bird End: "+this.getSalesStart().plusDays(1);
    }

    public boolean equals(Object o) {
       if(this == o){return true;}

       if (o==null){
            return false;
        }
        if (o instanceof TicketType) {
            TicketType ticketType = (TicketType) o;
            return this.eventId.equals(ticketType.getEventId());
        }
        return false; // the object does totalSpeakerst belong to Event
    }

}
