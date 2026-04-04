import java.time.LocalDateTime;

public class Payment{
    String name;
    String bookingId;
    String eventId;
    double paymentAmount;
    LocalDateTime paymentDate;

    public Payment(Attendee a, String eventId, String bookingId, double paymentAmount){
        name=a.getAccessUsername();
        this.bookingId=bookingId;
        this.eventId=eventId;
        this.paymentAmount=paymentAmount;
        paymentDate=LocalDateTime.now();
    }

    public boolean validationPaymentMethod(int method){
        if (method==1||method==2||method==3)
            return true;
        else
            return false;
    }

    public String toString(){
        return "\nName: "+name+"\nBooking Id: "+bookingId+"\nEvent Id: "+eventId+"\nPayment Amount: RM"+paymentAmount+"\nPayment Date:"+paymentDate;
    }
}
