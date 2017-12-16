package at.ac.tuwien.inso.sepm.ticketline.client.gui.customer;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerController extends TabElement implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController.class);

    @FXML
    private TabHeaderController tabHeaderController;
    @FXML
    public BorderPane customerOverviewRoot;

    @FXML
    public Pagination pagination;

    @FXML
    private Button btNew;
    @FXML
    private Button btEdit;
    @FXML
    private Button btTickets;
    @FXML
    private Button btSearch;

    @FXML
    private Label lbSearch;
    @FXML
    private Label lbNoMatch;
    @FXML
    private TextField tfSearch;

    private TableView<CustomerDTO> currentTableview;

    private TableColumn<CustomerDTO, String> tcName;
    private TableColumn<CustomerDTO, String> tcSurname;
    private TableColumn<CustomerDTO, LocalDate> tcBirthdate;
    private TableColumn<CustomerDTO, String> tcMail;
    private TableColumn<CustomerDTO, String> tcNumber;

    private Tab customerTab;
    private final int CUSTOMER_PER_PAGE = 12;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final CustomerService customerService;
    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

    private List<CustomerDTO> customer;

    private CustomerSearchFor searchFor = CustomerSearchFor.ALL;

    @Autowired
    private LocalizationSubject localizationSubject;
/*    @Autowired
    private CustomerService customerService;*/

    public Tab getCustomerTab() {
        return customerTab;
    }


    public CustomerController(MainController mainController, SpringFxmlLoader springFxmlLoader, CustomerService customerService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.customerService = customerService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.USERS);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("customer.customer"));
        localizationSubject.attach(this);
        btNew.setGraphic(fontAwesome.create("USER_PLUS"));
        btEdit.setGraphic(fontAwesome.create("PENCIL_SQUARE_ALT"));
        btTickets.setGraphic(fontAwesome.create("TICKET"));

        lbNoMatch.setVisible(false);
    }


    public void preparePagination() {
        //all customer at start or searchfield is empty
        try {
            customer = customerService.findAll(0, Integer.MAX_VALUE);
            pagination.setPageCount(customer.size() / CUSTOMER_PER_PAGE + 1);
            preparePagination(customer);
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access total number of customers");
        }
    }

    public void preparePagination(List<CustomerDTO> customer) {

/*        if (customer == null || customer.isEmpty()) {
            noMatchFound();
        } else {*/
        LOGGER.info("search matches");

        lbNoMatch.setVisible(false);
     //   int numOfCustomers = customer.size();
    //    pagination.setPageCount(numOfCustomers / CUSTOMER_PER_PAGE + 1);
        pagination.setCurrentPageIndex(0);
        //      }
        pagination.setPageFactory(new Callback<Integer, Node>() {

            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });
    }

    private List<CustomerDTO> loadPage(int pageIndex) {
        List<CustomerDTO> page = new ArrayList<CustomerDTO>();

        try {
            switch (searchFor) {
                case ALL:
                    customer = customerService.findAll(0, Integer.MAX_VALUE);
                    page = customerService.findAll(pageIndex, CUSTOMER_PER_PAGE);
                    pagination.setPageCount(customer.size() / CUSTOMER_PER_PAGE + 1);
                    break;
                case NAME:
                    customer = customerService.findByName(tfSearch.getText(), 0, Integer.MAX_VALUE);
                    page = customerService.findByName(tfSearch.getText(), pageIndex, CUSTOMER_PER_PAGE);
                    pagination.setPageCount(customer.size() / CUSTOMER_PER_PAGE + 1);

                    break;
                case CUSTOMER_NUMBER:
                    page = customerService.findByNumber(Long.parseLong(tfSearch.getText()));
                    pagination.setPageCount(1);
                    break;
            }

        } catch (SearchNoMatchException e) {
            noMatchFound();
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access find customers");
        }
        return page;
    }

    private Node createPage(int pageIndex) {
        pagination.setCurrentPageIndex(pageIndex);
        List<CustomerDTO> costumers = loadPage(pageIndex);

        TableView<CustomerDTO> tvCustomer = new TableView<>();

        tvCustomer.setFixedCellSize(25);
        tvCustomer.prefHeightProperty().bind(Bindings.size(tvCustomer.getItems()).multiply(tvCustomer.getFixedCellSize()).add(30));

        tcName = new TableColumn<>();
        tcSurname = new TableColumn<>();
        tcBirthdate = new TableColumn<>();
        tcMail = new TableColumn<>();
        tcNumber = new TableColumn<>();

        tcName.setText(BundleManager.getBundle().getString("customer.fname"));
        tcSurname.setText(BundleManager.getBundle().getString("customer.lname"));
        tcBirthdate.setText(BundleManager.getBundle().getString("customer.birthdate"));
        tcNumber.setText(BundleManager.getBundle().getString("customer.number"));
        tcMail.setText("E-Mail");

        tcBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        tcMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcNumber.setCellValueFactory(new PropertyValueFactory<>("knr"));

        tvCustomer.getColumns().addAll(tcNumber, tcName, tcSurname, tcBirthdate, tcMail);
        tvCustomer.getItems().addAll(costumers);
        tvCustomer.refresh();
        currentTableview = tvCustomer;

        return tvCustomer;
    }


    @FXML
    private void search(ActionEvent actionEvent) throws DataAccessException {

        String searchText = tfSearch.getText();

        if (searchText.isEmpty()) {
            LOGGER.info("Empty search");
            searchFor = CustomerSearchFor.ALL;
            preparePagination();
        } else {

            try {
                LOGGER.info("Search for Customer Number");
                long customerNumber = Long.parseLong(tfSearch.getText());
                searchFor = CustomerSearchFor.CUSTOMER_NUMBER;
                preparePagination(customer);
            } catch (NumberFormatException e) {
                LOGGER.info("Search for Customer Name");
                searchFor = CustomerSearchFor.NAME;
                preparePagination(customer);
            }
        }
    }

    private void noMatchFound() {
        LOGGER.info("no search match");
        lbNoMatch.setVisible(true);

        //set empty tv
    }

    @FXML
    public void openNewDialog(ActionEvent actionEvent) {

        SpringFxmlLoader.Wrapper<CustomerDialogController> wrapper =

            springFxmlLoader.loadAndWrap("/fxml/customer/customerEdit.fxml");

        wrapper.getController().initializeData(null, customerOverviewRoot);

        customerTab.setContent(wrapper.getLoadedObject());
    }

    @FXML
    public void openEditDialog(ActionEvent actionEvent) {
   CustomerDTO customer = currentTableview.getSelectionModel().getSelectedItem();

        if (customer != null) {
            SpringFxmlLoader.Wrapper<CustomerDialogController> wrapper =
                springFxmlLoader.loadAndWrap("/fxml/customer/customerEdit.fxml");
            wrapper.getController().initializeData(customer, customerOverviewRoot);
            wrapper.getController().setUpdate(true);
            customerTab.setContent(wrapper.getLoadedObject());
        }
    }


    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("customer.customer"));
        btSearch.setText(BundleManager.getBundle().getString("menu.search"));
        lbSearch.setText(BundleManager.getBundle().getString("menu.search"));
        tfSearch.setText(BundleManager.getBundle().getString("customer.searchField"));
        lbNoMatch.setText(BundleManager.getBundle().getString("customer.noMatches"));

        tcName.setText(BundleManager.getBundle().getString("customer.fname"));
        tcSurname.setText(BundleManager.getBundle().getString("customer.lname"));
        tcBirthdate.setText(BundleManager.getBundle().getString("customer.birthdate"));
        tcNumber.setText(BundleManager.getBundle().getString("customer.number"));
        //todo update TVcolumns
    }

    @Override
    protected void setTab(Tab tab) {
        customerTab = tab;
    }
}
