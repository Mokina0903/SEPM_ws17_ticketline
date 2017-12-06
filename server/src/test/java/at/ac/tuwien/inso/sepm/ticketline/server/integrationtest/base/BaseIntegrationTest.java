package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base;

import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationToken;
import at.ac.tuwien.inso.sepm.ticketline.server.configuration.JacksonConfiguration;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.security.AuthenticationConstants;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleHeaderTokenAuthenticationService;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.ObjectMapperConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.assertj.core.util.Strings;
import org.hibernate.cfg.annotations.ResultsetMappingSecondPass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.when;

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

    protected String validUserTokenWithPrefix;
    protected String validAdminTokenWithPrefix;

    @Mock
    DataSource dataSource;

    @Mock
    private Connection c;

    @Mock
    private PreparedStatement stmt;

    @Mock
    private ResultSet rs;



    @Before
    public void beforeBase() throws Exception {

        assertNotNull(dataSource);

        when(c.prepareStatement(startsWith(
            "SELECT user_name, password, (not blocked) as enabled from users where user_name"))).thenReturn(stmt);



        when(c.prepareStatement(any(String.class))).thenReturn(stmt);
        when(dataSource.getConnection()).thenReturn(c);

        //user_name, password, (not blocked) as enabled

        when(rs.first()).thenReturn(true);

        when(rs.getString(1)).thenReturn(USER_USERNAME);
        when(rs.getString(2)).thenReturn(USER_PASSWORD);
        when(rs.getBoolean(3)).thenReturn(false);

        when(rs.getString("user_name")).thenReturn(USER_USERNAME);
        when(rs.getString("password")).thenReturn(USER_PASSWORD);
        when(rs.getBoolean("enabled")).thenReturn(false);

        when(stmt.executeQuery()).thenReturn(rs);


        PreparedStatement preparedStatement =  c.prepareStatement("SELECT user_name, password, (not blocked) as enabled from users where user_name=?");

        preparedStatement.setInt(1,3);
        preparedStatement.executeQuery();

        System.out.println(rs.getString(1));





        RestAssured.baseURI = SERVER_HOST;
        RestAssured.basePath = contextPath;
        RestAssured.port = port;
        RestAssured.config = RestAssuredConfig.config().
            objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((aClass, s) ->
                jacksonConfiguration.jackson2ObjectMapperBuilder().build()));


        /*

             BDDMockito.
            given(newsRepository.findAllByOrderByPublishedAtDesc()).
            willReturn(Collections.singletonList(
                News.builder()
                    .id(TEST_NEWS_ID)
                    .title(TEST_NEWS_TITLE)
                    .text(TEST_NEWS_TEXT)
                    .publishedAt(TEST_NEWS_PUBLISHED_AT)
                    .build()));

             */




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


        //validAdminTokenWithPrefix = "adm";
        //validUserTokenWithPrefix = "usr";

    }
}
