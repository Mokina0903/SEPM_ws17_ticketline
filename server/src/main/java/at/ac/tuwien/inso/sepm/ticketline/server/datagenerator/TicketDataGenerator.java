package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.TicketRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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
    private final Faker faker;

    public TicketDataGenerator( TicketRepository ticketRepository, EventRepository eventRepository, CustomerRepository customerRepository, SeatRepository seatRepository ) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.customerRepository = customerRepository;
        this.seatRepository = seatRepository;
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
                for (int i=0;i<NUMBER_OF_TICKETSPEREVENT_TO_GENERATE;i++){

                    Ticket ticket= Ticket.builder()
                        .customer(customerRepository.findAll().get(1))
                        .event(event)
                        .seat(seats.remove(0))
                        .price(faker.number().numberBetween((int)event.getPrice(),(int)event.getPrice()*2))
                        .build();

                    LOGGER.debug("saving ticket {}", ticket);
                    ticketRepository.save(ticket);

                }

            }
        }
    }
}
