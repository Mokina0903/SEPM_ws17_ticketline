package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.SeatRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Profile("generateData")
@Component
public class LocationDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationDataGenerator.class);
    private static final int NUMBER_OF_LOCATIONS_TO_GENERATE = 10;

    private final LocationRepository locationRepository;
    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;
    private final Faker faker;

    public LocationDataGenerator(LocationRepository locationRepository, SeatRepository seatRepository, HallRepository hallRepository) {
        this.locationRepository = locationRepository;
        this.seatRepository = seatRepository;
        this.hallRepository = hallRepository;
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
    }
}
