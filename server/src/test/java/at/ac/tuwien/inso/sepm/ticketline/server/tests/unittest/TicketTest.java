package at.ac.tuwien.inso.sepm.ticketline.server.tests.unittest;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
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

import java.time.LocalDateTime;
import java.util.List;

import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.TICKET_ENDPOINT;
import static org.hamcrest.core.Is.is;

public class TicketTest extends BaseTestUnit {

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

    @Before
    public void setUp() {

    }

    @Test
    public void createTicketAsUser() {
        setUpDefaultEvent();

        List<TicketDTO> ticketDTOList = TestDTOs.setUpTicketDTO();

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketDTOList)
            .when().post(TICKET_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void createTwoTicketsAsUser() {
        setUpDefaultEvent();

        List<TicketDTO> ticketDTOList = TestDTOs.setUpTicketDTO();

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketDTOList)
            .when().post(TICKET_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketDTOList)
            .when().post(TICKET_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT.value()));
    }

    @Test
    public void loseReservationAfterTime() {
        setUpDefaultEvent(LocalDateTime.now().plusMinutes(20));

        List<TicketDTO> ticketDTOList = TestDTOs.setUpTicketDTO();

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketDTOList)
            .when().post(TICKET_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertThat(ticketRepository.findAll().size(),is(1));


        // TODO: Implement here
        /*
        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketDTOList)
            .when().post(TICKET_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));


        List<TicketDTO> ticketDTOListNew = response.as(ArrayList.class);
        */

    }

    @Test
    public void wrongPrice() {
        // TODO: (David) Edit this
        setUpDefaultEvent();

        List<TicketDTO> ticketDTOList = TestDTOs.setUpTicketDTO();

        ticketDTOList.get(0).setPrice(123L);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketDTOList)
            .when().post(TICKET_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));


    }

    @Test
    public void calculatePrice() {
        Event event = Event.builder()
            .price(10000L)
            .build();

        Seat seat = Seat.builder()
            .build();

        Ticket ticket = Ticket.builder()
            .seat(seat)
            .event(event)
            .build();


        seat.setSector((char) 96);
        Assert.assertThat(ticket.calculatePrice(),is(10000L));

        seat.setSector('a');
        Assert.assertThat(ticket.calculatePrice(),is(10000L));

        seat.setSector('b');
        Assert.assertThat(ticket.calculatePrice(),is(12000L));

        seat.setSector('c');
        Assert.assertThat(ticket.calculatePrice(),is(14000L));

        seat.setSector('d');
        Assert.assertThat(ticket.calculatePrice(),is(16000L));

        seat.setSector('e');
        Assert.assertThat(ticket.calculatePrice(),is(18000L));

        seat.setSector('f');
        Assert.assertThat(ticket.calculatePrice(),is(20000L));
    }
}
