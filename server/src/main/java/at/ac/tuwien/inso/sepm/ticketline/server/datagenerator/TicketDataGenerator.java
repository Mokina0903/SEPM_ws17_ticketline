package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Invoice;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.SeatRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Profile("generateData")
@Component
@Transactional
public class TicketDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketDataGenerator.class);
    private static final int NUMBER_OF_TICKETSPEREVENT_TO_GENERATE = 50;

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final CustomerRepository customerRepository;
    private final SeatRepository seatRepository;
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final Faker faker;

    public TicketDataGenerator( TicketRepository ticketRepository, EventRepository eventRepository, CustomerRepository customerRepository, SeatRepository seatRepository, InvoiceRepository invoiceRepository, UserRepository userRepository ) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.customerRepository = customerRepository;
        this.seatRepository = seatRepository;
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.faker = new Faker();
    }

    @PostConstruct
    private void generateNews() {
        if (ticketRepository.count() > 0) {
            LOGGER.info("tickets already generated");
        } else {
            LOGGER.info("generating {} ticket entries", NUMBER_OF_TICKETSPEREVENT_TO_GENERATE*eventRepository.count());
            for (Event event: eventRepository.findAll()) {
                List<Seat> seats= seatRepository.findAllByHallId(event.getHall().getId());

                List<Ticket> tickets=new ArrayList<>();

                for (int i=0;i<NUMBER_OF_TICKETSPEREVENT_TO_GENERATE;i++){
                    long reservationNR = (LocalDate.now().getYear()%100)*100000000 +event.getId();

                    Ticket ticket= Ticket.builder()
                        .customer(customerRepository.findAll().get(1))
                        .event(event)
                        .seat(seats.remove(0))
                        .price(faker.number().numberBetween(event.getPrice(),event.getPrice()*2))
                        .isPaid(true)
                        .reservationNumber(reservationNR)
                        .reservationDate(
                            LocalDateTime.ofInstant(
                                faker.date().past(100, TimeUnit.DAYS).toInstant(),
                                ZoneId.systemDefault()))
                        .build();

                    tickets.add(ticket);

                }
                LOGGER.debug("saving tickets {}", tickets);
                ticketRepository.save(tickets);

                Ticket ticket = tickets.get(0);

                Invoice invoice = new Invoice.InvoiceBuilder()
                    .invoiceDate(ticket.getReservationDate())
                    .invoiceNumber(ticket.getReservationNumber())
                    .customer(ticket.getCustomer())
                    .isStorno(false)
                    .vendor(userRepository.findOne(1l))
                    .tickets(tickets)
                    .build();

                LOGGER.debug("saving invoice {}", tickets);
                invoiceRepository.save(invoice);
            }
        }
    }
}
