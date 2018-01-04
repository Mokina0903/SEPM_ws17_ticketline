package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventService.class);

    private final EventRestClient eventRestClient;

    public SimpleEventService( EventRestClient eventRestClient ) {
        this.eventRestClient = eventRestClient;
    }

    @Override
    public List<SimpleEventDTO> findAll() throws DataAccessException {
        return eventRestClient.findAll();
    }

    @Override
    public DetailedEventDTO findById( Long id ) throws DataAccessException {
        return eventRestClient.findById(id);
    }

    @Override
    public List<SimpleEventDTO> findAllUpcoming( int pageIndex, int eventsPerPage ) throws DataAccessException,SearchNoMatchException {

        return eventRestClient.findAllUpcoming(pageIndex,eventsPerPage);
    }

    @Override
    public DetailedEventDTO publishEvent(DetailedEventDTO detailedEventDTO) throws DataAccessException {
        return eventRestClient.publishEvent(detailedEventDTO);
    }
}
