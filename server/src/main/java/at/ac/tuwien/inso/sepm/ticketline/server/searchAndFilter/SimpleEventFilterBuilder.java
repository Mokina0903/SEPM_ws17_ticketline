package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.QEvent;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

@Component
public class SimpleEventFilterBuilder implements EventFilterBuilder {

    private final QEvent EVENT = QEvent.event;


    //todo buildOr, buildAnd for general search vs filter search

    @Override
    public Predicate buildAnd(EventFilter filter) {

        return new OptionalBooleanBuilder(EVENT.isNotNull())
            .notEmptyAnd(EVENT.title::containsIgnoreCase, filter.getTitle())
            .notEmptyAnd(EVENT.description::containsIgnoreCase, filter.getDescription())
            .notNullAnd(EVENT.price::loe, filter.getPriceTo())
            .notNullAnd(EVENT.price::goe, filter.getPriceFrom())
            //todo filter time
            /*.notNullAnd(EVENT.startOfEvent::between, filter.getTimeStart(), filter.getTimeStart())
            .notNullAnd(EVENT.duration::before, filter.getDuration())*/
            .build();
    }

    @Override
    public Predicate buildOr(EventFilter filter) {
        return new OptionalBooleanBuilder(EVENT.isNotNull())
            .notEmptyOr(EVENT.title::containsIgnoreCase, filter.getTitle())
            .notEmptyOr(EVENT.description::containsIgnoreCase, filter.getDescription())
            .build();
    }
}
