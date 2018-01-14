package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_invoice_id")
    @SequenceGenerator(name = "seq_invoice_id", sequenceName = "seq_invoice_id")
    private Long id;

    @Column(nullable = false)
    private Long invoiceNumber;

    @OneToMany
    private List<Ticket> tickets;

    @Column(nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private User vendor;

    @Column(nullable = false)
    private LocalDateTime invoiceDate;

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

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets( List<Ticket> tickets ) {
        this.tickets = tickets;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer( Customer customer ) {
        this.customer = customer;
    }

    public User getVendor() {
        return vendor;
    }

    public void setVendor( User vendor ) {
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

    public static InvoiceBuilder builder(){return new InvoiceBuilder();}

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invoice invoice = (Invoice) o;

        if (isStorno() != invoice.isStorno()) return false;
        if (!getId().equals(invoice.getId())) return false;
        if (!getInvoiceNumber().equals(invoice.getInvoiceNumber())) return false;
        if (!getTickets().equals(invoice.getTickets())) return false;
        if (!getCustomer().equals(invoice.getCustomer())) return false;
        if (!getVendor().equals(invoice.getVendor())) return false;
        return getInvoiceDate().equals(invoice.getInvoiceDate());
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
        return "Invoice{" +
            "id=" + id +
            ", invoiceNumber=" + invoiceNumber +
            ", tickets=" + tickets +
            ", customer=" + customer +
            ", vendor=" + vendor +
            ", invoiceDate=" + invoiceDate +
            ", isStorno=" + isStorno +
            '}';
    }

    public static final class InvoiceBuilder{
        private Long id;
        private Long invoiceNumber;
        private List<Ticket> tickets;
        private Customer customer;
        private User vendor;
        private LocalDateTime invoiceDate;
        private boolean isStorno;

        public InvoiceBuilder id(Long id){
            this.id = id;
            return this;
        }
        public InvoiceBuilder invoiceNumber(Long invoiceNumber){
            this.invoiceNumber = invoiceNumber;
            return this;
        }
        public InvoiceBuilder tickets(List<Ticket> tickets){
            this.tickets = tickets;
            return this;
        }
        public InvoiceBuilder customer(Customer customer){
            this.customer = customer;
            return this;
        }
        public InvoiceBuilder vendor(User user){
            this.vendor = user;
            return this;
        }
        public InvoiceBuilder invoiceDate(LocalDateTime invoiceDate){
            this.invoiceDate = invoiceDate;
            return this;
        }
        public InvoiceBuilder isStorno(boolean isStorno){
            this.isStorno = isStorno;
            return this;
        }

        public Invoice build(){
            Invoice invoice = new Invoice();
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
