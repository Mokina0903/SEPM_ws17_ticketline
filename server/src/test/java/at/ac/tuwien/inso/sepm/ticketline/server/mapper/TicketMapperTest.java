package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class TicketMapperTest {


    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class SeatMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private TicketMapper ticketMapper;


    private static final Customer CUSTOMER = new Customer();
    private static final CustomerDTO CUSTOMERDTO = new CustomerDTO();
    private static final Event EVENT = new Event();
    private static final SimpleEventDTO EVENTDTO = new SimpleEventDTO();
    private static final Boolean PAID = false;
    private static final Boolean DELETED = true;
    private static final Integer PRICE = 13456;
    private static final Long NUMBER = 43L;
    private static final Seat SEAT = new Seat();
    private static final SeatDTO SEATDTO = new SeatDTO();
    private static final Long ID = 1L;



/*
//TODo Problem mit Event einmal wird Event erwartet, aber EventDTO benötigt und umgekehrt
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


        assertThat(ticketDTO).isNotNull();
        assertThat(ticketDTO.getId()).isEqualTo(ID);
        assertThat(ticketDTO.getPrice()).isEqualTo(PRICE);
        assertThat(ticketDTO.getReservationNumber()).isEqualTo(NUMBER);
        assertThat(ticketDTO.getEvent()).isEqualTo(EVENT);
        assertThat(ticketDTO.getSeat()).isEqualTo(SEAT);
        assertThat(ticketDTO.getCustomer()).isEqualTo(CUSTOMER);

    }

    @Test
    public void shouldMapTicketDTOToTicket() {
        TicketDTO ticketDTO = TicketDTO.builder()
            .customer(CUSTOMERDTO)
            .event(EVENTDTO)
            .isPaid(PAID)
            .price(PRICE)
            .reservationNumber(NUMBER)
            .seat(SEATDTO)
            .id(ID)
            .isDeleted(DELETED)
            .build();

        Ticket ticket = ticketMapper.ticketDTOtoTicket(ticketDTO);


        assertThat(ticket).isNotNull();
        assertThat(ticket.getId()).isEqualTo(ID);
        assertThat(ticket.getPrice()).isEqualTo(PRICE);
        assertThat(ticket.getReservationNumber()).isEqualTo(NUMBER);
        assertThat(ticket.getEvent()).isEqualTo(EVENTDTO);
        assertThat(ticket.getSeat()).isEqualTo(SEATDTO);
        assertThat(ticket.getCustomer()).isEqualTo(CUSTOMERDTO);

    }
*/


}
