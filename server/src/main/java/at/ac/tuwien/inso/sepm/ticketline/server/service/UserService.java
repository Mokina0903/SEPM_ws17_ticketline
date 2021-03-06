package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import java.util.List;

public interface UserService {
    /**
     * Find all user entries ordered by user name.
     *
     * @return ordered list of all user entries
     */
    List<User> findAll();


    /**
     * Find a single user entry by id.
     *
     * @param id the is of the user entry
     * @return the user entry
     */
    User findOne(Long id);

    /**
     * Find a single user entry by name.
     *
     * @param name of the user entry
     * @return the user entry
     */
    User findOneByName(String name);

    /**
     * Find a single user entry by name.
     *
     * @param userName of the user entry
     * @return the user entry
     */
    User findByUsername(String userName);


    /**
     * Create a single user entry
     *
     * @param user to create
     * @return created user entry
     */
    User createUser(User user);

    /**
     * save/update user
     *
     * @param user to save/update
     * @return saved/updated user
     */
    void save(User user);

    /**
     * update not seen News of User
     *
     * @param userId id of user
     * @param newsId id of news
     */
    void updateNotSeen( Long userId, Long newsId );

    /**
     * Block a single user entry
     *
     * @param user to block
     * @return blocked user entry
     */
    boolean blockUser(User user);

    /**
     * Unblock a single user entry
     *
     * @param user to block
     * @return blocked user entry
     */
    boolean unblockUser(User user);

    /**
     * Reset a specific users password
     *
     * @param user to block
     * @return blocked user entry
     */
    User resetPassword(User user);



    //boolean changeRole(User user, Integer role);
    //boolean updatePassword(User user, String password);


}
