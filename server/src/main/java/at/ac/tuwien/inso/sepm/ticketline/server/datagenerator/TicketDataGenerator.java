package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
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
    private final Faker faker;

    public TicketDataGenerator( TicketRepository ticketRepository, EventRepository eventRepository, CustomerRepository customerRepository, SeatRepository seatRepository ) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.customerRepository = customerRepository;
        this.seatRepository = seatRepository;
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
                int numbTickets = faker.number ().numberBetween(30,seats.size()-1);

                // TODO (David) Reservation Date

                int ticketCNT = 1;
                while (0 < numbTickets && customers.size() > 0) {
                    // TODO: (David) reservationNR is ok?
                    long reservationNR = (LocalDate.now().getYear()%100)*100000000 + event.getId()  *10000000  + ticketCNT++;

                    Customer customer = customers.remove(faker.number().numberBetween(0, customers.size()-1));

                    int numbTicketsCustomer = faker.number().numberBetween(1, (numbTickets < 10) ? numbTickets : 10);

                    boolean isPaid = (faker.number().numberBetween(0,1) == 0 ? false : true);

                    for (int i = 0; i < numbTicketsCustomer; i++) {
                        Seat seat = seats.remove(faker.number().numberBetween(0, seats.size() -1 ));

                        // TODO: (David) Pricecalculation
                        long price = (int) ((seat.getSector() - 96) * event.getPrice());


                        Ticket ticket= Ticket.builder()
                            .customer(customer)
                            .event(event)
                            .seat(seat)
                            .price(price)
                            .isPaid(isPaid)
                            .reservationNumber(reservationNR)
                            .build();

                        LOGGER.debug("saving ticket {}", reservationNR);
                        ticketRepository.save(ticket);

                        numbTickets--;
                    }
                }
            }
        }
    }
}
