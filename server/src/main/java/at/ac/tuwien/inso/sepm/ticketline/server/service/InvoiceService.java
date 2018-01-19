package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface InvoiceService {

    /**
     *find all invoices
     *
     * @param request defienes how to read paged from the database
     * @return a list of invoices, though the size of the list is dependent of the pageable object
     */
    Page<Invoice> findAll( Pageable request);

    /**
     * Find a single invoice entry by id.
     *
     * @param id the is of the invoice entry
     * @return Optional containing the invoice entry
     */
    Invoice findOneById( Long id);

    /**
     * Find a single invoice entry by reservationnumber.
     *
     * @param reservationNumber  of the invoice entry
     * @return InvoiceDTO of the invoice entry
     */
    List<Invoice> findByReservationNumber( Long reservationNumber);

    /**
     * save invoice
     *
     * @param invoice to be saved
     * @return saved invoice with created values
     */
    Invoice save(Invoice invoice);

    void saveInvoicePDF( PDDocument document) throws IOException;
}
