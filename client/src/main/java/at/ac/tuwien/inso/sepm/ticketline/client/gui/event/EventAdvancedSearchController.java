package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerDialogController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventAdvancedSearchController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventAdvancedSearchController.class);

    @FXML
    Label lbEventTitle;
    @FXML
    Label lbArtist;
    @FXML
    Label lbEventPrice;
    @FXML
    Label lbLocation;
    @FXML
    TextField tfEventTitle;
    @FXML
    TextField tfLocationTitle;


    @FXML
    private TabHeaderController tabHeaderController;

    @Autowired
    private LocalizationSubject localizationSubject;

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
        localizationSubject.attach(this);

    }

    void initializeData(Node oldContent) {
        this.oldContent = oldContent;
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        eventController.getEventTab().setContent(oldContent);
    }

    @Override
    public void update() {

    }
}
