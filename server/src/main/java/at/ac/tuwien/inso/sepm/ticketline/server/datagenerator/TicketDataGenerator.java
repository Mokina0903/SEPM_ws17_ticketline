package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
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
import java.util.ArrayList;
import java.util.List;

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

    public TicketDataGenerator(TicketRepository ticketRepository, EventRepository eventRepository, CustomerRepository customerRepository, SeatRepository seatRepository, InvoiceRepository invoiceRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.customerRepository = customerRepository;
        this.seatRepository = seatRepository;
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.faker = new Faker();
    }

    @PostConstruct
    private void generateTickets() {
        if (ticketRepository.count() > 0) {
            LOGGER.info("tickets already generated");
        } else {
            LOGGER.info("generating ticket entries");
            List<Event> events = eventRepository.findAll();

            for (Event event : events) {
                List<Seat> seats = seatRepository.findAllByHallId(event.getHall().getId());
                List<Customer> customers = customerRepository.findAll();
                event.setEventCategory(Event.EventCategory.Jazz); //verena: added vor testing, can be removed any time
                int numbTickets = faker.number().numberBetween(30, seats.size() - 1);

                // TODO (David) Reservation Date

                int ticketCNT = 1;

                while (0 < numbTickets && customers.size() > 0) {
                    // TODO: (David) reservationNR is ok?
                    long reservationNR = (LocalDate.now().getYear() % 100) * 100000000 + event.getId() * 10000000 + ticketCNT++;

                    Customer customer = customers.remove(faker.number().numberBetween(0, customers.size() - 1));

                    int numbTicketsCustomer = faker.number().numberBetween(1, (numbTickets < 10) ? numbTickets : 10);

                    boolean isPaid = (faker.number().numberBetween(0, 1) == 0 ? false : true);

                    List<Ticket> tickets = new ArrayList();
                    for (int i = 0; i < numbTicketsCustomer; i++) {
                        Seat seat = seats.remove(faker.number().numberBetween(0, seats.size() - 1));

                        // TODO: (David) Pricecalculation
                        long price = (int) ((seat.getSector() - 96) * event.getPrice());


                        Ticket ticket = Ticket.builder()
                            .customer(customer)
                            .event(event)
                            .seat(seat)
                            .price(price)
                            .isPaid(isPaid)
                            .reservationNumber(reservationNR)
                            .reservationDate(LocalDateTime.now())
                            .build();

                        LOGGER.debug("saving ticket {}", reservationNR);
                        ticketRepository.save(ticket);

                        numbTickets--;
                        tickets.add(ticket);
                    }

                    // TODO (David)

                    LOGGER.debug("saving tickets {}", tickets);

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
}
