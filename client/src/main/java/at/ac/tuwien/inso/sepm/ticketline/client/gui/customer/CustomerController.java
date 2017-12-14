package at.ac.tuwien.inso.sepm.ticketline.client.gui.customer;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;

import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
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

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CustomerController extends TabElement implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController.class);


    @FXML
    public Pagination pagination;


    @FXML
    private TabHeaderController tabHeaderController;
    @FXML
    public BorderPane customerOverviewRoot;

    @FXML
    private Button btNew;
    @FXML
    private Button btEdit;
    @FXML
    private Button btSearch;

    @FXML
    private Label lbSearch;
    @FXML
    private Label lbNoMatch;
    @FXML
    private TextField tfSearch;

    private Tab customerTab;
    private int customersPerPage = 3;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final CustomerService customerService;
    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

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
    //    lbNoMatch.setVisible(false);
        initTableView();


    }

    public void preparePagination(){
        //TODO: get customer count for right calculation of the PageCount
        pagination.setPageCount(20);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(new Callback<Integer, Node>() {

            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });
    }

   private List<CustomerDTO> loadPage(int pageIndex){
        List<CustomerDTO> list = new ArrayList<CustomerDTO>();
        try {
            list = customerService.findAll(pageIndex, customersPerPage);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Node createPage(int pageIndex){
       pagination.setCurrentPageIndex(pageIndex);
        List<CustomerDTO> costumers = loadPage(pageIndex);

        TableView<CustomerDTO> tvCustomer = new TableView<>();

        TableColumn<CustomerDTO, String> tcName = new TableColumn<>();
        TableColumn<CustomerDTO, String> tcSurname = new TableColumn<>();
        TableColumn<CustomerDTO, LocalDate> tcBirthdate = new TableColumn<>();
        TableColumn<CustomerDTO, String> tcMail = new TableColumn<>();
        TableColumn<CustomerDTO, String> tcNumber = new TableColumn<>();

        tcName.setText("Name");
        tcSurname.setText("Surname");
        tcBirthdate.setText("Birthdate");
        tcMail.setText("E-Mail");
        tcNumber.setText("Number");

        tcBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        tcMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcNumber.setCellValueFactory(new PropertyValueFactory<>("knr"));

        tvCustomer.getColumns().addAll(tcNumber, tcName, tcSurname, tcBirthdate, tcMail);
        tvCustomer.getItems().addAll(costumers);
        tvCustomer.refresh();

        return tvCustomer;
    }

    private void initTableView() {
        //todo implement me

       /* try {
            ObservableList<CustomerDTO> orders = FXCollections.observableList(customerService.findAll());

         } catch (DataAccessException e) {
           // e.printStackTrace();
        }*/
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
        //todo implement me
 /*       CustomerDTO customer = tvCustomer.getSelectionModel().getSelectedItem();

        if (customer != null) {
            SpringFxmlLoader.Wrapper<CustomerDialogController> wrapper =
                springFxmlLoader.loadAndWrap("/fxml/customer/customerEdit.fxml");
            wrapper.getController().initializeData(new CustomerDTO(), customerOverviewRoot);
            customerTab.setContent(wrapper.getLoadedObject());
        }*/
    }


    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("customer.customer"));
        btSearch.setText(BundleManager.getBundle().getString("menu.search"));
        lbSearch.setText(BundleManager.getBundle().getString("menu.search"));
        tfSearch.setText(BundleManager.getBundle().getString("customer.searchField"));
        lbNoMatch.setText(BundleManager.getBundle().getString("customer.noMatches"));

        //todo update TVcolumns
    }

    @Override
    protected void setTab(Tab tab) {
        customerTab = tab;
    }
}
