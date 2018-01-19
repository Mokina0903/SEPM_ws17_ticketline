package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.QArtist;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

@Component
public class SimpleArtistFilterBuilder implements ArtistFilterBuilder{

    private final QArtist ARTIST = QArtist.artist;

    @Override
    public Predicate buildAnd(ArtistFilter filter) {
        return new OptionalBooleanBuilder(ARTIST.isNotNull())
            .notEmptyAnd(ARTIST.artistFirstName::containsIgnoreCase, filter.getArtistFirstName())
            .notEmptyAnd(ARTIST.artistLastName::containsIgnoreCase, filter.getArtistLastName())
            .build();
    }

    @Override
    public Predicate buildOr(ArtistFilter filter) {
        return new OptionalBooleanBuilder(ARTIST.isNotNull())
            .notEmptyAnd(ARTIST.artistFirstName::containsIgnoreCase, filter.getArtistFirstName())
            .notEmptyOr(ARTIST.artistLastName::containsIgnoreCase, filter.getArtistLastName())
            .build();
    }
}
