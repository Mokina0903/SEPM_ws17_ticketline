package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;

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

    /**
     * decrease login attempts by name by 1
     *
     * @param userName of userAttemps entry
     * @return UserDTO
     * @throws DataAccessException in case something went wrong
     */
    SimpleUserDTO decreaseLoginAttempts(String userName) throws DataAccessException;

    /**
     * reset login attempts by name
     *
     * @param userName of userAttemps entry
     * @return UserDTO
     * @throws DataAccessException in case something went wrong
     */
    SimpleUserDTO resetLoginAttempts(String userName) throws DataAccessException;


    /**
     * block user by name
     *
     * @param userName of userAttemps entry
     * @return UserDTO
     * @throws DataAccessException in case something went wrong
     */
    SimpleUserDTO blockUser(String userName) throws DataAccessException;

    /**
     * unblock user by name
     *
     * @param userName of userAttemps entry
     * @return UserDTO
     * @throws DataAccessException in case something went wrong
     */
    SimpleUserDTO unblockUser(String userName) throws DataAccessException;

    /**
     * find user by username
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

}