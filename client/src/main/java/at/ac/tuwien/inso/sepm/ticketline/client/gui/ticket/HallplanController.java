package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabElement;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import net.bytebuddy.asm.Advice;
import org.controlsfx.control.GridView;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class HallplanController extends TabElement {
    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket.TicketController.class);
    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;

    @FXML
    public GridPane seatsContainerGV;
    @FXML
    public Label ticketAmountLb;
    @FXML
    public Button reserveBut;
    @FXML
    public Button buyBut;

    @FXML
    private TabHeaderController tabHeaderController;
    @FXML
    public Arc sceneArc;

    private Tab ticketTab;

  //  private DetailedHallDTO hall;

    public HallplanController(MainController mainController, SpringFxmlLoader springFxmlLoader) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    @FXML
    void initialize(){
        tabHeaderController.setIcon(FontAwesome.Glyph.TICKET);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("hallplan.chooseYourTickets"));
        initializeSeats();
    }

   void initializeSeats(){

     //*************** Preperation for testing ***************
        DetailedHallDTO hall = new DetailedHallDTO();
        ArrayList<SeatDTO> seatsToAdd = new ArrayList<>();
        int row = 1;
        int nr = 1;
       for (int i = 0; i < 74 ; i++) {
           SeatDTO seat = new SeatDTO();
           seat.setNr(nr);
           seat.setRow(row);
           seat.setSector('a');
           seatsToAdd.add(seat);
           if(nr == 10){
               nr = 0;
               row ++;
           }
           nr++;
       }
        hall.setSeats(seatsToAdd);
       //*************** END  ***************

        List<SeatDTO> seats = hall.getSeats();
        int rowCount = seats.get(seats.size()-1).getRow();
        int seatsPerRow = seats.size()%2==0?(seats.size()/rowCount):(seats.size()/rowCount)+1;
        seatsPerRow =10;

       for (SeatDTO seat : seats) {
           //show seats
           Rectangle r = new Rectangle(100, 150);
           SpringFxmlLoader.Wrapper<SeatElementController> wrapper =
               springFxmlLoader.loadAndWrap("/fxml/ticket/seatElement.fxml");
           wrapper.getController().initializeData(seat);
           r.setFill(Color.GRAY);
           seatsContainerGV.add(wrapper.getLoadedObject(), seat.getNr(), seat.getRow());
           if(seat.getNr() == 1){
               Label label = new Label();
               label.setText(String.valueOf(seat.getRow()));
               label.setFont(Font.font(16));
               label.setAlignment(Pos.CENTER);
               label.setPadding(new Insets(0,0,0,5));
               seatsContainerGV.add(label, 0, seat.getRow());
           }

       }
    }

    @Override
    protected void setTab(Tab tab) {
        ticketTab = tab;
    }
}
