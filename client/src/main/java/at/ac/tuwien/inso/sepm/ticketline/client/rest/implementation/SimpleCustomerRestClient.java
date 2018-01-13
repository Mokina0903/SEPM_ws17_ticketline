package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.OldVersionException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class SimpleCustomerRestClient implements CustomerRestClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCustomerRestClient.class);
    private static final String CUSTOMER_URL = "/customer";


    private final RestClient restClient;

    public SimpleCustomerRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Page<CustomerDTO> findAll(Pageable request) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all costumers from {}", restClient.getServiceURI(CUSTOMER_URL+"/"+request.getPageNumber()+"/"+request.getPageSize() ));
            ResponseEntity<RestResponsePage<CustomerDTO>> customer =
                restClient.exchange(
                    restClient.getServiceURI(CUSTOMER_URL+"/"+request.getPageNumber()+"/"+request.getPageSize()),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<RestResponsePage<CustomerDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", customer.getStatusCode(), customer.getBody());
            return customer.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve customer with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }


    @Override
    public Page<CustomerDTO> findByName(String name, Pageable request) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving found by name custumers from {}", restClient.getServiceURI(CUSTOMER_URL+"/findName/"+request.getPageNumber()+"/"+request.getPageSize()));
            ResponseEntity<RestResponsePage<CustomerDTO>> customer =
                restClient.exchange(
                    restClient.getServiceURI(CUSTOMER_URL+"/findName/"+request.getPageNumber()+"/"+request.getPageSize()+"/"+name),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<RestResponsePage<CustomerDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", customer.getStatusCode(), customer.getBody());
            return customer.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve customer with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public CustomerDTO findByNumber(Long customerNumber) throws DataAccessException, SearchNoMatchException {
        try {
            LOGGER.debug("Retrieving all costumers from {}", restClient.getServiceURI(CUSTOMER_URL+"/findWithKnr/"+customerNumber));
            ResponseEntity<CustomerDTO> customer =
                restClient.exchange(
                    restClient.getServiceURI(CUSTOMER_URL+"/findWithKnr/"+customerNumber),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<CustomerDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", customer.getStatusCode(), customer.getBody());
            return customer.getBody();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 404) {
                throw new SearchNoMatchException();
            }
            throw new DataAccessException("Failed retrieve customer with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void saveCustomer(CustomerDTO customerDTO) throws DataAccessException {
        try {
            LOGGER.debug("Save customer");
            HttpEntity<CustomerDTO> entity = new HttpEntity<>(customerDTO);
            restClient.exchange(
                    restClient.getServiceURI(CUSTOMER_URL+"/create"),
                    HttpMethod.POST,
                    entity,
                    Void.class);
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve customer with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void updateCustomer(CustomerDTO customer) throws DataAccessException, OldVersionException {
        try {
            LOGGER.debug("Update customer");
            HttpEntity<CustomerDTO> entity = new HttpEntity<>(customer);
            restClient.exchange(
                restClient.getServiceURI(CUSTOMER_URL+"/update"),
                HttpMethod.POST,
                entity,
                Void.class);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 424) {
                throw new OldVersionException();
            }
            else {
                throw new DataAccessException("Failed retrieve customer with status code " + e.getStatusCode().toString());
            }
            } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
