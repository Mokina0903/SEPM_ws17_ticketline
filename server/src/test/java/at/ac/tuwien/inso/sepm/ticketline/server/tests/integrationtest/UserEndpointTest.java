package at.ac.tuwien.inso.sepm.ticketline.server.tests.integrationtest;


import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.BaseIntegrationTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.*;
import static org.hamcrest.core.Is.is;

public class UserEndpointTest extends BaseIntegrationTest {




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
        //userRepository.findOneByUserName(USER_USERNAME).setBlocked(true);
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
        //setupDefaultNews();

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
            .when().post(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        //List<News> news = newsRepository.findNotSeenByUser(userRepository.findOneByUserName(USER_USERNAME + 1).getId());
        //Assert.assertTrue(news != null);
        //if (news != null) {
            //Assert.assertTrue(news.size() > 0);
        //}
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
