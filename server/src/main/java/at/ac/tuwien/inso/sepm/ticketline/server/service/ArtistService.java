package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;

public interface ArtistService {


    /**
     * find page of artist by parameters
     *
     * @param parameters for filtering
     * @param request for the page containing a page number and sort type
     * @return page of artists
     */
    Page<Artist> find(HashMap<String, String> parameters, Pageable request);

    /**
     * find page of artist by parameters
     *
     * @param parameters for filtering
     * @param request for the page containing a page number and sort type
     * @return page of artists
     */
    Page<Artist> findByAdvancedSearch(HashMap<String, String> parameters, Pageable request);
}
