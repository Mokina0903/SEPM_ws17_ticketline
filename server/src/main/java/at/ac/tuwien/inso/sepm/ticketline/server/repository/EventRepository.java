package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.QEvent;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>,
    QueryDslPredicateExecutor<Event>, QuerydslBinderCustomizer<QEvent> {

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
    Optional<Event> findOneById(Long id);

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
    @Query(value = "Select * from event where end_of_event > now()", nativeQuery = true)
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
    @Query(value = "Select * from event e where e.end_of_event > now() and e.title = :title", nativeQuery = true)
    List<Event> findAllUpcomingByTitle(@Param("title") String title);

    @Query(value = "SELECT * FROM event WHERE title = :title " +
        "AND description = :description AND hall_id = :hallId " +
        "AND start_of_event = :startOfEvent AND end_of_event = :endOfEvent", nativeQuery = true)
    List<Event> findDuplicates(@Param("title") String title, @Param("description") String description,
                               @Param("hallId") long hallId, @Param("startOfEvent") String startOfEvent,
                               @Param("endOfEvent") String endOfEvent);



    @Query(value = "SELECT TOP 10 e.id, e.title, e.description, e.duration, e.price, e.start_Of_Event, e.start_Of_Event_Time, e.event_date, e.end_Of_Event, e.seat_Selection, e.event_Category, e.hall_id" +
        " FROM event e join ticket t ON e.id = t.event_id " +
        " WHERE e.start_Of_Event >= :begin AND e.end_Of_Event <= :end " +
        " GROUP BY  e.id, e.title, e.description, e.event_Category, e.price, e.start_Of_Event, e.end_Of_Event, e.start_Of_Event_Time, e.event_date, e.duration, e.seat_Selection,  e.hall_id"+
        " ORDER BY count(*)", nativeQuery = true)
    List<Event> findTopTenEventsOfMonth(@Param("begin")Timestamp beginOfMonth, @Param("end") Timestamp endOfMonth);


    @Query(value = "SELECT TOP 10 e.id, e.title, e.description, e.duration, e.price, e.start_Of_Event, e.start_Of_Event_Time, e.event_date, e.end_Of_Event, e.seat_Selection, e.event_Category, e.hall_id" +
        " FROM event e join ticket t ON e.id = t.event_id" +
        " WHERE e.start_Of_Event >= :begin AND e.end_Of_Event <= :end AND e.event_Category = :category" +
        " GROUP BY  e.id, e.title, e.description, e.price, e.start_Of_Event, e.duration, e.end_Of_Event, e.seat_Selection, e.start_Of_Event_Time, e.event_date, e.event_Category, e.hall_id"+
        " ORDER BY count(*)", nativeQuery = true)
    List<Event> findTopTenEventsOfMonthFilteredByCategory(@Param("begin")Timestamp beginOfMonth, @Param("end") Timestamp endOfMonth, @Param("category") String category);


    /**
     * find list of future events by title
     *
     * @param title of the event
     * @return list of events
     */
    @Query(value = "Select * from event e where e.end_of_event > now() and e.title = :title and e.end_of_event =:date",  nativeQuery = true)
    List<Event> findAllUpcomingByTitleAndDate(@Param("title") String title, @Param("date") LocalDate date);

    /**
     * filter events by artist
     *
     * @param artistId of artist
     * @param request of page
     * @return page with filtered events
     */
    @Query(value = "Select * from Event where id in (SELECT event_id FROM ARTISTS_OF_EVENT where artist_id=:artistId) " +
        "order by start_of_event asc #pageable"
    , nativeQuery = true)
    Page<Event> findAllByArtistId( @Param("artistId") Long artistId, Pageable request);

    //todo find by type, implement type in event (enum)
    //todo find by duration (+-30 min)
    //todo find by content
    //todo find by date
    //todo find by time
    //todo find by w/o seat reservation
}
