package at.ac.tuwien.inso.sepm.ticketline.server.tests.base;

import at.ac.tuwien.inso.sepm.ticketline.server.configuration.JacksonConfiguration;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.security.AuthenticationConstants;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleHeaderTokenAuthenticationService;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.ObjectMapperConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import org.assertj.core.util.Strings;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public abstract class BaseIntegrationTest {
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

    /* @MockBean */
    @Autowired
    protected UserRepository userRepository;

    protected String validUserTokenWithPrefix;
    protected String validAdminTokenWithPrefix;

    public void beforeBaseMockup() {
        String passwort = encoder.encode(ADMIN_PASSWORD);

        BDDMockito.
            given(userRepository.findByUserName(ADMIN_USERNAME))
                .willReturn(Optional.of(User.builder()
                    .id(ADMIN_ID)
                    .userName(ADMIN_USERNAME)
                    .password(passwort)
                    .role(1)
                    .blocked(false)
                    .notSeen(null)
                    .build()));

        RestAssured.baseURI = SERVER_HOST;
        RestAssured.basePath = contextPath;
        RestAssured.port = port;
        RestAssured.config = RestAssuredConfig.config().
            objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((aClass, s) ->
                jacksonConfiguration.jackson2ObjectMapperBuilder().build()));
        /*
        validUserTokenWithPrefix = Strings
            .join(
                AuthenticationConstants.TOKEN_PREFIX,
                simpleHeaderTokenAuthenticationService.authenticate(USER_USERNAME, USER_PASSWORD).getCurrentToken())
            .with(" ");
        */

        User user = userRepository.findByUserName(ADMIN_USERNAME).get();

        System.out.println(passwort);
        System.out.println(user.getPassword());

        validAdminTokenWithPrefix = Strings
            .join(
                AuthenticationConstants.TOKEN_PREFIX,
                simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, passwort).getCurrentToken())
            .with(" ");
    }

    @Before
    public void beforeBase() throws Exception {
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
            //.notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
            .blocked(false)
            .role(1)
            .build();

        User user = User.builder()
            .userName(USER_USERNAME)
            .password(encoder.encode(USER_PASSWORD))
            //.notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
            .blocked(false)
            .role(2)
            .build();

        User admin2 = User.builder()
            .userName(ADMIN_USERNAME + "test")
            .password(encoder.encode(ADMIN_PASSWORD))
            //.notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
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
                //tUser.setNotSeen(newsRepository.findAllByOrderByPublishedAtDesc());
                userRepository.save(tUser);
            }
        }
    }


}
