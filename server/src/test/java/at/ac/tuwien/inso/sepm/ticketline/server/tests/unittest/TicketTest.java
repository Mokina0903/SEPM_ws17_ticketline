package at.ac.tuwien.inso.sepm.ticketline.server.tests.unittest;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.BaseTestUnit;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestDTOs;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.*;
import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestDTOs.*;
import static org.hamcrest.core.Is.is;

public class TicketTest extends BaseTestUnit {

    @Autowired
    private EventMapper eventMapper;

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

        TicketDTO ticket = ticketDTOList.get(0);
        ticket.setReservationNumber(ticket.getReservationNumber()+1);
        ticket.setSeat(setUpSeatDTO().get(1));
        ticket.setId(ticket.getId()+1);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketDTOList)
            .when().post(TICKET_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        ticket = ticketDTOList.get(0);
        ticket.setReservationNumber(ticket.getReservationNumber()+2);
        ticket.setSeat(setUpSeatDTO().get(1));
        ticket.setId(ticket.getId()+2);

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

        Event event = setUpDefaultEvent(LocalDateTime.now().plusMinutes(10));

        CustomerDTO customer = TestDTOs.setUpCustomerDTO();

        SimpleEventDTO simpleEventDTO = eventMapper.eventToSimpleEventDTO(event) ;

        List<SeatDTO> seat = TestDTOs.setUpSeatDTO();

        List<TicketDTO> ticketDTOList = new ArrayList<>();

        ticketDTOList.add(TicketDTO.builder()
            .id(TICKET_ID)
            .isDeleted(false)
            .isPaid(true)
            .price(TICKET_PRICE)
            .reservationNumber(TICKET_RESERVATIONNR)
            .customer(customer)
            .seat(seat.get(0))
            .event(simpleEventDTO)
            .build());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(ticketDTOList)
            .when().post(TICKET_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        int tickets = ticketRepository.findAll().size();

        System.out.println(eventRepository.findAll().size());

        ticketRepository.deleteAlloldReservations(30L);

        Assert.assertThat(tickets,is(ticketRepository.findAll().size()));

    }

    @Test
    public void calculatePrice() {
        Event event = defaultEvent();

        event.setPrice(10000L);

        Seat seat = event.getHall().getSeats().get(0);

        Ticket ticket = Ticket.builder()
            .isDeleted(false)
            .id(TICKET_ID)
            .seat(seat)
            .reservationNumber(45633565L)
            .isPaid(true)
            .event(event)
            .customer(defaultCustomer())
            .price(5738L)
            .reservationDate(LocalDateTime.of(2020, 4, 12, 4, 59))
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
