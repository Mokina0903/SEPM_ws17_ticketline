package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.EmptyValueException;
import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface InvoiceRestClient {
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
     * @return Optional containing the invoice entry
     */
    InvoiceDTO findOneById( Long id) throws DataAccessException;

    /**
     * Find invoice/reversal entries by reservationnumber.
     *
     * @param reservationNumber  of the invoice entry
     * @return List of InvoiceDTO of the invoice entry
     */
    List<InvoiceDTO> findByReservationNumber( Long reservationNumber) throws DataAccessException;

    /**
     * save invoice
     *
     * @param invoice to be saved
     * @return saved invoice with created values
     */
    InvoiceDTO create(InvoiceDTO invoice) throws DataAccessException, EmptyValueException;

    /**
     * update invoice
     *
     * @param invoice to be updated
     * @return updated invoice
     */
    InvoiceDTO update(InvoiceDTO invoice) throws DataAccessException;

    void saveInvoicePdf( File document) throws DataAccessException, IOException;
}
