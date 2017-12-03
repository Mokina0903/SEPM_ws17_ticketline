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
    public DetailedUserDTO findByName( String name ) throws DataAccessException {
        return userRestClient.findByName(name);
    }
}