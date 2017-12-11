package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.hall;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.SimpleHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = HallSummeryMapper.class)
public interface HallMapper {

    Hall detailedHallDTOToHall( DetailedHallDTO detailedHallDTO);

    DetailedHallDTO hallToDetailedHallDTO(Hall one);

    List<SimpleHallDTO> hallToSimpleHallDTO(List<Hall> all);

}
