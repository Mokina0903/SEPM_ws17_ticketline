package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;


import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
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

import javax.validation.constraints.AssertTrue;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class UserEndpointTest extends BaseIntegrationTest {

    private static final String USER_ENDPOINT = "/user";
    private static final String USER_ENDPOINT_BLOCK = "/user/block";
    private static final String USER_ENDPOINT_UNBLOCK = "/user/unblock";
    private static final String USER_ENDPOINT_FIND = "/user/find/{userName}";
    private static final String USER_ENDPOINT_RESET = "/user/resetPassword";
    private static final String USER_ENDPOINT_IS_BLOCKED = "/user/isBlocked/{username}";
    private static final String USER_ENDPOINT_NEW_USER = "/user";
    private static final String SPECIFIC_USER_PATH = "/{userId}";

    private static final String TEST_USER_TEXT = "TestUserText";
    private static final String TEST_USER_TITLE = "title";
    private static final LocalDateTime TEST_USER_PUBLISHED_AT =
        LocalDateTime.of(2016, 11, 13, 12, 15, 0, 0);
    private static final long TEST_USER_ID = 1L;

    @Test
    public void loginAsAnonymous() {
        super.setupDefaultUsers();
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

        //System.out.println(beforeLogin + "David" + afterLogin);
        Assert.assertTrue("Attemptreset does not work correct",beforeLogin < afterLogin);

    }

    // NOT Possible: (TEST) Add Test for Logout US 1
    // DONE: (TEST) Add Test Admin unlock US 1
    // TODO: (TEST) Test correct privileges at REST US 1
    // Done: (TEST) Show Users US 1
    // DONE: (TEST) Admin can not lock himself BUS
    // DONE: (TEST) Add New Users (Admin/Seller) (1/2) BUS
    // DONE: (TEST) Block/Unblock Users BUS
    // DONE: (TEST) SetPassword: Login with old -> reset Password -> login with new Password BUS
    // DONE: (TEST) AddNewUser(<bereits angelegter User>) darf nicht möglich sein

    @Test
    public void resetAdminAsUser() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(SimpleUserDTO.builder()
                .userName(ADMIN_USERNAME)
                .password(ADMIN_PASSWORD + "neu")
                .build())
            .when().post(USER_ENDPOINT_RESET)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void resetUserAsAnonym() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(SimpleUserDTO.builder()
                .userName(USER_USERNAME)
                .password(USER_PASSWORD + "neu")
                .build())
            .when().post(USER_ENDPOINT_RESET)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));

    }



    @Test
    public void blockUser(){
        super.setupDefaultUsers();

        super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD).getCurrentToken();

        User blockUser = userRepository.findOneByUserName(USER_USERNAME);

        userService.blockUser(blockUser);

        Assert.assertTrue("Could not block user.",
            blockUser.isBlocked());
        userService.unblockUser(blockUser);
    }



    @Test
    public void ResetUserAsAdmin() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(SimpleUserDTO.builder()
                .userName(USER_USERNAME)
                .password(USER_PASSWORD + "neu")
                .build())
            .when().post(USER_ENDPOINT_RESET)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(USER_ENDPOINT_FIND, USER_USERNAME)
            .then().extract().response();
            //Assert.assertTrue(response.asString().contains(USER_PASSWORD + "neu"));
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }


    @Test
    public void BlockAdminAsUser() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(SimpleUserDTO.builder()
                .userName(ADMIN_USERNAME)
                .build())
            .when().post(USER_ENDPOINT_BLOCK)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }


    @Test
    public void BlockUserAsAdmin() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(USER_USERNAME)
            .when().post(USER_ENDPOINT_BLOCK)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get("/user/user/isBlocked")
            .then().extract().response();
        Assert.assertTrue(response.asString().equals("true"));
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }


    @Test
    public void AccessUserWithSQLInjection(){
        String path = "/user/h' or 1=1/isBlocked";
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(path)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }


    @Test
    public void BlockAdminAsSameAdmin() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(ADMIN_USERNAME)
            .when().post(USER_ENDPOINT_BLOCK)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_ACCEPTABLE.value()));

    }

    @Test
    public void BlockAdminAsOtherAdmin() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(ADMIN_USERNAME + "test")
            .when().post(USER_ENDPOINT_BLOCK)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get("/user/admintest/isBlocked")
            .then().extract().response();
        Assert.assertTrue(response.asString().equals("true"));
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }


    @Test
    public void BlockAllAsAdmin() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body("f' or 'ha' = 'ha")
            .when().post(USER_ENDPOINT_BLOCK)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void UnblockUserAsAdmin() {
        super.setupDefaultUsers();
        userRepository.findOneByUserName(USER_USERNAME).setBlocked(true);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(USER_USERNAME)
            .when().post(USER_ENDPOINT_UNBLOCK)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get("/user/user/isBlocked")
            .then().extract().response();
        Assert.assertTrue(response.asString().equals("false"));
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void UnblockUnblockedUserAsAdmin() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(USER_USERNAME)
            .when().post(USER_ENDPOINT_UNBLOCK)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get("/user/user/isBlocked")
            .then().extract().response();
        Assert.assertTrue(response.asString().equals("false"));
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }



    @Test
    public void newUserAsAdmin() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedUserDTO.builder()
                .userName(USER_USERNAME + 1)
                .password(encoder.encode(USER_PASSWORD))
                .blocked(false)
                .role(2)
                .build())
            .when().post(USER_ENDPOINT_NEW_USER)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(USER_ENDPOINT_FIND, USER_USERNAME + 1)
            .then().extract().response();
        Assert.assertTrue(response.asString().contains(USER_USERNAME + 1));
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

    }

    @Test
    public void newAdminAsAdmin() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedUserDTO.builder()
                .userName(ADMIN_USERNAME + 1)
                .password(encoder.encode(ADMIN_PASSWORD))
                //.notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
                .blocked(false)
                .role(1)
                .build())
            .when().post(USER_ENDPOINT_NEW_USER)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(USER_ENDPOINT_FIND, ADMIN_USERNAME + 1)
            .then().extract().response();
        Assert.assertTrue(response.asString().contains(ADMIN_USERNAME + 1));
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void newUserAsUser() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(DetailedUserDTO.builder()
                .userName(USER_USERNAME + 2)
                .password(encoder.encode(USER_PASSWORD))
                //.notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
                .blocked(false)
                .role(2)
                .build())
            .when().post(USER_ENDPOINT_NEW_USER)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void newExistingUserAsAdmin() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedUserDTO.builder()
                .userName(USER_USERNAME)
                .password(encoder.encode(USER_PASSWORD))
                //.notSeen(newsRepository.findAllByOrderByPublishedAtDesc())
                .blocked(false)
                .role(2)
                .build())
            .when().post(USER_ENDPOINT_NEW_USER)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT.value()));
    }


    @Test
    public void newNewsForNewUserAsAdmin() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedUserDTO.builder()
                .userName(USER_USERNAME + 2)
                .password(encoder.encode(USER_PASSWORD))
                .blocked(false)
                .role(2)
                .build())
            .when().post(USER_ENDPOINT_NEW_USER)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        List<News> news = newsRepository.findNotSeenByUser(userRepository.findOneByUserName(USER_USERNAME + 2).getId());
        Assert.assertTrue(news != null);
        if(news != null){
            Assert.assertTrue(news.size() > 0);
        }
    }

    @Test
    public void showUserAsAdmin() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(USER_ENDPOINT_NEW_USER)
            .then().extract().response();
        Assert.assertTrue((response.asString().contains("user")&&response.asString().contains("admin")));
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void showUserAsUser() {
        super.setupDefaultUsers();
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(USER_ENDPOINT_NEW_USER)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }



}
