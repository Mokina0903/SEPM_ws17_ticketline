package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;

import javax.persistence.*;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_ticket_id")
    @SequenceGenerator(name = "seq_ticket_id", sequenceName = "seq_ticket_id")
    private Long id;

    @ManyToOne
    private Event event;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Seat seat;

    @Column(nullable = false)
    private int price;

    private boolean isDeleted;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted( boolean deleted ) {
        isDeleted = deleted;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid( boolean paid ) {
        isPaid = paid;
    }

    public Long getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber( Long reservationNumber ) {
        this.reservationNumber = reservationNumber;
    }

    private boolean isPaid;

    @Column(nullable = false, updatable = false)
    private Long reservationNumber;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent( Event event ) {
        this.event = event;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer( Customer customer ) {
        this.customer = customer;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat( Seat seat ) {
        this.seat = seat;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice( int price ) {
        this.price = price;
    }

    public Long calculatePrice() {
        if (event == null || seat == null)
            return 0L;

        if (((int) seat.getSector()) < 97)
            return event.getPrice();

        long priceSector = 100 + (((int) seat.getSector()) - 97) * 20;

        return (event.getPrice() * priceSector) / 100;
    }

    public static TicketBuilder builder(){
        return new TicketBuilder();
    }

    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + id +
            ", event=" + event +
            ", customer=" + customer +
            ", seat=" + seat +
            ", price=" + price +
            ", isDeleted=" + isDeleted +
            ", isPaid=" + isPaid +
            ", reservationNumber=" + reservationNumber +
            '}';
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (getPrice() != ticket.getPrice()) return false;
        if (isDeleted() != ticket.isDeleted()) return false;
        if (isPaid() != ticket.isPaid()) return false;
        if (!getId().equals(ticket.getId())) return false;
        if (!getEvent().equals(ticket.getEvent())) return false;
        if (!getCustomer().equals(ticket.getCustomer())) return false;
        if (!getSeat().equals(ticket.getSeat())) return false;
        return getReservationNumber().equals(ticket.getReservationNumber());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getEvent().hashCode();
        result = 31 * result + getCustomer().hashCode();
        result = 31 * result + getSeat().hashCode();
        result = 31 * result + getPrice();
        result = 31 * result + (isDeleted() ? 1 : 0);
        result = 31 * result + (isPaid() ? 1 : 0);
        result = 31 * result + getReservationNumber().hashCode();
        return result;
    }

    public static final class TicketBuilder{
        private Long id;
        private Event event;
        private Customer customer;
        private Seat seat;
        private int price;
        private long reservationNumbler;
        private boolean isDeleted;
        private boolean isPaid;

        public TicketBuilder id(Long id){
            this.id = id;
            return this;
        }
        public TicketBuilder event(Event event){
            this.event = event;
            return this;
        }
        public TicketBuilder customer(Customer customer){
            this.customer = customer;
            return this;
        }
        public TicketBuilder seat(Seat seat){
            this.seat = seat;
            return this;
        }
        public TicketBuilder price(int price){
            this.price = price;
            return this;
        }
        public TicketBuilder reservationNumber(Long reservationNumbler){
            this.reservationNumbler = reservationNumbler;
            return this;
        }
        public TicketBuilder isPaid(boolean isPaid){
            this.isPaid = isPaid;
            return this;
        }
        public TicketBuilder isDeleted(boolean isDeleted){
            this.isDeleted = isDeleted;
            return this;
        }

        public Ticket build(){
            Ticket ticket = new Ticket();
            ticket.setCustomer(customer);
            ticket.setEvent(event);
            ticket.setId(id);
            ticket.setPrice(price);
            ticket.setSeat(seat);
            ticket.setDeleted(isDeleted);
            ticket.setPaid(isPaid);
            ticket.setReservationNumber(reservationNumbler);

            return ticket;
        }
    }
}
