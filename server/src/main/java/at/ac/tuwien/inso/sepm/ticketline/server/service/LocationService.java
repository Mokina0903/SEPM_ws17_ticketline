package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationService {


    /**
     * Find location entries by search parameters.
     *
     * @param search list of search parameters
     * @param request Pagable request
     * @return Page containing the location entries
     */
    Page<Location> findByAdvancedSearch(String search, Pageable request);

        /**
         * Find a single location entry by id.
         *
         * @param locationId the is of the location entry
         * @return Optional containing the location entry
         */
    Location findLocationById( Long locationId);

    /**
     * Find all Locations
     *
     * @return List of Location entries
     */
    List<Location> findAllLocations();

    /**
     * Find a single hall entry by id.
     *
     * @param hallId the is of the hall entry
     * @return Optional containing the hall entry
     */
    Hall findHallById(Long hallId);

    /**
     * Find all Halls
     *
     * @return List of Hall entries
     */
    List<Hall> findAllHalls();
}
