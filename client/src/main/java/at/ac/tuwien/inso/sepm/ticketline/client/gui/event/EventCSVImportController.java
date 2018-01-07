package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventCSVImportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventController.class);
    @FXML
    public Button btnAddFile;
    @FXML
    public Button btnImport;
    @FXML
    public TextArea TAlogOutput;

    @FXML
    private TabHeaderController tabHeaderController;

   void EventCSVImportController(){

   }

}
