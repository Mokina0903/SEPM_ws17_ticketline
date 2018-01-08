package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabElement;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class HallplanController {
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
    public Label lbKnr;
    @FXML
    public Label lbVname;
    @FXML
    public Label lbSurname;
    @FXML
    public Label lblCustomerDescription;
    @FXML
    public Button backbut;
    @FXML
    public Label lbEventNameHeader;

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private final int FONT_SIZE = 16;

    private int ticketCount;

    @FXML
    private TabHeaderController tabHeaderController;


    private TicketService ticketService;
    private DetailedEventDTO event;
    private CustomerDTO customer;

    private DetailedHallDTO hall;
    private Node oldContent;

    public HallplanController(MainController mainController, SpringFxmlLoader springFxmlLoader, TicketService ticketService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.ticketService = ticketService;
    }


    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    @FXML
    void initialize(){
        tabHeaderController.setIcon(FontAwesome.Glyph.TICKET);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("hallplan.chooseYourTickets"));
        ticketCount = 0;

    }

    public void initializeData(DetailedEventDTO event, CustomerDTO customer, Node oldContent){
        this.event = event;
        this.hall = event.getHall();
        this.customer = customer;
        this.oldContent = oldContent;

        initializeSeats();

        setButtonGraphic( backbut, "TIMES", Color.DARKGRAY);

        lbEventNameHeader.setText(event.getTitle());
        lbKnr.setText(String.valueOf(customer.getKnr()));
        lbSurname.setText(customer.getSurname());
        lbVname.setText(customer.getName());
        lblCustomerDescription.setFont(Font.font(16));
        lbVname.setFont(Font.font(14));
        lbSurname.setFont(Font.font(14));
        lbKnr.setFont(Font.font(14));

    }

   void initializeSeats(){

     //* ************** Preperation for testing ***************
        DetailedHallDTO hall = new DetailedHallDTO();
        ArrayList<SeatDTO> seatsToAdd = new ArrayList<>();
        int row = 1;
        int nr = 1;
        char sector = 'a';
       for (int i = 0; i < 74 ; i++){
           SeatDTO seat = new SeatDTO();
           seat.setNr(nr);
           seat.setRow(row);
           seat.setSector(sector);
           seatsToAdd.add(seat);
           if(nr == 10){
               nr = 0;
               row ++;
               if(row%2==0){
                   sector++;
               }
           }
           nr++;
       }
        hall.setSeats(seatsToAdd);
       //*************** END  *************** */

        List<SeatDTO> seats = hall.getSeats();
       List<TicketDTO> occupiedTickets = null;
       try {
           occupiedTickets = ticketService.findByEventId(event.getId());
       } catch (DataAccessException e) {
         //TODO: add ticktes already reserverd lbl/warning
           e.printStackTrace();
       }
       List<SeatDTO> occupiedSeats = new ArrayList<>();
       if(occupiedTickets != null){
           for (TicketDTO ticket : occupiedTickets) {
               occupiedSeats.add(ticket.getSeat());
           }
       }

       for (SeatDTO seat : seats) {
           //show seats

           SpringFxmlLoader.Wrapper<SeatElementController> wrapper =
               springFxmlLoader.loadAndWrap("/fxml/ticket/seatElement.fxml");
           wrapper.getController().initializeData(seat, HallplanController.this);
           seatsContainerGV.add(wrapper.getController().vBseat, seat.getNr(), seat.getRow());
           if(seat.getNr() == 1){
               Label label = new Label();
               label.setText(String.valueOf(seat.getRow()));
               label.setFont(Font.font(16));
               label.setAlignment(Pos.CENTER);
               label.setPadding(new Insets(0,0,0,5));
               seatsContainerGV.add(label, 0, seat.getRow());
           }
           //******* for testting ************
           if(seat.getNr() == 1){
               wrapper.getController().vBseat.getStyleClass().add("occupied");
           }
           //********* end ******************

/*
           if(!occupiedSeats.isEmpty()) {
               if (occupiedSeats.contains(seat)) {
                   wrapper.getController().vBseat.getStyleClass().add("occupied");
               }
           } */

           char seatSector = seat.getSector();
           switch (seatSector){
               case 'a': wrapper.getController().vBseat.getStyleClass().add("sectorA");
               break;
               case 'b': wrapper.getController().vBseat.getStyleClass().add("sectorB");
               break;
               case 'c': wrapper.getController().vBseat.getStyleClass().add("sectorC");
               break;
               case 'd': wrapper.getController().vBseat.getStyleClass().add("sectorD");
               break;
               case 'e': wrapper.getController().vBseat.getStyleClass().add("sectorE");

           }

       }
    }

    private void setButtonGraphic(Button button, String glyphSymbol, Color color) {
        Glyph glyph = fontAwesome.create(FontAwesome.Glyph.valueOf(glyphSymbol));
        glyph.setColor(color);
        glyph.setFontSize(FONT_SIZE);
        button.setGraphic(glyph);
    }


    @FXML
    public void backToEventSelection(ActionEvent actionEvent) {
        mainController.getEventTab().setContent(oldContent);
    }
}
