package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.QEvent;
import com.querydsl.core.types.Predicate;
import org.hibernate.boot.jaxb.SourceType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class SimpleEventFilterBuilder implements EventFilterBuilder {

    private final QEvent EVENT = QEvent.event;

    @Override
    public Predicate buildAnd(EventFilter filter) {

        System.out.println("Building predicate:::::::::::::::::::::::::::::::::::::::");
        System.out.println("ELEMENT IN TIME:::::::: " + EVENT.startOfEventTime.getAnnotatedElement().toString());
        return new OptionalBooleanBuilder(EVENT.isNotNull())
            .notEmptyAnd(EVENT.title::containsIgnoreCase, filter.getTitle())
            .notEmptyAnd(EVENT.description::containsIgnoreCase, filter.getDescription())
            .notNullAnd(EVENT.price::loe, filter.getPriceTo())
            .notNullAnd(EVENT.price::goe, filter.getPriceFrom())
            .notNullAnd(EVENT.startOfEventTime::before, filter.getStartTimeUpperBound())
            .notNullAnd(EVENT.startOfEventTime::after, filter.getStartTimeLowerBound())

            //todo filter time
/*          .notNullAnd(EVENT.startOfEvent::between, filter.getStartTimeUpperBound())
          .notNullAnd(EVENT.startOfEvent::after, filter.getStartTimeLowerBound())*/

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
