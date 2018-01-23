package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;


import com.querydsl.core.types.Predicate;

public interface EventFilterBuilder {


    Predicate buildAnd(EventFilter filter);
    Predicate buildOr(EventFilter filter);
}