package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.artist.ArtistElementController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventElementController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventSearchFor;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.location.LocationElementController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ArtistService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.implementation.SimpleArtistService;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class PaginationHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventAdvancedSearchController.class);


    private Pagination pagination;
    private BorderPane bPContainer;
    private SpringFxmlLoader springFxmlLoader;
    private MainController mainController;
    private EventController controller;
    private EventService eventService;
    private LocationService locationService;
    private ArtistService artistService;
    private SimpleArtistDTO artistDTO;
    private SimpleLocationDTO locationDTO;

    private EventSearchFor searchFor;

    private final int ENTRIES_PER_PAGE = 7;

    private MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();


    public void initData(Pagination pagination, SpringFxmlLoader springFxmlLoader,
                         EventService eventService, LocationService locationService,
                         ArtistService artistService, EventController controller, MainController mainController, BorderPane bp) {
        this.pagination = pagination;
        this.springFxmlLoader = springFxmlLoader;
        this.eventService = eventService;
        this.locationService = locationService;
        this.artistService = artistService;
        this.controller = controller;
        this.mainController = mainController;
        this.bPContainer = bp;
    }

    public void setUpPagination() {

        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });
    }

    private Node createPage(Integer pageIndex) {
        ListView<VBox> lvElements;

        if (searchFor.equals(EventSearchFor.EVENT) || searchFor.equals(EventSearchFor.ALL)) { //todo
            lvElements = loadEvents(pageIndex);
        }
        else if (searchFor.equals(EventSearchFor.LOCATION)) {
            lvElements = loadLocations(pageIndex);

        } else {
            lvElements = loadArtists(pageIndex);
        }
        return lvElements;
    }


    private ListView<VBox> loadEvents(Integer pageIndex) {
        mainController.setGeneralErrorUnvisable();
        ListView<VBox> lvElements = new ListView<>();

        Task<Page<SimpleEventDTO>> taskloadEvents = new Task<>() {
            Page<SimpleEventDTO> events;
            @Override
            protected Page<SimpleEventDTO> call() throws DataAccessException {
                Pageable request = new PageRequest(pageIndex, ENTRIES_PER_PAGE);
                if(searchFor.equals(EventSearchFor.EVENT)) {
                    events = eventService.find(request, parameters);
                } else if(searchFor.equals(EventSearchFor.ALL)){
                    events = eventService.findAdvanced(request,parameters);
                } else if (searchFor.equals(EventSearchFor.EVENTS_BY_ARTIST)) {
                 //todo   events = eventService.findByArtist(request, artistDTO);
                }
                else {
               //     events = eventService.findByLocation(request, locationDTO);
                }
                return events;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                loadEventElements(events, lvElements);
            }

            @Override
            protected void failed() {
                super.failed();
                mainController.showGeneralError("Failure at load Events: " + getException().getMessage());
            }
        };

        taskloadEvents.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        new Thread(taskloadEvents).start();
        return lvElements;
    }

    private ListView<VBox> loadLocations(Integer pageIndex) {
        mainController.setGeneralErrorUnvisable();
        ListView<VBox> lvElements = new ListView<>();

        Task<Page<SimpleLocationDTO>> taskloadEvents = new Task<>() {
            Page<SimpleLocationDTO> locations;
            @Override
            protected Page<SimpleLocationDTO> call() throws DataAccessException {
                Pageable request = new PageRequest(pageIndex, ENTRIES_PER_PAGE);
                locations = locationService.find(request, parameters);
                return locations;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                loadLocationElements(locations, lvElements);
            }

            @Override
            protected void failed() {
                super.failed();
                mainController.showGeneralError("Failure at load Events: " + getException().getMessage());
            }
        };

        taskloadEvents.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        new Thread(taskloadEvents).start();
        return lvElements;
    }

    private ListView<VBox> loadArtists(Integer pageIndex) {
        mainController.setGeneralErrorUnvisable();
        ListView<VBox> lvElements = new ListView<>();

        Task<Page<SimpleArtistDTO>> taskloadEvents = new Task<>() {
            Page<SimpleArtistDTO> artists;

            @Override
            protected Page<SimpleArtistDTO> call() throws DataAccessException {
                Pageable request = new PageRequest(pageIndex, ENTRIES_PER_PAGE);
                artists = artistService.find(request, parameters);
                return artists;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                loadArtistElements(artists, lvElements);
            }

            @Override
            protected void failed() {
                super.failed();
                mainController.showGeneralError("Failure at load Events: " + getException().getMessage());
            }
        };

        taskloadEvents.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        new Thread(taskloadEvents).start();
        return lvElements;
    }

    private void loadEventElements(Page<SimpleEventDTO> events, ListView<VBox> lvElements) {
        pagination.setPageCount(events.getTotalPages());

        lvElements.setStyle("-fx-background-color: transparent;");
        if (!events.getContent().isEmpty()) {
            for (SimpleEventDTO element : events.getContent()) {
                SpringFxmlLoader.Wrapper<EventElementController> wrapper =
                    springFxmlLoader.loadAndWrap("/fxml/event/eventElement.fxml");
                wrapper.getController().initializeData(eventService, element, bPContainer);
                lvElements.getItems().add(wrapper.getController().vbElement);
            }
        }
        controller.setMatchInfoLabel(events.getTotalElements());
    }

    private void loadLocationElements(Page<SimpleLocationDTO> locations, ListView<VBox> lvElements) {
        pagination.setPageCount(locations.getTotalPages());
        lvElements.setStyle("-fx-background-color: transparent;");
        if (!locations.getContent().isEmpty()) {
            for (SimpleLocationDTO element : locations.getContent()) {
                SpringFxmlLoader.Wrapper<LocationElementController> wrapper =
                    springFxmlLoader.loadAndWrap("/fxml/location/locationElement.fxml");
                wrapper.getController().initializeData(locationService, element, bPContainer);
                lvElements.getItems().add(wrapper.getController().vbElement);
            }
        }
        controller.setMatchInfoLabel(locations.getTotalElements());

    }

    private void loadArtistElements(Page<SimpleArtistDTO> artists, ListView<VBox> lvElements) {
        pagination.setPageCount(artists.getTotalPages());
        lvElements.setStyle("-fx-background-color: transparent;");
        if (!artists.getContent().isEmpty()) {
            for (SimpleArtistDTO element : artists.getContent()) {
                SpringFxmlLoader.Wrapper<ArtistElementController> wrapper =
                    springFxmlLoader.loadAndWrap("/fxml/artist/artistElement.fxml");
                wrapper.getController().initializeData(artistService, element, bPContainer);
                lvElements.getItems().add(wrapper.getController().vbElement);
            }
        }
        controller.setMatchInfoLabel(artists.getTotalElements());
    }

    private Page<SimpleEventDTO> loadAdvancedSearchEventPage(Integer pageIndex) {
        Pageable request = new PageRequest(pageIndex, ENTRIES_PER_PAGE);
        try {
            Page<SimpleEventDTO> events = eventService.findAdvanced(request, parameters);
            return events;
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access data for event pagination");
        }
        return null;
    }

    private Page<SimpleEventDTO> loadEventPage(Integer pageIndex) {
        Pageable request = new PageRequest(pageIndex, ENTRIES_PER_PAGE);
        try {
            Page<SimpleEventDTO> events = eventService.find(request, parameters);
            return events;
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access data for event pagination");
        }
        return null;
    }

    private Page<SimpleLocationDTO> loadLocationPage(Integer pageIndex) {
        Pageable request = new PageRequest(pageIndex, ENTRIES_PER_PAGE);
        try {
            Page<SimpleLocationDTO> locations = locationService.find(request, parameters);
            return locations;
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access data for event pagination");
        }
        return null;
    }

    private Page<SimpleArtistDTO> loadArtistPage(Integer pageIndex) {
        Pageable request = new PageRequest(pageIndex, ENTRIES_PER_PAGE);
        try {
            Page<SimpleArtistDTO> artists = artistService.find(request, parameters);
            return artists;
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access data for event pagination");
        }
        return null;
    }

    public EventSearchFor getSearchFor() {
        return searchFor;
    }

    public void setSearchFor(EventSearchFor searchFor) {
        this.searchFor = searchFor;
    }


    public MultiValueMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(MultiValueMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public EventController getController() {
        return controller;
    }

    public void setController(EventController controller) {
        this.controller = controller;
    }

    public SimpleArtistDTO getArtistDTO() {
        return artistDTO;
    }

    public void setArtistDTO(SimpleArtistDTO artistDTO) {
        this.artistDTO = artistDTO;
    }

    public SimpleLocationDTO getLocationDTO() {
        return locationDTO;
    }

    public void setLocationDTO(SimpleLocationDTO locationDTO) {
        this.locationDTO = locationDTO;
    }
}
