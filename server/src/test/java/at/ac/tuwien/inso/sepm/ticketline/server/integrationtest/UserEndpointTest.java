package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;


import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.security.AuthenticationConstants;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.assertj.core.util.Strings;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;

public class UserEndpointTest extends BaseIntegrationTest {

    private static final String USER_ENDPOINT = "/user";
    private static final String USER_ENDPOINT_BLOCK = "/user/block";
    private static final String USER_ENDPOINT_FIND = "/user/find/{userName}";
    private static final String USER_ENDPOINT_RESET = "/user/resetPassword";
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
    public void loginBlockwhenAttempsZero() {
        super.setupDefaultUsers();

        for (int i = 1; i <= User.LOGIN_ATTEMPTS; i++) {
            try {
                super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD.substring(2));
            } catch (Exception e) {

            }

            Assert.assertFalse("Failure at run " + i + " is allready blocked",
                    userRepository.findOneByUserName(ADMIN_USERNAME).isBlocked());
        }

        try {
            super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD.substring(2));
        } catch (Exception e) {

        }

        Assert.assertTrue("Failure at run " + User.LOGIN_ATTEMPTS+1 + " is not blocked",
            userRepository.findOneByUserName(ADMIN_USERNAME).isBlocked());
    }


    @Test
    public void loginnerlyBlocked() {
        super.setupDefaultUsers();

        for (int i = 1; i < User.LOGIN_ATTEMPTS; i++) {
            try {
                super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD.substring(2));
            } catch (Exception e) {


            }

            Assert.assertFalse("Failure at run " + i + " is allready blocked",
                userRepository.findOneByUserName(ADMIN_USERNAME).isBlocked());

        }

        try {
            super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD);
        } catch (Exception e) {

        }

        Assert.assertFalse("Failure at run " + (User.LOGIN_ATTEMPTS + 1) + " blocked",
            userRepository.findOneByUserName(ADMIN_USERNAME).isBlocked());
    }


    @Test
    public void loginResetAttemps(){
        super.setupDefaultUsers();
        try {
            super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD.substring(2));
        } catch (Exception e) {

        }

        int beforeLogin = userRepository.findOneByUserName(ADMIN_USERNAME).getAttempts();

        super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD).getCurrentToken();

        int afterLogin = userRepository.findOneByUserName(ADMIN_USERNAME).getAttempts();

        System.out.println(beforeLogin + "David" + afterLogin);
        Assert.assertTrue("Attemptreset does not work correct",beforeLogin < afterLogin);

    }

    // TODO: (TEST) Add Test for Logout US 1
    // TODO: (TEST) Add Test Admin unlock US 1
    // TODO: (TEST) Test correct privileges at REST US 1
    // TODO: (TEST) Show Users US 1
    // TODO: (TEST) Admin can not lock himself BUS
    // TODO: (TEST) Add New Users (Admin/Seller) (1/2) BUS
    // TODO: (TEST) Block/Unblock Users BUS
    // TODO: (TEST) SetPassword: Login with old -> reset Password -> login with new Password BUS
    // TODO: (TEST) AddNewUser(<bereits angelegter User>) darf nicht möglich sein

    @Test
    public void blockUser(){
        super.setupDefaultUsers();

        super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD).getCurrentToken();

        User blockUser = userRepository.findOneByUserName(USER_USERNAME);

        userService.blockUser(blockUser);

        Assert.assertTrue("Could not block user.",
            blockUser.isBlocked());

    }


/*
    @Test
    public void BlockUserAsAdmin() {
        //super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(SimpleUserDTO.builder()
                .userName(USER_USERNAME)
                .build())
            .when().post(USER_ENDPOINT_BLOCK)
            .then().extract().response();

        System.out.println(response.asString());

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

    }



    @Test
    public void BlockUserAsAdmin() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(USER_ENDPOINT_FIND, "Florian")
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

    }
*/

}
