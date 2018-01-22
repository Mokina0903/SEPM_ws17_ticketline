package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.QLocation;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

@Component
public class SimpleLocationFilterBuilder implements LocationFilterBuilder{

    private final QLocation LOCATION = QLocation.location;

    //todo buildOr, buildAnd for general search vs filter search

    @Override
    public Predicate buildAnd(LocationFilter filter) {

        return new OptionalBooleanBuilder(LOCATION.isNotNull())
            .notEmptyAnd(LOCATION.description::containsIgnoreCase, filter.getDescription())
            .notEmptyAnd(LOCATION.city::containsIgnoreCase, filter.getCountry())
            .notEmptyAnd(LOCATION.street::containsIgnoreCase, filter.getStreet())
            .notNullAnd(LOCATION.zip::eq, filter.getZip())
            .notNullAnd(LOCATION.country::eq, filter.getCountry())
            .build();
    }

    @Override
    public Predicate buildOr(LocationFilter filter) {
        return new OptionalBooleanBuilder(LOCATION.isNotNull())
            .notEmptyAnd(LOCATION.description::containsIgnoreCase, filter.getDescription())
            .notEmptyOr(LOCATION.city::containsIgnoreCase, filter.getCountry())
            .notEmptyOr(LOCATION.street::containsIgnoreCase, filter.getStreet())
            .notNullAnd(LOCATION.zip::eq, filter.getZip())
            .build();
    }
}