package at.ac.tuwien.inso.sepm.ticketline.server.repository.Location;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query(value = "Select * from hall where location_id = :id", nativeQuery = true)
    List<Hall> findAllByLocationId(@Param("id") Long id);
}
