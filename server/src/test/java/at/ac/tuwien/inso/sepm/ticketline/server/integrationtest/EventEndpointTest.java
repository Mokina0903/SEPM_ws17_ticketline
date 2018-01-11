package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class EventEndpointTest extends BaseIntegrationTest {
    private static final String EVENT_ENDPOINT = "/event";

    @Test
    public void publishEventUnauthorizedAsUser() {
        setupDefaultLocation();

        DetailedLocationDTO locationDTO = DetailedLocationDTO.builder()
            .description(LOCATION_DESCRIPTION)
            .build();

        DetailedHallDTO detailedHallDTO = DetailedHallDTO.builder()
            .description(HALL_DESCRIPTION)
            .location(locationDTO)
            .build();

        List<SimpleArtistDTO> artists = new ArrayList<>();

        artists.add(SimpleArtistDTO.builder()
            .id(1L)
            .artistFirstname(ARTIST_FIRSTNAME)
            .artistLastName(ARTIST_LASTNAME)
            .build());

        DetailedEventDTO detailedEventDTO = DetailedEventDTO.builder()
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_START.plusHours(2))
            .artists(artists)
            .price(EVENT_PRICE)
            .description(EVENT_DESCRIPTION)
            .title(EVENT_TITLE)
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

        DetailedLocationDTO locationDTO = DetailedLocationDTO.builder()
            .id(LOCATION_ID)
            .description(LOCATION_DESCRIPTION)
            .build();

        DetailedHallDTO detailedHallDTO = DetailedHallDTO.builder()
            .id(HALL_ID)
            .description(HALL_DESCRIPTION)
            .location(locationDTO)
            .build();

        List<SimpleArtistDTO> artists = new ArrayList<>();

        artists.add(SimpleArtistDTO.builder()
            .id(ARTIST_ID)
            .artistFirstname(ARTIST_FIRSTNAME)
            .artistLastName(ARTIST_LASTNAME)
            .build());

        DetailedEventDTO detailedEventDTO = DetailedEventDTO.builder()
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_START.plusHours(2))
            .artists(artists)
            .price(EVENT_PRICE)
            .description(EVENT_DESCRIPTION)
            .title(EVENT_TITLE)
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

        detailedEventDTO = response.as(DetailedEventDTO.class);

        System.out.println(detailedEventDTO.toString());


        Assert.assertThat(detailedEventDTO, is(
            DetailedEventDTO.builder()
                .id(EVENT_ID)
                .startOfEvent(EVENT_START)
                .endOfEvent(EVENT_START.plusHours(2))
                .artists(artists)
                .price(EVENT_PRICE)
                .description(EVENT_DESCRIPTION)
                .title(EVENT_TITLE)
                .hall(detailedHallDTO)
                .seatSelection(true)
                .build()));

        //

        // TODO: David Compare Objects

    }

    // TODO: Test for Normal
    // TODO: Test for Hall not Found
    // TODO: Test for Location not Found
    // TODO: Test for new Artist
    // TODO: Test with duplicates
}
