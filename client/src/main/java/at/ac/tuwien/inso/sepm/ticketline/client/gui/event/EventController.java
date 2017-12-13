package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.AbstractSet;

@Component
public class EventController extends TabElement implements LocalizationObserver {


    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventController.class);


    @FXML
    private TabHeaderController tabHeaderController;

    private Tab eventTab;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;

    @Autowired
    private LocalizationSubject localizationSubject;

    public Tab getEventTab() {
        return eventTab;
    }

    public void setEventTab(Tab eventTab) {
        this.eventTab = eventTab;
    }


    public EventController(MainController mainController, SpringFxmlLoader springFxmlLoader) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.FILM);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events"));
        localizationSubject.attach(this);

    }


    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("events.events"));
    }


    @Override
    protected void setTab(Tab tab) {
        eventTab = tab;
    }
}
