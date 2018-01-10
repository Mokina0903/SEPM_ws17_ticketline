package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
    public Button btnAddEvent;
    @FXML
    public BorderPane eventRootContainer;
    @FXML
    private TabHeaderController tabHeaderController;

    private Tab eventTab;

    private Page<SimpleEventDTO> events;

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
            Pageable request = new PageRequest(0, EVENTS_PER_PAGE);
            int amount = eventService.findAllUpcoming(request).getTotalPages();
            Page<SimpleEventDTO> page1 = eventService.findAllUpcoming(request);
            events = eventService.findAllUpcoming(request);
            pagination.setPageCount(amount);
            //todo pages instead of content
            preparePagination(events);
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access total number of customers");
        } catch (SearchNoMatchException e) {
            noMatchFound();        }
    }

    public void preparePagination(Page<SimpleEventDTO> events) {

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

    @FXML
    public void openCSVImportWindow(ActionEvent actionEvent) {
        SpringFxmlLoader.Wrapper<EventCSVImportController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/event/addEventPerCSV.fxml");
        wrapper.getController().initializeData(EventController.this, eventRootContainer);
        BorderPane root = springFxmlLoader.load("/fxml/event/addEventPerCSV.fxml");
        eventTab.setContent(root);
    }

    private Page<SimpleEventDTO> loadPage(int pageIndex) {

        try {
            switch (searchFor) {
                case ALL:
                    Pageable request = new PageRequest(pageIndex, EVENTS_PER_PAGE);

                    events = eventService.findAllUpcoming(request);
                    pagination.setPageCount(events.getTotalPages());
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
        return events;
    }

    private Node createPage(int pageIndex) {
        pagination.setCurrentPageIndex(pageIndex);
        Page<SimpleEventDTO> events = loadPage(pageIndex);
        ListView<VBox> lvEventElements = new ListView<>();
        lvEventElements.setStyle("-fx-background-color: transparent;");

        if(!events.getContent().isEmpty()){
            for(SimpleEventDTO event : events.getContent()) {
                SpringFxmlLoader.Wrapper<EventElementController> wrapper =
                    springFxmlLoader.loadAndWrap("/fxml/event/eventElement.fxml");
                wrapper.getController().initializeData(eventService,event, bPEventContainer);
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
