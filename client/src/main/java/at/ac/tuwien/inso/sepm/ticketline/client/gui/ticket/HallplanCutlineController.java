package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HallplanCutlineController {
    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket.TicketController.class);

    @FXML
    public Label lbPriceA;
    @FXML
    public Label lbPriceB;
    @FXML
    public Label lbPriceC;
    @FXML
    public Label lbPriceD;
    @FXML
    public Label lbPriceE;

    void initializeData(double sectorPriceA){

        lbPriceA.setText(String.format("%10.0f",sectorPriceA));
        lbPriceB.setText(String.format("%10.0f",sectorPriceA*1.2));
        lbPriceC.setText(String.format("%10.0f",sectorPriceA*1.4));
        lbPriceD.setText(String.format("%10.0f",sectorPriceA*1.6));
        lbPriceE.setText(String.format("%10.0f",sectorPriceA*1.8));
    }


}
