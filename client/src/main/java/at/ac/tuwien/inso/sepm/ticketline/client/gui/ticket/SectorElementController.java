package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
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


    private HallplanController hallplanController;

    void initializeData(int reservedTickets, HallplanController hallplanController){
        currentReservedTickets.setText(String.valueOf(reservedTickets));
        this.hallplanController = hallplanController;

        setButtonGraphic(btnDecrease, "PLUS", Color.DARKGRAY);
        setButtonGraphic(btnIncrease, "MINUS", Color.DARKGRAY);
    }

    private void setButtonGraphic(Button button, String glyphSymbol, Color color) {
        Glyph glyph = fontAwesome.create(FontAwesome.Glyph.valueOf(glyphSymbol));
        glyph.setColor(color);
        glyph.setFontSize(FONT_SIZE);
        button.setGraphic(glyph);
    }

}
