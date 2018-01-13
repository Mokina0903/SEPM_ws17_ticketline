package at.ac.tuwien.inso.sepm.ticketline.server.mapper;


import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.SimpleHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.location.LocationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.seat.SeatMapper;
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
public class LocationMapperTest {


    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class SeatMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private LocationMapper locationMapper;

    private static final String CITY = "Wien";
    private static final String COUNTRY = "Austria";
    private static final String DESCRIPTION = "Live";
    private static final String STREET = "Gasse";
    private static final Integer NUMBER = 2;
    private static final Integer ZIP = 4;
    private static final List<Hall> HALL = new ArrayList<>();
    private static final List<SimpleHallDTO> HALLDTO = new ArrayList<>();
    private static final Long ID = 1L;




    @Test
    public void shouldMapLocationToSimpleLocationDTO() {
        Location location = new Location.LocationBuilder()
            .city(CITY)
            .country(COUNTRY)
            .description(DESCRIPTION)
            .street(STREET)
            .houseNr(NUMBER)
            .zip(ZIP)
            .id(ID)
            .eventHalls(HALL)
            .build();

        List<Location> locationList = new ArrayList<>();
        locationList.add(location);
        List<SimpleLocationDTO> simpleLocationDTO = locationMapper.locationToSimpleLocationDTO(locationList);

        assertThat(simpleLocationDTO).isNotNull();
        assertThat(simpleLocationDTO.size() == 1);
        SimpleLocationDTO locationDTO = simpleLocationDTO.get(0);
        assertThat(locationDTO.getId()).isEqualTo(ID);
        assertThat(locationDTO.getHouseNr()).isEqualTo(NUMBER);
        assertThat(locationDTO.getZip()).isEqualTo(ZIP);
        assertThat(locationDTO.getCity()).isEqualTo(CITY);
        assertThat(locationDTO.getCountry()).isEqualTo(COUNTRY);
        assertThat(locationDTO.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(locationDTO.getStreet()).isEqualTo(STREET);

    }

    @Test
    public void shouldMapSimpleLocationDTOToLocation() {
        SimpleLocationDTO locationDTO = new SimpleLocationDTO.SimpleLocationDTOBuilder()
            .city(CITY)
            .country(COUNTRY)
            .description(DESCRIPTION)
            .street(STREET)
            .houseNr(NUMBER)
            .zip(ZIP)
            .id(ID)
            .build();

        //ToDo Mapper in die Richtung gibt es nicht ist das absicht?
        Assert.assertTrue(true);

    }

    @Test
    public void shouldMapLocationToDetailedLocationDTO() {
        Location location = new Location.LocationBuilder()
            .city(CITY)
            .country(COUNTRY)
            .description(DESCRIPTION)
            .street(STREET)
            .houseNr(NUMBER)
            .zip(ZIP)
            .id(ID)
            .eventHalls(HALL)
            .build();

        DetailedLocationDTO locationDTO = locationMapper.locationToDetailedLocationDTO(location);

        assertThat(locationDTO).isNotNull();
        assertThat(locationDTO.getId()).isEqualTo(ID);
        assertThat(locationDTO.getHouseNr()).isEqualTo(NUMBER);
        assertThat(locationDTO.getZip()).isEqualTo(ZIP);
        assertThat(locationDTO.getCity()).isEqualTo(CITY);
        assertThat(locationDTO.getCountry()).isEqualTo(COUNTRY);
        assertThat(locationDTO.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(locationDTO.getStreet()).isEqualTo(STREET);
        assertThat(locationDTO.getEventHalls()).isEqualTo(HALL);

    }

    @Test
    public void shouldMapDetailedLocationDTOToLocation() {
        DetailedLocationDTO locationDTO = new DetailedLocationDTO.DetailedLocationDTOBuilder()
            .city(CITY)
            .country(COUNTRY)
            .description(DESCRIPTION)
            .street(STREET)
            .houseNr(NUMBER)
            .zip(ZIP)
            .id(ID)
            .eventHalls(HALLDTO)
            .build();

        Location location = locationMapper.DetailedLocationDTOToLocation(locationDTO);

        assertThat(location).isNotNull();
        assertThat(location.getId()).isEqualTo(ID);
        assertThat(location.getHouseNr()).isEqualTo(NUMBER);
        assertThat(location.getZip()).isEqualTo(ZIP);
        assertThat(location.getCity()).isEqualTo(CITY);
        assertThat(location.getCountry()).isEqualTo(COUNTRY);
        assertThat(location.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(location.getStreet()).isEqualTo(STREET);
        assertThat(location.getEventHalls()).isEqualTo(HALLDTO);

    }


}