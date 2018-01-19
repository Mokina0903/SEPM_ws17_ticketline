package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

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



    @Query(value = "SELECT TOP 10 e.id, e.title, e.description, e.price, e.start_Of_Event, e.end_Of_Event, e.seat_Selection, e.event_Category, e.hall_id" +
        " FROM event e join ticket t ON e.id = t.event_id " +
        "WHERE e.start_Of_Event >= :begin AND e.start_Of_Event <= :end AND e.end_Of_Event <= :end " +
        "GROUP BY  e.id, e.title, e.description, e.price, e.start_Of_Event, e.end_Of_Event, e.seat_Selection, e.event_Category, e.hall_id"+
        " ORDER BY count(*)", nativeQuery = true)
    List<Event> findTopTenEventsOfMonth(@Param("begin")Timestamp beginOfMonth, @Param("end") Timestamp endOfMonth);

    //todo find by type, implement type in event (enum)
    //todo find by duration (+-30 min)
    //todo find by content
    //todo find by date
    //todo find by time
    //todo find by w/o seat reservation
}
