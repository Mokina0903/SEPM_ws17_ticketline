package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private Long price;

    private boolean isPaid;

    @Column(nullable = false)
    private Long reservationNumber;

    private boolean isDeleted;

    private LocalDateTime reservationDate;

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate( LocalDateTime reservationDate ) {
        this.reservationDate = reservationDate;
    }

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

    public Long getPrice() {
        return price;
    }

    public void setPrice( Long price ) {
        this.price = price;
    }

    public Long calculatePrice() {
        if (event == null || seat == null)
            return 0L;

        if (((int) seat.getSector()) < 97)
            return event.getPrice();

        long priceSector = 100 + (((int) seat.getSector()) - 97) * 20;

        long calcPrice = (event.getPrice() * priceSector) / 100;

        setPrice((int) calcPrice);

        return calcPrice;
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
            ", isPaid=" + isPaid +
            ", reservationNumber=" + reservationNumber +
            ", isDeleted=" + isDeleted +
            ", reservationDate=" + reservationDate +
            '}';
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (isPaid() != ticket.isPaid()) return false;
        if (isDeleted() != ticket.isDeleted()) return false;
        if (!getId().equals(ticket.getId())) return false;
        if (!getEvent().equals(ticket.getEvent())) return false;
        if (!getCustomer().equals(ticket.getCustomer())) return false;
        if (!getSeat().equals(ticket.getSeat())) return false;
        if (!getPrice().equals(ticket.getPrice())) return false;
        if (!getReservationNumber().equals(ticket.getReservationNumber())) return false;
        return getReservationDate() != null ? getReservationDate().equals(ticket.getReservationDate()) : ticket.getReservationDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getEvent().hashCode();
        result = 31 * result + getCustomer().hashCode();
        result = 31 * result + getSeat().hashCode();
        result = 31 * result + getPrice().hashCode();
        result = 31 * result + (isPaid() ? 1 : 0);
        result = 31 * result + getReservationNumber().hashCode();
        result = 31 * result + (isDeleted() ? 1 : 0);
        result = 31 * result + (getReservationDate() != null ? getReservationDate().hashCode() : 0);
        return result;
    }

    public static final class TicketBuilder{
        private Long id;
        private Event event;
        private Customer customer;
        private Seat seat;
        private Long price;
        private long reservationNumbler;
        private boolean isDeleted;
        private boolean isPaid;
        private LocalDateTime reservationDate;

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
        public TicketBuilder price(Long price){
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
        public TicketBuilder reservationDate(LocalDateTime reservationDate){
            this.reservationDate = reservationDate;
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
            ticket.setReservationDate(reservationDate);

            return ticket;
        }
    }
}
