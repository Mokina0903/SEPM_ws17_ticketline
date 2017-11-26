package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a single user entry by Username
     *
     * @param userName the is of the user entry
     * @return Optional containing the user entry
     */
    User findByUserName(String userName);

    /**
     * Find a single user entry by id.
     *
     * @param id the is of the user entry
     * @return Optional containing the user entry
     */
    Optional<User> findOneById(Long id);

    /**
     * Find all user entries ordered by user name.
     *
     * @return ordered list of al user entries
     */
    List<User> findAll();

}
