package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.location.LocationElementController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EventController extends TabElement implements LocalizationObserver {


    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventController.class);


    @FXML
    public Pagination pagination;
    @FXML
    private Button btSearch;
    @FXML
    private Button btAdvSearch;
    @FXML
    public Button btnAddEvent;
    @FXML
    private TextField tfSearchFor;
    @FXML
    private ChoiceBox<String> cbSearch;
    @FXML
    public BorderPane bPEventContainer;
    @FXML
    public BorderPane eventRootContainer;
    @FXML
    private TabHeaderController tabHeaderController;

    private EventSearchFor searchFor;


    private Tab eventTab;

    private String searchForArtist = BundleManager.getBundle().getString("artist.artist");
    private String searchForEvent = BundleManager.getBundle().getString("events.events");
    private String searchForLocation = BundleManager.getBundle().getString("location.location");
    private ObservableList<String> searchForList = FXCollections.observableArrayList();
    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final EventService eventService;
    private final LocationService locationService;

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private final int FONT_SIZE = 16;

    private final int EVENTS_PER_PAGE = 7;

    @Autowired
    private LocalizationSubject localizationSubject;
    @Autowired
    private PaginationHelper paginationHelper;

    public Tab getEventTab() {
        return eventTab;
    }

    public void setEventTab(Tab eventTab) {
        this.eventTab = eventTab;
    }


    public EventController(MainController mainController, SpringFxmlLoader springFxmlLoader, EventService eventService,
                           LocationService locationService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.eventService = eventService;
        this.locationService = locationService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.FILM);
        localizationSubject.attach(this);
        searchForList.add(searchForEvent);
        searchForList.add(searchForLocation);
        searchForList.add(searchForArtist);
        update();
        //todo choicebox doesnt update language of items

        cbSearch.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                if (newValue.equals(searchForEvent)) {
                    searchFor = EventSearchFor.EVENT;
                }
                else if (newValue.equals(searchForLocation)) {
                    searchFor = EventSearchFor.LOCATION;
                } else {
                    searchFor = EventSearchFor.ARTIST;
                }
            }
        );

        cbSearch.getSelectionModel().selectFirst();

        btSearch.setGraphic(fontAwesome.create("SEARCH").size(FONT_SIZE));
        paginationHelper.initData(pagination, springFxmlLoader, eventService, locationService);
    }



    public void preparePagination() {
        //all customer at start or searchfield is empty
        LOGGER.info("prepareing Pagination for the event overview");
        try {
            Pageable request = new PageRequest(0, EVENTS_PER_PAGE);
            Page<SimpleEventDTO> events = eventService.findAllUpcoming(request);
            paginationHelper.setUpPagination(events);
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access total number of events");
        } catch (SearchNoMatchException e) {
            noMatchFound();        }
    }


    @FXML
    private void search(ActionEvent actionEvent) {
        String searchText = tfSearchFor.getText();
        String searchParam;
        Pageable request = new PageRequest(0, EVENTS_PER_PAGE);

        if (searchFor.equals(EventSearchFor.EVENT)) {
            searchParam = "title:" + searchText;
            try {
                Page<SimpleEventDTO> events = eventService.findAdvanced(request, searchParam);
                paginationHelper.setUpPagination(events);
            } catch (DataAccessException e) {
                LOGGER.warn("Could not access total number of events");
            }
        }
        else if (searchFor.equals(EventSearchFor.LOCATION)) {
            LOGGER.info("prepareing Pagination for the locations overview");
            searchParam ="description:" + searchText;
            try {
                Page<SimpleLocationDTO> locations = locationService.findAdvanced(request, searchParam);
                paginationHelper.setUpPagination(locations);
            } catch (DataAccessException e) {
                LOGGER.warn("Could not access total number of locations");
            }
        }
    }

    @FXML
    public void openCSVImportWindow(ActionEvent actionEvent) {
        SpringFxmlLoader.Wrapper<EventCSVImportController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/event/addEventPerCSV.fxml");
        wrapper.getController().initializeData(EventController.this, eventRootContainer);
        BorderPane root = springFxmlLoader.load("/fxml/event/addEventPerCSV.fxml");
        eventTab.setContent(root);
    }


    private void noMatchFound() {
        LOGGER.info("no search match");
        //lbNoMatch.setVisible(true);

        //set empty tv
    }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events"));
        btAdvSearch.setText(BundleManager.getBundle().getString("events.advSearch"));
        tfSearchFor.setPromptText(BundleManager.getBundle().getString("events.searchFor"));
        searchForArtist = BundleManager.getBundle().getString("artist.artist");
        searchForEvent = BundleManager.getBundle().getString("events.events");
        searchForLocation = BundleManager.getBundle().getString("location.location");
        cbSearch.setItems(searchForList);
    }


    @FXML
    public void openAdvancedSearch(ActionEvent actionEvent) throws DataAccessException {
            LOGGER.info("opening the advanced event search dialog.");

                SpringFxmlLoader.Wrapper<EventAdvancedSearchController> wrapper =
                    springFxmlLoader.loadAndWrap("/fxml/event/eventAdvancedSearchComponent.fxml");
                wrapper.getController().initializeData(eventRootContainer);
                eventTab.setContent(wrapper.getLoadedObject());
    }

    @Override
    protected void setTab(Tab tab) {
        eventTab = tab;
    }

    public void loadEvents(){

        searchFor = EventSearchFor.ALL;
        preparePagination();

        Task<Page<SimpleEventDTO>> taskLoadEvents = new Task<>() {
            @Override
            protected Page<SimpleEventDTO> call() throws DataAccessException {
                try {
                    Pageable request = new PageRequest(0, EVENTS_PER_PAGE);
                    return eventService.findAllUpcoming(request);
                } catch (SearchNoMatchException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                preparePagination();
            }

            @Override
            protected void failed() {
                if (getValue() == null || getValue().getContent().isEmpty()) {
                    super.failed();
                    mainController.showGeneralError("Failure at load events: " + getException().getMessage());
                }
            }
        };
        taskLoadEvents.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );


        new Thread(taskLoadEvents).start();

    }



    public void publishEvent(ActionEvent actionEvent) {

        Task<Void> workerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                // TODO: (David) csvFile == Textbox
                String csvFile = "import.csv";
                String cvsSplitBy = ";";
                BufferedReader br = null;
                try {
                    String line = "";
                    br = new BufferedReader(new FileReader(csvFile));
                    int cnt = 1;
                    while ((line = br.readLine()) != null) {
                        // TITLE;ARTIST_FIRST_NAME;ARTIST_LAST_NAME;DESCRIPTION;START_OF_EVENT;END_OF_EVENT;PRICE;HALL_ID
                        String[] column = line.split(cvsSplitBy);

                        // Column size = 8

                        /*
                        System.out.println("---- " + cnt++ + " ----");
                        for (String s : column) {
                            System.out.print(s+ "\t");
                        }
                        System.out.println();
                        */


                        // TODO: (David) Check input, duplicates?, end before begin?

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                        String title = column[0];
                        String firstName = column[1];
                        String lastName = column[2];
                        String description = column[3];
                        LocalDateTime start = LocalDateTime.parse(column[4], formatter);
                        LocalDateTime end = LocalDateTime.parse(column[5], formatter);
                        Long price = Long.valueOf(column[6]);
                        DetailedHallDTO hall = DetailedHallDTO.builder().id(Long.valueOf(column[7])).description("desc").build();

                        DetailedEventDTO detailedEventDTO = DetailedEventDTO.builder()
                            .title(title)
                            //.artistFirstname(firstName) TODO: (Von Verena an David) those methods are not implemented jet, when they are remove those comments
                           // .artistLastName(lastName)
                            .description(description)
                            .startOfEvent(start)
                            .endOfEvent(end)
                            .price(price)
                            .hall(hall)
                            .build();

                        detailedEventDTO = eventService.publishEvent(detailedEventDTO);
                        //System.out.println("---- " + detailedEventDTO.getId() + " ----");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                loadEvents();
            }

            @Override
            protected void failed() {
                super.failed();
                loadEvents();
                mainController.showGeneralError("Failure at PublishEvents: " + getException().getMessage());
            }
        };

        workerTask.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        new Thread(workerTask).start();
    }

}
