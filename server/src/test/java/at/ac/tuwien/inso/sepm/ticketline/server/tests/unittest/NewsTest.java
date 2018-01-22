package at.ac.tuwien.inso.sepm.ticketline.server.tests.unittest;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.BaseTestUnit;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;

import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.*;
import static org.hamcrest.core.Is.is;

public class NewsTest extends BaseTestUnit {

    @Test
    public void findAllNewsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));

    }

    @Test
    public void findAllNewsAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        SimpleNewsDTO[] simpleNewsDTOS = response.as(SimpleNewsDTO[].class);

        Assert.assertThat(simpleNewsDTOS.length,is(2));

        Assert.assertThat(simpleNewsDTOS[0], is(
            SimpleNewsDTO.builder()
                .id(NEWS_ID)
                .title(NEWS_TITLE)
                .summary(NEWS_TEXT)
                .publishedAt(NEWS_PUBLISHED_AT)
                .build()));

        Assert.assertThat(simpleNewsDTOS[1], is(
            SimpleNewsDTO.builder()
                .id(NEWS_ID + 1)
                .title(NEWS_TITLE + 2)
                .summary(NEWS_TEXT + 2)
                .publishedAt(NEWS_PUBLISHED_AT)
                .build()));
    }

    @Test
    public void findSpecificNewsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(NEWS_ENDPOINT + SPECIFIC_NEWS_PATH, NEWS_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findSpecificNewsAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT + SPECIFIC_NEWS_PATH, NEWS_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(DetailedNewsDTO.class), is(DetailedNewsDTO.builder()
            .id(NEWS_ID)
            .title(NEWS_TITLE)
            .text(NEWS_TEXT)
            .publishedAt(NEWS_PUBLISHED_AT)
            .build()));
    }

    @Test
    public void findSpecificNonExistingNewsNotFoundAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT + SPECIFIC_NEWS_PATH, 0)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void publishNewsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(DetailedNewsDTO.builder()
                .id(NEWS_ID)
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .publishedAt(NEWS_PUBLISHED_AT)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void publishNewsUnauthorizedAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(DetailedNewsDTO.builder()
                .id(NEWS_ID)
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .publishedAt(NEWS_PUBLISHED_AT)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void publishNewsAsAdmin() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedNewsDTO.builder()
                .title(NEWS_TITLE + 3)
                .text(NEWS_TEXT + 3)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        // Necessary because PusblishedAt is automatically calced
        DetailedNewsDTO detailedNewsDTO = response.as(DetailedNewsDTO.class);
        detailedNewsDTO.setPublishedAt(null);

        Assert.assertThat(detailedNewsDTO, is(DetailedNewsDTO.builder()
            .id(NEWS_ID+2)
            .title(NEWS_TITLE + 3)
            .text(NEWS_TEXT + 3)
            .publishedAt(null)
            .build()));


    }

    @Test
    public void findNotSeenbyUserAsUserUpdate(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT+"/notSeen/{userId}",1)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        SimpleNewsDTO[] simpleNewsDTOS = response.as(SimpleNewsDTO[].class);

        Assert.assertThat(simpleNewsDTOS.length,is(3));

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body("")
            .when().post(USER_ENDPOINT + "/{userId}/{newsId}",1,2)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));


        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedNewsDTO.builder()
                .title(NEWS_TITLE + 3)
                .text(NEWS_TEXT + 3)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT+"/old/{userId}",1)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        simpleNewsDTOS = response.as(SimpleNewsDTO[].class);

        Assert.assertThat(simpleNewsDTOS.length,is(1));


    }

    @Test
    public void findNotSeenByUserAsUser(){
        // TODO: Does not work!!
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT+"/notSeen/{userId}",1)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        SimpleNewsDTO[] simpleNewsDTOS = response.as(SimpleNewsDTO[].class);

        Assert.assertThat(simpleNewsDTOS.length,is(4));

        Assert.assertThat(simpleNewsDTOS[0], is(
            SimpleNewsDTO.builder()
                .id(NEWS_ID)
                .title(NEWS_TITLE)
                .summary(NEWS_TEXT)
                .publishedAt(NEWS_PUBLISHED_AT)
                .build()));

        Assert.assertThat(simpleNewsDTOS[1], is(
            SimpleNewsDTO.builder()
                .id(NEWS_ID + 1)
                .title(NEWS_TITLE + 2)
                .summary(NEWS_TEXT + 2)
                .publishedAt(NEWS_PUBLISHED_AT)
                .build()));
    }


    @Test
    public void findOldNewsByUserAsUser(){
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT+"/old/{userId}",1)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(SimpleNewsDTO[].class)), is(Collections.emptyList()));
    }

}
