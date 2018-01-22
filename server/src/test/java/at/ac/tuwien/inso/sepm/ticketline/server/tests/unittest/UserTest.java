package at.ac.tuwien.inso.sepm.ticketline.server.tests.unittest;


import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news.NewsMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.BaseTestUnit;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;

import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.*;
import static org.hamcrest.core.Is.is;

public class UserTest extends BaseTestUnit {

    @Autowired
    private NewsMapper newsMapper;

    @Before
    public void setUp() {

    }

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

        Assert.assertTrue("Failure at run " + User.LOGIN_ATTEMPTS + 1 + " is not blocked",
            userRepository.findOneByUserName(ADMIN_USERNAME).isBlocked());
    }


    @Test
    public void loginnerlyBlocked() {

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
    public void loginResetAttemps() {
        try {
            super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD.substring(2));
        } catch (Exception e) {

        }

        int beforeLogin = userRepository.findOneByUserName(ADMIN_USERNAME).getAttempts();

        super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD).getCurrentToken();

        int afterLogin = userRepository.findOneByUserName(ADMIN_USERNAME).getAttempts();

        Assert.assertTrue("Attemptreset does not work correct", beforeLogin < afterLogin);

    }

    @Test
    public void resetAdminAsUser() {
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
    public void blockUser() {
        super.simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD).getCurrentToken();

        User blockUser = userRepository.findOneByUserName(USER_USERNAME);

        userService.blockUser(blockUser);

        Assert.assertTrue("Could not block user.",
            blockUser.isBlocked());
        userService.unblockUser(blockUser);
    }


    @Test
    public void ResetUserAsAdmin() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(SimpleUserDTO.builder()
                .userName(USER_USERNAME)
                .password(USER_PASSWORD + "neu")
                .version(1)
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
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void ResetUserAsAdminWithOldVersion() {
        userRepository.findOneByUserName(USER_USERNAME).setVersion(5);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(SimpleUserDTO.builder()
                .userName(USER_USERNAME)
                .password(USER_PASSWORD + "neu")
                .version(3)
                .build())
            .when().post(USER_ENDPOINT_RESET)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FAILED_DEPENDENCY.value()));
    }

    @Test
    public void ResetUserAsAdminAndCheckVersion() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(SimpleUserDTO.builder()
                .userName(USER_USERNAME)
                .password(USER_PASSWORD + "neu")
                .version(1)
                .build())
            .when().post(USER_ENDPOINT_RESET)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertTrue(userRepository.findOneByUserName(USER_USERNAME).getVersion() == 2);
    }


    @Test
    public void BlockAdminAsUser() {
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
    public void AccessUserWithSQLInjection() {
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
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedUserDTO.builder()
                .userName(USER_USERNAME + "test1")
                .password(encoder.encode(USER_PASSWORD))
                .blocked(false)
                .role(2)
                .build())
            .when().post(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(USER_ENDPOINT_FIND, USER_USERNAME + "test1")
            .then().extract().response();
        Assert.assertTrue(response.asString().contains(USER_USERNAME + "test1"));
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

    }

    @Test
    public void newAdminAsAdmin() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedUserDTO.builder()
                .userName(ADMIN_USERNAME + 1)
                .password(encoder.encode(ADMIN_PASSWORD))
                .blocked(false)
                .role(1)
                .build())
            .when().post(USER_ENDPOINT)
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
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(DetailedUserDTO.builder()
                .userName(USER_USERNAME + 2)
                .password(encoder.encode(USER_PASSWORD))
                .blocked(false)
                .role(2)
                .build())
            .when().post(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void newExistingUserAsAdmin() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedUserDTO.builder()
                .userName(USER_USERNAME)
                .password(encoder.encode(USER_PASSWORD))
                .blocked(false)
                .role(2)
                .build())
            .when().post(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT.value()));
    }


    @Test
    public void newNewsForNewUserAsAdmin() {
        setupDefaultNews();

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedUserDTO.builder()
                .userName(USER_USERNAME + "123")
                .password(encoder.encode(USER_PASSWORD))
                .blocked(false)
                .role(2)
                .notSeen(newsMapper.newsToSimpleNewsDTO(newsRepository.findAllByOrderByPublishedAtDesc()))
                .build())
            .when().post(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));


        List<News> news = newsRepository.findNotSeenByUser(userRepository.findOneByUserName(USER_USERNAME + "123").getId());
        Assert.assertTrue(news != null);
        if (news != null) {
            Assert.assertTrue(news.size() == 0);
        }
        news = newsRepository.findOldNewsByUser(userRepository.findOneByUserName(USER_USERNAME + "123").getId());
        Assert.assertTrue(news != null);
        if (news != null) {
            Assert.assertTrue(news.size() > 0);
        }
    }

    @Test
    public void showUserAsAdmin() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertTrue((response.asString().contains("user") && response.asString().contains("admin")));
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void showUserAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }
}
