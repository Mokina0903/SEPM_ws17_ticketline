package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.QArtist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.QEvent;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> ,
    QueryDslPredicateExecutor<Artist>, QuerydslBinderCustomizer<QArtist> {

    @Override
    default public void customize(
        QuerydslBindings bindings, QArtist root) {
        bindings.bind(String.class)
            .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }


    @Query(value = "Select * from artist where artist_First_Name = :firstName and artist_Last_Name = :lastName", nativeQuery = true)
    Artist findByArtistFirstNameAndArtistLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
