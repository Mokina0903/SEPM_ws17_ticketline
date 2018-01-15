package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InvoiceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SimpleInvoiceService implements InvoiceService{

    private final InvoiceRestClient invoiceRestClient;

    public SimpleInvoiceService( InvoiceRestClient invoiceRestClient ) {
        this.invoiceRestClient = invoiceRestClient;
    }

    @Override
    public Page<InvoiceDTO> findAll( Pageable request ) throws DataAccessException {
        return invoiceRestClient.findAll(request);
    }

    @Override
    public InvoiceDTO findOneById( Long id ) throws DataAccessException {
        return invoiceRestClient.findOneById(id);
    }

    @Override
    public InvoiceDTO create( InvoiceDTO invoice ) throws DataAccessException {
        return invoiceRestClient.create(invoice);
    }
}
