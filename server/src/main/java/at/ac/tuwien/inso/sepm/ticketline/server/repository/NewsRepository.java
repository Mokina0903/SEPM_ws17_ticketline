package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Find a single news entry by id.
     *
     * @param id the is of the news entry
     * @return Optional containing the news entry
     */
    Optional<News> findOneById(Long id);

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of al news entries
     */
    List<News> findAllByOrderByPublishedAtDesc();


    /**
     * Find all news that are new to user with given id
     *
     * @param userId of the user
     * @return list of new news
     */
    @Query(value = "select * from " +
        "(news n join not_seen s on n.id=s.news_id )" +
        "where s.users_id = :userId " +
        "order by n.published_at desc",nativeQuery = true)
    List<News> findNotSeenByUser(@Param("userId")Long userId );

    /**
     * Find list of old news for user with given id
     *
     * @param userId of the user
     * @return list of old news
     */
    @Query(value = "select * from news n " +
        "where n.id not in " +
        "(select x.id from news x join not_seen s on x.id=s.news_id where s.users_id= :userId) " +
        "order by n.published_at desc",nativeQuery = true)
    List<News> findOldNewsByUser(@Param("userId")Long userId );
}
