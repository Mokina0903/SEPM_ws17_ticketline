package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.EmptyValueException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.TicketAlreadyExistsException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleTicketService implements TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTicketService.class);

    private final TicketRestClient ticketRestClient;

    public SimpleTicketService( TicketRestClient ticketRestClient ) {
        this.ticketRestClient = ticketRestClient;
    }

    @Override
    public TicketDTO findOneById( Long id ) throws DataAccessException {
        return ticketRestClient.findOneById(id);
    }

    @Override
    public List<TicketDTO> findByEventId( Long eventId ) throws DataAccessException {
        return ticketRestClient.findByEventId(eventId);
    }

    @Override
    public List<TicketDTO> findByCustomerId( Long customerId ) throws DataAccessException {
        return ticketRestClient.findByCustomerId(customerId);
    }

    @Override
    public Boolean isBooked( Long eventId, Long seatId ) throws DataAccessException {
        return ticketRestClient.isBooked(eventId,seatId);
    }

    @Override
    public List<TicketDTO> save( List<TicketDTO> ticketDTOS ) throws DataAccessException, TicketAlreadyExistsException, EmptyValueException {
        return ticketRestClient.save(ticketDTOS);
    }

    @Override
    public int ticketCountForEventForSector( Long event_id, char sector ) throws DataAccessException {
        return ticketRestClient.ticketCountForEventForSector(event_id,sector);
    }
}
