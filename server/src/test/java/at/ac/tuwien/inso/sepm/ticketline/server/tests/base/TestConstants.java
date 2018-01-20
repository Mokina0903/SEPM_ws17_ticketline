package at.ac.tuwien.inso.sepm.ticketline.server.tests.base;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class TestConstants {
    public static final String SERVER_HOST = "http://localhost";


    // ---------------------- USER ----------------------
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

    // -------------------- CUSTOMER ---------------------
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

}
