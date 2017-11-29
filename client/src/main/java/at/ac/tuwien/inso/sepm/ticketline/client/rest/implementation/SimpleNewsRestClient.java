package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.AliasFor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class SimpleNewsRestClient implements NewsRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsRestClient.class);
    private static final String NEWS_URL = "/news";

    private final RestClient restClient;

    public SimpleNewsRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<SimpleNewsDTO> findAll() throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all news from {}", restClient.getServiceURI(NEWS_URL));
            ResponseEntity<List<SimpleNewsDTO>> news =
                restClient.exchange(
                    restClient.getServiceURI(NEWS_URL),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleNewsDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public DetailedNewsDTO findById( Long id ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving news by id from {}", restClient.getServiceURI(NEWS_URL));
            ResponseEntity<DetailedNewsDTO> news =
                restClient.exchange(
                    restClient.getServiceURI(NEWS_URL+"/"+id),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<DetailedNewsDTO>() {}
                );
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }    }

    @Override
    public DetailedNewsDTO publishNews(DetailedNewsDTO newNews) throws DataAccessException {
        try {
            LOGGER.debug("Publish news", restClient.getServiceURI(NEWS_URL));
            ResponseEntity<DetailedNewsDTO> news =
                restClient.exchange(
                    restClient.getServiceURI(NEWS_URL),
                    HttpMethod.POST,
                    null,
                    new ParameterizedTypeReference<DetailedNewsDTO>() {}
                );
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }


}
