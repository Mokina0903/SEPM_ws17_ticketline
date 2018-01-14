package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.EmptyValueException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.TicketAlreadyExistsException;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService {

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
     Page<TicketDTO> findAll(Pageable request) throws DataAccessException, SearchNoMatchException;


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

    /**
     * get the number of tickets already booked in specific sector for event
     *
     * @param event_id of the event
     * @param sector to check for tickets
     * @return number of tickets
     */
    int ticketCountForEventForSector(Long event_id,char sector) throws DataAccessException;

    /**
     * find seats within sector that are still free for the specified event
     *
     * @param event_id of the event
     * @param sector to check for seats
     * @return list of free seats
     */
    List<SeatDTO> findFreeSeatsForEventInSector( Long event_id, char sector) throws DataAccessException;

    /**
     *
     * @param name input of the texfield, which should refer to some customer name
     * @param request describes the page issues (size, index)
     * @return list of tickets from a person whos name is described by 'name'
     */
    Page<TicketDTO> findByCustomerName(String name, Pageable request) throws DataAccessException, SearchNoMatchException;

    Page<TicketDTO> findByReservationNumber(Long reservationNumber, Pageable request) throws DataAccessException, SearchNoMatchException;
}
