package at.ac.tuwien.inso.sepm.ticketline.server.repository.Location;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall,Long>{

    /**
     * Find a single hall entry by id.
     *
     * @param id the is of the hall entry
     * @return Optional containing the hall entry
     */
    Optional<Hall> findOneById( Long id);
}
