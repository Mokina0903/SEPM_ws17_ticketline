package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;

import java.util.List;

public interface EventService {

    /**
     * Find all event entries.
     *
     * @return list of event entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleEventDTO> findAll() throws DataAccessException;

    /**
     * Find specific event by id
     *
     * @param id of the event searched for
     * @return Detailed event
     * @throws DataAccessException in case something went wrong
     */
    DetailedEventDTO findById( Long id) throws DataAccessException;
}
