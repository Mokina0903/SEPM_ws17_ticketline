package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Component
public class SimpleUserRestClient implements UserRestClient{
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleUserRestClient.class);
    private static final String User_URL = "/user";

    private final RestClient restClient;

    public SimpleUserRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public DetailedUserDTO findByName( String name ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving user by name from {}", restClient.getServiceURI(User_URL));
            ResponseEntity<DetailedUserDTO> user =
                restClient.exchange(
                    restClient.getServiceURI(User_URL+"/find/"+name),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<DetailedUserDTO>() {}
                );
            LOGGER.debug("Result status was {} with content {}", user.getStatusCode(), user.getBody());
            return user.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve user with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
