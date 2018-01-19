package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.ArtistRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ArtistService;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class SimpleArtistService implements ArtistService{

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventService.class);

    private final ArtistRestClient artistRestClient;

    public SimpleArtistService(ArtistRestClient artistRestClient ) {
        this.artistRestClient = artistRestClient;
    }

    @Override
    public Page<SimpleArtistDTO> find(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException {
        return artistRestClient.find(request, parameters);
    }
}
