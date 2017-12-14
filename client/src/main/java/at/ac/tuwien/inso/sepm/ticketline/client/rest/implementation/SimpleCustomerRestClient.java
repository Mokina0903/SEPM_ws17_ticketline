package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
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
    public List<CustomerDTO> findAll(int pageIndex, int costumerPerPage) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all costumers from {}", restClient.getServiceURI(CUSTOMER_URL+"/"+pageIndex+"/"+costumerPerPage ));
            ResponseEntity<List<CustomerDTO>> customer =
                restClient.exchange(
                    restClient.getServiceURI(CUSTOMER_URL+"/"+pageIndex+"/"+costumerPerPage ),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CustomerDTO>>() {
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
    public List<CustomerDTO> findByName(String name) throws DataAccessException {
        return null;
    }

    @Override
    public List<CustomerDTO> findByNumber(Long customerNumber) throws DataAccessException {
        return null;
    }
}
