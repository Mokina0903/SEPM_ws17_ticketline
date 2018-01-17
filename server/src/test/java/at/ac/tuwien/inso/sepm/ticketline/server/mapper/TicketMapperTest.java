package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.seat.SeatMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class TicketMapperTest {

//ToDo Hall und Seat nullpointer

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class SeatMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private TicketMapper ticketMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private EventMapper eventMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private SeatMapper seatMapper;

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private CustomerMapper customerMapper;

    //private static final Customer CUSTOMER = new Customer();
    private static final CustomerDTO CUSTOMERDTO = new CustomerDTO();
    //private static final Event EVENT = new Event();
    private static final SimpleEventDTO EVENTDTO = new SimpleEventDTO();
    private static final Boolean PAID = false;
    private static final Boolean DELETED = true;
    private static final Long PRICE = 13456L;
    private static final Long NUMBER = 43L;
    private static final Boolean PAID1 = true;
    private static final Boolean DELETED1 = true;
    private static final Integer PRICE1 = 134562;
    private static final Long NUMBER1 = 243L;
    //private static final Seat SEAT = new Seat();
    private static final SeatDTO SEATDTO = new SeatDTO();
    private static final Long ID = 1L;
    private static final Long ID1 = 2L;

    private final static Location LOCATION = Location.builder()
        .id(1L)
        .description("Description")
        .country("Country")
        .city("City")
        .zip(1234)
        .street("Street")
        .houseNr(1)
        .build();

    private static final Hall HALL = Hall.builder()
        .id(1L)
        .description("Description of the hall")
        .location(LOCATION)
        .build();

    private static final Customer CUSTOMER = Customer.builder()
        .id(1L)
        .knr(3L)
        .name("Max")
        .surname("Mustermann")
        .mail("mustermann@gmail.com")
        .birthDate(LocalDate.of(1997, 1, 1))
        .build();

    private static final String EVENT_DESCRIPTION_SUMMARY = "This is a very long text containing all the";

    private static final String EVENT_ARTIST_FIRSTNAME = "Firstname";
    private static final String EVENT_ARTIST_LASTNAME = "Lastname";
    private static final Artist EVENT_ARTIST =  Artist.builder()
        .id(1L)
        .artistFirstname(EVENT_ARTIST_FIRSTNAME)
        .artistLastName(EVENT_ARTIST_LASTNAME)
        .build();
    private List<Artist> EVENT_ARTISTS = new ArrayList<>();


    private Event EVENT = Event.builder()
        .endOfEvent(LocalDateTime.of(2016, 1, 1, 12, 0, 0, 0))
        .startOfEvent(LocalDateTime.of(2016, 1, 1, 10, 0, 0, 0))
        .description("spektakel")
        .price(394L)
        .title("gitarre")
        .id(2L)
        .seatSelection(true)
        .artists(EVENT_ARTISTS)
        .hall(HALL)
        .build();

    private Seat SEAT = Seat.builder()
        .hall(HALL)
        .nr(2)
        .row(4)
        .sector('A')
        .id(5L)
        .build();




    @Test
    public void shouldMapTicketToTicketDTO() {

        Ticket ticket = Ticket.builder()
            .customer(CUSTOMER)
            .event(EVENT)
            .isPaid(PAID)
            .price(PRICE)
            .reservationNumber(NUMBER)
            .seat(SEAT)
            .id(ID)
            .isDeleted(DELETED)
            .build();

        TicketDTO ticketDTO = ticketMapper.ticketToTicketDTO(ticket);

        SimpleEventDTO event = eventMapper.eventToSimpleEventDTO(EVENT);

        List<Seat> seatList = new ArrayList<>();
        seatList.add(SEAT);
        List<SeatDTO> seatDTO = seatMapper.seatToSeatDTO(seatList);
        SeatDTO seat = seatDTO.get(0);

        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(CUSTOMER);

        assertThat(ticketDTO).isNotNull();
        assertThat(ticketDTO.getId()).isEqualTo(ID);
        assertThat(ticketDTO.getPrice()).isEqualTo(PRICE);
        assertThat(ticketDTO.getReservationNumber()).isEqualTo(NUMBER);
        //assertThat(ticketDTO.getEvent()).isEqualTo(event);
        assertThat(ticketDTO.getSeat()).isEqualTo(seat);
        assertThat(ticketDTO.getCustomer()).isEqualTo(customerDTO);

    }

    @Test
    public void shouldMapTicketDTOToTicket() {

        List<Seat> seatList = new ArrayList<>();
        seatList.add(SEAT);
        List<SeatDTO> seatDTO = seatMapper.seatToSeatDTO(seatList);
        SeatDTO seat = seatDTO.get(0);

        SimpleEventDTO event = eventMapper.eventToSimpleEventDTO(EVENT);

        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(CUSTOMER);

        TicketDTO ticketDTO = TicketDTO.builder()
            .customer(customerDTO)
            .event(event)
            .isPaid(PAID)
            .price(PRICE)
            .reservationNumber(NUMBER)
            .seat(seat)
            .id(ID)
            .isDeleted(DELETED)
            .build();

        Ticket ticket = ticketMapper.ticketDTOtoTicket(ticketDTO);




        assertThat(ticket).isNotNull();
        assertThat(ticket.getId()).isEqualTo(ID);
        assertThat(ticket.getPrice()).isEqualTo(PRICE);
        assertThat(ticket.getReservationNumber()).isEqualTo(NUMBER);
        //assertThat(ticket.getEvent()).isEqualTo(EVENT);
        //assertThat(ticket.getSeat()).isEqualTo(SEAT);
        assertThat(ticket.getCustomer()).isEqualTo(CUSTOMER);

    }


    @Test
    public void shouldMapTicketListToTicketDTO() {

        Ticket ticket = Ticket.builder()
            .customer(CUSTOMER)
            .event(EVENT)
            .isPaid(PAID)
            .price(PRICE)
            .reservationNumber(NUMBER)
            .seat(SEAT)
            .id(ID)
            .isDeleted(DELETED)
            .build();
        Ticket ticket2 = Ticket.builder()
            .customer(CUSTOMER)
            .event(EVENT)
            .isPaid(PAID1)
            .price(PRICE)
            .reservationNumber(NUMBER1)
            .seat(SEAT)
            .id(ID1)
            .isDeleted(DELETED)
            .build();

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket);
        ticketList.add(ticket2);
        List<TicketDTO> ticketDTOList = ticketMapper.ticketToTicketDTO(ticketList);

        SimpleEventDTO event = eventMapper.eventToSimpleEventDTO(EVENT);

        List<Seat> seatList = new ArrayList<>();
        seatList.add(SEAT);
        List<SeatDTO> seatDTO = seatMapper.seatToSeatDTO(seatList);
        SeatDTO seat = seatDTO.get(0);

        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(CUSTOMER);


        TicketDTO ticketDTO = ticketDTOList.get(0);
        assertThat(ticketDTO).isNotNull();
        assertThat(ticketDTO.getId()).isEqualTo(ID);
        assertThat(ticketDTO.getPrice()).isEqualTo(PRICE);
        assertThat(ticketDTO.getReservationNumber()).isEqualTo(NUMBER);
        //assertThat(ticketDTO.getEvent()).isEqualTo(event);
        assertThat(ticketDTO.getSeat()).isEqualTo(seat);
        assertThat(ticketDTO.getCustomer()).isEqualTo(customerDTO);

        ticketDTO = ticketDTOList.get(1);
        assertThat(ticketDTO).isNotNull();
        assertThat(ticketDTO.getId()).isEqualTo(ID1);
        assertThat(ticketDTO.getPrice()).isEqualTo(PRICE1);
        assertThat(ticketDTO.getReservationNumber()).isEqualTo(NUMBER1);
        //assertThat(ticketDTO.getEvent()).isEqualTo(event);
        assertThat(ticketDTO.getSeat()).isEqualTo(seat);
        assertThat(ticketDTO.getCustomer()).isEqualTo(customerDTO);

    }

    @Test
    public void shouldMapTicketListDTOToTicket() {

        List<Seat> seatList = new ArrayList<>();
        seatList.add(SEAT);
        List<SeatDTO> seatDTO = seatMapper.seatToSeatDTO(seatList);
        SeatDTO seat = seatDTO.get(0);

        SimpleEventDTO event = eventMapper.eventToSimpleEventDTO(EVENT);

        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(CUSTOMER);

        TicketDTO ticketDTO = TicketDTO.builder()
            .customer(customerDTO)
            .event(event)
            .isPaid(PAID)
            .price(PRICE)
            .reservationNumber(NUMBER)
            .seat(seat)
            .id(ID)
            .isDeleted(DELETED)
            .build();
        TicketDTO ticketDTO2 = TicketDTO.builder()
            .customer(customerDTO)
            .event(event)
            .isPaid(PAID1)
            .price(PRICE)
            .reservationNumber(NUMBER1)
            .seat(seat)
            .id(ID1)
            .isDeleted(DELETED)
            .build();

        List<TicketDTO> ticketDTOList = new ArrayList<>();
        ticketDTOList.add(ticketDTO);
        ticketDTOList.add(ticketDTO2);
        List<Ticket> ticketList = ticketMapper.ticketDTOToTicket(ticketDTOList);



        Ticket ticket = ticketList.get(0);
        assertThat(ticket).isNotNull();
        assertThat(ticket.getId()).isEqualTo(ID);
        assertThat(ticket.getPrice()).isEqualTo(PRICE);
        assertThat(ticket.getReservationNumber()).isEqualTo(NUMBER);
        //assertThat(ticket.getEvent()).isEqualTo(event);
        //assertThat(ticket.getSeat()).isEqualTo(SEAT);
        assertThat(ticket.getCustomer()).isEqualTo(CUSTOMER);

        ticket = ticketList.get(1);
        assertThat(ticket).isNotNull();
        assertThat(ticket.getId()).isEqualTo(ID1);
        assertThat(ticket.getPrice()).isEqualTo(PRICE1);
        assertThat(ticket.getReservationNumber()).isEqualTo(NUMBER1);
        //assertThat(ticket.getEvent()).isEqualTo(event);
        //assertThat(ticket.getSeat()).isEqualTo(SEAT);
        assertThat(ticket.getCustomer()).isEqualTo(CUSTOMER);

    }


}
