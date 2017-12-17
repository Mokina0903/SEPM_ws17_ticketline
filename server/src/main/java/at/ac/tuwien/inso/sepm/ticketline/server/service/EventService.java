package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;

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
}
