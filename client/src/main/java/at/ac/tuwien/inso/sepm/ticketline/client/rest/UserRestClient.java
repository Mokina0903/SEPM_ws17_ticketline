package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;

import java.util.List;

public interface  UserRestClient {
/**
 * Find user by username
 *
 * @param userName of the user searched for
 * @return SimpleUserDTO
 * @throws DataAccessException in case something went wrong
 */
    SimpleUserDTO findByUsername(String userName) throws DataAccessException;


    /**
     * get left login Attempts by username
     *
     * @param username of the user searched for
     * @return Integer attempts
     */
    Integer getLoginAttemptsLeft(String username) throws DataAccessException;

    /**
     * decrease login Attempts by username by 1
     *
     * @param username of the user searched for
     * @return Integer attempts
     */
 //   SimpleUserDTO decreaseLoginAttempts(String username) throws DataAccessException;

    /**
     * reset login Attempts by username
     *
     * @param username
     * @return Integer attempts
     */
 //   SimpleUserDTO resetLoginAttempts(String username) throws DataAccessException;

    /**
     * block user by username
     *
     * @param username
     * @return Integer attempts
     */
    void blockUser(String username) throws DataAccessException;

    /**
     * unblock user by username
     *
     * @param username
     * @return Integer attempts
     */
     void unblockUser(String username) throws DataAccessException;


    /**
     * check if user is blocked
     *
     * @param username
     * @return boolean isBlocked
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
     * @param detailedUserDTO
     * @return SimpleUserDTO
     */
    SimpleUserDTO resetUserPassword(DetailedUserDTO detailedUserDTO);

    /**
     * add new User
     *
     * @param detailedUserDTO
     * @return SimpleUserDTO
     */
    SimpleUserDTO addNewUser(DetailedUserDTO detailedUserDTO);

    /**
     * Find all user entries.
     *
     * @return list of news entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleUserDTO> findAll() throws DataAccessException;

}