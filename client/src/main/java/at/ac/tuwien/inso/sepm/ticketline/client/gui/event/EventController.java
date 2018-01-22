package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ArtistService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
    public BorderPane eventRootContainer;
    @FXML
    public Label lbMatchInfo;
    @FXML
    private TabHeaderController tabHeaderController;

    private EventSearchFor searchFor = EventSearchFor.EVENT;
    private long numberOfMatches;


    private Tab eventTab;

    private String searchForArtist = BundleManager.getBundle().getString("artist.artist");
    private String searchForEvent = BundleManager.getBundle().getString("events.events");
    private String searchForLocation = BundleManager.getBundle().getString("location.location");
    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final EventService eventService;
    private final LocationService locationService;
    private final ArtistService artistService;

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private final int FONT_SIZE = 16;

    private final int EVENTS_PER_PAGE = 7;


    @Autowired
    private LocalizationSubject localizationSubject;
    @Autowired
    private PaginationHelper paginationHelper;

    public PaginationHelper getPaginationHelper() {
        return paginationHelper;
    }

    public void setPaginationHelper(PaginationHelper paginationHelper) {
        this.paginationHelper = paginationHelper;
    }

    public Tab getEventTab() {
        return eventTab;
    }

    public void setEventTab(Tab eventTab) {
        this.eventTab = eventTab;
    }


    public EventController(MainController mainController, SpringFxmlLoader springFxmlLoader, EventService eventService,
                           LocationService locationService, ArtistService artistService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.eventService = eventService;
        this.locationService = locationService;
        this.artistService = artistService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.FILM);
        localizationSubject.attach(this);
        btnAddEvent.setGraphic(fontAwesome.create("PLUS"));

        ObservableList<String> searchForList = FXCollections.observableArrayList();
        searchForList.addAll(searchForEvent, searchForLocation, searchForArtist);
        update();
        lbMatchInfo.setVisible(false);

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
            }
        );

        cbSearch.getSelectionModel().selectFirst();

        btSearch.setGraphic(fontAwesome.create("SEARCH").size(FONT_SIZE));
        paginationHelper.initData(pagination, springFxmlLoader, eventService, locationService, artistService, this, mainController, eventRootContainer);
    }


    @FXML
    private void search() {
        MultiValueMap<String, String> parameters;
        if (searchFor.equals(EventSearchFor.EVENT)) {
            LOGGER.info("preparing Pagination for the event search");
            parameters = setParametersForEventSearch();

        } else if (searchFor.equals(EventSearchFor.LOCATION)) {
            LOGGER.info("preparing Pagination for the locations search");
            parameters = setParametersForLocationSearch();
        } else {
            LOGGER.info("preparing Pagination for the artist search");
            parameters = setParametersForArtistSearch();
        }
        paginationHelper.setParameters(parameters);
        paginationHelper.setUpPagination();
    }


    private MultiValueMap<String, String> setParametersForEventSearch() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (!tfSearchFor.getText().isEmpty() && !tfSearchFor.getText().equals(" ")) {
            params.add("title", tfSearchFor.getText());
            params.add("description", tfSearchFor.getText());
        }
        return params;
    }

    private MultiValueMap<String, String> setParametersForLocationSearch() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (!tfSearchFor.getText().isEmpty() && !tfSearchFor.getText().equals(" ")) {
            if (isNumeric(tfSearchFor.getText())) {
                params.add("zip", tfSearchFor.getText());
            } else {
                params.add("descriptionEvent", tfSearchFor.getText());
                params.add("city", tfSearchFor.getText());
                params.add("street", tfSearchFor.getText());
            }
        }
        return params;
    }

    private MultiValueMap<String, String> setParametersForArtistSearch() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (!tfSearchFor.getText().isEmpty() && !tfSearchFor.getText().equals(" ")) {
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

    @FXML
    public void openAdvancedSearch(ActionEvent actionEvent) {
        LOGGER.info("opening the advanced event search dialog.");
        searchFor = EventSearchFor.ALL;

        SpringFxmlLoader.Wrapper<EventAdvancedSearchController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/event/eventAdvancedSearchComponent.fxml");
        wrapper.getController().initializeData(eventRootContainer);
        eventTab.setContent(wrapper.getLoadedObject());
    }

    public void loadEvents() {
        search();
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

    public void setMatchInfoLabel(long matches) {
        if (matches == 0) {
            lbMatchInfo.setText(BundleManager.getBundle().getString("general.noMatches"));
            lbMatchInfo.setTextFill(Color.CRIMSON);
        } else {
            lbMatchInfo.setText(BundleManager.getBundle().getString("general.matches") + " " + matches);
            lbMatchInfo.setTextFill(Color.BLACK);
        }
        numberOfMatches = matches;
        lbMatchInfo.setVisible(true);
    }

    @Override
    protected void setTab(Tab tab) {
        eventTab = tab;
    }


    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events"));
        btAdvSearch.setText(BundleManager.getBundle().getString("events.advSearch"));
        if (numberOfMatches == 0) {
            lbMatchInfo.setText(BundleManager.getBundle().getString("general.noMatches"));
        } else {
            lbMatchInfo.setText(BundleManager.getBundle().getString("general.matches") + " " + numberOfMatches);
        }
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
}
