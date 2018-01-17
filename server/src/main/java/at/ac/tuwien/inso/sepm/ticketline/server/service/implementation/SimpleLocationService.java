package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.LocationService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleLocationService implements LocationService{

    private final LocationRepository locationRepository;
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;

    public SimpleLocationService( LocationRepository locationRepository, HallRepository hallRepository, SeatRepository seatRepository ) {
        this.locationRepository = locationRepository;
        this.hallRepository = hallRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public Location findLocationById( Long locationId ) {
        return locationRepository.findOne(locationId);
    }

    @Override
    public List<Location> findAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Hall findHallById( Long hallId ) {
        return hallRepository.findOne(hallId);
    }

    @Override
    public List<Hall> findAllHalls() {
        return hallRepository.findAll();
    }

    @Override
    public List<Seat> findFreeSeatsForEventInSector( Long event_id, char sector ) {
        return seatRepository.findFreeSeatsForEventInSector(event_id,sector);
    }
}
