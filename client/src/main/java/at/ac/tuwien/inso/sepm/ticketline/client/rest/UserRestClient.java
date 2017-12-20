package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;

import java.util.List;

public interface  UserRestClient {

    /**
     * get left login Attempts by username
     *
     * @param username of the user searched for
     * @return Integer login attempts
     */
    Integer getLoginAttemptsLeft(String username) throws DataAccessException;

    /**
     * block user by username
     *
     * @param username of user entry
     */
    void blockUser(String username) throws DataAccessException;

    /**
     * unblock user by username
     *
     * @param username of user entry
     */
     void unblockUser(String username) throws DataAccessException;


    /**
     * check if user is blocked
     *
     * @param username of user entry
     * @return boolean if user is blocked
     */
    boolean isBlocked(String username) throws DataAccessException;


    /**
     * Find User by name
     *
     * @param name of the user
     * @return DetailedUserDTO
     * @throws DataAccessException if something went wrong
     */
    DetailedUserDTO findByName(String name) throws DataAccessException;

    /**
     * removes news from notSeen
     *
     * @param userId of user to remove news
     * @param newsId of news to remove from user
     * @throws DataAccessException
     */
    void removeFromUserNotSeen(Long userId,Long newsId) throws DataAccessException;

    /**
     * Reset Password of User
     *
     * @param simpleUserDTO of userDTO entry whose password should be reset
     * @return SimpleUserDTO
     */
    SimpleUserDTO resetUserPassword(SimpleUserDTO simpleUserDTO);

    /**
     * add new User
     *
     * @param simpleUserDTO that should be created
     * @return SimpleUserDTO
     */
    SimpleUserDTO addNewUser(SimpleUserDTO simpleUserDTO);

    /**
     * Find all user entries.
     *
     * @return list of user entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleUserDTO> findAll() throws DataAccessException;

}