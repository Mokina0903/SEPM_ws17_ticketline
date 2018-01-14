package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabElement;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerSearchFor;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketRepresentationClass;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class TicketController extends TabElement implements LocalizationObserver {


    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket.TicketController.class);

    @FXML
    public TextField tfSearch;

    @FXML
    public Pagination pagination;
    @FXML
    public Label lblNoMatch;

    private TableColumn<TicketRepresentationClass, String> tcName;
    private TableColumn<TicketRepresentationClass, String> tcSurname;
    private TableColumn<TicketRepresentationClass, String> tcIsPaid;
    private TableColumn<TicketRepresentationClass, Long> tcNumber;
    private TableColumn<TicketRepresentationClass, String> tcSelected;


    @FXML
    private TabHeaderController tabHeaderController;

    private Tab ticketTab;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final TicketService ticketService;
    private  Page<TicketDTO> ticketPage;
    private TableView<TicketRepresentationClass>currentTableview;
    private TicketSearchFor searchFor = TicketSearchFor.ALL;




    public Tab getTicketTab() {
        return ticketTab;
    }

    public void setTicketTab(Tab ticketTab) {
        this.ticketTab = ticketTab;
    }


    public TicketController(MainController mainController, SpringFxmlLoader springFxmlLoader, TicketService ticketService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.ticketService = ticketService;
    }

    @FXML
     void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.TICKET);
        tabHeaderController.setTitle("Tickets");

    }

    public void initializePagination(){
       try {
           Pageable request = new PageRequest(0, 12);
           ticketPage = ticketService.findAll(request);
           int amount = ticketPage.getTotalPages();
           pagination.setPageCount(amount);
           preparePagination();
       } catch (DataAccessException e) {
           e.printStackTrace();
       } catch (SearchNoMatchException e) {
           LOGGER.debug("There were no search results found when initializeing.");
           noMatchFound();
           e.printStackTrace();
       }
    }

    private void preparePagination() {

        lblNoMatch.setVisible(false);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(new Callback<Integer, Node>() {

            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });
    }
    private Node createPage(int pageIndex) {
        pagination.setCurrentPageIndex(pageIndex);
        TableView<TicketRepresentationClass> tvTickets = new TableView<>();

        Page<TicketDTO> tickets = loadPage(pageIndex);


        tvTickets.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvTickets.setFixedCellSize(25);
        tvTickets.prefHeightProperty().bind(Bindings.size(tvTickets.getItems()).multiply(tvTickets.getFixedCellSize()).add(30));
        tvTickets.getStylesheets().addAll(getClass().getResource("/css/customerComponent.css").toExternalForm());

        tcName = new TableColumn<>();
        tcSurname = new TableColumn<>();
        tcIsPaid = new TableColumn<>();
        tcNumber = new TableColumn<>();
        tcSelected = new TableColumn<>();


        tcName.setText(BundleManager.getBundle().getString("customer.fname"));
        tcSurname.setText(BundleManager.getBundle().getString("customer.lname"));
        tcIsPaid.setText(BundleManager.getBundle().getString("ticket.tcIsPaid"));
        tcNumber.setText(BundleManager.getBundle().getString("ticket.ticketNumber"));
        tcSelected.setText(BundleManager.getBundle().getString("ticket.sector"));

        tcName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tcSurname.setCellValueFactory(new PropertyValueFactory<>("customerSurname"));
        tcNumber.setCellValueFactory(new PropertyValueFactory<>("reservationNumber"));
        tcSelected.setCellValueFactory((cellData -> {
           boolean hasSeatSelection = cellData.getValue().getHasSeatSelection();
            String selection;
            if (hasSeatSelection) {
                selection = cellData.getValue().getSeatNr();
            } else {
                selection = cellData.getValue().getSector();
            }

            return new ReadOnlyStringWrapper(selection);
        }));

        tcIsPaid.setCellValueFactory((cellData -> {
                boolean isPaid = cellData.getValue().isPaid();
                String isPaidAsString;
                if (isPaid) {
                    isPaidAsString = "YES";
                } else {
                    isPaidAsString = "NO";
                }

                return new ReadOnlyStringWrapper(isPaidAsString);
            }));

        tvTickets.getColumns().addAll(tcNumber, tcName, tcSurname, tcSelected, tcIsPaid);
        tvTickets.getItems().addAll(getRepresentationList(tickets.getContent()));
        tvTickets.refresh();

        currentTableview = tvTickets;


        return tvTickets;
    }

    private ArrayList<TicketRepresentationClass> getRepresentationList(List<TicketDTO> tickets){

        ArrayList<TicketRepresentationClass> representedTickets = new ArrayList<>();
        for (TicketDTO ticket : tickets) {
            String eventname = ticket.getEvent().getTitle();
            Long reservationNumber = ticket.getReservationNumber();
            Long ticketID = ticket.getId();
            boolean isPaid = ticket.isPaid();
            String name= ticket.getCustomer().getName();
            String surname = ticket.getCustomer().getSurname();
            String seat = null;
            String sector = null;
            if(ticket.getEvent().getSeatSelection()){
                 seat = "Row "+ticket.getSeat().getRow()+ " Seat "+ ticket.getSeat().getNr();
            } else {
                 sector = String.valueOf(ticket.getSeat().getSector());
            }


            TicketRepresentationClass rep = new TicketRepresentationClass(eventname, reservationNumber, ticketID, isPaid, name, surname, sector, seat, ticket.getEvent().getSeatSelection());
            representedTickets.add(rep);
        }
        return representedTickets;

    }


    private Page<TicketDTO> loadPage(int pageIndex) {

        if (tfSearch.getText().isEmpty() || tfSearch.getText().equals("")) {
            searchFor = TicketSearchFor.ALL;
        }
        Pageable request = new PageRequest(pageIndex, 12);
       try {
            switch (searchFor) {
                case ALL:
                    ticketPage = ticketService.findAll(request);
                    pagination.setPageCount(ticketPage.getTotalPages());
                    break;
                case NAME:
                    ticketPage = ticketService.findByCustomerName(tfSearch.getText(), request);
                    pagination.setPageCount(ticketPage.getTotalPages());
                    break;
                case TICKET_NUMBER:
                    ticketPage = ticketService.findByReservationNumber(Long.valueOf(tfSearch.getText()), request);
                    pagination.setPageCount(ticketPage.getTotalPages());
                    break;
            }

       } catch (SearchNoMatchException e) {
            LOGGER.info("No match found");
            noMatchFound();
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access find customers");
        }

        return ticketPage;
    }

    private void noMatchFound() {
        LOGGER.info("no search match");
        lblNoMatch.setVisible(true);
    }

    @FXML
    private void search(ActionEvent actionEvent) {
        String searchText = tfSearch.getText();


        if (searchText.isEmpty() || searchText.equals("")) {
            LOGGER.info("Empty search");
            searchFor = TicketSearchFor.ALL;
            preparePagination();
        } else {
            try {
                LOGGER.info("Search for Ticket Number");
                long ticketNumber = Long.parseLong(tfSearch.getText());
                searchFor = TicketSearchFor.TICKET_NUMBER;
                preparePagination();
            } catch (NumberFormatException e) {
                LOGGER.info("Search for Customer Name");
                searchFor = TicketSearchFor.NAME;
                preparePagination();
            }
        }

    }

    @Override
    protected void setTab(Tab tab) {
        ticketTab = tab;
    }

    @Override
    public void update() {

        tcName.setText(BundleManager.getBundle().getString("customer.fname"));
        tcSurname.setText(BundleManager.getBundle().getString("customer.lname"));
        tcIsPaid.setText(BundleManager.getBundle().getString("ticket.tcIsPaid"));
        tcNumber.setText(BundleManager.getBundle().getString("ticket.ticketNumber"));
        lblNoMatch.setText(BundleManager.getBundle().getString("customer.noMatches"));
        tcSelected.setText(BundleManager.getBundle().getString("ticket.sector"));


    }
}
