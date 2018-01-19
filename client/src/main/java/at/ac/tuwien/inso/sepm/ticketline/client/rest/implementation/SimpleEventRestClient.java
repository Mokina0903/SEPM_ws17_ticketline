package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.ErrorDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.StatistikRequest;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    public Page<SimpleEventDTO> findAllUpcoming(Pageable request) throws DataAccessException {
        //use RestResponsePage instead of Page or PageImpl
        try {
            LOGGER.debug("Retrieving all upcoming events from {}", restClient.getServiceURI(EVENT_URL));
            ResponseEntity<RestResponsePage<SimpleEventDTO>> events =
                restClient.exchange(
                    restClient.getServiceURI(EVENT_URL + "/"+request.getPageNumber()+"/"+request.getPageSize()),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<RestResponsePage<SimpleEventDTO>>() {
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
    public DetailedEventDTO publishEvent(DetailedEventDTO detailedEventDTO) throws DataAccessException, ErrorDTO {
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
            throw ErrorDTO.ErrorDTOBuilder(e.getResponseBodyAsString());
            //throw new DataAccessException("Failed retrieve Event with status code " + e.getStatusCode().toString());
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<SimpleEventDTO> getTop10EventsOfMonthFilteredbyCategory(LocalDate beginOfMonth, LocalDate endOfMonth, String category) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving the events of a certain month from {}", restClient.getServiceURI(EVENT_URL));

            Long start = beginOfMonth.atStartOfDay(ZoneId.of("Europe/Paris")).toEpochSecond();
            Long end = endOfMonth.atStartOfDay(ZoneId.of("Europe/Paris")).toEpochSecond();

            ResponseEntity<List<SimpleEventDTO>> events =
                restClient.exchange(
                    restClient.getServiceURI(EVENT_URL+"/getTopTen/"+start+"/"+end+"/"+category),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleEventDTO>>() {
                    });
            System.out.println(events.getBody().size());
            LOGGER.debug("Result status was {}", events.getStatusCode());
            return events.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve events with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<SimpleEventDTO> getTop10EventsOfMonth(LocalDate beginOfMonth, LocalDate endOfMonth) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving the events of a certain month from {}", restClient.getServiceURI(EVENT_URL));

            Long start = beginOfMonth.atStartOfDay(ZoneId.of("Europe/Paris")).toEpochSecond();
            Long end = endOfMonth.atStartOfDay(ZoneId.of("Europe/Paris")).toEpochSecond();

            ResponseEntity<List<SimpleEventDTO>> events =
                restClient.exchange(
                    restClient.getServiceURI(EVENT_URL+"/getTopTen/"+start+"/"+end),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleEventDTO>>() {
                    });
            System.out.println(events.getBody().size());
            LOGGER.debug("Result status was {}", events.getStatusCode());
            return events.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve events with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
