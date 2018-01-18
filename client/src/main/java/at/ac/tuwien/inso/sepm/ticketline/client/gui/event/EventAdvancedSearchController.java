package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerDialogController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
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

@Component
public class EventAdvancedSearchController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventAdvancedSearchController.class);

    @FXML
    Label lbEventTitle;
    @FXML
    Label lbEventDescription;
    @FXML
    Label lbEventPriceFrom;
    @FXML
    Label lbEventPriceTo;
    @FXML
    Label lbLocation;
    @FXML
    Label lbArtist;

    @FXML
    TextField tfEventTitle;
    @FXML
    TextField tfEventDescription;
    @FXML
    TextField tfEventPriceFrom;
    @FXML
    TextField tfEventPriceTo;

    //todo

    @FXML
    TextField tfLocationTitle;

    @FXML
    Button btOk;
    @FXML
    Button btCancel;



    @FXML
    private TabHeaderController tabHeaderController;

    @Autowired
    private LocalizationSubject localizationSubject;
    @Autowired
    private PaginationHelper paginationHelper;


    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final EventController eventController;
    private final EventService eventService;
    private final LocationService locationService;
    private Node oldContent;



    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private final int FONT_SIZE = 16;

    public EventAdvancedSearchController(MainController mainController, SpringFxmlLoader springFxmlLoader, EventController eventController, EventService eventService, LocationService locationService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.eventController = eventController;
        this.eventService = eventService;
        this.locationService = locationService;
    }

    @FXML
    void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.FILM);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("event.advancedSearch"));

        localizationSubject.attach(this);

        setButtonGraphic(btOk, "CHECK", Color.OLIVE);
        setButtonGraphic(btCancel, "TIMES", Color.CRIMSON);
    }

    private void setButtonGraphic(Button button, String glyphSymbol, Color color) {
        Glyph glyph = fontAwesome.create(FontAwesome.Glyph.valueOf(glyphSymbol));
        glyph.setColor(color);
        glyph.setFontSize(FONT_SIZE);
        button.setGraphic(glyph);
    }

    void initializeData(Node oldContent) {
        this.oldContent = oldContent;
    }

    @FXML
    public void handleOk(ActionEvent actionEvent) {

        System.out.println("AdvSearch OK........ " );
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        if (!tfEventTitle.getText().isEmpty()) {
            parameters.set("title", tfEventTitle.getText());
        }
        if (!tfEventDescription.getText().isEmpty()) {
            parameters.set("description", tfEventDescription.getText());
        }
        if (!tfEventPriceFrom.getText().isEmpty()) {
            parameters.set("priceFrom", tfEventPriceFrom.getText());
        }
        if (!tfEventPriceTo.getText().isEmpty()) {
            parameters.set("priceTo", tfEventPriceTo.getText());
        }

        //todo task

        Page<SimpleEventDTO> events = null;
        paginationHelper.setSearchFor(EventSearchFor.ALL);
        paginationHelper.setParameters(parameters);
        paginationHelper.setUpPagination();
        eventController.getEventTab().setContent(oldContent);
        }



        /*Pageable request = new PageRequest(0, 7);//todo
        Page<SimpleEventDTO> events = null;
        try {
            events = eventService.findAdvanced(request, parameters);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        paginationHelper.setUpPagination(events);*/

       /* Task<Void> workerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                Pageable request = new PageRequest(0, 7);//todo
                Page<SimpleEventDTO> events = eventService.findAdvanced(request, parameters);
                paginationHelper.setUpPagination(events);
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                eventController.getEventTab().setContent(oldContent);
            }

            @Override
            protected void failed() {

                mainController.showGeneralError(getException().toString());
            }
        };

        workerTask.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        new Thread(workerTask).start();*/


    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        eventController.getEventTab().setContent(oldContent);
    }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("event.advancedSearch"));
    }
}
