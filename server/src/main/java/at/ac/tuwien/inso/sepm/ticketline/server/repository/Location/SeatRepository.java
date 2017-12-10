package at.ac.tuwien.inso.sepm.ticketline.server.repository.Location;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
