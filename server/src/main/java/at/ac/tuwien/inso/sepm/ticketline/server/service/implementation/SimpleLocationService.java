package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter.LocationFilter;
import at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter.SimpleLocationFilterBuilder;
import at.ac.tuwien.inso.sepm.ticketline.server.service.LocationService;
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
public class SimpleLocationService implements LocationService{

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleLocationService.class);


    private final LocationRepository locationRepository;
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;

    @Autowired
    SimpleLocationFilterBuilder filterBuilder;

    public SimpleLocationService( LocationRepository locationRepository, HallRepository hallRepository, SeatRepository seatRepository ) {
        this.locationRepository = locationRepository;
        this.hallRepository = hallRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public Page<Location> find(HashMap<String, String> parameters, Pageable request) {
        Predicate predicate = filterBuilder.buildOr(new LocationFilter(parameters));
        System.out.println("LOC Service Predicate: ....." + predicate);
        Iterable<Location> events = locationRepository.findAll(predicate);
        List<Location> eventList = Lists.newArrayList(events);
        System.out.println("LOCATION service: list:.... " + eventList.size());
        int start = request.getOffset();
        int end = (start + request.getPageSize()) > eventList.size() ? eventList.size() : (start + request.getPageSize());
        return new PageImpl<>(eventList.subList(start, end), request, eventList.size());
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
}
