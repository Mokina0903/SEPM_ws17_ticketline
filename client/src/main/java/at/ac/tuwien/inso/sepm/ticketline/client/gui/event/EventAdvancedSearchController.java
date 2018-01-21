package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@Component
public class EventAdvancedSearchController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventAdvancedSearchController.class);
    public Button dpResetButton;

    @FXML
    private Label lbEventTitle;
    @FXML
    private Label lbEventDescription;
    @FXML
    private Label lbEventPrice;
    @FXML
    private Label lbEventDate;
    @FXML
    private Label lbEventTime;
    @FXML
    private Label lbTimeInfo;
    @FXML
    private Label lbEventDuration;
    @FXML
    private Label lbDurationInfo;
    @FXML
    private Label lbEventType;
    @FXML
    private Label lbEventSeats;

    @FXML
    private Label lbLocation;
    @FXML
    private Label lbLocationTitle;
    @FXML
    private Label lbLocationZip;
    @FXML
    private Label lbLocationStreet;
    @FXML
    private Label lbLocationCity;
    @FXML
    private Label lbLocationCountry;

    @FXML
    private Label lbArtist;
    @FXML
    private Label lbArtistFName;
    @FXML
    private Label lbArtistLName;

    @FXML
    private TextField tfEventTitle;
    @FXML
    private TextField tfEventDescription;
    @FXML
    private TextField tfEventPriceFrom;
    @FXML
    private TextField tfEventPriceTo;


    @FXML
    private TextField tfLocationTitle;

    @FXML
    private Slider slTime = new Slider(0, 24 * 60, 120);
    @FXML
    private Slider slDuration;

    @FXML
    private RadioButton rbSeatsNo;
    @FXML
    private RadioButton rbSeatsYes;
    @FXML
    private RadioButton rbUpcoming;
    @FXML
    private RadioButton rbPast;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ChoiceBox<EventCatgory> cbCategory;

    @FXML
    private Button btOk;
    @FXML
    private Button btCancel;


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

        setUpSlider(slTime, lbTimeInfo);
        setUpSlider(slDuration, lbDurationInfo);
        cbCategory.setItems(FXCollections.observableArrayList( EventCatgory.values()));
        cbCategory.setValue(EventCatgory.All);
        dpDate.setEditable(false);

        rbUpcoming.setSelected(true);
        rbSeatsNo.setSelected(true);
        rbSeatsYes.setSelected(true);

        setButtonGraphic(btOk, "CHECK", Color.OLIVE);
        setButtonGraphic(btCancel, "TIMES", Color.CRIMSON);
        setButtonGraphic(dpResetButton,"TRASH",Color.GRAY);
    }

    private void setUpSlider(Slider slider, Label infoLabel) {
        slider.setMin(0);
        slider.setMax(24 * 60);
        slider.setMajorTickUnit(120);
        slider.setMinorTickCount(0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        StringConverter<Double> stringConverter = new StringConverter<>() {

            @Override
            public String toString(Double object) {
                Integer hours = (object).intValue() / 60;
                return String.format("%02d", hours) + ":00";
            }

            @Override
            public Double fromString(String string) {
                return null;
            }
        };
        slider.setLabelFormatter(stringConverter);
        slider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {

                int runtime = newValue.intValue(); // number of total runtime minutes
                int hours = runtime / 60;
                int minutes = runtime % 60;
                infoLabel.setText(String.format("%02d:%02d", hours, minutes));
                if (infoLabel.getText().matches("24:00")) {
                    infoLabel.setText(infoLabel.getText() + " +");
                }
                if (infoLabel.getText().matches("00:00")) {
                    infoLabel.setText("");
                }
            }
        });
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
        if (slTime.getValue() != 0) {
            Double timeInMinutes = slTime.getValue();
            Long timeInMinutesL = timeInMinutes.longValue();
            parameters.set("timeOfStart", timeInMinutesL.toString());
            System.out.println("looking for time: " + timeInMinutes.intValue());
        }
        if (slDuration.getValue() != 0) {
            Double timeInMinutes = slDuration.getValue();
            Long timeInMinutesL = timeInMinutes.longValue();
            parameters.set("duration", timeInMinutesL.toString());
        }
        if (!rbSeatsNo.isSelected()) {
            parameters.set("noSeats", "toFilter");
        }
        if (!rbSeatsYes.isSelected()) {
            parameters.set("seats", "toFilter");
        }
        if (!rbUpcoming.isSelected()) {
            parameters.set("upcoming", "toFilter");
        }
        if (!rbPast.isSelected()) {
            parameters.set("past", "toFilter");
        }
        if (!dpDate.getEditor().getText().isEmpty()) {
            parameters.set("eventDate", dpDate.getValue().toString());
            System.out.println("Date in AdvSer: " + dpDate.getValue().toString());
        }
        if (!cbCategory.getSelectionModel().getSelectedItem().equals(EventCatgory.All)) {
            parameters.set("category", cbCategory.getSelectionModel().getSelectedItem().toString());
            System.out.println("category " + cbCategory.getSelectionModel().getSelectedItem().toString());
        }



        //type

        //old
        paginationHelper.setSearchFor(EventSearchFor.ALL);
        paginationHelper.setParameters(parameters);
        paginationHelper.setUpPagination();
        eventController.getEventTab().setContent(oldContent);
    }


    public void onDPResetButtonClicked( ActionEvent actionEvent ) {
        dpDate.setValue(null);
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
        rbUpcoming.setId(BundleManager.getBundle().getString("events.upcoming"));
        rbPast.setId(BundleManager.getBundle().getString("events.past"));
        rbSeatsNo.setId(BundleManager.getBundle().getString("events.seatsNo"));
        rbSeatsYes.setId(BundleManager.getBundle().getString("events.seatsYes"));

    }
}

