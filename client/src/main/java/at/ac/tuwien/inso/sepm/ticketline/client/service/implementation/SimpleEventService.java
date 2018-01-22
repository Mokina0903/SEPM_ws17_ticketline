package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.rest.ErrorDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.List;

@Service
public class SimpleEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventService.class);

    private final EventRestClient eventRestClient;

    public SimpleEventService(EventRestClient eventRestClient) {
        this.eventRestClient = eventRestClient;
    }

    @Override
    public List<SimpleEventDTO> findAll() throws DataAccessException {
        return eventRestClient.findAll();
    }

    @Override
    public DetailedEventDTO findById(Long id) throws DataAccessException {
        return eventRestClient.findById(id);
    }

    @Override
    public Page<SimpleEventDTO> findAllUpcoming(Pageable request) throws DataAccessException, SearchNoMatchException {

        return eventRestClient.findAllUpcoming(request);
    }

    @Override
    public Page<SimpleEventDTO> findAllByArtistId(Long artistId, Pageable request) throws DataAccessException {
        return eventRestClient.findAllByArtistId(artistId, request);
    }

    @Override
    public DetailedEventDTO publishEvent(DetailedEventDTO detailedEventDTO) throws DataAccessException, ErrorDTO {
        return eventRestClient.publishEvent(detailedEventDTO);
    }

    @Override
    public List<SimpleEventDTO> getTop10EventsOfMonth(LocalDate beginOfMonth, LocalDate endOfMonth) throws DataAccessException {
        return eventRestClient.getTop10EventsOfMonth(beginOfMonth, endOfMonth);
    }

    @Override
    public List<SimpleEventDTO> getTop10EventsOfMonthFilteredbyCategory(LocalDate beginOfMonth, LocalDate endOfMonth, String category) throws DataAccessException {
        return eventRestClient.getTop10EventsOfMonthFilteredbyCategory(beginOfMonth, endOfMonth, category);
    }

    @Override
    public boolean checkDates(LocalDate beginDate, LocalDate endDate) {
        if (beginDate == null || endDate == null) {
            return false;
        }
        if (beginDate.isAfter(endDate)) {
            return false;
        }

        return true;
    }

    @Override
    public Page<SimpleEventDTO> findAdvanced(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException {
        return eventRestClient.findAdvanced(request, parameters);
    }

    @Override
    public Page<SimpleEventDTO> find(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException {
        return eventRestClient.find(request, parameters);
    }

    @Override
    public Page<SimpleEventDTO> findByArtist(Pageable request, Long id) throws DataAccessException {
        return eventRestClient.findAllByArtistId(id, request);
    }
}
