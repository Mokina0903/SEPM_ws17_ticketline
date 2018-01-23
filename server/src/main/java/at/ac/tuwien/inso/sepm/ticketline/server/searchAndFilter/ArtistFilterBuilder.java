package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;

import com.querydsl.core.types.Predicate;

public interface ArtistFilterBuilder {

    Predicate buildAnd(ArtistFilter filter);
    Predicate buildOr(ArtistFilter filter);
}
