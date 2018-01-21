package at.ac.tuwien.inso.sepm.ticketline.server.tests.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestDTOs;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.*;
import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestDTOs.defaultEvent;
import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestDTOs.defaultTicket;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;

public class TicketEndpointTest extends BaseIntegrationTest {

    // TODO: (David) Change to IntegrationTest
    // TODO: Remove reservation 30 min before
    // TODO: Reversal of Reservation
    // TODO: Sell book ticket
    // TODO: Reversal of Sold Ticket

    // TODO: Test privileges
    // TODO: post /tickets create ticket entry
    // Done: get /tickets/customer/{customerId} Get information about ticket entries by customer
    // Done: get /tickets/event/{eventId} Get information about ticket entries by event
    // Done: get /tickets/event/{eventId}/{sector} Get number of ticket entries by event and sector
    // Done: get /tickets/isBooked/{eventId}/{seatId} Check if seat is booked for the event
    // Done: get /tickets/isFree/{eventId}/{sector} Search for free seats for event in sector
    // TODO: get /tickets/{pageIndex}/{ticketsPerPage} Get list of ticket entries
    // Done: get /tickets/{ticketId} Get information about a specific ticket entry

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private TicketRepository ticketRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @Before
    public void setUp() {

    }

    @Test
    public void loadCustomerAsAnonym(){
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(TestDTOs.defaultTicket(5L));
        BDDMockito.given(ticketRepository.findByCustomer_Id(4L)).willReturn(ticketList);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            //.header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TICKET_CUSTOMER, 4L)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void loadCustomerAsUser(){
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(TestDTOs.defaultTicket(5L));
        BDDMockito.given(ticketRepository.findByCustomer_Id(4L)).willReturn(ticketList);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TICKET_CUSTOMER, 4L)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void loadCustomerAsAdmin(){
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(TestDTOs.defaultTicket(5L));
        BDDMockito.given(ticketRepository.findByCustomer_Id(4L)).willReturn(ticketList);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(TICKET_CUSTOMER, 4L)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void loadEventAsAdmin(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(TICKET_EVENT, 4L)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void loadEventAsUser(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TICKET_EVENT, 4L)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void loadEventAsAnonym(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(TICKET_EVENT, 4L)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void loadEventSectorAsAdmin(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(TICKET_EVENT_SECTOR, 4L, 'c')
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void loadEventSectorAsUser(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TICKET_EVENT_SECTOR, 4L, 'c')
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void loadEventSectorAsAnonym(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(TICKET_EVENT_SECTOR, 4L, 'c')
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void isBlockedEventSeatAsAdmin(){
        Optional<Ticket> ticketNull = Optional.ofNullable(defaultTicket(5L));

        BDDMockito.given(ticketRepository.findByEvent_idAndSeat_idAndIsDeletedFalse(any(),any())).willReturn(((ticketNull)));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(TICKET_EVENT_SEAT, 4L, 5L)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void isBlockedEventSeatAsUser(){
        Optional<Ticket> ticketNull = Optional.ofNullable(defaultTicket(5L));

        BDDMockito.given(ticketRepository.findByEvent_idAndSeat_idAndIsDeletedFalse(any(),any())).willReturn(((ticketNull)));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TICKET_EVENT_SEAT, 4L, 5L)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void isBlockedEventSeatAsAnonym(){
        Optional<Ticket> ticketNull = Optional.ofNullable(defaultTicket(5L));

        BDDMockito.given(ticketRepository.findByEvent_idAndSeat_idAndIsDeletedFalse(any(),any())).willReturn(((ticketNull)));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(TICKET_EVENT_SEAT, 4L, 5L)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void isFreeEventSectorAsAdmin(){

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(TICKET_FREE, 4L, 'c')
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void isFreeEventSectorAsUser(){

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TICKET_FREE, 4L, 'c')
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void isFreeEventSectorAsAnonym(){

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(TICKET_FREE, 4L, 'c')
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void ticketTicketAsAdmin(){
        BDDMockito.given(ticketRepository.findOneById(any())).willReturn(Optional.of(((defaultTicket(5L)))));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(TICKET_TICKET, 4L)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void ticketTicketAsUser(){
        BDDMockito.given(ticketRepository.findOneById(any())).willReturn(Optional.of(((defaultTicket(5L)))));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(TICKET_TICKET, 4L)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void ticketTicketAsAnonym(){
        BDDMockito.given(ticketRepository.findOneById(any())).willReturn(Optional.of(((defaultTicket(5L)))));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(TICKET_TICKET, 4L)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void paginationTicketAsAdmin(){

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(TICKET_PAGE, 1, 4)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }



































    @Test
    public void loseReservationAfterTime() {
        //setUpDefaultEvent(LocalDateTime.now().plusMinutes(20));

        List<TicketDTO> ticketDTOList = TestDTOs.setUpTicketDTO();

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketDTOList)
            .when().post(TICKET_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        System.out.println(response.asString());

        //Assert.assertThat(ticketRepository.findAll().size(),is(1));


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
        //setUpDefaultEvent();

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


    @Test
    public void createTicketAsUser() {
        Optional<Ticket> ticketNull = null;
        BDDMockito.given(eventRepository.findOneById(EVENT_ID)).willReturn(
            java.util.Optional.ofNullable(defaultEvent())
        );
        BDDMockito.given(ticketRepository.save(any(Ticket.class))).willReturn((defaultTicket(593L))
        );
        BDDMockito.given(ticketRepository.findByEvent_idAndSeat_idAndIsDeletedFalse(any(),any())).willReturn(((ticketNull)));

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
        //setUpDefaultEvent();

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

}
