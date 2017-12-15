package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository {

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
    List<Event> findAllOrderByStartOfEventDesc();
}
