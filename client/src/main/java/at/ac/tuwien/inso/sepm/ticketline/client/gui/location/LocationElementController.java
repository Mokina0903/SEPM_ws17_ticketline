package at.ac.tuwien.inso.sepm.ticketline.client.gui.location;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LocationElementController implements LocalizationObserver {

    @FXML
    public Label lbDescription;
    @FXML
    public Label lbStreet;
    @FXML
    public Label lbHouseNr;
    @FXML
    public Label lbCountry;
    @FXML
    public Label lbCity;
    @FXML
    public Label lbZip;

    @FXML
    public Button btShowEvents;
    @FXML
    public VBox vbElement;

    private LocationService locationService;
    private SimpleLocationDTO simpleLocationDTO;
    private MainController mainController;
    private SpringFxmlLoader loader;

    Node myContainer;


    @Autowired
    private LocalizationSubject localizationSubject;

    public LocationElementController(MainController mainController, SpringFxmlLoader loader) {
        this.mainController = mainController;
        this.loader = loader;
    }

    @FXML
    private void initialize() {
        localizationSubject.attach(this);
    }

    public void initializeData(LocationService locationService, SimpleLocationDTO simpleLocationDTO, Node myContainer) {

        this.simpleLocationDTO = simpleLocationDTO;
        this.locationService = locationService;
        this.myContainer = myContainer;

        lbDescription.setText(simpleLocationDTO.getDescription());
        lbCity.setText(simpleLocationDTO.getCity());
        lbCountry.setText(simpleLocationDTO.getCountry());
        lbStreet.setText(simpleLocationDTO.getStreet());
        lbHouseNr.setText(String.valueOf(simpleLocationDTO.getHouseNr()));
        lbZip.setText(String.valueOf(simpleLocationDTO.getZip()));
        }


    @Override
    public void update() {
        btShowEvents.setText(BundleManager.getBundle().getString("events.events"));
    }


}
