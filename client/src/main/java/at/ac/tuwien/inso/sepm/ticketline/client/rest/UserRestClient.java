package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;

public interface UserRestClient {

    /**
     * Find User by name
     *
     * @param name of the user
     * @return DetailedUserDTO
     * @throws DataAccessException if something went wrong
     */
    DetailedUserDTO findByName(String name) throws DataAccessException;
}
