package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabElement;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerSearchFor;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
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
import sun.security.krb5.internal.Ticket;

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

    private TableColumn<TicketDTO, String> tcName;
    private TableColumn<TicketDTO, String> tcSurname;
    private TableColumn<TicketDTO, Boolean> tcIsPaid;
    private TableColumn<TicketDTO, Long> tcNumber;


    @FXML
    private TabHeaderController tabHeaderController;

    private Tab ticketTab;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final TicketService ticketService;
    private  Page<TicketDTO> ticketPage;
    private TableView<TicketDTO>currentTableview;
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
           Pageable request = new PageRequest(0, 15);
           ticketPage = ticketService.findAll(request);
           int amount = ticketPage.getTotalPages();
           pagination.setPageCount(amount);
           preparePagination();
       } catch (DataAccessException e) {
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
        TableView<TicketDTO> tvTickets = new TableView<>();

        Page<TicketDTO> tickets = loadPage(pageIndex);

        tvTickets.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvTickets.setFixedCellSize(25);
        tvTickets.prefHeightProperty().bind(Bindings.size(tvTickets.getItems()).multiply(tvTickets.getFixedCellSize()).add(30));
        tvTickets.getStylesheets().addAll(getClass().getResource("/css/customerComponent.css").toExternalForm());

        tcName = new TableColumn<>();
        tcSurname = new TableColumn<>();
        tcIsPaid = new TableColumn<>();
        tcNumber = new TableColumn<>();

        tcName.setText(BundleManager.getBundle().getString("customer.fname"));
        tcSurname.setText(BundleManager.getBundle().getString("customer.lname"));
        tcIsPaid.setText(BundleManager.getBundle().getString("ticket.tcIsPaid"));
        tcNumber.setText(BundleManager.getBundle().getString("ticket.ticketNumber"));

        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        tcNumber.setCellValueFactory(new PropertyValueFactory<>("reservationNumber"));
        tcIsPaid.setCellValueFactory(new PropertyValueFactory<>("isPaid"));

        tvTickets.getColumns().addAll(tcNumber, tcName, tcSurname, tcIsPaid);
        tvTickets.getItems().addAll(tickets.getContent());
        tvTickets.refresh();
        currentTableview = tvTickets;


        return tvTickets;
    }


    private Page<TicketDTO> loadPage(int pageIndex) {

        if (tfSearch.getText().isEmpty() || tfSearch.getText().equals("")) {
            searchFor = TicketSearchFor.ALL;
        }
        Pageable request = new PageRequest(pageIndex, 15);
       // try { -> remove commentars, when belows todos need exceptions to be catched
            switch (searchFor) {
                case ALL:
                   // ticketPage = TicketService.findAll(request, isPaid); Todo: implement findAllTickets
                    pagination.setPageCount(ticketPage.getTotalPages());
                    break;
                case NAME:
                   // ticketPage = customerService.findByName(tfSearch.getText(), isPaid, request);TODO: implement findByName (Customername)
                    pagination.setPageCount(ticketPage.getTotalPages());
                    break;
                case TICKET_NUMBER:
                    long ticketNumber = Long.parseLong(tfSearch.getText());
                  //  CustomerDTO c = ticketPage.findByNumber(customerNumber); TODO: implemt findByNumber (Reservationnumber)
                    List<TicketDTO> ticketToList = new ArrayList<>();
                   // ticketToList.add(c);
                    ticketPage = new PageImpl<>(ticketToList);
                    pagination.setPageCount(1);
                    break;
            }

       /* } catch (SearchNoMatchException e) {
            LOGGER.info("No match found");
            noMatchFound();
        } //catch (DataAccessException e) {
            LOGGER.warn("Could not access find customers");
        } */

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

    }
}
