package at.ac.tuwien.inso.sepm.ticketline.client.gui.artist;


import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.PaginationHelper;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventSearchFor;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ArtistService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.awt.event.ActionEvent;

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

    @Autowired
    private PaginationHelper paginationHelper;
    @Autowired
    private EventController eventController;

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

    @FXML
    private void loadEvents() {
        paginationHelper.setSearchFor(EventSearchFor.EVENTS_BY_ARTIST);
        paginationHelper.setArtistDTO(simpleArtistDTO);
        paginationHelper.setUpPagination();
        /*
        mainController.setEvent(event);
        mainController.getEventTab().setContent(root);*/

    }

    @Override
    public void update() {
        btShowEvents.setText(BundleManager.getBundle().getString("events.events"));
    }


}

