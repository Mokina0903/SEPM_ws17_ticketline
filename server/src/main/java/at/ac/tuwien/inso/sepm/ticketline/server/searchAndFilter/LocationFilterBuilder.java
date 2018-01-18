package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;

import com.querydsl.core.types.Predicate;

public interface LocationFilterBuilder {

    Predicate buildAnd(LocationFilter filter);
    Predicate buildOr(LocationFilter filter);
}
