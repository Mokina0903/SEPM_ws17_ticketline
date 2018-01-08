package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.EmptyValueException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.TicketAlreadyExistsException;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;

import java.util.List;

public interface TicketRestClient {

    /**
     * Find a single ticket entry by id.
     *
     * @param id the is of the ticket entry
     * @return Ticket with specific id
     */
    TicketDTO findOneById( Long id) throws DataAccessException;

    /**
     *find all tickets
     *
     * @param request defienes how to read paged from the database
     * @return a list of tickets, though the size of the list is dependent of the pageable object
     */
    // Page<TicketDTO> findAll( Pageable request);


    /**
     * find all tickets for an event
     *
     * @param eventId of the event to find tickets for
     * @return list of tickets
     */
    List<TicketDTO> findByEventId( Long eventId) throws DataAccessException;

    /**
     * find all tickets of specific customer
     *
     * @param customerId of the customer to find tickets for
     * @return list of tickets
     */
    List<TicketDTO> findByCustomerId( Long customerId) throws DataAccessException;

    /**
     * check if seat is already sold
     *
     * @param eventId of the event
     * @param seatId of the seat
     * @return true if seat is already booked/sold
     */
    Boolean isBooked(Long eventId,Long seatId) throws DataAccessException;

    /**
     * save ticket if seat is free
     *
     * @param ticketDTOS to save
     * @return saved ticket with id, or throw AlreadyExistsException if ticket is already booked
     */
    List<TicketDTO> save(List<TicketDTO> ticketDTOS) throws DataAccessException, TicketAlreadyExistsException, EmptyValueException;
}
