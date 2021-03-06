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


    @Query(value = "SELECT * FROM event WHERE title = :title " +
        "AND description = :description AND hall_id = :hallId " +
        "AND start_of_event = :startOfEvent AND end_of_event = :endOfEvent", nativeQuery = true)
    List<Event> findDuplicates(@Param("title") String title, @Param("description") String description,
                               @Param("hallId") long hallId, @Param("startOfEvent") String startOfEvent,
                               @Param("endOfEvent") String endOfEvent);



    @Query(value = "SELECT TOP 10 e.id, e.title, e.description, e.event_Category, e.duration, e.price, e.start_Of_Event, e.start_Of_Event_Time, e.event_date, e.end_Of_Event, e.seat_Selection, e.category, e.hall_id" +
        " FROM event e join ticket t ON e.id = t.event_id " +
        " WHERE e.start_Of_Event >= :begin AND e.end_Of_Event <= :end " +
        " GROUP BY  e.id, e.title, e.description, e.category, e.price, e.event_Category, e.start_Of_Event, e.end_Of_Event, e.start_Of_Event_Time, e.event_date, e.duration, e.seat_Selection,  e.hall_id"+
        " ORDER BY count(*)", nativeQuery = true)
    List<Event> findTopTenEventsOfMonth(@Param("begin")Timestamp beginOfMonth, @Param("end") Timestamp endOfMonth);


    @Query(value = "SELECT TOP 10 e.id, e.title, e.description, e.event_Category, e.duration, e.price, e.start_Of_Event, e.start_Of_Event_Time, e.event_date, e.end_Of_Event, e.seat_Selection, e.category, e.hall_id" +
        " FROM event e join ticket t ON e.id = t.event_id" +
        " WHERE e.start_Of_Event >= :begin AND e.end_Of_Event <= :end AND e.category = :category" +
        " GROUP BY  e.id, e.title, e.description, e.price, e.start_Of_Event, e.event_Category, e.duration, e.end_Of_Event, e.seat_Selection, e.start_Of_Event_Time, e.event_date, e.category, e.hall_id"+
        " ORDER BY count(*)", nativeQuery = true)
    List<Event> findTopTenEventsOfMonthFilteredByCategory(@Param("begin")Timestamp beginOfMonth, @Param("end") Timestamp endOfMonth, @Param("category") String category);


    /**
     * filter events by artist
     *
     * @param artistId of artist
     * @param request of page
     * @return page with filtered events
     */
    @Query(value = "Select * from Event where id in (SELECT event_id FROM ARTISTS_OF_EVENT where artist_id=:artistId) " +
        "and end_of_event > now() /*#pageable*/"
    , nativeQuery = true)
    Page<Event> findAllByArtistId( @Param("artistId") Long artistId, Pageable request);

    /**
     * filter events by location
     *
     * @param locationId of location
     * @param request of page
     * @return page with filtered events
     */
    @Query(value = "SELECT e.* FROM EVENT e join hall h on e.hall_id = h.id where h.location_id = :locationId " +
        "and e.end_of_event > now() /*#pageable*/"
        , nativeQuery = true)
    Page<Event> findAllByLocationId( @Param("locationId") Long locationId, Pageable request);
}
