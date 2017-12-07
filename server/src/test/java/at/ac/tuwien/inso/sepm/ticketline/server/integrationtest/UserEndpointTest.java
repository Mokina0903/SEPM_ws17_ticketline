package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;


import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.security.AuthenticationConstants;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.assertj.core.util.Strings;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;

public class UserEndpointTest extends BaseIntegrationTest {

    private static final String USER_ENDPOINT = "/user";
    private static final String SPECIFIC_USER_PATH = "/{userId}";

    private static final String TEST_USER_TEXT = "TestUserText";
    private static final String TEST_USER_TITLE = "title";
    private static final LocalDateTime TEST_USER_PUBLISHED_AT =
        LocalDateTime.of(2016, 11, 13, 12, 15, 0, 0);
    private static final long TEST_USER_ID = 1L;

    @Test
    public void loginAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }



    @Test
    public void loginWithCorrectNameAndWrongPassword(){
        // TODO: TestCase edit after Reorganisation of Blocking users and count attempts

        try {
            String wrong = Strings
                .join(
                    AuthenticationConstants.TOKEN_PREFIX,
                    super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD.substring(2)).getCurrentToken())
                .with(" ");

        }catch (Exception e){

        } finally {
            User user = userRepository.findOneByUserName(ADMIN_USERNAME);
            System.out.println(user.getAttempts());
        }

        try {
            String wrong = Strings
                .join(
                    AuthenticationConstants.TOKEN_PREFIX,
                    super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD.substring(2)).getCurrentToken())
                .with(" ");

        }catch (Exception e){

        } finally {
            User user = userRepository.findOneByUserName(ADMIN_USERNAME);
            System.out.println(user.getAttempts());
        }

        /*
        wrong = Strings
            .join(
                AuthenticationConstants.TOKEN_PREFIX,
                super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD.substring(2)).getCurrentToken())
            .with(" ");
        wrong = Strings
            .join(
                AuthenticationConstants.TOKEN_PREFIX,
                super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD).getCurrentToken())
            .with(" ");
            */
    }




}
