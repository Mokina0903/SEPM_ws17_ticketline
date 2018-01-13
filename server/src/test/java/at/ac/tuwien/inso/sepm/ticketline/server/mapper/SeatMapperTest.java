package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.seat.SeatMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
public class SeatMapperTest {


    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class SeatMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private SeatMapper seatMapper;

    private static final Hall HALL = new Hall();
    private static final Integer NUMBER = 1;
    private static final Integer ROW = 3;
    private static final char SECTOR = 'A';
    private static final Long ID = 1L;




    @Test
    public void shouldMapSeatToSeatDTO() {
        Seat seat = Seat.builder()
            .hall(HALL)
            .nr(NUMBER)
            .row(ROW)
            .sector(SECTOR)
            .id(ID)
            .build();

        List<Seat> seatList = new ArrayList<>();
        seatList.add(seat);
        List<SeatDTO> SeatDTOList = seatMapper.seatToSeatDTO(seatList);

        assertThat(SeatDTOList).isNotNull();
        assertThat(SeatDTOList.size() == 1);
        SeatDTO seatDTO = SeatDTOList.get(0);
        assertThat(seatDTO.getId()).isEqualTo(ID);
        assertThat(seatDTO.getNr()).isEqualTo(NUMBER);
        assertThat(seatDTO.getRow()).isEqualTo(ROW);
        assertThat(seatDTO.getSector()).isEqualTo(SECTOR);
        //assertThat(seatDTO.getHall()).isEqualTo(HALL);
        //ToDo gibt es absichtlich kein SeatDTO.getHall?
    }

    @Test
    public void shouldMapSeatSTOToSeat() {
        SeatDTO seatDTO = new SeatDTO.SeatDTOBuilder()
            .nr(NUMBER)
            .row(ROW)
            .sector(SECTOR)
            .id(ID)
            .build();

        Seat seat = seatMapper.seatDTOToSeat(seatDTO);
        assertThat(seat).isNotNull();
        assertThat(seat.getId()).isEqualTo(ID);
        assertThat(seat.getNr()).isEqualTo(NUMBER);
        assertThat(seat.getRow()).isEqualTo(ROW);
        assertThat(seat.getSector()).isEqualTo(SECTOR);
        //assertThat(seat.getHall()).isEqualTo(HALL);
        //ToDo gibt es absichtlich kein SeatDTO.getHall?
    }


}
