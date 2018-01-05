package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.AlreadyExistsException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;

    public SimpleTicketService( TicketRepository ticketRepository ) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket findOneById( Long id ) {
        return ticketRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Page<Ticket> findAll( Pageable request ) {
        return ticketRepository.findAll(request);
    }

    @Override
    public List<Ticket> findByEventId( Long eventId ) {
        return ticketRepository.findByEvent_Id(eventId);
    }

    @Override
    public List<Ticket> findByCustomerId( Long customerId ) {
        return ticketRepository.findByCustomer_Id(customerId);
    }

    @Override
    public Ticket save( Ticket ticket ) {
        if(isBooked(ticket.getEvent().getId(),ticket.getSeat().getId())){
            throw new AlreadyExistsException("Ticket already sold.");
        }
        return ticketRepository.save(ticket);
    }

    @Override
    public Boolean isBooked(Long eventId,Long seatId){
        return ticketRepository.findByEvent_idAndSeat_id(eventId,seatId).isPresent();
    }
}
