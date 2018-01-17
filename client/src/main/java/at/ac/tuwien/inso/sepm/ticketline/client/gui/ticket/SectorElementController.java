package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SectorElementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController.class);

    @FXML
    public HBox hBSector;
    @FXML
    public Label currentReservedTickets;
    @FXML
    public Label maxAnzFromTickets;
    @FXML
    public Button btnIncrease;
    @FXML
    public Button btnDecrease;

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private final int FONT_SIZE = 16;

    private int seatCount;
    private int reservedTickets;
    private int capacity;
    private char sector;

    private Map<Character, Label> ticketAmountForEachSectorLabels;
    private Map<Character, Label> priceOfEachSectorLabels;
    private Map<Character, Double> priceOfEachSector;

    private HallplanController hallplanController;

    void initializeData(int reservedTickets, int capacity, HallplanController hallplanController){
        currentReservedTickets.setText(String.valueOf(reservedTickets));
        maxAnzFromTickets.setText(String .valueOf(capacity));
        this.hallplanController = hallplanController;
        this.seatCount = reservedTickets;
        this.reservedTickets = reservedTickets;
        this.capacity = capacity;
        setButtonGraphic(btnIncrease, "PLUS", Color.DARKGRAY);
        setButtonGraphic(btnDecrease, "MINUS", Color.DARKGRAY);
        ticketAmountForEachSectorLabels = hallplanController.getTicketAmountForEachSectorLabels();
        priceOfEachSector = hallplanController.getPriceOfEachSector();
        priceOfEachSectorLabels = hallplanController.getPriceOfEachSectorLabels();
    }

    private char defineSector(){
        if(hBSector.getStyleClass().contains("sectorA")){
            return 'a';
        }
        if(hBSector.getStyleClass().contains("sectorB")){
            return 'b';
        }
        if(hBSector.getStyleClass().contains("sectorC")){
            return 'c';
        }
        if(hBSector.getStyleClass().contains("sectorD")){
            return 'd';
        }
        if(hBSector.getStyleClass().contains("sectorE")){
            return 'e';
        }

        return 'a';

    }

    private void setButtonGraphic(Button button, String glyphSymbol, Color color) {
        Glyph glyph = fontAwesome.create(FontAwesome.Glyph.valueOf(glyphSymbol));
        glyph.setColor(color);
        glyph.setFontSize(FONT_SIZE);
        button.setGraphic(glyph);
    }

    @FXML
    public void increaseSeatCount(ActionEvent actionEvent) {
        hallplanController.setErrorLblUnvisable();
        sector = defineSector();
        if(seatCount == capacity){
            return;
        }
        seatCount++;
        hallplanController.setTicketCount(hallplanController.getTicketCount()+1);
        hallplanController.ticketAmountLb.setText(String.valueOf(hallplanController.getTicketCount()));
        hallplanController.ticketAmountForEachSector.put(sector, hallplanController.ticketAmountForEachSector.get(sector)+1);
        currentReservedTickets.setText(String.valueOf(seatCount));
        ticketAmountForEachSectorLabels.get(sector).setText(String.valueOf(hallplanController.ticketAmountForEachSector.get(sector)));
        priceOfEachSectorLabels.get(sector).setText(String.format("%.2f", hallplanController.ticketAmountForEachSector.get(sector)*priceOfEachSector.get(sector)));
        hallplanController.setTotalSum(hallplanController.getTotalSum()+priceOfEachSector.get(sector));
        hallplanController.updateTotalSumLbl();

        if( !hBSector.getStyleClass().contains("ticketsAdded")){
            hBSector.getStyleClass().add("ticketsAdded");
        }

    }

    @FXML
    public void decreaseSeatCount(ActionEvent actionEvent) {
        hallplanController.setErrorLblUnvisable();
        sector = defineSector();
        if(seatCount == 0 || seatCount == reservedTickets){
            return;
        }
        seatCount--;
        hallplanController.setTicketCount(hallplanController.getTicketCount()-1);
        hallplanController.ticketAmountLb.setText(String.valueOf(hallplanController.getTicketCount()));
        hallplanController.ticketAmountForEachSector.put(sector, hallplanController.ticketAmountForEachSector.get(sector)-1);
        currentReservedTickets.setText(String.valueOf(seatCount));

        ticketAmountForEachSectorLabels.get(sector).setText(String.valueOf(hallplanController.ticketAmountForEachSector.get(sector)));
        priceOfEachSectorLabels.get(sector).setText(String.format("%.2f", hallplanController.ticketAmountForEachSector.get(sector)*priceOfEachSector.get(sector)));
        hallplanController.setTotalSum(hallplanController.getTotalSum()-priceOfEachSector.get(sector));
        hallplanController.updateTotalSumLbl();

        if(seatCount == reservedTickets && hBSector.getStyleClass().contains("ticketsAdded")){
            hBSector.getStyleClass().remove("ticketsAdded");
        }
    }
}
