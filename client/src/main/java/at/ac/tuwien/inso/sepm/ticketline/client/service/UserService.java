package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;

public interface UserService {

    /**
     * Find specific user entry by name
     *
     * @param userName of userAttemps entry
     * @return SimpleUserDTO
     * @throws DataAccessException in case something went wrong
     */
    SimpleUserDTO findByUsername(String userName) throws DataAccessException;

    /**
     * get left login attempts by name
     *
     * @param userName of userAttemps entry
     * @return userAttempts
     * @throws DataAccessException in case something went wrong
     */
    Integer getLoginAttemptsLeft(String userName) throws DataAccessException;

}

