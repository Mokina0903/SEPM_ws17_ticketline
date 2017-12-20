package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.location;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = LocationSummaryMapper.class)
public interface LocationMapper {

    Location DetailedLocationDTOToLocation(DetailedLocationDTO detailedLocationDTO );

    DetailedLocationDTO locationToDetailedLocationDTO( Location one);

    List<SimpleLocationDTO> locationToSimpleLocationDTO( List<Location> all);
}
