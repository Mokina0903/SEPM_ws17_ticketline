package at.ac.tuwien.inso.sepm.ticketline.server.tests.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.BaseIntegrationTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.HALL_ID;
import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.LOCATION_ID;
import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestDTOs.defaultHall;
import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestDTOs.defaultLocation;
import static org.hamcrest.core.Is.is;

public class LocationEndpointTest extends BaseIntegrationTest {

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private HallRepository hallRepository;

    public final static String EVENTLOCATION_ENDPOINT = "/eventlocation/hall";
    public final static String EVENTLOCATION_ENDPOINT_HALLID = "/eventlocation/hall/{hallId} ";
    public final static String EVENTLOCATION_ENDPOINT_LOCATION = "/eventlocation/location";
    public final static String EVENTLOCATION_ENDPOINT_SEARCH =
        "/eventlocation/location/locationSearch/{pageIndex}/{locationsPerPage}";
    public final static String EVENTLOCATION_ENDPOINT_LOCATIONID = "/eventlocation/location/{locationId}";

    @Test
    public void findAllHallsAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(EVENTLOCATION_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllHallsAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENTLOCATION_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void findHallsbyIdAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(EVENTLOCATION_ENDPOINT_HALLID, HALL_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findHallsbyIdAsUser() {
        BDDMockito.doReturn(defaultHall()).when(hallRepository).findOne(HALL_ID);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENTLOCATION_ENDPOINT_HALLID, HALL_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void findAllLocationsAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(EVENTLOCATION_ENDPOINT_LOCATION)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllLocationsAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENTLOCATION_ENDPOINT_LOCATION)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void searchLocationsAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(EVENTLOCATION_ENDPOINT_SEARCH, 1, 1)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void searchAllLocationsAsUser() {
        // TODO: Returns Page
        /*
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENTLOCATION_ENDPOINT_SEARCH, 1, 1)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        */
    }

    @Test
    public void findLocationbyIdAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(EVENTLOCATION_ENDPOINT_LOCATIONID, LOCATION_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findLocationbyIdAsUser() {
        BDDMockito.doReturn(defaultLocation()).when(locationRepository).findOne(LOCATION_ID);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENTLOCATION_ENDPOINT_LOCATIONID, LOCATION_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }


}
