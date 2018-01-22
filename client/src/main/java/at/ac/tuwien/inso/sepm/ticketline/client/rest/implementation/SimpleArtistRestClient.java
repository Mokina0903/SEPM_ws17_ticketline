package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.ArtistRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
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

@Component
public class SimpleArtistRestClient implements ArtistRestClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArtistRestClient.class);
    private static final String ARTIST_URL = "/artist";

    private final RestClient restClient;

    public SimpleArtistRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Page<SimpleArtistDTO> find(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            String url = ARTIST_URL + "/search/" + request.getPageNumber() + "/" + request.getPageSize();

            UriComponents builder = UriComponentsBuilder.fromPath(url)
                .queryParams(parameters).build();
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<RestResponsePage<SimpleArtistDTO>> artist =
                restClient.exchange(
                    restClient.getServiceURI(builder.toUriString()),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<RestResponsePage<SimpleArtistDTO>>() {
                    });
            return artist.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve artists with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public Page<SimpleArtistDTO> findAdvanced(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            String url = ARTIST_URL + "/advSearch/" + request.getPageNumber() + "/" + request.getPageSize();

            UriComponents builder = UriComponentsBuilder.fromPath(url)
                .queryParams(parameters).build();
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<RestResponsePage<SimpleArtistDTO>> events =
                restClient.exchange(
                    restClient.getServiceURI(builder.toUriString()),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<RestResponsePage<SimpleArtistDTO>>() {
                    });
            return events.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve artists with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
