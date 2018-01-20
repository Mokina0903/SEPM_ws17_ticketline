package at.ac.tuwien.inso.sepm.ticketline.server.unittest.base;

import at.ac.tuwien.inso.sepm.ticketline.server.configuration.JacksonConfiguration;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.security.AuthenticationConstants;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleHeaderTokenAuthenticationService;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.ObjectMapperConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import org.assertj.core.util.Strings;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unit-test")
@Rollback(true)
public abstract class BaseUnitTest {

    protected static final String SERVER_HOST = "http://localhost";
    protected static final String USER_USERNAME = "user";
    protected static final String USER_PASSWORD = "password";
    protected static final String ADMIN_PASSWORD = "password";
    protected static final String ADMIN_USERNAME = "admin";

    protected static final String NEWS_TEXT = "TestNewsText";
    protected static final String NEWS_TITLE = "title";
    protected static final LocalDateTime NEWS_PUBLISHED_AT =
        LocalDateTime.of(2017, 11, 13, 12, 15, 0, 0);
    protected static final long NEWS_ID = 1L;

    protected static final Long ARTIST_ID = 1L;
    protected static final String ARTIST_FIRSTNAME = "Firstname";
    protected static final String ARTIST_LASTNAME = "Lastname";

    protected static final long LOCATION_ID = 1L;
    protected static final String LOCATION_DESCRIPTION = "description";
    protected static final String LOCATION_CITY= "TestCity";
    protected static final String LOCATION_COUNTRY = "TestCountry";
    protected static final int LOCATION_HOUSENR = 123;
    protected static final String LOCATION_STREET = "TestStreet";
    protected static final int LOCATION_ZIP = 1234;

    protected static final long HALL_ID = 1L;
    protected static final String HALL_DESCRIPTION = "Test Hall 1";

    protected static final long EVENT_ID = 1L;
    protected static final String EVENT_DESCRIPTION = "Event Description";
    protected static final String EVENT_TITLE = "Event Title";
    protected static final long EVENT_PRICE = 100;

    protected static LocalDateTime EVENT_START =
        LocalDateTime.of(2017, 11, 29, 12, 15, 0, 0);

    protected static final long CUSTOMER_ID = 1L;
    protected static final long CUSTOMER_NUMBER = 9999L;
    protected static final String  CUSTOMER_NAME = "Max";
    protected static final String  CUSTOMER_SURNAME = "Mustermann";
    protected static final String  CUSTOMER_MAIL = "Maxmustermann@gmail.com";
    protected static final LocalDate CUSTOMER_BIRTHDATE = LocalDate.of(1950, 1, 1);
    protected static final String  CUSTOMER_NAME_SUBSTRING = "muste";

    protected static final Long SEAT_ID = 1L;
    protected static final int SEAT_NR = 1;
    protected static final char SEAT_SECTOR = 'a';
    protected static final int SEAT_ROW = 1;

    protected static final Long TICKET_RESERVATIONNR = 10001L;
    protected static final Long TICKET_ID = 1L;
    protected static final long TICKET_PRICE = 100;

    @Value("${server.context-path}")
    private String contextPath;

    @LocalServerPort
    private int port;

    @Autowired
    protected SimpleHeaderTokenAuthenticationService simpleHeaderTokenAuthenticationService;

    @Autowired
    private JacksonConfiguration jacksonConfiguration;


    @Autowired
    protected PasswordEncoder encoder;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserService userService;

    @Autowired
    protected NewsRepository newsRepository;

    @Autowired
    protected LocationRepository locationRepository;

    @Autowired
    protected HallRepository hallRepository;

    @Autowired
    protected SeatRepository seatRepository;

    @Autowired
    protected ArtistRepository artistRepository;

    @Autowired
    protected CustomerRepository customerRepository;

    @Autowired
    protected EventRepository eventRepository;

    @Autowired
    protected TicketRepository ticketRepository;

    protected String validUserTokenWithPrefix;
    protected String validAdminTokenWithPrefix;

    @Before
    public void beforeBase() throws Exception {
        setupDefaultNews();
        setupDefaultUsers();

        RestAssured.baseURI = SERVER_HOST;
        RestAssured.basePath = contextPath;
        RestAssured.port = port;
        RestAssured.config = RestAssuredConfig.config().
            objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((aClass, s) ->
                jacksonConfiguration.jackson2ObjectMapperBuilder().build()));

        validUserTokenWithPrefix = Strings
            .join(
                AuthenticationConstants.TOKEN_PREFIX,
                simpleHeaderTokenAuthenticationService.authenticate(USER_USERNAME, USER_PASSWORD).getCurrentToken())
            .with(" ");

        validAdminTokenWithPrefix = Strings
            .join(
                AuthenticationConstants.TOKEN_PREFIX,
                simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD).getCurrentToken())
            .with(" ");
    }

    protected void setupDefaultUsers() {
        User admin = User.builder()
            .userName(ADMIN_USERNAME)
            .password(encoder.encode(ADMIN_PASSWORD))
            .notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
            .blocked(false)
            .role(1)
            .build();

        User user = User.builder()
            .userName(USER_USERNAME)
            .password(encoder.encode(USER_PASSWORD))
            .notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
            .blocked(false)
            .role(2)
            .build();

        User admin2 = User.builder()
            .userName(ADMIN_USERNAME + "test")
            .password(encoder.encode(ADMIN_PASSWORD))
            .notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
            .blocked(false)
            .role(1)
            .build();

        if (userRepository == null || userRepository.findAll().size() == 0) {
            userRepository.save(admin);
            userRepository.save(user);
            userRepository.save(admin2);
        } else {
            for (int i = 1; i <= 3; i++) {
                User tUser = userRepository.findOne((long) i);
                tUser.resetAttempts();
                tUser.setPassword(encoder.encode(USER_PASSWORD));
                tUser.setBlocked(false);
                tUser.setVersion(1);
                tUser.setNotSeen(newsRepository.findAllByOrderByPublishedAtDesc());
                userRepository.save(tUser);
            }
        }
    }

    public void setupDefaultNews(){
        if (newsRepository.findAll().size() == 0) {
            newsRepository.save(News.builder()
                .id(NEWS_ID)
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .publishedAt(NEWS_PUBLISHED_AT)
                .build());

            newsRepository.save(News.builder()
                .id(NEWS_ID+1)
                .title(NEWS_TITLE+"2")
                .text(NEWS_TEXT+"2")
                .publishedAt(NEWS_PUBLISHED_AT)
                .build());
        }
    }

    public void setupDefaultLocation() {
        if (locationRepository.count() > 0)
            return;

        Location location = Location.builder()
            .id(LOCATION_ID)
            .description(LOCATION_DESCRIPTION)
            .city(LOCATION_CITY)
            .country(LOCATION_COUNTRY)
            .houseNr(LOCATION_HOUSENR)
            .street(LOCATION_STREET)
            .zip(LOCATION_ZIP)
            .build();

        location = locationRepository.save(location);

        Hall hall = Hall.builder()
            .id(HALL_ID)
            .description(HALL_DESCRIPTION)
            .location(location)
            .build();
        hall = hallRepository.save(hall);

        Seat seat = Seat.builder()
            .id(SEAT_ID)
            .hall(hall)
            .nr(SEAT_NR)
            .sector(SEAT_SECTOR)
            .row(SEAT_ROW)
            .build();

        seatRepository.save(seat);

        /*
        Maybe not necessary
        // Seats
        int columns = 5;
        int rows = 5;
        int sectors = 3;
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
                seats.add(seat);
            }
        }
        */
    }

    public void setUpDefaultArtist() {
        if (artistRepository.count() > 0 )
            return;

        Artist artist = Artist.builder()
            .id(ARTIST_ID)
            .artistFirstname(ARTIST_FIRSTNAME)
            .artistLastName(ARTIST_LASTNAME)
            .build();

        artistRepository.save(artist);
    }

    public void setUpDefaultCustomers() {
        if (customerRepository.count() > 0)
            return;

        Customer customer = Customer.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name(CUSTOMER_NAME)
            .surname(CUSTOMER_SURNAME)
            .mail(CUSTOMER_MAIL)
            .birthDate(CUSTOMER_BIRTHDATE)
            .build();
        customerRepository.save(customer);
    }

    public void setUpDefaultEvent() {
        if (eventRepository.count() > 0)
            return;

        setupDefaultLocation();
        setUpDefaultArtist();
        setUpDefaultCustomers();

        Hall hall = hallRepository.findOne(HALL_ID);

        List<Artist> artists = new ArrayList<>();

        artists.add(artistRepository.findOne(ARTIST_ID));

        Event event = Event.builder()
            .id(EVENT_ID)
            .startOfEvent(EVENT_START)
            .endOfEvent(EVENT_START.plusHours(2))
            .artists(artists)
            .price(EVENT_PRICE)
            .description(EVENT_DESCRIPTION)
            .title(EVENT_TITLE)
            .hall(hall)
            .seatSelection(true)
            .build();

        eventRepository.save(event);
    }

    public void setUpDefaultEvent(LocalDateTime startOfEvent) {
        setupDefaultLocation();
        setUpDefaultArtist();
        setUpDefaultCustomers();

        Hall hall = hallRepository.findOne(HALL_ID);

        List<Artist> artists = new ArrayList<>();

        artists.add(artistRepository.findOne(ARTIST_ID));

        Event event = Event.builder()
            .id(EVENT_ID + 1)
            .startOfEvent(startOfEvent)
            .endOfEvent(startOfEvent.plusHours(2))
            .artists(artists)
            .price(EVENT_PRICE)
            .description(EVENT_DESCRIPTION)
            .title(EVENT_TITLE)
            .hall(hall)
            .seatSelection(true)
            .build();

        eventRepository.save(event);
    }
}
