package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class EventController extends TabElement implements LocalizationObserver {


    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventController.class);


    @FXML
    public Pagination pagination;
    @FXML
    private TabHeaderController tabHeaderController;

    private Tab eventTab;

    private List<SimpleEventDTO> events;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final EventService eventService;

    private final int EVENTS_PER_PAGE = 7;
    private EventSearchFor searchFor;

    @Autowired
    private LocalizationSubject localizationSubject;

    public Tab getEventTab() {
        return eventTab;
    }

    public void setEventTab(Tab eventTab) {
        this.eventTab = eventTab;
    }


    public EventController(MainController mainController, SpringFxmlLoader springFxmlLoader, EventService eventService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.eventService = eventService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.FILM);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events"));
        localizationSubject.attach(this);
        /*
        lvEventElements.getSelectionModel()
            .selectedIndexProperty()
            .addListener((observable, oldvalue, newValue) -> {

                Platform.runLater(() -> {
                    lvEventElements.getSelectionModel().clearSelection();
                });

            });
            */
    }

    public void preparePagination() {
        //all customer at start or searchfield is empty
        try {
            events = eventService.findAllUpcoming(0,Integer.MAX_VALUE);
            pagination.setPageCount(events.size() / EVENTS_PER_PAGE + 1);
            preparePagination(events);
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access total number of customers");
        } catch (SearchNoMatchException e) {
            noMatchFound();        }
    }

    public void preparePagination(List<SimpleEventDTO> events) {

/*        if (customer == null || customer.isEmpty()) {
            noMatchFound();
        } else {*/
        LOGGER.info("search matches");

        //lbNoMatch.setVisible(false);
        //   int numOfCustomers = customer.size();
        //    pagination.setPageCount(numOfCustomers / CUSTOMER_PER_PAGE + 1);
        pagination.setCurrentPageIndex(0);
        //      }
        pagination.setPageFactory(new Callback<Integer, Node>() {

            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });
    }

    private List<SimpleEventDTO> loadPage(int pageIndex) {
        List<SimpleEventDTO> page = new ArrayList<SimpleEventDTO>();

        try {
            switch (searchFor) {
                case ALL:
                    events = eventService.findAllUpcoming(0, Integer.MAX_VALUE);
                    page = eventService.findAllUpcoming(pageIndex, EVENTS_PER_PAGE);
                    pagination.setPageCount(events.size() / EVENTS_PER_PAGE + 1);
                    break;
                case ARTIST:
                    /*customer = customerService.findByName(tfSearch.getText(), 0, Integer.MAX_VALUE);
                    page = customerService.findByName(tfSearch.getText(), pageIndex, CUSTOMER_PER_PAGE);
                    pagination.setPageCount(customer.size() / CUSTOMER_PER_PAGE + 1);*/

                    break;
                case PRICE:
                    /*page = eventService.findByPrice(Long.parseLong(tfSearch.getText()));
                    pagination.setPageCount(1);*/
                    break;
            }

        } catch (SearchNoMatchException e) {
            noMatchFound();
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access find events");
        }
        return page;
    }

    private Node createPage(int pageIndex) {
        pagination.setCurrentPageIndex(pageIndex);
        List<SimpleEventDTO> events = loadPage(pageIndex);
        ListView<VBox> lvEventElements = new ListView<>();
        lvEventElements.setStyle("-fx-background-color: transparent;");

        if(!events.isEmpty()){
            for(SimpleEventDTO event : events) {
                SpringFxmlLoader.Wrapper<EventElementController> wrapper =
                    springFxmlLoader.loadAndWrap("/fxml/event/eventElement.fxml");
                wrapper.getController().initializeData(eventService,event);

                lvEventElements.getItems().add(wrapper.getController().vbEventElement);
            }
        }
        return lvEventElements;
    }

    private void noMatchFound() {
        LOGGER.info("no search match");
        //lbNoMatch.setVisible(true);

        //set empty tv
    }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events"));
    }


    @Override
    protected void setTab(Tab tab) {
        eventTab = tab;
    }

    public void loadEvents(){

        searchFor = EventSearchFor.ALL; //toDO: Add Searchfunctions

        preparePagination();


/*
        ObservableList<VBox> lvEventsChildren = lvEventElements.getItems();
        lvEventsChildren.clear();


        Task<List<SimpleEventDTO>> taskNewNews = new Task<>() {
            @Override
            protected List<SimpleEventDTO> call() throws DataAccessException, InterruptedException {

                return eventService.findAll();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if (!getValue().isEmpty() ) {
                    for (SimpleEventDTO event : getValue()) {

                        SpringFxmlLoader.Wrapper<EventElementController> wrapper =
                            springFxmlLoader.loadAndWrap("/fxml/event/eventElement.fxml");
                        wrapper.getController().initializeData(eventService,event);

                        lvEventsChildren.add(wrapper.getController().vbEventElement);
                    }

                }

            }

            @Override
            protected void failed() {
                if(getValue()==null || getValue().isEmpty()) {
                    super.failed();
                    JavaFXUtils.createExceptionDialog(getException(),
                        lvEventElements.getScene().getWindow()).showAndWait();
                }
            }
        };
        taskNewNews.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );


        new Thread(taskNewNews).start();
        */
    }
}
