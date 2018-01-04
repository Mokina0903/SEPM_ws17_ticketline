package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class SimpleEventRestClient implements EventRestClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventRestClient.class);
    private static final String EVENT_URL = "/event";

    private final RestClient restClient;

    public SimpleEventRestClient( RestClient restClient ) {
        this.restClient = restClient;
    }


    @Override
    public List<SimpleEventDTO> findAll() throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all events from {}", restClient.getServiceURI(EVENT_URL));
            ResponseEntity<List<SimpleEventDTO>> events =
                restClient.exchange(
                    restClient.getServiceURI(EVENT_URL),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleEventDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", events.getStatusCode(), events.getBody());
            return events.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve events with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public DetailedEventDTO findById( Long id ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving event by id from {}", restClient.getServiceURI(EVENT_URL));
            ResponseEntity<DetailedEventDTO> event =
                restClient.exchange(
                    restClient.getServiceURI(EVENT_URL+"/"+id),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<DetailedEventDTO>() {}
                );
            LOGGER.debug("Result status was {} with content {}", event.getStatusCode(), event.getBody());
            return event.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve event with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<SimpleEventDTO> findAllUpcoming( int pageIndex, int eventsPerPage ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all upcoming events from {}", restClient.getServiceURI(EVENT_URL));
            ResponseEntity<List<SimpleEventDTO>> events =
                restClient.exchange(
                    restClient.getServiceURI(EVENT_URL + "/"+pageIndex+"/"+eventsPerPage),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleEventDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", events.getStatusCode(), events.getBody());
            return events.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve events with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public DetailedEventDTO publishEvent(DetailedEventDTO detailedEventDTO) throws DataAccessException {
        try {
            LOGGER.debug("Publish Event", restClient.getServiceURI(EVENT_URL));
            HttpEntity<DetailedEventDTO> httpEntity = new HttpEntity<>(detailedEventDTO);
            ResponseEntity<DetailedEventDTO> event =
                restClient.exchange(
                    restClient.getServiceURI(EVENT_URL),
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<DetailedEventDTO>() {}
                );
            LOGGER.debug("Result status was {} with content {}", event.getStatusCode(), event.getBody());
            return event.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve Event with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
