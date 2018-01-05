package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SeatElementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket.TicketController.class);

    @FXML
    public VBox vBseat;
    @FXML
    public Label seatNrLbl;

    void initializeData(SeatDTO seat){
        seatNrLbl.setText(String.valueOf(seat.getNr()));
    }

}
