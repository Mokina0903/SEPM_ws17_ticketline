package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {


    /**
     * Find event entries by search parameters.
     *
     * @param search list of search parameters
     * @param request Pagable request
     * @return Page containing the event entries
     */
    //Page<Event> find(String search, Pageable request);

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
     * find page of events by combined parameters
     *
     * @param parameters for filtering
     * @param request for the page containing a page number and sort type
     * @return page of events
     */
    Page<Event> findByAdvancedSearch(HashMap<String, String> parameters, Pageable request);

    /**
     * filter events by artist
     *
     * @param artistId of artist
     * @param request of page
     * @return page with filtered events
     */
    Page<Event> findAllByArtistId(Long artistId, Pageable request);

    /**
     * filter events by location
     *
     * @param locationId of location
     * @param request of page
     * @return page with filtered events
     */
    Page<Event> findAllByLocationId(Long locationId, Pageable request);


    /**
     * find page of events by parameters
     *
     * @param parameters for filtering
     * @param request for the page containing a page number and sort type
     * @return page of events
     */
    Page<Event> find(HashMap<String, String> parameters, Pageable request);

    /**
     * Publish new Event
     *
     * @param event to publish
     * @return published Event entry
     */
    Event publishEvent(Event event);

    /**
     *
     * @param beginOfMonth begin of the month of which to get the top ten events
     * @param endOfMonth end of the month of which to get the top ten events
     * @return a list of events
     */
     List<Event> getTop10EventsOfMonth(LocalDateTime beginOfMonth,LocalDateTime endOfMonth);


    /**
     *
     * @param beginOfMonth begin of the month of which to get the top ten events
     * @param endOfMonth end of the month of which to get the top ten events
     * @param category to be filtered
     * @return a list of events filtered by the given category
     */
    List<Event> getTop10EventsOfMonthFilteredByCategory(LocalDateTime beginOfMonth,LocalDateTime endOfMonth, String category);

}