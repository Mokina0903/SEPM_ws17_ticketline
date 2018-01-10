package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;

import java.util.List;

public interface LocationService {

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

    /**
     * find seats within sector that are still free for the specified event
     *
     * @param event_id of the event
     * @param sector to check for seats
     * @return list of free seats
     */
    List<Seat> findFreeSeatsForEventInSector( Long event_id, char sector);
}
