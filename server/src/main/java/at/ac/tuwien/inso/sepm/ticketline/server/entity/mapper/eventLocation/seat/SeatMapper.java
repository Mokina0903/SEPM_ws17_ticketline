package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.seat;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = SeatSummaryMapper.class)
public interface SeatMapper {

    Seat seatDTOToSeat( SeatDTO seatDTO);

    List<SeatDTO> seatToSeatDTO(List<Seat> seat);

}
