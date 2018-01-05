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

import java.util.List;


@Service
public class SimpleUserService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsService.class);

    private final UserRestClient userRestClient;

    public SimpleUserService(UserRestClient userRestClient) {
        this.userRestClient = userRestClient;
    }


    @Override
    public Integer getLoginAttemptsLeft(String userName) throws DataAccessException {
        return userRestClient.getLoginAttemptsLeft(userName);
    }


    @Override
    public SimpleUserDTO resetUserPassword(SimpleUserDTO simpleUserDTO) throws DataAccessException {
        return userRestClient.resetUserPassword(simpleUserDTO);
    }

    @Override
    public SimpleUserDTO addNewUser(SimpleUserDTO simpleUserDTO) throws DataAccessException {
        return userRestClient.addNewUser(simpleUserDTO);
    }

    @Override
    public void blockUser(String userName) throws DataAccessException {
        userRestClient.blockUser(userName);
    }

    @Override
    public void unblockUser(String userName) throws DataAccessException {
        userRestClient.unblockUser(userName);
    }

    @Override
    public boolean isBlocked(String userName) throws DataAccessException {
        return userRestClient.isBlocked(userName);
    }

    @Override
    public DetailedUserDTO findByName( String name ) throws DataAccessException {
        return userRestClient.findByName(name);
    }

    @Override
    public void removeFromUserNotSeen( Long userId, Long newsId ) throws DataAccessException {
        userRestClient.removeFromUserNotSeen(userId,newsId);
    }

    @Override
    public List<SimpleUserDTO> findAll() throws DataAccessException {
        return userRestClient.findAll();
    }

    @Override
    public boolean confirmPasswordIsOk(String password, String confirmPassword) {
        if (password.trim().isEmpty() || !password.equals(confirmPassword)) {
            return false;
        }
        return true;
    }
}