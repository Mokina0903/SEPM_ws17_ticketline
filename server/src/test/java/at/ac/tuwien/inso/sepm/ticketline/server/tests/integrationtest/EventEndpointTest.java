package at.ac.tuwien.inso.sepm.ticketline.server.tests.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestDTOs;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.*;
import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestDTOs.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;

public class EventEndpointTest extends BaseIntegrationTest {
    // TODO: (David) Change to IntegrationTest

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private HallRepository hallRepository;

    @MockBean
    private ArtistRepository artistRepository;

    @MockBean
    private EventRepository eventRepository;

    @Test
    public void publishEventUnauthorizedAsUser() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void publishEventAsAdminLocationNotFound() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

        BDDMockito.given(locationRepository.findOneByDescription(EVENT_DESCRIPTION))
            .willReturn(null);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void publishEventAsAdminHallNotFound() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

        BDDMockito.doReturn(defaultLocation()).when(locationRepository).findOneByDescription(LOCATION_DESCRIPTION);
        BDDMockito.doReturn(null).when(hallRepository).findOneByDescriptionAndLocation(LOCATION_ID,HALL_DESCRIPTION);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void publishEventAsAdminwrongDate() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

        detailedEventDTO.setDescription("Wrong Date");
        detailedEventDTO.setStartOfEvent(EVENT_START);
        detailedEventDTO.setEndOfEvent(EVENT_START);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_ACCEPTABLE.value()));

        detailedEventDTO.setStartOfEvent(EVENT_ENDE);
        detailedEventDTO.setEndOfEvent(EVENT_START);

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_ACCEPTABLE.value()));
    }

    @Test
    public void publishEventAsAdmin() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

        BDDMockito.doReturn(defaultLocation()).when(locationRepository).findOneByDescription(LOCATION_DESCRIPTION);
        BDDMockito.doReturn(defaultHall()).when(hallRepository).findOneByDescriptionAndLocation(LOCATION_ID,HALL_DESCRIPTION);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = EVENT_START.format(formatter);
        String end = EVENT_ENDE.format(formatter);

        List<Event> emptyEventList = new ArrayList<>();

        BDDMockito.doReturn(emptyEventList).when(eventRepository).findDuplicates(EVENT_TITLE, EVENT_DESCRIPTION, HALL_ID, start, end);

        BDDMockito.doReturn(defaultEvent()).when(eventRepository).save(any(Event.class));

        BDDMockito.doReturn(defaultArtist()).when(artistRepository).findByArtistFirstNameAndArtistLastName(
            ARTIST_FIRSTNAME,
            ARTIST_LASTNAME
        );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        DetailedEventDTO detailedEventDTOResponse = response.as(DetailedEventDTO.class);

        Assert.assertThat(detailedEventDTO, is(detailedEventDTOResponse));
    }




    @Test
    public void publishEventAsAdminNewArtist() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

        BDDMockito.doReturn(defaultLocation()).when(locationRepository).findOneByDescription(LOCATION_DESCRIPTION);
        BDDMockito.doReturn(defaultHall()).when(hallRepository).findOneByDescriptionAndLocation(LOCATION_ID,HALL_DESCRIPTION);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = EVENT_START.format(formatter);
        String end = EVENT_ENDE.format(formatter);

        List<Event> emptyEventList = new ArrayList<>();

        BDDMockito.doReturn(emptyEventList).when(eventRepository).findDuplicates(EVENT_TITLE, EVENT_DESCRIPTION, HALL_ID, start, end);

        BDDMockito.doReturn(defaultEvent()).when(eventRepository).save(any(Event.class));

        BDDMockito.doReturn(null).when(artistRepository).findByArtistFirstNameAndArtistLastName(
            ARTIST_FIRSTNAME,
            ARTIST_LASTNAME
        );

        BDDMockito.doReturn(defaultArtist()).when(artistRepository).save(any(Artist.class));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void publishEventAsAdminEventDuplicate() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

        BDDMockito.doReturn(defaultLocation()).when(locationRepository).findOneByDescription(LOCATION_DESCRIPTION);
        BDDMockito.doReturn(defaultHall()).when(hallRepository).findOneByDescriptionAndLocation(LOCATION_ID,HALL_DESCRIPTION);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start = EVENT_START.format(formatter);
        String end = EVENT_ENDE.format(formatter);

        BDDMockito.doReturn(Collections.singletonList(defaultEvent())).when(eventRepository).findDuplicates(EVENT_TITLE, EVENT_DESCRIPTION, HALL_ID, start, end);

        BDDMockito.doReturn(defaultEvent()).when(eventRepository).save(any(Event.class));

        BDDMockito.doReturn(defaultArtist()).when(artistRepository).findByArtistFirstNameAndArtistLastName(
            ARTIST_FIRSTNAME,
            ARTIST_LASTNAME
        );

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT.value()));
    }



}
