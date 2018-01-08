package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;

import java.util.List;

public interface UserService {


    /**
     * get left login attempts by name
     *
     * @param userName of user entry
     * @return userAttempts
     * @throws DataAccessException in case something went wrong
     */
    Integer getLoginAttemptsLeft(String userName) throws DataAccessException;

    /**
     * block user by name
     *
     * @param userName of user entry
     * @throws DataAccessException in case something went wrong
     */
    void blockUser(String userName) throws DataAccessException;

    /**
     * unblock user by name
     *
     * @param userName of user entry
     * @throws DataAccessException in case something went wrong
     */
    void unblockUser(String userName) throws DataAccessException;

    /**
     * reset password of username
     *
     * @param simpleUserDTO of userDTO entry whose password should be reset
     * @return UserDTO
     * @throws DataAccessException in case something went wrong
     */
    SimpleUserDTO resetUserPassword(SimpleUserDTO simpleUserDTO) throws DataAccessException;

    /**
     * add new User
     *
     * @param simpleUserDTO of user entry
     * @return UserDTO
     * @throws DataAccessException in case something went wrong
     */
    SimpleUserDTO addNewUser(SimpleUserDTO simpleUserDTO) throws DataAccessException;

    /**
     * check if user is blocked
     *
     * @param userName of user entry
     * @return boolean if user is blocked
     * @throws DataAccessException in case something went wrong
     */
    boolean isBlocked(String userName) throws DataAccessException;


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

    /**
     * Find all user entries.
     *
     * @return list of news entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleUserDTO> findAll() throws DataAccessException;


    /**
     *  Validates if new password and confirmation is Ok
     *
     * @param password to set
     * @param confirmPassword confirmation of password to set
     * @return boolean if confirmation of new password is Ok
     */
    boolean confirmPasswordIsOk(String password, String confirmPassword);

}