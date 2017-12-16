package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base;

import at.ac.tuwien.inso.sepm.ticketline.server.configuration.JacksonConfiguration;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public abstract class BaseIntegrationTest {

    protected static final String SERVER_HOST = "http://localhost";
    protected static final String USER_USERNAME = "user";
    protected static final String USER_PASSWORD = "password";
    protected static final String ADMIN_PASSWORD = "password";
    protected static final String ADMIN_USERNAME = "admin";

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


        if (userRepository == null || userRepository.findAll().size() == 0) {
            userRepository.save(admin);
            userRepository.save(user);
        } else {
            for (int i = 1; i <= 2; i++) {
                User tUser = userRepository.findOne((long) i);
                tUser.resetAttempts();
                tUser.setBlocked(false);
                tUser.setNotSeen(newsRepository.findAllByOrderByPublishedAtDesc());
                userRepository.save(tUser);
            }
        }
    }

    // TODO: (Tutorin) Is this correct?
    public void setupDefaultNews(){
        // TODO: David Start at 1 sequenc
        String TEST_NEWS_TEXT = "TestNewsText";
        String TEST_NEWS_TITLE = "title";
        LocalDateTime TEST_NEWS_PUBLISHED_AT =
            LocalDateTime.of(2016, 11, 13, 12, 15, 0, 0);
        long TEST_NEWS_ID = 1L;

        if (newsRepository.findAll().size() == 0) {
            newsRepository.save(News.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build());
        }
    }
}
