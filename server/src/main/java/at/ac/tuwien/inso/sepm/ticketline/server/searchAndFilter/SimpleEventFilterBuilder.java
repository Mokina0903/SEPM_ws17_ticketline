package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.QEvent;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;


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

            .notEmptyAnd(EVENT.eventDate::eq, filter.getDate())

            .notNullAnd(EVENT.startOfEventTime::loe, filter.getStartTimeUpperBound())
            .notNullAnd(EVENT.startOfEventTime::goe, filter.getStartTimeLowerBound())

            .notNullAnd(EVENT.duration::loe, filter.getDurationUpperBound())
            .notNullAnd(EVENT.duration::goe, filter.getDurationLowerBound())

            .notNullAnd(EVENT.seatSelection::ne, filter.getNoSeats())
            .notNullAnd(EVENT.seatSelection::eq, filter.getSeats())

            .notNullAnd(EVENT.startOfEvent::loe, filter.getUpcoming())
            .notNullAnd(EVENT.startOfEvent::goe, filter.getPast())

             .notEmptyAnd(EVENT.category::eq, filter.getCategory())

            //todo also show ausverkauft
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
