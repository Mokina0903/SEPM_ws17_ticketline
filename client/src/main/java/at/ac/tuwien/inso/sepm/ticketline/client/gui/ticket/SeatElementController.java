package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class SeatElementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket.TicketController.class);

    @FXML
    public VBox vBseat;
    @FXML
    public Label seatNrLbl;


    private SeatDTO seat;
    private HallplanController hallplanController;
    private char sector;
    private Map<Character, Label> ticketAmountForEachSectorLabels;
    private Map<Character, Label> priceOfEachSectorLabels;
    private Map<Character, Double> priceOfEachSector;
    private Map<Character, Integer> ticketAmountForEachSector;

    void initializeData(SeatDTO seat, HallplanController hallplanController){
        seatNrLbl.setText(String.valueOf(seat.getNr()));
        this.seat = seat;
        this.hallplanController = hallplanController;
        sector = seat.getSector();
        ticketAmountForEachSectorLabels = hallplanController.getTicketAmountForEachSectorLabels();
        priceOfEachSectorLabels = hallplanController.getPriceOfEachSectorLabels();
        priceOfEachSector = hallplanController.getPriceOfEachSector();
        ticketAmountForEachSector = hallplanController.getTicketAmountForEachSector();
    }

    @FXML
    public void swichSelectedState(MouseEvent mouseEvent) {

        if(vBseat.getStyleClass().contains("selected")){
            vBseat.getStyleClass().remove("selected");
            hallplanController.setTicketCount(hallplanController.getTicketCount()-1);
            hallplanController.ticketAmountForEachSector.put(sector, hallplanController.ticketAmountForEachSector.get(sector)-1);
            hallplanController.ticketAmountLb.setText(String.valueOf(hallplanController.getTicketCount()));
            hallplanController.removeSelectedSeat(seat);
            ticketAmountForEachSectorLabels.get(sector).setText(String.valueOf(hallplanController.ticketAmountForEachSector.get(sector)));
            priceOfEachSectorLabels.get(sector).setText(String.format("%.2f", hallplanController.ticketAmountForEachSector.get(sector)*priceOfEachSector.get(sector)));

        } else if((!vBseat.getStyleClass().contains("selected"))&&(!vBseat.getStyleClass().contains("occupied"))) {

            vBseat.getStyleClass().add("selected");
            hallplanController.setTicketCount(hallplanController.getTicketCount()+1);
            hallplanController.ticketAmountForEachSector.put(sector, hallplanController.ticketAmountForEachSector.get(sector)+1);
            hallplanController.ticketAmountLb.setText(String.valueOf(hallplanController.getTicketCount()));
            hallplanController.addSelectedSeat(seat);

            ticketAmountForEachSectorLabels.get(sector).setText(String.valueOf(hallplanController.ticketAmountForEachSector.get(sector)));
            priceOfEachSectorLabels.get(sector).setText(String.format("%.2f",hallplanController.ticketAmountForEachSector.get(sector)*priceOfEachSector.get(sector)));

        }

    }

}
