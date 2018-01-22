package at.ac.tuwien.inso.sepm.ticketline.server.tests.unittest;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.BaseTestUnit;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestDTOs;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.ARTIST_FIRSTNAME;
import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.EVENT_START;
import static org.hamcrest.core.Is.is;

public class EventTest extends BaseTestUnit {
    private static final String EVENT_ENDPOINT = "/event";

    @Before
    public void setUp() {
        setUpDefaultEvent();
    }

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
    public void publishEventAsAdmin() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

        detailedEventDTO.setDescription(detailedEventDTO.getDescription() + " NEW");

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
    public void publishEventAsAdminHallNotFound() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

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
    public void publishEventAsAdminLocationNotFound() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

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
    public void publishEventAsAdminNewArtist() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

        detailedEventDTO.getArtists().get(0).setArtistFirstName(ARTIST_FIRSTNAME + " NEW");

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertThat(artistRepository.findAll().size(), is(2));
    }

    @Test
    public void publishEventAsAdminEventDuplicate() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT.value()));
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
    }

    @Test
    public void publishEventAsAdminNoCategory() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

        detailedEventDTO.setEventCategory(null);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_ACCEPTABLE.value()));
    }

    @Test
    public void publishEventAsAdminWrongCategory() {
        DetailedEventDTO detailedEventDTO = TestDTOs.setUpDetailedEventDTO();

        detailedEventDTO.setEventCategory("New Category");

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(detailedEventDTO)
            .when().post(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

}
