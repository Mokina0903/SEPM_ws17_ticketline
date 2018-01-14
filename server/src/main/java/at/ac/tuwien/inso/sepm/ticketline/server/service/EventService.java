package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface EventService {


    /**
     * Find event entries by search parameters.
     *
     * @param search list of search parameters
     * @param request Pagable request
     * @return Page containing the event entries
     */
    Page<Event> findByAdvancedSearch(String search, Pageable request);

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
    Page<Event> findAllUpcomingAsc(Pageable request);

    /**
     * find page of future events by title
     *
     * @param request for the page containing a page number and sort type
     * @param title   of the event
     * @return page of events
     */
    Page<Event> findAllUpcomingByTitle(Pageable request, String title);

    /**
     * Publish new Event
     *
     * @param event to publish
     * @return published Event entry
     */
    Event publishEvent(Event event);

}