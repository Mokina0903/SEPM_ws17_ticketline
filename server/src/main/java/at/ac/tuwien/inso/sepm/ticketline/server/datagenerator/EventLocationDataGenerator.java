package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
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
    private static final int NUMBER_OF_EVENTS_TO_GENERATE = 500;
    private static final int NUMBER_OF_ARTISTS = 20;
    private static final int NUMBER_OF_ARTISTS_PER_EVENT = 5;

    private final LocationRepository locationRepository;
    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;
    private final ArtistRepository artistRepository;
    private final EventRepository eventRepository;
    private final Faker faker;

    public EventLocationDataGenerator(LocationRepository locationRepository, SeatRepository seatRepository,
                                      HallRepository hallRepository, ArtistRepository artistRepository, EventRepository eventRepository) {
        this.locationRepository = locationRepository;
        this.seatRepository = seatRepository;
        this.hallRepository = hallRepository;
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
        faker = new Faker();
    }


    @PostConstruct
    private void generateLocations() {
        if (locationRepository.count() > 0) {
            LOGGER.info("locations already generated");
        } else {
            Location[] locationArray = new Location[]{
                new Location("Stadthalle Wien", "Austria", "Wien", 1150, "Roland-Rainer-Platz", 1),
                new Location("TipsArena Linz", "Austria", "Linz", 4020, "Ziegeleistrasse", 76),
                new Location("GLOBE WIEN", "Austria", "Wien", 1030, "Karl Farkas Gasse", 19),
                new Location("MuseumsQuartier Wien", "Austria", "Wien", 1070, "Museumsplatz", 1),
                new Location("Rabenhof Theater Wien", "Austria", "Wien", 1030, "Rabengasse", 3),
                new Location("Arena Wien", "Austria", "Wien", 1030, "Baumgasse", 80),
                new Location("Gasometer Wien", "Austria", "Wien", 1110, "Guglgasse", 8),
                new Location("Wiener Konzerthaus", "Austria", "Wien", 1030, "Lothringerstraße", 20),
                new Location("Szene Wien", "Austria", "Wien", 1110, "Hauffgasse", 26)
            };

            Hall[][] hallArray = new Hall[][]{
                {
                    new Hall("Halle A", locationArray[0]),
                    new Hall("Halle B", locationArray[0]),
                    new Hall("Halle C", locationArray[0]),
                    new Hall("Halle D", locationArray[0]),
                    new Hall("OpenAir Bühne", locationArray[0]),
                },
                {
                    new Hall("Halle A", locationArray[1]),
                    new Hall("Halle B", locationArray[1]),
                },
                {
                    new Hall("Marx Halle", locationArray[2]),
                },
                {
                    new Hall("Halle 1", locationArray[3]),
                    new Hall("Halle 2", locationArray[3])
                },
                {
                    new Hall("Halle E", locationArray[4]),
                    new Hall("Halle F", locationArray[4])
                },
                {
                    new Hall("Haupthalle", locationArray[5])
                },
                {
                    new Hall("Halle 1", locationArray[6]),
                    new Hall("Halle 2", locationArray[6])
                },
                {
                    new Hall("Kleiner Saal", locationArray[7]),
                    new Hall("Großer Saal", locationArray[7]),
                    new Hall("Konzert Saal", locationArray[7])
                },
                {
                    new Hall("Mainstage", locationArray[8]),
                }
            };

            int seatsArray[][] = new int[][]{
                // Columns, Rows, Sektors
                {15, 15, 5},
                {9, 9, 3},
                {8, 8, 2},
                {7, 7, 1},
                {20, 20, 1},
                {10, 10, 4},
                {7, 8, 2},
                {12, 12, 6},
                {15, 15, 5},
                {9, 9, 3},
                {8, 8, 2},
                {7, 7, 1},
                {10, 10, 4},
                {7, 8, 2},
                {12, 12, 6},
                {9, 9, 3},
                {8, 8, 2},
                {7, 7, 1},
                {7, 7, 1}
            };


            int hallCnt = 0;
            LOGGER.info("generating {} location entries", locationArray.length);
            for (int locationID = 0; locationID < locationArray.length; locationID++) {
                Location location = locationArray[locationID];
                LOGGER.debug("saving location {}", location);
                locationRepository.save(location);

                for (int hallID = 0; hallID < hallArray[locationID].length; hallID++) {
                    Hall hall = hallArray[locationID][hallID];

                    hall.setLocation(location);

                    LOGGER.debug("saving hall {}", hall);
                    hallRepository.save(hall);

                    // Seats
                    int columns = seatsArray[hallCnt][0];
                    int rows = seatsArray[hallCnt][1];
                    int sectors = seatsArray[hallCnt][2];
                    int secRows = columns / sectors;
                    List<Seat> seats = new ArrayList<>();

                    for (int row = 0; row < rows; row++) {
                        for (int col = 0; col < columns; col++) {
                            Seat seat = Seat.builder()
                                .nr(col + 1)
                                .row(row + 1)
                                .sector((char) (((row) / secRows) + 97))
                                .hall(hall)
                                .build();

                            LOGGER.debug("saving seat {}", seat);
                            seatRepository.save(seat);
                            seats.add(seat);
                        }
                    }
                    hallCnt++;
                }

            }
        }

        if (artistRepository.count() > 0) {
            LOGGER.info("artists already generated");
        } else {
            LOGGER.info("generating {} event entries", NUMBER_OF_ARTISTS);

            for (int i = 0; i < NUMBER_OF_ARTISTS; i++) {
                artistRepository.save(Artist.builder()
                    .artistFirstname(faker.name().firstName())
                    .artistLastName(faker.name().lastName())
                    .build()
                );
            }
        }


        if (eventRepository.count() > 0) {
            LOGGER.info("events already generated");
        } else {
            LOGGER.info("generating {} event entries", NUMBER_OF_EVENTS_TO_GENERATE);

            Event.EventCategory[] eventCategories = Event.EventCategory.values();

            for (int i = 0; i < NUMBER_OF_EVENTS_TO_GENERATE; i++) {

                LocalDateTime start = LocalDateTime.ofInstant(
                    faker.date()
                        .between(faker.date().past(365, TimeUnit.DAYS), faker.date().future(365, TimeUnit.DAYS)).
                        toInstant(),
                    ZoneId.systemDefault()
                );

                List<Artist> allArtits = artistRepository.findAll();

                List<Artist> artists = new ArrayList<>();

                int numOfArtists = faker.number().numberBetween(1,NUMBER_OF_ARTISTS_PER_EVENT);

                for (int j = 0; j < numOfArtists; j++) {
                    artists.add(allArtits.remove(faker.number().numberBetween(0,allArtits.size() - 1)));
                }


                List<Hall> allHalls = hallRepository.findAll();

                Event.EventCategory category = eventCategories[faker.number().numberBetween(0, eventCategories.length-1)];

                Event event = Event.builder()
                    .startOfEvent(start)
                    .endOfEvent(start.plusHours(2))
                    .price(faker.number().numberBetween(500L, 10000L))
                    .description(faker.gameOfThrones().quote())
                    .title(faker.music().instrument() + " concert")
                    .hall(allHalls.get(faker.number().numberBetween(0, allHalls.size()-1)))
                    .artists(artists)
                    .category(category)
                    .seatSelection(faker.bool().bool())
                    .build();
                LOGGER.debug("saving event {}", event);
                eventRepository.save(event);

            }

        }

    }
}
