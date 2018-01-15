package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InvoiceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

public class SimpleInvoiceRestClient implements InvoiceRestClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleInvoiceRestClient.class);
    private static final String INVOICE_URL = "/invoice";

    private final RestClient restClient;

    public SimpleInvoiceRestClient( RestClient restClient ) {
        this.restClient = restClient;
    }

    @Override
    public Page<InvoiceDTO> findAll( Pageable request ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all invoices from {}", restClient.getServiceURI(INVOICE_URL+"/"+request.getPageNumber()+"/"+request.getPageSize() ));
            ResponseEntity<RestResponsePage<InvoiceDTO>> invoice =
                restClient.exchange(
                    restClient.getServiceURI(INVOICE_URL+"/"+request.getPageNumber()+"/"+request.getPageSize()),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<RestResponsePage<InvoiceDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", invoice.getStatusCode(), invoice.getBody());
            return invoice.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve invoice with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public InvoiceDTO findOneById( Long id ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving invoice by id from {}", restClient.getServiceURI(INVOICE_URL));
            ResponseEntity<InvoiceDTO> invoice =
                restClient.exchange(
                    restClient.getServiceURI(INVOICE_URL+"/"+id),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<InvoiceDTO>() {}
                );
            LOGGER.debug("Result status was {} with content {}", invoice.getStatusCode(), invoice.getBody());
            return invoice.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve invoice with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public InvoiceDTO create( InvoiceDTO invoice ) throws DataAccessException {
        try {
            LOGGER.debug("save invoice");
            HttpEntity<InvoiceDTO> entity = new HttpEntity<>(invoice);
            restClient.exchange(
                restClient.getServiceURI(INVOICE_URL+"/create"),
                HttpMethod.POST,
                entity,
                Void.class);
            return entity.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed save invoice with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
