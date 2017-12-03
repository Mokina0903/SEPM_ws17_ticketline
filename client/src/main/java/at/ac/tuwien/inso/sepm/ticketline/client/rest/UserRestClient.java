package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;

public interface UserRestClient {

/**
 * Find user by username
 *
 * @param SimpleUserDTO of the user searched for
 * @return Integer attempts
 * @throws DataAccessException in case something went wrong
 */
    SimpleUserDTO findByUsername(String userName) throws DataAccessException;


    /**
     * get left login Attempts by username
     *
     * @param attempts of the user searched for
     * @return Integer attempts
     */
    Integer getLoginAttemptsLeft(String username) throws DataAccessException;

}
