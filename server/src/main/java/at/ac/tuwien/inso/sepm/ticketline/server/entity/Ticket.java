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
    @Column(nullable = false)
    private Event event;

    @ManyToOne
    @Column(nullable = false)
    private Customer customer;

    @ManyToOne
    @Column(nullable = false)
    private Seat seat;

    @Column(nullable = false)
    private int price;

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
            '}';
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (getPrice() != ticket.getPrice()) return false;
        if (!getId().equals(ticket.getId())) return false;
        if (!getEvent().equals(ticket.getEvent())) return false;
        if (!getCustomer().equals(ticket.getCustomer())) return false;
        return getSeat().equals(ticket.getSeat());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getEvent().hashCode();
        result = 31 * result + getCustomer().hashCode();
        result = 31 * result + getSeat().hashCode();
        result = 31 * result + getPrice();
        return result;
    }

    public static final class TicketBuilder{
        private Long id;
        private Event event;
        private Customer customer;
        private Seat seat;
        private int price;

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

        public Ticket build(){
            Ticket ticket = new Ticket();
            ticket.setCustomer(customer);
            ticket.setEvent(event);
            ticket.setId(id);
            ticket.setPrice(price);
            ticket.setSeat(seat);

            return ticket;
        }
    }
}
