package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.EmptyValueException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.TicketAlreadyExistsException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.HBox;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HallplanController implements LocalizationObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket.TicketController.class);

    @Autowired
    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;

    private CustomerController customerController;

    @FXML
    public Label lblError;
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

    Map<Character, Integer> ticketAmountForEachSector;


    @Autowired
    private LocalizationSubject localizationSubject;

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
        localizationSubject.attach(this);
        ticketAmountForEachSector = new HashMap<>();
        char sector= 'a';
        for (int i = 0; i <5 ; i++) {
            ticketAmountForEachSector.put(sector, 0);
            sector++;
        }
    }

    public void initializeData(DetailedEventDTO event, CustomerDTO customer, Node oldContent) {
        this.event = event;
        this.hall = event.getHall();

        this.oldContent = oldContent;


        if(event.getSeatSelection()) {
            initializeSeats();
        }else {
            initializeSectors();
        }
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

        List<SeatDTO> seats = hall.getSeats();
        List<TicketDTO> occupiedTickets = null;
        try {
            occupiedTickets = ticketService.findByEventId(event.getId());
        } catch (DataAccessException e) {
            //TODO: not able to get tickets
            e.printStackTrace();
        }

        List<SeatDTO> occupiedSeats = occupiedTickets ==null?new ArrayList<>(): occupiedTickets.stream().map(ticket -> ticket.getSeat()).collect(Collectors.toList());

        for (SeatDTO seat : seats) {

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
            if (occupiedSeats.contains(seat)) {
                wrapper.getController().vBseat.getStyleClass().add("occupied");
            }
           addSector( wrapper.getController().vBseat, seat.getSector());
        }
    }

    void initializeSectors() {

        List<SeatDTO> seats = hall.getSeats();
        char currentSector = (char) 96;
        for (SeatDTO seat : seats) {
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

        List<TicketDTO> tickets=new ArrayList<>();

        if(!event.getSeatSelection()) {
            selectedSeats.clear();

            //toDo: get number of tickets for sectors!
            int ticketCountSectorA = 0;
            int ticketCountSectorB = 0;
            int ticketCountSectorC = 0;
            int ticketCountSectorD = 1;
            int ticketCountSectorE = 5;

            try {
                List<SeatDTO> freeSeatsSectorA = ticketService.findFreeSeatsForEventInSector(event.getId(), 'a');
                List<SeatDTO> freeSeatsSectorB = ticketService.findFreeSeatsForEventInSector(event.getId(), 'b');
                List<SeatDTO> freeSeatsSectorC = ticketService.findFreeSeatsForEventInSector(event.getId(), 'c');
                List<SeatDTO> freeSeatsSectorD = ticketService.findFreeSeatsForEventInSector(event.getId(), 'd');
                List<SeatDTO> freeSeatsSectorE = ticketService.findFreeSeatsForEventInSector(event.getId(), 'e');

                if(freeSeatsSectorA.size()<ticketCountSectorA ||
                    freeSeatsSectorB.size()<ticketCountSectorB ||
                    freeSeatsSectorC.size()<ticketCountSectorC ||
                    freeSeatsSectorD.size()<ticketCountSectorD ||
                    freeSeatsSectorE.size()<ticketCountSectorE){
                    lblError.setText(BundleManager.getBundle().getString("exception.ticketAlreadyExists"));
                    selectedSeats.clear();

                    if(event.getSeatSelection()){
                        initializeSeats();
                    }else{
                        initializeSectors();
                    }
                    return;
                }
                for(int i=0; i<ticketCountSectorA;i++){
                    selectedSeats.add(freeSeatsSectorA.remove(0));
                }
                for(int i=0; i<ticketCountSectorB;i++){
                    selectedSeats.add(freeSeatsSectorB.remove(0));
                }
                for(int i=0; i<ticketCountSectorC;i++){
                    selectedSeats.add(freeSeatsSectorC.remove(0));
                }
                for(int i=0; i<ticketCountSectorD;i++){
                    selectedSeats.add(freeSeatsSectorD.remove(0));
                }
                for(int i=0; i<ticketCountSectorE;i++){
                    selectedSeats.add(freeSeatsSectorE.remove(0));
                }
            } catch (DataAccessException e) {
                lblError.setText(BundleManager.getBundle().getString("exception.unexpected"));
            }
        }
        for (SeatDTO seat : selectedSeats) {
            tickets.add(new TicketDTO().builder()
                .customer(mainController.getCutsomer())
                .event(new SimpleEventDTO().builder()
                    .endOfEvent(event.getEndOfEvent())
                    .startOfEvent(event.getStartOfEvent())
                    .artists(event.getArtists())
                    .id(event.getId())
                    .price(event.getPrice())
                    .title(event.getTitle())
                    .build())
                .isPaid(false)
                .price(event.getPrice())
                .seat(seat)
                .build());
        }
        try {
            ticketService.save(tickets);

            selectedSeats.clear();
            backToEventTabBeginning();

        } catch (DataAccessException e) {

            lblError.setText(BundleManager.getBundle().getString("exception.unexpected"));

        } catch (TicketAlreadyExistsException e) {

            lblError.setText(BundleManager.getBundle().getString("exception.ticketAlreadyExists"));
            selectedSeats.clear();

            if(event.getSeatSelection()){
                initializeSeats();
            }else{
                initializeSectors();
            }
        } catch (EmptyValueException e) {
            lblError.setText(BundleManager.getBundle().getString("exception.noSeatSelected"));
        }
    }

    @FXML
    public void buyTickets(ActionEvent actionEvent) {
        List<TicketDTO> tickets=new ArrayList<>();

        if(!event.getSeatSelection()) {
            selectedSeats.clear();

            //toDo: get number of tickets for sectors!
            int ticketCountSectorA = 0;
            int ticketCountSectorB = 0;
            int ticketCountSectorC = 0;
            int ticketCountSectorD = 1;
            int ticketCountSectorE = 2;

            try {
                List<SeatDTO> freeSeatsSectorA = ticketService.findFreeSeatsForEventInSector(event.getId(), 'a');
                List<SeatDTO> freeSeatsSectorB = ticketService.findFreeSeatsForEventInSector(event.getId(), 'b');
                List<SeatDTO> freeSeatsSectorC = ticketService.findFreeSeatsForEventInSector(event.getId(), 'c');
                List<SeatDTO> freeSeatsSectorD = ticketService.findFreeSeatsForEventInSector(event.getId(), 'd');
                List<SeatDTO> freeSeatsSectorE = ticketService.findFreeSeatsForEventInSector(event.getId(), 'e');

                if(freeSeatsSectorA.size()<ticketCountSectorA ||
                    freeSeatsSectorB.size()<ticketCountSectorB ||
                    freeSeatsSectorC.size()<ticketCountSectorC ||
                    freeSeatsSectorD.size()<ticketCountSectorD ||
                    freeSeatsSectorE.size()<ticketCountSectorE){
                    lblError.setText(BundleManager.getBundle().getString("exception.ticketAlreadyExists"));
                    selectedSeats.clear();

                    if(event.getSeatSelection()){
                        initializeSeats();
                    }else{
                        initializeSectors();
                    }
                    return;
                }
                for(int i=0; i<ticketCountSectorA;i++){
                    selectedSeats.add(freeSeatsSectorA.remove(0));
                }
                for(int i=0; i<ticketCountSectorB;i++){
                    selectedSeats.add(freeSeatsSectorB.remove(0));
                }
                for(int i=0; i<ticketCountSectorC;i++){
                    selectedSeats.add(freeSeatsSectorC.remove(0));
                }
                for(int i=0; i<ticketCountSectorD;i++){
                    selectedSeats.add(freeSeatsSectorD.remove(0));
                }
                for(int i=0; i<ticketCountSectorE;i++){
                    selectedSeats.add(freeSeatsSectorE.remove(0));
                }
            } catch (DataAccessException e) {
                lblError.setText(BundleManager.getBundle().getString("exception.unexpected"));
            }
        }
        for (SeatDTO seat : selectedSeats) {
            tickets.add(new TicketDTO().builder()
                .customer(mainController.getCutsomer())
                .event(new SimpleEventDTO().builder()
                    .endOfEvent(event.getEndOfEvent())
                    .startOfEvent(event.getStartOfEvent())
                    .artists(event.getArtists())
                    .id(event.getId())
                    .price(event.getPrice())
                    .title(event.getTitle())
                    .build())
                .isPaid(true)
                .price(event.getPrice())
                .seat(seat)
                .build());
        }
        try {
            ticketService.save(tickets);

            selectedSeats.clear();

            backToEventTabBeginning();

        } catch (DataAccessException e) {

            lblError.setText(BundleManager.getBundle().getString("exception.unexpected"));

        } catch (TicketAlreadyExistsException e) {

            lblError.setText(BundleManager.getBundle().getString("exception.ticketAlreadyExists"));
            ticketAmountLb.setText("");
            selectedSeats.clear();

            if(event.getSeatSelection()){
                initializeSeats();
            }else{
                initializeSectors();
            }
        } catch (EmptyValueException e) {
            lblError.setText(BundleManager.getBundle().getString("exception.noSeatSelected"));
        }
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


    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("hallplan.chooseYourTickets"));
        buyBut.setText(BundleManager.getBundle().getString("hallplan.buy"));
        reserveBut.setText(BundleManager.getBundle().getString("hallplan.reserveTickets"));
    }
}
