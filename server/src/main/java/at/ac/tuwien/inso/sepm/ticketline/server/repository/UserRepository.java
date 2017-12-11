package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a single user entry by Username
     *
     * @param userName of the user entry
     * @return Optional containing the user entry
     */
    Optional<User> findByUserName(String userName);

    User findOneByUserName(String userName);

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


    /**
     * upadte the user
     *
     * @param notSeen list of news to update,
     *        id of user to update
     * @return updated user
     */

    @Modifying
    @Query(value = "Delete from Not_Seen n WHERE n.users_id = :userId and n.news_id= :newsId",nativeQuery = true)
    void updateNotSeen( @Param("userId") Long userIs, @Param("newsId") Long newsId);

}
