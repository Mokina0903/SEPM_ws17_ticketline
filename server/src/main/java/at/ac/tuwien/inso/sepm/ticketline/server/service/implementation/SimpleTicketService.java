package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.AlreadyExistsException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.EmptyFieldException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.OldVersionException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        return ticketRepository.findByEvent_IdAndIsDeletedFalse(eventId);
    }

    @Override
    public List<Ticket> findByCustomerId( Long customerId ) {
        return ticketRepository.findByCustomer_Id(customerId);
    }

    @Override
    public void deleteTicketByTicket_Id(Long ticket_Id) throws NotFoundException, OldVersionException {

        Ticket ticket = ticketRepository.getOne(ticket_Id);
        if(ticket.isPaid()) {
            Ticket ticket1 = ticketRepository.findOne(ticket_Id);
            if(ticket1 == null){
                throw new NotFoundException();
            }
            if(ticket1.isDeleted()){
                throw new OldVersionException();
            }
            ticketRepository.deleteFlagTicketByTicket_Id(ticket_Id);

        }
        else{
            ticketRepository.deleteTicketByTicket_Id(ticket_Id);
        }
    }


    @Override
    public List<Ticket> save( List<Ticket> tickets ) {
        if(tickets==null || tickets.isEmpty()){throw new EmptyFieldException();}

        long reservationNR = (LocalDate.now().getYear() % 100) * 100000000 ;

        synchronized (this) {
            for(Ticket ticket : tickets) {
                if (isBooked(ticket.getEvent().getId(), ticket.getSeat().getId())) {
                    throw new AlreadyExistsException("Ticket already sold.");
                }

                ticket.setDeleted(false);
                ticket.setReservationNumber(reservationNR);
                ticket.setReservationDate(LocalDateTime.now());

            }
            tickets = ticketRepository.save(tickets);

            reservationNR+=+ tickets.get(0).getId();
            for(Ticket ticket: tickets){
                ticket.setReservationNumber(reservationNR);
            }
            return ticketRepository.save(tickets);
        }
    }

    @Override
    public int ticketCountForEventForSector( Long event_id, char sector ) {
        return ticketRepository.ticketCountForEventForSector(event_id,sector);
    }

    @Override
    public Long countByEvent_IdAndIsDeletedFalse(Long event_Id) {
        return ticketRepository.countByEvent_IdAndIsDeletedFalse(event_Id);
    }

    @Override
    public void setTicketsFreeIf30MinsBeforEvent() {
       ticketRepository.deleteAlloldReservations(30L);
    }

    @Override
    public void payTicketByReservation_Id(Long reservation_Id) throws OldVersionException{
        if(ticketRepository.findByReservation_NumberAndIsPaidFalse(reservation_Id).isEmpty()){

            throw new OldVersionException();
        }
        ticketRepository.is_paidFlagTicketByReservation_Id(reservation_Id);
    }

    @Override
    public Page<Ticket> findAllByCustomerName(String name, Pageable request) {

        return ticketRepository.findAllByCustomerName("%"+name+"%", request);
    }

    @Override
    public Page<Ticket> findAllByReservationNumber(Long reservationNumber, Pageable request) {
        return ticketRepository.findByReservationNumberAndIsDeletedFalse(reservationNumber, request);
    }


    @Override
    public Boolean isBooked(Long eventId,Long seatId){
        return ticketRepository.findByEvent_idAndSeat_idAndIsDeletedFalse(eventId,seatId).isPresent();

    }
}
