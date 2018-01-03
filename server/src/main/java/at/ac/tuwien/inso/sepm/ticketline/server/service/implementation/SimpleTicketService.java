package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
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
    public List<Ticket> findByEvent( Event event ) {
        return ticketRepository.findByEvent(event);
    }

    @Override
    public List<Ticket> findByCustomer( Customer customer ) {
        return ticketRepository.findByCustomer(customer);
    }
}
