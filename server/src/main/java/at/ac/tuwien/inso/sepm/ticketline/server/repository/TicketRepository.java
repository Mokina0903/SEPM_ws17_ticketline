package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
     * @param eventId of the event to find tickets for
     * @return list of tickets
     */
    List<Ticket> findByEvent_Id(Long eventId); //todo: check if name is correct

    /**
     * find all tickets of specific customer
     *
     * @param customerId of the customer to find tickets for
     * @return list of tickets
     */
    List<Ticket> findByCustomer_Id(Long customerId);


}
