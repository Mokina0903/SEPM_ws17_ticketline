package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventSummaryMapper.class)
public interface EventMapper {

    Event detailedEventDTOToEvent(DetailedEventDTO detailedEventDTO);

    DetailedEventDTO eventToDetailedEventDTO(Event one);

    List<SimpleEventDTO> eventToSimpleEventDTO( List<Event> all);

    SimpleEventDTO eventToSimpleEventDTO(Event one);

}
