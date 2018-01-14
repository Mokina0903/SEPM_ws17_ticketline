package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;

public class TicketEndpointTest extends BaseIntegrationTest {

    private static final String TICKET_ENDPOINT = "/tickets";
    private static final String TICKET_EVENT_PATH = "/event";
    private static final String TICKET_CUSTOMER_PATH = "/customer";

    // TODO: Buy ticket Normal
    // TODO: Buy same Ticket twice
    // TODO: Remove reservation 30 min before
    // TODO: Reversal of Reservation
    // TODO: Sell book ticket
    // TODO: Reversal of Sold Ticket

    // TODO: Test privileges
    // TODO: post /tickets create ticket entry
    // TODO: get /tickets/customer/{customerId} Get information about ticket entries by customer
    // TODO: get /tickets/event/{eventId} Get information about ticket entries by event
    // TODO: get /tickets/event/{eventId}/{sector} Get number of ticket entries by event and sector
    // TODO: get /tickets/isBooked/{eventId}/{seatId} Check if seat is booked for the event
    // TODO: get /tickets/isFree/{eventId}/{sector} Search for free seats for event in sector
    // TODO: get /tickets/{pageIndex}/{ticketsPerPage} Get list of ticket entries
    // TODO: get /tickets/{ticketId} Get information about a specific ticket entry


    @Test
    public void createTicketasUser() {
        /*
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(DetailedNewsDTO.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
        */
    }

}
