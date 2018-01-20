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
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
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
    private EventController controller;
    private EventService eventService;
    private LocationService locationService;
    private ArtistService artistService;
    private EventSearchFor searchFor;

    private final int ENTRIES_PER_PAGE = 7;

    private MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();


    public void initData(Pagination pagination, SpringFxmlLoader springFxmlLoader,
                         EventService eventService, LocationService locationService,
                         ArtistService artistService, EventController controller) {
        this.pagination = pagination;
        this.springFxmlLoader = springFxmlLoader;
        this.eventService = eventService;
        this.locationService = locationService;
        this.artistService = artistService;
        this.controller = controller;
    }

    public void setUpPagination() {
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });
    }


    private Node createPage(Integer pageIndex) {
        pagination.setCurrentPageIndex(pageIndex);
        ListView<VBox> lvElements = new ListView<>();

        Page<SimpleEventDTO> events;
        if (searchFor.equals(EventSearchFor.EVENT) || searchFor.equals(EventSearchFor.ALL)) {
            if (searchFor.equals(EventSearchFor.EVENT)) {
                events = loadEventPage(pageIndex);
                pagination.setPageCount(events.getTotalPages());
            } else {
                events = loadAdvancedSearchEventPage(pageIndex);
                pagination.setPageCount(events.getTotalPages());
            }

            System.out.println("ELEMENTS per PAge event create  !!!!!!!! + " + events.getContent().size());
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
            return lvElements;

        } else if (searchFor.equals(EventSearchFor.LOCATION)) {
            Page<SimpleLocationDTO> locations = loadLocationPage(pageIndex);
            pagination.setPageCount(locations.getTotalPages());
            System.out.println("ELEMENTS per PAge loc create  !!!!!!!! + " + locations.getContent().size());
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
            return lvElements;
        } else {
            Page<SimpleArtistDTO> artists = loadArtistPage(pageIndex);
            pagination.setPageCount(artists.getTotalPages());
            System.out.println("ELEMENTS per PAge art create  !!!!!!!! + " + artists.getTotalElements());
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
            return lvElements;
        }
    }

    //todo TASK

        private Page<SimpleEventDTO> loadAdvancedSearchEventPage (Integer pageIndex){
            Pageable request = new PageRequest(pageIndex, ENTRIES_PER_PAGE);
            try {
                Page<SimpleEventDTO> events = eventService.findAdvanced(request, parameters);
                return events;
            } catch (DataAccessException e) {
                LOGGER.warn("Could not access data for event pagination");
            }
            return null;
        }

        private Page<SimpleEventDTO> loadEventPage (Integer pageIndex){
            Pageable request = new PageRequest(pageIndex, ENTRIES_PER_PAGE);
            try {
                Page<SimpleEventDTO> events = eventService.find(request, parameters);
                return events;
            } catch (DataAccessException e) {
                LOGGER.warn("Could not access data for event pagination");
            }
            return null;
        }

        private Page<SimpleLocationDTO> loadLocationPage (Integer pageIndex){
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

        public EventSearchFor getSearchFor () {
            return searchFor;
        }

        public void setSearchFor (EventSearchFor searchFor){
            this.searchFor = searchFor;
        }


        public MultiValueMap<String, String> getParameters () {
            return parameters;
        }

        public void setParameters (MultiValueMap < String, String > parameters){
            this.parameters = parameters;
        }

        public EventController getController () {
            return controller;
        }

        public void setController (EventController controller){
            this.controller = controller;
        }
    }
