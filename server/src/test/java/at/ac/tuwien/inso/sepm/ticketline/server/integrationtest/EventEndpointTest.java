package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
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



    private DetailedEventDTO setUpDetailedEventDTO() {
        DetailedLocationDTO locationDTO = DetailedLocationDTO.builder()
            .id(LOCATION_ID)
            .description(LOCATION_DESCRIPTION)
            .build();

        DetailedHallDTO detailedHallDTO = DetailedHallDTO.builder()
            .id(HALL_ID)
            .description(HALL_DESCRIPTION)
            .location(locationDTO)
            .seats(new ArrayList<>())
            .build();

        List<SimpleArtistDTO> artists = new ArrayList<>();

        artists.add(SimpleArtistDTO.builder()
            .id(ARTIST_ID)
            .artistFirstname(ARTIST_FIRSTNAME)
            .artistLastName(ARTIST_LASTNAME)
            .build());

        DetailedEventDTO detailedEventDTO = DetailedEventDTO.builder()
            .id(EVENT_ID)
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_START.plusHours(2))
            .artists(artists)
            .price(EVENT_PRICE)
            .description(EVENT_DESCRIPTION)
            .title(EVENT_TITLE)
            .hall(detailedHallDTO)
            .seatSelection(true)
            .build();

        return detailedEventDTO;
    }

    @Test
    public void publishEventUnauthorizedAsUser() {
        DetailedEventDTO detailedEventDTO = setUpDetailedEventDTO();

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
        DetailedEventDTO detailedEventDTO = setUpDetailedEventDTO();

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
    public void publishEventAsAdminHallNotFound(){
        DetailedEventDTO detailedEventDTO = setUpDetailedEventDTO();

        detailedEventDTO.getHall().setDescription("Wrong Hall");

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
    public void publishEventAsAdminLocationNotFound(){
        DetailedEventDTO detailedEventDTO = setUpDetailedEventDTO();

        detailedEventDTO.getHall().getLocation().setDescription("Wrong Location");

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
    public void publishEventAsAdminNewArtist(){
        DetailedEventDTO detailedEventDTO = setUpDetailedEventDTO();

        detailedEventDTO.getArtists().get(0).setArtistFirstName(ARTIST_FIRSTNAME + " NEW");

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertThat(artistRepository.findAll().size(),is(2));
    }

    @Test
    public void publishEventAsAdminEventDuplicate(){
        DetailedEventDTO detailedEventDTO = setUpDetailedEventDTO();

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        detailedEventDTO.setId(99L);

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT.value()));

    }

}
