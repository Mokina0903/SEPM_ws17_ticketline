package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.rest.ErrorDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
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

    /**
     * publishes a new Event
     *
     * @param detailedEventDTO to be published
     * @return DetailedEventDTO of published Event
     * @throws DataAccessException in case something went wrong
     * @throws ErrorDTO in case something went wrong (http Status)
     */
    DetailedEventDTO publishEvent(DetailedEventDTO detailedEventDTO) throws DataAccessException, ErrorDTO;

    /**
     *
     * @param beginOfMonth the start of the month where to get the top events
     * @param endOfMonth the end of the month where to get the top events
     * @return a list of events
     */
    List<SimpleEventDTO> getTop10EventsOfMonth(LocalDateTime beginOfMonth, LocalDateTime endOfMonth) throws DataAccessException;
}
