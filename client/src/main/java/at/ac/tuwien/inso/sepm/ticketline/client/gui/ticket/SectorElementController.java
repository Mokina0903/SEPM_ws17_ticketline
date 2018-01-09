package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SectorElementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController.class);

    @FXML
    public HBox hBSector;
    @FXML
    public Label currentReservedTickets;
    @FXML
    public Label maxAnzFromTickets;


    private HallplanController hallplanController;

    void initializeData(int reservedTickets, HallplanController hallplanController){
        currentReservedTickets.setText(String.valueOf(reservedTickets));
        this.hallplanController = hallplanController;
    }

}
