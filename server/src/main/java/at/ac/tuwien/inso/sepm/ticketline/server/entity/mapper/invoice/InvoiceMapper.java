package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.invoice;

import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    Invoice InvoiceDTOToInvoice( InvoiceDTO one);

    InvoiceDTO InvoiceToInvoiceDTO( Invoice one);

    List<InvoiceDTO> InvoiceToInvoiceDTO(List<Invoice> all);
}
