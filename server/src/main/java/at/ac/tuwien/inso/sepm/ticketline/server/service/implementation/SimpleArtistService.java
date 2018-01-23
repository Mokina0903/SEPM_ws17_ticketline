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
        parameters = replaceUnderscores(parameters);
        Predicate predicate = filterBuilder.buildOr(new ArtistFilter(parameters));
        return artistRepository.findAll(predicate, request);
    }

    @Override
    public Page<Artist> findByAdvancedSearch(HashMap<String, String> parameters, Pageable request) {
        parameters = replaceUnderscores(parameters);
        Predicate predicate = filterBuilder.buildAnd(new ArtistFilter(parameters));
        return artistRepository.findAll(predicate, request);
    }

    private HashMap<String, String> replaceUnderscores(HashMap<String, String> parameters) {
        for(String key : parameters.keySet()) {
            String replace = parameters.get(key);
            if (replace.contains("_")) {
                replace = replace.replaceAll("_", " ").toLowerCase();
                parameters.put(key, replace);
                System.out.println("Replacing _");
            }
        }
        return parameters;
    }
}
