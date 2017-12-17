package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
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
    public ListView<VBox> lvEventElements;

    @FXML
    private TabHeaderController tabHeaderController;

    private Tab eventTab;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final EventService eventService;

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
        lvEventElements.getSelectionModel()
            .selectedIndexProperty()
            .addListener((observable, oldvalue, newValue) -> {

                Platform.runLater(() -> {
                    lvEventElements.getSelectionModel().clearSelection();
                });

            });
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
    }
}
