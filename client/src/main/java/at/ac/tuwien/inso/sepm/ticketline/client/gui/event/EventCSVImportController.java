package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabElement;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
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
    public Button btnFinish;
    @FXML
    public Label lblChooseFile;

    @FXML
    private TabHeaderController tabHeaderController;

    @Autowired
    private LocalizationSubject localizationSubject;

    private Node oldContent;
    private EventController c;

    @FXML
    void initialize(){
        tabHeaderController.setIcon(FontAwesome.Glyph.FILM);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("event.addEvent"));
        localizationSubject.attach(this);
        lblChooseFile.setFont(Font.font(16));


    }

   void initializeData(EventController c, Node oldContent){
        this.oldContent = oldContent;
       this.c = c;
   }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("event.addEvent"));
        btnAddFile.setText(BundleManager.getBundle().getString("event.chooseYourInputFile"));
        btnImport.setText(BundleManager.getBundle().getString("event.import"));
        btnFinish.setText(BundleManager.getBundle().getString("event.finish"));

    }


    @FXML
    public void goBack(ActionEvent actionEvent) {
       c.getEventTab().setContent(oldContent);
    }

    public void chooseFile(ActionEvent actionEvent) {
       //TODO: open Filepicker and save file
    }

    public void importCSVFile(ActionEvent actionEvent) {
       //TODO: import the file if already choosen
    }
}
