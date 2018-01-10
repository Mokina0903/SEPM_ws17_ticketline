package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.AlreadyExistsException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.EmptyFieldException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<Ticket> save( List<Ticket> tickets ) {
        if(tickets==null || tickets.isEmpty()){throw new EmptyFieldException();}

        long reservationNR = (LocalDate.now().getYear() % 100) * 100000000 ;

        for(Ticket ticket : tickets) {
            if (isBooked(ticket.getEvent().getId(), ticket.getSeat().getId())) {
                throw new AlreadyExistsException("Ticket already sold.");
            }
            ticket.setDeleted(false);
            ticket.setReservationNumber(reservationNR);
        }
        tickets = ticketRepository.save(tickets);
        reservationNR+=+ tickets.get(0).getId();
        for(Ticket ticket: tickets){
            ticket.setReservationNumber(reservationNR);
        }
        return ticketRepository.save(tickets);
    }

    @Override
    public int ticketCountForEventForSector( Long event_id, char sector ) {
        return ticketRepository.ticketCountForEventForSector(event_id,sector);
    }

    @Override
    public Boolean isBooked(Long eventId,Long seatId){
        return ticketRepository.findByEvent_idAndSeat_id(eventId,seatId).isPresent();
    }
}
