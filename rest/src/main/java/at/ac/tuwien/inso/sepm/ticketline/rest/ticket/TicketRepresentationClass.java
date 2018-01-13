package at.ac.tuwien.inso.sepm.ticketline.rest.ticket;

public class TicketRepresentationClass {

    private String eventName;
    private Long reservationNumber;
    private Long ticket_id;
    private boolean isPaid;
    private String customerName;
    private String customerSurname;

    public TicketRepresentationClass(String eventName, Long reservationNumber, Long ticket_id, boolean isPaid, String customerName, String customerSurname) {
        this.eventName = eventName;
        this.reservationNumber = reservationNumber;
        this.ticket_id = ticket_id;
        this.isPaid = isPaid;
        this.customerName = customerName;
        this.customerSurname = customerSurname;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(Long reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public Long getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(Long ticket_id) {
        this.ticket_id = ticket_id;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }
}
