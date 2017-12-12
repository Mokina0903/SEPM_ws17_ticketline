package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.SimpleHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.hall.HallMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.location.LocationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.seat.SeatMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.SeatRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/eventlocation")
@Api(value = "eventlocation")
public class LocationEndpoint {

    private final LocationRepository locationRepository;
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final LocationMapper locationMapper;
    private final HallMapper hallMapper;
    private final SeatMapper seatMapper;

    public LocationEndpoint( LocationRepository locationRepository, HallRepository hallRepository, SeatRepository seatRepository,
                             LocationMapper locationMapper, HallMapper hallMapper, SeatMapper seatMapper ) {
        this.locationRepository = locationRepository;
        this.hallRepository = hallRepository;
        this.seatRepository = seatRepository;
        this.locationMapper = locationMapper;
        this.hallMapper = hallMapper;
        this.seatMapper = seatMapper;
    }

    @RequestMapping(value = "/location/{locationId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific location entry")
    public DetailedLocationDTO findLocationById( @PathVariable Long locationId) {

        DetailedLocationDTO detailedLocationDTO = locationMapper.locationToDetailedLocationDTO(locationRepository.findOne(locationId));

        return detailedLocationDTO;
    }

    @RequestMapping( value = "/location" ,method = RequestMethod.GET)
    @ApiOperation(value = "Get simple information about a all location entries")
    public List<SimpleLocationDTO> findAllLocations() {

        List<SimpleLocationDTO> locations = locationMapper.locationToSimpleLocationDTO(locationRepository.findAll());

        return locations;
    }

    @RequestMapping(value = "/hall/{hallId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific hall entry")
    public DetailedHallDTO findHallById( @PathVariable Long hallId) {

        DetailedHallDTO detailedHallDTO = hallMapper.hallToDetailedHallDTO(hallRepository.findOne(hallId));

        return detailedHallDTO;
    }

    @RequestMapping( value = "/hall" ,method = RequestMethod.GET)
    @ApiOperation(value = "Get simple information about a all hall entries")
    public List<SimpleHallDTO> findAllHalls() {

        List<SimpleHallDTO> halls = hallMapper.hallToSimpleHallDTO(hallRepository.findAll());

        return halls;
    }

}
