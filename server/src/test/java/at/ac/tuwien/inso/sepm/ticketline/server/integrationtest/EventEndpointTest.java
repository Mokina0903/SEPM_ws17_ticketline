package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;

public class EventEndpointTest extends BaseIntegrationTest {

    private static final String EVENT_ENDPOINT = "/event";

    private static final String EVENT_ARTIST_FIRSTNAME = "Firstname";
    private static final String EVENT_ARTIST_LASTNAME = "Lastname";

    // TODO: Test more beautiful
    // TODO: Test for Normal
    // TODO: Test for Hall not Found
    // TODO: Test for Location not Found
    // TODO: Test for new Artist
    // TODO: Test with duplicates

    @Test
    public void publishEventUnauthorizedAsUser() {
        LocalDateTime start = LocalDateTime.of(2017, 11, 29, 12, 15, 0, 0);

        DetailedHallDTO detailedHallDTO = DetailedHallDTO.builder()
            .description("Hall Description")
            .id(1L)
            .build();

        List<SimpleArtistDTO> artists = new ArrayList<>();

        artists.add(SimpleArtistDTO.builder()
            .id(1L)
            .build());

        DetailedEventDTO detailedEventDTO = DetailedEventDTO.builder()
            .startOfEvent(start)
            .endOfEvent(start.plusHours(2))
            .artists(artists)
            .price(123L)
            .description("Description")
            .title("Title")
            .hall(detailedHallDTO)
            .seatSelection(true)
            .build();

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
    public void publishEventAsAdmin() {
        setupDefaultLocation();

        LocalDateTime start = LocalDateTime.of(2017, 11, 29, 12, 15, 0, 0);

        DetailedLocationDTO locationDTO = DetailedLocationDTO.builder()
            .description("Test 1")
            .build();

        DetailedHallDTO detailedHallDTO = DetailedHallDTO.builder()
            .description("Hall1")
            .location(locationDTO)
            .build();

        List<SimpleArtistDTO> artists = new ArrayList<>();

        artists.add(SimpleArtistDTO.builder()
            .id(1L)
            .artistFirstname(EVENT_ARTIST_FIRSTNAME)
            .artistLastName(EVENT_ARTIST_LASTNAME)
            .build());

        DetailedEventDTO detailedEventDTO = DetailedEventDTO.builder()
            .startOfEvent(start)
            .endOfEvent(start.plusHours(2))
            .artists(artists)
            .seatSelection(true)
            .price(123L)
            .description("Description")
            .title("Title")
            .hall(detailedHallDTO)
            .seatSelection(true)
            .build();

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));


        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT.value()));

        //detailedEventDTO = response.as(DetailedEventDTO.class);

        //System.out.println(detailedEventDTO.toString());

        // TODO: David Compare Objects

    }

}
