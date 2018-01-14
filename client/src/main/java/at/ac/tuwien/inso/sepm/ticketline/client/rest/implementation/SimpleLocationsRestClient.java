package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.LocationRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.SimpleHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Component
public class SimpleLocationsRestClient implements LocationRestClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLocationsRestClient.class);
    private static final String LOCATIONS_URL = "/eventlocation/location";
    private static final String HALL_URL = "/eventlocation/hall";

    private final RestClient restClient;

    public SimpleLocationsRestClient( RestClient restClient ) {
        this.restClient = restClient;
    }

    @Override
    public DetailedLocationDTO findLocationById( Long locationId ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving location by id from {}", restClient.getServiceURI(LOCATIONS_URL));
            ResponseEntity<DetailedLocationDTO> location =
                restClient.exchange(
                    restClient.getServiceURI(LOCATIONS_URL+"/"+locationId),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<DetailedLocationDTO>() {}
                );
            LOGGER.debug("Result status was {} with content {}", location.getStatusCode(), location.getBody());
            return location.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve location with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<SimpleLocationDTO> findAllLocations() throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all locations from {}", restClient.getServiceURI(LOCATIONS_URL));
            ResponseEntity<List<SimpleLocationDTO>> locations =
                restClient.exchange(
                    restClient.getServiceURI(LOCATIONS_URL),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleLocationDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", locations.getStatusCode(), locations.getBody());
            return locations.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve locations with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public DetailedHallDTO findHallById( Long hallId ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving hall by id from {}", restClient.getServiceURI(HALL_URL));
            ResponseEntity<DetailedHallDTO> hall =
                restClient.exchange(
                    restClient.getServiceURI(HALL_URL+"/"+hallId),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<DetailedHallDTO>() {}
                );
            LOGGER.debug("Result status was {} with content {}", hall.getStatusCode(), hall.getBody());
            return hall.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve hall with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<SimpleHallDTO> findAllHalls() throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all halls from {}", restClient.getServiceURI(HALL_URL));
            ResponseEntity<List<SimpleHallDTO>> halls =
                restClient.exchange(
                    restClient.getServiceURI(HALL_URL),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleHallDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", halls.getStatusCode(), halls.getBody());
            return halls.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve halls with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public Page<SimpleLocationDTO> findAdvanced(Pageable request, String search) throws DataAccessException {

        try {
            search = URLEncoder.encode(search, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("Error while encoding search path");
        }
        try {
            LOGGER.debug("Retrieving all upcoming events from {}", restClient.getServiceURI(LOCATIONS_URL));
            ResponseEntity<RestResponsePage<SimpleLocationDTO>> locations =
                restClient.exchange(
                    restClient.getServiceURI(LOCATIONS_URL + "/advSearch/" + request.getPageNumber() + "/" + request.getPageSize() + "/" + search),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<RestResponsePage<SimpleLocationDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", locations.getStatusCode(), locations.getBody());
            return locations.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve locations with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
