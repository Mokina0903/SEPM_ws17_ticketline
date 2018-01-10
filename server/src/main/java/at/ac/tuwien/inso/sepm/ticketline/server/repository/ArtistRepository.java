package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    // TODO: Javadoc
    @Query(value = "Select * from artist where artist_First_Name = :firstName and artist_Last_Name = :lastName", nativeQuery = true)
    Artist findByArtistFirstNameAndArtistLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
