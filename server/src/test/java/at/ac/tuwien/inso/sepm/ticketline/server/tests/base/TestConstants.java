package at.ac.tuwien.inso.sepm.ticketline.server.tests.base;

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




}
