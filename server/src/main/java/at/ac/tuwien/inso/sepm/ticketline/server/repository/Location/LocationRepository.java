package at.ac.tuwien.inso.sepm.ticketline.server.repository.Location;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.QEvent;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.QLocation;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long>,
    QueryDslPredicateExecutor<Location>, QuerydslBinderCustomizer<QLocation> {

    //siehe: http://www.baeldung.com/rest-api-search-language-spring-data-querydsl

    @Override
    default public void customize(
        QuerydslBindings bindings, QLocation root) {
        bindings.bind(String.class)
            .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }


    /**
     * Find a single location entry by id.
     *
     * @param id the is of the location entry
     * @return Optional containing the location entry
     */
    Optional<Location> findOneById( Long id);

    // TODO: (David) JavaDoc
    Location findOneByDescription( String description);
}
