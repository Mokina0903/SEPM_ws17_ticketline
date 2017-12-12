package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.SimpleHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;

import java.util.List;

public interface LocationService {

    DetailedLocationDTO findLocationById( Long locationId ) throws DataAccessException;

    List<SimpleLocationDTO> findAllLocations() throws DataAccessException;

    DetailedHallDTO findHallById( Long hallId ) throws DataAccessException;

    List<SimpleHallDTO> findAllHalls() throws DataAccessException;
}
