package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long>{

    /**
     * Find a single ticket entry by id.
     *
     * @param id the is of the ticket entry
     * @return Optional containing the ticket entry
     */
    Optional<Ticket> findOneById( Long id);

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
     * @param event_Id of the event to find tickets for
     * @return list of tickets
     */
    List<Ticket> findByEvent_Id(Long event_Id); //todo: check if name is correct

    /**
     * find all tickets of specific customer
     *
     * @param customer_Id of the customer to find tickets for
     * @return list of tickets
     */
    List<Ticket> findByCustomer_Id(Long customer_Id);

    /**
     * @param event_Id of the event the ticket is for
     * @param seat_id of the seat the ticket is for
     * @return optional containing ticket, if ticket is found the seat is sold, if no ticket found the seat is free
     */
    Optional<Ticket> findByEvent_idAndSeat_id(Long event_Id, Long seat_id);

    /**
     * get the number of tickets already booked in specific sector for event
     *
     * @param event_id of the event
     * @param sector to check for tickets
     * @return number of tickets
     */
    @Query(value = "select count(*) from " +
        "(ticket t join event e on t.event_id=e.id join seat s on t.seat_id=s.id)" +
        " where event_id= :event_id and sector=:sec",nativeQuery = true)
    int ticketCountForEventForSector( @Param("event_id") Long event_id, @Param("sec") char sector);


}
