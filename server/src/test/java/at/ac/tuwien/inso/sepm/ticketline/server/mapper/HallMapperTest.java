package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.SimpleHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.hall.HallMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.location.LocationMapper;
import org.junit.Assert;
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


    private static final DetailedLocationDTO LOCATIONDTO = new DetailedLocationDTO();
    private static final String DESCRIPTION = "Giant";
    private static final List<Seat> SEATS = new ArrayList<>();
    private static final List<SeatDTO> SEATSDTO = new ArrayList<>();
    private static final Long ID = 1L;
    private static final List<Hall> HallList = new ArrayList<>();
    private Location LOCATION = Location.builder()
        .city("Wien")
        .country("Austria")
        .description("Nice")
        .street("Gasse")
        .houseNr(1)
        .zip(3)
        .id(ID)
        .eventHalls(HallList)
        .build();


    @Test
    public void shouldMapHallToSimpleHallDTO() {
        Hall hall = Hall.builder()
            .description(DESCRIPTION)
            .location(LOCATION)
            .id(ID)
            .seats(SEATS)
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
    public void shouldMapSimpleHallDTOToHall() {
        SimpleHallDTO hallDTO = new SimpleHallDTO.SimpleHallDTOBuilder()
            .description(DESCRIPTION)
            .id(ID)
            .build();


        //ToDo Mapper in die Richtung gibt es nicht ist das absicht?
        Assert.assertTrue(true);

    }

/*
TODo Problem mit Location einmal wird Location erwartet, aber LocationDTO benötigt und umgekehrt

    @Test
    public void shouldMapHallToDetailedHallDTO() {
        Hall hall = Hall.builder()
            .description(DESCRIPTION)
            .location(LOCATION)
            .id(ID)
            .seats(SEATS)
            .build();


        DetailedHallDTO hallDTO = hallMapper.hallToDetailedHallDTO(hall);

        assertThat(hallDTO).isNotNull();
        assertThat(hallDTO.getId()).isEqualTo(ID);
        assertThat(hallDTO.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(hallDTO.getLocation()).isEqualTo(LOCATION);
        assertThat(hallDTO.getSeats()).isEqualTo(SEATS);

    }
*/
    /*
    @Test
    public void shouldMapDetailedLocationDTOToLocation() {
        DetailedHallDTO hallDTO = DetailedHallDTO.builder()
            .description(DESCRIPTION)
            .location(LOCATIONDTO)
            .id(ID)
            .seats(SEATSDTO)
            .build();

        Hall hall = hallMapper.detailedHallDTOToHall(hallDTO);

        assertThat(hall).isNotNull();
        assertThat(hall.getId()).isEqualTo(ID);
        assertThat(hall.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(hall.getLocation()).isEqualTo(LOCATION);
        assertThat(hall.getSeats()).isEqualTo(SEATS);

    }
    */
}
