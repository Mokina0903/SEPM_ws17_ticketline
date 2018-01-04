package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabElement;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.shape.Arc;
import net.bytebuddy.asm.Advice;
import org.controlsfx.control.GridView;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class HallplanController extends TabElement {
    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket.TicketController.class);

    @FXML
    public GridView seatsContainerGV;

    @FXML
    private TabHeaderController tabHeaderController;
    @FXML
    public Arc sceneArc;

    private Tab ticketTab;

    private DetailedHallDTO hall;

    @FXML
    void initialize(){
        tabHeaderController.setIcon(FontAwesome.Glyph.TICKET);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("hallplan.chooseYourTickets"));
        initializeSeats();
    }

   void initializeSeats(){

        List<SeatDTO> seats = hall.getSeats();
        int rowCount = seats.get(seats.size()-1).getRow();
        int seatsPerRow = seats.size()%2==0?(seats.size()/rowCount):(seats.size()/rowCount)+1;
       for (SeatDTO seat :
           seats) {
           //Stelle seats dar
       }
    }

    @Override
    protected void setTab(Tab tab) {
        ticketTab = tab;
    }
}
