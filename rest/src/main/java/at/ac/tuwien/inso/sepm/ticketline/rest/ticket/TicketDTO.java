package at.ac.tuwien.inso.sepm.ticketline.rest.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

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
    private Long price;

    @ApiModelProperty(required = true, name = "The reservationNumber of the ticket")
    private Long reservationNumber;

    @ApiModelProperty(required = true, name = "True if ticket is sold, False if ticket is reserved")
    private boolean isPaid;

    @ApiModelProperty(required = true, name = "Set to true if ticket order was canceled")
    private boolean isDeleted;

    @ApiModelProperty( name = "Date of the reservation")
    private LocalDateTime reservationDate;

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid( boolean paid ) {
        isPaid = paid;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted( boolean deleted ) {
        isDeleted = deleted;
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

    public double getPriceInEuro(){return(double)price/(double)100;}

    public Long getPrice() {
        return price;
    }

    public void setPrice( Long price ) {
        this.price = price;
    }

    public static TicketDTOBuilder builder(){return new TicketDTOBuilder();}

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate( LocalDateTime reservationDate ) {
        this.reservationDate = reservationDate;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
            "id=" + id +
            ", event=" + event +
            ", customer=" + customer +
            ", seat=" + seat +
            ", price=" + price +
            ", reservationNumber=" + reservationNumber +
            ", isPaid=" + isPaid +
            ", isDeleted=" + isDeleted +
            ", reservationDate=" + reservationDate +
            '}';
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketDTO ticketDTO = (TicketDTO) o;

        if (isPaid() != ticketDTO.isPaid()) return false;
        if (isDeleted() != ticketDTO.isDeleted()) return false;
        if (!getId().equals(ticketDTO.getId())) return false;
        if (!getEvent().equals(ticketDTO.getEvent())) return false;
        if (!getCustomer().equals(ticketDTO.getCustomer())) return false;
        if (!getSeat().equals(ticketDTO.getSeat())) return false;
        if (!getPrice().equals(ticketDTO.getPrice())) return false;
        if (!getReservationNumber().equals(ticketDTO.getReservationNumber())) return false;
        return getReservationDate() != null ? getReservationDate().equals(ticketDTO.getReservationDate()) : ticketDTO.getReservationDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getEvent().hashCode();
        result = 31 * result + getCustomer().hashCode();
        result = 31 * result + getSeat().hashCode();
        result = 31 * result + getPrice().hashCode();
        result = 31 * result + getReservationNumber().hashCode();
        result = 31 * result + (isPaid() ? 1 : 0);
        result = 31 * result + (isDeleted() ? 1 : 0);
        result = 31 * result + (getReservationDate() != null ? getReservationDate().hashCode() : 0);
        return result;
    }

    public static final class TicketDTOBuilder{

        private Long id;
        private SimpleEventDTO eventDTO;
        private CustomerDTO customerDTO;
        private SeatDTO seatDTO;
        private Long price;
        private Long reservationNumber;
        private boolean isPaid;
        private boolean isDeleted;
        private LocalDateTime reservationDate;

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

        public TicketDTOBuilder price(Long price){
            this.price = price;
            return this;
        }
        public TicketDTOBuilder isDeleted(boolean isDeleted){
            this.isDeleted= isDeleted;
            return this;
        }
        public TicketDTOBuilder isPaid(boolean isPaid){
            this.isPaid = isPaid;
            return this;
        }
        public TicketDTOBuilder reservationNumber(Long reservationNumber){
            this.reservationNumber = reservationNumber;
            return this;
        }
        public TicketDTOBuilder reservationDate(LocalDateTime reservationDate){
            this.reservationDate = reservationDate;
            return this;
        }

        public TicketDTO build(){
            TicketDTO ticketDTO= new TicketDTO();
            ticketDTO.setCustomer(customerDTO);
            ticketDTO.setEvent(eventDTO);
            ticketDTO.setId(id);
            ticketDTO.setPrice(price);
            ticketDTO.setSeat(seatDTO);
            ticketDTO.setDeleted(isDeleted);
            ticketDTO.setPaid(isPaid);
            ticketDTO.setReservationNumber(reservationNumber);
            ticketDTO.setReservationDate(reservationDate);

            return ticketDTO;
        }
    }
}
