package at.ac.tuwien.inso.sepm.ticketline.client.gui.artist;


import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ArtistService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
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
public class ArtistElementController implements LocalizationObserver {

    @FXML
    public Label lbArtistFName;
    @FXML
    public Label lbArtistLName;

    @FXML
    public Button btShowEvents;
    @FXML
    public VBox vbElement;

    private ArtistService artistService;
    private SimpleArtistDTO simpleArtistDTO;
    private MainController mainController;
    private SpringFxmlLoader loader;

    Node myContainer;


    @Autowired
    private LocalizationSubject localizationSubject;

    public ArtistElementController(MainController mainController, SpringFxmlLoader loader) {
        this.mainController = mainController;
        this.loader = loader;
    }

    @FXML
    private void initialize() {
        localizationSubject.attach(this);
    }

    public void initializeData(ArtistService artistService, SimpleArtistDTO simpleArtistDTO, Node myContainer) {

        this.simpleArtistDTO = simpleArtistDTO;
        this.artistService = artistService;
        this.myContainer = myContainer;

        lbArtistFName.setText(simpleArtistDTO.getArtistFirstName());
        lbArtistLName.setText(simpleArtistDTO.getArtistLastName());

    }

    @Override
    public void update() {
        btShowEvents.setText(BundleManager.getBundle().getString("events.events"));
    }


}

