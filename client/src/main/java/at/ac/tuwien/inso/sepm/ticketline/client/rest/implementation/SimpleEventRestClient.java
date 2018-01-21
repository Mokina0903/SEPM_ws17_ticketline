package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

@Component
public class SimpleEventRestClient implements EventRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventRestClient.class);
    private static final String EVENT_URL = "/event";

    private final RestClient restClient;

    public SimpleEventRestClient(RestClient restClient) {
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
    public DetailedEventDTO findById(Long id) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving event by id from {}", restClient.getServiceURI(EVENT_URL));
            ResponseEntity<DetailedEventDTO> event =
                restClient.exchange(
                    restClient.getServiceURI(EVENT_URL + "/" + id),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<DetailedEventDTO>() {
                    }
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
                    restClient.getServiceURI(EVENT_URL + "/" + request.getPageNumber() + "/" + request.getPageSize()),
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
    public Page<SimpleEventDTO> findAllByArtistId( Long artistId, Pageable request ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all upcoming events from {}", restClient.getServiceURI(EVENT_URL));
            ResponseEntity<RestResponsePage<SimpleEventDTO>> events =
                restClient.exchange(
                    restClient.getServiceURI(EVENT_URL + "/" + request.getPageNumber() + "/" + request.getPageSize()),
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
        }    }

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
                    new ParameterizedTypeReference<DetailedEventDTO>() {
                    }
                );
            LOGGER.debug("Result status was {} with content {}", event.getStatusCode(), event.getBody());
            return event.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve Event with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }


// https://stackoverflow.com/questions/8297215/spring-resttemplate-get-with-parameters

/*    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("title", title)
        ...

    HttpEntity<?> entity = new HttpEntity<>(headers);

    HttpEntity<String> response = restTemplate.exchange(
        builder.build().encode().toUri(),
        HttpMethod.GET,
        entity,
        String.class);*/

//https://docs.spring.io/spring-data/commons/docs/current/reference/html/#core.web.type-safe


    @Override
    public Page<SimpleEventDTO> findAdvanced(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            String url = EVENT_URL + "/advSearch/" + request.getPageNumber() + "/" + request.getPageSize();

            UriComponents builder = UriComponentsBuilder.fromPath(url)
                .queryParams(parameters).build();
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<RestResponsePage<SimpleEventDTO>> events =
                restClient.exchange(
                    restClient.getServiceURI(builder.toUriString()),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<RestResponsePage<SimpleEventDTO>>() {
                    });
            return events.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve events with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public Page<SimpleEventDTO> find(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            String url = EVENT_URL + "/search/" + request.getPageNumber() + "/" + request.getPageSize();

            UriComponents builder = UriComponentsBuilder.fromPath(url)
                .queryParams(parameters).build();
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<RestResponsePage<SimpleEventDTO>> events =
                restClient.exchange(
                    restClient.getServiceURI(builder.toUriString()),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<RestResponsePage<SimpleEventDTO>>() {
                    });
            return events.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve events with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

}
