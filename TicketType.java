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

    //constructor
    public TicketType(String eventId, int totalQuantity,int quantityEarlyBird,int quantityStandard,int quantityVip,double priceEarlyBird, double priceStandard, double priceVip, String perks, LocalDate salesStart, LocalDate salesEnd){
        this.eventId=eventId;
        this.totalQuantity=totalQuantity;
        this.quantityEarlyBird=quantityEarlyBird;
        this.quantityStandard=quantityStandard;
        this.quantityVip=quantityVip;
        this.priceEarlyBird=priceEarlyBird;
        this.priceStandard=priceStandard;
        this.priceVip=priceVip;
        this.perks=perks;
        this.salesStart=salesStart;
        this.salesEnd=salesEnd;
        this.earlyBirdEnd=salesStart.plusDays(1);

        generateSeats();
    }

    //getter method
    public String getEventId() {
        return this.eventId;
    }
    public int getTotalQuantity() {
        return this.totalQuantity;
    }
    public int getQuantityEarlyBird(){
        return this.quantityEarlyBird;
    }
    public int getQuantityStandard(){
        return this.quantityStandard;
    }
    public int getQuantityVip(){
        return this.quantityVip;
    }
    public double getPrice(String typeName){
        switch(typeName.toLowerCase()) {
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
    public LocalDate getSalesStart(){
        return this.salesStart;
    }
    public LocalDate getSalesEnd(){
        return this.salesEnd;
    }
    public LocalDate getEarlyBirdEnd(){
        return this.earlyBirdEnd;
    }

    //setter method
    public void setTotalQuantity(int totalQuantity,int quantityEarlyBird, int quantityStandard, int quantityVip){
        this.totalQuantity = totalQuantity;
        this.quantityEarlyBird = quantityEarlyBird;
        this.quantityStandard = quantityStandard;
        this.quantityVip = quantityVip;
    }
    public void setPriceEarlyBird(double priceEarlyBird){
        this.priceEarlyBird = priceEarlyBird;
    }
    public void setPriceStandard(double priceStandard){
        this.priceStandard = priceStandard;
    }
    public void setPriceVip(double priceVip){
        this.priceVip = priceVip;
    }
    public void setPerks(String perks) {
        this.perks = perks;
    }
    public void setSalesStart(LocalDate salesStart){
        this.salesStart = salesStart;
    }
    public void setSalesEnd(LocalDate salesEnd){
        this.salesEnd = salesEnd;
    }
    public void setEarlyBirdEnd(LocalDate earlybirdEnd){
        this.earlyBirdEnd = earlybirdEnd;
    }


    //check availability of quantity ticket
    public boolean isAvailable() {
        return availableQuantity > 0;
    }

    //reduce quantity of ticket left
    public boolean reduceQuantity(String typeName) {
    switch(typeName.toLowerCase()) {
        case "earlybird":
            if(quantityEarlyBird > 0) { quantityEarlyBird--; availableQuantity--; return true; }
            break;
        case "standard":
            if(quantityStandard > 0) { quantityStandard--; availableQuantity--; return true; }
            break;
        case "vip":
            if(quantityVip > 0) { quantityVip--; availableQuantity--; return true; }
            break;
    }
    return false; // no ticket available for that ticket type
        }

    //validate the quantity set
    public boolean validationQuantityTicket(){
        if (totalQuantity<quantityEarlyBird+quantityStandard+quantityVip){
            System.out.println("Sum of ticket type quantities exceeds totalQuantity. Please reset the quantity of ticket.");
            return false;}
        if (totalQuantity>quantityEarlyBird+quantityStandard+quantityVip){
            System.out.println("Sum of ticket type quantities less than totalQuantity. Please reset the quantity of ticket.");
            return false;
        }
        else{
            return true;}
    }

    // validate date set
    public boolean validateDates(LocalDate salesStart, LocalDate salesEnd, LocalDate earlyBirdEnd) {
    if (salesStart.isAfter(salesEnd)) {
        System.out.println("Sales start date must be before sales end date.");
        return false;
    }
    if (earlyBirdEnd.isBefore(salesStart) || earlyBirdEnd.isAfter(salesEnd)) {
        System.out.println("Early bird end date must be between sales start and sales end dates.");
        return false;
    }
    return true;
    }

    // validate price
    public boolean validatePrices() {
    if (priceEarlyBird < 0 || priceStandard < 0 || priceVip < 0) {
        System.out.println("Ticket prices cannot be negative.");
        return false;
    }
    if (priceVip <= priceStandard) {
        System.out.println("VIP price should be greater than Standard price.");
        return false;
    }
    if (priceStandard <= priceEarlyBird) {
        System.out.println("Standard price should be greater than Early Bird price.");
        return false;
    }
    return true;
    }

    // generate all seat available and store into array list
    private void generateSeats() {
        int seatsPerRow = 10;

        vipSeats = new ArrayList<>();
        standardSeats = new ArrayList<>();
        earlyBirdSeats = new ArrayList<>();

        // VIP rows, start from row A
        for (int i = 0; i < quantityVip; i++) {
            int rowIndex = i / seatsPerRow;
            int col = (i % seatsPerRow) + 1;
            char row = (char) ('A' + rowIndex);
            vipSeats.add(row +""+col);
        }

        // Standard rows, start from row after VIP
        int standardStartRow = (quantityVip / seatsPerRow) + 1; // Next row after VIP
        for (int i = 0; i < quantityStandard; i++) {
            int rowIndex = i / seatsPerRow;
            int col = (i % seatsPerRow) + 1;
            char row = (char) ('A' + standardStartRow + rowIndex);
            standardSeats.add(row +""+col);
        }

        // EarlyBird rows, start after Standard
        int earlyBirdStartRow = standardStartRow + (quantityStandard / seatsPerRow) + 1;
        for (int i = 0; i < quantityEarlyBird; i++) {
            int rowIndex = i / seatsPerRow;
            int col = (i % seatsPerRow) + 1;
            char row = (char) ('A' + earlyBirdStartRow + rowIndex);
            earlyBirdSeats.add(row +""+col);
        }
    }

    // assign seat and remove it from available seat
    public String getSeat(String ticketType) {
        switch (ticketType.toLowerCase()) {
            case "vip":
                if (!vipSeats.isEmpty()) return vipSeats.remove(0);//check is the list empty? if no, remove the first seat inside the list
                break;
            case "standard":
                if (!standardSeats.isEmpty()) return standardSeats.remove(0);
                break;
            case "earlybird":
                if (!earlyBirdSeats.isEmpty()) return earlyBirdSeats.remove(0);
                break;
        }
        return null; // no seat available
    }

    //display ticket type detail
    public void displayTicketType(){
        
    }

}
