package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.ErrorDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.List;

public interface EventRestClient {
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
    DetailedEventDTO findById(Long id) throws DataAccessException;

    /**
     * Find all upcoming events(that has not yet ended) ordered by Date ascending
     *
     * @param request page to load
     * @return list of upcoming events
     * @throws DataAccessException in case something went wrong
     */
    Page<SimpleEventDTO> findAllUpcoming(Pageable request) throws DataAccessException;

    /**
     * Find all events with given artist ordered by Date ascending
     *
     * @param request  page to load
     * @param artistId
     * @return list of events
     * @throws DataAccessException in case something went wrong
     */
    Page<SimpleEventDTO> findAllByArtistId(Long artistId, Pageable request) throws DataAccessException;

    /**
     * Find all events with given location ordered by Date ascending
     *
     * @param request  page to load
     * @param id of Location
     * @return list of events
     * @throws DataAccessException in case something went wrong
     */
    Page<SimpleEventDTO> findAllByLocationId(Long id, Pageable request) throws DataAccessException;

    /**
     * Find all events by combined search parameters ordered by Date ascending
     *
     * @param request page to load
     * @return page of events
     * @throws DataAccessException in case something went wrong
     */
    Page<SimpleEventDTO> findAdvanced(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException;

    /**
     * Find all events by search parameters ordered by Date ascending
     *
     * @param request page to load
     * @return page of events
     * @throws DataAccessException in case something went wrong
     */
    Page<SimpleEventDTO> find(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException;

    /**
     * publishes a new Event
     *
     * @param detailedEventDTO to be published
     * @return DetailedEventDTO of published Event
     * @throws DataAccessException in case something went wrong
     * @throws ErrorDTO            in case something went wrong (http Status)
     */
    DetailedEventDTO publishEvent(DetailedEventDTO detailedEventDTO) throws DataAccessException, ErrorDTO;

    /**
     * @param beginOfMonth the start of the month where to get the top events
     * @param endOfMonth   the end of the month where to get the top events
     * @return a list of events, without category search
     */
    List<SimpleEventDTO> getTop10EventsOfMonth(LocalDate beginOfMonth, LocalDate endOfMonth) throws DataAccessException;

    /**
     * @param beginOfMonth the start of the month where to get the top events
     * @param endOfMonth   the end of the month where to get the top events
     * @return a list of events, with category search
     */
    List<SimpleEventDTO> getTop10EventsOfMonthFilteredbyCategory(LocalDate beginOfMonth, LocalDate endOfMonth, String category) throws DataAccessException;


}
