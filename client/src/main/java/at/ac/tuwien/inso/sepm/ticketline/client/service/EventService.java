package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
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

    /**
     * Find all upcoming events(that has not yet ended) ordered by Date ascending
     *
     * @param pageIndex page to load
     * @param eventsPerPage number of events per page
     * @return list of upcoming events
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleEventDTO> findAllUpcoming(int pageIndex, int eventsPerPage) throws DataAccessException,SearchNoMatchException;

    // TODO: (David) Javadoc
    List<DetailedEventDTO> publishEventCSV(String filePath) throws DataAccessException;
}
