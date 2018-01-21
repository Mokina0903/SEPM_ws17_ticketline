package at.ac.tuwien.inso.sepm.ticketline.server.tests.base;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.*;

public abstract class TestDTOs {

    public static SimpleEventDTO setUpSimpleEventDTO() {
        List<SimpleArtistDTO> artists = new ArrayList<>();

        artists.add(SimpleArtistDTO.builder()
            .id(ARTIST_ID)
            .artistFirstname(ARTIST_FIRSTNAME)
            .artistLastName(ARTIST_LASTNAME)
            .build());

        SimpleEventDTO simpleEventDTO = SimpleEventDTO.builder()
            .id(EVENT_ID)
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_START.plusHours(2))
            .artists(artists)
            .price(EVENT_PRICE)
            .title(EVENT_TITLE)
            .seatSelection(true)
            .category(EVENT_CATEGORY.name())
            .build();

        return simpleEventDTO;
    }

    public static DetailedEventDTO setUpDetailedEventDTO() {
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
            .category(EVENT_CATEGORY.name())
            .build();

        return detailedEventDTO;
    }

    public static CustomerDTO setUpCustomerDTO() {
        return CustomerDTO.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name(CUSTOMER_NAME)
            .surname(CUSTOMER_SURNAME)
            .mail(CUSTOMER_MAIL)
            .birthDate(CUSTOMER_BIRTHDATE)
            .build();
    }

    public static SeatDTO setUpSeatDTO() {
        return SeatDTO.builder()
            .id(SEAT_ID)
            // TODO: (David) what is with Hall here?
            /*
            .hall(DetailedHallDTO.builder()
                .id(HALL_ID)
                .description(HALL_DESCRIPTION)
                .location(DetailedLocationDTO.builder()
                    .id(LOCATION_ID)
                    .description(LOCATION_DESCRIPTION)
                    .build())
                .seats(new ArrayList<>())
                .build())
             */
            .nr(SEAT_NR)
            .sector(SEAT_SECTOR)
            .row(SEAT_ROW)
            .build();
    }

    public static List<TicketDTO> setUpTicketDTO() {
        CustomerDTO customer = TestDTOs.setUpCustomerDTO();

        SimpleEventDTO event = TestDTOs.setUpSimpleEventDTO();

        SeatDTO seat = TestDTOs.setUpSeatDTO();

        List<TicketDTO> ticketDTOList = new ArrayList<>();

        ticketDTOList.add(TicketDTO.builder()
            .id(TICKET_ID)
            .isDeleted(false)
            .isPaid(true)
            .price(TICKET_PRICE)
            .reservationNumber(TICKET_RESERVATIONNR)
            .customer(customer)
            .seat(seat)
            .event(event)
            .build());

        return ticketDTOList;
    }

    public static Customer defaultCustomer() {
        return Customer.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name(CUSTOMER_NAME)
            .surname(CUSTOMER_SURNAME)
            .mail(CUSTOMER_MAIL)
            .birthDate(CUSTOMER_BIRTHDATE)
            .build();
    }

    public static List<Seat> defaultSeats() {
        Seat[] seats = new Seat[] {
            defaultSeat(1,1,1),
            defaultSeat(2,2,1),
            defaultSeat(3,3,1),
            defaultSeat(4,4,1),
            defaultSeat(5,5,1)
        };

        return Arrays.asList(seats);
    }

    public static Seat defaultSeat(long id, int nr, int row) {
        return Seat.builder()
            .id(id)
            .nr(nr)
            .row(row)
            .sector('a')
            .build();
    }

    public static Hall defaultHall() {
        return Hall.builder()
            .id(HALL_ID)
            .description(HALL_DESCRIPTION)
            .location(
                Location.builder()
                    .id(LOCATION_ID)
                    .description(LOCATION_DESCRIPTION)
                    .country(LOCATION_COUNTRY)
                    .city(LOCATION_CITY)
                    .zip(LOCATION_ZIP)
                    .street(LOCATION_STREET)
                    .houseNr(LOCATION_HOUSENR)
                    .eventHalls(Collections.emptyList())
                    .build()
            )
            .seats(defaultSeats())
            .build();
    }

    public static Location defaultLocation() {
        Location location = Location.builder()
            .id(LOCATION_ID)
            .description(LOCATION_DESCRIPTION)
            .country(LOCATION_COUNTRY)
            .city(LOCATION_CITY)
            .zip(LOCATION_ZIP)
            .street(LOCATION_STREET)
            .houseNr(LOCATION_HOUSENR)
            .eventHalls(Collections.singletonList(defaultHall()))
            .build();

        return location;
    }

    public static CustomerDTO defaultCustomerDTO() {
        return CustomerDTO.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name(CUSTOMER_NAME)
            .surname(CUSTOMER_SURNAME)
            .mail(CUSTOMER_MAIL)
            .birthDate(CUSTOMER_BIRTHDATE)
            .version(1)
            .build();
    }

    public static Artist defaultArtist() {
        return Artist.builder()
            .id(ARTIST_ID)
            .artistFirstname(ARTIST_FIRSTNAME)
            .artistLastName(ARTIST_LASTNAME)
            .build();
    }

    public static Event defaultEvent() {
        return Event.builder()
            .id(EVENT_ID)
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_START.plusHours(2))
            .artists(Collections.singletonList(defaultArtist()))
            .category(EVENT_CATEGORY)
            .price(EVENT_PRICE)
            .description(EVENT_DESCRIPTION)
            .title(EVENT_TITLE)
            .hall(defaultHall())
            .seatSelection(true)
            .build();
    }

    public static Ticket defaultTicket(long ID){
        return Ticket.builder()
            .isDeleted(false)
            .id(ID)
            .seat(defaultSeat(5L, 3, 5))
            .reservationNumber(45633565L)
            .isPaid(true)
            .event(defaultEvent())
            .customer(defaultCustomer())
            .price(5738L)
            .reservationDate(LocalDateTime.of(2020, 4, 12, 4, 59))
            .build();
    }

}
