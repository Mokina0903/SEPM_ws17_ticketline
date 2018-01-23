package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.QLocation;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

@Component
public class SimpleLocationFilterBuilder implements LocationFilterBuilder{

    private final QLocation LOCATION = QLocation.location;

    @Override
    public Predicate buildAnd(LocationFilter filter) {

        return new OptionalBooleanBuilder(LOCATION.isNotNull())
            .notEmptyAnd(LOCATION.description::containsIgnoreCase, filter.getDescription())
            .notEmptyAnd(LOCATION.city::containsIgnoreCase, filter.getCity())
            .notEmptyAnd(LOCATION.street::containsIgnoreCase, filter.getStreet())
            .notNullAnd(LOCATION.zip::eq, filter.getZip())
            .notEmptyAnd(LOCATION.country::containsIgnoreCase, filter.getCountry())
            .build();
    }

    @Override
    public Predicate buildOr(LocationFilter filter) {
        return new OptionalBooleanBuilder(LOCATION.isNotNull())
            .notEmptyAnd(LOCATION.description::containsIgnoreCase, filter.getDescription())
            .notEmptyOr(LOCATION.country::containsIgnoreCase, filter.getCountry())
            .notEmptyOr(LOCATION.street::containsIgnoreCase, filter.getStreet())
            .notEmptyOr(LOCATION.city::containsIgnoreCase, filter.getCity())
            .notNullAnd(LOCATION.zip::eq, filter.getZip())
            .build();
    }
}