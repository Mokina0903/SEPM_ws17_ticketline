package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabElement;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventController;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;

@Component
public class HallplanController {
    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket.TicketController.class);

    @Autowired
    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private CustomerController customerController;

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
    private ArrayList<SeatDTO> selectedSeats;

    @FXML
    private TabHeaderController tabHeaderController;


    private TicketService ticketService;
    private DetailedEventDTO event;


    private DetailedHallDTO hall;
    private Node oldContent;

    public HallplanController(MainController mainController, CustomerController customerController, SpringFxmlLoader springFxmlLoader, TicketService ticketService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.customerController = customerController;
        this.ticketService = ticketService;
        selectedSeats = new ArrayList<>();
    }


    int getTicketCount() {
        return ticketCount;
    }

    void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    void addSelectedSeat(SeatDTO seat) {
        selectedSeats.add(seat);
    }

    void removeSelectedSeat(SeatDTO seat) {
        selectedSeats.remove(seat);
    }

    @FXML
    void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.TICKET);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("hallplan.chooseYourTickets"));
        ticketCount = 0;
        ticketAmountLb.setText(String.valueOf(ticketCount));
    }

    public void initializeData(DetailedEventDTO event, CustomerDTO customer, Node oldContent) {
        this.event = event;
        this.hall = event.getHall();

        this.oldContent = oldContent;

        //TODO: when implemented, create if,  depending on the event.IsSectorView - then initializeSectors, else initializeSeats
        initializeSeats();
        //initializeSectors();

        setButtonGraphic(backbut, "ARROW_LEFT", Color.DARKGRAY);

        lbEventNameHeader.setText(event.getTitle());
        lbEventNameHeader.setFont(Font.font(20));
        lbEventNameHeader.setAlignment(Pos.CENTER);
        lbEventNameHeader.setPadding(new Insets(0, 0, 0, 25));
        lbKnr.setText(String.valueOf(customer.getKnr()));
        lbSurname.setText(customer.getSurname());
        lbVname.setText(customer.getName());
        lblCustomerDescription.setFont(Font.font(16));
        lbVname.setFont(Font.font(14));
        lbSurname.setFont(Font.font(14));
        lbKnr.setFont(Font.font(14));

    }

    void initializeSeats() {

        /* ************** Preperation for testing ***************
        DetailedHallDTO hall = new DetailedHallDTO();
        ArrayList<SeatDTO> seatsToAdd = new ArrayList<>();
        int row = 1;
        int nr = 1;
        char sector = 'a';
        for (int i = 0; i < 74; i++) {
            SeatDTO seat = new SeatDTO();
            seat.setNr(nr);
            seat.setRow(row);
            seat.setSector(sector);
            seatsToAdd.add(seat);
            if (nr == 10) {
                nr = 0;
                row++;
                if (row % 2 == 0) {
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
        if (occupiedTickets != null) {
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
            if (seat.getNr() == 1) {
                Label label = new Label();
                label.setText(String.valueOf(seat.getRow()));
                label.setFont(Font.font(16));
                label.setAlignment(Pos.CENTER);
                label.setPadding(new Insets(0, 2, 0, 5));
                seatsContainerGV.add(label, 0, seat.getRow());
            }

           if(!occupiedSeats.isEmpty()) {
               if (occupiedSeats.contains(seat)) {
                   wrapper.getController().vBseat.getStyleClass().add("occupied");
               }
           }
           addSector( wrapper.getController().vBseat, seat.getSector());
        }
    }

    void initializeSectors() {

        /* ************** Preperation for testing ***************
        DetailedHallDTO hall = new DetailedHallDTO();
        ArrayList<SeatDTO> seatsToAdd = new ArrayList<>();
        int row = 1;
        int nr = 1;
        char sector1 = 'a';
        for (int i = 0; i < 74; i++) {
            SeatDTO seat = new SeatDTO();
            seat.setNr(nr);
            seat.setRow(row);
            seat.setSector(sector1);
            seatsToAdd.add(seat);
            if (nr == 10) {
                nr = 0;
                row++;
                if (row % 2 == 0) {
                    sector1++;
                }
            }
            nr++;
        }
        hall.setSeats(seatsToAdd);
        //*************** END  *************** */

        List<SeatDTO> seats = hall.getSeats();

        char currentSector = (char) 96;

        for (SeatDTO seat : seats) {
            //show sectors

            char sector = seat.getSector();
            if (currentSector == sector) {
                continue;
            }

            int reservedTickets =0;

            try {
                reservedTickets = ticketService.ticketCountForEventForSector(event.getId(), sector);
            } catch (DataAccessException e) {
                //TODO: Add alert
                e.printStackTrace();
            }

            SpringFxmlLoader.Wrapper<SectorElementController> wrapper =
                springFxmlLoader.loadAndWrap("/fxml/ticket/sectorElement.fxml");
            wrapper.getController().initializeData(reservedTickets, 20,HallplanController.this);
            seatsContainerGV.add(wrapper.getController().hBSector, seat.getNr(), seat.getRow());
            if (seat.getNr() == 1) {
                Label label = new Label();
                String s = String.valueOf(seat.getSector());
                label.setText(s.toUpperCase());
                label.setFont(Font.font(14));
                label.setAlignment(Pos.CENTER);
                label.setPadding(new Insets(0, 5, 0, 0));
                seatsContainerGV.add(label, 0, seat.getRow());
            }
            addSector(wrapper.getController().hBSector, sector);
            currentSector++;
        }
    }

    private void addSector(Node node, char sector){

        switch (sector) {
            case 'a':
                node.getStyleClass().add("sectorA");
                break;
            case 'b':
               node.getStyleClass().add("sectorB");
                break;
            case 'c':
                node.getStyleClass().add("sectorC");
                break;
            case 'd':
                node.getStyleClass().add("sectorD");
                break;
            case 'e':
                node.getStyleClass().add("sectorE");

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

    @FXML
    public void reserveTickets(ActionEvent actionEvent) {
        //TODO: reserve/create Tickets from the seats within selectedSeats

      backToEventTabBeginning();
    }

    @FXML
    public void buyTickets(ActionEvent actionEvent) {
        //TODO: buy/create Tickets from the seats within selectedSeats

      backToEventTabBeginning();
    }

    private void backToEventTabBeginning(){


        customerController.setNormalTabView();
        SpringFxmlLoader.Wrapper<EventController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/event/eventComponent.fxml");
        Node root = springFxmlLoader.load("/fxml/event/eventComponent.fxml");

        EventController c = wrapper.getController();

        c.loadEvents();
        mainController.getEventTab().setContent(root);
        mainController.setCutsomer(null);
        mainController.setEvent(null);
    }
}
