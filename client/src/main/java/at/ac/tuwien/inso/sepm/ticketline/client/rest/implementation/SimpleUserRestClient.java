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

import java.util.List;

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
                    restClient.getServiceURI(USER_URL + "/find/" + userName),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<SimpleUserDTO>() {
                    }
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

/*    @Override
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
    }*/

  /*  @Override
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
    }*/

   @Override
    public void blockUser(String username) throws DataAccessException {
       try {
            ResponseEntity<SimpleUserDTO> user = restClient.postForEntity(
                restClient.getServiceURI(USER_URL) + "/block",
                username,
                SimpleUserDTO.class
            );
            LOGGER.debug("Result status code was {}", user.getStatusCode());
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
    public void unblockUser(String username) throws DataAccessException {
       try {
            ResponseEntity<SimpleUserDTO> user = restClient.postForEntity(
                restClient.getServiceURI(USER_URL) + "/unblock",
                username,
                SimpleUserDTO.class
            );
            LOGGER.debug("Result status code was {}", user.getStatusCode());
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
    public SimpleUserDTO resetUserPassword(DetailedUserDTO detailedUserDTO) {
       // TODO: Implement
       return null;
    }

    @Override
    public SimpleUserDTO addNewUser(DetailedUserDTO detailedUserDTO) {
       // TODO: Implement
       return null;
    }

    @Override
    public boolean isBlocked(String username) throws DataAccessException {

        try {
            ResponseEntity<Boolean> blocked = restClient.getForEntity(
                restClient.getServiceURI(USER_URL) + "/{username}/isBlocked",
                Boolean.class,
                username
            );

            LOGGER.debug("Result status code was {}", blocked.getStatusCode());
            return blocked.getBody();
        }
        catch(HttpStatusCodeException e) {
            throw new DataAccessException("Failed to retrieve blocked status of user " + username, e);
        }
        catch(RestClientException e) {
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

    @Override
    public List<SimpleUserDTO> findAll() throws DataAccessException {
       try {
            LOGGER.debug("Retrieving all users from {}", restClient.getServiceURI(USER_URL));
            ResponseEntity<List<SimpleUserDTO>> users =
                restClient.exchange(
                    restClient.getServiceURI(USER_URL),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SimpleUserDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", users.getStatusCode(), users.getBody());
            return users.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
