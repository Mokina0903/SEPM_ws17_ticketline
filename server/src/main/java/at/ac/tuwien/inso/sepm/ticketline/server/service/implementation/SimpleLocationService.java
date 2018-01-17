package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class SimpleLocationService implements LocationService{

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleLocationService.class);


    private final LocationRepository locationRepository;
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;

    public SimpleLocationService( LocationRepository locationRepository, HallRepository hallRepository, SeatRepository seatRepository ) {
        this.locationRepository = locationRepository;
        this.hallRepository = hallRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public Page<Location> findByAdvancedSearch(String search, Pageable request) {
       /* MyPredicatesBuilder builder = new MyPredicatesBuilder("location");
        if (search != null) {
            try {
                search = URLDecoder.decode(search, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.warn("Error while encoding search path");
            }

            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)([a-zA-Z0-9_.\\s]+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        Iterable<Location> locations = locationRepository.findAll(builder.build());

        List<Location> locationList = Lists.newArrayList(locations);
        return new PageImpl<>(locationList, request, locationList.size());*/
       return null;
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
