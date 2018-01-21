package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface ArtistRestClient {

    /**
     * Find all artists by search parameters ordered by last name
     *
     * @param request page to load
     * @param parameters for filter and search
     * @return Page of found artists
     * @throws DataAccessException in case something went wrong
     */
    Page<SimpleArtistDTO> find(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException;

}
