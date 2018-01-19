package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import javafx.stage.Window;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;

public interface InvoiceService {
    /**
     *find all invoices
     *
     * @param request defienes how to read paged from the database
     * @return a list of invoices, though the size of the list is dependent of the pageable object
     */
    Page<InvoiceDTO> findAll( Pageable request) throws DataAccessException;

    /**
     * Find a single invoice entry by id.
     *
     * @param id the is of the invoice entry
     * @return InvoiceDTO containing the invoice entry
     */
    InvoiceDTO findOneById( Long id) throws DataAccessException;

    /**
     * Find a single invoice entry by reservationnumber.
     *
     * @param reservationNumber the is of the invoice entry
     * @return InvoiceDTO containing the invoice entry
     */
    List<InvoiceDTO> findByReservationNumber( Long reservationNumber) throws DataAccessException;

    /**
     * save invoice
     *
     * @param invoice to be saved
     * @return saved invoice with created values
     */
    InvoiceDTO create(InvoiceDTO invoice) throws DataAccessException;

    File invoiceToPdf( InvoiceDTO invoiceDTO, Window window) throws DataAccessException;
}
