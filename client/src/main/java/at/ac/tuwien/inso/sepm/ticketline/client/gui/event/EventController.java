package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import io.swagger.models.auth.In;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    private EventSearchFor searchFor = EventSearchFor.EVENT;


    private Tab eventTab;

    private String searchForArtist = BundleManager.getBundle().getString("artist.artist");
    private String searchForEvent = BundleManager.getBundle().getString("events.events");
    private String searchForLocation = BundleManager.getBundle().getString("location.location");
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
        ObservableList<String> searchForList = FXCollections.observableArrayList();
        searchForList.addAll(searchForEvent, searchForLocation, searchForArtist);
        update();
        //todo matchNotFound label
        //todo show free seats for events

        cbSearch.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals(searchForEvent)) {
                    searchFor = EventSearchFor.EVENT;
                    tfSearchFor.setPromptText(BundleManager.getBundle().getString("events.searchForEvent"));
                } else if (newValue.equals(searchForLocation)) {
                    searchFor = EventSearchFor.LOCATION;
                    tfSearchFor.setPromptText(BundleManager.getBundle().getString("events.searchForLocation"));

                } else {
                    searchFor = EventSearchFor.ARTIST;
                    tfSearchFor.setPromptText(BundleManager.getBundle().getString("events.searchForArtist"));
                }
            }
                paginationHelper.setSearchFor(searchFor);
            System.out.println(searchFor);
            }
        );

        cbSearch.getSelectionModel().selectFirst();

        btSearch.setGraphic(fontAwesome.create("SEARCH").size(FONT_SIZE));
        paginationHelper.initData(pagination, springFxmlLoader, eventService, locationService, this);
    }


    public void preparePagination() {
        //all customer at start or searchfield is empty
        LOGGER.info("preparing Pagination for the event overview");
            search();
    }

    @FXML
    private void search() {
        MultiValueMap<String, String> parameters;
        if (searchFor.equals(EventSearchFor.EVENT)) {
            LOGGER.info("preparing Pagination for the event search");
            parameters = setParametersForEventSearch();
            paginationHelper.setParameters(parameters);
            paginationHelper.setUpPagination();

        } else if (searchFor.equals(EventSearchFor.LOCATION)) {
            LOGGER.info("preparing Pagination for the locations search");
            parameters = setParametersForLocationSearch();
            paginationHelper.setParameters(parameters);
            paginationHelper.setUpPagination();
        }
        //todo artists
    }


    private MultiValueMap<String,String> setParametersForEventSearch() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (!tfSearchFor.getText().isEmpty() && !tfSearchFor.getText().equals("")) {
            params.add("title", tfSearchFor.getText());
            params.add("description", tfSearchFor.getText());
        }
        return params;
    }

    private MultiValueMap<String,String> setParametersForLocationSearch() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (!tfSearchFor.getText().isEmpty() && !tfSearchFor.getText().equals("")) {
            if (isNumeric(tfSearchFor.getText())) {
                params.add("zip", tfSearchFor.getText());
            }
            else {
                params.add("description", tfSearchFor.getText());
                params.add("city", tfSearchFor.getText());
                params.add("street", tfSearchFor.getText());
            }
        }
        return params;
    }

    private MultiValueMap<String,String> setParametersForArtistSearch() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (!tfSearchFor.getText().isEmpty() && !tfSearchFor.getText().equals("")) {
            params.add("artistFirstName", tfSearchFor.getText());
            params.add("artistLastName", tfSearchFor.getText());
        }
        return params;
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
        searchForArtist = BundleManager.getBundle().getString("artist.artist");
        searchForEvent = BundleManager.getBundle().getString("events.events");
        searchForLocation = BundleManager.getBundle().getString("location.location");
        ObservableList<String> searchForList = FXCollections.observableArrayList();
        searchForList.addAll(searchForEvent, searchForLocation, searchForArtist);
        cbSearch.setItems(searchForList);
        if (searchFor.equals(EventSearchFor.EVENT)) {
            tfSearchFor.setPromptText(BundleManager.getBundle().getString("events.searchForEvent"));
        } else if (searchFor.equals(EventSearchFor.LOCATION)) {
            tfSearchFor.setPromptText(BundleManager.getBundle().getString("events.searchForLocation"));
        } else {
            tfSearchFor.setPromptText(BundleManager.getBundle().getString("events.searchForArtist"));
        }
    }


    @FXML
    public void openAdvancedSearch(ActionEvent actionEvent) {
        LOGGER.info("opening the advanced event search dialog.");
        searchFor = EventSearchFor.ALL;

        SpringFxmlLoader.Wrapper<EventAdvancedSearchController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/event/eventAdvancedSearchComponent.fxml");
        wrapper.getController().initializeData(eventRootContainer);
        eventTab.setContent(wrapper.getLoadedObject());
    }

    @Override
    protected void setTab(Tab tab) {
        eventTab = tab;
    }

    public void loadEvents() {

        paginationHelper.setSearchFor(EventSearchFor.EVENT);
        Task<Page<SimpleEventDTO>> taskLoadEvents = new Task<>() {
            @Override
            protected Page<SimpleEventDTO> call() throws DataAccessException {
                try {
                    Pageable request = new PageRequest(0, EVENTS_PER_PAGE);
                    return eventService.findAllUpcoming(request);
                } catch (SearchNoMatchException e) {
                   // e.printStackTrace();
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

    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
