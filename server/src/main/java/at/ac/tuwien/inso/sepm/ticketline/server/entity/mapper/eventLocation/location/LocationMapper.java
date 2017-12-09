package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.location;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.LocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = LocationSummaryMapper.class)
public interface LocationMapper {

    Location locationDTOToLocation(LocationDTO locationDTO);

    LocationDTO locationToLocationDTO(Location one);

    List<LocationDTO> locationToLocationDTO(List<Location> all);
}
