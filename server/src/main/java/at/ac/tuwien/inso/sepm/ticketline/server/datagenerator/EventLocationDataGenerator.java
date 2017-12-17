package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.SeatRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Profile("generateData")
@Component
public class EventLocationDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLocationDataGenerator.class);
    private static final int NUMBER_OF_LOCATIONS_TO_GENERATE = 10;
    private static final int NUMBER_OF_EVENTS_TO_GENERATE = 50;

    private final LocationRepository locationRepository;
    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;
    private final EventRepository eventRepository;
    private final Faker faker;

    public EventLocationDataGenerator( LocationRepository locationRepository, SeatRepository seatRepository,
                                       HallRepository hallRepository, EventRepository eventRepository) {
        this.locationRepository = locationRepository;
        this.seatRepository = seatRepository;
        this.hallRepository = hallRepository;
        this.eventRepository = eventRepository;
        faker = new Faker();
    }


    @PostConstruct
    private void generateLocations() {
        if (locationRepository.count() > 0) {
            LOGGER.info("locations already generated");
        } else {
            LOGGER.info("generating {} location entries", NUMBER_OF_LOCATIONS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_LOCATIONS_TO_GENERATE; i++) {
                List<Hall> halls = new ArrayList<>();

                Location location = Location.builder()
                    .description(faker.lorem().characters(25, 100))
                    .city(faker.lorem().characters(4, 10))
                    .country("Austria")
                    .houseNr(faker.number().numberBetween(1, 150))
                    .street(faker.lorem().characters(3, 7) + "street")
                    .zip(faker.number().numberBetween(1000, 10000))
                    .build();

                LOGGER.debug("saving location {}", location);
                location = locationRepository.save(location);

                for (int h = 0; h < 5; h++) {

                    List<Seat> seats = new ArrayList<>();

                    Hall hall = Hall.builder()
                        .description(faker.lorem().characters(25, 100))
                        .location(location)
                        .build();
                    LOGGER.debug("saving hall {}",hall);
                    hall = hallRepository.save(hall);

                    for (int j = 0; j < 100; j++) {
                        Seat seat = Seat.builder()
                            .nr(j)
                            .row(j / 10)
                            .sector('A')
                            .hall(hall)
                            .build();
                        LOGGER.debug("saving seat {}",seat);
                        seat = seatRepository.save(seat);
                        seats.add(seat);
                    }

                    hall.setSeats(seats);
                    LOGGER.debug("saving hall {}",hall);
                    hall = hallRepository.save(hall);
                    halls.add(hall);
                }

                location.setEventHalls(halls);

                LOGGER.debug("saving location {}", location);
                locationRepository.save(location);

            }
        }
        if (eventRepository.count() > 0) {
            LOGGER.info("events already generated");
        } else {
            LOGGER.info("generating {} event entries", NUMBER_OF_EVENTS_TO_GENERATE);

            for (int i = 0; i < NUMBER_OF_EVENTS_TO_GENERATE; i++) {

                LocalDateTime start=LocalDateTime.ofInstant(
                    faker.date()
                        .between(faker.date().past(365, TimeUnit.DAYS),faker.date().future(365, TimeUnit.DAYS)).
                        toInstant(),
                    ZoneId.systemDefault()
                );

                Event event = Event.builder()
                    .startOfEvent(start)
                    .endOfEvent(start.plusHours(2))
                    .artistFirstname(faker.name().firstName())
                    .artistLastName(faker.name().lastName())
                    .price(faker.number().numberBetween(5L,100L))
                    .description(faker.lorem().paragraph())
                    .title(faker.lorem().characters(20,100))
                    .hall(hallRepository.findAll().get(faker.number().numberBetween(0,(int)hallRepository.count())))
                    .build();
                LOGGER.debug("saving event {}", event);
                eventRepository.save(event);
            }
        }
    }
}
