package at.ac.tuwien.inso.sepm.ticketline.rest.invoice;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel(value = "InvoiceDTO", description = "A DTO for invoices via rest")
public class InvoiceDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(readOnly = true, name = "The automatically generated invoiceNumber")
    private Long invoiceNumber;

    @ApiModelProperty( name = "The list of tickets contained in the invoice")
    private List<TicketDTO> tickets;

    @ApiModelProperty( name = "The customer who ordered the tickets")
    private CustomerDTO customer;

    @ApiModelProperty( name = "The User who sold the tickets")
    private DetailedUserDTO vendor;

    @ApiModelProperty(readOnly = true, name = "The date and time the invoice was created")
    private LocalDateTime invoiceDate;

    @ApiModelProperty( name = "Set true if Invoice is a reversal")
    private boolean isStorno;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber( Long invoiceNumber ) {
        this.invoiceNumber = invoiceNumber;
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets( List<TicketDTO> tickets ) {
        this.tickets = tickets;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer( CustomerDTO customer ) {
        this.customer = customer;
    }

    public DetailedUserDTO getVendor() {
        return vendor;
    }

    public void setVendor( DetailedUserDTO vendor ) {
        this.vendor = vendor;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate( LocalDateTime invoiceDate ) {
        this.invoiceDate = invoiceDate;
    }

    public boolean isStorno() {
        return isStorno;
    }

    public void setStorno( boolean storno ) {
        isStorno = storno;
    }

    public double getTotalPriceInEuro(){
        double sum=0;
        for (TicketDTO ticketDTO:tickets){
            sum+=ticketDTO.getPriceInEuro();
        }
        return sum;
    }


    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvoiceDTO that = (InvoiceDTO) o;

        if (isStorno() != that.isStorno()) return false;
        if (!getId().equals(that.getId())) return false;
        if (!getInvoiceNumber().equals(that.getInvoiceNumber())) return false;
        if (!getTickets().equals(that.getTickets())) return false;
        if (!getCustomer().equals(that.getCustomer())) return false;
        if (!getVendor().equals(that.getVendor())) return false;
        return getInvoiceDate().equals(that.getInvoiceDate());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getInvoiceNumber().hashCode();
        result = 31 * result + getTickets().hashCode();
        result = 31 * result + getCustomer().hashCode();
        result = 31 * result + getVendor().hashCode();
        result = 31 * result + getInvoiceDate().hashCode();
        result = 31 * result + (isStorno() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "id=" + id +
            ", invoiceNumber=" + invoiceNumber +
            ", tickets=" + tickets +
            ", customer=" + customer +
            ", vendor=" + vendor +
            ", invoiceDate=" + invoiceDate +
            ", isStorno=" + isStorno +
            '}';
    }

    public static InvoiceDTOBuilder builder(){return new InvoiceDTOBuilder();}

    public static final class InvoiceDTOBuilder{
        private Long id;
        private Long invoiceNumber;
        private List<TicketDTO> tickets;
        private CustomerDTO customer;
        private DetailedUserDTO vendor;
        private LocalDateTime invoiceDate;
        private boolean isStorno;

        public InvoiceDTOBuilder id(Long id){
            this.id = id;
            return this;
        }
        public InvoiceDTOBuilder invoiceNumber(Long invoiceNumber){
            this.invoiceNumber = invoiceNumber;
            return this;
        }
        public InvoiceDTOBuilder tickets(List<TicketDTO> tickets){
            this.tickets = tickets;
            return this;
        }
        public InvoiceDTOBuilder customer(CustomerDTO customer){
            this.customer = customer;
            return this;
        }
        public InvoiceDTOBuilder vendor(DetailedUserDTO user){
            this.vendor = user;
            return this;
        }
        public InvoiceDTOBuilder invoiceDate(LocalDateTime invoiceDate){
            this.invoiceDate = invoiceDate;
            return this;
        }
        public InvoiceDTOBuilder isStorno(boolean isStorno){
            this.isStorno = isStorno;
            return this;
        }

        public InvoiceDTO build(){
            InvoiceDTO invoice = new InvoiceDTO();
            invoice.setId(id);
            invoice.setCustomer(customer);
            invoice.setInvoiceDate(invoiceDate);
            invoice.setInvoiceNumber(invoiceNumber);
            invoice.setTickets(tickets);
            invoice.setVendor(vendor);
            invoice.setStorno(isStorno);

            return invoice;
        }

    }
}
