package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.QEvent;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.MultiValueBinding;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>,
    QueryDslPredicateExecutor<Event>, QuerydslBinderCustomizer<QEvent> {

    //siehe: http://www.baeldung.com/rest-api-search-language-spring-data-querydsl

    @Override
    default public void customize(
        QuerydslBindings bindings, QEvent root) {
        bindings.bind(String.class)
            .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }



    /**
     * Find a single event entry by id.
     *
     * @param id of the event entry
     * @return Optional containing the event entry
     */
    Optional<Event> findOneById( Long id);

    /**
     * Find all event entries.
     *
     * @return list of all event entries
     */
    List<Event> findAllByOrderByStartOfEventDesc();


    /**
     * find page of future events
     *
     * @return list of events
     */
    @Query(value = "Select * from event where end_of_event > now()",  nativeQuery = true)
    List<Event> findAllUpcoming();

    /**
     * find list of events by title
     *
     * @param title of the event
     * @return list of events
     */
    @Query
    List<Event> findAllByTitle(@Param("title") String title);

    /**
     * find list of future events by title
     *
     * @param title of the event
     * @return list of events
     */
    @Query(value = "Select * from event e where e.end_of_event > now() and e.title = :title",  nativeQuery = true)
    List<Event> findAllUpcomingByTitle(@Param("title") String title);


    /**
     * find list of future events by title
     *
     * @param title of the event
     * @return list of events
     */
    @Query(value = "Select * from event e where e.end_of_event > now() and e.title = :title and e.end_of_event =:date",  nativeQuery = true)
    List<Event> findAllUpcomingByTitleAndDate(@Param("title") String title, @Param("date") LocalDate date);

    //todo find by type, implement type in event (enum)
    //todo find by duration (+-30 min)
    //todo find by content
    //todo find by date
    //todo find by time
    //todo find by w/o seat reservation
}
