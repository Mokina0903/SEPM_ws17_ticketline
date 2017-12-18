package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class EventMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class EventMapperTestConfiguration {
    }


    @Autowired
    private EventMapper eventMapper;

    private static final long EVENT_ID = 1L;
    private static final String EVENT_ARTIST_FIRSTNAME = "Firstname";
    private static final String EVENT_ARTIST_LASTNAME = "Lastname";
    private static final String EVENT_TITLE = "Title";
    private static final String EVENT_DESCRIPTION = "This is a very long text containing all the contents of the event" +
        " and a lot of other more or less useful information.";
    private static final long EVENT_PRICE = 100L;
    private static final LocalDateTime EVENT_START =
        LocalDateTime.of(2016, 1, 1, 10, 0, 0, 0);
    private static final LocalDateTime EVENT_END =
        LocalDateTime.of(2016, 1, 1, 12, 0, 0, 0);

    private final static Location LOCATION = Location.builder()
        .id(1L)
        .description("Description")
        .country("Country")
        .city("City")
        .zip(1234)
        .street("Street")
        .houseNr(1)
        .build();

    private final static DetailedLocationDTO DETAILED_LOCATION_DTO = DetailedLocationDTO.builder()
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

    private static final DetailedHallDTO HALL_DTO = DetailedHallDTO.builder()
        .id(1L)
        .description("Description of the hall")
        .location(DETAILED_LOCATION_DTO)
        .build();

    private Seat seat = Seat.builder()
        .id(1L)
        .nr(1)
        .row(1)
        .sector('A')
        .hall(HALL)
        .build();

    private void setHalls() {
        List<Hall> halls = new ArrayList<>();
        halls.add(HALL);
        LOCATION.setEventHalls(halls);

        List<DetailedHallDTO> hallsDTO = new ArrayList<>();
        hallsDTO.add(HALL_DTO);
    }

    private void setSeat() {
        List<Seat> seats = new ArrayList<>();
        seats.add(seat);
        HALL.setSeats(seats);
    }

    @Before
    public void setUp() {
        setHalls();
        setSeat();
    }

    @Test
    public void shouldMapEventsToDetailedEventsDTO() {
        Event event = Event.builder()
            .id(EVENT_ID)
            .artistFirstname(EVENT_ARTIST_FIRSTNAME)
            .artistLastName(EVENT_ARTIST_LASTNAME)
            .title(EVENT_TITLE)
            .description(EVENT_DESCRIPTION)
            .price(EVENT_PRICE)
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_END)
            .hall(HALL)
            .build();
        DetailedEventDTO detailedEventDTO = eventMapper.eventToDetailedEventDTO(event);
        assertThat(detailedEventDTO).isNotNull();
        assertThat(detailedEventDTO.getId()).isEqualTo(1L);
        assertThat(detailedEventDTO.getArtistFirstName()).isEqualTo(EVENT_ARTIST_FIRSTNAME);
        assertThat(detailedEventDTO.getArtistLastName()).isEqualTo(EVENT_ARTIST_LASTNAME);
        assertThat(detailedEventDTO.getTitle()).isEqualTo(EVENT_TITLE);
        assertThat(detailedEventDTO.getDescription()).isEqualTo(EVENT_DESCRIPTION);
        assertThat(detailedEventDTO.getPrice()).isEqualTo(EVENT_PRICE);
        assertThat(detailedEventDTO.getStartOfEvent()).isEqualTo(EVENT_START);
        assertThat(detailedEventDTO.getEndOfEvent()).isEqualTo(EVENT_END);
    }

    @Test
    public void shouldMapDetailedEventsDTOToEvents() {
        DetailedEventDTO detailedEventDTO = DetailedEventDTO.builder()
            .id(EVENT_ID)
            .artistFirstname(EVENT_ARTIST_FIRSTNAME)
            .artistLastName(EVENT_ARTIST_LASTNAME)
            .title(EVENT_TITLE)
            .description(EVENT_DESCRIPTION)
            .price(EVENT_PRICE)
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_END)
            .hall(HALL_DTO)
            .build();
        Event event = eventMapper.detailedEventDTOToEvent(detailedEventDTO);
        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo(1L);
        assertThat(event.getArtistFirstName()).isEqualTo(EVENT_ARTIST_FIRSTNAME);
        assertThat(event.getArtistLastName()).isEqualTo(EVENT_ARTIST_LASTNAME);
        assertThat(event.getTitle()).isEqualTo(EVENT_TITLE);
        assertThat(event.getDescription()).isEqualTo(EVENT_DESCRIPTION);
        assertThat(event.getPrice()).isEqualTo(EVENT_PRICE);
        assertThat(event.getStartOfEvent()).isEqualTo(EVENT_START);
        assertThat(event.getEndOfEvent()).isEqualTo(EVENT_END);
    }

    @Test
    public void shouldMapEventsToSimpleEventsDTO() {
        Event event = Event.builder()
            .id(EVENT_ID)
            .artistFirstname(EVENT_ARTIST_FIRSTNAME)
            .artistLastName(EVENT_ARTIST_LASTNAME)
            .title(EVENT_TITLE)
            .description(EVENT_DESCRIPTION)
            .price(EVENT_PRICE)
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_END)
            .hall(HALL)
            .build();
        SimpleEventDTO simpleEventDTO = eventMapper.eventToSimpleEventDTO(event);
        assertThat(simpleEventDTO).isNotNull();
        assertThat(simpleEventDTO.getId()).isEqualTo(1L);
        assertThat(simpleEventDTO.getArtistFirstName()).isEqualTo(EVENT_ARTIST_FIRSTNAME);
        assertThat(simpleEventDTO.getArtistLastName()).isEqualTo(EVENT_ARTIST_LASTNAME);
        assertThat(simpleEventDTO.getTitle()).isEqualTo(EVENT_TITLE);
        assertThat(simpleEventDTO.getPrice()).isEqualTo(EVENT_PRICE);
        assertThat(simpleEventDTO.getStartOfEvent()).isEqualTo(EVENT_START);
        assertThat(simpleEventDTO.getEndOfEvent()).isEqualTo(EVENT_END);
    }

    @Test
    public void shouldMapSimpleEventsDTOToEvents() {
        DetailedEventDTO detailedEventDTO = DetailedEventDTO.builder()
            .id(EVENT_ID)
            .artistFirstname(EVENT_ARTIST_FIRSTNAME)
            .artistLastName(EVENT_ARTIST_LASTNAME)
            .title(EVENT_TITLE)
            .description(EVENT_DESCRIPTION)
            .price(EVENT_PRICE)
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_END)
            .hall(HALL_DTO)
            .build();
        Event event = eventMapper.detailedEventDTOToEvent(detailedEventDTO);
        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo(1L);
        assertThat(event.getArtistFirstName()).isEqualTo(EVENT_ARTIST_FIRSTNAME);
        assertThat(event.getArtistLastName()).isEqualTo(EVENT_ARTIST_LASTNAME);
        assertThat(event.getTitle()).isEqualTo(EVENT_TITLE);
        assertThat(event.getDescription()).isEqualTo(EVENT_DESCRIPTION);
        assertThat(event.getPrice()).isEqualTo(EVENT_PRICE);
        assertThat(event.getStartOfEvent()).isEqualTo(EVENT_START);
        assertThat(event.getEndOfEvent()).isEqualTo(EVENT_END);
    }

}
