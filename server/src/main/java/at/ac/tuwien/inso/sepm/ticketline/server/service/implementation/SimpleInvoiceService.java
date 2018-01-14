package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.InvoiceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.InvoiceService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class SimpleInvoiceService implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public SimpleInvoiceService( InvoiceRepository invoiceRepository ) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Page<Invoice> findAll( Pageable request ) {
        return invoiceRepository.findAll(request);
    }

    @Override
    public Invoice findOneById( Long id ) {
        return invoiceRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }
}
