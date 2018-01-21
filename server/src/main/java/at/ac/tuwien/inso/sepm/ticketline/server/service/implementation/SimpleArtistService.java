package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter.ArtistFilter;
import at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter.SimpleArtistFilterBuilder;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ArtistService;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class SimpleArtistService implements ArtistService{


    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleArtistService.class);


    private final ArtistRepository artistRepository;

    @Autowired
    SimpleArtistFilterBuilder filterBuilder;

    public SimpleArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }


    @Override
    public Page<Artist> find(HashMap<String, String> parameters, Pageable request) {
        Predicate predicate = filterBuilder.buildOr(new ArtistFilter(parameters));
        Iterable<Artist> artists = artistRepository.findAll(predicate);
        List<Artist> artistList = Lists.newArrayList(artists);
        int start = request.getOffset();
        int end = (start + request.getPageSize()) > artistList.size() ? artistList.size() : (start + request.getPageSize());
        return new PageImpl<>(artistList.subList(start, end), request, artistList.size());

    }
}
