package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.seat;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SeatSummaryMapper.class)
public interface SeatMapper {
}
