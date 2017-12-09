package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Component
public class SimpleUserRestClient implements UserRestClient {


    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsRestClient.class);

    private static final String USER_URL = "/user";

    private final RestClient restClient;

    public SimpleUserRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public SimpleUserDTO findByUsername(String userName) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving attempts by userName from {}", restClient.getServiceURI(USER_URL));
            ResponseEntity<SimpleUserDTO> user =
                restClient.exchange(
                    restClient.getServiceURI(USER_URL + "/" + userName),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<SimpleUserDTO>() {
                    }
                );
            LOGGER.debug("Result status was {} with content {}", user.getStatusCode(), user.getBody());
            return user.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve userAttempts with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public Integer getLoginAttemptsLeft(String username) throws DataAccessException {

        try {
            ResponseEntity<Integer> loginAttemptsLeft = restClient.getForEntity(
                restClient.getServiceURI(USER_URL) + "/{username}/getAttempts",
                Integer.class,
                username
            );

            LOGGER.debug("Result status code was {}", loginAttemptsLeft.getStatusCode());
            return loginAttemptsLeft.getBody();
        }
        catch(HttpStatusCodeException e) {

            throw new DataAccessException("Failed to retrieve the number of login attempts left for user " + username, e);
        }
        catch(RestClientException e) {

            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public SimpleUserDTO decreaseLoginAttempts(String username) throws DataAccessException {

        try {
            ResponseEntity<SimpleUserDTO> user = restClient.postForEntity(
                restClient.getServiceURI(USER_URL) + "/decreaseAttempts",
                username,
                SimpleUserDTO.class
            );
            LOGGER.debug("Result status code was {}", user.getStatusCode());
            return user.getBody();
        }
        catch(HttpStatusCodeException e) {
            LOGGER.debug("Result status code was {}", e.getMessage());

            throw new DataAccessException("Failed to update user " + username + " with status code " + e.getStatusCode());
        }
        catch(RestClientException e) {
            LOGGER.debug("Result status code was {}", e.getMessage());

            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public SimpleUserDTO resetLoginAttempts(String username) throws DataAccessException {

        try {
            ResponseEntity<SimpleUserDTO> user = restClient.postForEntity(
                restClient.getServiceURI(USER_URL) + "/resetAttempts",
                username,
                SimpleUserDTO.class
            );
            LOGGER.debug("Result status code was {}", user.getStatusCode());
            return user.getBody();
        }
        catch(HttpStatusCodeException e) {
            LOGGER.debug("Result status code was {}", e.getMessage());

            throw new DataAccessException("Failed to update user " + username + " with status code " + e.getStatusCode());
        }
        catch(RestClientException e) {
            LOGGER.debug("Result status code was {}", e.getMessage());

            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public SimpleUserDTO blockUser(String username) throws DataAccessException {
        try {
            ResponseEntity<SimpleUserDTO> user = restClient.postForEntity(
                restClient.getServiceURI(USER_URL) + "/block",
                username,
                SimpleUserDTO.class
            );
            LOGGER.debug("Result status code was {}", user.getStatusCode());
            return user.getBody();
        }
        catch(HttpStatusCodeException e) {
            LOGGER.debug("Result status code was {}", e.getMessage());

            throw new DataAccessException("Failed to block user " + username + " with status code " + e.getStatusCode());
        }
        catch(RestClientException e) {
            LOGGER.debug("Result status code was {}", e.getMessage());

            throw new DataAccessException(e.getMessage(), e);
        }
    }


    @Override
    public SimpleUserDTO unblockUser(String username) throws DataAccessException {
        try {
            ResponseEntity<SimpleUserDTO> user = restClient.postForEntity(
                restClient.getServiceURI(USER_URL) + "/unblock",
                username,
                SimpleUserDTO.class
            );
            LOGGER.debug("Result status code was {}", user.getStatusCode());
            return user.getBody();
        }
        catch(HttpStatusCodeException e) {
            LOGGER.debug("Result status code was {}", e.getMessage());

            throw new DataAccessException("Failed to unblock user " + username + " with status code " + e.getStatusCode());
        }
        catch(RestClientException e) {
            LOGGER.debug("Result status code was {}", e.getMessage());

            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public DetailedUserDTO findByName(String name ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving user by name from {}", restClient.getServiceURI(USER_URL));
            ResponseEntity<DetailedUserDTO> user =
                restClient.exchange(
                    restClient.getServiceURI(USER_URL+"/find/"+name),
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

    @Override
    public void removeFromUserNotSeen( Long userId, Long newsId ) throws DataAccessException {
        try {
            LOGGER.debug("removing news from users notSeen from {}", restClient.getServiceURI(USER_URL));
            ResponseEntity user =
                restClient.exchange(
                    restClient.getServiceURI(USER_URL+"/"+userId+"/"+newsId),
                    HttpMethod.POST,
                    null,
                    new ParameterizedTypeReference<DetailedUserDTO>() {}
                );
            LOGGER.debug("Result status was {} with content {}", user.getStatusCode(), user.getBody());
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed to update user with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }


}
