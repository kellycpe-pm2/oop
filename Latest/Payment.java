import java.time.LocalDate;

public class Payment{
    private String name;
    private String bookingId;
    private static int bookingNo;
    private String eventId;
    double paymentAmount;
    private LocalDate paymentDate;

    public Payment(Attendee a, String eventId, double paymentAmount){
        name=a.getUsername();
        this.eventId=eventId;
        this.paymentAmount=paymentAmount;
        paymentDate=LocalDate.now();
        bookingId=generateBookingId(eventId);
        
    }

    public String getBookingId(){
        return this.bookingId;
    }

    public static void setbookingNo(int bookingNo){
        Payment.bookingNo=bookingNo;
    }

    public boolean validationPaymentMethod(int method){
        if (method==1||method==2||method==3)
            return true;
        else
            return false;
    }

    //generate Booking ID
    public String generateBookingId(String eventId){
        bookingId= "B" + eventId+String.format("%03d", bookingNo++);
        return bookingId;
    }


    public String toString(){
        return "Payment Details\n-------------------------\nName: "+name+"\nBooking Id: "+bookingId+"\nEvent Id: "+eventId+"\nPayment Amount: RM"+paymentAmount+"\nPayment Date:"+paymentDate;
    }

    public boolean equals(Object o) {
        if (this==o){return true;}
        if (o==null){
            return false;
        }
        if (o instanceof Payment) {
            Payment payment = (Payment) o;
            return this.bookingId.equals(payment.getBookingId());
        }
        return false; // the object does not belong to Event
    }
}
