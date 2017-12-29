package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
     * * @return page of events
     */
    @Query(value = "Select * from event where end_of_event > now()",  nativeQuery = true)
    List<Event> findAllUpcoming();

    /**
     * find page of future events by title
     *
     * @param request for the page containing a page number and sort type
     * @param title of the event
     * @return page of events
     */
    @Query(value = "Select * from event e where e.end_of_event > now() and e.title = :title/*#pageable*/",  nativeQuery = true)
    Page<Event> findAllUpcomingByTitle(Pageable request, @Param("title") String title);

}
