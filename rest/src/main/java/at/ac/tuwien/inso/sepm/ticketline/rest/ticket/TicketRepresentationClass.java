package at.ac.tuwien.inso.sepm.ticketline.rest.ticket;

public class TicketRepresentationClass {

    private String eventName;
    private Long reservationNumber;
    private Long ticket_id;
    private Boolean isPaid;
    private String customerName;
    private String customerSurname;
    private String sector;
    private String seatNr;
    private Boolean hasSeatSelection;


    public TicketRepresentationClass(String eventName, Long reservationNumber, Long ticket_id, Boolean isPaid, String customerName, String customerSurname, String sector, String seatNr, Boolean hasSeatSelection) {
        this.eventName = eventName;
        this.reservationNumber = reservationNumber;
        this.ticket_id = ticket_id;
        this.isPaid = isPaid;
        this.customerName = customerName;
        this.customerSurname = customerSurname;
        this.sector = sector == null ? null: sector.toUpperCase();
        this.seatNr = seatNr;
        this.hasSeatSelection=hasSeatSelection;
    }

    public String getSeatNr() {
        return seatNr;
    }

    public void setSeatNr(String seatNr) {
        this.seatNr = seatNr;
    }

    public Boolean getHasSeatSelection() {
        return hasSeatSelection;
    }

    public void setHasSeatSelection(Boolean hasSeatSelection) {
        this.hasSeatSelection = hasSeatSelection;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
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
