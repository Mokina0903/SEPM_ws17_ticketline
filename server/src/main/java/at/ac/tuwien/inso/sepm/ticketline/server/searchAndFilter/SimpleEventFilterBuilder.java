package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.QEvent;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class SimpleEventFilterBuilder implements EventFilterBuilder {

    private final QEvent EVENT = QEvent.event;

    @Override
    public Predicate buildAnd(EventFilter filter) {

        return new OptionalBooleanBuilder(EVENT.isNotNull())
            .notEmptyAnd(EVENT.title::containsIgnoreCase, filter.getTitle())
            .notEmptyAnd(EVENT.description::containsIgnoreCase, filter.getDescription())
            .notNullAnd(EVENT.price::loe, filter.getPriceTo())
            .notNullAnd(EVENT.price::goe, filter.getPriceFrom())
            //todo filter time
            .notNullAnd(EVENT.startOfEvent::ne, LocalDateTime.now())
           // .notNullAnd(EVENT.duration::before, filter.getDuration())*/
            .build();
    }

    @Override
    public Predicate buildOr(EventFilter filter) {
        return new OptionalBooleanBuilder(EVENT.isNotNull())
            .notEmptyAnd(EVENT.title::containsIgnoreCase, filter.getTitle())
            .notEmptyOr(EVENT.description::containsIgnoreCase, filter.getDescription())
            .build();
    }
}
