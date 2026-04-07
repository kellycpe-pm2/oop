import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EventManagementSystem {
    private static List<Ticket> tickets = new java.util.ArrayList<>();
    private static Payment [] payments = new Payment[100];

    private static final int MAX_EVENTS = 300;

    private Event[] events = new Event[MAX_EVENTS];
    private int eventCount = 0;

    private static int user_no=0;
    private User current_user;
    private  User [] user = new User [400];

    
//get methods for Events
// Get all events across every type (flat array)

    public EventManagementSystem(User [] user,Event [] events, List<Ticket> tickets, Payment [] payments ){
        this.user=user;
        this.events=events;
        EventManagementSystem.tickets=tickets;
        EventManagementSystem.payments=payments;
        this.user_no=countUser_Num();
    
    }   
     public EventManagementSystem(){
        this(null,null,null,null);
    }

    //Getter Method

    public Event [] getEvents(){
        return events;
    }
    public User [] getUsers(){
        return user;
    }
    public User getCuurent_User(){
        return current_user;
    }
    public static Payment [] getPayments(){
        return EventManagementSystem.payments;
    }

    public static List <Ticket>  getTicket(){
        return tickets;
    }
    
    public void setCuurent_User(Object current_User, int type){
        switch(type){
            case 1:
                this.current_user=(Organizer) current_User;
            break;
            case 2:
                this.current_user=(Speaker) current_User;

            break;
            case 3:
                this.current_user=(Staff) current_User;

            break;
            case 4:
                this.current_user=(Attendee) current_User;
            break;
        }
        
    }

    public void setUsers(User [] alluser){
        this.user=alluser;
    }

    public void setEvents(Event [] events){
        this.events=events;
    }

    public static void setTickets(List<Ticket> tickets){
        EventManagementSystem.tickets=tickets;
    }

    public static void setPayment(Payment [] payments){
        EventManagementSystem.payments=payments;
    }
// other method
    public Event[] getActiveEvents() {
        int total = eventCount;
        Event[] active = new Event[total];
        int idx = 0;
            for (int i = 0; i < total; i++) {
                active[idx++] = events[i];
            }
        
        return active;
    }

    public int countUser_Num(){
        for (User current_user: user){
            if(current_user!=null){
                user_no++;
            }
            
        }
        if (user_no==0){
            return 0;
        }else{
            return user_no--;
   
        }
    }

    // Get only Concert events
    public Concert[] getActiveConcerts() {
        Concert[] result = new Concert[eventCount];
        for (int i = 0; i < eventCount; i++) {
            if (events[i].isConcert()){
                result[i] = (Concert) events[i];
            }
        }
        return result;
    }

    // Get only Workshop events
    public Workshop[] getActiveWorkshops() {
        Workshop[] result = new Workshop[eventCount];
        for (int i = 0; i < eventCount; i++) {
            if (events[i].isWorkshop()){
                result[i] = (Workshop) events[i];
            }        }
        return result;
    }

    // Get only Conference events
    public Conference[] getActiveConferences() {
        Conference[] result = new Conference[eventCount];
        for (int i = 0; i < eventCount; i++) {
            if (events[i].isConference()){
                result[i] = (Conference) events[i];
            }        
        }
        return result;
    }

    // Get a single event by eventID (searches all types)
    public Event getEventById(String eventID) {
        for (int type = 0; type < 3; type++) {
            for (int i = 0; i < eventCount; i++) {
                if (events[i] != null && events[i].equals(eventID)) {
                    return events[i];
                }
            }
        }
        System.out.println("Error: Event [" + eventID + "] not found !");
        return null;
    }

    // Total number of events across all types
    public int getEventCount() {
        return eventCount;
    }
//------------------------------------------------------------------------Get event end here
    public boolean validationInputTicketType(int choice) {
        if (choice == 1 || choice == 2 || choice == 3) {
            return true;
        }
        return false;
    }


    //==================================User part==============================
    public void addNewUser(User user){
        this.user[user_no] =user;
        user_no++;
    
    }
    
    //----------------------------------------------------------------------------------------
    //Staff Part
    //---------------------------------------------------------------------------------------

    
    

    //----------------------------------------------------------------------------------------
    
    
    // -------------------------- validation for event ---------------------------

    // validate title (must not be empty and at least 3 characters)
    public boolean validationTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Error: Title cannot be empty !");
            return false;
        } else if (title.trim().length() < 3) {
            System.out.println("Error: Title must be at least 3 characters !");
            return false;
        } else {
            return true;
        }
    }

    // validate date string (must be YYYY-MM-DD format and a future date)
    // returns the parsed LocalDate if valid, or null if invalid
    public LocalDate validationDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            System.out.println("Error: Date cannot be empty !");
            return null;
        }
        try {
            LocalDate parsedDate = LocalDate.parse(date.trim());
            if (!parsedDate.isAfter(LocalDate.now())) {
                System.out.println("Error: Event date must be in the future !");
                return null;
            }
            return parsedDate;
        } catch (DateTimeParseException e) {
            System.out.println("Error: Date format must be YYYY-MM-DD !");
            return null;
        }
    }

    // validate venue (must not be empty)
    public boolean validationVenue(String venue) {
        if (venue == null || venue.trim().isEmpty()) {
            System.out.println("Error: Venue cannot be empty !");
            return false;
        } else {
            return true;
        }
    }

    // validate max tickets (must be greater than 0)
    public boolean validationMaxTickets(int maxTickets) {
        if (maxTickets <= 0) {
            System.out.println("Error: Max tickets must be greater than 0 !");
            return false;
        } else {
            return true;
        }
    }

    // validate session time (must be HHMM format, 0000 - 2359)
    public boolean validationSessionTime(String time) {
        if (time == null || time.trim().isEmpty()) {
            System.out.println("Error: Session time cannot be empty !");
            return false;
        }
        if (time.trim().length() != 4) {
            System.out.println("Error: Session time must be 4 digits in HHMM format (e.g. 0900, 1430) !");
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (!Character.isDigit(time.trim().charAt(i))) {
                System.out.println("Error: Session time must contain digits only (e.g. 0900, 1430) !");
                return false;
            }
        }
        int hh = Integer.parseInt(time.trim().substring(0, 2));
        int mm = Integer.parseInt(time.trim().substring(2, 4));
        if (hh < 0 || hh > 23) {
            System.out.println("Error: Hour must be between 00 and 23 !");
            return false;
        }
        if (mm < 0 || mm > 59) {
            System.out.println("Error: Minute must be between 00 and 59 !");
            return false;
        }
        return true;
    }
        public boolean validationInputEventId(String eventId) {
            for (Event e : this.events) {
                if (e != null && eventId.equals(e.getEventID())) {
                    return true;
                }
            
            }

        return false;
        }

    // -------------------------- create event ---------------------------

     // Creates a Concert and stores it in events[CONCERT][]
    public Concert createConcert(String title, String date, String venue, int maxTickets) {
        if (!validationTitle(title))
            return null;
        LocalDate parsedDate = validationDate(date);
        if (parsedDate == null)
            return null;
        if (!validationVenue(venue))
            return null;
        if (!validationMaxTickets(maxTickets))
            return null;
        if (eventCount>= MAX_EVENTS) {
            System.out.println("Error: Concert list is full !");
            return null;
        }

        Concert c = new Concert(title, parsedDate, venue, maxTickets);
        events[eventCount]= c;
        eventCount++;
        System.out.println("Concert created and stored successfully : " + c.getEventID());
        return c;
    }

    // Creates a Workshop and stores it in events[WORKSHOP][]
    public Workshop createWorkshop(String title, String date, String venue, int maxTickets) {
        if (!validationTitle(title))
            return null;
        LocalDate parsedDate = validationDate(date);
        if (parsedDate == null)
            return null;
        if (!validationVenue(venue))
            return null;
        if (!validationMaxTickets(maxTickets))
            return null;
        if (eventCount >= MAX_EVENTS) {
            System.out.println("Error: Workshop list is full !");
            return null;
        }

        Workshop w = new Workshop(title, parsedDate, venue, maxTickets);
        events[eventCount] = w;
        eventCount ++;
        System.out.println("Workshop created and stored successfully : " + w.getEventID());
        return w;
    }

    // Creates a Conference (with optional sessions) and stores it in
    // events[CONFERENCE][]
    public Conference createConference(String title, String date, String venue, int maxTickets,
            String[] sessionTopics, String[] sessionTimes) {
        if (!validationTitle(title))
            return null;
        LocalDate parsedDate = validationDate(date);
        if (parsedDate == null)
            return null;
        if (!validationVenue(venue))
            return null;
        if (!validationMaxTickets(maxTickets))
            return null;
        if (eventCount >= MAX_EVENTS) {
            System.out.println("Error: Conference list is full !");
            return null;
        }

        Conference conf = new Conference(title, parsedDate, venue, maxTickets);
        if (sessionTopics != null && sessionTimes != null) {
            conf.autoCreateSessions(sessionTopics, sessionTimes);
        }
        events[eventCount] = conf;
        eventCount ++;
        System.out.println("Conference created and stored successfully : " + conf.getEventID());
        return conf;
    }

    // Overload — create Conference without sessions
    public Conference createConference(String title, String date, String venue, int maxTickets) {
        return createConference(title, date, venue, maxTickets, null, null);
    }

    // -------------------------- assign speaker ---------------------------

    public boolean assignSpeaker(Session session, Speaker speaker) {
        if (session == null) {
            System.out.println("Error: Session not found !");
            return false;
        }
        if (speaker == null) {
            System.out.println("Error: Speaker not found !");
            return false;
        }
        if (session.addSpeaker(speaker)) {
            System.out.println("Speaker [" + speaker.getAccessUsername() + "] assigned to session ["
                    + session.getSessionID() + "] successfully.");
            return true;
        }
        return false;
    }

    public boolean removeSpeaker(Session session, String speakerUsername) {
        if (session == null) {
            System.out.println("Error: Session not found !");
            return false;
        }
        return session.removeSpeaker(speakerUsername);
    }

//ticket type part
    // validate the quantity set
    public boolean validationQuantityTicket(int totalQuantity, int quantityEarlyBird, int quantityStandard, int quantityVip) {
        if (totalQuantity == quantityEarlyBird + quantityStandard + quantityVip) {
            return true;
        }
        else{
            System.out.println("Sum of ticket type quantities not equal to totalQuantity. Please reset the quantity of ticket.");
            return false;
        }
    }

    // validate price
    public boolean validationPrice(double priceEarlyBird, double priceStandard, double priceVip) {
        if (priceEarlyBird == 0 || priceStandard == 0 || priceVip == 0){
            System.out.println("Ticket prices cannot be zero.");
            return false;
        }
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

    public boolean validationPerks(String perks) {
        if (perks == null || perks.trim().isEmpty()) {
            System.out.println("Error: Perks cannot be empty !");
            return false;
        } else {
            return true;
        }
    }

    public LocalDate validationSalesStartDate(String ssdate, LocalDate eventDate) {
        if (ssdate == null || ssdate.trim().isEmpty()) {
            System.out.println("Error: Sales Start Date cannot be empty !");
            return null;
        }
        try {
            LocalDate parsedDate = LocalDate.parse(ssdate.trim());
            if (parsedDate.isAfter(eventDate) || parsedDate.isEqual(eventDate)) {
                System.out.println("Error: Sales Start Date must before event date !");
                return null;
            }
            else if (parsedDate.isBefore(LocalDate.now())){
                System.out.println("Error: Sales Start Date must in the future !");
                return null;
            }
            return parsedDate;
        } catch (DateTimeParseException e) {
            System.out.println("Error: Date format must be YYYY-MM-DD !");
            return null;
        }
    }

    public LocalDate validationSalesEndDate(String sedate, LocalDate salesStartDate, LocalDate eventDate) {
        if (sedate == null || sedate.trim().isEmpty()) {
            System.out.println("Error: Sales End Date cannot be empty !");
            return null;
        }
        try {
            LocalDate parsedDate = LocalDate.parse(sedate.trim());
            if (parsedDate.isAfter(eventDate) || parsedDate.isEqual(eventDate)) {
                System.out.println("Error: Sales End Date must before event date !");
                return null;
            }
            else if (parsedDate.isBefore(salesStartDate) || parsedDate.isEqual(salesStartDate)){
                System.out.println("Error: Sales End Date must after sales start date !");
                return null; 
            }
            else if(parsedDate.isBefore(LocalDate.now())){
                System.out.println("Error: Sales End Date must in the future !");
                return null;
            }
            return parsedDate;
        } catch (DateTimeParseException e) {
            System.out.println("Error: Date format must be YYYY-MM-DD !");
            return null;
        }
    }

    public Event findEventById(String eventId) {
            for (Event e : this.events) {
                if (e != null && e.equals(eventId)) {
                    return e;
                }
            }
        
        return null;

    }

// In EventManagementSystem.java
    public Ticket purchaseTicket(TicketType tt,String eventId, String ticketTypeName, String bookingId, int ticketCount) {    
        if (tt == null) {
            System.out.println("Error: TicketType cannot be null!");
            return null;
        }
        Ticket ticket = new Ticket(tt, current_user.getAccessUsername(),ticketTypeName, true, eventId, ticketCount,bookingId);
        return ticket;
    }
    public boolean validationPurchaseTicket(TicketType tt, String ticketTypeName){
        if (LocalDate.now().isAfter(tt.getSalesEnd()) || LocalDate.now().isBefore(tt.getSalesStart())){
            System.out.println("Error: Ticket cannot be purchased because the sales period haven't start/already over!");
            return false;
        }
        else if (ticketTypeName.toLowerCase().equals("earlybird")){
            if (LocalDate.now().isAfter(tt.getEarlyBirdEnd())){
                System.out.println("Error: Early Bird ticket cannot be purchased due to period is over!");
                return false;
            }
            return true;
        }
        else{
            return true;
        }
    }
}
