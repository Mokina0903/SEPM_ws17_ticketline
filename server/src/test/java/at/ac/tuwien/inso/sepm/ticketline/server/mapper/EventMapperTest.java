package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist.ArtistMapper;
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

    @Autowired
    private ArtistMapper artistMapper;

    private static final long EVENT_ID = 1L;
    private static final String EVENT_TITLE = "Title";
    private static final String EVENT_DESCRIPTION = "This is a very long text containing all the contents of the event" +
        " and a lot of other more or less useful information.";
    private static final String EVENT_DESCRIPTION_SUMMARY = "This is a very long text containing all the";
    private static final long EVENT_PRICE = 100L;
    private static final LocalDateTime EVENT_START =
        LocalDateTime.of(2016, 1, 1, 10, 0, 0, 0);
    private static final LocalDateTime EVENT_END =
        LocalDateTime.of(2016, 1, 1, 12, 0, 0, 0);

    private static final String EVENT_ARTIST_FIRSTNAME = "Firstname";
    private static final String EVENT_ARTIST_LASTNAME = "Lastname";
    private static final Artist EVENT_ARTIST = Artist.builder()
        .id(1L)
        .artistFirstname(EVENT_ARTIST_FIRSTNAME)
        .artistLastName(EVENT_ARTIST_LASTNAME)
        .build();
    private static final List<Artist> EVENT_ARTISTS = new ArrayList<>();

    private static final SimpleArtistDTO EVENT_ARTIST_DTO = SimpleArtistDTO.builder()
        .id(1L)
        .artistFirstname(EVENT_ARTIST_FIRSTNAME)
        .artistLastName(EVENT_ARTIST_LASTNAME)
        .build();
    private static final List<SimpleArtistDTO> EVENT_ARTISTS_DTO = new ArrayList<>();

    private static final Event.EventCategory EVENT_CATEGORY = Event.EventCategory.Musical;

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
        EVENT_ARTISTS.add(EVENT_ARTIST);
        EVENT_ARTISTS_DTO.add(EVENT_ARTIST_DTO);
        setHalls();
        setSeat();
    }

    @Test
    public void shouldMapEventsToDetailedEventsDTO() {
        Event event = Event.builder()
            .id(EVENT_ID)
            .artists(EVENT_ARTISTS)
            .title(EVENT_TITLE)
            .description(EVENT_DESCRIPTION)
            .price(EVENT_PRICE)
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_END)
            .category(EVENT_CATEGORY)
            .hall(HALL)
            .build();
        DetailedEventDTO detailedEventDTO = eventMapper.eventToDetailedEventDTO(event);
        assertThat(detailedEventDTO).isNotNull();
        assertThat(detailedEventDTO.getId()).isEqualTo(1L);

        for (int i = 0; i < EVENT_ARTISTS.size(); i++) {
            assertThat(detailedEventDTO.getArtists().get(i).getId()).isEqualTo(EVENT_ARTISTS.get(i).getId());
            assertThat(detailedEventDTO.getArtists().get(i).getArtistFirstName()).isEqualTo(EVENT_ARTISTS.get(i).getArtistFirstName());
            assertThat(detailedEventDTO.getArtists().get(i).getArtistLastName()).isEqualTo(EVENT_ARTISTS.get(i).getArtistLastName());
        }

        assertThat(detailedEventDTO.getArtists().size()).isEqualTo(EVENT_ARTISTS.size());
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
            .artists(EVENT_ARTISTS_DTO)
            .title(EVENT_TITLE)
            .description(EVENT_DESCRIPTION)
            .price(EVENT_PRICE)
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_END)
            .category(EVENT_CATEGORY.name())
            .hall(HALL_DTO)
            .build();
        Event event = eventMapper.detailedEventDTOToEvent(detailedEventDTO);
        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo(1L);
        assertThat(event.getEventCategory()).isEqualTo(EVENT_CATEGORY);

        for (int i = 0; i < EVENT_ARTISTS_DTO.size(); i++) {
            assertThat(event.getArtists().get(i).getId()).isEqualTo(EVENT_ARTISTS.get(i).getId());
            assertThat(event.getArtists().get(i).getArtistFirstName()).isEqualTo(EVENT_ARTISTS.get(i).getArtistFirstName());
            assertThat(event.getArtists().get(i).getArtistLastName()).isEqualTo(EVENT_ARTISTS.get(i).getArtistLastName());
        }

        assertThat(event.getArtists().size()).isEqualTo(EVENT_ARTISTS.size());
        assertThat(event.getTitle()).isEqualTo(EVENT_TITLE);
        assertThat(event.getDescription()).isEqualTo(EVENT_DESCRIPTION);
        assertThat(event.getPrice()).isEqualTo(EVENT_PRICE);
        assertThat(event.getStartOfEvent()).isEqualTo(EVENT_START);
        assertThat(event.getEndOfEvent()).isEqualTo(EVENT_END);
    }

    @Test
    public void shouldMapEventsToSimpleEventsDTONotShorteningDescriptionToSummary() {
        Event event = Event.builder()
            .id(EVENT_ID)
            .title(EVENT_TITLE)
            .artists(EVENT_ARTISTS)
            .description(EVENT_DESCRIPTION_SUMMARY)
            .price(EVENT_PRICE)
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_END)
            .category(EVENT_CATEGORY)
            .build();
        SimpleEventDTO simpleEventDTO = eventMapper.eventToSimpleEventDTO(event);
        assertThat(simpleEventDTO).isNotNull();
        assertThat(simpleEventDTO.getId()).isEqualTo(1L);
        assertThat(simpleEventDTO.getTitle()).isEqualTo(EVENT_TITLE);
        assertThat(simpleEventDTO.getEventCategory()).isEqualTo(EVENT_CATEGORY.name());

        for (int i = 0; i < EVENT_ARTISTS_DTO.size(); i++) {
            assertThat(event.getArtists().get(i).getId()).isEqualTo(EVENT_ARTISTS.get(i).getId());
            assertThat(event.getArtists().get(i).getArtistFirstName()).isEqualTo(EVENT_ARTISTS.get(i).getArtistFirstName());
            assertThat(event.getArtists().get(i).getArtistLastName()).isEqualTo(EVENT_ARTISTS.get(i).getArtistLastName());
        }

        assertThat(event.getArtists().size()).isEqualTo(EVENT_ARTISTS.size());
        assertThat(simpleEventDTO.getDescriptionSummary()).isEqualTo(EVENT_DESCRIPTION_SUMMARY);
        assertThat(simpleEventDTO.getPrice()).isEqualTo(EVENT_PRICE);
        assertThat(simpleEventDTO.getStartOfEvent()).isEqualTo(EVENT_START);
        assertThat(simpleEventDTO.getEndOfEvent()).isEqualTo(EVENT_END);
    }

    @Test
    public void shouldMapEventsToSimpleEventsDTOShorteningDescriptionToSummary() {
        Event event = Event.builder()
            .id(EVENT_ID)
            .title(EVENT_TITLE)
            .artists(EVENT_ARTISTS)
            .description(EVENT_DESCRIPTION)
            .price(EVENT_PRICE)
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_END)
            .category(EVENT_CATEGORY)
            .build();
        SimpleEventDTO simpleEventDTO = eventMapper.eventToSimpleEventDTO(event);
        assertThat(simpleEventDTO).isNotNull();
        assertThat(simpleEventDTO.getId()).isEqualTo(1L);
        assertThat(simpleEventDTO.getTitle()).isEqualTo(EVENT_TITLE);
        assertThat(simpleEventDTO.getEventCategory()).isEqualTo(EVENT_CATEGORY.name());

        for (int i = 0; i < EVENT_ARTISTS_DTO.size(); i++) {
            assertThat(event.getArtists().get(i).getId()).isEqualTo(EVENT_ARTISTS.get(i).getId());
            assertThat(event.getArtists().get(i).getArtistFirstName()).isEqualTo(EVENT_ARTISTS.get(i).getArtistFirstName());
            assertThat(event.getArtists().get(i).getArtistLastName()).isEqualTo(EVENT_ARTISTS.get(i).getArtistLastName());
        }

        assertThat(event.getArtists().size()).isEqualTo(EVENT_ARTISTS.size());
        assertThat(simpleEventDTO.getDescriptionSummary()).isEqualTo(EVENT_DESCRIPTION_SUMMARY);
        assertThat(simpleEventDTO.getPrice()).isEqualTo(EVENT_PRICE);
        assertThat(simpleEventDTO.getStartOfEvent()).isEqualTo(EVENT_START);
        assertThat(simpleEventDTO.getEndOfEvent()).isEqualTo(EVENT_END);
    }
}
