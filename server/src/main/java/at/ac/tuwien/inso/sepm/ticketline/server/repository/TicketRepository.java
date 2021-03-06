package at.ac.tuwien.inso.sepm.ticketline.server.repository;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
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
    List<Ticket> findByEvent_IdAndIsDeletedFalse(Long event_Id);

    /**
     *
     * @param event_Id
     * @return the number of Tickets within this event
     */
    Long countByEvent_IdAndIsDeletedFalse(Long event_Id);

    /**
     * find all tickets of specific customer
     *
     * @param customer_Id of the customer to find tickets for
     * @return list of tickets
     */
    List<Ticket> findByCustomer_Id(Long customer_Id);

    /**
     * deletes a ticket with a certain ID
     *
     * @param ticket_Id of the ticket that should be deletet
     */
    @Modifying
    @Transactional
    @Query(value = "Delete from ticket where :ticket_Id = id", nativeQuery = true)
    void deleteTicketByTicket_Id(@Param("ticket_Id") Long ticket_Id);

    /**
     * deletes a ticket with a certain ID
     *
     * @param ticket_Id of the ticket that should be deletet
     */
    @Modifying
    @Transactional
    @Query(value = "update ticket set is_deleted = true where id = :ticket_Id", nativeQuery = true)
    void deleteFlagTicketByTicket_Id(@Param("ticket_Id") Long ticket_Id);

    /**
     * set is_paid flag true for all ticket with a certain reservationsnumber
     *
     * @param reservation_Id of the tickets that should be paid
     */
    @Modifying
    @Transactional
    @Query(value = "update ticket set is_paid = true where reservation_number = :reservation_Id", nativeQuery = true)
    void is_paidFlagTicketByReservation_Id(@Param("reservation_Id") Long reservation_Id);

    /**
     * @param reservation_number of the ticket
     * @param
     * @return optional containing ticket, if ticket is found that is part of a reservation and is not paid yet
     */
    @Query(value = "select * from ticket where reservation_number = :reservation_number and is_paid = false",nativeQuery = true)
    List<Ticket> findByReservation_NumberAndIsPaidFalse(@Param("reservation_number") Long reservation_number);

    /**
     * @param event_Id of the event the ticket is for
     * @param seat_id of the seat the ticket is for
     * @return optional containing ticket, if ticket is found the seat is sold, if no ticket found the seat is free
     */
    Optional<Ticket> findByEvent_idAndSeat_idAndIsDeletedFalse(Long event_Id, Long seat_id);

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

    /**
     *
     * @return list of tickets from events when startzeit-now < 30 minuten
     *
     */
    @Query(value = "select * from ticket t join event e on t.event_id=e.id " +
        "where TIMESTAMPDIFF('MINUTE', CURRENT_TIMESTAMP(), e.start_Of_Event)< 30 AND t.is_Paid = false", nativeQuery = true)
    List<Ticket>setTicketsFreeIf30MinsBeforeEvent();

    /**
     *
     * @param name of the customer
     * @param request describes the parameters of the page returned
     * @return a page of tickets
     */
    @Query(value="SELECT * from ticket t join customer c on t.customer_id = c.id " +
       "WHERE c.name LIKE :name OR c.surname LIKE :name"+
        " order by c.surname \n-- #pageable\n",
        countQuery = "SELECT count(*) from ticket t join customer c on t.customer_id = c.id" +
            " WHERE c.name LIKE :name OR c.surname LIKE :name "
        , nativeQuery = true)
    Page<Ticket> findAllByCustomerName(@Param("name") String name, Pageable request);

    /**
     *
     * @param reservationNumber reservation number of the tickets
     * @param request describes the parameters of the page returned
     * @return a page of tickets
     */
    @Query(value="SELECT * from ticket t " +
        "WHERE t.reservation_number=:nr \n-- #pageable\n",
        countQuery = "SELECT count(*) from ticket t" +
            " WHERE t.reservation_number=:nr "
        , nativeQuery = true)
    Page<Ticket> findByReservationNumberAndIsDeletedFalse(@Param("nr") Long reservationNumber, Pageable request);

    @Modifying
    @Query(value="UPDATE ticket t SET t.is_Deleted = true WHERE t.id=:id", nativeQuery = true)
    void updateReservationStatusToFalse(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE Ticket  WHERE is_paid = false AND event_id in "
        +"(Select id as event_id from event where DATEADD('DAY',-30,now()) < start_of_event AND "
        +"start_of_event < DATEADD('MINUTE',-1 * :minutes ,now()))", nativeQuery = true)
    void deleteAlloldReservations(@Param("minutes") Long minutes);


}
