package at.ac.tuwien.inso.sepm.ticketline.server.repository.Location;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {

    /**
     * Find a single seat entry by id.
     *
     * @param id the is of the seat entry
     * @return Optional containing the seat entry
     */
    Optional<Seat> findOneById( Long id);

    @Query(value = "Select * from seat where hall_id = :id", nativeQuery = true)
    List<Seat> findAllByHallId( @Param("id") Long id);

    /**
     * find seats within sector that are still free for the specified event
     *
     * @param event_id of the event
     * @param sector to check for seats
     * @return list of free seats
     */
    @Query(value = "Select * from seat s where s.id in (Select s.id from seat s join event e on s.hall_id = e.hall_id" +
        " where e.id= :event_id and s.sector= :sector " +
        "and s.id not in " +
        "( Select seat_id from seat s join ticket t on s.id=t.seat_id join event e on t.event_id=e.id where e.id= :event_id))",
        nativeQuery = true)
    List<Seat> findFreeSeatsForEventInSector( @Param("event_id") Long event_id, @Param("sector") char sector);
}
