package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

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
     * @param request page to load
     * @return list of upcoming events
     * @throws DataAccessException in case something went wrong
     */
    Page<SimpleEventDTO> findAllUpcoming(Pageable request) throws DataAccessException,SearchNoMatchException;

    // TODO: (David) Javadoc
    DetailedEventDTO publishEvent(DetailedEventDTO detailedEventDTO) throws DataAccessException;

    /**
     * Find all events by search parameters ordered by Date ascending
     *
     * @param request page to load
     * @param parameters for filter and search
     * @return Page of upcoming events
     * @throws DataAccessException in case something went wrong
     */
    Page<SimpleEventDTO> findAdvanced(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException;

    }
