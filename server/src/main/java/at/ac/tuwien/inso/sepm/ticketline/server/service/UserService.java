package at.ac.tuwien.inso.sepm.ticketline.server.service;

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

    // TODO: oder einfach nur update?
    /**
     * Block a single user entry
     *
     * @param user to block
     * @return blocked user entry
     */
    boolean blockUser(User user);

    //boolean changeRole(User user, Integer role);
    //boolean updatePassword(User user, String password);


}
