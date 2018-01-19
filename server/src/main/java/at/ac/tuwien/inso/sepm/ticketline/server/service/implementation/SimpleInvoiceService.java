package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.EmptyFieldException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.InvoiceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.InvoiceService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Service
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

    @Override
    public List<Invoice> findByReservationNumber( Long reservationNumber ) {
        List<Invoice> invoices = invoiceRepository.findByInvoiceNumber(reservationNumber);
        if(invoices==null || invoices.isEmpty()){throw new NotFoundException();}
        return invoices;
    }

    @Override
    public Invoice save( Invoice invoice ) {
        if(invoice.getTickets()==null || invoice.getTickets().isEmpty()){throw new EmptyFieldException();}

        invoice.setInvoiceNumber(invoice.getTickets().get(0).getReservationNumber());
        invoice.setInvoiceDate(LocalDateTime.now());

        return invoiceRepository.save(invoice);
    }

    @Override
    public void saveInvoicePDF( PDDocument document ) throws IOException {

        File template =  new File(getClass().getResource("/invoice/invoice.pdf").getPath()) ;
        File parent= template.getParentFile();


        document.save(parent.getPath()+"/Invoice"+document.getDocumentCatalog().getAcroForm().getFields().get(11).getValueAsString()+".pdf");

    }
}
