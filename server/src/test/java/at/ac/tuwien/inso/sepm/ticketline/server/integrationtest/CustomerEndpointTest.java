package at.ac.tuwien.inso.sepm.ticketline.server.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.CustomerNotValidException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidIdException;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtest.base.BaseIntegrationTest;
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

    @Autowired
    private CustomerService customerService;

    @Before
    public void setUp() {
        setUpDefaultCustomers();
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

        //todo customerEndpoint returns Page instead of list, but dont know how to change code to page
      /*  Assert.assertThat(Arrays.asList(response.as(CustomerDTO[].class)), is(Collections.singletonList(
            CustomerDTO.builder()
                .id(CUSTOMER_ID)
                .knr(CUSTOMER_NUMBER)
                .name(CUSTOMER_NAME)
                .surname(CUSTOMER_SURNAME)
                .mail(CUSTOMER_MAIL)
                .birthDate(CUSTOMER_BIRTHDATE)
                .build())));*/
    }

    @Test
    public void findByCustomerNumber() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_BY_NUMBER_PATH, CUSTOMER_NUMBER)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertThat(response.as(CustomerDTO.class), is(CustomerDTO.builder()
                .id(CUSTOMER_ID)
                .knr(CUSTOMER_NUMBER)
                .name(CUSTOMER_NAME)
                .surname(CUSTOMER_SURNAME)
                .mail(CUSTOMER_MAIL)
                .birthDate(CUSTOMER_BIRTHDATE)
                .version(1)
                .build()));

    }

    @Test
    public void findByFirstname() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_BY_NAME_PATH, 0, Integer.MAX_VALUE, CUSTOMER_NAME)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

      /*  Assert.assertThat(Arrays.asList(response.as(CustomerDTO[].class)), is(Collections.singletonList(
            CustomerDTO.builder()
                .id(CUSTOMER_ID)
                .knr(CUSTOMER_NUMBER)
                .name(CUSTOMER_NAME)
                .surname(CUSTOMER_SURNAME)
                .mail(CUSTOMER_MAIL)
                .birthDate(CUSTOMER_BIRTHDATE)
                .build())));*/
    }

    @Test
    public void findBySurname() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_BY_NAME_PATH, 0, Integer.MAX_VALUE, CUSTOMER_SURNAME)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

/*        Assert.assertTrue(Arrays.asList(response.as(CustomerDTO[].class)).contains(CustomerDTO.builder()
                .id(CUSTOMER_ID)
                .knr(CUSTOMER_NUMBER)
                .name(CUSTOMER_NAME)
                .surname(CUSTOMER_SURNAME)
                .mail(CUSTOMER_MAIL)
                .birthDate(CUSTOMER_BIRTHDATE)
                .build()));*/
    }

    @Test
    public void findByNameStartsWithString() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(CUSTOMER_ENDPOINT + CUSTOMER_BY_NAME_PATH, 0, Integer.MAX_VALUE, CUSTOMER_NAME_SUBSTRING)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

       /* Assert.assertTrue(Arrays.asList(response.as(CustomerDTO[].class)).contains(
            CustomerDTO.builder()
                .id(CUSTOMER_ID)
                .knr(CUSTOMER_NUMBER)
                .name(CUSTOMER_NAME)
                .surname(CUSTOMER_SURNAME)
                .mail(CUSTOMER_MAIL)
                .birthDate(CUSTOMER_BIRTHDATE)
                .build()));*/
    }

    //todo Validation Tests fail

    @Test(expected = CustomerNotValidException.class)
    public void createCustomerWithInvalidName() throws CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name("")
            .surname(CUSTOMER_SURNAME)
            .mail(CUSTOMER_MAIL)
            .birthDate(CUSTOMER_BIRTHDATE)
            .build();
            customerService.createCustomer(customer);
            fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void createCustomerWithInvalidSurname() throws CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name(CUSTOMER_NAME)
            .surname("")
            .mail(CUSTOMER_MAIL)
            .birthDate(CUSTOMER_BIRTHDATE)
            .build();
        customerService.createCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void createCustomerWithInvalidEmail() throws CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name(CUSTOMER_NAME)
            .surname(CUSTOMER_SURNAME)
            .mail("invalid.Email")
            .birthDate(CUSTOMER_BIRTHDATE)
            .build();
        customerService.createCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void createCustomerWithInvalidBirthdate() throws CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name(CUSTOMER_NAME)
            .surname(CUSTOMER_SURNAME)
            .mail(CUSTOMER_MAIL)
            .birthDate(LocalDate.now())
            .build();
        customerService.createCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void updateCustomerWithInvalidName() throws InvalidIdException, CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name("")
            .surname(CUSTOMER_SURNAME)
            .mail(CUSTOMER_MAIL)
            .birthDate(CUSTOMER_BIRTHDATE)
            .build();
        customerService.updateCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void updateCustomerWithInvalidSurname() throws InvalidIdException, CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name(CUSTOMER_NAME)
            .surname("")
            .mail(CUSTOMER_MAIL)
            .birthDate(CUSTOMER_BIRTHDATE)
            .build();
        customerService.updateCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void updateCustomerWithInvalidEmail() throws InvalidIdException, CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name(CUSTOMER_NAME)
            .surname(CUSTOMER_SURNAME)
            .mail("invalid.Email")
            .birthDate(CUSTOMER_BIRTHDATE)
            .build();
        customerService.updateCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

    @Test(expected = CustomerNotValidException.class)
    public void updateCustomerWithInvalidBirthdate() throws InvalidIdException, CustomerNotValidException {
        Customer customer = Customer.builder()
            .id(CUSTOMER_ID)
            .knr(CUSTOMER_NUMBER)
            .name(CUSTOMER_NAME)
            .surname(CUSTOMER_SURNAME)
            .mail(CUSTOMER_MAIL)
            .birthDate(LocalDate.now())
            .build();
        customerService.updateCustomer(customer);
        fail("CustomerNotValidException expected.");
    }

}
