package at.ac.tuwien.inso.sepm.ticketline.server.tests.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.BaseIntegrationTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;

public class NewsEndpointTest extends BaseIntegrationTest {

    @MockBean
    private NewsRepository newsRepository;

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
        BDDMockito.
            given(newsRepository.findAllByOrderByPublishedAtDesc()).
            willReturn(Collections.singletonList(
                News.builder()
                    .id(NEWS_ID)
                    .title(NEWS_TITLE)
                    .text(NEWS_TEXT)
                    .publishedAt(NEWS_PUBLISHED_AT)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(SimpleNewsDTO[].class)), is(Collections.singletonList(
            SimpleNewsDTO.builder()
                .id(NEWS_ID)
                .title(NEWS_TITLE)
                .summary(NEWS_TEXT)
                .publishedAt(NEWS_PUBLISHED_AT)
                .build())));
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
        BDDMockito.
            given(newsRepository.findOneById(NEWS_ID)).
            willReturn(Optional.of(News.builder()
                .id(NEWS_ID)
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .publishedAt(NEWS_PUBLISHED_AT)
                .build()));

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
        BDDMockito.
            given(newsRepository.findOneById(NEWS_ID)).
            willReturn(Optional.empty());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT + SPECIFIC_NEWS_PATH, NEWS_ID)
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
        BDDMockito.
            given(newsRepository.save(any(News.class))).
            willReturn(News.builder()
                .id(NEWS_ID)
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .publishedAt(NEWS_PUBLISHED_AT)
                .build());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedNewsDTO.builder()
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(DetailedNewsDTO.class), is(DetailedNewsDTO.builder()
            .id(NEWS_ID)
            .title(NEWS_TITLE)
            .text(NEWS_TEXT)
            .publishedAt(NEWS_PUBLISHED_AT)
            .build()));
    }

}