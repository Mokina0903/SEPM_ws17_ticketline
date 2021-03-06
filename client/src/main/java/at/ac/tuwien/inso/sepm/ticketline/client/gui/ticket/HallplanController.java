package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.EmptyValueException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.NotEnoughTicketsFreeException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.TicketAlreadyExistsException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.statistics.Top10Controller;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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

    @FXML
    public VBox lblSectorTicketAmountAndPriceOverview;
    @FXML
    public Label lblAmountOfTicketsInA;
    @FXML
    public Label lblPriceOfTicketsInA;
    @FXML
    public Label lblAmountOfTicketsInB;
    @FXML
    public Label lblPriceOfTicketsInB;
    @FXML
    public Label lblAmountOfTicketsInC;
    @FXML
    public Label lblPriceOfTicketsInC;
    @FXML
    public Label lblAmountOfTicketsInD;
    @FXML
    public Label lblPriceOfTicketsInD;
    @FXML
    public Label lblAmountOfTicketsInE;
    @FXML
    public Label lblPriceOfTicketsInE;
    @FXML
    public Label lblTotalSum;
    @FXML
    public HBox sectorALabels;
    @FXML
    public HBox sectorBLabels;
    @FXML
    public HBox sectorCLabels;
    @FXML
    public HBox sectorDLabels;
    @FXML
    public HBox sectorELabels;

    @FXML
    private HallplanCutlineController cutlineContainerController;

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
    private double totalSum;

    @FXML
    private TabHeaderController tabHeaderController;


    private TicketService ticketService;
    private final InvoiceService invoiceService;
    private DetailedEventDTO event;


    private DetailedHallDTO hall;
    private Node oldContent;
    Tab currentTab;

     Map<Character, Integer> ticketAmountForEachSector;
    private Map<Character, Double> priceOfEachSector;
    private Map<Character, Label> ticketAmountForEachSectorLabels;
    private Map<Character, Label> priceOfEachSectorLabels;

    @Autowired
    private LocalizationSubject localizationSubject;

    public HallplanController( MainController mainController, CustomerController customerController, SpringFxmlLoader springFxmlLoader, TicketService ticketService, InvoiceService invoiceService ) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.customerController = customerController;
        this.ticketService = ticketService;
        this.invoiceService = invoiceService;
        selectedSeats = new ArrayList<>();
    }


    Map<Character, Label> getTicketAmountForEachSectorLabels() {
        return ticketAmountForEachSectorLabels;
    }

    Map<Character, Label> getPriceOfEachSectorLabels() {
        return priceOfEachSectorLabels;
    }


    Map<Character, Double> getPriceOfEachSector() {
        return priceOfEachSector;
    }

    Map<Character, Integer> getTicketAmountForEachSector() {
        return ticketAmountForEachSector;
    }

    int getTicketCount() {
        return ticketCount;
    }

    void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    void setErrorLblUnvisable(){
        lblError.setVisible(false);
    }

    void addSelectedSeat(SeatDTO seat) {
        selectedSeats.add(seat);
    }

    void removeSelectedSeat(SeatDTO seat) {
        selectedSeats.remove(seat);
    }

    double getTotalSum() {
        return totalSum;
    }

    void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }

    public VBox getLblSectorTicketAmountAndPriceOverview() {
        return lblSectorTicketAmountAndPriceOverview;
    }

    public void setLblSectorTicketAmountAndPriceOverview(VBox lblSectorTicketAmountAndPriceOverview) {
        this.lblSectorTicketAmountAndPriceOverview = lblSectorTicketAmountAndPriceOverview;
    }

    void updateTotalSumLbl(){
        lblTotalSum.setText(String.format("%.2f", totalSum));
    }

    @FXML
    void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.TICKET);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("hallplan.chooseYourTickets"));
        ticketCount = 0;
        ticketAmountLb.setText(String.valueOf(ticketCount));
        localizationSubject.attach(this);
        ticketAmountForEachSector = new HashMap<>();
        ticketAmountForEachSectorLabels = new HashMap<>();
        priceOfEachSector = new HashMap<>();
        priceOfEachSectorLabels = new HashMap<>();
        totalSum = 0;

        char sector = 'a';
        for (int i = 0; i < 5; i++) {
            ticketAmountForEachSector.put(sector, 0);

            sector++;
        }
        lblError.setWrapText(true);
        lblError.setMaxWidth(100);
        lblError.setMinHeight(50);

        setAllLabelsNotManagedAndNotVisible();
        initializeLabelMaps();
    }

    private void setAllLabelsNotManagedAndNotVisible(){

        sectorALabels.setVisible(false);
        sectorALabels.setManaged(false);
        sectorBLabels.setVisible(false);
        sectorBLabels.setManaged(false);
        sectorCLabels.setVisible(false);
        sectorCLabels.setManaged(false);
        sectorDLabels.setVisible(false);
        sectorDLabels.setManaged(false);
        sectorELabels.setVisible(false);
        sectorELabels.setManaged(false);
    }

    private void initializeLabelMaps() {
        ticketAmountForEachSectorLabels.put('a', lblAmountOfTicketsInA);
        lblAmountOfTicketsInA.setText(String.valueOf(0));
        ticketAmountForEachSectorLabels.put('b', lblAmountOfTicketsInB);
        lblAmountOfTicketsInB.setText(String.valueOf(0));
        ticketAmountForEachSectorLabels.put('c', lblAmountOfTicketsInC);
        lblAmountOfTicketsInC.setText(String.valueOf(0));
        ticketAmountForEachSectorLabels.put('d', lblAmountOfTicketsInD);
        lblAmountOfTicketsInD.setText(String.valueOf(0));
        ticketAmountForEachSectorLabels.put('e', lblAmountOfTicketsInE);
        lblAmountOfTicketsInE.setText(String.valueOf(0));
        priceOfEachSectorLabels.put('a', lblPriceOfTicketsInA);
        priceOfEachSectorLabels.put('b', lblPriceOfTicketsInB);
        priceOfEachSectorLabels.put('c', lblPriceOfTicketsInC);
        priceOfEachSectorLabels.put('d', lblPriceOfTicketsInD);
        priceOfEachSectorLabels.put('e', lblPriceOfTicketsInE);
    }

    public void initializeData(DetailedEventDTO event, CustomerDTO customer, Node oldContent, Tab currentTab) {
        this.event = event;
        this.hall = event.getHall();
        this.currentTab = currentTab;

        this.oldContent = oldContent;

        cutlineContainerController.initializeData(event.getPriceInEuro());

        if (event.getSeatSelection()) {
            initializeSeats();
        } else {
            initializeSectors();
        }
        setButtonGraphic(backbut, "ARROW_LEFT", Color.BLACK);


        lbEventNameHeader.setText(event.getTitle());
        lbEventNameHeader.setFont(Font.font(20));
        lbEventNameHeader.setAlignment(Pos.CENTER);
        lbEventNameHeader.setPadding(new Insets(0, 0, 0, 25));
        lbKnr.setText(customer.getKnr()==null? "xxxxxxxx":String.valueOf(customer.getKnr()));
        lbSurname.setText(customer.getSurname());
        lbVname.setText(customer.getName());
        lblCustomerDescription.setFont(Font.font(16));
        lbVname.setFont(Font.font(14));
        lbSurname.setFont(Font.font(14));
        lbKnr.setFont(Font.font(14));

        priceOfEachSector.put('a', event.getPriceInEuro()*1.0);
        priceOfEachSector.put('b', event.getPriceInEuro()*1.2);
        priceOfEachSector.put('c', event.getPriceInEuro()*1.4);
        priceOfEachSector.put('d', event.getPriceInEuro()*1.6);
        priceOfEachSector.put('e', event.getPriceInEuro()*1.8);

    }

    void initializeSeats() {

        List<SeatDTO> seats = hall.getSeats();
        List<TicketDTO> occupiedTickets = null;
        try {
            occupiedTickets = ticketService.findByEventId(event.getId());
        } catch (DataAccessException e) {
            mainController.showGeneralError("Failed to initialize seats.");
           LOGGER.debug("DataAccessException throwen within method: initializeSeats.");

        }

        List<SeatDTO> occupiedSeats = occupiedTickets == null ? new ArrayList<>() : occupiedTickets.stream().map(ticket -> ticket.getSeat()).collect(Collectors.toList());

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
            addSector(wrapper.getController().vBseat, seat.getSector());
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
            int reservedTickets = 0;
            try {
                reservedTickets = ticketService.ticketCountForEventForSector(event.getId(), sector);
            } catch (DataAccessException e) {
                mainController.showGeneralError("Failed to initialize sectors.");
                LOGGER.debug("DataAccessException throwen within method: initialize sectors.");
            }

            int capacity = hall.getAmountOfSeatsInSector(seat.getSector());
            SpringFxmlLoader.Wrapper<SectorElementController> wrapper =
                springFxmlLoader.loadAndWrap("/fxml/ticket/sectorElement.fxml");
            wrapper.getController().initializeData(reservedTickets, capacity, HallplanController.this);
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

    private void addSector(Node node, char sector) {

        switch (sector) {
            case 'a':
                node.getStyleClass().add("sectorA");
                sectorALabels.setVisible(true);
                sectorALabels.setManaged(true);
                cutlineContainerController.setSectorLegendOfSectorVisableAndManagable('a');
                break;
            case 'b':
                node.getStyleClass().add("sectorB");
                sectorBLabels.setVisible(true);
                sectorBLabels.setManaged(true);
                cutlineContainerController.setSectorLegendOfSectorVisableAndManagable('b');

                break;
            case 'c':
                node.getStyleClass().add("sectorC");
                sectorCLabels.setVisible(true);
                sectorCLabels.setManaged(true);
                cutlineContainerController.setSectorLegendOfSectorVisableAndManagable('c');

                break;
            case 'd':
                node.getStyleClass().add("sectorD");
                sectorDLabels.setVisible(true);
                sectorDLabels.setManaged(true);
                cutlineContainerController.setSectorLegendOfSectorVisableAndManagable('d');

                break;
            case 'e':
                node.getStyleClass().add("sectorE");
                sectorELabels.setVisible(true);
                sectorELabels.setManaged(true);
                cutlineContainerController.setSectorLegendOfSectorVisableAndManagable('e');

                break;
        }
    }


    @FXML
    public void backToEventSelection(ActionEvent actionEvent) {
        mainController.setCutsomer(null);
        currentTab.setContent(oldContent);
    }

    @FXML
    public void reserveTickets(ActionEvent actionEvent) {

        List<TicketDTO> tickets = getTicketsFromSelectedStateAs(false);
        saveTickets(tickets);
    }

    @FXML
    public void buyTickets(ActionEvent actionEvent) {
        List<TicketDTO> tickets = getTicketsFromSelectedStateAs(true);
        saveTickets(tickets);
    }

    private void saveTickets(List<TicketDTO> tickets) {

        try {
            List<TicketDTO> ticketsSaved = ticketService.save(tickets);
            selectedSeats.clear();
            totalSum = 0;

            //create invoice if tickets are bought
            if(tickets.get(0).isPaid()) {
                InvoiceDTO invoice = new InvoiceDTO.InvoiceDTOBuilder()
                    .isStorno(false)
                    .tickets(ticketsSaved)
                    .vendor(mainController.getUser())
                    .customer(mainController.getCutsomer())
                    .build();

                invoice = invoiceService.create(invoice);

                javafx.stage.Window window = this.seatsContainerGV.getParent().getScene().getWindow();
                invoiceService.invoiceToPdf(invoice,window);
            }else{
                javafx.stage.Window window = this.seatsContainerGV.getParent().getScene().getWindow();
                ticketService.reservationPdf(ticketsSaved,window);
            }

            if(tickets.get(0).isPaid()){
                mainController.showGeneralFeedback(BundleManager.getBundle().getString("ticket.feedbackBuy"));
            }else {
                mainController.showGeneralFeedback(BundleManager.getBundle().getString("ticket.feedbackReserve"));
            }
            backToEventTabBeginning();


        } catch (DataAccessException e) {

            lblError.setText(BundleManager.getBundle().getString("exception.unexpected"));
            lblError.setVisible(true);
            lblError.setWrapText(true);

        } catch (TicketAlreadyExistsException e) {

            lblError.setText(BundleManager.getBundle().getString("exception.ticketAlreadyExists"));
            lblError.setVisible(true);
            lblError.setWrapText(true);
            ticketAmountLb.setText("");
            selectedSeats.clear();
            updateHallplan();
        } catch (EmptyValueException e) {
            lblError.setText(BundleManager.getBundle().getString("exception.noSeatSelected"));
            lblError.setWrapText(true);
            lblError.setVisible(true);
        }
    }


    /**
     * @param isPaid defines if tickets will be reserved or buyed
     * @return the list of tickets to be reserved or buyed, from the seats/sectors that are selected
     */
    private List<TicketDTO> getTicketsFromSelectedStateAs(boolean isPaid) {

        List<TicketDTO> tickets = new ArrayList<>();

        if (!event.getSeatSelection()) {

            selectedSeats.clear();
            try {

                for (Map.Entry<Character, Integer> entry : ticketAmountForEachSector.entrySet()) {
                    addSelectedSeatsFromSector(entry.getValue(), entry.getKey());
                }

            } catch (NotEnoughTicketsFreeException e) {
                lblError.setText(BundleManager.getBundle().getString("exception.notEnoughTickets"));
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
                .isPaid(isPaid)
                .price(event.getPrice())
                .seat(seat)
                .build());
        }

        return tickets;
    }

    private void addSelectedSeatsFromSector(Integer anzTickets, char sector) throws NotEnoughTicketsFreeException {
        try {
            List<SeatDTO> freeSeatsFromSector = ticketService.findFreeSeatsForEventInSector(event.getId(), sector);

            if (anzTickets > freeSeatsFromSector.size()) {
                updateHallplan();
                throw new NotEnoughTicketsFreeException(" not enough tickts in sector: " + sector);
            }
            for (int i = 0; i < anzTickets; i++) {
                selectedSeats.add(freeSeatsFromSector.remove(0));
            }
        } catch (DataAccessException e) {
            lblError.setText(BundleManager.getBundle().getString("exception.unexpected"));
            LOGGER.debug("DataAccessException throwen within method: addSelectedSeatsFromSector.");
        }

    }

    private void updateHallplan() {
        if (event.getSeatSelection()) {
            initializeSeats();
        } else {
            initializeSectors();
        }
    }

    private void backToEventTabBeginning() {

        Node root;
        customerController.setNormalTabView();
        if(currentTab ==mainController.getEventTab()){
            SpringFxmlLoader.Wrapper<EventController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/event/eventComponent.fxml");
            root = springFxmlLoader.load("/fxml/event/eventComponent.fxml");
            EventController c = wrapper.getController();
            c.loadEvents();
        }else{
            SpringFxmlLoader.Wrapper<Top10Controller> wrapper =
                springFxmlLoader.loadAndWrap("/fxml/statistics/top10Statistics.fxml");
            root = springFxmlLoader.load("/fxml/statistics/top10Statistics.fxml");
            Top10Controller c = wrapper.getController();
            c.initializeData();
        }

        currentTab.setContent(root);
        mainController.setCutsomer(null);
        mainController.setEvent(null);
    }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("hallplan.chooseYourTickets"));
        buyBut.setText(BundleManager.getBundle().getString("hallplan.buy"));
        reserveBut.setText(BundleManager.getBundle().getString("hallplan.reserveTickets"));
        lblCustomerDescription.setText(BundleManager.getBundle().getString("customer.customerSelected"));
    }

    private void setButtonGraphic(Button button, String glyphSymbol, Color color) {
        Glyph glyph = fontAwesome.create(FontAwesome.Glyph.valueOf(glyphSymbol));
        glyph.setColor(color);
        glyph.setFontSize(FONT_SIZE);
        button.setGraphic(glyph);
    }
}
