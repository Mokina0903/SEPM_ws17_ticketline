package at.ac.tuwien.inso.sepm.ticketline.rest.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.naming.event.EventDirContext;

@ApiModel(value = "TicketDTO" , description = "A DTO for ticket entries via rest")
public class TicketDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(readOnly = true, required = true,name = "The event the ticket is for")
    private SimpleEventDTO event;

    @ApiModelProperty(readOnly = true, required = true, name = "The customer the ticket is sold/reserved to")
    private CustomerDTO customer;

    @ApiModelProperty(required = true, name = "The seat the ticket is associated with")
    private SeatDTO seat;

    @ApiModelProperty(required = true, name = "The explicit price of the ticket")
    private int price;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public SimpleEventDTO getEvent() {
        return event;
    }

    public void setEvent( SimpleEventDTO event ) {
        this.event = event;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer( CustomerDTO customer ) {
        this.customer = customer;
    }

    public SeatDTO getSeat() {
        return seat;
    }

    public void setSeat( SeatDTO seat ) {
        this.seat = seat;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice( int price ) {
        this.price = price;
    }

    public static TicketDTOBuilder builder(){return new TicketDTOBuilder();}

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketDTO ticketDTO = (TicketDTO) o;

        if (getPrice() != ticketDTO.getPrice()) return false;
        if (!getId().equals(ticketDTO.getId())) return false;
        if (!getEvent().equals(ticketDTO.getEvent())) return false;
        if (!getCustomer().equals(ticketDTO.getCustomer())) return false;
        return getSeat().equals(ticketDTO.getSeat());
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

    @Override
    public String toString() {
        return "TicketDTO{" +
            "id=" + id +
            ", event=" + event +
            ", customer=" + customer +
            ", seat=" + seat +
            ", price=" + price +
            '}';
    }

    public static final class TicketDTOBuilder{

        private Long id;
        private SimpleEventDTO eventDTO;
        private CustomerDTO customerDTO;
        private SeatDTO seatDTO;
        private int price;

        public TicketDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public TicketDTOBuilder event(SimpleEventDTO eventDTO){
            this.eventDTO = eventDTO;
            return this;
        }

        public TicketDTOBuilder customer(CustomerDTO customerDTO){
            this.customerDTO = customerDTO;
            return this;
        }

        public TicketDTOBuilder seat(SeatDTO seatDTO){
            this.seatDTO = seatDTO;
            return this;
        }

        public TicketDTOBuilder price(int price){
            this.price = price;
            return this;
        }

        public TicketDTO build(){
            TicketDTO ticketDTO= new TicketDTO();
            ticketDTO.setCustomer(customerDTO);
            ticketDTO.setEvent(eventDTO);
            ticketDTO.setId(id);
            ticketDTO.setPrice(price);
            ticketDTO.setSeat(seatDTO);

            return ticketDTO;
        }
    }
}
