package at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabElement;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TicketController extends TabElement{


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
    private Page<TicketDTO> ticketPage;



    public Tab getTicketTab() {
        return ticketTab;
    }

    public void setTicketTab(Tab ticketTab) {
        this.ticketTab = ticketTab;
    }


    public TicketController(MainController mainController, SpringFxmlLoader springFxmlLoader) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.TICKET);
        tabHeaderController.setTitle("Tickets");
        initializePagination();
    }

    void initializePagination(){

        Pageable request = new PageRequest(0, 15);
        //ticketPage = TicketService.findAll(request); todo: check if findAll does work
        System.out.println("*************************** " + ticketPage.getTotalElements());
        int amount = ticketPage.getTotalPages();
        pagination.setPageCount(amount);
        preparePagination();
    }

    public void preparePagination() {

        LOGGER.info("search matches");

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

        return tvTickets;
    }

    @Override
    protected void setTab(Tab tab) {
        ticketTab = tab;
    }
}
