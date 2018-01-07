package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SeatElementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket.TicketController.class);

    @FXML
    public VBox vBseat;
    @FXML
    public Label seatNrLbl;

    private SeatDTO seat;

    void initializeData(SeatDTO seat){
        seatNrLbl.setText(String.valueOf(seat.getNr()));
        this.seat = seat;

    }

    @FXML
    public void swichSelectedState(MouseEvent mouseEvent) {

        if(vBseat.getStyleClass().contains("selected")){
            vBseat.getStyleClass().remove("selected");

        } else {
            vBseat.getStyleClass().add("selected");
        }

    }
}
