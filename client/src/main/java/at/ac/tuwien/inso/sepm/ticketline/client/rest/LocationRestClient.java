package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.SimpleHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface LocationRestClient {

    /**
     * find location by id
     *
     * @param locationId of the location
     * @return detailed Location DTO
     * @throws DataAccessException in case something went wrong
     */
    DetailedLocationDTO findLocationById(Long locationId) throws DataAccessException;

    /**
     * find all locations
     *
     * @return List of all simple locations
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleLocationDTO> findAllLocations() throws DataAccessException;

    /**
     * find hall by id
     *
     * @param hallId of the hall
     * @return detailed hall DTO
     * @throws DataAccessException in case something went wrong
     */
    DetailedHallDTO findHallById( Long hallId ) throws DataAccessException;


    /**
     * find all halls
     *
     * @return List of all simple halls
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleHallDTO> findAllHalls() throws DataAccessException;

    /**
     * Find all locations by search parameters ordered by City ascending
     *
     * @param request page to load
     * @param parameters search parameters
     * @return page of locations
     * @throws DataAccessException in case something went wrong
     */
    Page<SimpleLocationDTO> findAdvanced(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException;
}
