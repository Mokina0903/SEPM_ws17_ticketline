package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidIdException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.OldVersionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TicketService {

    /**
     * Find a single ticket entry by id.
     *
     * @param id the is of the ticket entry
     * @return Ticket with specific id
     */
    Ticket findOneById( Long id);

    /**
     *find all tickets
     *
     * @param request defienes how to read paged from the database
     * @return a list of tickets, though the size of the list is dependent of the pageable object
     */
    Page<Ticket> findAll( Pageable request);


    /**
     * find all tickets for an event
     *
     * @param eventId of the event to find tickets for
     * @return list of tickets
     */
    List<Ticket> findByEventId( Long eventId);

    /**
     * find all tickets of specific customer
     *
     * @param customerId of the customer to find tickets for
     * @return list of tickets
     */
    List<Ticket> findByCustomerId(Long customerId);


    /**
     * deletes a ticket with a certain ID
     *
     * @param ticket_Id of the ticket that should be deletet
     */
    void deleteTicketByTicket_Id(Long ticket_Id) throws NotFoundException, OldVersionException;

    /**
     * check if seat is already sold
     *
     * @param eventId of the event
     * @param seatId of the seat
     * @return true if seat is already booked/sold
     */
    Boolean isBooked(Long eventId,Long seatId);

    /**
     * save tickets if seat is free
     *
     * @param tickets to save
     * @return saved tickets with id, or throw AlreadyExistsException if ticket is already booked
     */
    List<Ticket> save(List<Ticket> tickets);

    /**
     * get the number of tickets already booked in specific sector for event
     *
     * @param event_id of the event
     * @param sector to check for tickets
     * @return number of tickets
     */
    int ticketCountForEventForSector(Long event_id,char sector);


    /**
     *
     * @param event_Id
     * @return the number of Tickets within this event
     */
    Long countByEvent_IdAndIsDeletedFalse(Long event_Id);

    /**
     * Checks if tickets from any event are reserved 30mins before event start and sets them free if they are
     */
    void setTicketsFreeIf30MinsBeforEvent();

    /**
     *
     * @param name of a customer
     * @param request defienes how to read paged from the database
     * @return the list of Tickets of this customer
     */
    Page<Ticket>findAllByCustomerName(String name,Pageable request);

    /**
     *
     * @param reservationNumber
     * @param request defienes how to read paged from the database
     * @return the list of tickets from this reservation number
     */
    Page<Ticket>findAllByReservationNumber(Long reservationNumber, Pageable request);

}
