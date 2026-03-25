import java.time.LocalDate;

public class EventManagementSystem {

    public boolean validationInputEventId(Event[] events, String eventId){
        for (Event e : events) {
            if (e!=null && eventId.equals(e.getEventID())){
                return true;
            }
        }
        return false;
    }

    public Event findEventById(Event[] events, String eventId) {
        for (Event e : events) {
            if (e.getEventID().equals(eventId)) {
                return e;
            }
        }
        return null;
    }

    public boolean validationInputTicketType(int choice){
            if (choice==1||choice==2||choice==3){
                return true;
            }
        return false;
    }

    public boolean payment(double amount){
        System.out.println("\nThe total amount = RM "+amount);
        System.out.println("Payment Method\n1. Touch N Go\n2.Credit/Debit Card\n3.Online Banking\nSelect your payment method:");
        return false;
    }
    
    public Ticket purchaseTicket(Attendee attendee,Event event,String type){
        TicketType tt=event.getTicketType();
        Ticket ticket = new Ticket(tt, type, null, event.getEventID(), LocalDate.now());

        if (ticket.validationTicket()) {
            attendee.addTicket(ticket);
            System.out.println("Purchase successful!");
            return ticket;
        } else {
            System.out.println("Purchase failed.");
            return null;
        }
    }
}
