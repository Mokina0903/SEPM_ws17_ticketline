package at.ac.tuwien.inso.sepm.ticketline.server.tests.base;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class TestConstants {
    public static final String SERVER_HOST = "http://localhost";

    // ---------------------- USER ----------------------
    public static final String USER_ENDPOINT = "/user";
    public static final String USER_ENDPOINT_BLOCK = "/user/block";
    public static final String USER_ENDPOINT_UNBLOCK = "/user/unblock";
    public static final String USER_ENDPOINT_FIND = "/user/find/{userName}";
    public static final String USER_ENDPOINT_RESET = "/user/resetPassword";
    public static final String USER_ENDPOINT_IS_BLOCKED = "/user/isBlocked/{username}";
    public static final String SPECIFIC_USER_PATH = "/{userId}";

    public static final String TEST_USER_TEXT = "TestUserText";
    public static final String TEST_USER_TITLE = "title";
    public static final LocalDateTime TEST_USER_PUBLISHED_AT =
        LocalDateTime.of(2016, 11, 13, 12, 15, 0, 0);
    public static final long TEST_USER_ID = 1L;


    public static final long   ADMIN_ID = 1L;
    public static final String ADMIN_PASSWORD = "password";
    public static final String ADMIN_USERNAME = "admin";

    public static final long   USER_ID = 2L;
    public static final String USER_USERNAME = "user";
    public static final String USER_PASSWORD = "password";


    // ---------------------- NEWS ----------------------
    public static final String NEWS_ENDPOINT = "/news";
    public static final String SPECIFIC_NEWS_PATH = "/{newsId}";

    public static final String NEWS_TEXT = "TestNewsText";
    public static final String NEWS_TITLE = "title";
    public static final LocalDateTime NEWS_PUBLISHED_AT =
        LocalDateTime.of(2016, 11, 13, 12, 15, 0, 0);
    public static final long NEWS_ID = 1L;

    // -------------------- CUSTOMER --------------------
    public static final String CUSTOMER_ENDPOINT = "/customer";
    public static final String CUSTOMER_CREATE_PATH = "/create";
    public static final String CUSTOMER_UPDATE_PATH = "/update";
    public static final String CUSTOMER_BY_NUMBER_PATH = "/findWithKnr/{knr}";
    public static final String CUSTOMER_BY_NAME_PATH = "/findName/{pageIndex}/{customerPerPage}/{name}";

    public static final String CUSTOMER_PAGE_INDEX = "/{pageIndex}";
    public static final String CUSTOMER_ID_INDEX = "/{id}";
    public static final String CUSTOMER_PER_PAGE = "/{customerPerPage}";

    public static final long CUSTOMER_ID = 1L;
    public static final long CUSTOMER_NUMBER = 9999L;
    public static final String  CUSTOMER_NAME = "Max";
    public static final String  CUSTOMER_SURNAME = "Mustermann";
    public static final String  CUSTOMER_MAIL = "Maxmustermann@gmail.com";
    public static final LocalDate CUSTOMER_BIRTHDATE = LocalDate.of(1950, 1, 1);
    public static final String  CUSTOMER_NAME_SUBSTRING = "muste";

    // ---------------------- EVENT ---------------------
    public static final String EVENT_ENDPOINT = "/event";
    public static final long EVENT_ID = 1L;
    public static final String EVENT_DESCRIPTION = "Event Description";
    public static final String EVENT_TITLE = "Event Title";
    public static final long EVENT_PRICE = 100;
    public static final Event.EventCategory EVENT_CATEGORY = Event.EventCategory.Musical;
    public static LocalDateTime EVENT_START =
        LocalDateTime.of(2017, 11, 29, 12, 15, 0, 0);
    public static LocalDateTime EVENT_ENDE = EVENT_START.plusHours(2);

    // --------------------- ARTIST ---------------------
    public static final Long ARTIST_ID = 1L;
    public static final String ARTIST_FIRSTNAME = "Firstname";
    public static final String ARTIST_LASTNAME = "Lastname";

    // --------------------- ARTIST ---------------------
    public static final String TICKET_ENDPOINT = "/tickets";
    public static final String TICKET_CUSTOMER = "/tickets/customer/{customerId}";
    public static final String TICKET_EVENT = "/tickets/event/{eventId}";
    public static final String TICKET_EVENT_SECTOR = "/tickets/event/{eventId}/{sector}";
    public static final String TICKET_EVENT_SEAT = "/tickets/isBooked/{eventId}/{seatId}";
    public static final String TICKET_FREE = "/tickets/isFree/{eventId}/{sector}";
    public static final String TICKET_TICKET = "/tickets/{ticketId}";
    public static final String TICKET_PAGE = "/tickets/{pageIndex}/{ticketsPerPage}";
    // -------------------- LOCATION --------------------
    public static final long LOCATION_ID = 1L;
    public static final String LOCATION_DESCRIPTION = "description";
    public static final String LOCATION_CITY= "TestCity";
    public static final String LOCATION_COUNTRY = "TestCountry";
    public static final int LOCATION_HOUSENR = 123;
    public static final String LOCATION_STREET = "TestStreet";
    public static final int LOCATION_ZIP = 1234;

    // ---------------------- HALL ----------------------
    public static final long HALL_ID = 1L;
    public static final String HALL_DESCRIPTION = "Test Hall 1";

    // ---------------------- SEAT ----------------------
    public static final Long SEAT_ID = 1L;
    public static final int SEAT_NR = 1;
    public static final char SEAT_SECTOR = 'a';
    public static final int SEAT_ROW = 1;

    // --------------------- TICKET ---------------------
    public static final Long TICKET_RESERVATIONNR = 10001L;
    public static final Long TICKET_ID = 1L;
    public static final long TICKET_PRICE = 100;

    // -------------------- INVOiCE ---------------------
    public static final String INVOICE_ENDPOINT_CREATE = "/invoice/create";
    public static final String INVOICE_ENDPOINT_UPDATE = "/invoice/update";
    public static final String INVOICE_ENDPOINT_NEWPDF = "/invoice/newPdf";
    public static final String INVOICE_ENDPOINT_NR = "/invoice/rNr/{reservationNumber}";
    public static final String INVOICE_ENDPOINT_INNR = "/invoice/{invoiceId}";
    public static final String INVOICE_ENDPOINT_LIST = "/invoice/{pageIndex}/{invoicesPerPage}";


}
