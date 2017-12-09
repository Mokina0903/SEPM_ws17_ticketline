package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class SimpleUserService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsService.class);

    private final UserRestClient userRestClient;

    public SimpleUserService(UserRestClient userRestClient) {
        this.userRestClient = userRestClient;
    }

    @Override
    public SimpleUserDTO findByUsername(String userName) throws DataAccessException {
        return userRestClient.findByUsername(userName);
    }

    @Override
    public Integer getLoginAttemptsLeft(String userName) throws DataAccessException {
        return userRestClient.getLoginAttemptsLeft(userName);
    }

    @Override
    public SimpleUserDTO decreaseLoginAttempts(String userName) throws DataAccessException {
        return userRestClient.decreaseLoginAttempts(userName);
    }

    @Override
    public SimpleUserDTO resetLoginAttempts(String userName) throws DataAccessException {
        return userRestClient.resetLoginAttempts(userName);
    }

    @Override
    public SimpleUserDTO blockUser(String userName) throws DataAccessException {
        return userRestClient.blockUser(userName);
    }

    @Override
    public SimpleUserDTO unblockUser(String userName) throws DataAccessException {
        return userRestClient.unblockUser(userName);
    }

    @Override
    public DetailedUserDTO findByName( String name ) throws DataAccessException {
        return userRestClient.findByName(name);
    }

    @Override
    public void removeFromUserNotSeen( Long userId, Long newsId ) throws DataAccessException {
        userRestClient.removeFromUserNotSeen(userId,newsId);
    }
}