package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabElement;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventCSVImportController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventController.class);
    @FXML
    public Button btnAddFile;
    @FXML
    public Button btnImport;
    @FXML
    public TextArea TAlogOutput;

    @FXML
    private TabHeaderController tabHeaderController;

    @Autowired
    private LocalizationSubject localizationSubject;

    @FXML
    void initialize(){
        tabHeaderController.setIcon(FontAwesome.Glyph.FILM);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("event.addEvent"));
        localizationSubject.attach(this);
    }

   void EventCSVImportController(){

   }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("event.addEvent"));
        btnAddFile.setText(BundleManager.getBundle().getString(" event.chooseYourInputFile"));
        btnImport.setText(BundleManager.getBundle().getString("event.import"));

    }

}
