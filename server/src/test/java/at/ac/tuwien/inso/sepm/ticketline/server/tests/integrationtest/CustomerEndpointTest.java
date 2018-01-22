package at.ac.tuwien.inso.sepm.ticketline.server.tests.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.BaseIntegrationTest;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestConstants.*;
import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestDTOs.defaultCustomer;
import static at.ac.tuwien.inso.sepm.ticketline.server.tests.base.TestDTOs.defaultCustomerDTO;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;

public class CustomerEndpointTest extends BaseIntegrationTest {

    @MockBean
    private CustomerRepository customerRepository;

    private final Pageable REQUEST= new PageRequest(0, 10);


    @Test
    public void findAllCustomerAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_PAGE_INDEX + CUSTOMER_PER_PAGE, 0, Integer.MAX_VALUE)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllCustomerAsUser() {
        List<Customer> customerList = Collections.singletonList(defaultCustomer());
        Page<Customer> customers = new PageImpl<>(customerList);

        Mockito.when(customerRepository.findAll(any(Pageable.class))).thenReturn(customers);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_PAGE_INDEX + CUSTOMER_PER_PAGE, 0, Integer.MAX_VALUE)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

    }

    @Test
    public void findByCustomerKnrAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_BY_NUMBER_PATH, CUSTOMER_NUMBER)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findByCustomerKnrAsUser() {
        Mockito.when(customerRepository.findOneByKnr(CUSTOMER_NUMBER)).thenReturn(defaultCustomer());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_BY_NUMBER_PATH, CUSTOMER_NUMBER)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }


    @Test
    public void findByNameAsAnonymous() {
        Page<Customer> customers = Mockito.mock(Page.class);
        Mockito.when(customerRepository
            .findByNameStartingWithIgnoreCaseOrSurnameStartingWithIgnoreCase(CUSTOMER_NAME, CUSTOMER_NAME, REQUEST))
            .thenReturn(customers);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_BY_NAME_PATH, 0, Integer.MAX_VALUE, CUSTOMER_NAME)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }


    @Test
    public void findByNameAsUser() {
        List<Customer> customerList = Collections.singletonList(defaultCustomer());
        Page<Customer> customers = new PageImpl<>(customerList);
        Mockito.when(customerRepository
            .findByNameStartingWithIgnoreCaseOrSurnameStartingWithIgnoreCase(any(String.class), any(String.class), any(Pageable.class)))
            .thenReturn(customers);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_BY_NAME_PATH, 0, Integer.MAX_VALUE, CUSTOMER_NAME)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void createCustomerAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(defaultCustomerDTO())
            .when().post(CUSTOMER_ENDPOINT + CUSTOMER_CREATE_PATH)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void createCustomerAsUser() {
        Mockito.when(customerRepository.save(any(Customer.class)))
            .thenReturn(defaultCustomer());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(defaultCustomerDTO())
            .when().post(CUSTOMER_ENDPOINT + CUSTOMER_CREATE_PATH)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void createCustomerWithInvalidNameAsUser() {
        CustomerDTO customerDTO = defaultCustomerDTO();
        customerDTO.setName("");

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(customerDTO)
            .when().post(CUSTOMER_ENDPOINT + CUSTOMER_CREATE_PATH)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_ACCEPTABLE.value()));
    }

    @Test
    public void updateCustomerAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(defaultCustomerDTO())
            .when().post(CUSTOMER_ENDPOINT + CUSTOMER_UPDATE_PATH)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void updateCustomerAsUser() {
        Mockito.when(customerRepository.findOneByKnr(CUSTOMER_NUMBER)).thenReturn(defaultCustomer());

        CustomerDTO customerDTO = defaultCustomerDTO();
        customerDTO.setName("Neuer-Name");

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(customerDTO)
            .when().post(CUSTOMER_ENDPOINT + CUSTOMER_UPDATE_PATH)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void updateCustomerWithInvalidNameAsUser() {
        Mockito.when(customerRepository.findOneByKnr(CUSTOMER_NUMBER)).thenReturn(defaultCustomer());

        CustomerDTO customerDTO = defaultCustomerDTO();
        customerDTO.setName("");

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(customerDTO)
            .when().post(CUSTOMER_ENDPOINT + CUSTOMER_UPDATE_PATH)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_ACCEPTABLE.value()));
    }

    @Test
    public void updateWrongVersionCustomerAsUser() {
        Customer customer = defaultCustomer();
        customer.setVersion(2);

        Mockito.when(customerRepository.findOneByKnr(CUSTOMER_NUMBER)).thenReturn(customer);

        CustomerDTO customerDTO = defaultCustomerDTO();
        customerDTO.setName("Neuer-Name");

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(customerDTO)
            .when().post(CUSTOMER_ENDPOINT + CUSTOMER_UPDATE_PATH)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FAILED_DEPENDENCY.value()));
    }

    @Test
    public void updateWrongKnrCustomerAsUser() {
        Customer customer = defaultCustomer();

        Mockito.when(customerRepository.findOneByKnr(CUSTOMER_NUMBER)).thenReturn(customer);

        CustomerDTO customerDTO = defaultCustomerDTO();
        customerDTO.setKnr(2L);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(customerDTO)
            .when().post(CUSTOMER_ENDPOINT + CUSTOMER_UPDATE_PATH)
            .then().extract().response();

        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void idCustomerAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_ID_INDEX, CUSTOMER_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void idCustomerAsUser() {
        Mockito.when(customerRepository.findOneById(CUSTOMER_ID)).thenReturn(java.util.Optional.ofNullable(defaultCustomer()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_ID_INDEX, CUSTOMER_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void idWrongCustomerAsUser() {
        Mockito.when(customerRepository.findOneById(CUSTOMER_ID)).thenReturn(null);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_ID_INDEX, 2)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }
}
