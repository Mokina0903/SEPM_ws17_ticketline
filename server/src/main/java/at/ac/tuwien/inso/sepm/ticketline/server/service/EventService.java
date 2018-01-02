package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface EventService {
    /**
     * Find all event entries
     *
     * @return list of all event entries
     */
    List<Event> findAll();


    /**
     * Find a single event entry by id.
     *
     * @param id of the event entry
     * @return the event entry
     */
    Event findOne(Long id);


    /**
     * Find all upcoming events(that has not yet ended) ordered by Date ascending
     *
     * @param request pageRequest, which page and how many entries
     * @return List of events
     */
    List<Event> findAllUpcomingAsc( Pageable request);

    /**
     * Publish new Event
     *
     * @param event to publish
     * @return published Event entry
     */
    Event publishEvent(Event event);
}
