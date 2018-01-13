package at.ac.tuwien.inso.sepm.ticketline.server.repository.Location;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {

    /**
     * Find a single location entry by id.
     *
     * @param id the is of the location entry
     * @return Optional containing the location entry
     */
    Optional<Location> findOneById( Long id);

    /**
     * Find a single Location enery by Description
     *
     * @param description
     * @return containing the Location
     */
    Location findOneByDescription( String description);
}
