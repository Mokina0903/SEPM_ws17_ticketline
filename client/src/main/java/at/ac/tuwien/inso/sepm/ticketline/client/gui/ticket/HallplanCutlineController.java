package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
    @FXML
    public HBox sectorELegend;
    @FXML
    public HBox sectorDLegend;
    @FXML
    public HBox sectorCLegend;
    @FXML
    public HBox sectorBLegend;
    @FXML
    public HBox sectorALegend;

    void initializeData(double sectorPriceA){

        lbPriceA.setText(String.format("%10.2f",sectorPriceA));
        lbPriceB.setText(String.format("%10.2f",sectorPriceA*1.2));
        lbPriceC.setText(String.format("%10.2f",sectorPriceA*1.4));
        lbPriceD.setText(String.format("%10.2f",sectorPriceA*1.6));
        lbPriceE.setText(String.format("%10.2f",sectorPriceA*1.8));
        setAllLabelsNotManagedAndNotVisible();
    }

    private void setAllLabelsNotManagedAndNotVisible(){

        sectorALegend.setVisible(false);
        sectorALegend.setManaged(false);
        sectorBLegend.setVisible(false);
        sectorBLegend.setManaged(false);
        sectorCLegend.setVisible(false);
        sectorCLegend.setManaged(false);
        sectorDLegend.setVisible(false);
        sectorDLegend.setManaged(false);
        sectorELegend.setVisible(false);
        sectorELegend.setManaged(false);

    }

    public void setSectorLegendOfSectorVisableAndManagable( char sector) {

        switch (sector) {
            case 'a':
                sectorALegend.setVisible(true);
                sectorALegend.setManaged(true);
                break;
            case 'b':
                sectorBLegend.setVisible(true);
                sectorBLegend.setManaged(true);
                break;
            case 'c':
                sectorCLegend.setVisible(true);
                sectorCLegend.setManaged(true);
                break;
            case 'd':
                sectorDLegend.setVisible(true);
                sectorDLegend.setManaged(true);
                break;
            case 'e':
                sectorELegend.setVisible(true);
                sectorELegend.setManaged(true);
                break;
        }
    }


}
