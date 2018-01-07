package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
    public TicketDTO save( TicketDTO ticketDTO ) throws DataAccessException {
        return ticketRestClient.save(ticketDTO);
    }
}
