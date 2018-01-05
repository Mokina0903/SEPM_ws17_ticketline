package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * check if seat is already sold
     *
     * @param eventId of the event
     * @param seatId of the seat
     * @return true if seat is already booked/sold
     */
    Boolean isBooked(Long eventId,Long seatId);

    /**
     * save ticket if seat is free
     *
     * @param ticket to save
     * @return saved ticket with id, or throw AlreadyExistsException if ticket is already booked
     */
    Ticket save(Ticket ticket);
}
