package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.SimpleHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.hall.HallMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.location.LocationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.seat.SeatMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class HallMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class SeatMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private HallMapper hallMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private SeatMapper seatMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private LocationMapper locationMapper;

    private static final DetailedLocationDTO LOCATIONDTO = new DetailedLocationDTO();
    private static final String DESCRIPTION = "Giant";
    private static final Long ID = 1L;
    private static final List<Hall> HallList = new ArrayList<>();
    private static Location LOCATION = Location.builder()
        .id(1L)
        .description("Description")
        .country("Country")
        .city("City")
        .zip(1234)
        .street("Street")
        .houseNr(1)
        .build();

    private static final Hall HALL = Hall.builder()
        .id(1L)
        .description("Description of the hall")
        .location(LOCATION)
        .build();

    private Seat SEAT = Seat.builder()
        .hall(HALL)
        .nr(2)
        .row(4)
        .sector('A')
        .id(5L)
        .build();

    @Test
    public void shouldMapHallToSimpleHallDTO() {
        List<Seat> seats = new ArrayList<>();
        seats.add(SEAT);
        Hall hall = Hall.builder()
            .description(DESCRIPTION)
            .location(LOCATION)
            .id(ID)
            .seats(seats)
            .build();

        List<Hall> hallList = new ArrayList<>();
        hallList.add(hall);
        List<SimpleHallDTO> hallListDTO = hallMapper.hallToSimpleHallDTO(hallList);

        assertThat(hallListDTO).isNotNull();
        assertThat(hallListDTO.size() == 1);
        SimpleHallDTO hallDTO = hallListDTO.get(0);
        assertThat(hallDTO.getId()).isEqualTo(ID);
        assertThat(hallDTO.getDescription()).isEqualTo(DESCRIPTION);
    }

    @Test
    public void shouldMapHallToDetailedHallDTO() {
        List<Seat> seats = new ArrayList<>();
        seats.add(SEAT);
        Hall hall = Hall.builder()
            .description(DESCRIPTION)
            .location(LOCATION)
            .id(ID)
            .seats(seats)
            .build();

        DetailedLocationDTO locationDTO = locationMapper.locationToDetailedLocationDTO(LOCATION);

        DetailedHallDTO hallDTO = hallMapper.hallToDetailedHallDTO(hall);

        List<SeatDTO> seatDTO = seatMapper.seatToSeatDTO(seats);

        assertThat(hallDTO).isNotNull();
        assertThat(hallDTO.getId()).isEqualTo(ID);
        assertThat(hallDTO.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(hallDTO.getLocation()).isEqualTo(locationDTO);
        assertThat(hallDTO.getSeats()).isEqualTo(seatDTO);
    }

    @Test
    public void shouldMapDetailedLocationDTOToLocation() {
        List<Seat> seats = new ArrayList<>();
        seats.add(SEAT);
        List<SeatDTO> seatDTOS = seatMapper.seatToSeatDTO(seats);
        DetailedHallDTO hallDTO = DetailedHallDTO.builder()
            .description(DESCRIPTION)
            .location(LOCATIONDTO)
            .id(ID)
            .seats(seatDTOS)
            .build();

        Hall hall = hallMapper.detailedHallDTOToHall(hallDTO);

        assertThat(hall).isNotNull();
        assertThat(hall.getId()).isEqualTo(ID);
        assertThat(hall.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(hall.getLocation()).isEqualTo(new Location());
        assertThat(hall.getSeats().toString()).isEqualTo(seats.toString());
    }

}
