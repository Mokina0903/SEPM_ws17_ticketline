package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.EmptyValueException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.invoice.InvoiceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketRepresentationClass;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class TicketController extends TabElement implements LocalizationObserver {


    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket.TicketController.class);

    @FXML
    public TextField tfSearch;

    @FXML
    public Pagination pagination;

    @FXML
    public Label lblNoMatch;

    @FXML
    public Button btnStorno;

    @FXML
    public Label lblStorno;

    @FXML
    public Button btnPay;
    @FXML
    public Button btnSuche;

    @FXML
    public Label lblPay;
    @FXML
    public Button btnStornoInvoice;
    @FXML
    public Label lblStornoInvoice;

    private TableColumn<TicketRepresentationClass, String> tcName;
    private TableColumn<TicketRepresentationClass, String> tcSurname;
    private TableColumn<TicketRepresentationClass, String> tcIsPaid;
    private TableColumn<TicketRepresentationClass, Long> tcNumber;
    private TableColumn<TicketRepresentationClass, String> tcSelected;
    private TableColumn<TicketRepresentationClass, String> tcIsDeleted;


    @FXML
    private TabHeaderController tabHeaderController;

    private Tab ticketTab;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final TicketService ticketService;
    private final InvoiceService invoiceService;
    private  Page<TicketDTO> ticketPage;
    private TableView<TicketRepresentationClass>currentTableview;
    private TicketSearchFor searchFor = TicketSearchFor.ALL;

    private InvoiceDTO invoiceTMP;

    @Autowired
    private LocalizationSubject localizationSubject;

    void setErrorLblUnvisable(){
        lblStorno.setVisible(false);
        lblStornoInvoice.setVisible(false);
        lblPay.setVisible(false);
        lblNoMatch.setVisible(false);
    }

    public Tab getTicketTab() {
        return ticketTab;
    }

    public void setTicketTab(Tab ticketTab) {
        this.ticketTab = ticketTab;
    }


    public TicketController( MainController mainController, SpringFxmlLoader springFxmlLoader, TicketService ticketService, InvoiceService invoiceService ) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.ticketService = ticketService;
        this.invoiceService = invoiceService;
    }

    @FXML
     void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.TICKET);
        tabHeaderController.setTitle("Tickets");
        lblStorno.setWrapText(true);
        lblStorno.setMaxWidth(100.0);
        localizationSubject.attach(this);

        localizationSubject.attach(this);
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
        lblStorno.setVisible(false);
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
        tcIsDeleted = new TableColumn<>();


        tcName.setText(BundleManager.getBundle().getString("customer.fname"));
        tcSurname.setText(BundleManager.getBundle().getString("customer.lname"));
        tcIsPaid.setText(BundleManager.getBundle().getString("ticket.tcIsPaid"));
        tcNumber.setText(BundleManager.getBundle().getString("ticket.ticketNumber"));
        tcSelected.setText(BundleManager.getBundle().getString("ticket.sector"));
        tcIsDeleted.setText(BundleManager.getBundle().getString("ticket.isDeleted"));

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

        tcIsDeleted.setCellValueFactory((cellData -> {
            boolean isDeleted = cellData.getValue().getIsDeleted();
            String isDeletedAsString;
            if (isDeleted) {
                isDeletedAsString = "YES";
            } else {
                isDeletedAsString = "NO";
            }

            return new ReadOnlyStringWrapper(isDeletedAsString);
        }));

        tvTickets.getColumns().addAll(tcNumber, tcName, tcSurname, tcSelected, tcIsPaid, tcIsDeleted);

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
            boolean isDeleted = ticket.isDeleted();
            String name= ticket.getCustomer().getName();
            String surname = ticket.getCustomer().getSurname();
            String seat = null;
            String sector = null;
            if(ticket.getEvent().getSeatSelection()){
                 seat = "Row "+ticket.getSeat().getRow()+ " Seat "+ ticket.getSeat().getNr();
            } else {
                 sector = String.valueOf(ticket.getSeat().getSector());
            }


            TicketRepresentationClass rep = new TicketRepresentationClass(eventname, reservationNumber, ticketID, isPaid, isDeleted, name, surname, sector, seat, ticket.getEvent().getSeatSelection());
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

    @FXML
    private void storno(ActionEvent actionEvent){
        lblStorno.setVisible(false);
        if(currentTableview.getSelectionModel() != null) {
            TicketRepresentationClass ticket = currentTableview.getSelectionModel().getSelectedItem();
            if ((ticket == null)) {
                lblStorno.setText(BundleManager.getBundle().getString("ticket.chooseOne"));
                lblStorno.setVisible(true);
                lblStorno.setWrapText(true);
                return;
            }
        }
        else{
            lblStorno.setText(BundleManager.getBundle().getString("ticket.chooseOne"));
            lblStorno.setVisible(true);
            lblStorno.setWrapText(true);
            return;
        }

        Task<Void> workerTask = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                //ToDo fragen wie ich hier die exception abfangen kann
                TicketRepresentationClass ticket = currentTableview.getSelectionModel().getSelectedItem();
                ticketService.deleteTicketByTicket_Id(ticket.getTicket_id());

                if(ticketService.findOneById(ticket.getTicket_id()).isPaid()) {
                    List<InvoiceDTO> invoices = invoiceService.findByReservationNumber(ticket.getReservationNumber());
                    InvoiceDTO invoice = invoices.get(0);

                    List<TicketDTO> tickets = new ArrayList<>();
                    if(invoice.getTickets()!=null && !invoice.getTickets().isEmpty()) {
                        for (TicketDTO ticketDTO : invoice.getTickets()) {
                            if (!ticketDTO.getId().equals(ticket.getTicket_id()) && ticketDTO.getId() != (ticket.getTicket_id())) {
                                tickets.add(ticketDTO);
                            }
                        }
                    }
                    invoice.setTickets(tickets);
                    invoiceService.update(invoice);

                }

                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                lblStorno.setVisible(false);
                lblPay.setVisible(false);
                lblStornoInvoice.setVisible(false);
                mainController.showGeneralFeedback(BundleManager.getBundle().getString("ticket.feedbackStorno"));

            }
            //ToDo update
            @Override
            protected void failed() {
                if (getException().getMessage().trim().equals("424")) {
                    lblStorno.setText(BundleManager.getBundle().getString("ticket.allready"));
                    lblStorno.setVisible(true);
                    lblStorno.setWrapText(true);
                } else if (getException().getMessage().trim().equals("500")) {

                    mainController.showGeneralError("Sorry your ticket could not be found!");
                } else {
                    mainController.showGeneralError(getException().toString());
                }
            }

        };

        workerTask.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        new Thread(workerTask).start();

    }

    @FXML
    private void pay(ActionEvent actionEvent){
        lblPay.setVisible(false);
        if(currentTableview.getSelectionModel() != null) {
            TicketRepresentationClass ticket = currentTableview.getSelectionModel().getSelectedItem();
            if ((ticket == null)) {
                lblPay.setText(BundleManager.getBundle().getString("ticket.chooseOne"));
                lblPay.setVisible(true);
                lblPay.setWrapText(true);
                return;
            }

            try {
                TicketDTO ticketToCheck= ticketService.findOneById(ticket.getTicket_id());
                if(ticketToCheck.isPaid()){
                    lblPay.setText(BundleManager.getBundle().getString("ticket.allreadyPaid"));
                    lblPay.setVisible(true);
                    return;
                }
            } catch (DataAccessException e) {
                lblPay.setText(BundleManager.getBundle().getString("ticket.payNotFound"));
                lblPay.setVisible(true);
            }

        }
        else{
            lblPay.setText(BundleManager.getBundle().getString("ticket.chooseOne"));
            lblPay.setVisible(true);
            lblPay.setWrapText(true);
            return;
        }

        Task<InvoiceDTO> workerTask = new Task<>() {

            @Override
            protected InvoiceDTO call() throws Exception {
                //ToDo fragen wie ich hier die exception abfangen kann
                TicketRepresentationClass ticket = currentTableview.getSelectionModel().getSelectedItem();
                System.out.println(ticket.getReservationNumber());
                ticketService.payTicketByReservation_Id(ticket.getReservationNumber());

                List<TicketDTO> tickets = ticketService
                    .findByReservationNumber(ticket.getReservationNumber(),new PageRequest(0,Integer.MAX_VALUE))
                    .getContent();

                InvoiceDTO invoice = new InvoiceDTO.InvoiceDTOBuilder()
                    .isStorno(false)
                    .tickets(tickets)
                    .vendor(mainController.getUser())
                    .customer(tickets.get(0).getCustomer())
                    .build();


                invoice=invoiceService.create(invoice);
                TicketController.this.invoiceTMP = invoice;
                return invoice;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                lblStorno.setVisible(false);
                lblPay.setVisible(false);
                lblStornoInvoice.setVisible(false);
                mainController.showGeneralFeedback(BundleManager.getBundle().getString("ticket.feedbackBuy"));

            }
            //ToDo update
            @Override
            protected void failed() {
                if (getException().getMessage().trim().equals("424")) {
                    lblPay.setText(BundleManager.getBundle().getString("ticket.allreadyPaid"));
                    lblPay.setVisible(true);
                    lblPay.setWrapText(true);
                } else if (getException().getMessage().trim().equals("500")) {

                    mainController.showGeneralError("Sorry your tickets could not be found!");
                } else {
                    mainController.showGeneralError(getException().toString());
                }
            }

        };

        workerTask.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        Thread thread = new Thread(workerTask);
        thread.start();

       while (thread.isAlive()){
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        javafx.stage.Window window = pagination.getScene().getWindow();
        invoiceService.invoiceToPdf(invoiceTMP,window);

    }

    @Override
    protected void setTab(Tab tab) {
        ticketTab = tab;
    }

    public void createStornoInvoice( ActionEvent actionEvent ) {

        lblStornoInvoice.setVisible(false);
        try {
            TicketRepresentationClass ticket = currentTableview.getSelectionModel().getSelectedItem();

            if(ticket==null){
                lblStornoInvoice.setText(BundleManager.getBundle().getString("ticket.chooseOne"));
                lblStornoInvoice.setVisible(true);
                return;
            }
            TicketDTO ticketToCheck= ticketService.findOneById(ticket.getTicket_id());
            if(!ticketToCheck.isPaid()){
                lblStornoInvoice.setText(BundleManager.getBundle().getString("ticket.stornoInvoiceNotPaid"));
                lblStornoInvoice.setVisible(true);
                return;}

            Page page = ticketService.findByReservationNumber(ticket.getReservationNumber(),new PageRequest(0,Integer.MAX_VALUE));
            List<TicketDTO> tickets = page.getContent();


            if(tickets==null || tickets.isEmpty()){
                lblStornoInvoice.setText(BundleManager.getBundle().getString("ticket.stornoInvoiceNoTickets"));
                lblStornoInvoice.setVisible(true);
                return;}
            List<TicketDTO> stornoTickets = new ArrayList<>();
            for(TicketDTO ticketDTO: tickets){
                if(ticketDTO.isDeleted()){
                    stornoTickets.add(ticketDTO);
                }
            }
            if(stornoTickets.isEmpty()){
                lblStornoInvoice.setText(BundleManager.getBundle().getString("ticket.stornoInvoiceNoTickets"));
                lblStornoInvoice.setVisible(true);
                return;}

            List<InvoiceDTO> invoices = invoiceService.findByReservationNumber(ticket.getReservationNumber());
            for(InvoiceDTO invoice : invoices){
                if(invoice.isStorno()){
                    for (TicketDTO ticketDTO:invoice.getTickets()){
                        if(stornoTickets.contains(ticketDTO)){
                            stornoTickets.remove(ticketDTO);
                        }
                    }
                }
            }

            InvoiceDTO stornoInvoice = InvoiceDTO.builder()
                .isStorno(true)
                .customer(tickets.get(0).getCustomer())
                .tickets(stornoTickets)
                .vendor(mainController.getUser())
                .build();
            stornoInvoice = invoiceService.create(stornoInvoice);

            Window window = btnStorno.getParent().getScene().getWindow();
            invoiceService.invoiceToPdf(stornoInvoice,window);

            lblStorno.setVisible(false);
            lblPay.setVisible(false);
            lblStornoInvoice.setVisible(false);
            mainController.showGeneralFeedback(BundleManager.getBundle().getString("ticket.feedbackStornoInvoice"));


        } catch (DataAccessException e) {
            lblStornoInvoice.setText(BundleManager.getBundle().getString("exception.unexpected"));
            lblStornoInvoice.setVisible(true);
        } catch (SearchNoMatchException | EmptyValueException e) {
            lblStornoInvoice.setText(BundleManager.getBundle().getString("ticket.stornoInvoiceNoTickets"));
            lblStornoInvoice.setVisible(true);
        }

    }

    @Override
    public void update() {
        btnStorno.setText(BundleManager.getBundle().getString("ticket.storno"));
        lblStorno.setVisible(false);
        btnStornoInvoice.setText(BundleManager.getBundle().getString("ticket.stornoInvoice"));
        lblStornoInvoice.setVisible(false);
        tcName.setText(BundleManager.getBundle().getString("customer.fname"));
        tcSurname.setText(BundleManager.getBundle().getString("customer.lname"));
        tcIsPaid.setText(BundleManager.getBundle().getString("ticket.tcIsPaid"));
        tcNumber.setText(BundleManager.getBundle().getString("ticket.ticketNumber"));
        lblNoMatch.setText(BundleManager.getBundle().getString("customer.noMatches"));
        tcSelected.setText(BundleManager.getBundle().getString("ticket.sector"));
        lblPay.setVisible(false);
        btnPay.setText(BundleManager.getBundle().getString("ticket.pay"));
        tcIsDeleted.setText(BundleManager.getBundle().getString("ticket.isDeleted"));
        tfSearch.setPromptText(BundleManager.getBundle().getString("ticket.searchField"));
        btnSuche.setText(BundleManager.getBundle().getString("ticket.search"));
    }
}
