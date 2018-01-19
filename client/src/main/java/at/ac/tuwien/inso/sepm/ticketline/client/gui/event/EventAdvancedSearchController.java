package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    Label lbEventPrice;
    @FXML
    Label lbEventDate;
    @FXML
    Label lbEventTime;
    @FXML
    Label lbEventDuration;
    @FXML
    Label lbEventType;
    @FXML
    Label lbEventSeats;

    @FXML
    Label lbLocation;
    @FXML
    Label lbLocationTitle;
    @FXML
    Label lbLocationZip;
    @FXML
    Label lbLocationStreet;
    @FXML
    Label lbLocationCity;
    @FXML
    Label lbLocationCountry;

    @FXML
    Label lbArtist;
    @FXML
    Label lbArtistFName;
    @FXML
    Label lbArtistLName;

    @FXML
    TextField tfEventTitle;
    @FXML
    TextField tfEventDescription;
    @FXML
    TextField tfEventPriceFrom;
    @FXML
    TextField tfEventPriceTo;


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
            eventController.getEventTab().setContent(oldContent);
        }


    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        eventController.getEventTab().setContent(oldContent);
    }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("event.advancedSearch"));
        lbEventTitle.setText(BundleManager.getBundle().getString("events.title"));
        lbEventDescription.setText(BundleManager.getBundle().getString("events.content"));
        lbEventPrice.setText(BundleManager.getBundle().getString("events.price"));
        lbEventDate.setText(BundleManager.getBundle().getString("events.date"));
        lbEventTime.setText(BundleManager.getBundle().getString("events.time"));
        lbEventDuration.setText(BundleManager.getBundle().getString("events.duration"));
        lbEventType.setText(BundleManager.getBundle().getString("events.type"));
        lbEventSeats.setText(BundleManager.getBundle().getString("events.seats"));
        lbLocation.setText(BundleManager.getBundle().getString("location.location"));
        lbLocationTitle.setText(BundleManager.getBundle().getString("events.title"));
        lbLocationZip.setText(BundleManager.getBundle().getString("location.zip"));
        lbLocationStreet.setText(BundleManager.getBundle().getString("location.street"));
        lbLocationCity.setText(BundleManager.getBundle().getString("location.city"));
        lbLocationCountry.setText(BundleManager.getBundle().getString("location.country"));
        lbArtist.setText(BundleManager.getBundle().getString("artist.artist"));
        lbArtistFName.setText(BundleManager.getBundle().getString("artist.fname"));
        lbArtistLName.setText(BundleManager.getBundle().getString("artist.lname"));
    }
}
