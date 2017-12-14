package at.ac.tuwien.inso.sepm.ticketline.client.gui.customer;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerController extends TabElement implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController.class);


    @FXML
    private TabHeaderController tabHeaderController;
    @FXML
    public BorderPane customerOverviewRoot;


/*    @FXML
    private TableView<CustomerDTO> tvCustomer;*/

    private Tab customerTab;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;

    @Autowired
    private LocalizationSubject localizationSubject;

    public Tab getCustomerTab() {
        return customerTab;
    }


    public CustomerController(MainController mainController, SpringFxmlLoader springFxmlLoader) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.USERS);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("customer.customer"));
        localizationSubject.attach(this);
    }


    @FXML
    public void openNewDialog(ActionEvent actionEvent) {

        SpringFxmlLoader.Wrapper<CustomerDialogController> wrapper =

            springFxmlLoader.loadAndWrap("/fxml/customer/customerEdit.fxml");

        wrapper.getController().initializeData(/*null,*/ customerOverviewRoot);

        customerTab.setContent(wrapper.getLoadedObject());
    }

    @FXML
    public void openEditDialog(ActionEvent actionEvent) {

        // CustomerDTO customer = tvCustomer.getSelectionModel();

        //  if(customer != null) {

        SpringFxmlLoader.Wrapper<CustomerDialogController> wrapper =

            springFxmlLoader.loadAndWrap("/fxml/customer/customerEdit.fxml");
        //todo get selected model customer and fill filds
        //   wrapper.getController().initializeData(new CustomerDTO(), userOverviewRoot);

        customerTab.setContent(wrapper.getLoadedObject());
        //   }
    }


    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("customer.customer"));
        //todo update TVcolumns
    }

    @Override
    protected void setTab(Tab tab) {
        customerTab = tab;
    }
}
