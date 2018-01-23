package at.ac.tuwien.inso.sepm.ticketline.client.gui.location;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tab;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class LocationController extends TabElement implements LocalizationObserver {


    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.location.LocationController.class);

    @FXML
    public Pagination pagination;
    @FXML
    private TabHeaderController tabHeaderController;

    private List<SimpleLocationDTO> locations;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final LocationService locationService;

    private final int LOCATIONS_PER_PAGE = 7;
    private LocationSearchFor searchFor;

    private Tab locationTab;

    @Autowired
    private LocalizationSubject localizationSubject;


    public LocationController(MainController mainController, SpringFxmlLoader springFxmlLoader, LocationService locationService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.locationService = locationService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.MAP_MARKER);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("location.location"));
        localizationSubject.attach(this);

    }

    public void preparePagination() {
        //todo implement Pagination, change list to page
    }


    public void preparePagination(List<SimpleLocationDTO> locations) {

        LOGGER.info("search matches");
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(new Callback<Integer, Node>() {

            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });
    }

    private List<SimpleEventDTO> loadPage(int pageIndex) {
        return null;
    }

    private Node createPage(int pageIndex) {

        return null;
    }

    private void noMatchFound() {
        LOGGER.info("no search match");

    }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("location.location"));
    }


    @Override
    protected void setTab(Tab tab) {
        locationTab = tab;
    }

    public void loadLocations(){

/*        searchFor = LocationSearchFor.EVENT_ADV;
        preparePagination();*/
    }
}
