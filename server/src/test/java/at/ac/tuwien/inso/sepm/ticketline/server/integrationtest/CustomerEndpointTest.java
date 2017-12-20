package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.CustomerNotValidException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidIdException;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static junit.framework.TestCase.fail;
import static org.hamcrest.core.Is.is;

public class CustomerEndpointTest extends BaseIntegrationTest{

    private static final String CUSTOMER_ENDPOINT = "/customer";
    private static final String CUSTOMER_CREATE_PATH = "/create";
    private static final String CUSTOMER_UPDATE_PATH = "/update";
    private static final String CUSTOMER_BY_NUMBER_PATH = "/findWithKnr/{knr}";
    private static final String CUSTOMER_BY_NAME_PATH = "/findName/{pageIndex}/{customerPerPage}/{name}";

    private static final String PAGE_INDEX = "/{pageIndex}";
    private static final String CUSTOMER_PER_PAGE = "/{customerPerPage}";

    private static final long TEST_CUSTOMER_ID = 1L;
    private static final long TEST_CUSTOMER_NUMBER = 9999L;
    private static final String  TEST_CUSTOMER_NAME = "Max";
    private static final String  TEST_CUSTOMER_SURNAME = "Mustermann";
    private static final String  TEST_CUSTOMER_MAIL = "Maxmustermann@gmail.com";
    private static final LocalDate TEST_CUSTOMER_BIRTHDATE = LocalDate.of(1950, 1, 1);
    private static final String  TEST_CUSTOMER_NAME_SUBSTRING = "muste";


    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;

    @Before
    public void addCustomer() {
        Customer customer = Customer.builder()
            .id(TEST_CUSTOMER_ID)
            .knr(TEST_CUSTOMER_NUMBER)
            .name(TEST_CUSTOMER_NAME)
            .surname(TEST_CUSTOMER_SURNAME)
            .mail(TEST_CUSTOMER_MAIL)
            .birthDate(TEST_CUSTOMER_BIRTHDATE)
            .build();
        customerRepository.save(customer);
    }

    @Test
    public void findAllCustomerUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(CUSTOMER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllCustomerAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + PAGE_INDEX + CUSTOMER_PER_PAGE, 0, Integer.MAX_VALUE)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertThat(Arrays.asList(response.as(CustomerDTO[].class)), is(Collections.singletonList(
            CustomerDTO.builder()
                .id(TEST_CUSTOMER_ID)
                .knr(TEST_CUSTOMER_NUMBER)
                .name(TEST_CUSTOMER_NAME)
                .surname(TEST_CUSTOMER_SURNAME)
                .mail(TEST_CUSTOMER_MAIL)
                .birthDate(TEST_CUSTOMER_BIRTHDATE)
                .build())));
    }

    @Test
    public void findByCustomerNumber() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_BY_NUMBER_PATH, TEST_CUSTOMER_NUMBER)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertThat(response.as(CustomerDTO.class), is(CustomerDTO.builder()
                .id(TEST_CUSTOMER_ID)
                .knr(TEST_CUSTOMER_NUMBER)
                .name(TEST_CUSTOMER_NAME)
                .surname(TEST_CUSTOMER_SURNAME)
                .mail(TEST_CUSTOMER_MAIL)
                .birthDate(TEST_CUSTOMER_BIRTHDATE)
                .build()));

    }

    @Test
    public void findByFirstname() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_BY_NAME_PATH, 0, Integer.MAX_VALUE, TEST_CUSTOMER_NAME)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertThat(Arrays.asList(response.as(CustomerDTO[].class)), is(Collections.singletonList(
            CustomerDTO.builder()
                .id(TEST_CUSTOMER_ID)
                .knr(TEST_CUSTOMER_NUMBER)
                .name(TEST_CUSTOMER_NAME)
                .surname(TEST_CUSTOMER_SURNAME)
                .mail(TEST_CUSTOMER_MAIL)
                .birthDate(TEST_CUSTOMER_BIRTHDATE)
                .build())));
    }

    @Test
    public void findBySurname() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_BY_NAME_PATH, 0, Integer.MAX_VALUE, TEST_CUSTOMER_SURNAME)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertTrue(Arrays.asList(response.as(CustomerDTO[].class)).contains(CustomerDTO.builder()
                .id(TEST_CUSTOMER_ID)
                .knr(TEST_CUSTOMER_NUMBER)
                .name(TEST_CUSTOMER_NAME)
                .surname(TEST_CUSTOMER_SURNAME)
                .mail(TEST_CUSTOMER_MAIL)
                .birthDate(TEST_CUSTOMER_BIRTHDATE)
                .build()));
    }

    @Test
    public void findByNameStartsWithString() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_BY_NAME_PATH, 0, Integer.MAX_VALUE, TEST_CUSTOMER_NAME_SUBSTRING)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertTrue(Arrays.asList(response.as(CustomerDTO[].class)).contains(
            CustomerDTO.builder()
                .id(TEST_CUSTOMER_ID)
                .knr(TEST_CUSTOMER_NUMBER)
                .name(TEST_CUSTOMER_NAME)
                .surname(TEST_CUSTOMER_SURNAME)
                .mail(TEST_CUSTOMER_MAIL)
                .birthDate(TEST_CUSTOMER_BIRTHDATE)
                .build()));
    }

    //todo Validation Tests fail

    @Test(expected = CustomerNotValidException.class)
    public void createCustomerWithInvalidName() throws CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(TEST_CUSTOMER_ID)
            .knr(TEST_CUSTOMER_NUMBER)
            .name("")
            .surname(TEST_CUSTOMER_SURNAME)
            .mail(TEST_CUSTOMER_MAIL)
            .birthDate(TEST_CUSTOMER_BIRTHDATE)
            .build();
            customerService.createCustomer(customer);
            fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void createCustomerWithInvalidSurname() throws CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(TEST_CUSTOMER_ID)
            .knr(TEST_CUSTOMER_NUMBER)
            .name(TEST_CUSTOMER_NAME)
            .surname("")
            .mail(TEST_CUSTOMER_MAIL)
            .birthDate(TEST_CUSTOMER_BIRTHDATE)
            .build();
        customerService.createCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void createCustomerWithInvalidEmail() throws CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(TEST_CUSTOMER_ID)
            .knr(TEST_CUSTOMER_NUMBER)
            .name(TEST_CUSTOMER_NAME)
            .surname(TEST_CUSTOMER_SURNAME)
            .mail("invalid.Email")
            .birthDate(TEST_CUSTOMER_BIRTHDATE)
            .build();
        customerService.createCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void createCustomerWithInvalidBirthdate() throws CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(TEST_CUSTOMER_ID)
            .knr(TEST_CUSTOMER_NUMBER)
            .name(TEST_CUSTOMER_NAME)
            .surname(TEST_CUSTOMER_SURNAME)
            .mail(TEST_CUSTOMER_MAIL)
            .birthDate(LocalDate.now())
            .build();
        customerService.createCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void updateCustomerWithInvalidName() throws InvalidIdException, CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(TEST_CUSTOMER_ID)
            .knr(TEST_CUSTOMER_NUMBER)
            .name("")
            .surname(TEST_CUSTOMER_SURNAME)
            .mail(TEST_CUSTOMER_MAIL)
            .birthDate(TEST_CUSTOMER_BIRTHDATE)
            .build();
        customerService.updateCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void updateCustomerWithInvalidSurname() throws InvalidIdException, CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(TEST_CUSTOMER_ID)
            .knr(TEST_CUSTOMER_NUMBER)
            .name(TEST_CUSTOMER_NAME)
            .surname("")
            .mail(TEST_CUSTOMER_MAIL)
            .birthDate(TEST_CUSTOMER_BIRTHDATE)
            .build();
        customerService.updateCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void updateCustomerWithInvalidEmail() throws InvalidIdException, CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(TEST_CUSTOMER_ID)
            .knr(TEST_CUSTOMER_NUMBER)
            .name(TEST_CUSTOMER_NAME)
            .surname(TEST_CUSTOMER_SURNAME)
            .mail("invalid.Email")
            .birthDate(TEST_CUSTOMER_BIRTHDATE)
            .build();
        customerService.updateCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void updateCustomerWithInvalidBirthdate() throws InvalidIdException, CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(TEST_CUSTOMER_ID)
            .knr(TEST_CUSTOMER_NUMBER)
            .name(TEST_CUSTOMER_NAME)
            .surname(TEST_CUSTOMER_SURNAME)
            .mail(TEST_CUSTOMER_MAIL)
            .birthDate(LocalDate.now())
            .build();
        customerService.updateCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

}
